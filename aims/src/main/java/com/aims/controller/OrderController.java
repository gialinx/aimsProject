package com.aims.controller;

import com.aims.dao.OrderDAO;
import com.aims.entity.Order;
import com.aims.view.OrderFrame;

import java.util.List;

public class OrderController {
    private OrderFrame view;
    private OrderDAO orderDAO;

    public OrderController(OrderFrame view, OrderDAO orderDAO) {
        this.view = view;
        this.orderDAO = orderDAO;
    }

    public List<Order> getOrders() {
        return orderDAO.getOrders();
    }

    public void updateOrderStatus(int orderId, String status) {
        orderDAO.updateOrderStatus(orderId, status);
        view.loadOrders();
    }
}