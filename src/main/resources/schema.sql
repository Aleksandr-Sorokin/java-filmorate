DROP TABLE IF EXISTS FILMS_RATINGS, FILMS, GENRES, FILMS_GENRES, USERS, FILMS_LIKES, USERS_FRIENDS;
create table IF NOT EXISTS FILMS_RATINGS
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING(50),
    constraint FILMS_RATINGS_PK
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID           INTEGER auto_increment,
    FILM_NAME         CHARACTER VARYING(50)  not null,
    FILM_DESCRIPTION  CHARACTER VARYING(200) not null,
    FILM_RELEASE_DATE DATE                   not null,
    FILM_DURATION     INTEGER                not null,
    FILM_RATING       INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_FILMS_RATINGS_RATING_ID_FK
        foreign key (FILM_RATING) references FILMS_RATINGS
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50),
    constraint FILMS_GENRES_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILMS_GENRES
(
    FILM_ID    INTEGER,
    GENRE_FILM INTEGER,
    constraint FILMS_GENRES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILMS_GENRES_GENRES_GENRE_ID_FK
        foreign key (GENRE_FILM) references GENRES
);

create table IF NOT EXISTS USERS
(
    USER_ID       INTEGER auto_increment,
    USER_NAME     CHARACTER VARYING(50),
    USER_BIRTHDAY DATE                  not null,
    USER_LOGIN    CHARACTER VARYING(50) not null,
    USER_EMAIL    CHARACTER VARYING(50),
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FILMS_LIKES
(
    USER_ID INTEGER,
    FILM_ID INTEGER,
    constraint FILMS_LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILMS_LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS USERS_FRIENDS
(
    USER_ID           INTEGER,
    FRIEND_ID         INTEGER,
    STATUS_FRIENDSHIP BOOLEAN default FALSE,
    USER_FRIEND_ID    INTEGER auto_increment,
    constraint USERS_FRIENDS_PK
        primary key (USER_FRIEND_ID),
    constraint USERS_FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint USERS_FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);



