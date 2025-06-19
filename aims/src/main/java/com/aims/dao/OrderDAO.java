package com.aims.dao;

import com.aims.entity.Order;
import com.aims.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                order.setRecipientName(rs.getString("recipient_name"));
                order.setEmail(rs.getString("email"));
                order.setPhoneNumber(rs.getString("phone_number"));
                order.setDeliveryAddress(rs.getString("delivery_address"));
                order.setProvinceCity(rs.getString("province_city"));
                order.setDeliveryFee(rs.getDouble("delivery_fee"));
                order.setRushDelivery(rs.getBoolean("is_rush_delivery"));
                order.setRushDeliveryTime(rs.getTimestamp("rush_delivery_time") != null ?
                    rs.getTimestamp("rush_delivery_time").toLocalDateTime() : null);
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}