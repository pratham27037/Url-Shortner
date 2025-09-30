# URL Shortener API 🚀

A simple **URL Shortener** built with **Spring Boot** and **SQL database**.
It allows users to shorten long URLs and redirect to the original URL via a short code.

---

## 📂 Project Structure

```
src/main/java/com/pratham/shortner
│── controller       # REST Controllers
│   └── UrlController.java
│
│── service          # Business logic
│   └── UrlService.java
│
│── repository       # Database layer
│   └── UrlRepository.java
│
│── model            # Entity + DTOs
│   └── Url.java
│   └── UrlRequest.java
│   └── UrlResponse.java
│
│── util             # Utility classes (Base62 encoder, etc.)
│   └── Base62.java
│
│── ShortnerApplication.java   # Main Spring Boot Application
```

---

## ⚙️ Configurations

* **application.properties**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

## 📌 API Endpoints

### 1️⃣ Shorten URL

**POST** `/api/url`
Request:

```json
{
  "longUrl": "https://www.example.com/very/long/link"
}
```

Response:

```json
{
  "shortUrl": "http://localhost:8080/api/url/abc123",
  "longUrl": "https://www.example.com/very/long/link"
}
```

---

### 2️⃣ Redirect to Original URL

**GET** `/api/url/{shortCode}`

Example:

```
GET http://localhost:8080/api/url/abc123
```

Redirects → `https://www.example.com/very/long/link`

---

## 🧪 Running Tests

Run unit tests with:

```bash
./mvnw test
```

---

## 🚀 Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/url-shortener.git
   cd url-shortener
   ```

2. Configure **MySQL** in `application.properties`.

3. Run the app:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Test the API using **Postman / cURL**.

---

## 📖 Future Enhancements

* Expiry time for URLs
* Analytics (click count, last accessed)
* User authentication
* Rate limiting

---
