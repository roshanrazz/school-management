# School Management System
- Basic demo project for school management using spring boot microservices architecture.


## Microservices

### User Service (port- 9091)

> One Admin is created while running the user service:<br>
> use this credential to create other users<br>
> **email**: rock@mail.com<br>
> **password**: 12345678

- Authentication and authorization using spring security with jwt
- Student, teacher and admin registration
- Student, teacher and admin are distinguished based on role
- Admin can only register Student and teacher
- List all users and delete user- admin only feature (authorization)
- soft delete used

### Assignment Service (port- 9093)

- Teachers can create assignment and schedule it
- Student can see assignments scheduled
- Spring Scheduler is used to schedule assignment 
- Apache kafka producer used to send events to notification service when assignment created
- Redis used for caching of list of assignemts data

### Notification Service
- Consumes events of apache kafka
- sends email to demo user after assignment is created
- For mail server goto: http://localhost:8025

### Discovery Service (port- 8761)
- Eureka Server for service discovery
- Register all the microservices at one place 

### Gateway Service (port- 9090)
- Spring cloud gateway used for routing
- Authentication and jwt validation
- Route request to other microservices based on pattern matching

## Running 
- First run the discovery service followed by other services
- change the postgresql credentials according to your system
- Run these services using docker or else:
- **Apache Kafka**:
    > docker run -p 9092:9092  apache/kafka    
- **Mailhog**:
    > docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog   
- **Redis**:
    > docker run -p 6379:6379 redis   
