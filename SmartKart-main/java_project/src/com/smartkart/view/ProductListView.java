package com.smartkart.view;

import com.smartkart.controller.CartController;
import com.smartkart.controller.ProductController;
import com.smartkart.model.Product;
import com.smartkart.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * JPanel to display products in a table.
 */
public class ProductListView extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton addToCartButton;

    private ProductController productController;
    private CartController cartController;
    private User loggedInUser;
    private List<Product> currentProductList; // To store the currently displayed products for easy access

    /**
     * Constructor for ProductListView.
     *
     * @param productController The controller for product actions.
     * @param cartController    The controller for cart actions.
     * @param loggedInUser      The currently logged-in user.
     */
    public ProductListView(ProductController productController, CartController cartController, User loggedInUser) {
        this.productController = productController;
        this.cartController = cartController;
        this.loggedInUser = loggedInUser;

        setLayout(new BorderLayout(5, 5));
        initComponents();
        layoutComponents();
        addListeners();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        String[] columnNames = {"Product ID", "Name", "Description", "Price", "Stock", "Quantity to Add"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make "Quantity to Add" column (index 5) editable
                return column == 5;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getColumnModel().getColumn(0).setPreferredWidth(70); // Product ID
        productTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Name
        productTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Description
        productTable.getColumnModel().getColumn(3).setPreferredWidth(70);  // Price
        productTable.getColumnModel().getColumn(4).setPreferredWidth(50);  // Stock
        productTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Quantity to Add

        addToCartButton = new JButton("Add Selected to Cart");
    }

    /**
     * Lays out the UI components.
     */
    private void layoutComponents() {
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(addToCartButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds action listeners to the buttons.
     */
    private void addListeners() {
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        // Get product ID from the first column
                    int productId = (Integer) tableModel.getValueAt(selectedRow, 0);
                        // Get quantity from the "Quantity to Add" column (index 5)
                        Object quantityObj = tableModel.getValueAt(selectedRow, 5);
                        int quantity;
                        if (quantityObj instanceof Integer) {
                            quantity = (Integer) quantityObj;
                        } else if (quantityObj instanceof String) {
                            quantity = Integer.parseInt((String) quantityObj);
                        } else {
                            // Default to 1 if the type is unexpected or null, or show error
                            JOptionPane.showMessageDialog(ProductListView.this,
                                    "Invalid quantity format for the selected product. Please enter a number.",
                                    "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (quantity <= 0) {
                            JOptionPane.showMessageDialog(ProductListView.this,
                                    "Please enter a quantity greater than 0.",
                                    "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                    cartController.addToCart(productId, quantity);
                        // Optionally, refresh product list or clear quantity field
                        // tableModel.setValueAt(1, selectedRow, 5); // Reset quantity after adding
                        JOptionPane.showMessageDialog(ProductListView.this,
                                "Product added to cart successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ProductListView.this,
                                "Invalid quantity. Please enter a valid number.",
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ProductListView.this,
                                "Error adding product to cart: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(ProductListView.this,
                            "Please select a product to add to cart.",
                            "No Product Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    /**
     * Populates the JTable with a list of products.
     *
     * @param products The list of products to display.
     */
    public void displayProducts(List<Product> products) {
        this.currentProductList = products; // Store the list
        // Clear existing rows
        tableModel.setRowCount(0);
        if (products != null) {
            for (Product product : products) {
                Object[] rowData = {
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        String.format("%.2f", product.getPrice()), // Format price
                        product.getStockQuantity(),
                        1 // Default quantity to add
                };
                tableModel.addRow(rowData);
            }
        }
    }
}
