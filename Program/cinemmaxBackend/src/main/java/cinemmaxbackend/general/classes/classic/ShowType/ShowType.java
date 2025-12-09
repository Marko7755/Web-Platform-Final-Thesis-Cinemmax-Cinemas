package cinemmaxbackend.general.classes.classic.ShowType;

import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.commands.ShowType.ShowTypeCommand;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Show_Type")
public class ShowType {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "type")
     private String type;

     @Column(name = "additional_price", precision = 5, scale = 1)
     private BigDecimal additionalPrice;

    public static ShowType convertToShowType(ShowTypeCommand showTypeCommand) {
        ShowType showType = new ShowType();
        showType.setType(showTypeCommand.getType());
        showType.setAdditionalPrice(showTypeCommand.getAdditionalPrice());
        return showType;
    }

}
