# java-filmorate

## ER diagram

Диаграмма сущность - связь отображена на рисунке ниже

![Diagram](media/filmorate%20ER-2.png)
- Согласно [1NF](https://info-comp.ru/first-normal-form) в таблицах не будет дублирующихся строк, значения ячеек атомарны и явно типизированны.
- Согласно [2NF](https://info-comp.ru/second-normal-form) в таблицах имеются столбец PK, а неключевые столбцы зависят от него.
- Согласно [3NF](https://info-comp.ru/third-normal-form) в таблицах неключевые столбцы не пытаются играть роль ключа.
- Связь между фильмом и жанром - многие ко многим **(Один фильм может иметь нестолько жанров; Один жанр может содержаться в нескольких фильмах)**. Реализована через вспомогательную таблицу.
- Связь между пользователем и фильмом - многие ко многим **(Один пользователь может отметить несколько фильмов; Один фильм может быть отмечен несколькими пользователями)**. Реализована через вспомогательную таблицу.
- Статус дружбы *(friend_status)* определен перечислением

![Enums](media/filmorate%20ER-3.png)

### Типовые запросы

Для фильмов:
* Получить все фильмы 
```sql
select * from filmorate.film
```
* Получить фильм по идентификатору
```sql
select * from filmorate.film where film_id = $1
```
* Добавить фильм
```sql
insert into filmorate.film (film_name, film_desc, film_duration, film_release_date, film_mpa) 
values ($1, $2, $3, $4, $5) 
```
* Обновить фильм
```sql
update filmorate.film
set film_name = $2
where film_id = $1
```

Для пользователей:
* Получить всех пользователей
```sql
select * from filmorate.user
```
* Получить пользователя по идентификатору
```sql
select * from filmorate.user where user_id = $1
```
* Добавить пользователя
```sql
insert into filmorate.user (user_name, user_email, user_login, user_birthday) 
values ($1, $2, $3, $4) 
```
* Обновить пользователя
```sql
update filmorate.user
set user_name = $2
where user_id = $1
```

Для понравившихся фильмов
* Добавить понравившийся фильм
```sql
insert into filmorate.user_film (user_id, film_id)
values ($1, $2)
```
* Удалить понравившийся фильм
```sql
delete from filmorate.user_film where user_id = $1 and film_id = $2
```

etc...