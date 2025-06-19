// EditUserFrame.java
package com.aims.view;

import com.aims.controller.UserController;
import com.aims.dao.UserDAO;
import com.aims.entity.User;

import javax.swing.*;
import java.awt.*;

public class EditUserFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JCheckBox blockedCheckBox;
    private UserController userController;
    private AdminFrame adminFrame;
    private User user;

    public EditUserFrame(AdminFrame adminFrame, User user) {
        this.adminFrame = adminFrame;
        this.user = user;
        this.userController = new UserController(adminFrame, new UserDAO());

        setTitle("Edit User");
        setSize(350, 300);
        setLocationRelativeTo(adminFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        panel.add(new JLabel(user.getUsername()));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField(user.getEmail());
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(user.getPassword());
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"CUSTOMER", "PRODUCT_MANAGER", "ADMIN"});
        roleComboBox.setSelectedItem(user.getRole());
        panel.add(roleComboBox);

        panel.add(new JLabel("Blocked:"));
        blockedCheckBox = new JCheckBox("Blocked", user.isBlocked());
        panel.add(blockedCheckBox);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveUser());
        panel.add(new JLabel());
        panel.add(saveButton);

        add(panel);
    }

    private void saveUser() {
        user.setEmail(emailField.getText().trim());
        user.setPassword(new String(passwordField.getPassword()).trim());
        user.setRole((String) roleComboBox.getSelectedItem());
        user.setBlocked(blockedCheckBox.isSelected());

        userController.updateUser(user);
        adminFrame.loadUsers();
        JOptionPane.showMessageDialog(this, "User updated successfully.");
        dispose();
    }
}
