package com.smartkart.controller;

import com.smartkart.dao.CartDao;
import com.smartkart.dao.ProductDao;
import com.smartkart.model.CartItem;
import com.smartkart.model.Product;
import com.smartkart.model.User;
import com.smartkart.view.CartView;
import com.smartkart.view.MainFrame; // To refresh product list from MainFrame

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Controller for cart-related actions.
 */
public class CartController {
    private CartDao cartDao;
    private ProductDao productDao; // For stock checks/updates
    private User currentUser;
    private CartView cartView; // Optional: if controller manages view lifecycle or needs frequent direct access
    private MainFrame mainFrame; // To notify mainframe about checkout for product list refresh

    /**
     * Constructor for CartController.
     * @param currentUser The currently logged-in user.
     */
    public CartController(User currentUser) {
        this.cartDao = new CartDao();
        this.productDao = new ProductDao();
        this.currentUser = currentUser;
    }
    
    /**
     * Sets the current user. Useful if the controller is created before user logs in,
     * or if user changes.
     * @param user The user to set as current.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
      * Sets the MainFrame instance to allow CartController to call MainFrame's refreshProductList.
      * @param mainFrame The MainFrame instance.
      */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Adds an item to the cart.
     *
     * @param productId The ID of the product to add.
     * @param quantity  The quantity to add.
     */
    public void addToCart(int productId, int quantity) {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "No user logged in.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Product product = productDao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "Product not found.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(null, "Quantity must be positive.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Stock check is also done in CartDao.addItemToCart, but good to have a preliminary one here for immediate feedback.
        if (quantity > product.getStockQuantity()) {
            JOptionPane.showMessageDialog(null, 
                "Requested quantity (" + quantity + ") for '" + product.getName() + "' exceeds available stock (" + product.getStockQuantity() + ").", 
                "Stock Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cartDao.addItemToCart(currentUser.getUserId(), productId, quantity)) {
            JOptionPane.showMessageDialog(null, "'"+product.getName() + "' added to cart.", "Cart Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // JOptionPane error is usually shown by DAO itself.
            // JOptionPane.showMessageDialog(null, "Failed to add item to cart. Check stock or database connection.", "Cart Error", JOptionPane.ERROR_MESSAGE);
        }
        // Refresh cart view if it's open and managed by this controller
        if (this.cartView != null && this.cartView.isVisible()) {
            loadCartForView(this.cartView);
        }
        // After adding to cart, product list stock might change, so refresh MainFrame's product list
        if (this.mainFrame != null) {
            this.mainFrame.refreshProductList();
        }
    }

    /**
     * Loads cart items for the current user and tells the given CartView to display them.
     *
     * @param view The CartView to display items on.
     */
    public void loadCartForView(CartView view) {
        if (currentUser == null) {
            // This case should ideally not happen if CartView is only accessible post-login
            view.displayCartItems(null, 0.0);
            return;
        }
        List<CartItem> items = cartDao.getCartItemsByUserId(currentUser.getUserId());
        double totalPrice = 0.0;
        if (items != null) {
            for (CartItem item : items) {
                totalPrice += item.getTotalPrice();
            }
        }
        view.displayCartItems(items, totalPrice);
        this.cartView = view; // Keep a reference if needed for future refreshes from controller actions
    }

    /**
     * Removes an item from the cart.
     *
     * @param productId The ID of the product to remove.
     */
    public void removeFromCart(int productId) {
        if (currentUser == null) return;

        if (cartDao.removeItemFromCart(currentUser.getUserId(), productId)) {
            JOptionPane.showMessageDialog(null, "Item removed from cart.", "Cart Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to remove item from cart.", "Cart Error", JOptionPane.ERROR_MESSAGE);
        }
        // Refresh cart view
        if (this.cartView != null && this.cartView.isVisible()) {
            loadCartForView(this.cartView);
        }
    }

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param productId   The ID of the product.
     * @param newQuantity The new quantity.
     */
    public void updateCartQuantity(int productId, int newQuantity) {
        if (currentUser == null) return;

        Product product = productDao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "Product not found for update.", "Cart Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newQuantity <= 0) {
            JOptionPane.showMessageDialog(null, "Quantity must be positive. To remove, use the remove button.", "Cart Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (newQuantity > product.getStockQuantity()) {
            JOptionPane.showMessageDialog(null, 
                "Requested quantity (" + newQuantity + ") for '" + product.getName() + "' exceeds available stock (" + product.getStockQuantity() + ").", 
                "Stock Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cartDao.updateCartItemQuantity(currentUser.getUserId(), productId, newQuantity)) {
            JOptionPane.showMessageDialog(null, "Cart quantity updated.", "Cart Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Error message typically from DAO
        }
        // Refresh cart view
        if (this.cartView != null && this.cartView.isVisible()) {
            loadCartForView(this.cartView);
        }
    }

    /**
     * Handles the checkout process.
     */
    public void checkout() {
        if (currentUser == null) return;

        List<CartItem> cartItems = cartDao.getCartItemsByUserId(currentUser.getUserId());
        if (cartItems == null || cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your cart is empty.", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean allStockUpdatesSuccessful = true;
        // Check stock for all items before proceeding with any DB updates (optional but good practice)
        for (CartItem item : cartItems) {
            Product product = productDao.getProductById(item.getProductId());
            if (product == null || item.getQuantity() > product.getStockQuantity()) {
                JOptionPane.showMessageDialog(null, 
                    "Stock changed for product: '" + (product != null ? product.getName() : "Unknown") + 
                    "'. Requested: " + item.getQuantity() + ", Available: " + (product != null ? product.getStockQuantity() : "N/A") + 
                    ". Please review your cart.", 
                    "Checkout Stock Error", JOptionPane.ERROR_MESSAGE);
                // Refresh cart view to show updated stock/availability if possible
                 if (this.cartView != null && this.cartView.isVisible()) {
                    loadCartForView(this.cartView);
                }
                return; // Stop checkout
            }
        }
        
        // Proceed with updating stock and clearing cart
        for (CartItem item : cartItems) {
            Product product = productDao.getProductById(item.getProductId()); // Re-fetch for safety, though might be redundant if previous check is trusted
            if (product != null) {
                int newStock = product.getStockQuantity() - item.getQuantity();
                if (!productDao.updateProductStock(item.getProductId(), newStock)) {
                    allStockUpdatesSuccessful = false;
                    // Log this error, potentially try to rollback previous stock updates if in a transaction
                    JOptionPane.showMessageDialog(null, "Error updating stock for product: " + product.getName(), "Checkout Error", JOptionPane.ERROR_MESSAGE);
                    // break; // Stop further processing if one fails, or continue and report all failures
                }
            }
        }

        if (allStockUpdatesSuccessful) {
            if (cartDao.clearCart(currentUser.getUserId())) {
                JOptionPane.showMessageDialog(null, "Checkout successful! Thank you for your order.", "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Checkout completed but failed to clear cart. Please contact support.", "Checkout Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Checkout failed due to stock update issues. Some items may not have been processed correctly.", "Checkout Failed", JOptionPane.ERROR_MESSAGE);
        }

        // Refresh cart view (it should be empty)
        if (this.cartView != null && this.cartView.isVisible()) {
            loadCartForView(this.cartView);
        }
        // Refresh product list in MainFrame
        if (this.mainFrame != null) {
            this.mainFrame.refreshProductList();
        }
    }
}
