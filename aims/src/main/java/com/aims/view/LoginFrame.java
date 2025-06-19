package com.aims.view;

import com.aims.controller.CartController;
import com.aims.model.Conste;
import com.aims.dao.CartDAO;
import com.aims.dao.UserDAO;
import com.aims.entity.User;
import com.aims.util.Session;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame parentFrame;

    public LoginFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)); // Tăng số dòng
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Nút home
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dispose(); // đóng LoginFrame hiện tại
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
        panel.add(homeButton);

        // Nút login
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);
        
        add(panel);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            Conste.changeConst(1);
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsernameAndPassword(username, password);
        if (user != null) {
            String oldSessionId = Session.getSessionId();
            Session.login(user);
            CartController cartController = new CartController(null, new CartDAO());
            cartController.mergeCart(oldSessionId, username);

            Conste.changeConst(2);
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

            if (parentFrame instanceof CartFrame) {
                ((CartFrame) parentFrame).refreshToolBar();
                ((CartFrame) parentFrame).loadProductList();
            } else if (parentFrame instanceof MainFrame) {
                ((MainFrame) parentFrame).refreshToolBar();
            }
            dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            Conste.changeConst(1);
            JOptionPane.showMessageDialog(this, "Invalid username, password, or account is blocked.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
