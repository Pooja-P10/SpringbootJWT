
# Spring Security Application

This is a simple Spring Security application showcasing basic authentication, user registration with a built-in H2 in-memory database.

## Features
- User registration with encrypted passwords using BCrypt.
- Basic authentication using Spring Security.
- CSRF token generation.
- H2 database integration for user storage.
- Stateless session management.
- REST APIs for managing users and students.

---

## Prerequisites

- Java 17 or later
- Maven 3.6+
- IDE with Spring Boot support (e.g., IntelliJ IDEA, Eclipse)


---
# Config Module

This module contains the configuration for Spring Security in the application.

## Class: SecurityConfig

### Features
- Configures authentication using `DaoAuthenticationProvider`.
- Uses `BCryptPasswordEncoder` for password encryption.
- Defines security filter chain for:
  - Disabling CSRF.
  - Enabling basic authentication.
  - Enforcing stateless session management.

### Key Methods
1. **`authenticationProvider()`**:
   - Configures the `DaoAuthenticationProvider` with a custom `UserDetailsService` and `BCryptPasswordEncoder`.

2. **`securityFilterChain(HttpSecurity http)`**:
   - Sets up the HTTP security configurations, including disabling CSRF, enabling basic authentication, and stateless sessions.

### Dependencies
- Spring Security
- BCrypt

# Controller Module

This module contains REST controllers for handling requests related to users and students.

## Classes

### 1. TaskController
#### Endpoints:
- **`GET /api/tasks`**: Returns a All Tasks as a List from h2 database.
- **`GET /api/tasks/{id}`**: Returns an Particular Task from databse based on id.
- **`POST /api/tasks`**: Returns an created Task in the databse.
- **`PUT /api/tasks/{id}`**: Returns an Update Task in the database.
- **`DELETE /api/tasks/{id}`**: deletes Task from database based on id.

---

### 2. UserController
#### Endpoints:
- **`POST /register`**: Registers a new user by saving them into the database.

---

### Dependencies
- Spring Web

# Models Module

This module contains the data models used in the application.

## Classes

### 1. Task
- **Attributes**:
  - `id`: Unique identifier for the Task.
  - `title`: Task Title.
  - `description`: Task description.
  - `status`:Describes Status of Task.
  - `createdAt`: Created Time when Task is created.

### 2. User
- **Attributes**:
  - `id`: Unique identifier for the user (Primary Key, Auto Increment).
  - `username`: Username of the user (Unique, Non-Nullable).
  - `password`: Password of the user (Encrypted using BCrypt).

---

### 3. UserPrincipal
- Implements `UserDetails` to integrate Spring Security with the `User` entity.
- Provides:
  - User's authorities.
  - Username.
  - Password.
  - Account status flags (all set to `true`).

---

### Dependencies
- Spring Data JPA
- Hibernate

# Repository Module

This module contains the repository interface for database operations.

## Classes

### UserRepo
- Extends `JpaRepository` to perform CRUD operations on the `User` table.
- **Custom Query**:
  - `User findByUsername(String username)`:
    - Fetches a user by their username.

---

### Dependencies
- Spring Data JPA
- H2 Database

# Service Module

This module contains the business logic for the application.

## Classes

### 1. MyUserDetailsService
- Implements `UserDetailsService` to provide user details to Spring Security.
- **Key Method**:
  - `loadUserByUsername(String username)`:
    - Fetches the user from the database using `UserRepo` and wraps it in `UserPrincipal`.

---

### 2. UserService
- Handles user registration and password encryption.
- **Key Method**:
  - `saveUser(User user)`:
    - Encrypts the user's password using `BCryptPasswordEncoder` and saves the user to the database.

---

### Dependencies
- Spring Security
- BCrypt
- Spring Data JPA

# Spring Security Application

The main module to bootstrap the Spring Security application.

## Class: SpringSecurityApplication

- The entry point of the application.
- Annotated with `@SpringBootApplication`.

### Features
- Starts the embedded Tomcat server.
- Initializes the application context.

---

### Dependencies
- Spring Boot

# JWT Integration Module

This module adds JSON Web Token (JWT) functionality to your Spring Security project for stateless authentication.

## Overview

With this enhancement:
- **JWT-based Authentication** is implemented to replace session-based authentication.
- A **JWT Filter** intercepts requests to validate tokens and extract user details.
- Users can authenticate and receive a JWT token using the `/login` endpoint.
- The token is used for accessing secured endpoints.

---

## Key Components

### 1. **JWT Filter**
Class: `JwtFilter`
- Intercepts incoming requests to validate JWT tokens and extract user details.
- Adds `UsernamePasswordAuthenticationToken` to the Spring Security context if the token is valid.

### Key Method:
- **`doFilterInternal()`**:
  - Extracts the JWT token from the `Authorization` header.
  - Validates the token using `JwtService`.
  - Authenticates the user if the token is valid.

---

### 2. **Security Config**
Class: `SecurityConfig`

#### Enhancements:
- Added `JwtFilter` in the Spring Security filter chain.
- Configured `/api/auth/register` and `/api/auth/login` endpoints to be accessible without authentication.

#### Key Additions:
- **`AuthenticationManager` Bean**: For manual user authentication during login.
- **Updated Security Chain**:
  ```java
  http.authorizeHttpRequests(request -> request
      .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
      .anyRequest().authenticated()
  )
  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  .addFilterBefore(JwtFilter, UsernamePasswordAuthenticationFilter.class);

## UserController

The `UserController` handles user registration and login functionalities.

### Endpoints:

1. **`POST /api/auth/register`**: Registers a new user.
   - **Payload**:
     ```json
     {
       "username": "user1",
       "password": "password123"
     }
     ```
   - **Response**: Returns the newly registered user object.

2. **`POST /api/auth/login`**: Authenticates the user and generates a JWT token.
   - **Payload**:
     ```json
     {
       "username": "user1",
       "password": "password123"
     }
     ```
   - **Response**: Returns the JWT token upon successful authentication.
     "eyJhbGciOiJIUzI1NiJ9..."
     ```

---

## JwtService

The `JwtService` is responsible for generating, validating, and extracting information from JWT tokens.

### Key Methods:

1. **`generateToken(String username)`**:
   - Generates a JWT token with the username as the subject and a 3-minute expiration.

2. **`extractUsername(String token)`**:
   - Extracts the username from the token.

3. **`validateToken(String token, UserDetails userDetails)`**:
   - Validates the token against the user’s details and ensures the token has not expired.

4. **`generateSecretKey()`**:
   - Dynamically generates a secret key for signing tokens.

---

## How to Use

1. **Register a Task**:
   - Send a `POST` request to `/register` with the user details.

2. **Login and Get JWT**:
   - Send a `POST` request to `/login` with valid credentials.
   - Receive the JWT token in the response.

3. **Access Secured Endpoints**:
   - Use the JWT token in the `Authorization` header to access secured endpoints:
     ```
     Authorization: Bearer <your-jwt-token>
     ```
---

