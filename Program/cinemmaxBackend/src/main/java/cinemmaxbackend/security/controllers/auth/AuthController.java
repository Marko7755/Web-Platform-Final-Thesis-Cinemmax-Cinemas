package cinemmaxbackend.security.controllers.auth;

import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateEmailException;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateUserException;
import cinemmaxbackend.security.classes.commands.register.RegisterCommand;
import cinemmaxbackend.security.classes.commands.login.LoginCommand;
import cinemmaxbackend.security.classes.dto.JwtResponseDTO;
import cinemmaxbackend.security.classes.dto.UserDTO;
import cinemmaxbackend.security.classes.enums.Role;
import cinemmaxbackend.security.classes.general.tokens.RefreshToken;
import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.repositories.tokens.RefreshTokenRepository;
import cinemmaxbackend.security.repositories.UserRepository;
import cinemmaxbackend.security.services.tokens.JwtService;
import cinemmaxbackend.security.services.tokens.RefreshTokenService;
import cinemmaxbackend.security.services.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/authenticate")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;

    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private UserService userService;


    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody @Valid LoginCommand authRequestDTO,
                                                  HttpServletResponse response) {
        //UsernamePasswordAuthenticationToken sends username and password to DaoAuthenticationProvider authProvider from
        //SecurityConf and then tries to find that User in DB

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Username or password is incorrect");
        }

        if(authentication.isAuthenticated()) {
            User user = userRepository.findByUsernameIgnoreCase(authRequestDTO.getUsername()).orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                    .httpOnly(true)
                    .secure(false) // For local dev; change to true in prod
                    .path("/") // Send this cookie on all routes
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Lax") // Good default for dev/prod balance
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(user.getId(), user.getUsername(), String.valueOf(user.getRole())))
                    //.refreshToken(refreshToken.getToken())
                    .build();
        }
        else {
            throw new BadCredentialsException("Username or password is incorrect");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterCommand registerCommand) {
        Map<String, String> response = new HashMap<>();

        if(userService.checkDuplicate(registerCommand.getUsername())) {
            /*response.put("message", "Username is already in use");
            return ResponseEntity.badRequest().body(response);*/
            throw new DuplicateUserException("Username " + registerCommand.getUsername() + " is already in use");
        }

        if(userService.checkDuplicateEmail(registerCommand.getEmail())) {
            throw new DuplicateEmailException("Email " + registerCommand.getEmail() + " is already in use");
        }

        if(!registerCommand.getPassword().equals(registerCommand.getPasswordConfirmation())) {
            throw new BadCredentialsException("Passwords do not match");
        }

        User newUser = new User();
        newUser.setName(registerCommand.getName());
        newUser.setSurname(registerCommand.getSurname());
        newUser.setUsername(registerCommand.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerCommand.getPassword()));
        newUser.setEmail(registerCommand.getEmail());
        newUser.setRole(registerCommand.getRole());

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }


    @PostMapping("/refreshAccessToken")
    public JwtResponseDTO refreshToken(HttpServletRequest request) {
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token cookie is missing"));

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    Role role = userInfo.getRole();
                    String accessToken = jwtService.generateToken(userInfo.getId(), userInfo.getUsername(), String.valueOf(role));
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .build();
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token was not found in DB."));
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, HttpServletRequest request) {

        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to log out.");
        }

        if(refreshToken != null) {
            refreshTokenService.deleteRefreshToken(refreshToken);
        }

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());

        return ResponseEntity.ok("User successfully logged out!");
    }


    @GetMapping("/currentUser")
    public UserDTO getCurrentUser() {
        return userService.getCurrentUserDTO();
    }








    /*@PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody LoginCommand authRequestDTO) {
        //UsernamePasswordAuthenticationToken sends username and password to DaoAuthenticationProvider authProvider from
        //SecurityConf and then tries to find that User in DB
        Authentication authentication = null;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Username or password is incorrect");
        }

        if(authentication.isAuthenticated()) {
            User user = userRepository.findByUsernameIgnoreCase(authRequestDTO.getUsername());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(user.getId(), user.getUsername(), String.valueOf(user.getRole())))
                    .refreshToken(refreshToken.getToken())
                    .build();
        }
        else {
            throw new BadCredentialsException("Username or password is incorrect");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterCommand registerCommand) {
        Map<String, String> response = new HashMap<>();

        if(userRepository.findByUsernameIgnoreCase(registerCommand.getUsername()) != null) {
            response.put("message", "Username is already in use");
            return ResponseEntity.badRequest().body(response);
        }

        User newUser = new User();
        newUser.setName(registerCommand.getName());
        newUser.setSurname(registerCommand.getSurname());
        newUser.setUsername(registerCommand.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerCommand.getPassword()));
        newUser.setEmail(registerCommand.getEmail());
        newUser.setRole(registerCommand.getRole());

        userRepository.save(newUser);

        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refreshAccessToken")
    public JwtResponseDTO refreshToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenDTO) {
        return refreshTokenService.findByToken(refreshTokenDTO.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    Role role = userInfo.getRole();
                    String accessToken = jwtService.generateToken(userInfo.getId(), userInfo.getUsername(), String.valueOf(role));
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenDTO.getRefreshToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token was not found in DB."));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to log out.");
        }

        refreshTokenRepository.deleteByUserInfoId(currentUser.getId());
        return ResponseEntity.ok("User successfully logged out!");
    }*/



}
