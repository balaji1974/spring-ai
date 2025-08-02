drop table IF EXISTS netflix_shows;
drop table IF EXISTS TBL_ACCOUNT;
drop table IF EXISTS TBL_USER;

CREATE TABLE netflix_shows (
    show_id text NOT NULL,
    type text,
    title text,
    director text,
    cast_members text,
    country text,
    date_added date,
    release_year integer,
    rating text,
    duration text,
    listed_in text,
    description text
);

create table TBL_USER (
    id int not null,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table TBL_ACCOUNT (
    id int not null PRIMARY KEY,
    accountNumber varchar(255) not null,
    user_id int not null,
    balance decimal(10, 2) not null,
    openDate date not null,
    foreign key (user_id) references TBL_USER(id)
);