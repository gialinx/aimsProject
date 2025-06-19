package com.aims.view;

import com.aims.controller.UserController;
import com.aims.dao.UserDAO;
import com.aims.entity.User;
import com.aims.util.Session;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JDialog {
    private UserController userController;
    private JTextField usernameField, emailField, passwordField;

    public ProfileFrame() {
        super((Frame) null, true);
        userController = new UserController(null, new UserDAO());
        setTitle("Profile");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField(Session.getCurrentUser().getUsername(), 20);
        emailField = new JTextField(Session.getCurrentUser().getEmail(), 20);
        passwordField = new JPasswordField(20);

        mainPanel.add(new JLabel("Username:"));
        mainPanel.add(usernameField);
        mainPanel.add(new JLabel("Email:"));
        mainPanel.add(emailField);
        mainPanel.add(new JLabel("New Password:"));
        mainPanel.add(passwordField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveProfile());
        cancelButton.addActionListener(e -> dispose());

        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);
    }

    private void saveProfile() {
        try {
            User user = Session.getCurrentUser();
            user.setUsername(usernameField.getText());
            user.setEmail(emailField.getText());
            if (!passwordField.getText().isEmpty()) {
                user.setPassword(passwordField.getText()); // Cần mã hóa trong thực tế
            }
            userController.updateUser(user);
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}