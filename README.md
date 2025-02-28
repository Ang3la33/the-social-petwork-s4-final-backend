# 📦 **Social Petwork Server**

## 📝 **Project Description**
The **Social Petwork Server** is the backend service for **The Social Petwork**, a social networking platform that connects pet lovers. This server provides RESTful APIs for managing posts, comments, followers, and user profiles using **Spring Boot** and **MySQL**.

---

## 🚀 **Getting Started**

### **Prerequisites**
- **Java OpenJDK 23**
- **Maven**
- **MySQL Server**
- **Postman** (for API testing)

### **Installation**

1. **Clone the Repository**
```bash
git clone https://github.com/yourusername/Social-Petwork-Server.git
cd Social-Petwork-Server
```

2. **Setup Database**
```sql
CREATE DATABASE social_petwork;
```

3. **Configure Application Properties**
Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/social_petwork
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. **Build and Run the Application**
```bash
mvn clean install
mvn spring-boot:run
```

5. **API Testing**
- Use **Postman** or any API testing tool to interact with the server.

---

## 📚 **API Endpoints**

### 📝 **Posts**
- `GET /posts` - Retrieve all posts
- `GET /posts/{id}` - Retrieve a post by ID
- `POST /posts` - Create a new post
- `PUT /posts/{id}` - Update an existing post
- `DELETE /posts/{id}` - Delete a post

### 💬 **Comments**
- `GET /comments` - Retrieve all comments
- `POST /comments` - Add a comment to a post

### 👥 **Users**
- `GET /users` - Retrieve all users
- `POST /users` - Register a new user

---

## 🧪 **Testing**

Run unit tests with:
```bash
mvn test
```

---

## 💡 **Contributing**

1. **Fork the repository**
2. **Create a new branch:** `git checkout -b feature-name`
3. **Commit your changes:** `git commit -m 'Add some feature'`
4. **Push to the branch:** `git push origin feature-name`
5. **Open a Pull Request**

---

## 📄 **License**

This project is licensed under the MIT License.

---

## ✨ **Acknowledgments**
- The amazing **Social Petwork** development team!
- Angela Flynn-Smith, Angelia Romanchuk, Beth-Ann Penney, & Victoria Breen
