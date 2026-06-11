# URL Shortener API

A high-performance, production-ready RESTful service built with Spring Boot, PostgreSQL, and Hibernate to shorten long URLs into manageable codes or custom aliases with built-in analytics and expiration management.

---

## 🚀 Key Features

* **URL Shortening**: Generates secure 7-character alphanumeric short-codes using a cryptographic random generator.
* **Collision Resolution**: Resolves URL collisions gracefully with a high-throughput retry algorithm (up to 10,000 attempts).
* **Vanity URLs (Custom Aliases)**: Allows users to define custom shortcodes (between 3 and 20 alphanumeric characters) with duplicate checks and strict input validation.
* **Redirection Engine**: Performs ultra-fast redirection from shortcodes to original URLs using HTTP 302 (Temporary Redirect) statuses.
* **Expiration Management & Policies**:
  * **Lazy Expiration Checks**: Validates TTL (Time-To-Live) on each redirection, serving `404 Not Found` if a URL has expired.
  * **Active Background Cleanup**: Automatically prunes expired URLs from the database at midnight daily via an asynchronous Spring `@Scheduled` cron task.
* **Click Analytics**: Tracks redirection statistics and logs click count metrics for each active shortlink.
* **API Documentation**: Auto-generates a clean, interactive documentation dashboard using **Swagger UI**.

---

## 🛠️ Tech Stack

* **Language**: Java 21
* **Framework**: Spring Boot 4.x / 3.x
* **Database**: PostgreSQL (with indexing on `shortCode`, `createdAt`, and `expiresAt` columns to ensure high-performance query speeds)
* **ORM / Persistence**: Spring Data JPA / Hibernate
* **API Documentation**: Springdoc OpenAPI (Swagger UI)
* **Build Tool**: Maven

---

## 🧠 Learning Outcomes & Backend Concepts

Building this project served as a gateway to learning standard industry practices in backend engineering, including:

1. **Three-Layer Architecture**: Implementing a decoupled layout across Controllers (HTTP interface), Services (business logic & rules), and Repositories (persistence layer).
2. **DTO Pattern**: Separating representation models from database entities using Data Transfer Objects (`UrlRequest`, `UrlCountResponse`) to control inputs safely.
3. **Database Performance & Schema Tuning**: Using composite database indexes to optimize queries on frequently read columns (`shortCode`) and cleanup ranges (`expiresAt`).
4. **Asynchronous & Scheduled Tasks**: Implementing Spring's `@Scheduled` background engine to run database maintenance routines independently from client requests.
5. **Robust Exception Handling**: Constructing centralized REST error handlers using `@RestControllerAdvice` to map exceptions (e.g., `CustomCodeAlreadyExistsException`) to user-friendly HTTP statuses.
6. **API Specifications**: Using Swagger UI to document endpoints, facilitating seamless collaboration between backend developers and frontend clients.

---

## 🛣️ API Endpoints & Swagger Documentation

### Interactive Docs (Swagger UI)
Once the server is running, navigate to the following URL in your browser to inspect, test, and run requests against all endpoints:
👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Rest API Reference

#### 1. Shorten a URL
* **HTTP Method**: `POST`
* **Path**: `/api/url/shorten`
* **Request Body**:
  ```json
  {
    "url": "https://github.com/google",
    "customCode": "google-hub",
    "ttlInSeconds": 86400
  }
  ```
  *(Note: Both `customCode` and `ttlInSeconds` are optional. If no `ttlInSeconds` is provided, a default lifespan of 30 days is applied).*
* **Success Response (200 OK)**:
  `"google-hub"`

#### 2. Redirect User
* **HTTP Method**: `GET`
* **Path**: `/api/url/{shortencode}`
* **Success Response (302 Found)**: Redirects client to the original URL.
* **Error Response (404 Not Found)**: Returned if the shortcode is invalid or has expired.

#### 3. Retrieve Click Count
* **HTTP Method**: `GET`
* **Path**: `/api/url/{shortencode}/count`
* **Success Response (200 OK)**:
  ```json
  {
    "url": "google-hub",
    "count": 142
  }
  ```

---

## ⚙️ Setup & Installation

### Prerequisites
* Java 21+ SDK
* PostgreSQL Server
* Maven (or wrapper script included)

### Database Configuration
1. Create a local PostgreSQL database named `urls`.
2. Update the credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5420/urls
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Execution
Run the following command to start the Spring Boot web application:
```bash
./mvnw spring-boot:run
```
Once started, the backend server will listen at `http://localhost:8080`.