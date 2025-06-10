package com.smartkart.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for handling password hashing and verification.
 */
public class PasswordUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private PasswordUtils() {
    }

    /**
     * Hashes a plain text password using SHA-256 algorithm.
     *
     * @param password The plain text password to hash.
     * @return The hexadecimal string representation of the hashed password, or null if hashing fails.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Log or handle more gracefully in a real app
            return null;
        }
    }

    /**
     * Verifies a plain text password against a stored hashed password.
     *
     * @param plainPassword  The plain text password to verify.
     * @param hashedPassword The stored hashed password.
     * @return True if the plain password matches the hashed password, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        String newHash = hashPassword(plainPassword);
        return hashedPassword.equals(newHash);
    }
}
