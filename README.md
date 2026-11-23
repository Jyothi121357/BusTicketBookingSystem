# MicroProject
# MicroProject: Bus Ticket Booking System

A console-based Java application connected to a Supabase (PostgreSQL) database. 

## Features:
- **Role-based access:** Separate menus for Admins and regular Users.
- **Admin Features:** Add new buses, view all buses, delete buses, and view all customer bookings across the system.
- **User Features:** Register, login, view available buses, book tickets (with overbooking prevention), view personal bookings, and cancel bookings.
- **Robustness:** Input validation is implemented to prevent the application from crashing on invalid (non-numeric) input.

## Tech Stack:
- **Language:** Java
- **Database:** Supabase (PostgreSQL)
- **Connectivity:** JDBC (Java Database Connectivity)

---

## 🚀 How to Run This Project Locally

### 1. Prerequisites
- Java JDK installed (Java 8 or higher).
- A Supabase account (or any PostgreSQL database).

### 2. Database Setup
Go to your Supabase SQL Editor and run the following commands to create the required tables:

```sql
-- Create Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20) DEFAULT 'user'
);

-- Create Buses Table
CREATE TABLE buses (
    id SERIAL PRIMARY KEY,
    bus_number VARCHAR(50),
    capacity INT,
    operator VARCHAR(100)
);

-- Create Bookings Table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    bus_id INT REFERENCES buses(id),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);