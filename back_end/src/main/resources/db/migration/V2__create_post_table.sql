CREATE TABLE post (
    id VARCHAR(70) PRIMARY KEY,
    text VARCHAR(5000),
    user_id VARCHAR(70),
    FOREIGN KEY (user_id) REFERENCES user(id)
);