package com.smartkart.view;

import com.smartkart.controller.AuthController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Modern, sleek login view with gradient background and rounded components.
 */
public class ModernLoginView extends JFrame {
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(64, 81, 181);
    private static final Color SECONDARY_COLOR = new Color(92, 107, 192);
    private static final Color ACCENT_COLOR = new Color(255, 87, 34);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color LIGHT_GRAY = new Color(245, 245, 245);
    private static final Color PLACEHOLDER_COLOR = new Color(158, 158, 158);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton;
    private AuthController authController;
    private JPanel mainPanel;

    public ModernLoginView(AuthController authController) {
        this.authController = authController;
        initializeFrame();
        createComponents();
        layoutComponents();
        addListeners();
        addAnimations();
    }

    private void initializeFrame() {
        setTitle("SmartKart - Welcome Back");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Remove default border
        setUndecorated(false);
        
        // Set custom icon
        try {
            setIconImage(createAppIcon());
        } catch (Exception e) {
            // Icon creation failed, continue without icon
        }
    }

    private Image createAppIcon() {
        // Create a simple app icon
        BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); // Increased size for better quality
        Graphics2D g2 = icon.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Background with a subtle gradient or a softer primary color
        GradientPaint iconGradient = new GradientPaint(0, 0, PRIMARY_COLOR.brighter(), 0, 64, PRIMARY_COLOR);
        g2.setPaint(iconGradient);
        g2.fillRoundRect(0, 0, 64, 64, 16, 16); // Slightly more rounded

        // Border
        g2.setColor(PRIMARY_COLOR.darker());
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, 62, 62, 15, 15);

        // Text "SK" with Accent Color and better font
        g2.setColor(ACCENT_COLOR); 
        g2.setFont(new Font("Segoe UI", Font.BOLD, 30)); // Using Segoe UI for consistency
        FontMetrics fm = g2.getFontMetrics();
        int textX = (64 - fm.stringWidth("SK")) / 2;
        int textY = (64 - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString("SK", textX, textY);
        g2.dispose();
        return icon;
    }

    private void createComponents() {
        // Create main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, 
                                                         0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Create styled text fields
        usernameField = createStyledTextField("Username or Email");
        passwordField = createStyledPasswordField("Password");

        // Create styled buttons
        loginButton = createPrimaryButton("Sign In");
        registerButton = createSecondaryButton("Create Account");
        forgotPasswordButton = createLinkButton("Forgot Password?");
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                super.paintComponent(g);
                
                // Draw border
                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        
        styleTextField(field, placeholder);
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                super.paintComponent(g);
                
                // Draw border
                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        
        styleTextField(field, placeholder);
        return field;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(300, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setOpaque(false);
        
        // Add placeholder functionality
        field.setText(placeholder);
        field.setForeground(PLACEHOLDER_COLOR);
        
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color color1 = (Color) getClientProperty("gradientColor1");
                Color color2 = (Color) getClientProperty("gradientColor2");
                if (color1 == null) color1 = PRIMARY_COLOR;
                if (color2 == null) color2 = SECONDARY_COLOR;

                GradientPaint gradient = new GradientPaint(0, 0, color1, 
                                                         0, getHeight(), color2);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Draw text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
            }
        };
        
        button.setPreferredSize(new Dimension(300, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color borderColor = (Color) getClientProperty("borderColor");
                Color textColor = (Color) getClientProperty("textColor");
                if (borderColor == null) borderColor = Color.WHITE;
                if (textColor == null) textColor = Color.WHITE;

                // Draw border
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 25, 25);
                
                // Draw text
                g2.setColor(textColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
            }
        };
        
        button.setPreferredSize(new Dimension(300, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add underline on hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setText("<html><u>" + text + "</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setText(text);
            }
        });
        
        return button;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // App logo/title
        JLabel titleLabel = new JLabel("SmartKart");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40)); // Slightly larger title
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel subtitleLabel = new JLabel("Welcome back! Please sign in to your account.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 220)); // Slightly more opaque
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 20, 10, 20); // Increased top padding, added side padding
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 50, 20); // Increased bottom padding
        mainPanel.add(subtitleLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0); // Increased bottom padding
        mainPanel.add(usernameField, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 25, 0); // Increased bottom padding
        mainPanel.add(passwordField, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 15, 0); // Adjusted top/bottom padding
        mainPanel.add(loginButton, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 20, 0); // Adjusted bottom padding
        mainPanel.add(registerButton, gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 50, 0); // Increased bottom padding
        mainPanel.add(forgotPasswordButton, gbc);
    }

    private void addListeners() {
        loginButton.addActionListener(e -> {
            String username = getUsername();
            String password = getPassword();
            
            if (!username.isEmpty() && !password.isEmpty()) {
                authController.handleLogin(username, password);
            } else {
                showMessage("Please fill in all fields", "Input Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> authController.showRegistrationView());

        forgotPasswordButton.addActionListener(e -> {
            showMessage("Password reset functionality coming soon!", "Information", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Add Enter key support
        getRootPane().setDefaultButton(loginButton);
    }

    private void addAnimations() {
        // Add hover effects to buttons
        addPrimaryButtonHoverEffect(loginButton);
        addSecondaryButtonHoverEffect(registerButton);
    }

    private void addPrimaryButtonHoverEffect(JButton button) {
        Color originalColor1 = PRIMARY_COLOR;
        Color originalColor2 = SECONDARY_COLOR;
        Color hoverColor1 = PRIMARY_COLOR.darker();
        Color hoverColor2 = SECONDARY_COLOR.darker();

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Update gradient for hover state
                button.putClientProperty("gradientColor1", hoverColor1);
                button.putClientProperty("gradientColor2", hoverColor2);
                button.repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Revert to original gradient
                button.putClientProperty("gradientColor1", originalColor1);
                button.putClientProperty("gradientColor2", originalColor2);
                button.repaint();
            }
        });
    }

    private void addSecondaryButtonHoverEffect(JButton button) {
        Color originalBorderColor = Color.WHITE;
        Color originalTextColor = Color.WHITE;
        Color hoverBorderColor = ACCENT_COLOR; // Use accent for hover border
        Color hoverTextColor = ACCENT_COLOR;   // Use accent for hover text

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.putClientProperty("borderColor", hoverBorderColor);
                button.putClientProperty("textColor", hoverTextColor);
                button.repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.putClientProperty("borderColor", originalBorderColor);
                button.putClientProperty("textColor", originalTextColor);
                button.repaint();
            }
        });
    }

    public String getUsername() {
        String text = usernameField.getText().trim();
        return text.equals("Username or Email") ? "" : text;
    }

    public String getPassword() {
        String text = new String(passwordField.getPassword());
        return text.equals("Password") ? "" : text;
    }

    public void clearFields() {
        usernameField.setText("Username or Email");
        usernameField.setForeground(PLACEHOLDER_COLOR);
        passwordField.setText("Password");
        passwordField.setForeground(PLACEHOLDER_COLOR);
    }

    public void showMessage(String message, String title, int messageType) {
        // Create custom styled message dialog
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
