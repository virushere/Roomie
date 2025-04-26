# Roommate Finder

**Roommate Finder** is a web application designed to help students and working professionals find compatible roommates or list available rooms/apartments. It supports detailed user profiles, preference matching, rich room listings, and in-app messaging.

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Database Schema](#database-schema)
4. [Setup & Installation](#setup--installation)
5. [Scripts](#scripts)
6. [Usage](#usage)
7. [Contributing](#contributing)
8. [License](#license)
9. [Future Prospects](#future-prospects)

---

## Features

- **User Profiles**: Register as a seeker or lister, with extended profile data (age, bio, university).
- **Preferences**: Specify roommate preferences (gender, smoking, noise tolerance, lifestyle, etc.).
- **Room Listings**: Post rooms/apartments with detailed info (rent, utilities, beds, amenities, availability).
- **Preferred Areas & Languages**: Store multiple preferred neighborhoods and spoken languages per user.
- **Messaging**: One-to-one chats between users, with read/unread tracking.
- **Media Support**: Upload profile images and room photos/videos.

---

## Tech Stack

- **Backend**: MySQL 8.x
- **Language**: SQL, Java (Spring Boot optional)
- **Frontend**: JSP (Can be modified to React, Angular, etc.)
- **Hosting**: Any MySQL-compatible host or local Docker setup.

---

## Database Schema

### Tables

1. **users**: Stores core user data, authentication, and status flags.
2. **user\_preferences**: Detailed roommate and lifestyle preferences.
3. **room\_info**: Room/apartment listings with location, price, and amenities.
4. **preferred\_areas**: Many‑to‑one mapping for neighborhoods a user prefers.
5. **languages\_known**: Spoken languages per user preference.
6. **messages**: In-app messaging conversation history.

Refer to the `create_tables.sql` script for full definitions, including all columns, data types, and foreign keys.

---

## Setup & Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/virushere/Roomie.git
   cd roommate-finder
   ```

2. **Configure MySQL**:

   - Ensure MySQL 8.x is installed and running.
   - Create a user/database or update credentials in your application config.

3. **Run SQL scripts**:

   ```bash
   mysql -u <username> -p < create_tables.sql
   mysql -u <username> -p < view_tables.sql
   ```

4. **Start Backend**:

   - If using Spring Boot: `mvn spring-boot:run`
   - Otherwise, start your chosen server.

5. **Launch Frontend**:

     - No Additional steps required for JSP pages to start.

---

## Scripts

- **create\_tables.sql**: Builds the full database schema, including all tables, indexes, and foreign keys.
- **view\_tables.sql**: Lists and describes tables; previews sample rows for quick verification.

---

## Usage

1. **Register** as a new user (seeker or lister).
2. **Complete** your profile and preferences.
3. **Browse** or **post** room listings.
4. **Match** based on preferences and send messages.

---

## Contributing

Contributions are welcome! Please fork the repo, create a feature branch, and open a pull request. Ensure new SQL changes are reflected in the scripts.

---

## License

This project is licensed under the MIT License. See `LICENSE` for details.

## Future Prospects

- Mobile App: Develop iOS/Android clients for on-the-go matching and messaging.

- AI Matching: Leverage machine learning to suggest optimal roommate pairings based on behavior and preferences.

- Payment Integration: Add rent payment and utility split features within the platform.

- Social Features: Enable user reviews, ratings, and community forums for local insights.

- Campus Partnerships: Integrate with university housing systems to streamline official housing listings.

- Data Analytics: Provide users with market trends, price heatmaps, and neighborhood insights.

- Internationalization: Support multiple languages and regional preferences for global expansion.
