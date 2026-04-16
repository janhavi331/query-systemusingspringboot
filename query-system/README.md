# 🎓 Faculty Student Query System
### Dr. Renuka Rajendra Kajale | NMIET, Pune

A full-stack Student Query Management System built with **Spring Boot (backend)** and **HTML/CSS/JavaScript (frontend)**.

---

## 📁 Project Structure

```
query-system/
├── backend/                          ← Spring Boot Application
│   ├── pom.xml
│   └── src/main/java/com/faculty/queryapp/
│       ├── QueryAppApplication.java  ← Main entry point
│       ├── controller/
│       │   ├── QueryController.java  ← REST API endpoints
│       │   └── AppConfig.java        ← CORS + exception config
│       ├── model/
│       │   ├── StudentQuery.java     ← JPA Entity
│       │   ├── QueryRequest.java     ← DTO: student submits query
│       │   ├── AnswerRequest.java    ← DTO: teacher submits answer
│       │   └── ApiResponse.java      ← Generic response wrapper
│       ├── repository/
│       │   └── StudentQueryRepository.java  ← JPA Repository
│       └── service/
│           ├── QueryService.java     ← Business logic
│           └── EmailService.java     ← Email notifications
└── frontend/
    └── index.html                    ← Complete frontend (student + teacher)
```

---

## 🔧 Backend Setup (Spring Boot)

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

H2 Console (dev): `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:querydb`
- Username: `sa` | Password: *(empty)*

---

## 🌐 REST API Endpoints

| Method | URL | Description | Auth Required |
|--------|-----|-------------|--------------|
| `POST` | `/api/queries/add` | Student submits a query | None |
| `GET` | `/api/queries/all?pin={pin}` | Get all queries | Teacher PIN |
| `GET` | `/api/queries/mine?email={email}` | Student's own queries | Email only |
| `POST` | `/api/queries/answer` | Teacher submits answer | Teacher PIN |
| `DELETE` | `/api/queries/{id}?pin={pin}` | Delete a query | Teacher PIN |
| `GET` | `/api/queries/stats` | Dashboard statistics | None |
| `GET` | `/api/queries/health` | Health check | None |

### POST /api/queries/add
```json
{
  "studentEmail": "yourname@nmiet.edu.in",
  "question": "What is the halting problem?",
  "subject": "Theory of Computation"
}
```

### POST /api/queries/answer
```json
{
  "queryId": 1,
  "answer": "The halting problem is undecidable...",
  "teacherPin": "teacher123"
}
```

---

## ⚙️ Configuration (`application.properties`)

```properties
# Change email domain to your college
app.allowed-email-domain=@nmiet.edu.in

# Change teacher PIN (use a stronger one in production!)
app.teacher-pin=teacher123

# Enable email notifications (optional)
app.mail.enabled=false
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

---

## 🖥️ Frontend Setup

Simply open `frontend/index.html` in a browser.

> **Note**: Update `API_BASE` in the JavaScript if your backend runs on a different port.

```javascript
const API_BASE = 'http://localhost:8080/api/queries';
```

The frontend works in **demo mode** even without the backend running.

---

## 🔐 Security Notes

- Teacher PIN is stored in `application.properties` — **change it** in production
- Email domain is validated on both frontend AND backend
- For production: use Spring Security with JWT instead of PIN-based auth
- Enable HTTPS in production

---

## 📧 Email Notifications (Optional Enhancement)

1. Set `app.mail.enabled=true` in `application.properties`
2. Configure SMTP credentials (Gmail example uses App Passwords)
3. Students automatically receive email when teacher answers

---

## 🗄️ Production Database Switch

Replace H2 with MySQL/PostgreSQL:

```xml
<!-- pom.xml -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
</dependency>
```

```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/querydb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

## 👩‍🏫 Teacher Default PIN
```
teacher123
```
*(Change this immediately in production!)*
