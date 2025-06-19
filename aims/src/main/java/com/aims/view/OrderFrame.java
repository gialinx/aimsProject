package com.aims.view;

import com.aims.controller.OrderController;
import com.aims.dao.OrderDAO;
import com.aims.entity.Order;
import com.aims.util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderFrame extends JFrame {
    private JTable orderTable;
    private OrderController orderController;

    public OrderFrame() {
        setTitle("AIMS - Order Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thanh bar trên cùng
        JPanel toolBar = new JPanel(new FlowLayout());
        JButton mainButton = new JButton("Home");
        JButton cartButton = new JButton("Cart");
        JButton ordersButton = new JButton("Orders");
        JButton adminButton = new JButton("Admin");
        JButton profileButton = new JButton("Profile");
        JButton logoutButton = new JButton("Logout");

        mainButton.addActionListener(e -> new MainFrame().setVisible(true));
        cartButton.addActionListener(e -> new CartFrame().setVisible(true));
        ordersButton.addActionListener(e -> new OrderFrame().setVisible(true));
        adminButton.addActionListener(e -> {
            if (Session.getRole() != null && Session.getRole().equals("ADMIN")) {
                new AdminFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Only Admins can access this panel.");
            }
        });
        profileButton.addActionListener(e -> new ProfileFrame().setVisible(true));
        logoutButton.addActionListener(e -> {
            Session.logout();
            new MainFrame().setVisible(true);
            dispose();
        });

        toolBar.add(mainButton);
        toolBar.add(cartButton);
        toolBar.add(ordersButton);
        if (Session.isLoggedIn() && Session.getRole().equals("ADMIN")) {
            toolBar.add(adminButton);
        }
        if (Session.isLoggedIn()) {
            toolBar.add(profileButton);
            toolBar.add(logoutButton);
        }

        // Bảng đơn hàng
        String[] columns = {"ID", "Recipient", "Email", "Address", "Status", "Total", "Actions"};
        orderTable = new JTable(new DefaultTableModel(new Object[][]{}, columns));
        JScrollPane scrollPane = new JScrollPane(orderTable);

        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        orderController = new OrderController(this, new OrderDAO());
        loadOrders();
    }

    public void loadOrders() {
        List<Order> orders = orderController.getOrders();
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        model.setRowCount(0);
        for (Order order : orders) {
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"PENDING", "APPROVED", "REJECTED", "CANCELED"});
            statusCombo.setSelectedItem(order.getStatus());
            statusCombo.addActionListener(e -> {
                orderController.updateOrderStatus(order.getOrderId(), (String) statusCombo.getSelectedItem());
            });
            model.addRow(new Object[]{
                order.getOrderId(),
                order.getRecipientName(),
                order.getEmail(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getTotalAmount(),
                statusCombo
            });
        }
    }

    public JTable getOrderTable() {
        return orderTable;
    }
}