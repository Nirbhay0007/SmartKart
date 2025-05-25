package com.smartkart.view;

import com.smartkart.controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * View class for the user registration screen.
 */
public class RegistrationView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private AuthController authController;

    /**
     * Constructor for RegistrationView.
     * @param authController The authentication controller.
     */
    public RegistrationView(AuthController authController) {
        this.authController = authController;
        setTitle("SmartKart Registration");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close, don't exit app
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
        confirmPasswordField = new JPasswordField(20);
        registerButton = new JButton("Register");
    }

    /**
     * Lays out the UI components using GridBagLayout.
     */
    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
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

        // Confirm Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Confirm Password:"), gbc);

        // Confirm Password Field
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(confirmPasswordField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);
    }

    /**
     * Adds action listeners to the buttons.
     */
    private void addListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authController.handleRegistration(getUsername(), getPassword(), getConfirmPassword());
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
     * Gets the confirmed password entered by the user.
     * @return The confirmed password string.
     */
    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    /**
     * Clears the input fields.
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
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
