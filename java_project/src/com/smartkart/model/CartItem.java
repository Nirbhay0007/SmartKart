package com.smartkart.model;

/**
 * Represents an item in the shopping cart.
 */
public class CartItem {
    private int cartItemId; // Optional, useful if mapping directly to a DB primary key
    private int productId;
    private String productName; // Denormalized for display purposes
    private int quantity;
    private double pricePerUnit; // Price at the time of adding to cart

    /**
     * Parameterized constructor.
     *
     * @param productId    The ID of the product.
     * @param productName  The name of the product.
     * @param quantity     The quantity of the product in the cart.
     * @param pricePerUnit The price per unit of the product.
     */
    public CartItem(int productId, String productName, int quantity, double pricePerUnit) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
    
    /**
     * Parameterized constructor including cartItemId.
     *
     * @param cartItemId   The ID of the cart item (from database).
     * @param productId    The ID of the product.
     * @param productName  The name of the product.
     * @param quantity     The quantity of the product in the cart.
     * @param pricePerUnit The price per unit of the product.
     */
    public CartItem(int cartItemId, int productId, String productName, int quantity, double pricePerUnit) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    // Getters and Setters

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * Calculates the total price for this cart item.
     *
     * @return The total price (quantity * pricePerUnit).
     */
    public double getTotalPrice() {
        return quantity * pricePerUnit;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
