# Personal Finance Management API

A RESTful API built with Spring Boot for managing personal finances, tracking expenses, setting budgets, and monitoring financial goals.

## Features

- User Authentication: Secure JWT-based authentication and authorization
- Category Management: Create and manage income/expense categories
- Transaction Tracking: Record and monitor all financial transactions
- Budget Management: Set monthly budgets and track spending
- Real-time Budget Monitoring: Automatic calculation of spent amounts and remaining budget
- Date-based Filtering: Query transactions by date range or specific months

## Tech Stack

- Backend: Spring Boot 3.4.0
- Security: Spring Security with JWT
- Database: PostgreSQL 18
- ORM: Hibernate/JPA
- Build Tool: Maven
- Java Version: 17

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 18
- Maven 3.9+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/CoderParva/finance-tracker-api.git
cd finance-tracker-api
```

2. Configure PostgreSQL database:
```sql
CREATE DATABASE finance_tracker;
```

3. Update application.properties with your database credentials

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

The API will start on http://localhost:8080

## API Endpoints

### Authentication
- POST /api/auth/register - Register new user
- POST /api/auth/login - User login

### Categories (Protected)
- POST /api/categories - Create category
- GET /api/categories - Get all categories
- GET /api/categories/{id} - Get by ID
- PUT /api/categories/{id} - Update category
- DELETE /api/categories/{id} - Delete category

### Transactions (Protected)
- POST /api/transactions - Create transaction
- GET /api/transactions - Get all transactions
- GET /api/transactions/{id} - Get by ID
- GET /api/transactions/monthly?month=11&year=2025 - Get by month
- PUT /api/transactions/{id} - Update transaction
- DELETE /api/transactions/{id} - Delete transaction

### Budgets (Protected)
- POST /api/budgets - Create budget
- GET /api/budgets - Get all budgets
- GET /api/budgets/{id} - Get by ID
- GET /api/budgets/monthly?month=11&year=2025 - Get by month
- PUT /api/budgets/{id} - Update budget
- DELETE /api/budgets/{id} - Delete budget

## Security

- All endpoints except /api/auth/** require JWT authentication
- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours
- CORS enabled for cross-origin requests

## Author

Parva Nachan
- GitHub: @CoderParva
- Email: parvanachan2005@gmail.com

## License

This project is open source and available under the MIT License.