package com.smartkart.model;

/**
 * Represents a product in the SmartKart system.
 */
public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imagePath; // Can be null

    /**
     * Default constructor.
     */
    public Product() {
    }

    /**
     * Parameterized constructor.
     *
     * @param productId     The ID of the product.
     * @param name          The name of the product.
     * @param description   The description of the product.
     * @param price         The price of the product.
     * @param stockQuantity The stock quantity of the product.
     * @param imagePath     The path to the product image (can be null).
     */
    public Product(int productId, String name, String description, double price, int stockQuantity, String imagePath) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns a string representation of the Product object.
     * @return A string representation of the Product.
     */
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
