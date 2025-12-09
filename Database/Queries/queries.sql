`cinemmax`SELECT DAYNAME(CURDATE());


SELECT NOW();
SELECT DAYNAME(NOW())

DECLARE seatsCount INT DEFAULT 0;
	SELECT COUNT(*) FROM Seats INTO seatsCount;
	
	
	
	
INSERT INTO halls(hall_number, capacity) VALUES(10, 2);	
	
INSERT INTO seats(hall_id, row_letter, seat_number) VALUES(1, 'B', 6);
	


DELIMITER //
DROP TRIGGER IF EXISTS hallsCapacityTrigger //
CREATE TRIGGER hallsCapacityTrigger BEFORE INSERT ON seats
  FOR EACH ROW
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
  END; //
DELIMITER ;   




INSERT INTO film(`name`, genre, about, age_rate, duration, cinema_release_date, cinema_end_date) 
	VALUES('Shrek 5', 'Comedy/Adventure','Once upon a time in the land of Far Far Away, Shrek, the beloved green ogre, returns for a brand-new adventure filled with humor, heart, and fairy-tale mayhem. "Shrek 5" brings back the original voice cast, including Mike Myers, Eddie Murphy, and Cameron Diaz, as they embark on an exciting journey that will test their friendships and family bonds. With fresh animation, a captivating story, and the signature wit of the franchise, this long-awaited sequel promises to delight audiences of all ages.',
	8, 90, '2026-12-23', '2027-02-01');
 
 
 
 INSERT INTO cinemas(location, `name`) VALUES('Zagreb', 'Arena centar');
 
 INSERT INTO show_time(film_id, cinema_id, show_time, `type`, hall_id) VALUES(1, 1, '2026-12-23 15:15:00', '2D', 1);
 
 INSERT INTO users(`name`, username, `password`, email, `role`) VALUES('Pero', 'peroPeric', 'probniPass', 'peroPeric@gmail.com', 'user');
 
 
 INSERT INTO reservations(show_time_id, seat_id, user_id, reservation_time) 
	VALUES(2, 2, 1, '2026-12-23 15:15:00');
	
	
	
INSERT INTO actors(`name`, surname) VALUES('Mike', 'Myers');
INSERT INTO actors(`name`, surname) VALUES('Eddie ', 'Murphy');	
INSERT INTO actors(`name`, surname) VALUES('Cameron ', 'Diaz');	
INSERT INTO actors(`name`, surname) VALUES('Zendaya ', 'Coleman');		

INSERT INTO directors(`name`, surname) VALUES('Walt', 'Dohrn');
INSERT INTO directors(`name`, surname) VALUES('Conrad ', 'Vernon');

INSERT INTO film_directors VALUES(1, 1);
INSERT INTO film_directors VALUES(1, 2);


INSERT INTO film_roles VALUES(1, 1);
INSERT INTO film_roles VALUES(1, 2);
INSERT INTO film_roles VALUES(1, 3);
INSERT INTO film_roles VALUES(1, 4);

#Ispis direktora filma
SELECT d.name, d.surname
FROM film f INNER JOIN film_directors fd 
     ON fd.film_id = f.id INNER JOIN directors d
     ON fd.director_id = d.id
WHERE f.`name` = 'Shrek 5'

#Ispis glumaca filma
SELECT a.name, a.surname
FROM film f INNER JOIN film_roles fr
     ON fr.film_id = f.id INNER JOIN actors a
     ON fr.actor_id = a.id
WHERE f.`name` = 'Shrek 5'

#Ispis termina, lokacije i dvorane filma
SELECT *
FROM films f INNER JOIN show_time st
     ON f.id = st.film_id INNER JOIN cinemas c
     ON st.cinema_id = c.id INNER JOIN halls h
     ON st.hall_id = h.id

#Ispis rezervacije, korisnika, dvorane i sjedala filma
SELECT *
FROM reservations r INNER JOIN users u
     ON r.user_id = u.id INNER JOIN seats s
     ON r.seat_id = s.id INNER JOIN halls h
     ON s.hall_id = h.id
     
UPDATE film
SET image_url = 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1742475749/logo2_xdieie.png',
    trailer_url = 'https://www.youtube.com/watch?v=KbiwL74KyJQ'
WHERE id = 1     





SELECT * FROM films
WHERE cinema_release_date

SELECT SUBDATE('2025.07.29'), NOW())


#Get 3 	upcoming movies
SELECT *
FROM Films
WHERE DATE(cinema_release_date) >= CURRENT_DATE
ORDER BY ABS(DATEDIFF(cinema_release_date, CURRENT_DATE))
LIMIT 3;

#Coming soon
SELECT *
FROM Films
WHERE cinema_release_date > CURDATE()


#Ending soon
SELECT *
FROM Films
WHERE cinema_end_date > CURDATE() AND cinema_end_date  BETWEEN SUBDATE(CURDATE(), INTERVAL 10 DAY) AND CURDATE()
ORDER BY cinema_end_date DESC


SELECT CURDATE()

SELECT DATE(SUBDATE(NOW(), INTERVAL 10 DAY))

SELECT DISTINCT genre FROM films ORDER BY 1;
