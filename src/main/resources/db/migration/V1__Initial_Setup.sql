DROP TABLE IF EXISTS account;

CREATE TABLE account (
    id        INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50),
    active    BOOLEAN,
    balance   DECIMAL(20, 2)
);