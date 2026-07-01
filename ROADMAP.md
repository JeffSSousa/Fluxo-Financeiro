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

 ### Homologation Environment

 - [ ] PostgreSQL database configuration
 - [ ] Postgres Docker container
 - [ ] Test environment configuration
 - [ ] Front-end tests
 - [ ] Protect Swagger documentation
 - [ ] MapStruct configuration

 ### Production Environment


 ## MVP

 ### Authentication & User Management

 - [ ] JWT authentication
 - [ ] Register Account
 - [ ] View Profile
 - [ ] Alter Password
 - [ ] Encrypted password

 ### Financial Management

 - [ ] Expense Managment
 - [ ] Income Managment
 - [ ] Category Managment
 - [ ] View upcoming due dates
 - [ ] View All Transactions

 ### Dashboard

 - [ ] View account summary
 - [ ] View monthly summary

 ### Business Rules

 - [ ] Standardize exceptions
 - [ ] Implements validations
 - [ ] Validate access authorization

 ### Quality and infrastructure

 - [ ] Refactor the architecture to a Package-by-Feature architecture
 - [ ] Clean up boilerplates
 - [ ] Refactor inconsistent names
 - [ ] Implement Flyway
 - [ ] Logs and observability

 ### Documentation
 
 - [ ] Implement tags in Swagger documentation
 - [ ] Implement operations in Swagger documentation
 - [ ] Implement ApiResponses in Swagger documentation