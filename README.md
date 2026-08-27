# Fluxo - Financial Management System

A financial management platform developed with Java and Spring Boot, focused on
maintainability, security, automated testing, and scalable software architecture.

The project is evolving toward a **Package-by-Feature architecture**, organizing
the application around business features and domain boundaries. This approach
is intended to improve maintainability while establishing clearer boundaries
for a potential future evolution toward a **microservices architecture**.

## Environments

| Environment | Status |
|-------------|--------|
| Development | ✅ Active |
| Homologation | 🚧 Planned |
| Production | 🚧 Planned |

For the complete roadmap and future milestones, see the dedicated documentation:

[ROADMAP.md](./ROADMAP.md)


## Table of Contents

- [About](#about)
- [Business](#business)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [About the Author](#-about-the-author)



## About

Fluxo is a personal financial management platform designed to help users
organize and manage their finances through an intuitive and reliable
application.

The project is being developed with a strong focus on software architecture,
maintainability, security, automated testing, and production readiness.

The backend is currently being evolved toward a **Package-by-Feature
architecture**, organizing the application around business features and clearer
domain boundaries. This structure is intended to support maintainability and
facilitate a potential future evolution toward a **microservices architecture**.

The project follows an incremental development process, where new functionality
is introduced alongside automated tests, architectural improvements,
documentation, and infrastructure evolution.

## Business

### Current Features

- Financial control
- Income management
- Expense management
- Category management
- Dashboard
- Transaction management
- Upcoming due dates
- Authentication
- User profile management

### Planned Features

- Reports
- Notifications
- Credit management
- Goal tracking
- WhatsApp chatbot

## Technologies Used

### Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- H2
- JWT
- Bean Validation
- MapStruct

### Testing

- JUnit
- Mockito

### Documentation

- Swagger/OpenAPI

### Build

- Maven


## Project Structure

## Project Structure

The project follows a **Package-by-Feature architecture**, organizing the
application around business features rather than technical layers.

This structure improves separation of responsibilities and creates clearer
boundaries between business domains, making the application easier to maintain
and evolve.

```text
    src/main/java/com/jeffssousa/fluxo
    │
    ├── account/          # Account summary and financial overview
    ├── auth/             # Authentication and user registration
    ├── category/         # Category management
    ├── expense/          # Expense management
    ├── income/           # Income management
    ├── profile/          # User profile management
    ├── transaction/      # Transaction queries and management
    │
    ├── config/           # Application and framework configuration
    ├── exception/        # Global and business exception handling
    └── security/         # Authentication and authorization configuration
```

Each business feature contains the components related to its own domain,
such as controllers, DTOs, services, repositories, entities, and mappers.

### Example:
```text
expense/
    │
    ├── controller/
    ├── dto/
    ├── entity/
    ├── mapper/
    ├── repository/
    └── service/
```

The architecture is being structured as a modular monolith, with the goal of
maintaining clear domain boundaries and facilitating a potential future
evolution toward a microservices architecture.


## How to Run

### Prerequisites

Before running the application, make sure you have installed:

- Java 21+
- Maven 3.9+
- Git

### Clone the Repository

```bash
git clone https://github.com/JeffSSousa/Fluxo-Financeiro.git
cd Fluxo-Financeiro
```

### Environment Variables

The application requires a JWT secret to generate and validate authentication
tokens.


Configure the following environment variable:

```properties
JWT_SECRET=your-super-secret-jwt-key
```

The application reads this value through application.yml:

```yaml
jwt:
  secret: ${JWT_SECRET}
```


> **Note:** For security reasons, sensitive information such as JWT secrets should never be hardcoded into the source code or committed to the repository.



### Run the application

Using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

You can also run the main application class directly from your IDE.


### Development Environment

The application currently runs in the development environment using an
embedded H2 database.

Current configuration:

- Database: H2 In-Memory
- Profile: dev
- Authentication: JWT
- API Documentation: Swagger/OpenAPI
- Containerization: Planned

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

> Note: PostgreSQL, Docker, CI/CD, and cloud deployment will be introduced in future
development cycles according to the project roadmap.


<br><br>

---

## 🙋 About the Author

Developed by Jefferson Sousa  
[GitHub](https://github.com/JeffSSousa) | [LinkedIn](https://www.linkedin.com/in/jefferson-sousa-8b93a81a2/)