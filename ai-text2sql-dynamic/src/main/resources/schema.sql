
drop table IF EXISTS TBL_ACCOUNT;
drop table IF EXISTS TBL_USER;

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