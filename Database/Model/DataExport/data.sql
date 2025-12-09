/*
SQLyog Community v13.2.1 (64 bit)
MySQL - 8.0.36 : Database - zavrsni
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cinemmax` /*!40100 DEFAULT CHARACTER SET cp1250 COLLATE cp1250_croatian_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cinemmax`;

/*Data for the table `actors` */

insert  into `actors`(`id`,`name`,`surname`) values 
(1,'Mike','Myers'),
(2,'Eddie ','Murphy'),
(3,'Cameron ','Diaz'),
(4,'Zendaya ','Coleman');

/*Data for the table `cinemas` */

insert  into `cinemas`(`id`,`location`,`name`) values 
(1,'Zagreb','Arena centar');

/*Data for the table `directors` */

insert  into `directors`(`id`,`name`,`surname`) values 
(1,'Walt','Dohrn'),
(2,'Conrad ','Vernon');

/*Data for the table `film` */

insert  into `film`(`id`,`name`,`genre`,`about`,`age_rate`,`duration`,`image_url`,`trailer_url`,`cinema_release_date`,`cinema_end_date`) values 
(1,'Shrek 5','Comedy/Adventure','Once upon a time in the land of Far Far Away, Shrek, the beloved green ogre, returns for a brand-new adventure filled with humor, heart, and fairy-tale mayhem. \"Shrek 5\" brings back the original voice cast, including Mike Myers, Eddie Murphy, and Cameron Diaz, as they embark on an exciting journey that will test their friendships and family bonds. With fresh animation, a captivating story, and the signature wit of the franchise, this long-awaited sequel promises to delight audiences of all ages.',8,90,'https://res.cloudinary.com/dqqjdinyg/image/upload/v1742475749/logo2_xdieie.png','https://www.youtube.com/watch?v=KbiwL74KyJQ','2026-12-23','2027-02-01');

/*Data for the table `film_directors` */

insert  into `film_directors`(`film_id`,`director_id`) values 
(1,1),
(1,2);

/*Data for the table `film_roles` */

insert  into `film_roles`(`film_id`,`actor_id`) values 
(1,1),
(1,2),
(1,3),
(1,4);

/*Data for the table `halls` */

insert  into `halls`(`id`,`hall_number`,`capacity`) values 
(1,10,2);

/*Data for the table `refresh_token` */

/*Data for the table `reservations` */

insert  into `reservations`(`id`,`show_time_id`,`seat_id`,`user_id`,`reservation_time`,`status`) values 
(1,2,2,1,'2026-12-23 15:15:00','reserved');

/*Data for the table `seats` */

insert  into `seats`(`id`,`hall_id`,`row_letter`,`seat_number`) values 
(2,1,'A',6),
(5,1,'B',6);

/*Data for the table `show_time` */

insert  into `show_time`(`id`,`film_id`,`cinema_id`,`show_time`,`type_id`,`hall_id`,`base_price`) values 
(2,1,1,'2025-06-05 19:30:00',1,1,5.0000);

/*Data for the table `show_type` */

insert  into `show_type`(`id`,`type`,`additional_price`) values 
(1,'1D',0.00);

/*Data for the table `users` */

insert  into `users`(`id`,`name`,`username`,`password`,`email`,`role`) values 
(1,'Pero','peroPeric','probniPass','peroPeric@gmail.com','user');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
