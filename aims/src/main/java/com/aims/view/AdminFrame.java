// AdminFrame.java
package com.aims.view;

import com.aims.controller.UserController;
import com.aims.dao.UserDAO;
import com.aims.entity.User;
import com.aims.util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {
    private JTable userTable;
    private UserController userController;

    public AdminFrame() {
        setTitle("AIMS - Admin Panel");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel toolBar = new JPanel(new FlowLayout());
        JButton mainButton = new JButton("Home");
        JButton logoutButton = new JButton("Logout");

        mainButton.addActionListener(e -> {
            dispose();
        });
        logoutButton.addActionListener(e -> {
            Session.logout();
            new MainFrame().setVisible(true);
            dispose();
        });

        toolBar.add(mainButton);
        toolBar.add(logoutButton);

        String[] columns = {"ID", "Username", "Email", "Role", "Blocked"};
        userTable = new JTable(new DefaultTableModel(new Object[][]{}, columns));
        JScrollPane scrollPane = new JScrollPane(userTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit Selected");

        addUserButton.addActionListener(e -> new AddUserFrame(this).setVisible(true));
        editUserButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (int) userTable.getValueAt(selectedRow, 0);
                User user = userController.getUserById(userId);
                new EditUserFrame(this, user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            }
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);

//        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        userController = new UserController(this, new UserDAO());
        loadUsers();
    }

    public void loadUsers() {
        List<User> users = userController.getAllUsers();
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        for (User user : users) {
            model.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isBlocked() ? "Yes" : "No"
            });
        }
    }

    public JTable getUserTable() {
        return userTable;
    }
}