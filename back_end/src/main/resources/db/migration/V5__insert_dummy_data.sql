INSERT INTO grey_user (id, username, email, password)
VALUES
  (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')), 'john_doe', 'john@example.com', 'password123'),
  (UNHEX(REPLACE('123e4567-e89b-12d3-a456-426614174001', '-', '')), 'jane_doe', 'jane@example.com', 'securepass');


INSERT INTO post (id, text, user_id)
VALUES (
    UNHEX(REPLACE('a0c8a56e-c212-4a65-bfdb-50a68cf916df', '-', '')),
    'This is the text of the second post.',
    UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', ''))
),
(
    UNHEX(REPLACE('29669647-dde3-45c4-82df-0d020b4ee022', '-', '')),
    'This is the text of the post.',
    UNHEX(REPLACE('123e4567-e89b-12d3-a456-426614174001', '-', ''))
);
