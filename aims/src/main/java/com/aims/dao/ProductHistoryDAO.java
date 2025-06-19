package com.aims.dao;

import com.aims.entity.ProductHistory;
import com.aims.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductHistoryDAO {
    public List<ProductHistory> getAllProductHistories() {
        List<ProductHistory> histories = new ArrayList<>();
        String sql = "SELECT * FROM product_history";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProductHistory ph = new ProductHistory();
                ph.setHistoryId(rs.getInt("history_id"));
                ph.setProductId(rs.getInt("product_id"));
                ph.setOperation(rs.getString("operation"));
                ph.setDescription(rs.getString("description"));
                ph.setUserId(rs.getInt("user_id"));
                ph.setCreatedAt(rs.getTimestamp("created_at"));
                histories.add(ph);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return histories;
    }

    public void addProductHistory(ProductHistory ph) {
        String sql = "INSERT INTO product_history (product_id, operation, description, user_id, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ph.getProductId());
            stmt.setString(2, ph.getOperation());
            stmt.setString(3, ph.getDescription());
            stmt.setInt(4, ph.getUserId());
            stmt.setTimestamp(5,ph.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
