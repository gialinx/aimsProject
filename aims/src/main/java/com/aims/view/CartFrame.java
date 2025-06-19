package com.aims.view;

import com.aims.controller.CartController;
import com.aims.dao.CartDAO;
import com.aims.entity.CartItem;
import com.aims.util.Session;
import com.aims.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CartFrame extends JFrame {
    private List<ProductPanel> productList = new ArrayList<>();
    private JPanel productPanel;
    private CartController cartController;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> rushEligibleCombo;
    private JComboBox<String> sortTypeCombo;
    private JComboBox<String> sortOrderCombo;
    private JButton loginButton, logoutButton, signupButton, profileButton, buyButton;
    private int page = 1;
    private final int PAGE_SIZE = 12; // 4x3 grid
    private JLabel pageLabel;
    private JLabel totalPriceLabel;
    private double totalPrice;
    private JPanel toolBar;
    private JLabel sessionLabel; // Thêm nhãn cho session_id

    public CartFrame() {
        setTitle("AIMS - Shopping Cart");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Thanh điều hướng
        toolBar = createToolBar();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(toolBar, BorderLayout.NORTH);

        // Thanh header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel storeName = new JLabel("AIMS Cart");
        storeName.setFont(new Font("Arial", Font.BOLD, 24));
        leftPanel.add(storeName);

        searchField = new JTextField(20);
        leftPanel.add(new JLabel("Search Title: "));
        leftPanel.add(searchField);

        categoryCombo = new JComboBox<>(getCategories());
        categoryCombo.insertItemAt("All", 0);
        categoryCombo.setSelectedIndex(0);
        leftPanel.add(new JLabel("Category: "));
        leftPanel.add(categoryCombo);

        rushEligibleCombo = new JComboBox<>(new String[]{"All", "Rush Eligible", "Non-Rush"});
        leftPanel.add(new JLabel("Rush Delivery: "));
        leftPanel.add(rushEligibleCombo);

        sortTypeCombo = new JComboBox<>(new String[]{"Sort by Title", "Sort by Price"});
        sortOrderCombo = new JComboBox<>(new String[]{"Asc", "Desc"});
        leftPanel.add(sortTypeCombo);
        leftPanel.add(sortOrderCombo);

        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        leftPanel.add(searchButton);
        leftPanel.add(resetButton);

        buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> processPurchase());
        leftPanel.add(buyButton);

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        profileButton = new JButton("Profile");
        logoutButton = new JButton("Logout");
        updateLoginUI(rightPanel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        topPanel.add(headerPanel, BorderLayout.CENTER);

        // Panel sản phẩm
        productPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(productPanel);

        // Panel phân trang
        JPanel paginationPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("Previous");
        pageLabel = new JLabel("Page 1");
        JButton nextButton = new JButton("Next");
        buttonPanel.add(prevButton);
        buttonPanel.add(pageLabel);
        buttonPanel.add(nextButton);

        totalPriceLabel = new JLabel("Total: 0.00 VND");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        paginationPanel.add(buttonPanel, BorderLayout.CENTER);
        paginationPanel.add(totalPriceLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        // Khởi tạo controller
        cartController = new CartController(this, new CartDAO());

        // Sự kiện
        searchButton.addActionListener(e -> {
            page = 1;
            loadProductList();
        });
        resetButton.addActionListener(e -> {
            page = 1;
            searchField.setText("");
            categoryCombo.setSelectedIndex(0);
            rushEligibleCombo.setSelectedIndex(0);
            sortTypeCombo.setSelectedIndex(0);
            sortOrderCombo.setSelectedIndex(0);
            loadProductList();
        });
        prevButton.addActionListener(e -> {
            if (page > 1) {
                page--;
                displayProducts();
            }
        });
        nextButton.addActionListener(e -> {
            if (page * PAGE_SIZE < productList.size()) {
                page++;
                displayProducts();
            }
        });
        loginButton.addActionListener(e -> new LoginFrame(this).setVisible(true));
        signupButton.addActionListener(e -> new SignupFrame(this).setVisible(true));
        profileButton.addActionListener(e -> new ProfileFrame().setVisible(true));
        logoutButton.addActionListener(e -> {
            Session.logout();
            updateLoginUI(rightPanel);
            refreshToolBar();
            loadProductList();
        });
        page=1;
        loadProductList();
    }

    private JPanel createToolBar() {
        JPanel toolBar = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton mainButton = new JButton("Home");
        JButton cartButton = new JButton("Cart");
        JButton ordersButton = new JButton("Orders");
        JButton adminButton = new JButton("Admin");

        mainButton.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });
        cartButton.addActionListener(e -> {
            dispose();
            new CartFrame().setVisible(true);
        });
        ordersButton.addActionListener(e -> {
            if (Session.getRole() != null && Session.getRole().equals("PRODUCT_MANAGER")) {
                dispose();
                new OrderFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Only Product Managers can view orders.");
            }
        });
        adminButton.addActionListener(e -> {
            if (Session.getRole() != null && Session.getRole().equals("ADMIN")) {
                dispose();
                new AdminFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Only Admins can access this panel.");
            }
        });

        leftPanel.add(mainButton);
        leftPanel.add(cartButton);
        if (Session.isLoggedIn() && Session.getRole() != null && Session.getRole().equals("PRODUCT_MANAGER")) {
            leftPanel.add(ordersButton);
        }
        if (Session.isLoggedIn() && Session.getRole() != null && Session.getRole().equals("ADMIN")) {
            leftPanel.add(adminButton);
        }

        // Thêm nhãn session_id
        sessionLabel = new JLabel("Session: " + (Session.getSessionId() != null ? Session.getSessionId() : "Guest"));
        rightPanel.add(sessionLabel);

        toolBar.add(leftPanel, BorderLayout.WEST);
        toolBar.add(rightPanel, BorderLayout.EAST);
        return toolBar;
    }

    private void updateLoginUI(JPanel rightPanel) {
        rightPanel.removeAll();
        if (Session.isLoggedIn()) {
        	rightPanel.add(buyButton);
        	rightPanel.add(profileButton);
            rightPanel.add(logoutButton);
        } else {
        	rightPanel.add(buyButton);
        	rightPanel.add(loginButton);
            rightPanel.add(signupButton);
        }
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void refreshToolBar() {
        JPanel topPanel = (JPanel) getContentPane().getComponent(0);
        topPanel.remove(0);
        toolBar = createToolBar();
        topPanel.add(toolBar, BorderLayout.NORTH);
        topPanel.revalidate();
        topPanel.repaint();
        loadProductList();
    }

    private String[] getCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT p.category FROM products p " +
                     "JOIN cart_items ci ON p.product_id = ci.product_id " +
                     "JOIN cart c ON ci.cart_id = c.cart_id " +
                     "WHERE c.session_id = ? ORDER BY p.category";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Session.getSessionId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }

    public void loadProductList() {
        productList.clear();
        totalPrice = 0.0;
        String searchText = searchField.getText().trim();
        String category = categoryCombo.getSelectedItem().equals("All") ? null : (String) categoryCombo.getSelectedItem();
        String rushEligible = (String) rushEligibleCombo.getSelectedItem();
        Boolean isRushEligible = null;
        if (rushEligible.equals("Rush Eligible")) {
            isRushEligible = true;
        } else if (rushEligible.equals("Non-Rush")) {
            isRushEligible = false;
        }
        String sortBy = (String) sortTypeCombo.getSelectedItem();
        String sortOrder = (String) sortOrderCombo.getSelectedItem();
        String sortColumn = sortBy.equals("Sort by Title") ? "p.title" : "p.price";
        String order = sortOrder.equals("Asc") ? "ASC" : "DESC";

        List<CartItem> cartItems = cartController.getCartItems(Session.getSessionId(), searchText, category, isRushEligible, sortColumn, order);
        for (CartItem item : cartItems) {
            ProductPanel panel = ProductPanel.createFromCartItem(item, this);
            productList.add(panel);
            totalPrice += item.getQuantity() * item.getProduct().getPrice();
        }

        updateTotalPrice();
        
        displayProducts();
    }

    private void updateTotalPrice() {
        totalPriceLabel.setText(String.format("Total: %.2f VND", totalPrice));
    }

    public void displayProducts() {
        productPanel.removeAll();
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, productList.size());
        for (int i = start; i < end; i++) {
            productPanel.add(productList.get(i));
        }
        pageLabel.setText("Page " + page);
        productPanel.revalidate();
        productPanel.repaint();
    }
    
    
    
    
    
    private void processPurchase() {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "You must log in to make a purchase.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (totalPrice <= 0.01) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField recipientNameField = new JTextField();
        JTextField emailField = new JTextField(Session.getCurrentUser().getEmail());
        JTextField phoneField = new JTextField();
        JTextField deliveryAddressField = new JTextField();
        JTextField provinceCityField = new JTextField();
        JCheckBox rushCheckbox = new JCheckBox("Rush Delivery");
        Object[] inputFields = {
            "Recipient Name:", recipientNameField,
            "Email:", emailField,
            "Phone Number:", phoneField,
            "Delivery Address:", deliveryAddressField,
            "Province/City:", provinceCityField,
            "Rush Delivery:", rushCheckbox
        };
        int option = JOptionPane.showConfirmDialog(this, inputFields, "Enter Shipping Information", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String recipientName = recipientNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String deliveryAddress = deliveryAddressField.getText().trim();
            String provinceCity = provinceCityField.getText().trim();
            boolean isRushDelivery = rushCheckbox.isSelected();

            if (recipientName.isEmpty() || email.isEmpty() || phone.isEmpty() || deliveryAddress.isEmpty() || provinceCity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!Pattern.matches(emailRegex, email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder purchaseDetails = new StringBuilder();
            purchaseDetails.append("Purchase Details:\n\n");
            purchaseDetails.append("Buyer: ").append(Session.getSessionId()).append("\n");
            purchaseDetails.append("Recipient: ").append(recipientName).append("\n");
            purchaseDetails.append("Email: ").append(email).append("\n");
            purchaseDetails.append("Phone Number: ").append(phone).append("\n");
            purchaseDetails.append("Delivery Address: ").append(deliveryAddress).append("\n");
            purchaseDetails.append("Province/City: ").append(provinceCity).append("\n");
            purchaseDetails.append("Rush Delivery: ").append(isRushDelivery ? "Yes" : "No").append("\n\n");
            purchaseDetails.append("Items:\n");
            for (ProductPanel panel : productList) {
                purchaseDetails.append(String.format("- %s | Price: %.2f VND | Quantity: %d\n",
                        panel.getProduct().getTitle(), panel.getProduct().getPrice(), panel.getQuantity()));
            }
            purchaseDetails.append(String.format("\nTotal: %.2f VND", totalPrice));

            int confirmOption = JOptionPane.showConfirmDialog(this,
                    new Object[]{new JScrollPane(new JTextArea(purchaseDetails.toString(), 15, 40))},
                    "Confirm Purchase", JOptionPane.OK_CANCEL_OPTION);
            if (confirmOption == JOptionPane.OK_OPTION) {
                try {
                    cartController.completePurchase(Session.getSessionId(), recipientName, email, phone, deliveryAddress, provinceCity, isRushDelivery, totalPrice);
                    JOptionPane.showMessageDialog(this, "Purchase completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProductList();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Purchase failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }
}