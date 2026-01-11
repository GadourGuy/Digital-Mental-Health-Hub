-- 1. Create Mood Entries (Using INT to match 'int(11)')
CREATE TABLE IF NOT EXISTS mood_entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT(11) NOT NULL,             -- Matches your 'userID' type exactly
    mood VARCHAR(50),
    date DATETIME,
    -- Reference the correct column name 'userID'
    CONSTRAINT fk_mood_user FOREIGN KEY (user_id) REFERENCES users(userID) 
) ENGINE=InnoDB;

-- 2. Create Activity Logs (Using INT to match 'int(11)')
CREATE TABLE IF NOT EXISTS activity_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT(11) NOT NULL,             -- Matches your 'userID' type exactly
    type VARCHAR(50),
    title VARCHAR(255),
    status VARCHAR(50),
    date DATETIME,
    -- Reference the correct column name 'userID'
    CONSTRAINT fk_activity_user FOREIGN KEY (user_id) REFERENCES users(userID)
) ENGINE=InnoDB;

-- Replace '1' with a valid userID from your users table
INSERT INTO mood_entries (user_id, mood, date) VALUES 
(1, 'Happy', NOW()),
(1, 'Stressed', NOW() - INTERVAL 1 DAY),
(1, 'Calm', NOW() - INTERVAL 2 DAY),
(1, 'Happy', NOW() - INTERVAL 3 DAY),
(1, 'Focused', NOW() - INTERVAL 4 DAY);

INSERT INTO activity_logs (user_id, type, title, status, date) VALUES 
(1, 'MOOD', 'Logged mood: Happy', 'COMPLETED', NOW()),
(1, 'EXERCISE', 'Completed: Mindfulness', 'COMPLETED', NOW() - INTERVAL 5 HOUR),
(1, 'READING', 'Read: Managing Exam Stress', 'COMPLETED', NOW() - INTERVAL 1 DAY);