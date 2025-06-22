package com.aims.dao;

import com.aims.entity.Order;
import com.aims.entity.OrderItem;
import com.aims.entity.Product;
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
        String sql = "SELECT * FROM orders ORDER BY order_id ASC";
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
    
    public Order getOrderById(int orderId) {
        Order order = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Lấy thông tin chính
            String sql = "SELECT * FROM orders WHERE order_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setOrderId(orderId);
                order.setRecipientName(rs.getString("recipient_name"));
                order.setEmail(rs.getString("email"));
                order.setDeliveryAddress(rs.getString("delivery_address"));
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(rs.getDouble("total_amount"));
            }

            // Lấy các sản phẩm trong order
            if (order != null) {
                String itemSql = "SELECT oi.*, p.* FROM order_items oi JOIN products p ON oi.product_id = p.product_id WHERE oi.order_id = ?";
                PreparedStatement itemStmt = conn.prepareStatement(itemSql);
                itemStmt.setInt(1, orderId);
                ResultSet itemRs = itemStmt.executeQuery();
                List<OrderItem> items = new ArrayList<>();
                while (itemRs.next()) {
                    Product product = new Product();
                    product.setProductId(itemRs.getInt("product_id"));
                    product.setTitle(itemRs.getString("title"));
                    // (nếu cần có thể set thêm thuộc tính)

                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setQuantity(itemRs.getInt("quantity"));
                    item.setPrice(itemRs.getDouble("price"));
                    items.add(item);
                }
                order.setOrderItems(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

}