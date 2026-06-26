# Fluxo - Financial Management System

    A modern financial management platform developed using Java and Spring Boot
    adhering to software engineering best practices and Clean Architecture
    principles, including automated testing, authentication and authorization,
    JWT-based security, and documentation using Swagger/OpenAPI.


## Environments

| Environment | Status |
|------------|--------|
| Development | ✅ |
| Homologation | 🚧 Planned |
| Production | 🚧 Planned |

    Current milestone: development Environment
    Next milestone:  

For the complete roadmap and future milestones, see the dedicated documentation:

[ROADMAP.md](/ROADMAP.md)


### Table of Contents
- [About](#about)
- [Business](#business)
- [Technologies used](#technologies-used)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [About the Autor](#-about-the-author)



## About

Fluxo is a personal financial management platform designed to help users manage their finances through an intuitive and scalable application.

The project is being developed with a strong focus on software architecture, maintainability, security and cloud-native practices.

Although still under development, the project follows a production-oriented development lifecycle, including multiple environments, versioning strategy and engineering best practices.

## Business

- Financial control
- Income management
- Expense management
- Dashboard
- Reports
- Authentication
- User management
- Notifications
- Credit management
- Goal tracking
- WhatsApp chatbot implementation
- Cloud deployment

## Technologies used

- Java
- Spring Boot
- Spring Security
- PostgreSQL
- H2 Console
- JWT
- Maven
- JUnit
- Mockito
- Swagger/OpenAPI
- Bean Validation
- Mapstruct


## Project Structure

The project currently follows the MVC (Model-View-Controller) architectural pattern, with a strong focus on separation of concerns, maintainability and scalability.

    
    src/main/java/com/jeffssousa/fluxo
    │
    │
    ├── config          # Application and framework configurations
    ├── controller      # REST API endpoints
    ├── dto             # Data Transfer Objects
    ├── entities        # Domain entities and JPA models
    ├── enums           # Enumerations used throughout the application
    ├── exceptions
    │   ├── business    # Business exceptions
    │   └── handler     # Global exception handlers
    ├── mapper          # Entity and DTO mappings
    ├── repository      # Data access layer
    ├── security        # Authentication and authorization
    └── service         # Business rules and application services

This structure provides a clear separation between presentation, business and persistence layers, allowing the project to evolve toward more advanced architectural patterns in future versions.


## How to Run

### Prerequisites

Before running the application, make sure you have installed:

* Java 21+
* Maven 3.9+
* Git

### Clone the repository

```bash
git clone https://github.com/your-username/fluxo.git

cd fluxo
```

### Environment Variables

Before running the application, configure the JWT secret key in your environment variables.

The application expects the following property in the `application.yml` file:

```yaml
jwt:
  secret: ${JWT_SECRET}
```

Create an environment variable named `JWT_SECRET` and assign a secure secret key value.

Example:

```bash
JWT_SECRET=your-super-secret-jwt-key
```

This key is used by the application to generate and validate JWT authentication tokens.

> **Note:** For security reasons, sensitive information such as JWT secret keys should never be hardcoded into the source code or committed to the repository.


### Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or run the main class directly from your IDE.


### Development Environment

The project is currently running in the **development environment**, using an embedded H2 database for rapid development and testing.

Current configuration:

* Database: H2 In-Memory
* Profile: dev
* Authentication: JWT
* API Documentation: Under development
* Containerization: Planned for future versions

### Access H2 Console

After starting the application, access:

```text
http://localhost:8080/h2-console
```

Default connection settings:

```text
JDBC URL: jdbc:h2:mem:fluxodb
User: sa
Password:
```

### API Base URL

```text
http://localhost:8080
```

> Note: Docker, PostgreSQL, CI/CD pipelines and cloud deployment will be introduced in future versions as part of the project's roadmap.


<br><br>

---

## 🙋 About the Author

Developed by Jefferson Sousa  
[GitHub](https://github.com/JeffSSousa) | [LinkedIn](https://www.linkedin.com/in/jefferson-sousa-8b93a81a2/)