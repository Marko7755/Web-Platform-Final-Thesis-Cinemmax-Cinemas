-- MySQL Workbench Synchronization
-- Generated: 2025-08-06 00:24
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: mgrad

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER TABLE `cinemmax`.`Film_Roles` 
DROP FOREIGN KEY `fk_Film_Roles_Actors1`,
DROP FOREIGN KEY `fk_Film_Roles_Film1`;

ALTER TABLE `cinemmax`.`Film_Directors` 
DROP FOREIGN KEY `fk_Film_has_Directors_Directors1`;

ALTER TABLE `cinemmax`.`Show_Time` 
DROP FOREIGN KEY `fk_Show_Time_Cinemas1`,
DROP FOREIGN KEY `fk_Show_Time_Film1`,
DROP FOREIGN KEY `fk_Show_Time_Halls1`;

ALTER TABLE `cinemmax`.`Seats` 
DROP FOREIGN KEY `fk_Seats_Halls1`;

ALTER TABLE `cinemmax`.`Reservations` 
DROP FOREIGN KEY `fk_Reservations_Seats1`,
DROP FOREIGN KEY `fk_Reservations_Users1`;

ALTER TABLE `cinemmax`.`Refresh_token` 
DROP FOREIGN KEY `fk_Refresh_token_Users1`;

ALTER TABLE `cinemmax`.`Password_reset_token` 
DROP FOREIGN KEY `fk_Password_reset_token_Users1`;

ALTER TABLE `cinemmax`.`Films` 
ADD COLUMN `imdbRating` DECIMAL(3,1) NOT NULL AFTER `about`;

ALTER TABLE `cinemmax`.`Film_Roles` 
ADD CONSTRAINT `fk_Film_Roles_Actors1`
  FOREIGN KEY (`actor_id`)
  REFERENCES `cinemmax`.`Actors` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Film_Roles_Film1`
  FOREIGN KEY (`film_id`)
  REFERENCES `cinemmax`.`Films` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `cinemmax`.`Film_Directors` 
DROP FOREIGN KEY `fk_Film_has_Directors_Film1`;

ALTER TABLE `cinemmax`.`Film_Directors` ADD CONSTRAINT `fk_Film_has_Directors_Film1`
  FOREIGN KEY (`film_id`)
  REFERENCES `cinemmax`.`Films` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Film_has_Directors_Directors1`
  FOREIGN KEY (`director_id`)
  REFERENCES `cinemmax`.`Directors` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `cinemmax`.`Show_Time` 
DROP FOREIGN KEY `fk_Show_Time_Show_Type1`;

ALTER TABLE `cinemmax`.`Show_Time` ADD CONSTRAINT `fk_Show_Time_Cinemas1`
  FOREIGN KEY (`cinema_id`)
  REFERENCES `cinemmax`.`Cinemas` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Show_Time_Film1`
  FOREIGN KEY (`film_id`)
  REFERENCES `cinemmax`.`Films` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Show_Time_Show_Type1`
  FOREIGN KEY (`type_id`)
  REFERENCES `cinemmax`.`Show_Type` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Show_Time_Halls1`
  FOREIGN KEY (`hall_id`)
  REFERENCES `cinemmax`.`Halls` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `cinemmax`.`Seats` 
ADD CONSTRAINT `fk_Seats_Halls1`
  FOREIGN KEY (`hall_id`)
  REFERENCES `cinemmax`.`Halls` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `cinemmax`.`Reservations` 
DROP FOREIGN KEY `fk_Reservations_Show_Time1`;

ALTER TABLE `cinemmax`.`Reservations` ADD CONSTRAINT `fk_Reservations_Show_Time1`
  FOREIGN KEY (`show_time_id`)
  REFERENCES `cinemmax`.`Show_Time` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Reservations_Seats1`
  FOREIGN KEY (`seat_id`)
  REFERENCES `cinemmax`.`Seats` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Reservations_Users1`
  FOREIGN KEY (`user_id`)
  REFERENCES `cinemmax`.`Users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `cinemmax`.`Refresh_token` 
ADD CONSTRAINT `fk_Refresh_token_Users1`
  FOREIGN KEY (`user_id`)
  REFERENCES `cinemmax`.`Users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `cinemmax`.`Password_reset_token` 
ADD CONSTRAINT `fk_Password_reset_token_Users1`
  FOREIGN KEY (`user_id`)
  REFERENCES `cinemmax`.`Users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


DELIMITER $$

USE `cinemmax`$$
DROP TRIGGER IF EXISTS `cinemmax`.`Seats_BEFORE_INSERT` $$

USE `cinemmax`$$
CREATE DEFINER = CURRENT_USER TRIGGER `cinemmax`.`Seats_BEFORE_INSERT` BEFORE INSERT ON `Seats` FOR EACH ROW
BEGIN
	DECLARE seatsCount INT DEFAULT 0;
    DECLARE hallCapacity INT DEFAULT 0;
    DECLARE errorMessage TEXT;
    
    SELECT capacity INTO hallCapacity FROM halls
    WHERE id = new.hall_id;
    
    SELECT COUNT(*) INTO seatsCount FROM seats 
    WHERE hall_id = new.hall_id;
    
    IF seatsCount >= hallCapacity THEN
      SET errorMessage = CONCAT('You cannot add more than ', hallCapacity, ' seats.');
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = errorMessage;
    END IF;
END$$


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
