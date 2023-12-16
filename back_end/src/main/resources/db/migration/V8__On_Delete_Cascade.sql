ALTER TABLE post_feelings
ADD CONSTRAINT fk_post_feelings_post_id
FOREIGN KEY (post_id)
REFERENCES post(id)
ON DELETE CASCADE;


ALTER TABLE reported_posts
ADD CONSTRAINT fk_reported_posts_post_id
FOREIGN KEY (post_id)
REFERENCES post(id)
ON DELETE CASCADE;

ALTER TABLE saved_post
ADD CONSTRAINT fk_saved_post_post_id
FOREIGN KEY (post_id)
REFERENCES post(id)
ON DELETE CASCADE;
