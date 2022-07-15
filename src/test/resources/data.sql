MERGE INTO "mpa" (ID, NAME)
VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

MERGE INTO "genre" (GENRE_ID, NAME)
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

MERGE INTO "film" (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)
VALUES
(1, 'Матрица', 'Добро пожаловать в реальный мир', '1999-03-24', 136, 4, 4),
(2, 'Форрет Гамп', 'Полувековая история США глазами чудака из Алабамы.', '1994-06-23', 142, 4, 3),
(3, '2001 год: Космическая одиссея', 'the time is now', '1968-04-2', 149, 4, 1);

MERGE  INTO "user"  (USER_ID,  NAME, EMAIL, LOGIN, BIRTHDAY)
VALUES 
(1, 'Антон', 'anton@mail.ru', 'anton', '2000-01-01'),
(2, 'Андрей', 'andrey@mail.ru', 'andrey', '1990-01-01'),
(3, 'Юля', 'ulya@mail.ru', 'ulya', '1980-01-01'),
(4, 'Света', 'sveta@mail.ru', 'sveta', '1970-01-01');


MERGE INTO "film_genre" (FILM_ID, GENRE_ID)
VALUES (1, 6),
       (2, 2),
       (3, 4);

MERGE INTO "like" (FILM_ID, USER_ID)
VALUES 
(1, 1), (2, 2), (1, 2);

MERGE INTO "friendship" (SENDER_ID, RECIPIENT_ID, STATUS)
VALUES 
(1, 2, 'неподтверждённая'), (1, 3, 'подтверждённая');
