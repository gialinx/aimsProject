package com.aims.dao;

import com.aims.entity.Transaction;
import com.aims.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setTransactionId(rs.getInt("transaction_id"));
                tx.setOrderId(rs.getInt("order_id"));
                tx.setTransactionVnpayId(rs.getString("transaction_vnpay_id"));
                tx.setAmount(rs.getDouble("amount"));
                tx.setContent(rs.getString("content"));
                tx.setCreatedAt(rs.getTimestamp("created_at"));
                transactions.add(tx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void addTransaction(Transaction tx) {
        String sql = "INSERT INTO transactions (order_id, transaction_vnpay_id, amount, content, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tx.getOrderId());
            stmt.setString(2, tx.getTransactionVnpayId());
            stmt.setDouble(3, tx.getAmount());
            stmt.setString(4, tx.getContent());
            stmt.setTimestamp(5, tx.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
