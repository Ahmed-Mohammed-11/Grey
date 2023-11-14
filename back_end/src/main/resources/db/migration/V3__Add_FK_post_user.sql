ALTER TABLE post
ADD CONSTRAINT fk_post_user
FOREIGN KEY (user_id)
REFERENCES user(id);