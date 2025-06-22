package com.aims.dao;

import com.aims.entity.Cart;
import com.aims.entity.CartItem;
import com.aims.entity.Product;
import com.aims.util.DatabaseConnection;
import com.aims.util.Session;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CartDAO {
    public Cart getCartBySessionId(String sessionId) {
        if (sessionId == null) {
            return null;
        }

        String sql = "SELECT * FROM cart WHERE session_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setSessionId(rs.getString("session_id"));
                cart.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return cart;
            } else {
                return createNewCart(sessionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Cart createNewCart(String sessionId) {
        String sql = "INSERT INTO cart (session_id, created_at) VALUES (?, CURRENT_TIMESTAMP) RETURNING cart_id, session_id, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setSessionId(rs.getString("session_id"));
                cart.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return cart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void mergeCart(String oldSessionId, String newSessionId) {
        if (oldSessionId == null || newSessionId == null || oldSessionId.equals(newSessionId)) {
            return;
        }

        String sql = "UPDATE cart SET session_id = ? WHERE session_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newSessionId);
            stmt.setString(2, oldSessionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Trùng session_id
                // Nếu newSessionId đã tồn tại, xóa cart cũ và thêm cart_items vào cart mới
                mergeCartItems(oldSessionId, newSessionId);
            } else {
                e.printStackTrace();
            }
        }
    }

    private void mergeCartItems(String oldSessionId, String newSessionId) {
        Cart oldCart = getCartBySessionId(oldSessionId);
        Cart newCart = getCartBySessionId(newSessionId);
        if (oldCart == null || newCart == null) {
            return;
        }

        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity) " +
                     "SELECT ?, product_id, quantity FROM cart_items WHERE cart_id = ? " +
                     "ON CONFLICT (cart_id, product_id) DO UPDATE SET quantity = cart_items.quantity + EXCLUDED.quantity";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newCart.getCartId());
            stmt.setInt(2, oldCart.getCartId());
            stmt.executeUpdate();

            // Xóa cart_items cũ
            String deleteSql = "DELETE FROM cart_items WHERE cart_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, oldCart.getCartId());
                deleteStmt.executeUpdate();
            }

            // Xóa cart cũ
            deleteSql = "DELETE FROM cart WHERE cart_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, oldCart.getCartId());
                deleteStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CartItem> getCartItems(String sessionId, String searchText, String category, Boolean isRushEligible, String sortColumn, String order) {
        List<CartItem> cartItems = new ArrayList<>();
        Cart cart = getCartBySessionId(sessionId);
        if (cart == null) {
            return cartItems;
        }

        StringBuilder sql = new StringBuilder("SELECT ci.*, p.* " +
                                              "FROM cart_items ci " +
                                              "JOIN products p ON ci.product_id = p.product_id " +
                                              "WHERE ci.cart_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(cart.getCartId());

        if (searchText != null && !searchText.isEmpty()) {
            sql.append(" AND LOWER(p.title) LIKE LOWER(?)");
            params.add("%" + searchText + "%");
        }
        if (category != null) {
            sql.append(" AND p.category = ?");
            params.add(category);
        }
        if (isRushEligible != null) {
            sql.append(" AND p.is_rush_eligible = ?");
            params.add(isRushEligible);
        }
        sql.append(" ORDER BY ").append(sortColumn).append(" ").append(order);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));

                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setTitle(rs.getString("title"));
                product.setCategory(rs.getString("category"));
                product.setValue(rs.getDouble("value"));
                product.setPrice(rs.getDouble("price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setRushEligible(rs.getBoolean("is_rush_eligible"));
                product.setWeight(rs.getDouble("weight"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                product.setAuthors(rs.getString("authors"));
                product.setCoverType(rs.getString("cover_type"));
                product.setPublisher(rs.getString("publisher"));
                if (rs.getDate("publication_date") != null) {
                    product.setPublicationDate(rs.getDate("publication_date").toLocalDate());
                }
                product.setNumPages(rs.getObject("num_pages") != null ? rs.getInt("num_pages") : null);
                product.setBookLanguage(rs.getString("book_language"));
                product.setBookGenre(rs.getString("book_genre"));
                product.setArtists(rs.getString("artists"));
                product.setRecordLabel(rs.getString("record_label"));
                product.setTracklist(rs.getString("tracklist"));
                product.setMusicGenre(rs.getString("music_genre"));
                if (rs.getDate("release_date") != null) {
                    product.setReleaseDate(rs.getDate("release_date").toLocalDate());
                }
                product.setDiscType(rs.getString("disc_type"));
                product.setDirector(rs.getString("director"));
                product.setRuntime(rs.getObject("runtime") != null ? rs.getInt("runtime") : null);
                product.setStudio(rs.getString("studio"));
                product.setDvdLanguage(rs.getString("dvd_language"));
                product.setSubtitles(rs.getString("subtitles"));
                product.setAvailable(rs.getString("available"));
                item.setProduct(product);
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public void addCartItem(String sessionId, int productId, int quantity) {
        Cart cart = getCartBySessionId(sessionId);
        if (cart == null) {
            return;
        }

        // Kiểm tra stock_quantity
        String checkStockSql = "SELECT stock_quantity FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkStockSql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next() || rs.getInt("stock_quantity") < quantity) {
                throw new SQLException("Not enough stock or product does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity) " +
                     "VALUES (?, ?, ?) ON CONFLICT (cart_id, product_id) " +
                     "DO UPDATE SET quantity = cart_items.quantity + EXCLUDED.quantity";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cart.getCartId());
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCartItem(int cartItemId, int quantity) {
        // Kiểm tra stock_quantity
        String checkStockSql = "SELECT p.stock_quantity FROM cart_items ci " +
                              "JOIN products p ON ci.product_id = p.product_id WHERE ci.cart_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkStockSql)) {
            stmt.setInt(1, cartItemId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next() || rs.getInt("stock_quantity") < quantity) {
                throw new SQLException("Not enough stock.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void completePurchase(String sessionId, String recipientName, String email, String phoneNumber, String deliveryAddress, String provinceCity, boolean isRushDelivery, double totalAmount) {
        Cart cart = getCartBySessionId(sessionId);
        if (cart == null) {
            return;
        }

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderItemStmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String orderSQL = "INSERT INTO orders (user_id, recipient_name, email, phone_number, delivery_address, province_city, delivery_fee, is_rush_delivery, total_amount, status, created_at) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING', CURRENT_TIMESTAMP) RETURNING order_id";
            orderStmt = conn.prepareStatement(orderSQL);
            orderStmt.setInt(1, Session.getCurrentUser().getUserId());
            orderStmt.setString(2, recipientName);
            orderStmt.setString(3, email);
            orderStmt.setString(4, phoneNumber);
            orderStmt.setString(5, deliveryAddress);
            orderStmt.setString(6, provinceCity);
            orderStmt.setBigDecimal(7, new java.math.BigDecimal("0.00")); // Giả định phí giao hàng
            orderStmt.setBoolean(8, isRushDelivery);
            orderStmt.setBigDecimal(9, new java.math.BigDecimal(totalAmount).setScale(2, java.math.RoundingMode.HALF_UP));
            orderStmt.executeUpdate();

            generatedKeys = orderStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Failed to retrieve order ID.");
            }
            int orderId = generatedKeys.getInt(1);

            String orderItemSQL = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            orderItemStmt = conn.prepareStatement(orderItemSQL);
            List<CartItem> cartItems = getCartItems(sessionId, null, null, null, "p.title", "ASC");
            for (CartItem item : cartItems) {
                // Kiểm tra stock_quantity
                String checkStockSql = "SELECT stock_quantity FROM products WHERE product_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
                    checkStmt.setInt(1, item.getProductId());
                    ResultSet rs = checkStmt.executeQuery();
                    if (!rs.next() || rs.getInt("stock_quantity") < item.getQuantity()) {
                        throw new SQLException("Not enough stock for product ID: " + item.getProductId());
                    }
                }

                // Cập nhật stock_quantity
                String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateStockSql)) {
                    updateStmt.setInt(1, item.getQuantity());
                    updateStmt.setInt(2, item.getProductId());
                    updateStmt.executeUpdate();
                }

                orderItemStmt.setInt(1, orderId);
                orderItemStmt.setInt(2, item.getProductId());
                orderItemStmt.setInt(3, item.getQuantity());
                orderItemStmt.setBigDecimal(4, new java.math.BigDecimal(item.getProduct().getPrice()).setScale(2, java.math.RoundingMode.HALF_UP));
                orderItemStmt.addBatch();
            }
            orderItemStmt.executeBatch();

            String deleteSQL = "DELETE FROM cart_items WHERE cart_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setInt(1, cart.getCartId());
                deleteStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (orderStmt != null) orderStmt.close();
                if (orderItemStmt != null) orderItemStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}