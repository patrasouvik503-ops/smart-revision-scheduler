# Smart Revision Scheduler

A full-stack spaced-repetition planner for students. Add a topic once, and the app automatically creates revision dates for Day 1, Day 3, Day 7, Day 14, Day 30, Day 60, and Day 90.

## Tech Stack

- Frontend: React, Bootstrap, Vite
- Backend: Java 21, Spring Boot, Spring Data JPA
- Database: MySQL
- Notifications: Browser Notifications

## Project Structure

```text
Smart-Revision-Scheduler
├── backend
│   ├── pom.xml
│   └── src/main/java/com/smartrevision/scheduler
├── frontend
│   ├── package.json
│   └── src
├── database
│   └── schema.sql
└── README.md
```

## Run Locally

### 1. Create the MySQL database

```sql
CREATE DATABASE smart_revision_scheduler;
```

Then run:

```bash
mysql -u root -p smart_revision_scheduler < database/schema.sql
```

### 2. Configure backend database credentials

Edit `backend/src/main/resources/application.properties` if your MySQL username or password is different. You can also set `MYSQL_USER` and `MYSQL_PASSWORD`. An example config is available at `backend/src/main/resources/application-example.properties`.

### 3. Start the backend

```bash
cd backend
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

### 4. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

The app runs at `http://localhost:5173`.

## Production

See `DEPLOYMENT.md` for SEO, email OTP setup, hosting, and traffic-scaling notes.

## API Summary

- `POST /api/auth/register/request-otp` - send OTP before account creation
- `POST /api/auth/register/verify` - verify OTP, create account, and receive access token
- `POST /api/auth/login` - login with email and password
- `POST /api/auth/google` - login or create account with Google
- `POST /api/auth/password/request-reset` - send OTP for forgot-password flow
- `POST /api/auth/password/reset` - verify OTP and set a new password
- `GET /api/dashboard` - dashboard stats and today/upcoming revisions
- `GET /api/revisions/today` - today's revision list
- `GET /api/revisions/calendar?from=2026-06-01&to=2026-07-31` - revisions in a date range
- `POST /api/topics` - add a topic and auto-generate revision dates
- `PATCH /api/revisions/{id}/complete` - mark a revision complete

## Authentication Flow

1. New user enters name, email, and password.
2. App sends an OTP to verify the email.
3. User enters OTP, account is created, and the password is stored as a BCrypt hash.
4. Next login uses email and password only.
5. If the user forgets the password, the app sends a reset OTP to the email and then allows setting a new password.

## Google Login Setup

1. In Google Cloud Console, create/select a project.
2. Configure OAuth consent screen.
3. Create an OAuth Client ID with application type `Web application`.
4. Add authorized JavaScript origins:

```text
http://localhost:5173
https://your-domain.com
```

5. Copy the Client ID.
6. Backend PowerShell before `mvn spring-boot:run`:

```powershell
$env:GOOGLE_CLIENT_ID="your_google_client_id"
```

7. Create `frontend/.env`:

```text
VITE_GOOGLE_CLIENT_ID=your_google_client_id
```

8. Restart both backend and frontend.

Example topic request:

```json
{
  "topicName": "SQL Injection",
  "subject": "Cyber Security",
  "difficulty": "MEDIUM",
  "dateLearned": "2026-06-28"
}
```

## Resume Talking Points

- Built REST APIs with Spring Boot and layered architecture.
- Used MySQL relational design with topics and generated revision schedules.
- Implemented spaced-repetition logic using Java `LocalDate`.
- Created a React dashboard with calendar, progress stats, and browser notifications.
- Added email verification, BCrypt password hashing, forgot-password OTP reset, and user-scoped revision data.

## Note

The scheduler follows the algorithm in the brief exactly: `{1, 3, 7, 14, 30, 60, 90}` days after the learned date. For a topic learned on 28 June 2026, Day 3 is 1 July 2026.
