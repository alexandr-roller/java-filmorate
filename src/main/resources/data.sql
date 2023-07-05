--delete from mpa;
--alter table mpa alter column mpa_id restart with 1;
merge into mpa key(mpa_id) values
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

--delete from genres;
--alter table genres alter column genre_id restart with 1;
merge into genres key(genre_id) values
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

--delete from friend_statuses;
--alter table friend_statuses alter column frstat_id restart with 1;
merge into friend_statuses key(frstat_id) values
(1, 'Неподтвержден'),
(2, 'Подтвержден');

merge into event_types key(evtp_id) values
(1, 'LIKE'),
(2, 'REVIEW'),
(3, 'FRIEND');

merge into operation_types key(optp_id) values
(1, 'REMOVE'),
(2, 'ADD'),
(3, 'UPDATE');