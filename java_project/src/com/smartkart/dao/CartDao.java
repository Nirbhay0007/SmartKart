package com.smartkart.dao;

import com.smartkart.model.CartItem;
import com.smartkart.model.Product; // Required for stock check
import com.smartkart.util.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Cart related database operations.
 */
public class CartDao {

    private ProductDao productDao; // To check product stock

    public CartDao() {
        this.productDao = new ProductDao(); // Initialize ProductDao
    }

    /**
     * Adds an item to the user's cart or updates its quantity if it already exists.
     * Ensures quantity requested does not exceed product stock.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product.
     * @param quantity  The quantity to add.
     * @return True if the item was added/updated successfully, false otherwise.
     */
    public boolean addItemToCart(int userId, int productId, int quantity) {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "Product not found.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (quantity <= 0) {
             JOptionPane.showMessageDialog(null, "Quantity must be greater than zero.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (quantity > product.getStockQuantity()) {
            JOptionPane.showMessageDialog(null, "Requested quantity exceeds available stock (" + product.getStockQuantity() + ").", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String checkSql = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false); // Start transaction

            // Check if item exists
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) { // Item exists, update quantity
                int existingQuantity = rs.getInt("quantity");
                int newQuantity = existingQuantity + quantity;
                if (newQuantity > product.getStockQuantity()) {
                    JOptionPane.showMessageDialog(null, "Total quantity in cart would exceed stock for product: " + product.getName(), "Cart Error", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                    return false;
                }
                pstmt.close(); // Close previous pstmt
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, userId);
                pstmt.setInt(3, productId);
            } else { // Item does not exist, insert new
                pstmt.close(); // Close previous pstmt
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantity);
            }
            int affectedRows = pstmt.executeUpdate();
            conn.commit(); // Commit transaction
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException se) { se.printStackTrace(); }
            JOptionPane.showMessageDialog(null, "Error adding item to cart: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Retrieves all cart items for a given user.
     * Joins with the products table to get product name and price.
     *
     * @param userId The ID of the user.
     * @return A list of CartItem objects.
     */
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        // SQL to join cart and products table
        String sql = "SELECT c.cart_id, c.product_id, p.name AS product_name, c.quantity, p.price AS price_per_unit " +
                     "FROM cart c JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return cartItems;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem(
                        rs.getInt("cart_id"), // Assuming cart_id is the PK of cart table
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price_per_unit")
                );
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching cart items: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return cartItems;
    }

    /**
     * Removes a specific product from the user's cart.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to remove.
     * @return True if the item was removed successfully, false otherwise.
     */
    public boolean removeItemFromCart(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error removing item from cart: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
     * Updates the quantity of an item in the cart.
     * Ensures newQuantity is valid ( > 0 and <= product stock).
     *
     * @param userId      The ID of the user.
     * @param productId   The ID of the product.
     * @param newQuantity The new quantity.
     * @return True if the quantity was updated successfully, false otherwise.
     */
    public boolean updateCartItemQuantity(int userId, int productId, int newQuantity) {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "Product not found for update.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newQuantity <= 0) {
            // If new quantity is 0 or less, consider removing the item instead
            // For this method, we strictly update. UI logic might call removeItemFromCart.
            JOptionPane.showMessageDialog(null, "New quantity must be greater than zero.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newQuantity > product.getStockQuantity()) {
            JOptionPane.showMessageDialog(null, "New quantity exceeds available stock (" + product.getStockQuantity() + ").", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating cart item quantity: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
     * Removes all items from a user's cart.
     *
     * @param userId The ID of the user whose cart is to be cleared.
     * @return True if the cart was cleared successfully, false otherwise.
     */
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error clearing cart: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
}
