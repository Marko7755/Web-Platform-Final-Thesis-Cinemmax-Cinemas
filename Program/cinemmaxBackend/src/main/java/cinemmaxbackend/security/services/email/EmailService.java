package cinemmaxbackend.security.services.email;

import cinemmaxbackend.general.classes.DTO.EmailReservationDetailsDTO.EmailReservationDetailsDTO;
import cinemmaxbackend.general.classes.DTO.seat.EmailSeatDTO;
import cinemmaxbackend.general.classes.classic.qr.QrUtil;
import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import cinemmaxbackend.general.classes.classic.reservation.ReservationSeat;
import cinemmaxbackend.security.classes.general.EmailDetails;
import cinemmaxbackend.security.interfaces.EmailServiceInterface;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendPasswordResetMail(EmailDetails emailDetails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String currDate = LocalDate.now().format(formatter);

            LocalTime now = LocalTime.now();
            LocalTime timePlus20 = now.plusMinutes(20); //token expires after 20 minutes

            int curHour = timePlus20.getHour();
            int curMinute = timePlus20.getMinute();

            String htmlContent = createPasswordResetEmail(emailDetails, currDate, curHour, curMinute);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendReservationDetailsMail(EmailReservationDetailsDTO dto) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(dto.getUserEmail());
            helper.setSubject("Your Cinemmax reservation ✔");

            String html = createReservationEmail(dto);
            helper.setText(html, true);

            // 2) generiraj onoliko QR kodova koliko ima karata
            for (int i = 0; i < dto.getNumberOfTickets(); i++) {

                String qrPayload = "RESERVATION_" + dto.getReservationId() + "_SEAT_RESERVATION_ " + dto.getSeatReservationIds().get(i)
                        + "_TICKET_" + (i+1);

                byte[] qrPng = QrUtil.generateQrPng(qrPayload, 320);

                // dodaj inline sliku (ticketQr0, ticketQr1, …)
                helper.addInline("ticketQr" + i, new ByteArrayResource(qrPng), "image/png");
            }

            javaMailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send reservation email", e);
        }
    }

    private static String createReservationEmail(EmailReservationDetailsDTO dto) {
        // napravimo HTML sekciju s više QR kodova
        StringBuilder qrSection = new StringBuilder();
        qrSection.append("<div class='section qr-wrap'>")
                .append("<h3>🎟️ Your Tickets (QR)</h3>");
        for (int i = 0; i < dto.getNumberOfTickets(); i++) {
            qrSection.append("<div style='margin:10px; display:inline-block;'>")
                    .append("<img src='cid:ticketQr").append(i).append("' alt='Ticket ").append(i + 1).append("' />")
                    .append("<div class='note'>Ticket ").append(i + 1).append("</div>")
                    .append("</div>");
        }
        qrSection.append("</div>");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                "<title>Cinemmax Reservation</title>" +
                "<style>" +
                "  body { margin:0; padding:0; background:#0f172a; font-family: Arial, sans-serif; color:#0b1220; }" +
                "  .outer { padding:24px; }" +
                "  .email { max-width:640px; margin:0 auto; background:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 10px 30px rgba(2,6,23,.25); border:1px solid #e5e7eb; }" +
                "  .brand { background:linear-gradient(135deg,#1337B4,#041D57); padding:20px 24px; }" +
                "  .brand .logo { display:block; max-width:180px; height:auto; }" +
                "  .content { padding:28px; }" +
                "  h1 { margin:0 0 6px 0; font-size:22px; line-height:28px; color:#1337B4; }" +
                "  .muted { color:#6b7280; font-size:14px; line-height:20px; margin:0 0 18px 0; }" +
                "  .poster { width:260px; max-width:100%; border-radius:12px; display:block; margin:0 0 12px 0; }" +
                "  h3 { margin:8px 0 10px 0; color:#1337B4; font-size:16px; }" +
                "  .kv { margin:6px 0; font-size:14px; line-height:20px; color:#111827; }" +
                "  .key { font-weight:bold; }" +
                "  .chip { display:inline-block; padding:4px 10px; border-radius:999px; background:#f1f5f9; color:#0b1220; font-size:12px; border:1px solid #e5e7eb; }" +
                "  .section { margin-top:18px; }" +
                "  .divider { height:1px; background:#e5e7eb; margin:18px 0; }" +
                "  .qr-wrap { text-align:center; padding:16px; border:1px dashed #e5e7eb; border-radius:12px; background:#fafafa; }" +
                "  .qr-wrap h3 { margin:0 0 12px 0; color:#1337B4; font-size:16px; }" +
                "  .qr-wrap img { width:160px; height:160px; border-radius:8px; }" +
                "  .note { font-size:12px; color:#6b7280; margin-top:8px; }" +
                "  .footer { padding:18px 28px 26px; text-align:center; color:#6b7280; font-size:12px; line-height:18px; }" +
                "  .kv-list { margin:6px 0; font-size:14px; line-height:20px; color:#111827; }" +
                "  .kv-list .key { font-weight:bold; display:block; margin-bottom:4px; }" +
                "  .seats-list { margin:0; padding-left:18px; }" +
                "  .seats-list li { margin:2px 0; }" +
                "  @media (max-width:620px) { .content { padding:20px; } .poster { width:100%; } }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='outer'>" +
                "    <div class='email'>" +
                "      <div class='brand'>" +
                "        <img class='logo' src='https://res.cloudinary.com/dqqjdinyg/image/upload/v1758373582/logoNoviPng_z8b8fi.png' alt='Cinemmax Logo'/>" +
                "      </div>" +
                "      <div class='content'>" +
                "        <h1>Hello " + dto.getUserName() + ",</h1>" +
                "        <p class='muted'>Your reservation has been confirmed. Below are your details:</p>" +
                "" +
                "        <h3>🎬 Movie</h3>" +
                "        <img src='" + dto.getFilmImageUrl() + "' alt='Movie Poster' class='poster'/>" +
                "        <p class='kv'><span class='key'>Title:</span> " + dto.getFilmName() + "</p>" +
                "        <p class='kv'><span class='key'>Duration:</span> " + dto.getFilmDuration() + " min</p>" +
                "        <p class='kv'><span class='key'>Show type:</span> <span class='chip'>" + dto.getShowType() + "</span></p>" +
                "" +
                "        <div class='section'>" +
                "          <h3>📍 Cinema</h3>" +
                "          <p class='kv'><span class='key'>Name:</span> " + dto.getCinemaName() + "</p>" +
                "          <p class='kv'><span class='key'>Location:</span> " + dto.getCinemaLocation() + "</p>" +
                "          <p class='kv'><span class='key'>Hall:</span> " + dto.getHallNumber() + "</p>" +
                "        </div>" +
                "" +
                "        <div class='divider'></div>" +
                "" +
                "        <div class='section'>" +
                "          <h3>🕒 Showtime</h3>" +
                "          <p class='kv'><span class='key'>Date &amp; Time:</span> " + dto.getShowTime() + "</p>" +
                "" +
                "          <div class='kv-list'>" +
                "            <span class='key'>Seats:</span>" +
                "            <ul class='seats-list'>" +
                dto.getSeats().stream()
                        .map(s -> s.replaceAll("(?i)Seats\\s+(\\d+)\\s*$", "Seat $1"))
                        .map(s -> "<li>" + s + "</li>")
                        .collect(java.util.stream.Collectors.joining()) +
                "            </ul>" +
                "          </div>" +
                "" +
                "          <p class='kv'><span class='key'>Tickets:</span> " + dto.getNumberOfTickets() + "</p>" +
                "          <p class='kv'><span class='key'>Total Price:</span> € " + dto.getFinalPrice() + "</p>" +
                "          <p class='kv'><span class='key'>Reservation Time:</span> " + dto.getReservationTime() + "</p>" +
                "        </div>" +
                "" +
                "        <!-- Your multi-QR section (unchanged logic) -->" +
                qrSection +
                "" +
                "      </div>" +
                "      <div class='footer'>" +
                "        Thank you for choosing <strong>Cinemmax Cinemas</strong>. Enjoy your movie! 🍿<br/>" +
                "        © 2025 Cinemmax Cinemas. This is an automated message; please do not reply." +
                "      </div>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";







    }



    private static String createPasswordResetEmail(EmailDetails emailDetails, String currDate, int curHour, int curMinute) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "  body { margin: 0; padding: 0; background-color: #0b0f29; font-family: Arial, sans-serif; color: #ffffff; }" +
                "  .email-container { max-width: 600px; margin: 0 auto; background-color: #121640; border-radius: 10px; padding: 30px; text-align: center; }" +
                "  .button { background-color: #00b4f1; color: #ffffff !important; text-decoration: none; padding: 12px 24px; border-radius: 5px; font-weight: bold; display: inline-block; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                "<img src='https://res.cloudinary.com/dqqjdinyg/image/upload/v1758373582/logoNoviPng_z8b8fi.png' alt='Cinemmax Logo' style='max-width: 180px; margin-bottom: 10px;'/>" +
                "<h2 style='color: #ffffff;'>Hello " + emailDetails.getUsername() + ",</h2>" +
                "<p style='color: #cccccc;'>We've received your request to reset your password for your Cinemmax Cinemas account.</p>" +
                "<p style='color: #cccccc;'>To proceed, click the button below:</p>" +
                "<div style='margin: 30px 0;'>" +
                "<a href=\"" + emailDetails.getLink() + "\" " +
                "style='padding:10px 20px; background-color:#007BFF; color:white; text-decoration:none; border-radius:5px; display:inline-block;' " +
                "class='button'>Reset Password</a>" +
                "</div>" +
                "<p style='color: #cccccc;'>This link is valid until: " + currDate + " " + String.format("%02d", curHour) + ":" + String.format("%02d", curMinute) + "</p>" +
                "<p style='color: #888888; font-size: 12px;'>If you didn’t request this, simply ignore this email.</p>" +
                "<p style='color: #888888; font-size: 12px;'>© 2025 Cinemmax Cinemas. This is an automated message, please do not reply.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    public static EmailReservationDetailsDTO prepareReservationForEmail(Reservation saved, String userEmail, String userName) {
        //total number seats = total number of reservations
        int totalSeats = saved.getSeats().size();

        Map<Character, String> seatsByRow = saved.getSeats().stream()
                .map(res -> new EmailSeatDTO(res.getSeat().getRowLetter(), res.getSeat().getSeatNumber()))
                .collect(Collectors.groupingBy(
                        EmailSeatDTO::getSeatRow,
                        Collectors.mapping(
                                EmailSeatDTO::getSeatNumber,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .sorted()
                                                .map(String::valueOf)
                                                .collect(Collectors.joining(", "))
                                )
                        )
                ));

        List<String> groupedSeatsForEmail = seatsByRow.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> "Row " + e.getKey() + " - Seats " + e.getValue())
                .toList();


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy. | HH:mm");

        List<Long> reservationIds = saved.getSeats().stream().map(ReservationSeat::getId).toList();
        List<Long> reservationSeatIds = saved.getSeats().stream().map(rs -> rs.getSeat().getId()).toList();

        return EmailReservationDetailsDTO.builder()
                .reservationId(saved.getId())
                .seatReservationIds(reservationIds)
                .reservationSeatIds(reservationSeatIds)
                .userEmail(userEmail)
                .userName(userName)
                .filmName(saved.getShowTime().getFilm().getName())
                .filmImageUrl(saved.getShowTime().getFilm().getImageUrl())
                .filmDuration(saved.getShowTime().getFilm().getDuration())
                .cinemaName(saved.getShowTime().getHall().getCinema().getName())
                .cinemaLocation(saved.getShowTime().getHall().getCinema().getLocation())
                .showTime(saved.getShowTime().getShowTime().format(fmt))
                .hallNumber(saved.getShowTime().getHall().getNumber())
                .seats(groupedSeatsForEmail)
                .showType(saved.getShowTime().getShowType().getType())
                .numberOfTickets(totalSeats)
                .finalPrice(saved.getFinalPrice())
                .reservationTime(saved.getReservationTime().format(fmt))
                .build();
    }


}
