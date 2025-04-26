-- create_tables.sql

CREATE DATABASE IF NOT EXISTS roommate_finder;
USE roommate_finder;

-- Users table
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    university VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    profile_picture VARCHAR(255),
    bio VARCHAR(255),
    user_type ENUM('looking_for_apartment','has_apartment') NOT NULL,
    has_preferences BIT(1),
    has_room BIT(1),
    is_active BIT(1),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    profile_image_data LONGBLOB,
    profile_image_type VARCHAR(255),
    age TINYINT UNSIGNED
);

-- User preferences
CREATE TABLE user_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    gender ENUM('MALE','FEMALE','OTHER'),
    food_pref ENUM('VEGAN','VEGETARIAN','NON_VEGETARIAN','JAIN'),
    alcohol_pref ENUM('YES','NO','OCCASIONALLY'),
    smoking TINYINT(1),
    cleanliness_rating INT,
    sleep_schedule VARCHAR(255),
    year_of_intake INT,
    lease_option ENUM('SHORT_TERM','LONG_TERM'),
    max_roommates INT,
    date_of_birth DATE,
    occupation ENUM('STUDENT','WORKING_PROFESSIONAL','OTHER'),
    party_friendly ENUM('YES','NO','SOMETIMES'),
    study_work_schedule ENUM('DAY','NIGHT','FLEXIBLE'),
    max_rent_preference INT,
    is_pet_friendly TINYINT(1) DEFAULT 0,
    interests VARCHAR(500),
    preferred_roommate_gender ENUM('MALE','FEMALE','ANY'),
    preferred_min_age INT,
    preferred_max_age INT,
    guest_frequency ENUM('RARELY','SOMETIMES','OFTEN'),
    noise_tolerance ENUM('LOW','MEDIUM','HIGH'),
    roommate_lifestyle ENUM('INTROVERT','EXTROVERT','BALANCED'),
    room_type_preference ENUM('PRIVATE','SHARED','NO_PREFERENCE'),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Room listings
CREATE TABLE room_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    rent INT NOT NULL,
    rent_negotiable TINYINT(1),
    utilities_cost INT,
    sublet_available TINYINT(1),
    transport_options VARCHAR(255),
    num_beds INT,
    current_occupancy INT,
    total_capacity INT,
    available_from DATE,
    video_url VARCHAR(255),
    description TEXT,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    image_filename VARCHAR(255),
    image_data LONGBLOB,
    image_type VARCHAR(100),
    num_baths FLOAT,
    square_feet INT,
    is_furnished TINYINT(1) DEFAULT 0,
    has_parking TINYINT(1) DEFAULT 0,
    has_laundry TINYINT(1) DEFAULT 0,
    pets_allowed TINYINT(1) DEFAULT 0,
    nearby_universities VARCHAR(255),
    available_until DATE,
    distance_to_university INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Preferred areas per preference
CREATE TABLE preferred_areas (
    preference_id BIGINT,
    preferred_area ENUM('JAMAICA_PLAIN','FENWAY','KENMORE','HUNTINGTON_AVENUE'),
    FOREIGN KEY (preference_id) REFERENCES user_preferences(id) ON DELETE CASCADE
);

-- Languages known per preference
CREATE TABLE languages_known (
    preference_id BIGINT,
    language VARCHAR(255),
    FOREIGN KEY (preference_id) REFERENCES user_preferences(id) ON DELETE CASCADE
);

-- Messaging between users
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id CHAR(36) NOT NULL,
    recipient_id CHAR(36) NOT NULL,
    room_id BIGINT,
    content TEXT NOT NULL,
    sent_at DATETIME NOT NULL,
    is_read TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (recipient_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES room_info(id) ON DELETE SET NULL
);

-- Indexes for faster lookups
CREATE INDEX idx_messages_sender    ON messages(sender_id);
CREATE INDEX idx_messages_recipient ON messages(recipient_id);
CREATE INDEX idx_messages_room      ON messages(room_id);
