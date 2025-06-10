package com.smartkart.controller;

import com.smartkart.dao.UserDao;
import com.smartkart.model.User;
import com.smartkart.util.PasswordUtils;
import com.smartkart.view.LoginView;
import com.smartkart.view.RegistrationView;
import com.smartkart.view.MainFrame;

import javax.swing.JOptionPane;

/**
 * Controller for authentication-related actions (login, registration).
 */
public class AuthController {
    private LoginView loginView;
    private RegistrationView registrationView;
    private UserDao userDao;
    // To create MainFrame, we need other controllers as well
    private ProductController productController;
    private CartController cartController;

    /**
     * Constructor for AuthController.
     */
    public AuthController() {
        this.userDao = new UserDao();
        // Initialize other controllers that MainFrame will need
        // This creates a dependency cycle if not handled carefully.
        // For simplicity here, we might instantiate them here or pass them if they are singletons.
    }
    
    // Setter for ProductController - to break potential circular dependency during instantiation
    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    // Setter for CartController
    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    /**
     * Creates and shows the LoginView.
     */
    public void showLoginView() {
        if (loginView == null || !loginView.isDisplayable()) {
            loginView = new LoginView(this);
        }
        loginView.setVisible(true);
    }

    /**
     * Handles the login process.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    public void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Username and password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDao.getUserByUsername(username);

        if (user != null && PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            loginView.showMessage("Login successful!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            loginView.dispose();
            
            // Ensure controllers for MainFrame are set up
            if (this.productController == null) {
                this.productController = new ProductController(); // Or get instance if singleton
            }
            if (this.cartController == null) {
                this.cartController = new CartController(user); // Pass user to CartController
                 // If CartController needs ProductDao, it instantiates it itself or gets it passed.
            } else {
                 this.cartController.setCurrentUser(user); // Update user if cartController already exists
            }
            
            // Link controllers if they need each other, e.g. CartController might need ProductController
            // This example assumes CartController can get/create ProductDao itself, or ProductController is passed to it.
            // If ProductController needs MainFrame/CartView etc, they are passed via methods.

            MainFrame mainFrame = new MainFrame(user, productController, cartController, this);
            mainFrame.setVisible(true);
        } else {
            loginView.showMessage("Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            loginView.clearFields();
        }
    }

    /**
     * Creates and shows the RegistrationView.
     */
    public void showRegistrationView() {
        if (registrationView == null || !registrationView.isDisplayable()) {
            registrationView = new RegistrationView(this);
        }
        // If loginView is not null, make registrationView appear relative to it or center screen
        registrationView.setLocationRelativeTo(loginView != null && loginView.isVisible() ? loginView : null);
        registrationView.setVisible(true);
    }

    /**
     * Handles the user registration process.
     *
     * @param username        The username for registration.
     * @param password        The password for registration.
     * @param confirmPassword The confirmed password.
     */
    public void handleRegistration(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registrationView.showMessage("All fields are required.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            registrationView.showMessage("Passwords do not match.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length() < 6) { // Example: Basic password policy
            registrationView.showMessage("Password must be at least 6 characters long.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if username already exists
        if (userDao.getUserByUsername(username) != null) {
            registrationView.showMessage("Username already exists. Please choose another.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(password); // DAO will hash it before saving

        if (userDao.addUser(newUser)) {
            registrationView.showMessage("Registration successful! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            registrationView.dispose(); // Close registration window
            // Optionally show login view again if it was closed or to prompt login
            // showLoginView(); 
        } else {
            registrationView.showMessage("Registration failed. Please try again.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
