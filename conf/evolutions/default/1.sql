# --- !Ups
CREATE TABLE tasks (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title text NOT NULL,
  completed boolean NOT NULL,
  priority text NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE tasks;