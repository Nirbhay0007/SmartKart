package com.smartkart;

import com.smartkart.controller.AuthController;
import com.smartkart.controller.CartController;
import com.smartkart.controller.ProductController;
import javax.swing.SwingUtilities;

/**
 * Main application class to start the SmartKart Shopping System.
 */
public class MainApp {

    /**
     * Main method to launch the application.
     * Uses SwingUtilities.invokeLater to ensure GUI creation is on the Event Dispatch Thread.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize controllers
            AuthController authController = new AuthController();
            ProductController productController = new ProductController();
            // CartController is typically initialized after login with the logged-in user,
            // or AuthController can create it when needed and pass the user.
            // For this setup, AuthController will instantiate CartController upon successful login.
            // AuthController also needs references to ProductController and CartController to pass to MainFrame.
            
            // It's important to manage dependencies. One way is to have AuthController aware of other controllers
            // it needs to launch the MainFrame, or have a central place for controller instantiation/lookup.
            // Here, we'll set them on AuthController after it's created.
            authController.setProductController(productController);
            // CartController will be set within AuthController when user logs in for now,
            // as it requires a User object for its constructor in this design.
            // Alternatively, CartController could be instantiated here and user set later.

            // Show the initial login view
            authController.showLoginView();
        });
    }
}
