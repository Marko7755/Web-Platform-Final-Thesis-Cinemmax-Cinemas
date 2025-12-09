-- Users
INSERT INTO Users (name, surname, username, password, email, role)
VALUES ('Marko', 'Gradiščaj', 'mgradiscaj', '$2a$12$oNXR1CieDVtD/S7NCnIUuOovcZtDdGY3t5Ks5Z/zQjpMEiLiZgyga',
        'mgradiscaj@gmail.com', 'admin'),
       ('Ana', 'Gradiščaj', 'agradiscaj', '$2a$12$uUkT17eOGW5jQjrLWcVI3Om3Al/UHr/x9SVi26dK.v2K41PuJZuMu',
        'roleteiprozori@gmail.com', 'user');

-- Directors
INSERT INTO Directors (name, surname)
VALUES ('Christopher', 'Nolan'),
       ('Wes', 'Anderson'),
       ('Denis', 'Villeneuve'),
       ('Greta', 'Gerwig'),
       ('George', 'Miller'),
       ('Todd', 'Phillips'),
       ('Ridley', 'Scott'),
       ('James', 'Cameron'),
       ('Matt', 'Reeves'),
       ('Christopher', 'McQuarrie');


-- Actors
INSERT INTO Actors (name, surname)
VALUES ('Leonardo', 'DiCaprio'),
       ('Joseph', 'Gordon-Levitt'),
       ('Tony', 'Revolori'),
       ('Ralph', 'Fiennes'),
       ('Matthew', 'McConaughey'),
       ('Anne', 'Hathaway'),
       ('Cillian', 'Murphy'),
       ('Emily', 'Blunt'),
       ('Florence', 'Pugh'),
       ('Ryan', 'Gosling'),
       ('Margot', 'Robbie'),
       ('Timothée', 'Chalamet'),
       ('Austin', 'Butler'),
       ('Anya', 'Taylor-Joy'),
       ('Joaquin', 'Phoenix'),
       ('Robert', 'De Niro'),
       ('Paul', 'Mescal'),
       ('Pedro', 'Pascal'),
       ('Denzel', 'Washington'),
       ('Sam', 'Worthington'),
       ('Zoe', 'Saldaña'),
       ('Sigourney', 'Weaver'),
       ('Robert', 'Pattinson'),
       ('Zoë', 'Kravitz'),
       ('Colin', 'Farrell'),
       ('Tom', 'Cruise'),
       ('Hayley', 'Atwell'),
       ('Simon', 'Pegg');


-- Films
INSERT INTO Films (name, genre, about, imdb_rating, age_rate, duration, image_url, trailer_url, cinema_release_date,
                   cinema_end_date)
VALUES
-- Inception
('Inception', 'Thriller',
 'A skilled thief is offered a chance to have his past crimes forgiven if he can implant an idea into someone''s subconscious.',
 8.8, 12, 148, 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1758366601/inception_pbfd3i.webp',
 'https://www.youtube.com/watch?v=YoHD9XEInc0', '2025-08-30', '2025-09-23'),

-- The Grand Budapest Hotel
('The Grand Budapest Hotel', 'Comedy',
 'A writer encounters the owner of an aging high-class hotel and hears his story.', 8.1, 12, 99,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1758366601/The_Grand_Budapest_Hotel_yrrduf.webp',
 'https://www.youtube.com/watch?v=1Fg5iWmQjwk', '2025-09-01', '2025-09-22'),

-- Interstellar
('Interstellar', 'Sci-Fi',
 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.', 8.7, 10, 169,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1758366423/inter_rou3tj.webp',
 'https://www.youtube.com/watch?v=zSWdZVtXT7E', '2025-09-16', '2025-09-30'),

('Oppenheimer', 'Biography', 'The story of J. Robert Oppenheimer and the Manhattan Project.', 8.3, 15, 180,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757863502/opennhimer_jzzokf.webp',
 'https://www.youtube.com/watch?v=uYPbbksJxIg', '2025-08-10', '2025-09-20'),

('Barbie', 'Comedy', 'Barbie and Ken venture into the real world and question perfection.', 6.8, 10, 114,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757864863/barbie_wdtocl.webp',
 'https://www.youtube.com/watch?v=pBk4NYhWNMM', '2025-09-05', '2025-09-30'),

('Dune: Part Two', 'Sci-Fi', 'Paul Atreides unites with the Fremen to avenge his family.', 8.5, 12, 166,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757861535/dune_rvq32u.webp',
 'https://www.youtube.com/watch?v=U2Qp5pL3ovA', '2025-09-12', '2025-10-12'),

('Joker: Folie à Deux', 'Drama', 'Arthur Fleck returns as Joker in a twisted duet.', 5.2, 15, 140,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757861535/joker_ggmclu.webp',
 'https://www.youtube.com/watch?v=_OKAwz2MsJs&ab_channel=WarnerBros.', '2025-09-05', '2025-10-20'),

('Gladiator II', 'Action', 'Decades after Maximus, Rome faces new power struggles and redemption.', 6.5, 15, 150,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757864982/glad2_lc57yx.webp',
 'https://www.youtube.com/watch?v=TQwSz88ITAE&ab_channel=ParamountPictures', '2025-09-10', '2025-10-10'),

('Avatar 3', 'Sci-Fi', 'The saga of Pandora continues with new clans and conflicts.', 0.0, 10, 180,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757861534/avatar_qxdt6p.webp',
 'https://www.youtube.com/watch?v=nb_fFj_0rq8&ab_channel=Avatar', '2025-09-22', '2025-10-20'),

('The Batman Part II', 'Action', 'Batman faces a darker conspiracy gripping Gotham.', 0.0, 15, 170,
 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757861536/batman_z03csu.webp',
 'https://www.youtube.com/watch?v=T7_zMl_ZhdQ&t=1s&ab_channel=TeaserCon', '2025-10-01', '2025-10-25'),

('Mission: Impossible - The Final Reckoning', 'Action', 'Ethan Hunt embarks on his most perilous mission yet.', 7.3,
 12, 160, 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1757861536/missionImpossible_ksopbx.webp',
 'https://www.youtube.com/watch?v=fsQgc9pCyDU&ab_channel=ParamountPictures', '2025-09-07', '2025-10-18');


-- Film_Roles
INSERT INTO Film_Roles (film_id, actor_id)
VALUES
-- Inception: Leonardo, Joseph
(1, 1),
(1, 2),
-- Grand Budapest Hotel: Tony, Ralph
(2, 3),
(2, 4),
-- Interstellar: Matthew, Anne
(3, 5),
(3, 6),
(4, 7),
(4, 8),
(4, 9),
(5, 11),
(5, 10),
(6, 12),
(6, 13),
(6, 14),
(7, 15),
(7, 16),
-- Gladiator II
(8, 17),
(8, 18),
(8, 19),
-- Avatar 3
(9, 20),
(9, 21),
(9, 22),
-- The Batman Part II
(10, 23),
(10, 24),
(10, 25),
-- MI: Dead Reckoning Part Two
(11, 26),
(11, 27),
(11, 28);

-- Film_Directors
INSERT INTO Film_Directors (film_id, director_id)
VALUES (1, 1),  -- Inception - Nolan
       (2, 2),  -- Grand Budapest - Anderson
       (3, 1),  -- Interstellar - Nolan
       (4, 1),
       (5, 4),
       (6, 3),
       (7, 6),
       (8, 7),  -- Gladiator II - Ridley Scott
       (9, 8),  -- Avatar 3 - James Cameron
       (10, 9), -- The Batman Part II - Matt Reeves
       (11, 10);
-- MI: DR Part Two - Christopher McQuarrie


-- Cinemas
INSERT INTO Cinemas (location, name)
VALUES ('Zagreb', 'Avenue Mall'),
       ('Zagreb', 'Arena Centar'),
       ('Split', 'City Center'),
       ('Split', 'Split Mall'),
       ('Osijek', 'Osijek Centar'),
       ('Osijek', 'Osijek Arena'),
       ('Rijeka', 'Rijeka Tower'),
       ('Rijeka', 'Rijeka Mall'),
       ('Velika Gorica', 'VG Galleria'),
       ('Velika Gorica', 'Velika Gorica Mall');

-- Halls
INSERT INTO Halls (cinema_Id, hall_number, capacity)
VALUES (1, 1, 100),
       (1, 2, 30),
       (1, 3, 20),

       (2, 1, 40),
       (2, 2, 50),
       (2, 3, 60),

       (3, 1, 60),
       (3, 2, 70),
       (3, 3, 80),

       (4, 1, 100),
       (4, 2, 110),
       (4, 3, 120),
       (4, 4, 130),

       (5, 1, 50),
       (5, 2, 60),
       (5, 3, 70),

       (6, 1, 100),
       (6, 2, 110),
       (6, 3, 120),

       (7, 1, 90),
       (7, 2, 100),
       (7, 3, 110),
       (7, 4, 120),
       (7, 5, 130),
       (7, 6, 140),

       (8, 1, 50),
       (8, 2, 60),
       (8, 3, 70),
       (8, 4, 80),

       (9, 1, 20),
       (9, 2, 30),
       (9, 3, 50),

       (10, 1, 90),
       (10, 2, 100),
       (10, 3, 110);

-- Show_Type
INSERT INTO Show_Type (type, additional_price)
VALUES ('2D', 0.0),
       ('3D', 2.5),
       ('IMAX', 3.5);


-- SHOW TIMES for film_id = 1, razlicita vremena po gradovima (3 termina / dan / grad)
-- Datumi: 2025-09-14, 2025-09-15, 2025-09-16
-- Jutarnji termin (09:00) na 2025-09-14 je već prošao.

INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- ZAGREB (halls 1,5,6)
(1, 1, TIMESTAMP '2025-09-20 09:00:00', 1),
(1, 5, TIMESTAMP '2025-09-20 17:20:00', 2),
(1, 6, TIMESTAMP '2025-09-20 20:10:00', 3),
(1, 2, TIMESTAMP '2025-09-21 09:00:00', 1),
(1, 5, TIMESTAMP '2025-09-21 16:45:00', 2),
(1, 6, TIMESTAMP '2025-09-21 21:05:00', 3),
(1, 1, TIMESTAMP '2025-09-22 09:00:00', 1),
(1, 5, TIMESTAMP '2025-09-22 18:00:00', 2),
(1, 6, TIMESTAMP '2025-09-22 20:50:00', 3),

-- SPLIT (halls 8,10,7)
(1, 8, TIMESTAMP '2025-09-20 09:00:00', 1),
(1, 10, TIMESTAMP '2025-09-20 17:10:00', 2),
(1, 7, TIMESTAMP '2025-09-20 19:50:00', 3),
(1, 8, TIMESTAMP '2025-09-21 09:00:00', 1),
(1, 10, TIMESTAMP '2025-09-21 13:40:00', 2),
(1, 7, TIMESTAMP '2025-09-21 20:30:00', 3),
(1, 8, TIMESTAMP '2025-09-22 09:00:00', 1),
(1, 10, TIMESTAMP '2025-09-22 14:20:00', 2),
(1, 7, TIMESTAMP '2025-09-22 21:15:00', 3),

-- OSIJEK (halls 15,18,14)
(1, 15, TIMESTAMP '2025-09-20 09:00:00', 1),
(1, 18, TIMESTAMP '2025-09-20 17:00:00', 2),
(1, 14, TIMESTAMP '2025-09-20 20:40:00', 3),
(1, 15, TIMESTAMP '2025-09-21 09:00:00', 1),
(1, 18, TIMESTAMP '2025-09-21 16:30:00', 2),
(1, 14, TIMESTAMP '2025-09-21 19:55:00', 3),
(1, 15, TIMESTAMP '2025-09-22 09:00:00', 1),
(1, 18, TIMESTAMP '2025-09-22 14:45:00', 2),
(1, 14, TIMESTAMP '2025-09-22 21:10:00', 3),

-- RIJEKA (halls 22,27,24)
(1, 22, TIMESTAMP '2025-09-20 09:00:00', 1),
(1, 27, TIMESTAMP '2025-09-20 17:25:00', 2),
(1, 24, TIMESTAMP '2025-09-20 20:35:00', 3),
(1, 22, TIMESTAMP '2025-09-21 09:00:00', 1),
(1, 27, TIMESTAMP '2025-09-21 16:20:00', 2),
(1, 24, TIMESTAMP '2025-09-21 21:20:00', 3),
(1, 22, TIMESTAMP '2025-09-22 09:00:00', 1),
(1, 27, TIMESTAMP '2025-09-22 18:05:00', 2),
(1, 24, TIMESTAMP '2025-09-22 20:55:00', 3),

-- VELIKA GORICA (halls 31,33,32)
(1, 31, TIMESTAMP '2025-09-20 09:00:00', 1),
(1, 33, TIMESTAMP '2025-09-20 17:15:00', 2),
(1, 32, TIMESTAMP '2025-09-20 19:40:00', 3),
(1, 31, TIMESTAMP '2025-09-21 09:00:00', 1),
(1, 33, TIMESTAMP '2025-09-21 13:20:00', 2),
(1, 32, TIMESTAMP '2025-09-21 20:20:00', 3),
(1, 31, TIMESTAMP '2025-09-22 09:00:00', 1),
(1, 33, TIMESTAMP '2025-09-22 14:10:00', 2),
(1, 32, TIMESTAMP '2025-09-22 21:30:00', 3)
;

/* =========================
   FILM 2 — The Grand Budapest Hotel (2025-09-01 → 2025-09-25)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (2,4)
(2, 2, TIMESTAMP '2025-09-14 08:45:00', 1),
(2, 4, TIMESTAMP '2025-09-14 18:20:00', 2),
(2, 2, TIMESTAMP '2025-09-15 20:40:00', 3),
-- Split (9)
(2, 9, TIMESTAMP '2025-09-14 17:00:00', 2),
-- Osijek (16)
(2, 16, TIMESTAMP '2025-09-15 19:10:00', 1),
-- Rijeka (21)
(2, 21, TIMESTAMP '2025-09-16 21:05:00', 3),
-- Velika Gorica (30)
(2, 30, TIMESTAMP '2025-09-16 14:10:00', 2);

/* =========================
   FILM 3 — Interstellar (2025-09-16 → 2025-09-30)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (3,4)
(3, 3, TIMESTAMP '2025-09-20 09:10:00', 1),
(3, 4, TIMESTAMP '2025-09-20 19:30:00', 2),
-- Split (13)
(3, 13, TIMESTAMP '2025-09-21 20:45:00', 3),
-- Osijek (17)
(3, 17, TIMESTAMP '2025-09-21 18:00:00', 2),
-- Rijeka (20)
(3, 20, TIMESTAMP '2025-09-22 21:20:00', 3),
-- Velika Gorica (35)
(3, 35, TIMESTAMP '2025-09-22 14:00:00', 1);

/* =========================
   FILM 4 — Oppenheimer (2025-09-10 → 2025-10-05)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (2)
(4, 2, TIMESTAMP '2025-09-20 10:00:00', 1),
(4, 2, TIMESTAMP '2025-09-20 21:30:00', 2),

(4, 2, TIMESTAMP '2025-09-21 18:15:00', 2),
-- Split (9,13)
(4, 9, TIMESTAMP '2025-09-21 21:10:00', 3),
(4, 13, TIMESTAMP '2025-09-22 17:40:00', 2),
-- Osijek (19)
(4, 19, TIMESTAMP '2025-09-22 20:55:00', 3),
-- Rijeka (23)
(4, 23, TIMESTAMP '2025-09-23 19:05:00', 1),
-- Velika Gorica (34)
(4, 34, TIMESTAMP '2025-09-23 13:30:00', 2);

/* =========================
   FILM 5 — Barbie (2025-09-05 → 2025-09-30)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (3)
(5, 3, TIMESTAMP '2025-09-14 08:20:00', 1),
(5, 3, TIMESTAMP '2025-09-14 18:30:00', 2),
-- Split (12)
(5, 12, TIMESTAMP '2025-09-15 20:10:00', 3),
-- Osijek (16)
(5, 16, TIMESTAMP '2025-09-16 17:00:00', 2),
-- Rijeka (26)
(5, 26, TIMESTAMP '2025-09-15 19:25:00', 1),
-- Velika Gorica (30)
(5, 30, TIMESTAMP '2025-09-16 21:35:00', 3);

/* =========================
   FILM 6 — Dune: Part Two (2025-09-18 → 2025-10-12)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (4)
(6, 4, TIMESTAMP '2025-09-14 09:05:00', 1),
(6, 4, TIMESTAMP '2025-09-14 19:10:00', 2),
-- Split (11)
(6, 11, TIMESTAMP '2025-09-15 21:00:00', 3),
-- Osijek (17)
(6, 17, TIMESTAMP '2025-09-15 16:40:00', 2),
-- Rijeka (25)
(6, 25, TIMESTAMP '2025-09-16 20:20:00', 3),
-- Velika Gorica (35)
(6, 35, TIMESTAMP '2025-09-16 14:25:00', 1);

/* =========================
   FILM 7 — Joker: Folie à Deux (2025-09-25 → 2025-10-20)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (2)
(7, 2, TIMESTAMP '2025-09-14 09:30:00', 1),
(7, 2, TIMESTAMP '2025-09-14 18:10:00', 2),
-- Split (10)
(7, 10, TIMESTAMP '2025-09-15 20:50:00', 3),
-- Osijek (18)
(7, 18, TIMESTAMP '2025-09-16 16:55:00', 2),
-- Rijeka (23)
(7, 23, TIMESTAMP '2025-09-17 21:10:00', 3),
-- Velika Gorica (32)
(7, 32, TIMESTAMP '2025-09-17 14:15:00', 1);

/* =========================
   FILM 8 — Gladiator II (2025-09-20 → 2025-10-10)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (4)
(8, 4, TIMESTAMP '2025-09-14 08:50:00', 1),
(8, 4, TIMESTAMP '2025-09-14 19:00:00', 2),
-- Split (13)
(8, 13, TIMESTAMP '2025-09-16 21:25:00', 3),
-- Osijek (19)
(8, 19, TIMESTAMP '2025-09-16 16:20:00', 2),
-- Rijeka (21)
(8, 21, TIMESTAMP '2025-09-17 20:40:00', 3),
-- Velika Gorica (34)
(8, 34, TIMESTAMP '2025-09-17 13:50:00', 1);

/* =========================
   FILM 9 — Avatar 3 (2025-09-22 → 2025-10-20)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (2)
(9, 2, TIMESTAMP '2025-09-22 09:15:00', 1),
(9, 2, TIMESTAMP '2025-09-22 18:45:00', 2),
-- Split (9)
(9, 9, TIMESTAMP '2025-09-23 21:30:00', 3),
-- Osijek (17)
(9, 17, TIMESTAMP '2025-09-23 16:35:00', 2),
-- Rijeka (28)
(9, 28, TIMESTAMP '2025-09-24 20:15:00', 3),
-- Velika Gorica (35)
(9, 35, TIMESTAMP '2025-09-24 14:35:00', 1);

/* =========================
   FILM 10 — The Batman Part II (2025-10-01 → 2025-10-25)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (3)
(10, 3, TIMESTAMP '2025-10-01 09:10:00', 1),
(10, 3, TIMESTAMP '2025-10-01 19:05:00', 2),
-- Split (10)
(10, 10, TIMESTAMP '2025-10-02 21:15:00', 3),
-- Osijek (16)
(10, 16, TIMESTAMP '2025-10-02 16:25:00', 2),
-- Rijeka (24)
(10, 24, TIMESTAMP '2025-10-03 20:50:00', 3),
-- Velika Gorica (30)
(10, 30, TIMESTAMP '2025-10-03 14:20:00', 1);

/* =========================
   FILM 11 — Mission: Impossible – Dead Reckoning Part Two (2025-09-28 → 2025-10-18)
   ========================= */
INSERT INTO Show_Time (film_id, hall_id, show_time, type_id)
VALUES
-- Zagreb (4)
(11, 4, TIMESTAMP '2025-09-15 08:40:00', 1),
(11, 4, TIMESTAMP '2025-09-15 18:35:00', 2),
-- Split (12)
(11, 12, TIMESTAMP '2025-09-16 21:00:00', 3),
-- Osijek (18)
(11, 18, TIMESTAMP '2025-09-21 16:50:00', 2),
-- Rijeka (29)
(11, 29, TIMESTAMP '2025-09-22 20:30:00', 3),
-- Velika Gorica (33)
(11, 33, TIMESTAMP '2025-09-24 13:55:00', 1);


-- Seats
INSERT INTO Seats (hall_id, row_letter, seat_number)
VALUES (2, 'A', 1),
       (2, 'A', 2),
       (2, 'A', 3),
       (2, 'A', 4),
       (2, 'A', 5),
       (2, 'A', 6),
       (2, 'A', 7),
       (2, 'A', 8),
       (2, 'A', 9),
       (2, 'A', 10),

       (2, 'B', 1),
       (2, 'B', 2),
       (2, 'B', 3),
       (2, 'B', 4),
       (2, 'B', 5),
       (2, 'B', 6),
       (2, 'B', 7),
       (2, 'B', 8),
       (2, 'B', 9),
       (2, 'B', 10),

       (2, 'C', 1),
       (2, 'C', 2),
       (2, 'C', 3),
       (2, 'C', 4),
       (2, 'C', 5),
       (2, 'C', 6),
       (2, 'C', 7),
       (2, 'C', 8),
       (2, 'C', 9),
       (2, 'C', 10);


INSERT INTO Seats (hall_id, row_letter, seat_number)
VALUES
-- Hall 7 (Osijek 1): A1–A8, B1–B8, C1–C8
(7, 'A', 1),
(7, 'A', 2),
(7, 'A', 3),
(7, 'A', 4),
(7, 'A', 5),
(7, 'A', 6),
(7, 'A', 7),
(7, 'A', 8),
(7, 'B', 1),
(7, 'B', 2),
(7, 'B', 3),
(7, 'B', 4),
(7, 'B', 5),
(7, 'B', 6),
(7, 'B', 7),
(7, 'B', 8),
(7, 'C', 1),
(7, 'C', 2),
(7, 'C', 3),
(7, 'C', 4),
(7, 'C', 5),
(7, 'C', 6),
(7, 'C', 7),
(7, 'C', 8);

INSERT INTO Seats (hall_id, row_letter, seat_number)
VALUES
-- Hall 8 (Osijek 2): A1–A8, B1–B8
(8, 'A', 1),
(8, 'A', 2),
(8, 'A', 3),
(8, 'A', 4),
(8, 'A', 5),
(8, 'A', 6),
(8, 'A', 7),
(8, 'A', 8),
(8, 'B', 1),
(8, 'B', 2),
(8, 'B', 3),
(8, 'B', 4),
(8, 'B', 5),
(8, 'B', 6),
(8, 'B', 7),
(8, 'B', 8);

INSERT INTO Seats (hall_id, row_letter, seat_number)
VALUES
-- Hall 9 (Rijeka 1): A–D × 1–10
(9, 'A', 1),
(9, 'A', 2),
(9, 'A', 3),
(9, 'A', 4),
(9, 'A', 5),
(9, 'A', 6),
(9, 'A', 7),
(9, 'A', 8),
(9, 'A', 9),
(9, 'A', 10),
(9, 'B', 1),
(9, 'B', 2),
(9, 'B', 3),
(9, 'B', 4),
(9, 'B', 5),
(9, 'B', 6),
(9, 'B', 7),
(9, 'B', 8),
(9, 'B', 9),
(9, 'B', 10),
(9, 'C', 1),
(9, 'C', 2),
(9, 'C', 3),
(9, 'C', 4),
(9, 'C', 5),
(9, 'C', 6),
(9, 'C', 7),
(9, 'C', 8),
(9, 'C', 9),
(9, 'C', 10),
(9, 'D', 1),
(9, 'D', 2),
(9, 'D', 3),
(9, 'D', 4),
(9, 'D', 5),
(9, 'D', 6),
(9, 'D', 7),
(9, 'D', 8),
(9, 'D', 9),
(9, 'D', 10);

INSERT INTO Seats (hall_id, row_letter, seat_number)
VALUES
-- Hall 10 (Rijeka 2): A–C × 1–8
(10, 'A', 1),
(10, 'A', 2),
(10, 'A', 3),
(10, 'A', 4),
(10, 'A', 5),
(10, 'A', 6),
(10, 'A', 7),
(10, 'A', 8),
(10, 'B', 1),
(10, 'B', 2),
(10, 'B', 3),
(10, 'B', 4),
(10, 'B', 5),
(10, 'B', 6),
(10, 'B', 7),
(10, 'B', 8),
(10, 'C', 1),
(10, 'C', 2),
(10, 'C', 3),
(10, 'C', 4),
(10, 'C', 5),
(10, 'C', 6),
(10, 'C', 7),
(10, 'C', 8);

INSERT INTO Seats (hall_id, row_letter, seat_number)
VALUES
-- Hall 11 (VG 1): A–C × 1–9
(11, 'A', 1),
(11, 'A', 2),
(11, 'A', 3),
(11, 'A', 4),
(11, 'A', 5),
(11, 'A', 6),
(11, 'A', 7),
(11, 'A', 8),
(11, 'A', 9),
(11, 'B', 1),
(11, 'B', 2),
(11, 'B', 3),
(11, 'B', 4),
(11, 'B', 5),
(11, 'B', 6),
(11, 'B', 7),
(11, 'B', 8),
(11, 'B', 9),
(11, 'C', 1),
(11, 'C', 2),
(11, 'C', 3),
(11, 'C', 4),
(11, 'C', 5),
(11, 'C', 6),
(11, 'C', 7),
(11, 'C', 8),
(11, 'C', 9);


-- Reservations
INSERT INTO Reservations (show_time_id, user_id, reservation_time, status, final_price)
VALUES (2, 1, '2025-09-11 20:00:00', 'reserved', 15.00),
       (3, 2, '2025-09-10 20:00:00', 'reserved', 15.00),
       (46, 1, '2025-09-12 10:00:00', 'reserved', 10.00),
       (60, 1, '2025-09-14 12:00:00', 'reserved', 60.00);;


-- Reservation_Seats
INSERT INTO Reservation_Seats (reservation_id, seat_id, price)
VALUES (1, 1, 7.50),
       (1, 2, 7.50),

       (2, 1, 7.50),
       (2, 2, 7.50),

       (3, 1, 5.00),
       (3, 2, 5.00),

       (4, 1, 7.5),
       (4, 2, 7.5),
       (4, 3, 7.5),
       (4, 10, 7.5),
       (4, 12, 7.5),
       (4, 15, 7.5),
       (4, 16, 7.5),
       (4, 21, 7.5);

INSERT INTO Booked_Seats (show_time_id, seat_id, reservation_seat_id)
VALUES (2, 1, 1),
       (2, 2, 2),

       (3, 1, 3),
       (3, 2, 4),

       (46, 1, 5),
       (46, 2, 6),

       (60, 1, 7),
       (60, 2, 8),

       (60, 3, 9),
       (60, 10, 10),
       (60, 12, 11),
       (60, 15, 12),
       (60, 16, 13),
       (60, 21, 14);

