package com.aims.view;

import com.aims.controller.UserController;
import com.aims.dao.UserDAO;
import com.aims.entity.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class AddUserFrame extends JDialog {
    private UserController userController;
    private User user;

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JCheckBox blockedCheck;

    public AddUserFrame(AdminFrame parentFrame) {
        this(parentFrame, null);
    }

    public AddUserFrame(AdminFrame parentFrame, User user) {
        super(parentFrame, true);
        this.user = user;
        this.userController = new UserController(parentFrame, new UserDAO());

        setTitle(user == null ? "Add User" : "Edit User");
        setSize(400, 350);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleCombo = new JComboBox<>(new String[]{"CUSTOMER", "PRODUCT_MANAGER", "ADMIN"});
        blockedCheck = new JCheckBox("Blocked");

        mainPanel.add(new JLabel("Username:"));
        if (user != null) {
            usernameField.setText(user.getUsername());
            usernameField.setEditable(false); // không cho sửa username
        }
        mainPanel.add(usernameField);

        mainPanel.add(new JLabel("Email:"));
        mainPanel.add(emailField);

        mainPanel.add(new JLabel("Password:"));
        mainPanel.add(passwordField);

        mainPanel.add(new JLabel("Role:"));
        mainPanel.add(roleCombo);

        mainPanel.add(new JLabel("Blocked:"));
        mainPanel.add(blockedCheck);

        JButton saveButton = new JButton(user == null ? "Add" : "Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());

        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);

        if (user != null) {
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            roleCombo.setSelectedItem(user.getRole());
            blockedCheck.setSelected(user.isBlocked());
        }
    }

    private void saveUser() {
        try {
            if (usernameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Email are required.");
                return;
            }

            if (user == null) {
                // Add user
                User newUser = new User();
                newUser.setUsername(usernameField.getText().trim());
                newUser.setEmail(emailField.getText().trim());
                newUser.setPassword(new String(passwordField.getPassword()).trim().isEmpty()
                        ? "default_password" : new String(passwordField.getPassword()).trim()); // đặt mật khẩu mặc định nếu rỗng
                newUser.setRole((String) roleCombo.getSelectedItem());
                newUser.setBlocked(blockedCheck.isSelected());
                newUser.setCreatedAt(LocalDateTime.now());

                userController.addUser(newUser);
            } else {
                // Edit user
                user.setEmail(emailField.getText().trim());
                user.setPassword(new String(passwordField.getPassword()).trim());
                user.setRole((String) roleCombo.getSelectedItem());
                user.setBlocked(blockedCheck.isSelected());

                userController.updateUser(user);
            }

            ((AdminFrame) getOwner()).loadUsers();
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save user: " + ex.getMessage());
        }
    }
}
