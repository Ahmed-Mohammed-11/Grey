CREATE TABLE reported_posts (
    post_id     BINARY(16),
    reporter_id VARCHAR(70),
    PRIMARY KEY (post_id, reporter_id),
    FOREIGN KEY (post_id) REFERENCES post (id),
    FOREIGN KEY (reporter_id) REFERENCES user (id)
);