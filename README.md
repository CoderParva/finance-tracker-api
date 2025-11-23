# 💰 Personal Finance Management API

A RESTful API built with Spring Boot for managing personal finances, tracking expenses, setting budgets, and monitoring financial goals.

## 🚀 Features

- **User Authentication**: Secure JWT-based authentication and authorization
- **Category Management**: Create and manage income/expense categories
- **Transaction Tracking**: Record and monitor all financial transactions
- **Budget Management**: Set monthly budgets and track spending
- **Real-time Budget Monitoring**: Automatic calculation of spent amounts
- **Date-based Filtering**: Query transactions by date range or specific months

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.4.0
- **Security**: Spring Security with JWT
- **Database**: PostgreSQL 18
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Java Version**: 17

## 📦 Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- PostgreSQL Driver
- Lombok
- JJWT (JSON Web Token)

## 🚦 Getting Started

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

3. Update `application.properties` with your credentials

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## 📚 API Documentation

### Authentication

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "johndoe",
  "email": "john@example.com"
}
```

### Categories

#### Create Category
```http
POST /api/categories
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Food & Dining",
  "type": "EXPENSE",
  "color": "#FF5733",
  "icon": "🍔"
}
```

#### Get All Categories
```http
GET /api/categories
Authorization: Bearer {token}
```

### Transactions

#### Create Transaction
```http
POST /api/transactions
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryId": 1,
  "amount": 500.00,
  "type": "EXPENSE",
  "description": "Lunch with friends",
  "transactionDate": "2025-11-20"
}
```

#### Get All Transactions
```http
GET /api/transactions
Authorization: Bearer {token}
```

#### Get Transactions by Month
```http
GET /api/transactions/monthly?month=11&year=2025
Authorization: Bearer {token}
```

### Budgets

#### Create Budget
```http
POST /api/budgets
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryId": 1,
  "amount": 10000,
  "month": 11,
  "year": 2025
}
```

#### Get Budget Status
```http
GET /api/budgets/monthly?month=11&year=2025
Authorization: Bearer {token}
```

**Response:**
```json
{
  "id": 1,
  "categoryName": "Food & Dining",
  "amount": 10000.00,
  "spent": 500.00,
  "remaining": 9500.00,
  "month": 11,
  "year": 2025
}
```

## 🔐 Security

- All endpoints except `/api/auth/**` require JWT authentication
- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours
- CORS enabled for cross-origin requests

## 📊 Database Schema

### Users
- id, username, email, password, firstName, lastName, createdAt

### Categories
- id, user_id, name, type, color, icon

### Transactions
- id, user_id, category_id, amount, type, description, transactionDate, createdAt

### Budgets
- id, user_id, category_id, amount, month, year, createdAt

## 🚀 Future Enhancements

- [ ] Expense reports and analytics
- [ ] Recurring transactions
- [ ] Data visualization dashboards
- [ ] Export to CSV/PDF
- [ ] Multi-currency support
- [ ] Email notifications for budget alerts

## 👨‍💻 Author

**Parva Nachan**
- GitHub: [@CoderParva](https://github.com/CoderParva)
- Email: parvanachan2005@gmail.com

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- Spring Boot Documentation
- PostgreSQL Documentation
- JWT.io

---

**Built with ❤️ using Spring Boot**
