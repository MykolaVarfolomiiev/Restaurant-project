# Restaurant Management System

A modular restaurant management application built with Spring Boot, Jakarta EE, Spring MVC, and Spring Data JPA.

## Features
- Home page with restaurant information
![home.png](Artifacs/Project%20pics/home.png)
- Restaurant menu and menu item management
![menus.png](Artifacs/Project%20pics/menus.png)
![menu_items.png](Artifacs/Project%20pics/menu_items.png)
- Reservation request handling
![reserve.png](Artifacs/Project%20pics/reserve.png)
![reservation_request.png](Artifacs/Project%20pics/reservation_request.png)
![track_reservation.png](Artifacs/Project%20pics/track_reservation.png)
- Seating and table management
![seatings.png](Artifacs/Project%20pics/seatings.png)
![dining_tables.png](Artifacs/Project%20pics/dining_tables.png)
- Event support
![events.png](Artifacs/Project%20pics/events.png)
- User authentication and authorization
- JWT-based security
- Email notification integration
- REST API and web UI support
- Centralized error handling
- Logging and environment-based configuration

## Tech Stack
- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Jakarta EE
- MySQL
- Thymeleaf
- JWT
- Spring Mail

## Architecture
The project is organized into multiple modules, including:
- `auth` for authentication and user management
- `restaurant` for restaurant business logic
- `common` for shared functionality
- `email` for mail services
- `app-web` and `app-rest-api` for application entry points

## Purpose
This project is designed to manage restaurant operations efficiently, including reservations, seating, menu handling, and secure user access.