-- Users
INSERT INTO Users (name, surname, username, password, email, role) VALUES
                                                                       ('Marko', 'Gradiščaj', 'mgradiscaj', '$2a$12$oNXR1CieDVtD/S7NCnIUuOovcZtDdGY3t5Ks5Z/zQjpMEiLiZgyga', 'mgradiscaj@gmail.com', 'admin'),
                                                                       ('Ana', 'Gradiščaj', 'agradiscaj', '$2a$12$uUkT17eOGW5jQjrLWcVI3Om3Al/UHr/x9SVi26dK.v2K41PuJZuMu', 'roleteiprozori@gmail.com', 'user');

-- Directors
INSERT INTO Directors (name, surname) VALUES
                                          ('Christopher', 'Nolan'),          -- id 1
                                          ('Wes', 'Anderson');              -- id 2

-- Actors
INSERT INTO Actors (name, surname) VALUES
                                       ('Leonardo', 'DiCaprio'),         -- id 1
                                       ('Joseph', 'Gordon-Levitt'),      -- id 2
                                       ('Tony', 'Revolori'),             -- id 3
                                       ('Ralph', 'Fiennes'),             -- id 4
                                       ('Matthew', 'McConaughey'),       -- id 5
                                       ('Anne', 'Hathaway');             -- id 6

-- Films
INSERT INTO Films (name, genre, about, imdb_rating, age_rate, duration, image_url, trailer_url, cinema_release_date, cinema_end_date) VALUES
-- Inception
('Inception', 'Sci-Fi, Thriller', 'A skilled thief is offered a chance to have his past crimes forgiven if he can implant an idea into someone''s subconscious.', 8.8,12, 148, 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1753729991/inception_g543tp.jpg', 'https://www.youtube.com/watch?v=YoHD9XEInc0', '2025-08-10', '2025-09-15'),
-- The Grand Budapest Hotel
('The Grand Budapest Hotel', 'Comedy', 'A writer encounters the owner of an aging high-class hotel and hears his story.', 8.1, 12, 99, 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1753734551/The_Grand_Budapest_Hotel_ck7pmr.jpg', 'https://www.youtube.com/watch?v=1Fg5iWmQjwk', '2025-08-20', '2025-09-30'),
-- Interstellar
('Interstellar', 'Sci-Fi, Adventure', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.', 8.7,10, 169, 'https://res.cloudinary.com/dqqjdinyg/image/upload/v1753795028/interstellar_fbwjaj.jpg', 'https://www.youtube.com/watch?v=zSWdZVtXT7E', '2025-08-25', '2025-10-01');

-- Film_Roles
INSERT INTO Film_Roles (film_id, actor_id) VALUES
-- Inception: Leonardo, Joseph
(1, 1), (1, 2),
-- Grand Budapest Hotel: Tony, Ralph
(2, 3), (2, 4),
-- Interstellar: Matthew, Anne
(3, 5), (3, 6);

-- Film_Directors
INSERT INTO Film_Directors (film_id, director_id) VALUES
                                                      (1, 1),  -- Inception - Nolan
                                                      (2, 2),  -- Grand Budapest - Anderson
                                                      (3, 1);  -- Interstellar - Nolan

-- Cinemas
INSERT INTO Cinemas (location, name) VALUES
                                         ('Zagreb', 'Cinemmax Avenue Mall'),
                                         ('Split', 'Cinemmax City Center');

-- Halls
INSERT INTO Halls (hall_number, capacity) VALUES
                                              (1, 100),
                                              (2, 80);

-- Show_Type
INSERT INTO Show_Type (type, additional_price) VALUES
                                                   ('2D', 0.00),
                                                   ('3D', 2.50);

-- Show_Time
INSERT INTO Show_Time (film_id, cinema_id, show_time, type_id, hall_id) VALUES
                                                                                        (1, 1, '2025-08-15 20:00:00', 2, 1),
                                                                                        (2, 1, '2025-08-22 19:30:00', 1, 2),
                                                                                        (3, 2, '2025-08-28 21:00:00', 2, 1);

-- Seats
INSERT INTO Seats (hall_id, row_letter, seat_number) VALUES
                                                         (1, 'A', 1), (1, 'A', 2),
                                                         (2, 'B', 1);

-- Reservations
INSERT INTO Reservations (show_time_id, seat_id, user_id, reservation_time, status) VALUES
                                                                                        (1, 1, 1, '2025-08-10 10:00:00', 'reserved'),
                                                                                        (2, 2, 2, '2025-08-18 14:00:00', 'reserved'),
                                                                                        (2, 3, 2, '2025-08-18 12:00:00', 'reserved');

