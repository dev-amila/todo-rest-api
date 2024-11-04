# todo-rest-api
This project is a To-Do REST API built with Spring Boot. It uses Docker to run a MySQL database and secures endpoints with Spring Security and JWT (JSON Web Tokens). The application provides CRUD (Create, Read, Update, Delete) operations for managing to-do items.





### Table of Contents
* Features
* Technologies
*  Getting Started
* Configuration
*  Running the Application
*  API Endpoints
*  License

### Technologies

* Java 17
* Spring Boot 3.x
* Spring Security
* JWT for authentication
* Docker (for running MySQL database)
* MySQL
* JPA (Hibernate) for database interaction

## Getting Started

### Prerequisites
* Java 17 or higher installed on your machine.
* Docker and Docker Compose installed for database setup.
* Maven for dependency management and building the application.

**Cloning the Repository**

```bash
git clone https://github.com/AmilaBandara1994/todo-rest-api.git
cd todo-rest-api
```
**Configuration**
1. Database Configuration
   Update the application.properties file in the src/main/resources directory with your preferred database credentials if needed:
2. JWT Configuration
   Set your JWT secret and expiration time in application.properties:


## Running the Application
1. Starting MySQL with Docker
Use Docker Compose to start the MySQL database:

```bash
docker-compose up -d
```
2. Build and Run the Application
Use Maven to build and start the application:

```bash
mvn clean install
mvn spring-boot:run
```
The application will be available at http://localhost:8080.

## API Endpoints
Here are the key endpoints:

* Authentication

    * POST /auth/register - Register a new user.
     ```bash
    {
      "username":"name",
      "password":"12345678",
      "email":"example@gmail.com"
      }
     ```

    * POST /auth/login - Login and retrieve JWT token.
    ```bash
      {
        "email":"examle@gmail.com",
        "password":"12345678"
      }
    ```

* To-Do Management (JWT token required)

  * GET /api/todos - Retrieve all to-do items. this method is retrieve all the todos related with particular user
  here have implemented search facility with pagination in order to call the endpoint 
  ```bash
   url : http://localhost:8080/api/todos?searchText=&page=0&size=10
  ```
  * GET /api/todos/{id} - Retrieve a specific to-do item.
  * POST /api/todos - Create a new to-do item.
    ```bash
      {
      "task": "Example Task",
      "deadline": "2024-11-04T10:00:00Z",
      "priority": "HIGH"
      }
    ```
    createdAt Date will be add through the system. By default isComplete is false once you can toggle that using change status url method
  * PUT /api/todos/changestatus/{id} - can toggle isCompleted field in to-do item.
  * DELETE /api/todos/{id} - Delete a to-do item.
  
### License
  This project is licensed under the MIT License.