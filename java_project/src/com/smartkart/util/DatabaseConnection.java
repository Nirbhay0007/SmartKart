package com.smartkart.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Manages MySQL database connection.
 */
public class DatabaseConnection {

    private static String dbHost = "localhost";
    private static String dbPort = "3306";
    private static String dbName = "smartkart_db";
    private static String dbUser = "root";
    private static String dbPassword = "password";
    private static boolean credentialsConfigured = true;
    private static boolean showedErrorOnce = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseConnection() {
    }

    /**
     * Configures the database connection settings.
     * @return True if configuration was successful, false otherwise.
     */
    public static boolean configureConnection() {
        DatabaseCredentialsDialog dialog = new DatabaseCredentialsDialog(new JFrame());
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            dbHost = dialog.getHost();
            dbPort = dialog.getPort();
            dbUser = dialog.getUsername();
            dbPassword = dialog.getPassword();
            credentialsConfigured = true;
            
            // Test the connection
            Connection testConn = null;
            try {
                testConn = getConnectionWithoutCreatingDB();
                if (testConn != null) {
                    // Try to create the database if it doesn't exist
                    Statement stmt = testConn.createStatement();
                    stmt.execute("CREATE DATABASE IF NOT EXISTS " + dbName);
                    JOptionPane.showMessageDialog(null, "Database connection successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to connect to database!\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (testConn != null) {
                    try {
                        testConn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets a connection to the MySQL server without specifying a database.
     * @return A connection to the MySQL server.
     * @throws SQLException If a database access error occurs.
     */
    private static Connection getConnectionWithoutCreatingDB() throws SQLException {
        String url = "jdbc:mysql://" + dbHost + ":" + dbPort;
        return DriverManager.getConnection(url, dbUser, dbPassword);
    }

    /**
     * Establishes and returns a database connection.
     *
     * @return A java.sql.Connection object or null if connection fails.
     */
    public static Connection getConnection() {
        // If credentials are not configured, try common passwords first
        if (!credentialsConfigured) {
            // Try common MySQL passwords
            String[] commonPasswords = {"", "root", "password", "admin", "mysql"};
            
            for (String password : commonPasswords) {
                try {
                    dbPassword = password;
                    String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
                    Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                    if (conn != null) {
                        credentialsConfigured = true;
                        JOptionPane.showMessageDialog(null, "Connected successfully with password: '" + password + "'\nPlease remember this for future use.", "Connection Success", JOptionPane.INFORMATION_MESSAGE);
                        return conn;
                    }
                } catch (SQLException e) {
                    // Just try the next password
                    System.out.println("Failed with password: " + password);
                }
            }
            
            // If all common passwords fail, ask the user
            boolean configured = configureConnection();
            if (!configured) {
                return null;
            }
        }
        
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            return DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found!\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            if (!showedErrorOnce) {
                showedErrorOnce = true;
                String errorMsg = "Failed to connect to database!\n" + e.getMessage();
                String[] options = {"Try Different Credentials", "Try Without Password", "Cancel"};
                int choice = JOptionPane.showOptionDialog(null, errorMsg, "Database Error", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                
                if (choice == 0) {
                    // Try different credentials
                    credentialsConfigured = false;
                    showedErrorOnce = false;
                    return getConnection();
                } else if (choice == 1) {
                    // Try without password
                    dbPassword = "";
                    showedErrorOnce = false;
                    return getConnection();
                } else {
                    // User canceled
                    return null;
                }
            }
            
            // If the database doesn't exist, try to create it
            if (e.getMessage().contains("Unknown database")) {
                try {
                    Connection conn = getConnectionWithoutCreatingDB();
                    Statement stmt = conn.createStatement();
                    stmt.execute("CREATE DATABASE IF NOT EXISTS " + dbName);
                    conn.close();
                    // Try again with the new database
                    return getConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to create database!\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // If it's an authentication issue, ask for credentials again
                credentialsConfigured = false;
                return getConnection();
            }
        }
        return null;
    }
}
