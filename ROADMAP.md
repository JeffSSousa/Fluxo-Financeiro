# Fluxo Development Roadmap

This roadmap represents the planned evolution of the Fluxo platform through
incremental development cycles.

The roadmap may evolve as new requirements, technical improvements, and product
opportunities are identified throughout the development lifecycle.

---

# Sprint 0 — Project Review

**Objective:** Review the current implementation before starting the next development cycle.

- [ ] Review entities and relationships
- [ ] Review DTOs
- [ ] Review controllers
- [ ] Review services
- [ ] Review repositories
- [ ] Review mappers
- [ ] Review exception handling
- [ ] Review JWT/Security
- [ ] Review validations
- [ ] Review existing endpoints
- [ ] Test current application flows
- [ ] Review Swagger
- [ ] Review README

---

# Sprint 0.5 — Package-by-Feature Architecture

**Objective:** Refactor the application from a layer-based package structure into a Package-by-Feature architecture without changing system behavior.

### Target Structure

```text
com.fluxo
│
├── auth
├── account
├── transaction
├── income
├── expense
├── category
├── profile
│
├── security
├── exception
└── config
```

- [ ] Define application features
- [ ] Create package structure
- [ ] Migrate authentication
- [ ] Migrate account
- [ ] Migrate transactions
- [ ] Migrate income
- [ ] Migrate expense
- [ ] Migrate category
- [ ] Migrate profile
- [ ] Organize feature-specific DTOs
- [ ] Organize feature-specific services
- [ ] Organize feature-specific repositories
- [ ] Organize feature-specific mappers
- [ ] Organize shared components
- [ ] Update imports
- [ ] Update test packages
- [ ] Validate Spring configuration
- [ ] Validate Security
- [ ] Validate Swagger
- [ ] Run complete test suite

**Rule:** No new business functionality should be introduced during this sprint.

---

# Sprint 1 — Transaction Lifecycle

**Objective:** Evolve income and expense management into a complete financial transaction lifecycle.

### Expenses

- [ ] Define `dueDate`
- [ ] Define `paidAt`
- [ ] Define transaction status
- [ ] Implement `PENDING`
- [ ] Implement `PAID`
- [ ] Implement `OVERDUE`
- [ ] Implement `CANCELED`
- [ ] Endpoint to mark expense as paid
- [ ] Endpoint to update transaction status
- [ ] Implement status transition rules

### Income

- [ ] Define income status
- [ ] Define receiving date
- [ ] Support pending income
- [ ] Support received income
- [ ] Implement status rules

---

# Sprint 2 — Filtering, Sorting and Pagination

**Objective:** Make financial data manageable as the number of transactions increases.

- [ ] Pagination
- [ ] Sorting
- [ ] Filter by date range
- [ ] Filter by category
- [ ] Filter by transaction type
- [ ] Filter by status
- [ ] Pagination for income
- [ ] Pagination for expenses
- [ ] Pagination for categories when necessary
- [ ] Define maximum page size

Example:

```http
GET /transactions?startDate=2026-08-01&endDate=2026-08-31&categoryId=3&status=PAID&page=0&size=20
```

---

# Sprint 3 — Financial Dashboard

**Objective:** Provide a meaningful overview of the user's financial situation.

### Account Summary

- [ ] Current balance
- [ ] Total income
- [ ] Total expenses
- [ ] Period balance
- [ ] Pending income
- [ ] Pending expenses
- [ ] Overdue expenses

### Year Summary

- [ ] Income by month
- [ ] Expenses by month
- [ ] Balance by month
- [ ] Annual totals
- [ ] Income and expense comparison

---

# Sprint 4 — Due Date Management

**Objective:** Make due-date management a core part of the financial workflow.

- [ ] View upcoming due dates
- [ ] View overdue transactions
- [ ] View recently paid transactions
- [ ] Sort transactions by due date
- [ ] Filter by period
- [ ] Mark transaction as paid
- [ ] Identify overdue transactions
- [ ] Calculate days until due date

---

# Sprint 5 — Category Management

**Objective:** Complete category management and enforce category business rules.

- [ ] Create category
- [ ] Update category
- [ ] Delete category
- [ ] List categories
- [ ] Separate income and expense categories
- [ ] Prevent duplicate categories
- [ ] Validate category existence
- [ ] Define category deletion behavior
- [ ] Prevent deletion of categories in use
- [ ] Evaluate soft delete strategy
- [ ] Define default categories

---

# Sprint 6 — Security and Business Rules

**Objective:** Prepare the application for production by strengthening security and business rules.

### Security

- [ ] Ensure user data isolation
- [ ] Prevent access to other users' data
- [ ] Protect authenticated endpoints
- [ ] Implement JWT expiration
- [ ] Handle invalid tokens
- [ ] Handle expired tokens
- [ ] Correct `401 Unauthorized` responses
- [ ] Correct `403 Forbidden` responses
- [ ] Secure password hashing
- [ ] Prevent sensitive data exposure

### Business Rules

- [ ] Validate monetary values
- [ ] Validate dates
- [ ] Validate categories
- [ ] Validate resources
- [ ] Validate transaction status
- [ ] Validate update operations
- [ ] Validate delete operations
- [ ] Validate required fields
- [ ] Validate status transitions

---

# Sprint 7 — Automated Testing

**Objective:** Increase confidence in the application and make future changes safer.

### Unit Tests

- [ ] Services
- [ ] Business rules
- [ ] Financial calculations
- [ ] Validations
- [ ] Status transitions

### Integration Tests

- [ ] Controllers
- [ ] Repositories
- [ ] PostgreSQL
- [ ] Security
- [ ] JWT authentication

### Critical Scenarios

- [ ] Register user
- [ ] Login
- [ ] Create income
- [ ] Create expense
- [ ] Update income
- [ ] Update expense
- [ ] Delete income
- [ ] Delete expense
- [ ] Mark expense as paid
- [ ] Retrieve dashboard
- [ ] Retrieve due dates
- [ ] Create category

### Negative Scenarios

- [ ] Unauthenticated request
- [ ] Non-existent resource
- [ ] Access to another user's resource
- [ ] Non-existent category
- [ ] Invalid request data
- [ ] Expired token
- [ ] Invalid monetary values
- [ ] Invalid transaction status

---

# Sprint 8 — Docker

**Objective:** Make the application environment reproducible and suitable for deployment.

### Backend

- [ ] Dockerfile
- [ ] Application build
- [ ] Environment variables

### Database

- [ ] PostgreSQL container
- [ ] Docker Compose
- [ ] Database persistence

### Configuration

- [ ] Development profile
- [ ] Production profile
- [ ] JWT secret through environment variables
- [ ] Database credentials through environment variables
- [ ] Ensure secrets are not committed to Git

---

# Sprint 9 — CI/CD

**Objective:** Automate application validation and delivery.

### Pipeline

```text
git push
   ↓
GitHub
   ↓
GitHub Actions
   ↓
Build
   ↓
Tests
   ↓
Docker Build
   ↓
Docker Registry
   ↓
Deploy
```

- [ ] Create CI workflow
- [ ] Configure Maven build
- [ ] Run automated tests
- [ ] Build application
- [ ] Build Docker image
- [ ] Push Docker image
- [ ] Configure secrets
- [ ] Create deployment workflow

---

# Sprint 10 — Backend Production Deployment

**Objective:** Make the Fluxo API available through the internet.

- [ ] Select cloud/server environment
- [ ] Deploy backend
- [ ] Configure production PostgreSQL
- [ ] Configure environment variables
- [ ] Configure HTTPS
- [ ] Configure domain/subdomain
- [ ] Configure CORS
- [ ] Configure firewall
- [ ] Secure database access

### Target Architecture

```text
Internet
   ↓
HTTPS
   ↓
Fluxo API
   ↓
Spring Boot
   ↓
PostgreSQL
```

---

# Sprint 11 — Production Readiness

**Objective:** Prepare the application for continuous operation in production.

- [ ] Application logging
- [ ] Structured logs
- [ ] Health checks
- [ ] Monitoring
- [ ] Production error handling
- [ ] PostgreSQL backup strategy
- [ ] Recovery strategy
- [ ] Basic rate limiting
- [ ] Security headers
- [ ] Production CORS configuration
- [ ] Deployment documentation
- [ ] Production API documentation

---

# Sprint 12 — Frontend Foundation

**Objective:** Establish the frontend architecture and API integration.

- [ ] Create frontend project
- [ ] Define frontend structure
- [ ] Define design system
- [ ] Implement responsive layout
- [ ] Create API client
- [ ] Implement error handling
- [ ] Implement loading states
- [ ] Authentication integration
- [ ] JWT handling
- [ ] Logout
- [ ] Protected routes

---

# Sprint 13 — Authentication UI

- [ ] Login
- [ ] Registration
- [ ] Logout
- [ ] Form validation
- [ ] Error handling
- [ ] Session expiration
- [ ] Redirect handling

---

# Sprint 14 — Dashboard UI

- [ ] Account summary
- [ ] Year summary
- [ ] Financial chart
- [ ] Upcoming due dates
- [ ] Overdue expenses
- [ ] Loading states
- [ ] Empty states
- [ ] Error states

---

# Sprint 15 — Income UI

- [ ] Income list
- [ ] Create income
- [ ] Edit income
- [ ] Delete income
- [ ] Filters
- [ ] Pagination
- [ ] Categories
- [ ] Status
- [ ] User feedback

---

# Sprint 16 — Expense UI

- [ ] Expense list
- [ ] Create expense
- [ ] Edit expense
- [ ] Delete expense
- [ ] Filters
- [ ] Pagination
- [ ] Categories
- [ ] Status
- [ ] Mark as paid
- [ ] Due dates

---

# Sprint 17 — Transactions UI

```text
All | Income | Expenses
```

- [ ] Transaction list
- [ ] Filters
- [ ] Search
- [ ] Sorting
- [ ] Pagination
- [ ] Date range filter
- [ ] Category filter
- [ ] Status filter

---

# Sprint 18 — Categories UI

- [ ] Category list
- [ ] Create category
- [ ] Edit category
- [ ] Delete category
- [ ] Validation
- [ ] User feedback

---

# Sprint 19 — Profile UI

- [ ] User profile
- [ ] Editable user information
- [ ] Change password
- [ ] Logout
- [ ] Validation
- [ ] Error handling

---

# Sprint 20 — Real User Validation

**Objective:** Validate the application through real usage.

- [ ] Use the application regularly
- [ ] Create realistic financial data
- [ ] Test on mobile
- [ ] Test on desktop
- [ ] Identify UX problems
- [ ] Fix application errors
- [ ] Fix inconsistencies
- [ ] Improve user messages
- [ ] Improve loading states
- [ ] Improve empty states
- [ ] Improve navigation

### Main Question

> Can a person use Fluxo without requiring assistance from the developer?

---

# Sprint 21 — Portfolio and Documentation

**Objective:** Document Fluxo as a complete production-oriented application.

### README

- [ ] Problem
- [ ] Solution
- [ ] Features
- [ ] Architecture
- [ ] Technologies
- [ ] Database
- [ ] Security
- [ ] Testing
- [ ] Docker
- [ ] CI/CD
- [ ] Deployment
- [ ] Screenshots
- [ ] Application URL
- [ ] API URL
- [ ] Swagger documentation

### GitHub

- [ ] Organized commits
- [ ] Branch strategy
- [ ] Pull Requests where appropriate
- [ ] Issues
- [ ] Releases
- [ ] Git tags
- [ ] `v1.0.0`

---

# Development Principles

- Keep the application as a well-structured monolith before introducing distributed architecture.
- Preserve existing behavior during architectural refactoring.
- Keep existing tests during Package-by-Feature migration.
- Update tests when business behavior changes.
- Prioritize real product functionality over unnecessary technologies.
- Introduce new technologies only when they solve a real problem.
