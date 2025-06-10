package com.smartkart.view;

import com.smartkart.controller.CartController;
import com.smartkart.model.CartItem;
import com.smartkart.model.User;
import com.smartkart.model.Product; // For potential stock display or checks, though not primary here

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * View class for displaying the shopping cart contents.
 * Can be a JDialog or a JPanel managed by MainFrame.
 * This implementation uses JDialog for modality.
 */
public class CartView extends JDialog {
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalPricemessageLabel;
    private JButton removeItemButton;
    private JButton updateQuantityButton;
    private JSpinner newQuantitySpinner; // For updating quantity
    private JButton checkoutButton;
    private JButton closeButton;

    private CartController cartController;
    private User loggedInUser;
    private List<CartItem> currentCartItems; // To access item details for actions
    private MainFrame ownerFrame; // To refresh product list on checkout

    /**
     * Constructor for CartView.
     *
     * @param owner          The parent frame (MainFrame).
     * @param cartController The controller for cart actions.
     * @param loggedInUser   The currently logged-in user.
     */
    public CartView(MainFrame owner, CartController cartController, User loggedInUser) {
        super(owner, "Shopping Cart", true); // Modal dialog
        this.ownerFrame = owner;
        this.cartController = cartController;
        this.loggedInUser = loggedInUser;
        
        setSize(700, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        addListeners();
    }

    /**
     * Initializes the UI components.
     */
    private void initComponents() {
        String[] columnNames = {"Item ID", "Product ID", "Product Name", "Quantity", "Price/Unit", "Item Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table
            }
        };
        cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         // Adjust column widths as needed
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(60); // Item ID (CartItem ID from DB)
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(70); // Product ID
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Name
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(70); // Quantity
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Price/Unit
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Item Total


        totalPricemessageLabel = new JLabel("Total Price: $0.00");
        totalPricemessageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        removeItemButton = new JButton("Remove Selected");
        updateQuantityButton = new JButton("Update Quantity");
        newQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Default 1, min 1, max 100
        checkoutButton = new JButton("Checkout");
        closeButton = new JButton("Close");
    }

    /**
     * Lays out the UI components.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10,5));
        JPanel buttonActionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonActionsPanel.add(removeItemButton);
        buttonActionsPanel.add(new JLabel("New Qty:"));
        buttonActionsPanel.add(newQuantitySpinner);
        buttonActionsPanel.add(updateQuantityButton);
        
        bottomPanel.add(buttonActionsPanel, BorderLayout.WEST);

        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkoutPanel.add(totalPricemessageLabel);
        checkoutPanel.add(checkoutButton);
        checkoutPanel.add(closeButton);
        bottomPanel.add(checkoutPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds action listeners to the buttons.
     */
    private void addListeners() {
        closeButton.addActionListener(e -> dispose());

        removeItemButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (Integer) tableModel.getValueAt(selectedRow, 1); // Product ID is in column 1
                cartController.removeFromCart(productId);
                // cartController.loadCartForView(this); // Refresh view is handled by controller potentially, or call here
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        updateQuantityButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (Integer) tableModel.getValueAt(selectedRow, 1);
                int newQuantity = (Integer) newQuantitySpinner.getValue();
                cartController.updateCartQuantity(productId, newQuantity);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        checkoutButton.addActionListener(e -> {
            if (currentCartItems == null || currentCartItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty. Add items before checkout.", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to proceed to checkout?", 
                "Confirm Checkout", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                cartController.checkout();
                // Refresh the product list in MainFrame as stock has changed
                if (ownerFrame != null) {
                    ownerFrame.refreshProductList();
                }
                dispose(); // Close cart view after checkout
            }
        });
    }

    /**
     * Populates the cart table and updates the total price label.
     *
     * @param items      The list of CartItem objects.
     * @param totalPrice The total price of all items in the cart.
     */
    public void displayCartItems(List<CartItem> items, double totalPrice) {
        this.currentCartItems = items;
        tableModel.setRowCount(0); // Clear existing rows
        if (items != null) {
            for (CartItem item : items) {
                Object[] rowData = {
                        item.getCartItemId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        String.format("%.2f", item.getPricePerUnit()),
                        String.format("%.2f", item.getTotalPrice())
                };
                tableModel.addRow(rowData);
            }
        }
        totalPricemessageLabel.setText(String.format("Total Price: $%.2f", totalPrice));
        // Reset spinner to 1 after updating cart potentially
        newQuantitySpinner.setValue(1);
    }

    /**
     * Gets the currently selected product ID from the cart table.
     * @return Product ID or -1 if no selection.
     */
    public int getSelectedProductId() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Integer) tableModel.getValueAt(selectedRow, 1); // Product ID is in column 1
        }
        return -1;
    }
}
