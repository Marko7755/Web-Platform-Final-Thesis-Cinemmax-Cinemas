package cinemmaxbackend.general.classes.DTO.EmailReservationDetailsDTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class EmailReservationDetailsDTO {
    private Long reservationId;
    private List<Long> seatReservationIds;
    private List<Long> reservationSeatIds;

    private String userEmail;
    private String userName;

    private String filmName;
    private String filmImageUrl;
    private Integer filmDuration;

    private String cinemaName;
    private String cinemaLocation;

    private String showTime;

    private Integer hallNumber;

    private List<String> seats;

    private String showType;

    private Integer numberOfTickets;

    private BigDecimal finalPrice;

    private String reservationTime;

}
