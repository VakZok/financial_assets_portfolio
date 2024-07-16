@echo off

REM Start Spring Boot Application
start cmd /c "cd ./backend/ && mvnw spring-boot:run"

REM Wait for Spring Boot to start (adjust the sleep time accordingly)
timeout /t 10

REM Start Angular Frontend
start cmd /c "cd ./frontend/ && ng serve"