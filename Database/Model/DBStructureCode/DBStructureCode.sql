-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema cinemmax
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cinemmax
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cinemmax` DEFAULT CHARACTER SET cp1250 COLLATE cp1250_croatian_ci ;
USE `cinemmax` ;

-- -----------------------------------------------------
-- Table `cinemmax`.`Film`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Film` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `genre` VARCHAR(155) NOT NULL,
  `about` LONGTEXT NULL,
  `age_rate` INT NOT NULL,
  `duration` INT NOT NULL,
  `image_url` VARCHAR(255) NULL,
  `trailer_url` VARCHAR(255) NULL,
  `cinema_release_date` DATE NOT NULL,
  `cinema_end_date` DATE NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Actors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Actors` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `surname` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Film_Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Film_Roles` (
  `film_id` INT NOT NULL,
  `actor_id` INT NOT NULL,
  PRIMARY KEY (`film_id`, `actor_id`),
  INDEX `fk_Film_has_Actors_Actors1_idx` (`actor_id` ASC) VISIBLE,
  INDEX `fk_Film_has_Actors_Film_idx` (`film_id` ASC) VISIBLE,
  CONSTRAINT `fk_Film_has_Actors_Film`
    FOREIGN KEY (`film_id`)
    REFERENCES `cinemmax`.`Film` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Film_has_Actors_Actors1`
    FOREIGN KEY (`actor_id`)
    REFERENCES `cinemmax`.`Actors` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Directors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Directors` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `surname` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Film_Directors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Film_Directors` (
  `film_id` INT NOT NULL,
  `director_id` INT NOT NULL,
  PRIMARY KEY (`film_id`, `director_id`),
  INDEX `fk_Film_has_Directors_Directors1_idx` (`director_id` ASC) VISIBLE,
  INDEX `fk_Film_has_Directors_Film1_idx` (`film_id` ASC) VISIBLE,
  CONSTRAINT `fk_Film_has_Directors_Film1`
    FOREIGN KEY (`film_id`)
    REFERENCES `cinemmax`.`Film` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Film_has_Directors_Directors1`
    FOREIGN KEY (`director_id`)
    REFERENCES `cinemmax`.`Directors` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Cinemas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Cinemas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(155) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Halls`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Halls` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `hall_number` INT NOT NULL,
  `capacity` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Show_Type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Show_Type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `additional_price` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Show_Time`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Show_Time` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `film_id` INT NOT NULL,
  `cinema_id` INT NOT NULL,
  `show_time` DATETIME NOT NULL,
  `type_id` INT NOT NULL,
  `hall_id` INT NOT NULL,
  `base_price` DECIMAL(19,4) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Show_Time_Cinemas1_idx` (`cinema_id` ASC) VISIBLE,
  INDEX `fk_Show_Time_Film1_idx` (`film_id` ASC) VISIBLE,
  INDEX `fk_Show_Time_Halls1_idx` (`hall_id` ASC) VISIBLE,
  INDEX `fk_Show_Time_Show_Type1_idx` (`type_id` ASC) VISIBLE,
  CONSTRAINT `fk_Show_Time_Cinemas1`
    FOREIGN KEY (`cinema_id`)
    REFERENCES `cinemmax`.`Cinemas` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Show_Time_Film1`
    FOREIGN KEY (`film_id`)
    REFERENCES `cinemmax`.`Film` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Show_Time_Halls1`
    FOREIGN KEY (`hall_id`)
    REFERENCES `cinemmax`.`Halls` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Show_Time_Show_Type1`
    FOREIGN KEY (`type_id`)
    REFERENCES `cinemmax`.`Show_Type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Seats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Seats` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `hall_id` INT NOT NULL,
  `row_letter` CHAR(1) NOT NULL,
  `seat_number` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `seatsIndex` (`hall_id` ASC, `row_letter` ASC, `seat_number` ASC) VISIBLE,
  UNIQUE INDEX `uniqueInd` (`hall_id` ASC, `row_letter` ASC, `seat_number` ASC) VISIBLE,
  CONSTRAINT `fk_Seats_Halls1`
    FOREIGN KEY (`hall_id`)
    REFERENCES `cinemmax`.`Halls` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `username` VARCHAR(155) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `role` ENUM('user', 'admin') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Reservations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Reservations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `show_time_id` INT NOT NULL,
  `seat_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `reservation_time` DATETIME NOT NULL,
  `status` ENUM('reserved', 'cancelled') NOT NULL DEFAULT 'reserved',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `showTimeSeatInd` (`show_time_id` ASC, `seat_id` ASC) VISIBLE,
  INDEX `fk_Reservations_Users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Reservations_Show_Time1`
    FOREIGN KEY (`show_time_id`)
    REFERENCES `cinemmax`.`Show_Time` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservations_Seats1`
    FOREIGN KEY (`seat_id`)
    REFERENCES `cinemmax`.`Seats` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservations_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `cinemmax`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinemmax`.`Refresh_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinemmax`.`Refresh_token` (
  `idRefresh_token` INT NOT NULL AUTO_INCREMENT,
  `expiration` DATE NOT NULL,
  `user_id` INT NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`idRefresh_token`),
  INDEX `fk_Refresh_token_Users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Refresh_token_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `cinemmax`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `cinemmax`;

DELIMITER $$
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
