-- Drop tables if they exist
DROP TABLE IF EXISTS Refresh_token, Reservations, Seats, Show_Time, Film_Roles, Film_Directors,
    Halls, Cinemas, Show_Type, Users, Film, Actors, Directors;

-- Users table
CREATE TABLE Users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(60),
                       surname VARCHAR(90),
                       username VARCHAR(155) UNIQUE,
                       password VARCHAR(255),
                       email VARCHAR(255) UNIQUE ,
                       role ENUM('user', 'admin') NOT NULL
);


-- Refresh token table
CREATE TABLE Refresh_token (
                               id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               expiration DATETIME(0),
                               user_id BIGINT,
                               token VARCHAR(255),
                               FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Password reset token table
CREATE TABLE Password_reset_token (
                               id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               expiration DATETIME(0),
                               user_id BIGINT,
                               token VARCHAR(255),
                               FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Directors table
CREATE TABLE Directors (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           name VARCHAR(255),
                           surname VARCHAR(255)
);

-- Actors table
CREATE TABLE Actors (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name VARCHAR(255),
                        surname VARCHAR(255)
);


-- Films table
CREATE TABLE Films (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       genre VARCHAR(100) NOT NULL,
                       about TEXT,
                       imdb_rating DECIMAL(3, 1) NOT NULL,
                       age_rate INT NOT NULL CHECK (age_rate >= 0),
                       duration INT NOT NULL CHECK (duration > 0),
                       image_url VARCHAR(1000),
                       trailer_url VARCHAR(1000),
                       cinema_release_date DATE NOT NULL,
                       cinema_end_date DATE NOT NULL,
                       UNIQUE (name),
                       CHECK (cinema_end_date >= cinema_release_date)
);

-- Film_Roles (Many-to-Many between Films and Actors)
CREATE TABLE Film_Roles (
                            film_id BIGINT,
                            actor_id BIGINT,
                            PRIMARY KEY (film_id, actor_id),
                            FOREIGN KEY (film_id) REFERENCES Films(id),
                            FOREIGN KEY (actor_id) REFERENCES Actors(id)
);

-- Film_Directors (Many-to-Many between Films and Directors)
CREATE TABLE Film_Directors (
                                film_id BIGINT,
                                director_id BIGINT,
                                PRIMARY KEY (film_id, director_id),
                                FOREIGN KEY (film_id) REFERENCES Films(id),
                                FOREIGN KEY (director_id) REFERENCES Directors(id)
);

-- Cinemas table
CREATE TABLE Cinemas (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         location VARCHAR(155),
                         name VARCHAR(255)
);

-- Halls table
CREATE TABLE Halls (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       cinema_id BIGINT,
                       hall_number BIGINT,
                       capacity INT,
                       FOREIGN KEY (cinema_id) REFERENCES Cinemas(id),
                       UNIQUE (cinema_id, hall_number)
);

-- Show types
CREATE TABLE Show_Type (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           type VARCHAR(45),
                           additional_price DECIMAL(5,1) DEFAULT 0.0
);

-- Show times for films
CREATE TABLE Show_Time (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           film_id BIGINT,
                           hall_id BIGINT,
                           show_time DATETIME,
                           type_id BIGINT,
                           base_price DECIMAL(5,2) DEFAULT 5.00,
                           FOREIGN KEY (film_id) REFERENCES Films(id),
                           FOREIGN KEY (type_id) REFERENCES Show_Type(id),
                           FOREIGN KEY (hall_id) REFERENCES Halls(id),
                           UNIQUE (show_time, hall_id)
);

CREATE INDEX idx_show_time__hall_time ON Show_Time (hall_id, show_time);

-- Seats in each hall
CREATE TABLE Seats (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       hall_id BIGINT,
                       row_letter CHAR(1),
                       seat_number INT,
                       FOREIGN KEY (hall_id) REFERENCES Halls(id),
                       UNIQUE (hall_id, row_letter, seat_number)

);

-- Reservations table
CREATE TABLE Reservations (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              show_time_id BIGINT,
                              user_id BIGINT,
                              reservation_time DATETIME(0),
                              status ENUM('reserved', 'cancelled'),
                              final_price DECIMAL(5,2),
                              FOREIGN KEY (show_time_id) REFERENCES Show_Time(id),
                              FOREIGN KEY (user_id) REFERENCES Users(id)
);




-- Reservation_Seats table
CREATE TABLE Reservation_Seats (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              reservation_id BIGINT,
                              /*show_time_id BIGINT,*/
                              seat_id BIGINT,
                              price DECIMAL(5,2) default 5.00,
                              FOREIGN KEY (reservation_id) REFERENCES Reservations(id),
                              /*FOREIGN KEY (show_time_id) REFERENCES Show_Time(id),*/
                              FOREIGN KEY (seat_id) REFERENCES Seats(id)
                              /*UNIQUE (show_time_id, seat_id)*/
);

-- Booked_Seats table - it's a helper table - it's purpose is to check if a seat was already booked for that showTime
CREATE TABLE Booked_seats (
                              show_time_id        BIGINT NOT NULL,
                              seat_id             BIGINT NOT NULL,
                              reservation_seat_id BIGINT NOT NULL UNIQUE,
                              PRIMARY KEY (show_time_id, seat_id),
                              FOREIGN KEY (show_time_id)        REFERENCES show_time(id)        ON DELETE CASCADE,
                              FOREIGN KEY (seat_id)             REFERENCES seats(id)            ON DELETE RESTRICT,
                              FOREIGN KEY (reservation_seat_id) REFERENCES reservation_seats(id) ON DELETE CASCADE
);




