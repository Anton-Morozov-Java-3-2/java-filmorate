# Filmorate application
### ER - диаграмма
![ER-Диаграмма!](/media/images/ER-DB-Filmorate.png "Диаграмма зависимостей")

### Опимание моделей данных
1. film - таблица с данными о фильмах
2. user - таблица с данными о пользователях
3. genre - таблица с жанрами
4. film_genre - таблица связи фильма и жанра
5. like - таблица связи фильмов и лайков пользователй
6. friendship - таблица связци заявок на дружбу пользоватей (sender - пользователь отправляющий заявку на дружбу, recipient - пользователь принимающий заявку)
7. mpa - таблица с рейтингами

### Примеры основных запросов к базе данных
****
Получение списка всех фильмов:
~~~
SELECT *
FROM film
~~~
Получение списка названий 20 самых популярных фильмов:
~~~
SELECT f.film_id, 
       f.name, 
       COUNT(f.film_id) likes_count
FROM film as f
LEFT JOIN like as l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY likes_count DESC
LIMIT 20
~~~
Получение списка пользователей:
~~~
SELECT *
FROM user
~~~
Получение списка id друзей пользователя с id = 1:
~~~
SELECT f.sender_id  
FROM friendship as f
WHERE f.recipient_id = 1
UNIT
SELECT f.recipient_id friends  
FROM friendship as f
WHERE f.sender_id = 1
~~~

