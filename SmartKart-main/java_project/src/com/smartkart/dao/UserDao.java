package com.smartkart.dao;

import com.smartkart.model.User;
import com.smartkart.util.DatabaseConnection;
import com.smartkart.util.PasswordUtils;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User related database operations.
 */
public class UserDao {

    /**
     * Adds a new user to the database.
     *
     * @param user The User object to add.
     * @return True if the user was added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            // Hash the password before storing
            String hashedPassword = PasswordUtils.hashPassword(user.getPasswordHash()); // Assuming getPasswordHash() initially holds plain password from registration
            if (hashedPassword == null) {
                JOptionPane.showMessageDialog(null, "Error hashing password.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            pstmt.setString(2, hashedPassword);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding user to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a user from the database by username.
     *
     * @param username The username to search for.
     * @return A User object if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }
}
