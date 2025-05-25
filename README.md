
![Screenshot 2025-05-25 174602](https://github.com/user-attachments/assets/5dd9176f-f681-4e36-8e34-b96c6dc0c94d)
![Screenshot 2025-05-25 174552](https://github.com/user-attachments/assets/5cbf16b3-cf6b-462f-9dea-abef281ec8ad)
# SmartKart - Java GUI Shopping Application

## Project Description

SmartKart is a feature-rich desktop shopping application built with Java Swing that provides a seamless shopping experience. It's designed to help users browse products, manage their shopping cart, and complete purchases efficiently.

### Key Features

- 🛒 **User Authentication**: Secure login and registration system
- 🏪 **Product Catalog**: Browse available products with images and details
- 🛍️ **Shopping Cart**: Add/remove items and manage quantities
- 💳 **Checkout Process**: Simple and secure payment flow
- 📊 **Admin Dashboard**: Manage products and view sales statistics
- 📱 **Responsive Design**: Adapts to different screen sizes

### Target Users

- General consumers looking for an easy shopping experience
- Small business owners managing their product inventory

## Technologies Used

### Core Technologies

- **Java**: JDK 17
- **UI Framework**: Java Swing
- **Database**: MySQL 8.0+
- **Database Connectivity**: JDBC
- **Build Tool**: None (Raw JDK)
- **Version Control**: Git

### Development Tools

- **IDE**: IntelliJ IDEA / VS Code
- **Database Management**: MySQL Workbench
- **Version Control**: GitHub

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17**
  - [Download JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
  - Set `JAVA_HOME` environment variable
- **MySQL Server 8.0+**
  - [Download MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
  - Install with default settings
  - Note down root password
- **MySQL Workbench (Recommended)**
  - [Download MySQL Workbench](https://www.mysql.com/products/workbench/)
- **Git**
  - [Download Git](https://git-scm.com/downloads)
- **IDE (Optional but Recommended)**
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - [VS Code](https://code.visualstudio.com/)
  - [Eclipse](https://www.eclipse.org/downloads/)

### 🛠️ Installation & Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/Nirbhay0007/SmartKart-Java-GUI.git
   cd SmartKart-Java-GUI
   ```

2. **Set up the database**

   - Start MySQL server
   - Open MySQL Workbench
   - Create a new connection if needed
   - Run the SQL script from `smartkart_schema.sql`

3. **Configure database connection**
   - Open `src/com/smartkart/util/DatabaseConnection.java`
   - Update the following with your MySQL credentials:
     ```java
     private static String dbHost = "localhost";
     private static String dbPort = "3306";
     private static String dbName = "smartkart_db";
     private static String dbUser = "root";
     private static String dbPassword = "your_password";
     ```

## 🗃️ Database Setup

### Database Schema

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS smartkart_db;
USE smartkart_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100),
    address TEXT,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    category VARCHAR(50),
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    shipping_address TEXT,
    payment_method VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Sample data
INSERT INTO products (name, description, price, stock_quantity, category) VALUES
('Laptop', 'High-performance laptop with 16GB RAM', 999.99, 50, 'Electronics'),
('Smartphone', 'Latest smartphone with 128GB storage', 699.99, 100, 'Electronics'),
('Headphones', 'Wireless noise-canceling headphones', 199.99, 75, 'Accessories'),
('Desk Chair', 'Ergonomic office chair', 249.99, 30, 'Furniture'),
('Coffee Maker', 'Programmable coffee machine', 89.99, 40, 'Appliances');
```

### Database Configuration

1. **MySQL Server Setup**

   - Install MySQL Server 8.0+
   - Start the MySQL service
   - Note down your root password

2. **Create Database**

   - Open MySQL Workbench
   - Connect to your local MySQL server
   - Run the SQL script provided above

3. **Update Configuration**
   - Locate `DatabaseConnection.java` in the project
   - Update the connection details:
     ```java
     private static String dbHost = "localhost";
     private static String dbPort = "3306";
     private static String dbName = "smartkart_db";
     private static String dbUser = "root";
     private static String dbPassword = "your_mysql_password";
     ```

## 🚀 Running the Project

### Option 1: Using the Launcher (Windows)

1. Navigate to the project directory
2. Double-click on `start_smartkart.bat`
   - This will:
     - Compile all Java source files
     - Download MySQL Connector/J if needed
     - Start the application
     - Initialize the database if not already done

## 🏗️ Project Structure & Code Quality

### Project Structure

```
src/
├── com/
│   └── smartkart/
│       ├── MainApp.java           # Application entry point
│       │
│       ├── controller/           # Business logic and flow control
│       │   ├── AuthController.java    # Authentication logic
│       │   ├── CartController.java    # Shopping cart operations
│       │   └── ProductController.java # Product management
│       │
│       ├── dao/                  # Data Access Objects
│       │   ├── CartDao.java          # Cart database operations
│       │   ├── ProductDao.java       # Product database operations
│       │   └── UserDao.java          # User database operations
│       │
│       ├── model/                # Data models
│       │   ├── CartItem.java         # Cart item model
│       │   ├── Product.java          # Product model
│       │   └── User.java             # User model
│       │
│       ├── util/                 # Utility classes
│       │   ├── DatabaseConnection.java  # Database connection handler
│       │   ├── DatabaseInitializer.java # Database setup
│       │   └── PasswordUtils.java      # Password hashing
│       │
│       └── view/                 # GUI components
│           ├── CartView.java         # Shopping cart UI
│           ├── LoginView.java        # Login screen
│           ├── MainFrame.java        # Main application window
│           └── ProductListView.java  # Product catalog
```

### Code Quality Standards

✅ **MVC Architecture**

- Clear separation between Model, View, and Controller components
- Business logic separated from presentation layer

✅ **Code Organization**

- Logical package structure
- Consistent naming conventions (PascalCase for classes, camelCase for methods/variables)
- Proper file organization

✅ **Documentation**

- Comprehensive JavaDoc for all public classes and methods
- Inline comments for complex logic
- README with setup and usage instructions

✅ **Error Handling**

- Try-catch blocks for database operations
- Input validation
- User-friendly error messages

### Component Placement

#### 1. Login/Registration

- Centered card layout
- Clear form fields with proper labels
- Helpful error messages
- Easy navigation between login and registration

#### 2. Main Dashboard

- **Top Navigation**: App logo, search bar, user menu
- **Sidebar**: Navigation menu with icons
- **Content Area**: Dynamic content based on selection
- **Status Bar**: Shows current user and system status

#### 3. Product Catalog

- Grid layout for products
- Filter and sort options
- Product cards with images and key details
- Quick add to cart button

#### 4. Shopping Cart

- Clear item list with thumbnails
- Quantity controls
- Order summary
- Checkout button

### Responsiveness & Accessibility

- **Responsive Layout**:

  - Adapts to different window sizes
  - Collapsible sidebar on smaller screens
  - Responsive grid for product listings

- **Keyboard Navigation**:

  - Tab navigation between form fields
  - Enter key to submit forms
  - Arrow keys for dropdowns and lists

- **Accessibility Features**:

  - High contrast mode
  - Keyboard shortcuts
  - Screen reader support for all interactive elements
  - Proper ARIA labels

- **User Feedback**:
  - Clear loading indicators
  - Success/error messages
  - Hover effects on interactive elements
  - Disabled states for unavailable actions

## Key Features

### User Authentication

- [x] Secure user registration and login
- [x] Password hashing with salt
- [x] Session management
- [x] Remember me functionality

### Product Management

- [x] Browse products by categories
- [x] Search functionality
- [x] Product details view
- [x] Product filtering and sorting

### Shopping Cart

- [x] Add/remove items
- [x] Update quantities
- [x] Save cart between sessions
- [x] Apply discount codes

### Checkout Process

- [x] Multi-step checkout
- [x] Shipping information
- [x] Payment integration (simulated)
- [x] Order confirmation

### User Profile

- [x] View order history
- [x] Update personal information
- [x] Manage addresses
- [x] Change password

### Admin Features

- [x] Product management (CRUD)
- [x] User management
- [x] Order management
- [x] Sales reports

## Future Enhancements

### High Priority

- [ ] Implement payment gateway integration (Stripe/PayPal)
- [ ] Add product reviews and ratings
- [ ] Email notifications for orders

### Medium Priority

- [ ] Wishlist functionality
- [ ] Product recommendations
- [ ] Order tracking
- [ ] Multi-language support

## Developer

Nirbhay - [@Nirbhay0007](https://github.com/Nirbhay0007)  
Adarsh
Hetish
Yashasvi
