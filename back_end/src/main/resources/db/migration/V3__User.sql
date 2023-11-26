CREATE TABLE user
(
    id            BINARY(16) PRIMARY KEY,
    username      VARCHAR(20) NOT NULL UNIQUE,
    email         VARCHAR(70) NOT NULL UNIQUE,
    registration_type VARCHAR(10) NOT NULL,
    role          VARCHAR(70),
    tier          VARCHAR(70),
    avatar_id     BINARY(16),
    enabled       TINYINT(1),
    authenticated TINYINT(1),
    FOREIGN KEY (avatar_id) REFERENCES avatar (id)
);

CREATE TABLE user_google_oauth
(
    local_id    BINARY(16) PRIMARY KEY,
    external_id VARCHAR(70) NOT NULL,
    FOREIGN KEY (local_id) REFERENCES user (id)
);

CREATE TABLE user_basic_auth
(
    local_id BINARY(16) PRIMARY KEY,
    password VARCHAR(70) NOT NULL,
    FOREIGN KEY (local_id) REFERENCES user (id)
);