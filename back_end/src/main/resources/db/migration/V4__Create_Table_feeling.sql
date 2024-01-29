CREATE TABLE post_feelings (
    post_id VARCHAR(70),
    feeling ENUM('DISGUST', 'HAPPY', 'SAD', 'FEAR', 'ANGER', 'ANXIOUS', 'LOVE', 'INSPIRE'),
    PRIMARY KEY (post_id, feeling),
    FOREIGN KEY (post_id) REFERENCES post(id)
);