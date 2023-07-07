delete from likes;

delete from friends;

delete from films;
alter table films alter column film_id restart with 1;
insert into films (name, description, release_date, duration, mpa_mpa_id) values
('Film 1', 'Film 1', '1990-01-01', 100, 1),
('Film 2', 'Film 2', '1990-01-01', 100, 1);

delete from users;
alter table users alter column user_id restart with 1;
insert into users (email, login, name, birthday) values
('test1@test.ru', 'test1', 'test1', '1976-09-20'),
('test2@test.ru', 'test2', 'test2', '1976-09-20');

insert into likes (film_film_id, user_user_id) values
(1, 1);

insert into friends (first_user_id, second_user_id, frstat_frstat_id) values
(1, 2, 2);
