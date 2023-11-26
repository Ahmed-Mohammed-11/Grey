CREATE TABLE user (
id BINARY(16) PRIMARY KEY,
username VARCHAR(20) NOT NULL UNIQUE,
email VARCHAR(70) NOT NULL UNIQUE,
password VARCHAR(70) NOT NULL,
role VARCHAR(70),
tier VARCHAR(70),
avatar_id BINARY(16),
enabled TINYINT(1),
authenticated TINYINT(1),
registration_confirmation_code VARCHAR(20),
FOREIGN KEY (avatar_id) REFERENCES avatar(id)
);