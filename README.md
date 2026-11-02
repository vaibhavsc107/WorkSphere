# WorkSphere -- Enterprise HR & Workforce Management System

WorkSphere is an enterprise-style Human Resource Management System
(HRMS) built as a Java full-stack project. It is designed to bring
common workforce operations into one application, including employee
administration, organizational structure, attendance, leave, payroll,
performance, announcements, notifications and reporting.

The project is being developed as a **modular monolith**: a single
Spring Boot backend organized by business feature, with a React frontend
and MySQL database. This keeps the initial architecture maintainable
while leaving room to extract selected modules into services later if
required.

> **Current status:** Phase 1 -- Project Foundation, Phase 2 -- Backend
> Authentication & Authorization, and Phase 3 -- Frontend Authentication
> Integration are completed. Phase 4 -- Employee Management is next.

------------------------------------------------------------------------

## Table of Contents

1.  [Project Overview](#1-project-overview)
2.  [Project Goals](#2-project-goals)
3.  [Technology Stack](#3-technology-stack)
4.  [Architecture](#4-architecture)
5.  [Repository Structure](#5-repository-structure)
6.  [Local Development Requirements](#6-local-development-requirements)
7.  [Environment Configuration](#7-environment-configuration)
8.  [Run the Application](#8-run-the-application)
9.  [Phase 1 -- Project Foundation](#9-phase-1--project-foundation)
10. [Phase 2 -- Backend Authentication &
    Authorization](#10-phase-2--backend-authentication--authorization)
11. [Phase 3 -- Frontend Authentication
    Integration](#11-phase-3--frontend-authentication-integration)
12. [Authentication API Reference](#12-authentication-api-reference)
13. [Roles and Access Rules](#13-roles-and-access-rules)
14. [Database Design -- Current and
    Planned](#14-database-design--current-and-planned)
15. [Testing Guide](#15-testing-guide)
16. [Git and Commit Workflow](#16-git-and-commit-workflow)
17. [Security Considerations](#17-security-considerations)
18. [Known Limitations and Future
    Hardening](#18-known-limitations-and-future-hardening)
19. [Roadmap](#19-roadmap)
20. [Documentation](#20-documentation)

------------------------------------------------------------------------

## 1. Project Overview

**Project name:** WorkSphere\
**Project type:** Enterprise HR & Workforce Management System\
**Architecture:** Modular monolith\
**Frontend:** React + JavaScript + Vite\
**Backend:** Java + Spring Boot REST API\
**Database:** MySQL\
**API base path:** `/api/v1`

WorkSphere is intended to support three main user roles:

-   `SUPER_ADMIN`
-   `HR_MANAGER`
-   `EMPLOYEE`

The planned business modules include:

-   Authentication and authorization
-   Employee management
-   Departments and designations
-   Attendance management
-   Leave management
-   Payroll management
-   Performance reviews
-   Notifications and announcements
-   Reports and audit logs

The current completed implementation focuses on the application
foundation and authentication lifecycle. The remaining HR modules will
be implemented in later phases.

## 2. Project Goals

-   Build a portfolio-quality Java full-stack application.
-   Apply layered Spring Boot design using controllers, services,
    repositories, DTOs and entities.
-   Implement secure authentication and role-based authorization.
-   Build a feature-oriented React frontend.
-   Use MySQL for persistent application data.
-   Practice REST API design, validation, exception handling and
    frontend/backend integration.
-   Add automated testing, containerization and CI/CD as the project
    progresses.

## 3. Technology Stack

### Frontend

  Technology     Purpose
  -------------- --------------------------------------
  React          Component-based user interface
  JavaScript     Frontend programming language
  Vite           Development server and build tooling
  React Router   Client-side routing
  Tailwind CSS   UI styling
  Axios          HTTP client for backend APIs
  Context API    Authentication state management
  lucide-react   Interface icons

### Backend

  Technology        Purpose
  ----------------- ---------------------------------------
  Java 21           Backend programming language
  Spring Boot       REST API and application framework
  Maven             Dependency and build management
  Spring Web        REST controllers and HTTP endpoints
  Spring Security   Authentication and authorization
  Spring Data JPA   Persistence abstraction
  Hibernate         ORM implementation
  Bean Validation   Request validation
  JJWT              JWT access-token creation and parsing
  BCrypt            Password hashing

### Database and planned engineering tools

  -----------------------------------------------------------------------
  Technology                          Purpose / status
  ----------------------------------- -----------------------------------
  MySQL 8                             Relational database

  Swagger / OpenAPI                   API documentation --
                                      planned/configuration as project
                                      progresses

  JUnit 5                             Backend testing -- planned

  Mockito                             Backend mocking -- planned

  Vitest / React Testing Library      Frontend testing -- planned

  Docker                              Containerization -- planned

  GitHub Actions                      CI/CD -- planned
  -----------------------------------------------------------------------

## 4. Architecture

WorkSphere currently follows a **modular monolith** approach.

``` text
                    React Frontend
                 React + Vite + Tailwind
                           |
                           | HTTP / JSON
                           | Bearer access JWT
                           | HttpOnly refresh cookie
                           v
                  Spring Boot REST API
                       /api/v1
                           |
          +----------------+----------------+
          |                |                |
      Controllers       Services        Security
          |                |                |
          +----------------+----------------+
                           |
                     Repositories
                           |
                      Spring Data JPA
                           |
                         MySQL
```

### Backend layering

-   **Controller:** accepts HTTP requests and returns HTTP responses.
-   **DTO:** defines request and response data contracts.
-   **Service:** contains business logic and transaction boundaries.
-   **Repository:** performs database operations.
-   **Entity:** maps Java objects to database tables.
-   **Security:** authenticates requests and enforces access rules.

The backend base package is:

``` text
com.project.WorkSphere
```

Preserve this package name and casing in Java source files.

## 5. Repository Structure

The project is maintained as a monorepo:

``` text
WorkSphere/
├── worksphere-backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/project/WorkSphere/
│   │       │       ├── auth/
│   │       │       │   ├── controller/
│   │       │       │   ├── dto/
│   │       │       │   ├── entity/
│   │       │       │   ├── repository/
│   │       │       │   └── service/
│   │       │       ├── config/
│   │       │       ├── role/
│   │       │       │   ├── entity/
│   │       │       │   └── repository/
│   │       │       ├── security/
│   │       │       └── user/
│   │       │           ├── entity/
│   │       │           └── repository/
│   │       └── resources/
│   └── pom.xml
│
├── worksphere-frontend/
│   ├── src/
│   │   ├── components/
│   │   │   └── layout/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── routes/
│   │   └── services/
│   ├── .env                 # local only; do not commit
│   ├── package.json
│   └── vite.config.js
│
├── database/
├── docs/
│   ├── Phase_1_Project_Setup_Report.docx
│   ├── Phase_2_Authentication_Authorization_Report.docx
│   └── WorkSphere_Phase_3_Implementation_Report.docx
├── .gitignore
└── README.md
```

This is a representative structure. The actual directory may contain
additional files as modules are developed.

## 6. Local Development Requirements

Install the following:

-   Java Development Kit (JDK) 21
-   Maven, or the Maven Wrapper if included
-   Node.js and npm
-   MySQL Server 8
-   Git
-   An IDE such as IntelliJ IDEA or VS Code
-   Postman for API testing

### Local service addresses

  Service        Address
  -------------- --------------------------------
  Frontend       `http://localhost:5173`
  Backend        `http://localhost:8085`
  API base URL   `http://localhost:8085/api/v1`
  MySQL          `localhost:3306`

## 7. Environment Configuration

### 7.1 Create the database

In MySQL, create the development database:

``` sql
CREATE DATABASE worksphere_db;
```

### 7.2 Backend configuration

Create the local file:

`worksphere-backend/src/main/resources/application.properties`

Example configuration:

``` properties
server.port=8085

spring.datasource.url=jdbc:mysql://localhost:3306/worksphere_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.flyway.enabled=false

jwt.secret=YOUR_BASE64_JWT_SECRET
jwt.expiration=900000
jwt.refresh-expiration=604800000
```

Notes:

-   Replace placeholders with your own local values.
-   `jwt.expiration=900000` represents 15 minutes.
-   `jwt.refresh-expiration=604800000` represents 7 days.
-   The project used Hibernate schema update during development.
-   Flyway was disabled during the initial development setup.
-   Do not commit the real `application.properties`, credentials or JWT
    secret.

### 7.3 Frontend configuration

Create:

`worksphere-frontend/.env`

``` env
VITE_API_BASE_URL=http://localhost:8085/api/v1
```

Restart the Vite development server after changing Vite environment
variables.

## 8. Run the Application

### 8.1 Start MySQL

Ensure the MySQL service is running and `worksphere_db` exists.

### 8.2 Start the backend

Open a terminal in the project root:

``` bash
cd worksphere-backend
```

If the Maven Wrapper exists:

**Windows PowerShell:**

``` powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

``` bash
./mvnw spring-boot:run
```

If the wrapper is not present:

``` bash
mvn spring-boot:run
```

The backend should start on:

`http://localhost:8085`

### 8.3 Start the frontend

Open a second terminal:

``` bash
cd worksphere-frontend
npm install
npm run dev
```

Open:

`http://localhost:5173`

## 9. Phase 1 -- Project Foundation

**Status: Completed**

### Objective

Create the monorepo, configure the React and Spring Boot applications,
connect the backend to MySQL, and verify frontend/backend communication.

### Work completed

1.  Created the root `WorkSphere` project directory.
2.  Created separate `worksphere-frontend` and `worksphere-backend`
    applications.
3.  Created the React frontend using Vite.
4.  Installed initial frontend dependencies, including Axios, React
    Router and lucide-react.
5.  Created the Spring Boot backend using Java 21 and Maven.
6.  Configured the MySQL database `worksphere_db`.
7.  Configured the backend datasource and JPA/Hibernate.
8.  Configured the backend to run on port `8085`.
9.  Created a test REST endpoint under `/api/v1/test`.
10. Configured CORS to allow the frontend at `http://localhost:5173`.
11. Connected React to Spring Boot using Axios.
12. Verified that the frontend could receive a JSON response from the
    backend.
13. Initialized Git and organized the repository as a monorepo.
14. Added `.gitignore` rules to keep local configuration and generated
    files out of Git.

### Phase 1 outcome

The frontend, backend and database foundation was established, and
React-to-Spring Boot communication was verified.

## 10. Phase 2 -- Backend Authentication & Authorization

**Status: Completed**

### Objective

Implement user registration, secure password storage, JWT
authentication, request authentication filters and role-based backend
authorization.

### 10.1 Roles

The application defines:

-   `SUPER_ADMIN`
-   `HR_MANAGER`
-   `EMPLOYEE`

Role names are represented by a Java enum and persisted as strings.

### 10.2 User and role persistence

Created the core persistence components:

-   `User` entity mapped to `users`.
-   `Role` entity mapped to `roles`.
-   Many-to-many association through `user_roles`.
-   `UserRepository`.
-   `RoleRepository`.
-   `RoleName` enum.

The user repository fetches roles with the user for authentication use.

### 10.3 Registration

Implemented:

`POST /api/v1/auth/register`

Registration includes:

-   Email validation.
-   Password validation.
-   Duplicate email checking.
-   BCrypt password hashing.
-   Default assignment of the `EMPLOYEE` role.

Public registration does not accept an elevated role from the client.
This prevents users from self-assigning `HR_MANAGER` or `SUPER_ADMIN`.

### 10.4 Login and password verification

Implemented:

`POST /api/v1/auth/login`

The backend:

1.  Finds the user by email.
2.  Checks whether the account is active.
3.  Verifies the submitted password using `PasswordEncoder.matches()`.
4.  Loads the user's roles.
5.  Generates a signed JWT access token.
6.  Returns the token, email and roles in the authentication response.

Passwords are hashed with BCrypt and are never decrypted.

### 10.5 JWT service

Implemented `JwtService` to:

-   Load the Base64-encoded signing secret from local configuration.
-   Generate signed JWT access tokens.
-   Set the email as the subject.
-   Include role claims.
-   Set issued-at and expiration timestamps.
-   Parse and verify signed claims.
-   Check token validity and expiration.

### 10.6 JWT authentication filter

Implemented `JwtAuthenticationFilter` using `OncePerRequestFilter`.

The filter:

1.  Reads the `Authorization` header.
2.  Checks for the `Bearer` prefix.
3.  Extracts and validates the JWT.
4.  Loads the user and roles.
5.  Checks that the user is active.
6.  Converts roles into Spring Security authorities such as
    `ROLE_SUPER_ADMIN`.
7.  Sets the authenticated principal in the `SecurityContext`.

### 10.7 Spring Security configuration

Configured:

-   Stateless session management.
-   JWT filter before `UsernamePasswordAuthenticationFilter`.
-   Public registration and login endpoints.
-   Authentication for protected endpoints.
-   CORS for the frontend origin.
-   BCrypt `PasswordEncoder`.
-   Method-level security with `@EnableMethodSecurity`.

### 10.8 Role-based authorization

Added protected test endpoints for Admin, HR and Employee dashboards
using `@PreAuthorize`.

Examples:

``` java
@PreAuthorize("hasRole('SUPER_ADMIN')")
```

``` java
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
```

``` java
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
```

### Phase 2 outcome

The backend supports registration, BCrypt password verification,
JWT-based authentication, stateless security and role-based endpoint
authorization.

## 11. Phase 3 -- Frontend Authentication Integration

**Status: Completed**

### Objective

Connect the React application to the authentication backend, implement
role-aware navigation and protected routes, and support a
refresh-token-based session lifecycle.

### 11.1 Tailwind CSS setup

Configured Tailwind CSS with Vite and imported Tailwind into the
frontend stylesheet.

### 11.2 Axios API client

Created:

`src/services/api.js`

The shared Axios client:

-   Uses `VITE_API_BASE_URL`.
-   Sets JSON content type.
-   Enables `withCredentials` so the browser can send and receive
    refresh-token cookies.

### 11.3 Authentication service

Created:

`src/services/authService.js`

Provides:

-   `registerUser(userData)`
-   `loginUser(credentials)`

### 11.4 Authentication context

Created:

-   `src/context/authContext.js`
-   `src/context/AuthContext.jsx`
-   `src/hooks/useAuth.js`

The provider manages:

-   `user`
-   `accessToken`
-   `isAuthenticated`
-   `isInitializing`
-   `login()`
-   `logout()`

The access token is stored in React memory. It is not persisted in
localStorage.

### 11.5 Login and registration pages

Created:

-   Login page with email/password input, password visibility, loading
    and error states.
-   Registration page with email/password/confirm-password validation.
-   Successful registration redirects to login.
-   Successful login routes users according to their roles.

### 11.6 Protected routes

Created `ProtectedRoute.jsx`.

It checks authentication and allowed roles before rendering protected
dashboard content. It waits for session restoration to finish before
deciding whether to redirect.

The frontend route guard is for navigation and user experience. The
backend remains responsible for enforcing security.

### 11.7 Dashboard layout and role-aware navigation

Created `DashboardLayout.jsx` with a sidebar and logout action.

Navigation visibility:

  Role            Dashboard links       Management links
  --------------- --------------------- ------------------------
  `SUPER_ADMIN`   Admin, HR, Employee   Employees, Departments
  `HR_MANAGER`    HR, Employee          Employees, Departments
  `EMPLOYEE`      Employee              None

The dashboard routes are:

  Route                   Allowed roles
  ----------------------- -----------------------------------------
  `/admin/dashboard`      `SUPER_ADMIN`
  `/hr/dashboard`         `SUPER_ADMIN`, `HR_MANAGER`
  `/employee/dashboard`   `SUPER_ADMIN`, `HR_MANAGER`, `EMPLOYEE`

These route permissions are also enforced by backend authorization for
API resources as those modules are developed.

### 11.8 Refresh-token backend support

Added:

-   `RefreshToken` entity.
-   `RefreshTokenRepository`.
-   `RefreshTokenService`.
-   `RefreshResult` DTO.
-   Login cookie issuance.
-   Refresh endpoint.
-   Logout endpoint.
-   Credentialed CORS configuration.

The refresh token is generated using `SecureRandom`. Its SHA-256 hash is
stored in MySQL; the raw token is delivered to the browser in an
HttpOnly cookie.

The refresh-token record includes:

-   Token hash.
-   Associated user.
-   Expiration timestamp.
-   Revocation status.
-   Creation timestamp.

### 11.9 Refresh-token rotation

`POST /api/v1/auth/refresh`:

1.  Reads the refresh cookie.
2.  Hashes the supplied token and finds its database record.
3.  Checks revocation, expiration and account status.
4.  Revokes the old refresh-token record.
5.  Creates a replacement refresh token.
6.  Generates a new access JWT using current database roles.
7.  Returns the new access token and sets the replacement cookie.

### 11.10 Axios interceptors

Created `src/services/setupInterceptors.js`.

The request interceptor attaches the current access token as a Bearer
token.

The response interceptor handles an eligible 401 response by attempting
refresh and retrying the original request once. Login, refresh and
logout requests are excluded from the refresh retry path.

### 11.11 Session restoration

When the React application starts, `AuthProvider` calls the refresh
endpoint.

-   If the refresh cookie is valid, the backend rotates it and returns a
    new access token and user details.
-   The frontend restores the user and roles.
-   `isInitializing` prevents protected routes from redirecting while
    restoration is in progress.
-   If restoration fails, the frontend clears the in-memory session and
    displays the public login route.

### 11.12 Logout

The frontend calls:

`POST /api/v1/auth/logout`

The backend revokes the refresh-token record and clears the cookie. The
frontend clears user and access-token state and navigates to the login
page.

### Phase 3 outcome

The frontend and backend authentication lifecycle is integrated.
Registration, login, role-based dashboard navigation, session
restoration, access-token renewal and backend-backed logout were
implemented and exercised during development.

## 12. Authentication API Reference

The current authentication endpoints are:

  -------------------------------------------------------------------------------
  Method            Endpoint                  Access            Purpose
  ----------------- ------------------------- ----------------- -----------------
  POST              `/api/v1/auth/register`   Public            Register a user
                                                                with default
                                                                `EMPLOYEE` role

  POST              `/api/v1/auth/login`      Public            Verify
                                                                credentials,
                                                                return access
                                                                token and set
                                                                refresh cookie

  POST              `/api/v1/auth/refresh`    Refresh cookie    Rotate refresh
                                                                token and issue a
                                                                new access token

  POST              `/api/v1/auth/logout`     Refresh cookie if Revoke refresh
                                              present           session and clear
                                                                cookie
  -------------------------------------------------------------------------------

### Example registration request

``` http
POST /api/v1/auth/register
Content-Type: application/json
```

``` json
{
  "email": "employee@example.com",
  "password": "ExamplePass123"
}
```

### Example login request

``` http
POST /api/v1/auth/login
Content-Type: application/json
```

``` json
{
  "email": "employee@example.com",
  "password": "ExamplePass123"
}
```

The successful login response contains an access token, token type,
email and role names. The refresh token is sent separately as an
HttpOnly cookie.

### Refresh request

``` http
POST /api/v1/auth/refresh
```

The browser or API client must send the refresh-token cookie.

### Logout request

``` http
POST /api/v1/auth/logout
```

The backend revokes the refresh-token record and expires the cookie.

## 13. Roles and Access Rules

The intended high-level role model is:

  Capability              SUPER_ADMIN   HR_MANAGER   EMPLOYEE
  ---------------------- ------------- ------------ ----------
  Admin dashboard             Yes           No          No
  HR dashboard                Yes          Yes          No
  Employee dashboard          Yes          Yes         Yes
  View all employees          Yes          Yes          No
  Create employees            Yes          Yes          No
  Update employees            Yes          Yes          No
  Deactivate employees        Yes          Yes          No
  View own profile            Yes          Yes         Yes

The employee-management permissions are planned module-level rules. When
implementing the module, enforce them on backend endpoints and ensure
employees can only access their own records.

## 14. Database Design -- Current and Planned

### Implemented core tables

The authentication implementation uses:

-   `users`
-   `roles`
-   `user_roles`
-   `refresh_tokens`

The development configuration uses Hibernate schema update. Confirm the
actual MySQL schema in your local database; this README describes the
intended mappings, not a database dump.

### Planned business tables

The broader HRMS data model is expected to include:

-   `employees`
-   `departments`
-   `designations`
-   `attendance`
-   `leave_types`
-   `leave_balances`
-   `leave_requests`
-   `salary_structures`
-   `payroll`
-   `payroll_items`
-   `performance_reviews`
-   `notifications`
-   `announcements`
-   `audit_logs`

These business tables are part of the project roadmap and should not be
assumed to be implemented in the current phases.

## 15. Testing Guide

### Phase 1 checks

-   Confirm MySQL is running.
-   Confirm the backend starts on port `8085`.
-   Call the public test endpoint.
-   Confirm the React frontend can call the backend and display its JSON
    response.

### Phase 2 checks

-   Register a new user.
-   Confirm duplicate email registration is rejected.
-   Confirm public registration assigns only `EMPLOYEE`.
-   Log in with valid credentials.
-   Confirm invalid credentials return `401`.
-   Call protected endpoints without a token and confirm access is
    rejected.
-   Call protected endpoints with a valid token and confirm role
    permissions are enforced.

### Phase 3 checks

-   Log in as `SUPER_ADMIN`, `HR_MANAGER` and `EMPLOYEE`.
-   Confirm each role sees the correct navigation links.
-   Navigate between dashboards permitted for the signed-in role.
-   Confirm protected routes redirect unauthenticated users to login.
-   Confirm login sets a refresh-token cookie.
-   Call refresh and confirm the old token is rotated.
-   Reuse the old refresh token and confirm it is rejected.
-   Refresh the browser and confirm session restoration.
-   Log out and confirm the cookie is cleared and the refresh session is
    revoked.
-   Attempt session restoration after logout and confirm the user
    remains logged out.

### Useful MySQL inspection query

``` sql
SELECT id, user_id, revoked, expires_at, created_at
FROM refresh_tokens;
```

Do not select or expose real token material in screenshots, logs or
shared documentation. The table stores hashes rather than raw refresh
tokens.

## 16. Git and Commit Workflow

Run Git commands from the repository root.

### Check changes

``` bash
git status
git diff --check
```

### Check that local secrets are ignored

``` bash
git check-ignore -v worksphere-backend/src/main/resources/application.properties
git check-ignore -v worksphere-frontend/.env
```

If a local secret file is already tracked, `.gitignore` alone will not
remove it from Git history or the index. Remove it from tracking
carefully and rotate any secret that was exposed.

### Stage Phase 1--3 project files

Review the output before committing:

``` bash
git add README.md
git add .gitignore
git add docs/
git add database/
git add worksphere-frontend/
git add worksphere-backend/
git status
```

Do not stage local secret files, `node_modules`, build output, generated
logs or IDE-specific files.

### Suggested commit messages

``` bash
git commit -m "Complete Phase 1 project foundation"
```

``` bash
git commit -m "Implement Phase 2 authentication and authorization"
```

``` bash
git commit -m "Complete Phase 3 frontend authentication integration"
```

If the phases are already committed separately, commit only the
remaining README and documentation changes:

``` bash
git add README.md docs/
git commit -m "Update WorkSphere project documentation through Phase 3"
```

Push the current branch:

``` bash
git push origin main
```

Use your actual branch name if it is not `main`.

## 17. Security Considerations

-   Passwords are hashed using BCrypt; do not store plaintext passwords.
-   Public registration must not allow a client to choose elevated
    roles.
-   JWT secrets and database credentials must remain outside Git.
-   The access token is held in React memory, not localStorage.
-   The refresh token is delivered in an HttpOnly cookie; only its hash
    is stored in the database.
-   Frontend route guards are not a replacement for backend
    authorization.
-   Enforce record ownership in backend services and controllers.
-   The local development cookie currently uses `secure(false)` because
    the local app uses HTTP. Production must use HTTPS and
    `secure(true)`.
-   CORS must use explicit trusted origins when credentials are enabled.

## 18. Known Limitations and Future Hardening

Before production use, address the following:

1.  **CSRF protection:** The described development SecurityConfig
    disables CSRF. Because refresh and logout use cookies, implement and
    test an appropriate CSRF defense before production. `SameSite=Lax`
    is useful but should not be treated as a complete CSRF strategy.
2.  **Concurrent refresh requests:** Add single-flight refresh handling
    so simultaneous 401 responses share one refresh operation. Otherwise
    concurrent requests may race while rotating the same refresh token.
3.  **Refresh-token reuse detection:** A revoked token replay should be
    detected and can trigger revocation of the related token
    family/session.
4.  **Refresh-session management:** Consider tracking token families,
    device/session metadata, and per-session revocation.
5.  **Expired-token cleanup:** Add scheduled cleanup for expired
    refresh-token rows.
6.  **Rate limiting:** Protect login and refresh endpoints against
    brute-force and abuse.
7.  **Centralized errors:** Standardize API error responses using a
    global exception handler.
8.  **Automated tests:** Add unit and integration tests for
    authentication, refresh rotation, logout and role access.
9.  **Production configuration:** Use HTTPS, secure cookies, managed
    secrets and production-specific CORS origins.
10. **Audit logging:** Record security-sensitive actions without logging
    credentials or raw tokens.

## 19. Roadmap

The initial project plan is 12 weeks. The next planned development phase
is Employee Management.

  -----------------------------------------------------------------------
  Phase                   Scope                   Status
  ----------------------- ----------------------- -----------------------
  1                       Project setup,          Completed
                          React/Spring Boot       
                          foundation, MySQL and   
                          API integration         

  2                       Backend registration,   Completed
                          login, JWT and RBAC     

  3                       Frontend                Completed
                          authentication,         
                          protected routes,       
                          refresh sessions and    
                          logout                  

  4                       Employee Management     Next

  5                       Departments and         Planned
                          Designations            

  6                       Attendance Management   Planned

  7                       Leave Management        Planned

  8                       Payroll Management      Planned

  9                       Performance Management  Planned

  10                      Dashboard and Reports   Planned

  11                      Testing, optimization   Planned
                          and UI polish           

  12                      Docker, CI/CD,          Planned
                          deployment and final    
                          documentation           
  -----------------------------------------------------------------------

## 20. Documentation

Keep implementation reports in the `docs/` directory. The Phase 3 report
should describe the frontend authentication integration and
refresh-token lifecycle.

Suggested documentation files:

-   `docs/Phase_1_Project_Setup_Report.docx`
-   `docs/Phase_2_Authentication_Authorization_Report.docx`
-   `docs/WorkSphere_Phase_3_Implementation_Report.docx`

Only include report files that actually exist in the repository.

------------------------------------------------------------------------

**WorkSphere -- Enterprise HR & Workforce Management System**\
Built with React, Java, Spring Boot and MySQL.
