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
                       email VARCHAR(255),
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
                       hall_number INT,
                       capacity INT
);

-- Show types (e.g., 3D, IMAX)
CREATE TABLE Show_Type (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           type VARCHAR(45),
                           additional_price DECIMAL(5,2)
);

-- Show times for films
CREATE TABLE Show_Time (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           film_id BIGINT,
                           cinema_id BIGINT,
                           show_time DATETIME,
                           type_id BIGINT,
                           hall_id BIGINT,
                           base_price DECIMAL(5,2) DEFAULT 5.00,
                           FOREIGN KEY (film_id) REFERENCES Films(id),
                           FOREIGN KEY (cinema_id) REFERENCES Cinemas(id),
                           FOREIGN KEY (type_id) REFERENCES Show_Type(id),
                           FOREIGN KEY (hall_id) REFERENCES Halls(id),
                           UNIQUE (show_time, hall_id)
);

-- Seats in each hall
CREATE TABLE Seats (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       hall_id BIGINT,
                       row_letter CHAR(1),
                       seat_number INT,
                       FOREIGN KEY (hall_id) REFERENCES Halls(id)
);

-- Reservations table
CREATE TABLE Reservations (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              show_time_id BIGINT,
                              seat_id BIGINT,
                              user_id BIGINT,
                              reservation_time DATETIME,
                              status ENUM('reserved', 'cancelled'),
                              FOREIGN KEY (show_time_id) REFERENCES Show_Time(id),
                              FOREIGN KEY (seat_id) REFERENCES Seats(id),
                              FOREIGN KEY (user_id) REFERENCES Users(id),
                              UNIQUE (show_time_id, seat_id)
);



