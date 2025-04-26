-- view_tables.sql

USE roommate_finder;

-- List all tables
SHOW TABLES;

-- Show structure of each table
DESCRIBE users;
DESCRIBE user_preferences;
DESCRIBE room_info;
DESCRIBE preferred_areas;
DESCRIBE languages_known;
DESCRIBE messages;

-- Preview data (first 10 rows)
SELECT * FROM users            LIMIT 10;
SELECT * FROM user_preferences LIMIT 10;
SELECT * FROM room_info        LIMIT 10;
SELECT * FROM preferred_areas  LIMIT 10;
SELECT * FROM languages_known  LIMIT 10;
SELECT * FROM messages         LIMIT 10;
