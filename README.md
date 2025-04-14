# 🐾 **The Social Petwork Server** — Final Sprint

## 📝 **Project Description**
**The Social Petwork Server** powers a social networking platform for pet lovers to connect, share stories, and interact. Built with **Spring Boot** and **MySQL**, the backend provides secure, RESTful APIs for managing users, posts, comments, and more — including **personalized profile avatars** and robust user authentication.

---

## 🚀 **Getting Started**

### ✅ Prerequisites
- Java OpenJDK 23
- Maven
- MySQL Server
- Postman (for API testing)

### 📦 Installation

1. **Clone the Repository**
```bash
git clone https://github.com/yourusername/Social-Petwork-Server.git
cd Social-Petwork-Server
```

2. **Create the MySQL Database**
```sql
CREATE DATABASE social_petwork;
```

3. **Configure Application Properties**
   In `src/main/resources/application.properties`, update with your local database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/social_petwork
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. **Run the Application**
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🌐 **Core API Endpoints**

### 👥 **User Management**
- `POST /users/register` – Register a new user
- `POST /users/login` – Authenticate and return a JWT token
- `GET /users` – Retrieve all users
- `GET /users/{id}` – Get user by ID
- `PUT /users/{id}` – Update user
- `DELETE /users/{id}` – Admin-only user deletion

### 📸 **Avatar Upload**
- `POST /users/{id}/upload-avatar`
    - Accepts image files (jpg, png, gif, webp)
    - Max size: 5MB
    - Auto-resizes and compresses to 400x400 using Thumbnailator
    - Secure UUID file names, old avatar cleanup, static hosting ready

### 📝 **Posts**
- `GET /posts` – Get all posts
- `GET /posts/{id}` – Get a specific post
- `POST /posts` – Create a post
- `PUT /posts/{id}` – Update post
- `DELETE /posts/{id}` – Delete post

### 💬 **Comments**
- `GET /comments` – Get all comments
- `POST /comments` – Add comment to a post

---

## 🛠️ **Security Features**
- Role-based access control (ADMIN / USER)
- JWT-based authentication
- Restricted access to protected routes
- Secure file upload validation (MIME type, size, extensions)
- Filename randomization and cleanup logic

---

## 🧪 **Testing**

Run backend unit tests with:
```bash
mvn test
```

---

## 🤝 **Contributing**

1. Fork this repo
2. Create your branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m "Feature description"`
4. Push to your branch: `git push origin feature-name`
5. Open a Pull Request

---

## 📂 **Project Structure Highlights**
```
📁 controller/
    └── UserController.java
📁 entity/
    └── User.java, Post.java, Comment.java
📁 repository/
    └── UserRepository.java, PostRepository.java
📁 service/
    └── UserService.java
📁 auth/
    └── JwtUtil.java, JwtAuthFilter.java
📁 uploads/
    └── avatar/  ← Uploaded avatar files
```

---

## ✨ **Team & Acknowledgments**
- Built by the **Social Petwork** Final Sprint Team:
    - Angela Smith
    - Angelia Romanchuk
    - Beth-Ann Penney
    - Victoria Breen

Special thanks to our mentors and instructors for their guidance and support.

---

## 📄 **License**
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
