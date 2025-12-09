-- MySQL Workbench Synchronization
-- Generated: 2025-03-22 12:48
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: mgrad

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER TABLE `zavrsni`.`Film_Roles` 
DROP FOREIGN KEY `fk_Film_has_Actors_Actors1`;

ALTER TABLE `zavrsni`.`Film_Directors` 
DROP FOREIGN KEY `fk_Film_has_Directors_Directors1`;

ALTER TABLE `zavrsni`.`Show_Time` 
DROP FOREIGN KEY `fk_Show_Time_Cinemas1`,
DROP FOREIGN KEY `fk_Show_Time_Film1`,
DROP FOREIGN KEY `fk_Show_Time_Halls1`;

ALTER TABLE `zavrsni`.`Seats` 
DROP FOREIGN KEY `fk_Seats_Halls1`;

ALTER TABLE `zavrsni`.`Reservations` 
DROP FOREIGN KEY `fk_Reservations_Seats1`,
DROP FOREIGN KEY `fk_Reservations_Users1`;

ALTER TABLE `zavrsni`.`Show_Time` 
ADD COLUMN `ticketPrice` DECIMAL(19,4) NOT NULL AFTER `hall_id`;

ALTER TABLE `zavrsni`.`Reservations` 
DROP INDEX `fk_Reservations_Seats1` ;
;

ALTER TABLE `zavrsni`.`Film_Roles` 
DROP FOREIGN KEY `fk_Film_has_Actors_Film`;

ALTER TABLE `zavrsni`.`Film_Roles` ADD CONSTRAINT `fk_Film_has_Actors_Film`
  FOREIGN KEY (`film_id`)
  REFERENCES `zavrsni`.`Film` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Film_has_Actors_Actors1`
  FOREIGN KEY (`actor_id`)
  REFERENCES `zavrsni`.`Actors` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `zavrsni`.`Film_Directors` 
DROP FOREIGN KEY `fk_Film_has_Directors_Film1`;

ALTER TABLE `zavrsni`.`Film_Directors` ADD CONSTRAINT `fk_Film_has_Directors_Film1`
  FOREIGN KEY (`film_id`)
  REFERENCES `zavrsni`.`Film` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Film_has_Directors_Directors1`
  FOREIGN KEY (`director_id`)
  REFERENCES `zavrsni`.`Directors` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `zavrsni`.`Show_Time` 
ADD CONSTRAINT `fk_Show_Time_Cinemas1`
  FOREIGN KEY (`cinema_id`)
  REFERENCES `zavrsni`.`Cinemas` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Show_Time_Film1`
  FOREIGN KEY (`film_id`)
  REFERENCES `zavrsni`.`Film` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Show_Time_Halls1`
  FOREIGN KEY (`hall_id`)
  REFERENCES `zavrsni`.`Halls` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `zavrsni`.`Seats` 
ADD CONSTRAINT `fk_Seats_Halls1`
  FOREIGN KEY (`hall_id`)
  REFERENCES `zavrsni`.`Halls` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `zavrsni`.`Reservations` 
DROP FOREIGN KEY `fk_Reservations_Show_Time1`;

ALTER TABLE `zavrsni`.`Reservations` ADD CONSTRAINT `fk_Reservations_Show_Time1`
  FOREIGN KEY (`show_time_id`)
  REFERENCES `zavrsni`.`Show_Time` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Reservations_Seats1`
  FOREIGN KEY (`seat_id`)
  REFERENCES `zavrsni`.`Seats` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Reservations_Users1`
  FOREIGN KEY (`user_id`)
  REFERENCES `zavrsni`.`Users` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


DELIMITER $$

USE `zavrsni`$$
DROP TRIGGER IF EXISTS `zavrsni`.`Seats_BEFORE_INSERT` $$

USE `zavrsni`$$
CREATE DEFINER = CURRENT_USER TRIGGER `zavrsni`.`Seats_BEFORE_INSERT` BEFORE INSERT ON `Seats` FOR EACH ROW
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
