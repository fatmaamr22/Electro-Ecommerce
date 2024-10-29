# Electro 💻- A Simple E-Commerce Web Application for Laptops

## Project Overview

**Electro** is a web-based e-commerce application specifically designed for selling laptops. This project was developed to explore core Java web development and Spring technologies including:

- **Client-Side Technologies**: HTML5, CSS3, JavaScript
- **Backend Technologies**: Spring Boot, Spring Security, Spring Data JPA
- **Asynchronous Development**: AJAX
- **Object-Relational Mapping (ORM)**: JPA & Hibernate
- **Database**: MySQL

The project provides a full-featured e-commerce platform with both user and administrator functionalities.

---

## Table of Contents

- [Main Features](#main-features)
    - [General Features](#general-features)
    - [Administrator Functions](#administrator-functions)
    - [User Functions](#user-functions)
- [Technologies Used](#technologies-used)
    - [Frontend](#frontend)
    - [Backend](#backend)
    - [Database](#database)
    - [Asynchronous Development](#asynchronous-development)
- [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Steps](#steps)
- [Usage](#usage)
    - [Admin Panel](#admin-panel)
    - [User Panel](#user-panel)
- [Asynchronous Features](#asynchronous-features)
- [Future Enhancements](#future-enhancements)
- [Contributers](#contributers)
- [Contact](#contact)

---

## Main Features

### General Features
- **Home Page**: A welcoming home page accessible to all users.
- **User Privileges**: Different pages and functionalities are available based on user roles (Admin/User).

### Administrator Functions
- **Manage Products**: View, add, update, and delete laptop products, managing All Products Details.
- **Review Customer Profiles**: View and review all registered customer profiles.
- **Review Customer Orders**: View and review all customer Orders.
- **All Admin Pages contain Pagination for Easier browsing**

### User Functions
- **Registration & Authentication**: Sign up with email or social login (Google), including profile details like name, email, address, and birthday.
- **User Login**: Sign in using email and password.
- **Edit Profile**: Update personal information at any time.
- **Browse Products**: View product catalog with options to filter by category and price range.
- **Filtering Products**: Filtering all products based on their specs.
- **Shopping Cart**: Add or remove laptops from the shopping cart, update quantities, and virtually purchase items within the credit limit.
- **Purchase(Payment Gateway)**: Upon purchase, the user is redirected to a paymob payment gateway to enter his credit card credintials, upon success the order status changes to be paid.

---

## Technologies Used


### Frontend 🎨
- **HTML5**: Structuring the web pages.
- **CSS3**: Styling the application with a responsive, mobile-first approach.
- **JavaScript & JQuery**: Client-side validation and dynamic UI updates.

### Backend 🔧
- **Spring Boot**: Core server-side framework for business logic.
- **Spring Security**: Handles authentication and data security.
- **Spring Data JPA & Hibernate**: ORM for efficient database operations

### Database 🧱
- **MySQL**: Relational database for storing product, user, and order details.

### Asynchronous Development ✨
- **AJAX & JavaScript**: Implemented asynchronous features for enhanced user interactions.


---

## Installation

### Prerequisites
- **Java JDK**: Version 8 or higher
- **Apache Tomcat**: (or other Java servlet container)
- **MySQL**: Database server
- **Maven**: Dependency management

### Steps

1. **Clone the Repository**
    ```bash
    git clone https://github.com/your-username/Electro-Ecommerce.git
    cd Electro-Ecommerce

    ```

2. **Configure the MySQL Database**
    - Create a new database in MySQL.
    - Update the database connection settings in `application.properties` 


3. **Build the Project Using Maven**
    ```bash
    mvn clean install
    ```

4. **Deploy the Project on Apache Tomcat**
    - Copy the generated `.war` file from the `target` directory to the `webapps` folder of your Tomcat installation.


5. **Access the Application**
    - Navigate to `http://localhost:8080/` in your web browser or your custom port number.

---

## Usage

### Admin Panel 🧑‍💻
- **Login**: Admin logs in using predefined credentials (configured in the database).
- **Manage Products**: Admin can add, edit, or delete laptops, view customer profiles and track orders.

### User Panel 🙋‍♀️
- **Sign Up & Profile Management**: New users sign up with personal information and update their own profile.
- **View Products**: Browse available laptops and add them to the shopping cart.
- **Shopping Cart**: Manage cart items, adjust quantities, and proceed to checkout.

---

## Asynchronous Features

1. **Username Availability Check**
    - Real-time username validation during registration.

2. **Shopping Cart Updates**
    - Users can add, remove, or update product quantities in their cart asynchronously, ensuring a smooth user experience without reloading the page.

---


## Future Enhancements

- **User Reviews & Ratings**
    - Allow users to leave reviews and ratings for products.

- **Product Recommendations**
    - Implement a recommendation engine based on user interests and browsing history.

- **Email Notifications**
    - Send order confirmations and updates to users via email.

- **Expanded Payment Options**
    - Introduce additional payment methods, including popular digital wallets and international gateways.

---

## Contributers

* Ahmed Mandour
* Omar Rashid
* Momen Ashraf
* Fatma Amro

---


## Contact

For any inquiries or support, please reach out to
* [fatmaamro34@gmail.com](mailto:fatmaamro34@gmail.com)
* [momen0ashraf@gmail.com](mailto:momen0ashraf@gmail.com)
* [ahmedmandor@rocketmail.com](mailto:ahmedmandor@rocketmail.com)
* [omar.salah.rashid@gmail.com](mailto:omar.salah.rashid@gmail.com)
