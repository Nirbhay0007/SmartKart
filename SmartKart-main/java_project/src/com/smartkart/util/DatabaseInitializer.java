package com.smartkart.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * Utility class to initialize the database from SQL script.
 */
public class DatabaseInitializer {

    /**
     * Main method to run the database initialization.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        initializeDatabase();
    }

    /**
     * Initializes the database from the SQL script.
     * @return True if initialization was successful, false otherwise.
     */
    public static boolean initializeDatabase() {
        String sqlFilePath = "smartkart_schema.sql";
        StringBuilder sqlScript = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip commented lines in SQL
                if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                    sqlScript.append(line);
                    if (line.trim().endsWith(";")) {
                        sqlScript.append("\n");
                    }
                }
            }
            
            // Split script into individual statements
            String[] statements = sqlScript.toString().split(";");
            
            // Execute each statement
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password")) {
                Statement stmt = conn.createStatement();
                
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        // Uncomment the sample data insertion
                        String modifiedStatement = statement.replace("/*", "").replace("*/", "");
                        stmt.execute(modifiedStatement);
                    }
                }
                
                JOptionPane.showMessageDialog(null, "Database initialized successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error initializing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }
}
