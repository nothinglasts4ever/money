DROP TABLE IF EXISTS person;

CREATE TABLE person
(
    id         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name  VARCHAR(50) NOT NULL
);