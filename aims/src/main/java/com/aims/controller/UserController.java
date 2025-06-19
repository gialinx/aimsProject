package com.aims.controller;

import com.aims.dao.UserDAO;
import com.aims.entity.User;
import com.aims.view.AdminFrame;
import com.aims.view.MainFrame;

import java.util.List;
import javax.swing.JFrame;

public class UserController {
    private AdminFrame adminView;
    private MainFrame mainView;
    private UserDAO userDAO;

    public UserController(JFrame view, UserDAO userDAO) {
        if (view instanceof AdminFrame) {
            this.adminView = (AdminFrame) view;
        } else if (view instanceof MainFrame) {
            this.mainView = (MainFrame) view;
        }
        this.userDAO = userDAO;
    }

    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userDAO.getUserByUsernameAndPassword(username, password);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void addUser(User user) {
        try {
            userDAO.addUser(user);
            if (adminView != null) {
                adminView.loadUsers();
            }
            if (mainView != null) {
                mainView.loadProductList();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage());
        }
    }

    public void updateUser(User user) {
        try {
            userDAO.updateUser(user);
            if (adminView != null) {
                adminView.loadUsers();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) {
        try {
            userDAO.deleteUser(userId);
            if (adminView != null) {
                adminView.loadUsers();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }

    public void resetPassword(int userId) {
        try {
            userDAO.resetPassword(userId);
            if (adminView != null) {
                adminView.loadUsers();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to reset password: " + e.getMessage());
        }
    }

    public void toggleBlockUser(int userId, boolean isBlocked) {
        try {
            userDAO.toggleBlockUser(userId, isBlocked);
            if (adminView != null) {
                adminView.loadUsers();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to toggle block status: " + e.getMessage());
        }
    }
}