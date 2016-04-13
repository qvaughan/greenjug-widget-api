# --- !Ups

CREATE TABLE Widget (
  id varbinary(16) primary key,
  make varchar(255),
  model varchar(255)
);

# --- !Downs

DROP TABLE Widget;