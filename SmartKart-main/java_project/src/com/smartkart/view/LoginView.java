package com.smartkart.view;

import com.smartkart.controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * View class for the login screen.
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private AuthController authController;

    /**
     * Constructor for LoginView.
     * @param authController The authentication controller.
     */
    public LoginView(AuthController authController) {
        this.authController = authController;
        setTitle("SmartKart Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        initComponents();
        layoutComponents();
        addListeners();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
    }

    /**
     * Lays out the UI components using GridBagLayout.
     */
    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    /**
     * Adds action listeners to the buttons.
     */
    private void addListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authController.handleLogin(getUsername(), getPassword());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authController.showRegistrationView();
            }
        });
    }

    /**
     * Gets the username entered by the user.
     * @return The username string.
     */
    public String getUsername() {
        return usernameField.getText().trim();
    }

    /**
     * Gets the password entered by the user.
     * @return The password string.
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    /**
     * Clears the input fields.
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    /**
     * Shows a message dialog.
     * @param message The message to display.
     * @param title The title of the dialog.
     * @param messageType The type of message (e.g., JOptionPane.ERROR_MESSAGE).
     */
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
