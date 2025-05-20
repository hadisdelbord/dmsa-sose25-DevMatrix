# Park and Charge Platform

Welcome to the `dmsa-sose25-DevMatrix` project!

## 🧭 Summary

The **Park and Charge Platform** is a smart city mobility solution for electric vehicle (EV) users and private charging station owners. It enables station owners to offer time slots and prices, while EV drivers can search, book, and pay for charging sessions.

The system ensures real-time availability and provides usage statistics via an interactive dashboard.

---

## 🚀 Features

- **User Management**: Register, log in, and access features based on roles.
- **Charging Station Management**: Station owners can create, read, update, and delete their stations.
- **Offer Management**: Define time intervals and pricing for each charging station.
- **Search and Booking**: EV users can search for available slots and book them.
- **Booking Exclusivity**: Stations are locked for others during active bookings.
- **Usage Statistics**: Station owners can view booking frequency and revenue.
- **Map Integration**: Free stations displayed on a real-time interactive map.
- **Booking Cancellation**: Owners can cancel ongoing bookings.

---

## 🧱 Architecture

This project uses a **Microservices Architecture** following **Domain-Driven Design (DDD)** principles.

### 🔧 Technologies Used

- Java 21
- Spring Boot
- REST APIs
- H2 Database (in-memory)
- Map Integration (Leaflet)
- Maven

### 📦 Microservices

- **User Service** – Handles authentication and profile management.
- **Station Service** – Manages stations and availability slots.
- **Booking Service** – Handles booking creation, updates, and cancellations.
- **Payment Service** – Manages booking payment data.
- **Statistics Service** – Aggregates booking and revenue data.
- **Map Service** – Integrates and displays locations using third-party map APIs.

---

## 🗂️ Domain Model Overview

| Entity           | Fields                                                                 |
|------------------|------------------------------------------------------------------------|
| **User**         | `user_id`, `name`, `email`, `passwordHash`, `role`                    |
| **ChargingStation** | `station_id`, `user_id`, `address_id`, `activity_status`, `power_output` |
| **Offer_Slot**   | `offer_id`, `station_id`, `slot_time`, `date`, `price_per_slot`, `is_available` |
| **Booking**      | `booking_id`, `user_id`, `offer_id`, `booking_status`                 |
| **Payment**      | `payment_id`, `booking_id`, `payment_amount`, `payment_date`, `payment_method` |
| **Address**      | `address_id`, `street`, `city`, `postal_code`, `country`, `latitude`, `longitude` |

---

## 📚 License

This project is part of a university course project and is intended for educational use.
