# Smart Revision Scheduler - Project Documentation

## 1. Project Overview

Smart Revision Scheduler is a full-stack spaced-repetition web application. The user adds a topic once, selects subject, difficulty, and learned date, and the system automatically creates revision dates.

Revision schedule:

```text
Day 1
Day 3
Day 7
Day 14
Day 30
Day 60
Day 90
```

Main purpose:

- Help students remember topics for a longer time.
- Automatically generate revision reminders.
- Track completed revisions.
- Show dashboard progress, calendar view, statistics, and memory score.
- Support email/password login, OTP verification for account creation, password reset with OTP, and optional Google login.

## 2. Technology Stack

| Part | Technology |
|---|---|
| Frontend | React + Vite |
| Styling | CSS, Bootstrap dependency, custom responsive CSS |
| Icons | lucide-react |
| Backend | Java Spring Boot |
| Database | MySQL |
| Authentication | Custom token authentication + Spring Security password encoder |
| OTP | Email OTP system |
| Google Login | Google OAuth ID token verification |
| Deployment Files | Dockerfile, nginx.conf, render.yaml |

## 3. Main Folder Structure

Project root:

```text
C:\Users\souvi\OneDrive\Documents\New project
```

Important folders:

```text
New project
├── backend
├── frontend
├── database
├── README.md
├── DEPLOYMENT.md
├── PROJECT_DOCUMENTATION.md
├── preview-loading.html
├── preview-full.html
└── render.yaml
```

## 4. Frontend Structure

Frontend folder:

```text
frontend
├── index.html
├── package.json
├── package-lock.json
├── Dockerfile
├── nginx.conf
├── public
│   ├── robots.txt
│   ├── sitemap.xml
│   └── site.webmanifest
└── src
    ├── main.jsx
    ├── App.jsx
    ├── api.js
    └── styles.css
```

### 4.1 `frontend/src/main.jsx`

This is the React entry file. It loads the React application and renders the main `App` component into the HTML root element.

### 4.2 `frontend/src/App.jsx`

This is the main frontend file. It contains most of the application UI and screen logic.

Main responsibilities:

- Landing page
- Login page
- Registration page
- Password reset page
- Google login button logic
- Dashboard
- Add Topic page
- Calendar page
- Statistics page
- Dark mode toggle
- Sidebar navigation
- Top bar
- Floating Add Topic button
- Revision completion button visibility rules

Important frontend states:

```js
isAuthenticated
activeView
dashboard
statistics
calendarItems
loading
query
darkMode
```

`activeView` controls which page is visible:

```text
dashboard
add
calendar
statistics
```

### 4.3 `frontend/src/api.js`

This file contains all frontend API calls to the Spring Boot backend.

Base URL:

```js
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
```

Important functions:

| Function | Purpose |
|---|---|
| `requestRegistrationOtp(email)` | Sends OTP for registration |
| `completeRegistration(...)` | Verifies OTP and creates account |
| `login(email, password)` | Logs in using email and password |
| `loginWithGoogle(credential)` | Logs in using Google token |
| `requestPasswordReset(email)` | Sends OTP for password reset |
| `resetPassword(...)` | Resets password after OTP verification |
| `getDashboard()` | Loads dashboard data |
| `getStatistics()` | Loads real statistics data |
| `addTopic(topic)` | Adds topic and creates revision schedule |
| `completeRevision(id)` | Marks revision complete |
| `getCalendar(from, to)` | Loads revisions between dates |

### 4.4 `frontend/src/styles.css`

This file contains all frontend design and responsive styling.

It includes styles for:

- Landing page
- Login/register pages
- Sidebar
- Dashboard cards
- Today goal card
- Memory score progress ring
- Add Topic form
- Calendar grid
- Calendar popup
- Statistics charts
- Dark mode
- Mobile responsiveness

Main theme colors:

```css
--primary: #4f46e5;
--bg: #f8fafc;
--panel: #ffffff;
--success: #22c55e;
--warning: #f59e0b;
--danger: #ef4444;
```

Dark mode is applied using:

```css
.theme-dark
```

## 5. Backend Structure

Backend folder:

```text
backend
├── pom.xml
├── Dockerfile
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── smartrevision
    │   │           └── scheduler
    │   │               ├── SmartRevisionSchedulerApplication.java
    │   │               ├── api
    │   │               ├── auth
    │   │               ├── config
    │   │               ├── controller
    │   │               ├── revision
    │   │               ├── service
    │   │               ├── topic
    │   │               └── user
    │   └── resources
    │       ├── application.properties
    │       └── application-example.properties
    └── test
```

### 5.1 `SmartRevisionSchedulerApplication.java`

This is the main Spring Boot application class. It starts the backend server.

Run command:

```powershell
mvn spring-boot:run
```

Default backend URL:

```text
http://localhost:8080
```

## 6. Backend Packages

### 6.1 `api`

This package stores request and response DTO files.

Examples:

| File | Purpose |
|---|---|
| `AddTopicRequest.java` | Request body for adding topic |
| `TopicResponse.java` | Response after topic creation |
| `RevisionResponse.java` | Revision data sent to frontend |
| `DashboardResponse.java` | Dashboard data |
| `StatisticsResponse.java` | Statistics page data |
| `DayCountResponse.java` | Day-wise chart data |
| `SubjectCountResponse.java` | Subject chart data |
| `LoginRequest.java` | Login request |
| `AuthResponse.java` | Login/register response with token |
| `RegisterOtpRequest.java` | Registration OTP request |
| `RegisterVerifyRequest.java` | OTP verification and account creation |
| `PasswordResetRequest.java` | Password reset request |
| `GoogleLoginRequest.java` | Google login token request |

### 6.2 `controller`

This package contains REST API controllers.

Important files:

```text
AuthController.java
TopicController.java
RevisionController.java
```

### 6.3 `service`

This package contains business logic.

Important files:

```text
AuthService.java
TopicService.java
RevisionService.java
EmailService.java
```

### 6.4 `topic`

This package contains topic entity and repository.

Files:

```text
Topic.java
TopicRepository.java
Difficulty.java
```

### 6.5 `revision`

This package contains revision entity and repository.

Files:

```text
Revision.java
RevisionRepository.java
```

### 6.6 `user`

This package contains user entity and repository.

Files:

```text
User.java
UserRepository.java
```

### 6.7 `auth`

This package handles authentication tokens and OTP records.

Files:

```text
AccessToken.java
AccessTokenRepository.java
AuthInterceptor.java
CurrentUser.java
LoginOtp.java
LoginOtpRepository.java
OtpPurpose.java
TokenHasher.java
```

### 6.8 `config`

This package contains backend configuration.

Files:

```text
AuthBeans.java
CorsConfig.java
```

## 7. Database Structure

Database schema file:

```text
database/schema.sql
```

Database name:

```text
smart_revision_scheduler
```

### 7.1 `users`

Stores registered users.

Columns:

| Column | Meaning |
|---|---|
| `id` | User ID |
| `name` | User name |
| `email` | User email |
| `password` | Old/plain compatibility column, should not be used |
| `password_hash` | Secure hashed password |
| `email_verified` | Whether email is verified |
| `created_at` | Account creation time |

### 7.2 `login_otps`

Stores OTPs for registration and password reset.

Columns:

| Column | Meaning |
|---|---|
| `id` | OTP row ID |
| `email` | Email receiving OTP |
| `purpose` | `REGISTER` or `PASSWORD_RESET` |
| `otp_hash` | Hashed OTP |
| `expires_at` | OTP expiry time |
| `used` | Whether OTP was already used |
| `created_at` | OTP creation time |

### 7.3 `access_tokens`

Stores login sessions.

Columns:

| Column | Meaning |
|---|---|
| `id` | Token row ID |
| `user_id` | Connected user |
| `token_hash` | Hashed login token |
| `expires_at` | Token expiry |
| `created_at` | Token creation time |

The frontend stores the raw token in browser localStorage. The backend stores only the hashed version.

### 7.4 `topics`

Stores topics learned by the user.

Columns:

| Column | Meaning |
|---|---|
| `id` | Topic ID |
| `user_id` | Owner user |
| `topic_name` | Topic name |
| `subject` | Subject name |
| `difficulty` | EASY, MEDIUM, HARD |
| `date_learned` | Date when user learned the topic |
| `date_added` | Date when topic was added |

### 7.5 `revisions`

Stores all automatically generated revision reminders.

Columns:

| Column | Meaning |
|---|---|
| `id` | Revision ID |
| `topic_id` | Connected topic |
| `revision_number` | Revision sequence number |
| `revision_day` | Day gap, like 1, 3, 7 |
| `revision_date` | Actual revision date |
| `completed` | Whether user completed it |
| `completed_at` | Completion timestamp |

## 8. Authentication Flow

### 8.1 Registration

Registration uses OTP only for the first account creation.

Flow:

1. User opens Register page.
2. User enters name, email, password, and confirm password.
3. Frontend checks password and confirm password match.
4. Frontend calls:

```text
POST /api/auth/register/request-otp
```

5. Backend creates a 6-digit OTP.
6. Backend hashes the OTP and saves it in `login_otps`.
7. Backend sends OTP by email.
8. User enters OTP.
9. Frontend calls:

```text
POST /api/auth/register/verify
```

10. Backend checks OTP.
11. Backend creates user.
12. Backend hashes password using `PasswordEncoder`.
13. Backend creates access token.
14. Frontend stores token in localStorage.
15. User enters dashboard.

### 8.2 Login

Normal login does not require OTP.

Flow:

1. User enters email and password.
2. Frontend calls:

```text
POST /api/auth/login
```

3. Backend finds user by email.
4. Backend compares entered password with `password_hash`.
5. Backend creates an access token.
6. Frontend stores token.
7. User enters the app.

### 8.3 Forgot Password

Password reset uses OTP.

Flow:

1. User clicks Forgot Password.
2. User enters email.
3. Frontend calls:

```text
POST /api/auth/password/request-reset
```

4. Backend sends OTP.
5. User enters OTP, new password, and confirm password.
6. Frontend checks both passwords match.
7. Frontend calls:

```text
POST /api/auth/password/reset
```

8. Backend verifies OTP.
9. Backend saves new password hash.

### 8.4 Google Login

Google login is optional and depends on configuration.

Flow:

1. User clicks Continue with Google.
2. Google returns an ID token credential.
3. Frontend sends credential to:

```text
POST /api/auth/google
```

4. Backend verifies token using Google client ID.
5. Backend checks Google email is verified.
6. Backend creates user if account does not exist.
7. Backend returns access token.

Required environment variables:

```text
GOOGLE_CLIENT_ID
VITE_GOOGLE_CLIENT_ID
```

## 9. Token Authentication

After login/register/password reset session creation:

1. Backend creates a random raw token.
2. Backend hashes it with SHA-256.
3. Backend stores hash in `access_tokens`.
4. Frontend stores raw token in localStorage.
5. For protected API calls, frontend sends:

```text
Authorization: Bearer <token>
```

6. `AuthInterceptor` checks token hash and loads current user.

## 10. Topic Creation and Revision Scheduling

When a user adds a topic, the frontend sends:

```text
POST /api/topics
```

Example request:

```json
{
  "topicName": "SQL Injection",
  "subject": "Cyber Security",
  "difficulty": "MEDIUM",
  "dateLearned": "2026-06-28"
}
```

Backend file:

```text
backend/src/main/java/com/smartrevision/scheduler/service/TopicService.java
```

Scheduling logic:

```java
private static final int[] REVISION_DAYS = {1, 3, 7, 14, 30, 60, 90};
```

For each day:

```java
revisionDate = dateLearned.plusDays(day)
```

Example:

```text
Date learned: 28 June 2026

Day 1  -> 29 June 2026
Day 3  -> 1 July 2026
Day 7  -> 5 July 2026
Day 14 -> 12 July 2026
Day 30 -> 28 July 2026
Day 60 -> 27 August 2026
Day 90 -> 26 September 2026
```

Each generated revision is saved in the `revisions` table.

## 11. Dashboard

Dashboard data comes from:

```text
GET /api/dashboard
```

Backend file:

```text
RevisionService.java
```

Dashboard shows:

- Today's Goal
- Topics Due
- Completed
- Remaining
- Memory Score
- Topics Learned
- Revisions Completed
- Current Streak
- Next 7 Days
- Today's Revisions
- Upcoming grouped by date

### 11.1 Today's Goal

Frontend calculates:

```text
Topics Due = today.length
Completed = today items where completed = true
Remaining = Topics Due - Completed
```

### 11.2 Today's Revisions

Each due revision appears as a card.

Mark Complete button rule:

- Future revision date: button hidden, shows locked/not due style.
- Today or previous date: Mark Complete button appears.
- Completed revision: shows completed status.

### 11.3 Upcoming Section

Upcoming revisions are grouped by date.

Example:

```text
2 Jul
SQL
DBMS - Day 1

CRUD
DBMS - Day 1
```

The date appears once, and topics appear below it.

## 12. Memory Score

Memory Score is calculated in the backend.

File:

```text
RevisionService.java
```

Formula:

```text
Memory Score =
40% revision completion
+ 30% on-time completion
+ 20% streak consistency
+ 10% difficulty-weighted progress
```

### 12.1 Completion Score

Checks how many due revisions are completed.

```text
completed due revisions / total due revisions
```

### 12.2 On-Time Score

Checks whether revisions were completed on or before their scheduled date.

```text
completed_at date <= revision_date
```

### 12.3 Streak Score

Uses current streak up to 7 days.

```text
min(currentStreak, 7) / 7
```

### 12.4 Difficulty Score

Harder topics get slightly more weight.

```text
EASY   = 1.0
MEDIUM = 1.15
HARD   = 1.3
```

## 13. Calendar

Calendar data comes from:

```text
GET /api/revisions/calendar?from=2024-01-01&to=2099-12-31
```

Frontend calendar features:

- Month selector
- Year selector from 2024 to 2099
- Actual calendar grid
- Clickable day cells
- Popup for selected day
- Shows topics, subject, revision day, and status
- Mark Complete appears only for today or overdue revisions

Calendar page file:

```text
frontend/src/App.jsx
```

Calendar styling:

```text
frontend/src/styles.css
```

## 14. Statistics

Statistics data comes from:

```text
GET /api/statistics
```

Backend response includes:

```text
weeklyActivity
revisionConsistency
subjects
```

### 14.1 Weekly Bar Chart

Shows how many revisions were completed each day in the current week.

Example:

```text
Mon 2
Tue 4
Wed 0
Thu 1
```

The bar width is based on the highest count in the week.

### 14.2 Revision Consistency

Shows the last 91 days as a heatmap.

Meaning:

- Empty/light box: no or low completed revisions.
- Darker box: more completed revisions on that day.

Data source:

```text
completed_at
```

### 14.3 Subjects

Shows subject distribution based on saved revision records.

Example:

```text
DBMS: 14
Cyber Security: 7
Java: 5
```

## 15. Add Topic Page

Frontend fields:

- Topic Name
- Subject
- Difficulty
- Date Learned
- Priority stars
- Notes
- Generate Schedule
- Save

Important note:

Priority and notes currently exist in the frontend UI, but they are not stored in the backend database yet. To permanently store them, new columns should be added to the `topics` table and backend DTO/entity should be updated.

When user clicks Generate Schedule, frontend previews:

```text
Tomorrow
Day 3
Day 7
Day 14
Day 30
Day 60
Day 90
```

When user clicks Save, backend stores topic and actual revision rows.

## 16. API Endpoints

### 16.1 Auth APIs

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register/request-otp` | Send OTP for registration |
| POST | `/api/auth/register/verify` | Verify OTP and create account |
| POST | `/api/auth/login` | Login with email/password |
| POST | `/api/auth/google` | Login with Google |
| POST | `/api/auth/password/request-reset` | Send password reset OTP |
| POST | `/api/auth/password/reset` | Reset password |

### 16.2 Topic APIs

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/topics` | Add topic and generate revisions |

### 16.3 Revision APIs

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/dashboard` | Dashboard data |
| GET | `/api/statistics` | Statistics page data |
| GET | `/api/revisions/today` | Today's revisions |
| GET | `/api/revisions/calendar?from=&to=` | Calendar revisions |
| PATCH | `/api/revisions/{id}/complete` | Mark revision complete |

## 17. Environment Configuration

Backend config file:

```text
backend/src/main/resources/application.properties
```

Important properties:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/smart_revision_scheduler
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:password}
app.cors.allowed-origin=${APP_CORS_ALLOWED_ORIGIN:http://localhost:5173}
app.auth.otp-expiry-minutes=${OTP_EXPIRY_MINUTES:10}
app.auth.token-expiry-days=${TOKEN_EXPIRY_DAYS:30}
app.auth.google-client-id=${GOOGLE_CLIENT_ID:}
app.mail.enabled=${MAIL_ENABLED:false}
```

Frontend environment:

```text
VITE_API_BASE_URL=http://localhost:8080/api
VITE_GOOGLE_CLIENT_ID=your_google_client_id
```

## 18. How to Run Locally

### 18.1 Start MySQL

Make sure MySQL is running and your password is ready.

Database name:

```text
smart_revision_scheduler
```

Spring Boot can create/update tables automatically because:

```properties
spring.jpa.hibernate.ddl-auto=update
```

You can also run:

```text
database/schema.sql
```

manually in MySQL if needed.

### 18.2 Start Backend

Open PowerShell:

```powershell
cd "C:\Users\souvi\OneDrive\Documents\New project\backend"
$env:MYSQL_PASSWORD="your_mysql_password"
mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

### 18.3 Start Frontend

Open another PowerShell:

```powershell
cd "C:\Users\souvi\OneDrive\Documents\New project\frontend"
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## 19. Build Commands

Backend build:

```powershell
cd "C:\Users\souvi\OneDrive\Documents\New project\backend"
mvn -DskipTests package
```

Frontend build:

```powershell
cd "C:\Users\souvi\OneDrive\Documents\New project\frontend"
npm run build
```

## 20. Deployment Files

### 20.1 `render.yaml`

Used for deployment configuration on Render.

### 20.2 `backend/Dockerfile`

Defines how to build and run the backend in a container.

### 20.3 `frontend/Dockerfile`

Defines how to build frontend production files and serve them.

### 20.4 `frontend/nginx.conf`

Nginx configuration for serving the React app.

## 21. SEO Files

Frontend public folder:

```text
frontend/public
├── robots.txt
├── sitemap.xml
└── site.webmanifest
```

Purpose:

- `robots.txt`: tells search engines which pages can be crawled.
- `sitemap.xml`: helps search engines discover pages.
- `site.webmanifest`: improves installable web app/browser metadata.

## 22. Important Current Limitations

These are useful to mention in interviews because they show you understand future improvements.

1. Priority stars and notes are currently frontend-only.
2. Email sending needs real SMTP configuration for production.
3. Google login requires valid Google OAuth client ID.
4. Statistics are based on completed revision records; if no revisions are completed, charts show low/empty values.
5. Calendar currently loads a very wide range from 2024 to 2099, which is fine for small projects but may need pagination or month-based API calls for very large traffic.
6. Authentication uses custom access tokens, not JWT.
7. More production security can be added, such as rate limiting OTP requests.

## 23. Future Improvements

Recommended future features:

- Store notes and priority in database.
- Add email reminders.
- Add browser push notifications.
- Add Google Calendar sync.
- Add AI quiz generation from notes.
- Add PDF upload.
- Add subject filters.
- Add search for all topics.
- Add admin dashboard.
- Add rate limiting for login and OTP.
- Add monthly statistics trend.
- Add mobile app version.
- Add unit and integration tests.

## 24. Interview Explanation

Short explanation:

```text
Smart Revision Scheduler is a full-stack spaced-repetition application built with React, Spring Boot, and MySQL. Users register with email OTP, log in using email/password or Google, add study topics, and the backend automatically generates revision dates for Day 1, 3, 7, 14, 30, 60, and 90. The dashboard shows today's revisions, progress, memory score, upcoming tasks, calendar view, and real statistics based on completed revisions.
```

Technical explanation:

```text
The frontend is built in React using Vite. It communicates with the Spring Boot backend through REST APIs. The backend stores users, OTPs, access tokens, topics, and revision schedules in MySQL. When a topic is created, the TopicService generates multiple Revision rows using the spaced-repetition intervals. Dashboard and statistics APIs aggregate revision data for progress tracking, memory score, weekly activity, consistency heatmap, and subject distribution.
```

## 25. Key Files Summary

| File | Purpose |
|---|---|
| `frontend/src/App.jsx` | Main React UI and page logic |
| `frontend/src/api.js` | Frontend API calls |
| `frontend/src/styles.css` | Full app styling and dark mode |
| `backend/src/main/java/.../AuthService.java` | Registration, login, OTP, password reset, Google login |
| `backend/src/main/java/.../TopicService.java` | Add topic and generate revision schedule |
| `backend/src/main/java/.../RevisionService.java` | Dashboard, calendar, complete revision, memory score, statistics |
| `backend/src/main/java/.../AuthController.java` | Auth REST endpoints |
| `backend/src/main/java/.../TopicController.java` | Topic REST endpoint |
| `backend/src/main/java/.../RevisionController.java` | Dashboard, calendar, statistics, revision endpoints |
| `database/schema.sql` | MySQL table design |
| `backend/src/main/resources/application.properties` | Backend configuration |
| `frontend/package.json` | Frontend dependencies and scripts |
| `backend/pom.xml` | Backend dependencies and build config |

