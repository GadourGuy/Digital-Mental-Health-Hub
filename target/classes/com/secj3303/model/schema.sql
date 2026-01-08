
-- User table
CREATE TABLE users (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Content Category table
CREATE TABLE content_category (
    categoryID INT PRIMARY KEY AUTO_INCREMENT,
    content_title VARCHAR(255) NOT NULL
);

-- Sub Contents table
CREATE TABLE sub_contents (
    contentID INT PRIMARY KEY AUTO_INCREMENT,
    content_title VARCHAR(255) NOT NULL,
    contentCategoryID INT NOT NULL,
    description TEXT,
    contentURL VARCHAR(500),
    FOREIGN KEY (contentCategoryID) REFERENCES content_category(categoryID) ON DELETE CASCADE
);

-- Completed Content table (tracks user progress)
CREATE TABLE completed_content (
    contentID INT NOT NULL,
    contentCategoryID INT NOT NULL,
    userID INT NOT NULL,
    completion_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (contentID, userID),
    FOREIGN KEY (contentID) REFERENCES sub_contents(contentID) ON DELETE CASCADE,
    FOREIGN KEY (contentCategoryID) REFERENCES content_category(categoryID) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Forum Post table
CREATE TABLE forum_post (
    postID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Post Likes table
CREATE TABLE post_likes (
    postID INT NOT NULL,
    userID INT NOT NULL,
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (postID, userID),
    FOREIGN KEY (postID) REFERENCES forum_post(postID) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Post Comments table
CREATE TABLE post_comments (
    commentID INT PRIMARY KEY AUTO_INCREMENT,
    postID INT NOT NULL,
    userID INT NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (postID) REFERENCES forum_post(postID) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Quiz Questions table
CREATE TABLE quiz_questions (
    quizID INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    userID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Quiz Answers table
CREATE TABLE quiz_answers (
    answerID INT PRIMARY KEY AUTO_INCREMENT,
    quizID INT NOT NULL,
    answer_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (quizID) REFERENCES quiz_questions(quizID) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO users (name, email, role, password) VALUES
('John Doe', 'john@example.com', 'student', 'password123'),
('Jane Smith', 'jane@example.com', 'admin', 'password456');

INSERT INTO content_category (content_title) VALUES
('Java Programming'),
('Database Fundamentals');

INSERT INTO sub_contents (content_title, contentCategoryID, description, contentURL) VALUES
('Introduction to Java', 1, 'Basic Java concepts', 'http://example.com/java-intro'),
('SQL Basics', 2, 'Introduction to SQL', 'http://example.com/sql-basics');