CREATE TABLE IF NOT EXISTS "mpa"
(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL
); -- ������� ���������

CREATE UNIQUE INDEX IF NOT EXISTS GENRE_UNIQUE ON "mpa" (name);

CREATE TABLE IF NOT EXISTS "film"
(
    film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,    
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,  
    duration INT NOT NULL,
    rate INT NOT NULL,
    mpa_id INT REFERENCES "mpa" (id)
); -- ������� �������

CREATE TABLE IF NOT EXISTS "user"
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,    
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    login VARCHAR(100) NOT NULL,    
    birthday DATE NOT NULL          
); -- ������� �������������

CREATE TABLE IF NOT EXISTS "genre"
(
    genre_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,    
    name VARCHAR(100) NOT NULL    
); -- ������� ������

CREATE UNIQUE INDEX IF NOT EXISTS GENRE_UNIQUE ON "genre" (name);

CREATE TABLE IF NOT EXISTS "film_genre"
(
    film_id BIGINT REFERENCES "film" (film_id),
    genre_id BIGINT REFERENCES "genre" (genre_id),
    PRIMARY KEY(film_id, genre_id)
); -- ������� ������������ ������ ������

CREATE TABLE IF NOT EXISTS "like"
(
    film_id BIGINT REFERENCES "film" (film_id),
    user_id BIGINT REFERENCES "user" (user_id),
    PRIMARY KEY(film_id, user_id)
); -- ������� ������

CREATE TABLE IF NOT EXISTS "friendship"
(
    sender_id BIGINT REFERENCES "user" (user_id),
    recipient_id BIGINT REFERENCES "user" (user_id),
    status VARCHAR(100) DEFAULT '���������������',
    PRIMARY KEY(sender_id, recipient_id)
); -- ������� ������ � ������





