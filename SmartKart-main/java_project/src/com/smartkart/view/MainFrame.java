package com.smartkart.view;

import com.smartkart.controller.AuthController; // To handle logout
import com.smartkart.controller.CartController;
import com.smartkart.controller.ProductController;
import com.smartkart.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main application window after successful login.
 * Holds ProductListView and provides access to CartView.
 */
public class MainFrame extends JFrame {
    private User loggedInUser;
    private ProductListView productListView;
    private JButton viewCartButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;

    private ProductController productController;
    private CartController cartController;
    private AuthController authController; // For logout

    /**
     * Constructor for MainFrame.
     *
     * @param loggedInUser      The user who has logged in.
     * @param productController The controller for product-related actions.
     * @param cartController    The controller for cart-related actions.
     * @param authController    The controller for auth-related actions (logout).
     */
    public MainFrame(User loggedInUser, ProductController productController, CartController cartController, AuthController authController) {
        this.loggedInUser = loggedInUser;
        this.productController = productController;
        this.cartController = cartController;
        this.authController = authController;

        setTitle("SmartKart - Welcome");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        addListeners();

        // Load products into the product list view
        this.productController.loadProductsForView(productListView);
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Pass controllers and user to ProductListView
        productListView = new ProductListView(productController, cartController, loggedInUser);

        viewCartButton = new JButton("View Cart");
        logoutButton = new JButton("Logout");
    }

    /**
     * Lays out the UI components using BorderLayout.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10)); // Add some padding

        // Top panel for welcome message and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(viewCartButton);
        buttonPanel.add(logoutButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(productListView), BorderLayout.CENTER); // Product list in the center
    }

    /**
     * Adds action listeners to the buttons.
     */
    private void addListeners() {
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show CartView as a modal dialog
                CartView cartView = new CartView(MainFrame.this, cartController, loggedInUser);
                cartController.loadCartForView(cartView); // Load current cart items
                cartView.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the MainFrame
                authController.showLoginView(); // Show the login view again
            }
        });
    }
    
    /**
     * Refreshes the product list view. Typically called after checkout.
     */
    public void refreshProductList() {
        if (productListView != null && productController != null) {
            productController.loadProductsForView(productListView);
        }
    }
}
