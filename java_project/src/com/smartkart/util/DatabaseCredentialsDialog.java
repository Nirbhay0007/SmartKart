package com.smartkart.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog to collect database credentials from the user.
 */
public class DatabaseCredentialsDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField hostField;
    private JTextField portField;
    private boolean confirmed = false;

    /**
     * Constructor for the dialog.
     * @param parent The parent frame.
     */
    public DatabaseCredentialsDialog(Frame parent) {
        super(parent, "Database Connection Settings", true);
        initComponents();
        layoutComponents();
        addListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Initializes the components.
     */
    private void initComponents() {
        usernameField = new JTextField("root", 20);
        passwordField = new JPasswordField(20);
        hostField = new JTextField("localhost", 20);
        portField = new JTextField("3306", 5);
    }

    /**
     * Lays out the components.
     */
    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Host
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Host:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(hostField, gbc);

        // Port
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Port:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(portField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add listeners
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Adds listeners to the components.
     */
    private void addListeners() {
        // Additional listeners if needed
    }

    /**
     * Gets the username entered by the user.
     * @return The username.
     */
    public String getUsername() {
        return usernameField.getText().trim();
    }

    /**
     * Gets the password entered by the user.
     * @return The password.
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    /**
     * Gets the host entered by the user.
     * @return The host.
     */
    public String getHost() {
        return hostField.getText().trim();
    }

    /**
     * Gets the port entered by the user.
     * @return The port.
     */
    public String getPort() {
        return portField.getText().trim();
    }

    /**
     * Checks if the user confirmed the dialog.
     * @return True if confirmed, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
