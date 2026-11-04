# WorkSphere -- Enterprise HR & Workforce Management System

WorkSphere is a modular, role-based Human Resource Management System
(HRMS) designed to manage employee information and core workforce
operations. This repository contains the project implementation
completed through **Phase 4**.

> **Project status:** Phases 1--4 completed. Phase 5 (Attendance
> Management) is planned and has not yet been implemented.

## Table of Contents

-   [Project Overview](#project-overview)
-   [Technology Stack](#technology-stack)
-   [Architecture](#architecture)
-   [Implemented Features](#implemented-features)
-   [User Roles and Authorization](#user-roles-and-authorization)
-   [Project Structure](#project-structure)
-   [Prerequisites](#prerequisites)
-   [Local Setup](#local-setup)
-   [Environment Configuration](#environment-configuration)
-   [Running the Application](#running-the-application)
-   [API Overview](#api-overview)
-   [Profile Image Storage](#profile-image-storage)
-   [Testing and Verification](#testing-and-verification)
-   [Security Notes](#security-notes)
-   [Roadmap](#roadmap)
-   [Git Workflow](#git-workflow)

## Project Overview

**Name:** WorkSphere -- Enterprise HR & Workforce Management System

**Architecture:** Modular monolith with a React frontend and Spring Boot
REST API.

**Current implementation:** Project setup, authentication and
authorization, React authentication integration, and
department/designation/employee/profile management.

WorkSphere is being developed as a portfolio-level full-stack
application. The backend exposes REST endpoints under `/api/v1`, while
the frontend consumes them through Axios.

## Technology Stack

  Layer                       Technologies
  --------------------------- -------------------------------------------------
  Frontend                    React 19, JavaScript, Vite
  Styling and UI              Tailwind CSS v4, Lucide React, Sonner
  Routing and state           React Router, Context API
  HTTP client                 Axios
  Backend                     Java 21, Spring Boot, Maven
  Persistence                 Spring Data JPA, Hibernate
  Database                    MySQL 8
  Security                    Spring Security, JWT, BCrypt, refresh tokens
  Validation                  Jakarta Bean Validation
  API design                  REST
  Planned API documentation   Swagger / OpenAPI
  Planned testing             JUnit 5, Mockito, Vitest, React Testing Library
  Planned delivery            Docker, GitHub Actions

## Architecture

The application uses a **modular monolith**. Frontend and backend are
separate applications in one repository.

``` text
WorkSphere/
├── worksphere-backend/       # Java 21 + Spring Boot REST API
├── worksphere-frontend/      # React + Vite frontend
├── database/                 # Database-related scripts/documentation
├── docs/                     # Project documentation
├── .gitignore
└── README.md
```

Backend package root:

``` text
com.project.WorkSphere
```

The API base path is:

``` text
/api/v1
```

Local development addresses:

  Service               URL
  --------------------- ------------------------------
  React frontend        http://localhost:5173
  Spring Boot backend   http://localhost:8085
  REST API base         http://localhost:8085/api/v1
  MySQL                 localhost:3306

## Implemented Features

### Phase 1 -- Project Setup and Integration

-   Created the React frontend using Vite.
-   Created the Spring Boot backend using Java 21 and Maven.
-   Created the MySQL database `worksphere_db`.
-   Configured JPA/Hibernate database connectivity.
-   Added a test REST endpoint and configured CORS for local frontend
    development.
-   Connected React to Spring Boot using Axios and verified a JSON
    response.
-   Initialized a Git monorepo and added a root `.gitignore`.

### Phase 2 -- Authentication and Authorization

-   User registration and login endpoints.
-   Password hashing using BCrypt.
-   JWT access-token generation and validation.
-   Stateless Spring Security configuration.
-   JWT request filter using `OncePerRequestFilter`.
-   Role-based authorization for `SUPER_ADMIN`, `HR_MANAGER`, and
    `EMPLOYEE`.
-   Refresh tokens generated using secure random bytes.
-   SHA-256 hashes stored in the database instead of raw refresh tokens.
-   Refresh-token rotation, logout revocation, and HttpOnly
    refresh-token cookie.
-   Protected dashboard endpoints for the three roles.

Authentication endpoints:

  -------------------------------------------------------------------------
  Method                  Endpoint                  Purpose
  ----------------------- ------------------------- -----------------------
  POST                    `/api/v1/auth/register`   Register a standard
                                                    employee account

  POST                    `/api/v1/auth/login`      Authenticate and issue
                                                    tokens

  POST                    `/api/v1/auth/refresh`    Rotate refresh token
                                                    and issue a new access
                                                    token

  POST                    `/api/v1/auth/logout`     Revoke refresh token
                                                    and clear cookie
  -------------------------------------------------------------------------

Public registration assigns the `EMPLOYEE` role. Elevated roles must not
be self-assigned through public registration.

### Phase 3 -- React Authentication Integration

-   Integrated the frontend with backend authentication APIs.
-   Added an Axios API instance with credentials enabled.
-   Added authentication service methods.
-   Implemented `AuthContext` for user, access-token and session state.
-   Restored sessions through the refresh endpoint during application
    initialization.
-   Added Axios request and response interceptors for Bearer-token
    attachment and retry after access-token expiry.
-   Added protected routes and role-aware navigation.
-   Added login, registration and role-specific dashboard routes.
-   Added employee profile navigation and dashboard layout.
-   Verified login, logout, role access and browser-refresh session
    persistence.

### Phase 4 -- Department, Designation, Employee and Profile Management

#### Department management

-   Department entity and repository.
-   Create, retrieve, update and deactivate operations.
-   Duplicate department-name validation.
-   Active-department retrieval.
-   Role restrictions for administrative changes.

#### Designation management

-   Designation entity and repository.
-   Create, retrieve, update and deactivate operations.
-   Active designation retrieval and validation.
-   Role restrictions for administrative changes.

#### Employee management

-   Employee entity linked to a user account.
-   Employee code uniqueness validation.
-   Department and designation associations.
-   Employment status and gender enums.
-   Employee creation, retrieval, update and deactivation.
-   Validation that linked users exist, are active and are not already
    assigned to another employee record.
-   Employee summary endpoint.
-   Backend role restrictions for administrative employee operations.

#### Employee search and pagination

`GET /api/v1/employees/search` supports:

-   Text search across first name, last name, employee code and user
    email.
-   Department filter.
-   Designation filter.
-   Employment-status filter.
-   Pagination.
-   Sorting by a selected field and direction.
-   Combining multiple filters.

Example:

``` http
GET /api/v1/employees/search?search=Rahul&status=ACTIVE&page=0&size=5&sortBy=firstName&direction=asc
```

#### Employee self-service profile

-   Authenticated employee profile retrieval.
-   Employee profile update for permitted personal fields.
-   Profile update validation.
-   Employee self-service restrictions: employees cannot update their
    employee code, account email, department, designation, joining date,
    employment status or salary through the self-profile endpoint.

Endpoints:

  ------------------------------------------------------------------------
  Method                  Endpoint                 Purpose
  ----------------------- ------------------------ -----------------------
  GET                     `/api/v1/employees/me`   Retrieve the
                                                   authenticated employee
                                                   profile

  PUT                     `/api/v1/employees/me`   Update permitted
                                                   personal profile fields
  ------------------------------------------------------------------------

#### Profile image management

-   Upload profile image as multipart form data.
-   Allowed image extensions and declared MIME types: JPG/JPEG, PNG and
    WebP.
-   Maximum upload size: 2 MB.
-   Image content validation using Apache Commons Imaging.
-   UUID-based stored filenames.
-   Image retrieval through an authenticated endpoint.
-   Image replacement and best-effort deletion of the previous file.
-   Image removal and database-reference cleanup.
-   Upload files stored locally under
    `worksphere-backend/uploads/profiles/`.
-   Upload directory excluded from Git.

Endpoints:

  --------------------------------------------------------------------------------------
  Method                  Endpoint                               Purpose
  ----------------------- -------------------------------------- -----------------------
  POST                    `/api/v1/employees/me/profile-image`   Upload or replace
                                                                 profile image

  GET                     `/api/v1/employees/me/profile-image`   Retrieve authenticated
                                                                 employee profile image

  DELETE                  `/api/v1/employees/me/profile-image`   Remove authenticated
                                                                 employee profile image
  --------------------------------------------------------------------------------------

#### Error handling

A global exception handler returns structured error responses for
handled errors, including validation errors, unreadable request bodies,
oversized uploads and generic server failures. Oversized profile image
uploads are mapped to HTTP 413.

#### Employee profile frontend

-   Profile details display.
-   Edit profile form.
-   Client-side field validation and field-level error messages.
-   Profile image preview and upload.
-   Profile image removal.
-   Profile completion percentage.
-   Loading skeleton.
-   Sonner success/error notifications.
-   Disabled controls while saving and duplicate-submit guard.
-   Responsive layouts for mobile, tablet and desktop.

## User Roles and Authorization

  Role            General access
  --------------- -----------------------------------
  `SUPER_ADMIN`   Administrative access
  `HR_MANAGER`    HR and employee-management access
  `EMPLOYEE`      Employee self-service access

Examples of role-protected dashboard endpoints:

  ------------------------------------------------------------------------------
  Method                  Endpoint                       Allowed roles
  ----------------------- ------------------------------ -----------------------
  GET                     `/api/v1/admin/dashboard`      SUPER_ADMIN

  GET                     `/api/v1/hr/dashboard`         SUPER_ADMIN, HR_MANAGER

  GET                     `/api/v1/employee/dashboard`   SUPER_ADMIN,
                                                         HR_MANAGER, EMPLOYEE
  ------------------------------------------------------------------------------

Authorization must always be enforced by the backend. Frontend route
protection is for user experience and is not a replacement for backend
access control.

## Project Structure

The exact set of files may evolve as modules are added. The high-level
structure is:

``` text
WorkSphere/
├── worksphere-backend/
│   ├── src/main/java/com/project/WorkSphere/
│   │   ├── auth/
│   │   ├── config/
│   │   ├── department/
│   │   ├── designation/
│   │   ├── employee/
│   │   ├── role/
│   │   ├── security/
│   │   └── storage/
│   ├── src/main/resources/
│   ├── uploads/               # Local runtime files; not committed
│   └── pom.xml
├── worksphere-frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── routes/
│   │   ├── services/
│   │   └── utils/
│   ├── .env                   # Local environment file; not committed
│   └── package.json
├── database/
├── docs/
├── .gitignore
└── README.md
```

## Prerequisites

Install the following before running the project:

-   Java Development Kit (JDK) 21.
-   Maven (or use the Maven wrapper if present).
-   Node.js compatible with the installed Vite version.
-   npm.
-   MySQL Server 8.
-   Git.

## Local Setup

### 1. Clone the repository

``` bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd WorkSphere
```

### 2. Create the MySQL database

``` sql
CREATE DATABASE worksphere_db;
```

### 3. Configure backend properties

Create or update:

`worksphere-backend/src/main/resources/application.properties`

Use your own local credentials and JWT secret. Example template:

``` properties
spring.application.name=WorkSphere
server.port=8085

spring.datasource.url=jdbc:mysql://localhost:3306/worksphere_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB
app.upload.profile-dir=uploads/profiles

jwt.secret=YOUR_BASE64_ENCODED_SECRET
jwt.expiration=900000
jwt.refresh-expiration=604800000
```

This is a configuration template, not a production-ready
secret-management strategy. Preserve any additional properties already
required by the current application. Never commit real database
credentials or JWT secrets.

### 4. Configure frontend environment

Create:

`worksphere-frontend/.env`

``` env
VITE_API_BASE_URL=http://localhost:8085/api/v1
```

Do not commit `.env`.

### 5. Install frontend dependencies

``` bash
cd worksphere-frontend
npm install
```

### 6. Start the backend

Open a terminal:

``` bash
cd worksphere-backend
mvn spring-boot:run
```

If the project includes Maven Wrapper, use `mvnw.cmd spring-boot:run` on
Windows.

### 7. Start the frontend

Open another terminal:

``` bash
cd worksphere-frontend
npm run dev
```

Open http://localhost:5173.

## Environment Configuration

Keep local secrets out of source control.

Recommended ignored files and directories include:

``` gitignore
# Backend secrets / local config
worksphere-backend/src/main/resources/application.properties

# Frontend environment files
worksphere-frontend/.env
worksphere-frontend/.env.*

# Runtime uploads
worksphere-backend/uploads/

# Build output and dependencies
**/target/
**/node_modules/
**/dist/

# IDE and OS files
.idea/
.vscode/
*.iml
.DS_Store
Thumbs.db
```

If your project intentionally tracks a safe example environment file,
name it `.env.example` and ensure it contains placeholders only. Review
the existing root `.gitignore` before replacing it.

## Running the Application

Run MySQL first, then the backend, then the frontend.

-   Backend: http://localhost:8085
-   Frontend: http://localhost:5173
-   API prefix: http://localhost:8085/api/v1

The authentication and employee endpoints require the expected
authentication and role permissions. Use an API client such as Postman
for backend verification.

## API Overview

The following endpoints are implemented as of Phase 4. Exact
request/response DTO fields should be checked in the source code.

  ---------------------------------------------------------------------------------------------
  Module            Method            Endpoint                                Access
  ----------------- ----------------- --------------------------------------- -----------------
  Auth              POST              `/api/v1/auth/register`                 Public

  Auth              POST              `/api/v1/auth/login`                    Public

  Auth              POST              `/api/v1/auth/refresh`                  Refresh cookie

  Auth              POST              `/api/v1/auth/logout`                   Refresh cookie

  Dashboard         GET               `/api/v1/admin/dashboard`               SUPER_ADMIN

  Dashboard         GET               `/api/v1/hr/dashboard`                  SUPER_ADMIN,
                                                                              HR_MANAGER

  Dashboard         GET               `/api/v1/employee/dashboard`            Authenticated
                                                                              roles

  Department        POST              `/api/v1/departments`                   SUPER_ADMIN,
                                                                              HR_MANAGER

  Department        GET               `/api/v1/departments`                   Authenticated
                                                                              roles

  Department        GET               `/api/v1/departments/active`            Authenticated
                                                                              roles

  Department        GET               `/api/v1/departments/{id}`              Authenticated
                                                                              roles

  Department        PUT               `/api/v1/departments/{id}`              SUPER_ADMIN,
                                                                              HR_MANAGER

  Department        PATCH             `/api/v1/departments/{id}/deactivate`   SUPER_ADMIN,
                                                                              HR_MANAGER

  Designation       Various           `/api/v1/designations/**`               As configured by
                                                                              controller

  Employee          POST              `/api/v1/employees`                     SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          GET               `/api/v1/employees`                     SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          GET               `/api/v1/employees/{id}`                SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          PUT               `/api/v1/employees/{id}`                SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          PATCH             `/api/v1/employees/{id}/deactivate`     SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          GET               `/api/v1/employees/active`              SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          GET               `/api/v1/employees/summaries`           SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          GET               `/api/v1/employees/search`              SUPER_ADMIN,
                                                                              HR_MANAGER

  Employee          GET               `/api/v1/employees/me`                  Authenticated
                                                                              roles

  Employee          PUT               `/api/v1/employees/me`                  Authenticated
                                                                              roles

  Profile image     POST              `/api/v1/employees/me/profile-image`    Authenticated
                                                                              roles

  Profile image     GET               `/api/v1/employees/me/profile-image`    Authenticated
                                                                              roles

  Profile image     DELETE            `/api/v1/employees/me/profile-image`    Authenticated
                                                                              roles
  ---------------------------------------------------------------------------------------------

## Profile Image Storage

Profile images are currently stored on the backend filesystem, using the
configured `app.upload.profile-dir`. In the local development setup this
is `uploads/profiles`.

The upload directory is runtime data and is excluded from Git. A fresh
clone will not contain previously uploaded profile images. For a
production deployment, use persistent storage or an object-storage
service and configure backups, access controls and lifecycle policies.

## Testing and Verification

The completed development checks include:

-   React-to-Spring Boot test endpoint integration.
-   Registration and login.
-   BCrypt password verification.
-   JWT-protected endpoint access.
-   Role-based access checks for admin, HR and employee roles.
-   Refresh-token rotation, logout and browser session restoration.
-   Department and designation operations.
-   Employee creation, retrieval, update, deactivation and search
    filters.
-   Employee self-profile retrieval and update.
-   Profile image upload, retrieval, replacement and removal.
-   Invalid file rejection and 2 MB upload-size handling.
-   Profile form validation and error display.
-   Profile completion percentage updates.
-   Responsive profile page checks at mobile, tablet and desktop sizes.
-   Final Phase 4 regression checklist completed by the developer.

Run backend tests with:

``` bash
cd worksphere-backend
mvn test
```

Run frontend checks using the scripts defined in
`worksphere-frontend/package.json`, for example:

``` bash
cd worksphere-frontend
npm run
```

Only scripts that are actually defined in `package.json` can be run.

## Security Notes

-   Use BCrypt for password hashing; passwords must never be stored in
    plain text.
-   Public registration must not allow users to grant themselves
    elevated roles.
-   Keep access tokens short-lived and refresh tokens protected.
-   Store only hashed refresh-token values in the database.
-   Keep JWT secrets and database credentials outside Git.
-   Enforce role and ownership checks on the backend.
-   The frontend route guard does not replace backend authorization.
-   Profile image uploads must be size-limited and content-validated.
-   Local profile image files are not committed to the repository.
-   The current local refresh-cookie settings and CSRF configuration
    require production hardening. Use HTTPS, secure cookies and an
    appropriate CSRF strategy for cookie-authenticated endpoints.
-   Review exception handling, logging, rate limiting, token reuse
    detection and file-storage controls before production deployment.

## Roadmap

  -----------------------------------------------------------------------
  Phase                   Scope                   Status
  ----------------------- ----------------------- -----------------------
  Phase 1                 Project setup, database Completed
                          and frontend/backend    
                          integration             

  Phase 2                 Authentication, JWT,    Completed
                          refresh tokens and RBAC 

  Phase 3                 React authentication    Completed
                          integration and         
                          protected routes        

  Phase 4                 Department,             Completed
                          designation, employee   
                          and profile management  

  Phase 5                 Attendance management   Planned

  Phase 6                 Leave management        Planned

  Phase 7                 Payroll management      Planned

  Phase 8                 Performance management  Planned

  Phase 9                 Dashboard,              Planned
                          notifications and       
                          reports                 

  Phase 10                Frontend polish and     Planned
                          user experience         

  Phase 11                Testing and             Planned
                          optimization            

  Phase 12                Docker, CI/CD,          Planned
                          deployment and          
                          documentation           
  -----------------------------------------------------------------------

Planned modules are roadmap items and should not be interpreted as
implemented features.

## Git Workflow

Before pushing changes:

``` bash
git status
```

Review the staged files and verify that no secrets, local environment
files, database dumps or uploaded profile images are included.

``` bash
git add .
git status
git commit -m "Complete Phase 4: Employee Management and Profile"
git push origin main
```

If the branch is not `main`, push the current branch instead.

## License

Add a license file if you intend to distribute this project publicly.
Until a license is added, do not assume that others have permission to
reuse the source code.
