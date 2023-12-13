CREATE TABLE saved_post(
    user_id VARCHAR(70),
    post_id BINARY(16),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (post_id) REFERENCES post (id),
    PRIMARY KEY (user_id, post_id)
);