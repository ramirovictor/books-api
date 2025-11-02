# Books API - Spring Boot REST API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen)
![H2](https://img.shields.io/badge/Database-H2-blue)
![HTTPS](https://img.shields.io/badge/Protocol-HTTPS-red)

Fullstack portfolio project: **Books CRUD REST API** with Java + Spring Boot + H2 running on HTTPS, consumed by a React frontend with Vite + TypeScript + TailwindCSS.

---

## 📋 Description

Complete book management system featuring:
- RESTful backend with Java 17 + Spring Boot 3.x
- H2 in-memory database (PostgreSQL mode)
- HTTPS communication with self-signed certificate
- Automatic API documentation with Swagger UI
- React + Vite + TypeScript frontend ([separate repository](https://github.com/ramirovictor/books-frontend))

---

## 🛠️ Backend Technologies

| Technology | Version | Description |
|-----------|---------|-------------|
| **Java** | 17 | Main language |
| **Spring Boot** | 3.2.4 | Web framework |
| **Spring Data JPA** | - | Data persistence |
| **Spring Validation** | - | Data validation |
| **H2 Database** | - | In-memory database |
| **Lombok** | - | Reduce boilerplate |
| **springdoc-openapi** | 2.3.0 | Swagger/OpenAPI 3.0 documentation |
| **Maven** | - | Dependency management |

---

## 📁 Project Structure

```
books-api/
├── src/
│   ├── main/
│   │   ├── java/books/api/
│   │   │   ├── BooksApiApplication.java      # Main class
│   │   │   ├── bootstrap/
│   │   │   │   └── DataLoader.java           # Initial data loader
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java           # CORS configuration
│   │   │   │   └── SwaggerConfig.java        # Swagger configuration
│   │   │   ├── domain/
│   │   │   │   └── Book.java                 # JPA entity
│   │   │   ├── dto/
│   │   │   │   ├── BookRequest.java          # Request DTO
│   │   │   │   └── BookResponse.java         # Response DTO
│   │   │   ├── mapper/
│   │   │   │   └── BookMapper.java           # Entity <-> DTO conversion
│   │   │   ├── repository/
│   │   │   │   └── BookRepository.java       # Data access
│   │   │   ├── service/
│   │   │   │   └── BookService.java          # Business logic
│   │   │   └── web/
│   │   │       └── BookController.java       # REST endpoints
│   │   └── resources/
│   │       ├── application.properties        # Configuration
│   │       └── keystore.p12                  # SSL certificate
│   └── test/
│       ├── java/books/api/
│       │   ├── service/BookServiceTest.java  # TODO: Unit tests
│       │   └── web/BookControllerTest.java   # TODO: Integration tests
│       └── resources/
│           └── application-test.properties
├── pom.xml
└── README.md
```

---

## 🔒 HTTPS Configuration

The project uses a self-signed SSL certificate. If it doesn't exist yet, generate it with:

```bash
keytool -genkeypair -alias booksapi -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore src/main/resources/keystore.p12 \
  -validity 3650 -storepass changeit \
  -dname "CN=localhost, OU=Dev, O=BooksAPI, L=City, ST=State, C=BR"
```

**Current configuration** (`application.properties`):
- **Port**: 8443
- **Keystore**: `classpath:keystore.p12`
- **Password**: `changeit`
- **Alias**: `booksapi`

---

## 🚀 How to Run the Backend

### Prerequisites
- Java 17+
- Maven 3.6+

### Run

```bash
# Via Maven Wrapper (recommended)
./mvnw spring-boot:run

# Or via installed Maven
mvn spring-boot:run
```

The backend will be available at: **https://localhost:8443**

> ⚠️ **First access**: Your browser will show a security warning. Accept the certificate to proceed.

---

## 📚 API Endpoints

**Base URL**: `https://localhost:8443/api/v1/books`

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| `GET` | `/api/v1/books` | List all books (paginated) | 200 |
| `GET` | `/api/v1/books?q=clean` | Search by title (case-insensitive) | 200 |
| `GET` | `/api/v1/books/{id}` | Get book by ID | 200 / 404 |
| `POST` | `/api/v1/books` | Create new book | 201 |
| `PUT` | `/api/v1/books/{id}` | Update existing book | 200 / 404 |
| `DELETE` | `/api/v1/books/{id}` | Delete book | 204 / 404 |

---

## 🧪 Swagger UI

Access the interactive API documentation at:

**https://localhost:8443/swagger-ui/index.html**

or simply:

**https://localhost:8443/swagger**

---

## 🗄️ H2 Console

Web console to view the H2 in-memory database:

**https://localhost:8443/h2**

**Access credentials**:
- **JDBC URL**: `jdbc:h2:mem:booksdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

---

## 🎨 React Frontend

**📦 Frontend Repository**: [https://github.com/ramirovictor/books-frontend](https://github.com/ramirovictor/books-frontend)

### How to run

```bash
# Clone the frontend repository
git clone https://github.com/ramirovictor/books-frontend.git
cd books-frontend

# Install dependencies
npm install

# Run in development mode
npm run dev

# Access: http://localhost:5173
```

> ⚠️ **Important**: Before using the frontend, start the backend (this API) and accept the SSL certificate by accessing `https://localhost:8443` in your browser.

---

## 📊 Initial Data

The project automatically loads 2 books on startup (`DataLoader.java`):

1. **Clean Code** - Robert C. Martin ($120.00)
2. **Refactoring** - Martin Fowler ($150.00)

---

## 🔜 Next Steps

1. Implement unit and integration tests
2. Add global exception handling (ControllerAdvice)
3. Add authentication (Spring Security + JWT)
4. Configure CI/CD
5. Deploy to cloud (Heroku/Railway/Render)

---

## 📝 Important Notes

- H2 database is **in-memory** - data is lost on restart
- SSL certificate is **self-signed** - for development only
- **First frontend request**: May fail with SSL error. Access `https://localhost:8443` in browser and accept the certificate first
- CORS is configured only for `http://localhost:5173`

---

## 📄 License

This project is free for educational and portfolio use.
