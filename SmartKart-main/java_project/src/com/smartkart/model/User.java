package com.smartkart.model;

/**
 * Represents a user in the SmartKart system.
 */
public class User {
    private int userId;
    private String username;
    private String passwordHash;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Parameterized constructor.
     *
     * @param userId       The ID of the user.
     * @param username     The username of the user.
     * @param passwordHash The hashed password of the user.
     */
    public User(int userId, String username, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters

    /**
     * Gets the user ID.
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * @param userId The user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the hashed password.
     * @return The hashed password.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the hashed password.
     * @param passwordHash The hashed password to set.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns a string representation of the User object.
     * @return A string representation of the User.
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                // Avoid printing password hash in logs or toString for security
                '}';
    }
}
