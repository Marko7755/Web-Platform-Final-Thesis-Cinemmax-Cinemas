package cinemmaxbackend.security.interfaces;

import cinemmaxbackend.general.classes.DTO.EmailReservationDetailsDTO.EmailReservationDetailsDTO;
import cinemmaxbackend.security.classes.general.EmailDetails;

public interface EmailServiceInterface {
    //String sendSimpleMail(EmailDetails emailDetails);
    void sendPasswordResetMail(EmailDetails emailDetails);
    void sendReservationDetailsMail(EmailReservationDetailsDTO emailReservationDetailsDTO);
}
