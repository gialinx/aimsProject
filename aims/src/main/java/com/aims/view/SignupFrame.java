package com.aims.view;

import com.aims.controller.CartController;
import com.aims.dao.CartDAO;
import com.aims.dao.UserDAO;
import com.aims.entity.User;
import com.aims.util.Session;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class SignupFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JFrame parentFrame;

    public SignupFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Sign Up");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dispose(); // đóng signup frame
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
        panel.add(homeButton);

        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(e -> signup());
        panel.add(signupButton);
        
        add(panel);
    }

    private void signup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.length() < 3 || username.length() > 50) {
            JOptionPane.showMessageDialog(this, "Username must be between 3 and 50 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length() < 6 || password.length() > 100) {
            JOptionPane.showMessageDialog(this, "Password must be between 6 and 100 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        try {
            userDAO.addUser(user);
            JOptionPane.showMessageDialog(this, "Sign up successful! You are now logged in.", "Success", JOptionPane.INFORMATION_MESSAGE);

            String oldSessionId = Session.getSessionId();
            Session.login(user);
            CartController cartController = new CartController(null, new CartDAO());
            cartController.mergeCart(oldSessionId, username);

            if (parentFrame instanceof CartFrame) {
                ((CartFrame) parentFrame).refreshToolBar();
                ((CartFrame) parentFrame).loadProductList();
            } else if (parentFrame instanceof MainFrame) {
                ((MainFrame) parentFrame).refreshToolBar();
            }
            dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
