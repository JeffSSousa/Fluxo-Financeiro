# Fluxo Development Roadmap

    This roadmap represents the planned evolution of the Fluxo platform through
    incremental delivery cycles. Each milestone is designed to introduce business
    value while continuously improving architecture, security, scalability,
    testing, and operational maturity.

    The roadmap may evolve as new requirements, technical improvements, and product
    opportunities are identified throughout the development lifecycle.

## Infrastructure

### Development Environment

 - [x] Spring Boot project setup
 - [x] Environment configuration
 - [x] H2 in memory database
 - [x] Spring Security Configuration
 - [x] Exception Handling
 - [x] MVC architecture definition
 - [x] Initial project documentation
 - [x] Development profile configuration
 - [x] MapStruct configuration

 ### Homologation Environment

 - [ ] PostgreSQL database configuration
 - [ ] Postgres Docker container
 - [ ] Fluxo API containerization
 - [ ] Test environment configuration
 - [ ] Front-end tests
 - [ ] Protect Swagger documentation

 ### Production Environment


 ## MVP

 ### Authentication & User Management

 - [x] JWT authentication
 - [x] Register Account
 - [x] View Profile
 - [x] Alter Password
 - [x] Encrypted password

 ### Financial Management

 - [x] Expense Managment
 - [x] Income Managment
 - [x] Category Managment
 - [x] View upcoming due dates
 - [x] View All Transactions

 ### Dashboard

 - [x] View account summary
 - [x] View monthly summary

 ### Business Rules

 - [x] Standardize exceptions
 - [x] Implements validations
 - [x] Validate access authorization

 ### Quality and infrastructure

 - [ ] Refactor the architecture to a Package-by-Feature architecture
 - [ ] Clean up boilerplates
 - [ ] Refactor inconsistent names
 - [ ] Implement Flyway
 - [x] Logs and observability

 ### Documentation
 
 - [x] Implement tags in Swagger documentation
 - [x] Implement operations in Swagger documentation
 - [ ] Implement ApiResponses in Swagger documentation