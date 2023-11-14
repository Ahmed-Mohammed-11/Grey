INSERT INTO grey_user (id, username, email, password)
VALUES
  (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')), 'john_doe', 'john@example.com', 'password123'),
  (UNHEX(REPLACE('123e4567-e89b-12d3-a456-426614174001', '-', '')), 'jane_doe', 'jane@example.com', 'securepass');


INSERT INTO post (text, user_id)
VALUES
  ('This is the first post.', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', ''))),
  ('Another post from a different user.', UNHEX(REPLACE('123e4567-e89b-12d3-a456-426614174001', '-', '')));
