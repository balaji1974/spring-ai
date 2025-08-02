drop table IF EXISTS passenger;

CREATE TABLE passenger (
    passengerid integer NOT NULL,
    survived double precision,
    pclass integer,
    name text,
    sex text,
    age double precision,
    sibsp integer,
    parch integer,
    ticket text,
    fare double precision,
    cabin text,
    embarked text,
    wikiid double precision,
    name_wiki text,
    age_wiki double precision,
    hometown text,
    boarded text,
    destination text,
    lifeboat text,
    body text,
    class integer
);

ALTER TABLE ONLY passenger
    ADD CONSTRAINT passenger_pkey PRIMARY KEY (passengerid);