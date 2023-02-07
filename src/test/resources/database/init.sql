CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       name varchar(255) not null
);

INSERT INTO books (name) VALUES ('Book 1');
INSERT INTO books (name) VALUES ('Book 2');
INSERT INTO books (name) VALUES ('Book Ndr');