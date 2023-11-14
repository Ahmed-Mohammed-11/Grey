CREATE TABLE post (
    id INT PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(5000),
    user_id BINARY(16),
    FOREIGN KEY (user_id) REFERENCES grey_user(id)
);