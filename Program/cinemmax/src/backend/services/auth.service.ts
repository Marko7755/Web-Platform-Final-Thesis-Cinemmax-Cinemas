import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { JwtResponseDTO } from '../classes/DTO/JwtResponse/jwt-response-dto';
import { Router } from '@angular/router';
import { Credentials } from '../classes/general/Credentials/credentials';
import { DOCUMENT } from '@angular/common';
import { User } from '../classes/general/User/User/user';
import { DecodedToken } from '../interfaces/decoded-token';
import { jwtDecode } from 'jwt-decode';
import { UserInfo } from '../classes/general/User/UserInfo/user-info';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8080/api/authenticate';
  private accessTokenKey = 'access_token';
  private isRefreshing = false;
  private refreshInProgress$ = new BehaviorSubject<boolean>(false);
  private localStorage: Storage | undefined;

  /* private userRoleSubject = new BehaviorSubject<string | null>(null);
  userRole$ = this.userRoleSubject.asObservable(); */

  private currentUserSubject = new BehaviorSubject<DecodedToken | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  //@Inject(DOCUMENT) private document: Document is used to inject document as object since we are using SSR(Server Side Rendering - 
  // Angular runs app on the server first to render HTML before sending it to the browser) and things like document, localStorage and window do not exists
  // so we had to use inject to be able to use document in the project
  constructor(private http: HttpClient, private router: Router, @Inject(DOCUMENT) private document: Document) {
    this.localStorage = this.document.defaultView?.localStorage;
  }

  /*
  Login Flow Summary:
    User logs in
    → backend returns access token in response body
    → backend sets refresh token in HTTP-only cookie

    Frontend stores access token in memory
    → used for authorized API requests

    When access token expires
    → HTTP interceptor sends a request to /refreshAccessToken
    → browser automatically includes refresh token cookie
    → backend verifies it and issues a new access token

    Frontend replaces old access token in memory
    → original request is retried


    Auto-logout when refresh token expires
  */

  login(credentials: Credentials): Observable<any> {
    return this.http.post<JwtResponseDTO>(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap(response => {
        this.localStorage?.setItem(this.accessTokenKey, response.accessToken);
      })
    )
  }


  logout(): Observable<any> {
    return this.http.get(`${this.apiUrl}/logout`, {
      withCredentials: true, responseType: 'text'
    });
  }



  localLogout() {
    this.localStorage?.removeItem(this.accessTokenKey);
    this.router.navigate(['']);
  }


  getAccessToken(): string | null | undefined {
    return this.localStorage?.getItem(this.accessTokenKey);
  }


  refreshAccessToken(): Observable<any> {
    if (this.isRefreshing) return this.refreshInProgress$.asObservable();

    this.isRefreshing = true;
    this.refreshInProgress$.next(true);

    return this.http.post<JwtResponseDTO>(`${this.apiUrl}/refreshAccessToken`, {}, { withCredentials: true }).pipe(
      tap(response => {
        this.localStorage?.setItem(this.accessTokenKey, response.accessToken);
        this.isRefreshing = false;
        this.refreshInProgress$.next(false);
      }),
      catchError(err => {
        this.isRefreshing = false;
        this.refreshInProgress$.next(false);
        this.logout().subscribe(); //subscribe executes observable
        return throwError(() => err);
      })
    )
  }

  isLoggedIn(): boolean {
    return !!this.getAccessToken();
  }


  register(newUser: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, newUser);
  }

  sendEmail(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgottenPassword/sendEmail`, { email }, { responseType: 'text' });
  }

  passwordReset(token: string, newPassword: string, passwordConfirmation: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgottenPassword/passwordReset`, { token, newPassword, passwordConfirmation });
  }


  getUserFromToken(): DecodedToken | null {
    const token = this.getAccessToken();
    if (!token) return null;
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (e) {
      console.error('Invalid token');
      return null;
    }
  }

  isAdmin(): boolean {
    return !!this.getUserFromToken()?.role.includes('admin');
  }

  updateUserRoleFromToken(): void {
    const user = this.getUserFromToken();
    /* this.userRoleSubject.next(user?.role || null); */
    this.currentUserSubject.next(user || null);
    /* console.log('User role updated:', user?.role); */
  }

  getCurrentUser(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.apiUrl}/currentUser`);
  }


}
