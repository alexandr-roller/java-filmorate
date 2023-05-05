drop table IF EXISTS FILM_GENRES;
drop table IF EXISTS FILM_LIKES;
drop table IF EXISTS FILMS;
drop table IF EXISTS FRIENDS;
drop table IF EXISTS GENRES;
drop table IF EXISTS MPA;
drop table IF EXISTS USERS;

create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER,
    MPA_NAME CHARACTER VARYING(50),
    constraint MPA
        primary key (MPA_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(50),
    DESCRIPTION  CHARACTER VARYING(255),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint "FILMS_MPA_MPA_ID_fk"
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER,
    GENRE_NAME CHARACTER VARYING(50),
    constraint GENRES
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILM_GENRES_GENRES_GENRES_ID_fk"
        foreign key (GENRE_ID) references GENRES
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     CHARACTER VARYING(50),
    LOGIN     CHARACTER VARYING(50),
    USER_NAME CHARACTER VARYING(50),
    BIRTHDAY  DATE,
    constraint "USERS_pk"
        primary key (USER_ID)
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "FILM_LIKES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILM_LIKES_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint "FRIENDS_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS,
    constraint "FRIENDS_USERS_USER_ID_fk2"
        foreign key (FRIEND_ID) references USERS
);