CREATE TABLE saved_post(
user_id BINARY(16),
post_id INT,
FOREIGN KEY (user_id) REFERENCES grey_user(id),
FOREIGN KEY (post_id) REFERENCES post(id)
);