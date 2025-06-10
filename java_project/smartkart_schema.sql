-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS smartkart_db;

-- Use the database
USE smartkart_db;

-- Table: users
-- Stores user information, including their hashed passwords.
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL -- Expecting SHA-256 hash (64 hex characters)
);

-- Table: products
-- Stores product details.
CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    image_path VARCHAR(255) NULL -- Path to product image, can be null
);

-- Table: cart
-- Stores items currently in users' shopping carts.
-- Each user can have multiple products, each with a specific quantity.
CREATE TABLE IF NOT EXISTS cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each cart entry
    user_id INT NOT NULL,                   -- Foreign key to the users table
    product_id INT NOT NULL,                -- Foreign key to the products table
    quantity INT NOT NULL,                  -- How many of this product the user has in their cart
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    
    -- Ensures that a user has only one cart entry per product;
    -- quantity should be updated for existing entries.
    UNIQUE KEY unique_user_product (user_id, product_id) 
);

-- Optional: You can add some sample data for testing after creating the tables.
-- Remove the block comments (/* ... */) if you want to insert this sample data.

-- Sample users (passwords are 'pass123', 'testuserpass' before hashing)
-- The application will hash these upon registration. For manual insertion, you'd pre-hash them.
-- Example hashes (SHA-256 for 'pass123'): 0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90
-- Example hashes (SHA-256 for 'testuserpass'): 2c5800077f099098900382092816692699813c30e007728f5932f7d8fe7a7005
-- INSERT INTO users (username, password_hash) VALUES 
-- ('testuser', '0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90'),
-- ('anotheruser', '2c5800077f099098900382092816692699813c30e007728f5932f7d8fe7a7005');

-- Sample products
INSERT INTO products (name, description, price, stock_quantity, image_path) VALUES
('Laptop UltraPro', '15-inch, 16GB RAM, 512GB SSD, Latest Gen CPU', 1299.99, 25, 'images/laptop_ultrapro.png'),
('Wireless Ergonomic Mouse', 'Comfortable design, long battery life, multi-device support', 39.50, 75, 'images/mouse_ergo.png'),
('Mechanical Gaming Keyboard', 'RGB backlit, Cherry MX switches, full-size layout', 119.00, 40, 'images/keyboard_gaming.png'),
('4K UHD Monitor', '27-inch IPS panel, HDR support, slim bezels', 349.95, 15, 'images/monitor_4k.png'),
('Noise Cancelling Headphones', 'Over-ear, Bluetooth 5.0, 30-hour playtime', 199.00, 50, 'images/headphones_noise_cancelling.png'),
('Smart Coffee Maker', 'Wi-Fi enabled, programmable, 12-cup capacity', 89.75, 0, 'images/coffee_maker_smart.png'); -- Out of stock example

-- Display a message indicating script completion (optional, for command line)
SELECT 'SmartKart schema created successfully with users, products, and cart tables.' AS status;
