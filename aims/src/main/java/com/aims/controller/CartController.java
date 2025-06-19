package com.aims.controller;

import com.aims.dao.CartDAO;
import com.aims.entity.CartItem;
import com.aims.view.CartFrame;

import java.sql.SQLException;
import java.util.List;

public class CartController {
    private CartFrame view;
    private CartDAO cartDAO;

    public CartController(CartFrame view, CartDAO cartDAO) {
        this.view = view;
        this.cartDAO = cartDAO;
    }

    public List<CartItem> getCartItems(String sessionId, String searchText, String category, Boolean isRushEligible, String sortColumn, String order) {
        return cartDAO.getCartItems(sessionId, searchText, category, isRushEligible, sortColumn, order);
    }

    public void addCartItem(String sessionId, int productId, int quantity) {
        cartDAO.addCartItem(sessionId, productId, quantity);
        if(view!=null) {
            view.loadProductList();        	
        }
    }

    public void updateCartItem(int cartItemId, int quantity) {
        cartDAO.updateCartItem(cartItemId, quantity);
        if (view != null) {
            view.loadProductList();
        }
    }

    public void deleteCartItem(int cartItemId) {
        cartDAO.deleteCartItem(cartItemId);
        if (view != null) {
            view.loadProductList();
        }
    }

    public void completePurchase(String sessionId, String recipientName, String email, String phoneNumber, String deliveryAddress, String provinceCity, boolean isRushDelivery, double totalPrice) throws SQLException {
        cartDAO.completePurchase(sessionId, recipientName, email, phoneNumber, deliveryAddress, provinceCity, isRushDelivery, totalPrice);
        if (view != null) {
            view.loadProductList();
        }
    }

    public void mergeCart(String oldSessionId, String newSessionId) {
        cartDAO.mergeCart(oldSessionId, newSessionId);
        if (view != null) {
            view.loadProductList();
        }
    }
}