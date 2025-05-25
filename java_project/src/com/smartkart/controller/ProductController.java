package com.smartkart.controller;

import com.smartkart.dao.ProductDao;
import com.smartkart.model.Product;
import com.smartkart.view.ProductListView;

import java.util.List;

/**
 * Controller for product-related actions.
 */
public class ProductController {
    private ProductDao productDao;
    // private ProductListView productListView; // View can be passed as a parameter to methods instead of being a field

    /**
     * Constructor for ProductController.
     */
    public ProductController() {
        this.productDao = new ProductDao();
    }

    /**
     * Loads all products and tells the given view to display them.
     *
     * @param view The ProductListView to display products on.
     */
    public void loadProductsForView(ProductListView view) {
        if (view == null) {
            System.err.println("ProductListView is null in ProductController.loadProductsForView");
            return;
        }
        List<Product> products = productDao.getAllProducts();
        view.displayProducts(products);
    }

    /**
     * Retrieves a product by its ID.
     * This might be used by other controllers (e.g. CartController for stock checks) or internal logic.
     * @param productId The ID of the product.
     * @return The Product object or null if not found.
     */
    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }
    
    /**
     * Updates the stock of a product. 
     * Typically called during checkout by CartController.
     * @param productId The ID of the product.
     * @param newStock The new stock quantity.
     * @return true if update was successful, false otherwise.
     */
    public boolean updateProductStock(int productId, int newStock) {
        return productDao.updateProductStock(productId, newStock);
    }
}
