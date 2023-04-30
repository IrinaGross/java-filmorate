-- DROP SCHEMA "filmorate" CASCADE;

CREATE SCHEMA IF NOT EXISTS "filmorate";

CREATE TYPE IF NOT EXISTS "filmorate"."friend_status" AS ENUM (
  'APPROVED',
  'NOT_APPROVED'
);

CREATE TABLE IF NOT EXISTS "filmorate"."film" (
  "film_id" long PRIMARY KEY NOT NULL AUTO_INCREMENT,
  "film_name" text NOT NULL,
  "film_desc" varchar(200) NOT NULL,
  "film_duration" long NOT NULL,
  "film_release_date" timestamp NOT NULL,
  "film_mpa" long
);

CREATE TABLE IF NOT EXISTS "filmorate"."mpa" (
  "mpa_id" long PRIMARY KEY NOT NULL AUTO_INCREMENT,
  "mpa_name" text NOT NULL
);

CREATE TABLE IF NOT EXISTS "filmorate"."genre" (
  "genre_id" long PRIMARY KEY NOT NULL AUTO_INCREMENT,
  "genre_name" text NOT NULL
);

CREATE TABLE IF NOT EXISTS "filmorate"."film_genre" (
  "film_id" long NOT NULL,
  "genre_id" long NOT NULL,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE IF NOT EXISTS "filmorate"."user" (
  "user_id" long PRIMARY KEY NOT NULL AUTO_INCREMENT,
  "user_name" text NOT NULL,
  "user_email" text NOT NULL,
  "user_login" text NOT NULL,
  "user_birthday" timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS "filmorate"."user_film" (
  "user_id" long NOT NULL,
  "film_id" long NOT NULL,
  PRIMARY KEY ("user_id", "film_id")
);

CREATE TABLE IF NOT EXISTS "filmorate"."friends" (
  "user_id" long NOT NULL,
  "followed_user_id" long NOT NULL,
  "friend_status" "filmorate"."friend_status" NOT NULL,
  PRIMARY KEY ("user_id", "followed_user_id")
);

COMMENT ON TABLE "filmorate"."film" IS 'Фильм';

COMMENT ON COLUMN "filmorate"."film"."film_id" IS 'Уникальный идентификатор фильма';

COMMENT ON COLUMN "filmorate"."film"."film_name" IS 'Название фильма';

COMMENT ON COLUMN "filmorate"."film"."film_desc" IS 'Описание фильма';

COMMENT ON COLUMN "filmorate"."film"."film_duration" IS 'Длительность фильма';

COMMENT ON COLUMN "filmorate"."film"."film_release_date" IS 'Дата релиза фильма';

COMMENT ON COLUMN "filmorate"."film"."film_mpa" IS 'Рейтинг ассоцииации кинокомпаний';

COMMENT ON TABLE "filmorate"."mpa" IS 'Рейтинг ассоциации кинокомпаний';

COMMENT ON COLUMN "filmorate"."mpa"."mpa_id" IS 'Уникальный идентификатор рейтинга';

COMMENT ON COLUMN "filmorate"."mpa"."mpa_name" IS 'Текстовое описание рейтинга';

COMMENT ON TABLE "filmorate"."genre" IS 'Жанр';

COMMENT ON COLUMN "filmorate"."genre"."genre_id" IS 'Уникальный идентификатор жанра';

COMMENT ON COLUMN "filmorate"."genre"."genre_name" IS 'Текстовое описание жанра';

COMMENT ON TABLE "filmorate"."film_genre" IS 'Ассоциация фильм - жанр';

COMMENT ON COLUMN "filmorate"."film_genre"."film_id" IS 'Уникальный идентификатор фильма';

COMMENT ON COLUMN "filmorate"."film_genre"."genre_id" IS 'Уникальный идентификатор жанра';

COMMENT ON COLUMN "filmorate"."user"."user_id" IS 'Уникальный идентификатор пользователя';

COMMENT ON COLUMN "filmorate"."user"."user_name" IS 'Имя пользователя';

COMMENT ON COLUMN "filmorate"."user"."user_email" IS 'Электронная почта пользователя';

COMMENT ON COLUMN "filmorate"."user"."user_login" IS 'Логин пользователя';

COMMENT ON COLUMN "filmorate"."user"."user_birthday" IS 'День рождения пользователя';

COMMENT ON TABLE "filmorate"."user_film" IS 'Понравившийся пользователю фильм';

COMMENT ON COLUMN "filmorate"."user_film"."user_id" IS 'Уникальный идентификатор пользователя';

COMMENT ON COLUMN "filmorate"."user_film"."film_id" IS 'Уникальный идентификатор фильма';

COMMENT ON COLUMN "filmorate"."friends"."user_id" IS 'Уникальный идентификатор пользователя. Инициатор дружбы';

COMMENT ON COLUMN "filmorate"."friends"."followed_user_id" IS 'Уникальный идентификатор пользователя';

COMMENT ON COLUMN "filmorate"."friends"."friend_status" IS 'Статус дружбы';

ALTER TABLE "filmorate"."film" ADD FOREIGN KEY ("film_mpa") REFERENCES "filmorate"."mpa" ("mpa_id");

ALTER TABLE "filmorate"."film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "filmorate"."film" ("film_id");

ALTER TABLE "filmorate"."film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "filmorate"."genre" ("genre_id");

ALTER TABLE "filmorate"."friends" ADD FOREIGN KEY ("user_id") REFERENCES "filmorate"."user" ("user_id");

ALTER TABLE "filmorate"."friends" ADD FOREIGN KEY ("followed_user_id") REFERENCES "filmorate"."user" ("user_id");

ALTER TABLE "filmorate"."user_film" ADD FOREIGN KEY ("user_id") REFERENCES "filmorate"."user" ("user_id");

ALTER TABLE "filmorate"."user_film" ADD FOREIGN KEY ("film_id") REFERENCES "filmorate"."film" ("film_id");

-------------------------------------- prepopulate --------------------------------------------------------------

MERGE INTO "filmorate"."mpa" ("mpa_id","mpa_name")
VALUES (1,'G'), (2,'PG'), (3,'PG-13'), (4,'R'), (5,'NC-17');

MERGE INTO "filmorate"."genre" ("genre_id","genre_name")
VALUES (1,'Комедия'), (2,'Драма'), (3,'Мультфильм'), (4,'Триллер'), (5,'Документальный'), (6,'Боевик');