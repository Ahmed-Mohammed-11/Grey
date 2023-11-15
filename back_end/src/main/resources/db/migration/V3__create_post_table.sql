CREATE TABLE post (
    id BINARY(16) PRIMARY KEY,
    text VARCHAR(5000),
    user_id BINARY(16),
    FOREIGN KEY (user_id) REFERENCES grey_user(id)
);