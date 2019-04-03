DROP TABLE IF EXISTS person;

CREATE TABLE person
(
    id         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name  VARCHAR(50) NOT NULL
);

INSERT INTO person
VALUES (1, 'Rick', 'Sanchez'),
       (2, 'Morty', 'Smith'),
       (3, 'Summer', 'Smith'),
       (4, 'Jerry', 'Smith'),
       (5, 'Beth', 'Smith');