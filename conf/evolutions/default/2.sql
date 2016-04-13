# --- !Ups

ALTER TABLE Widget ADD COLUMN color varchar(255);

# --- !Downs

ALTER TABLE Widget DROP COLUMN color;