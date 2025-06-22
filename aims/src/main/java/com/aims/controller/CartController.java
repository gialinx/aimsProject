package com.aims.controller;

import com.aims.dao.CartDAO;
import com.aims.entity.CartItem;
import com.aims.entity.User;
import com.aims.entity.OrderItem;
import com.aims.entity.Product;
import com.aims.view.CartFrame;
import com.aims.util.DatabaseConnection;

import java.util.List;
import java.util.UUID;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public void completePurchase(Integer userId,
    			String sessionId,
	            String recipientName,
	            String email,
	            String phoneNumber,
	            String deliveryAddress,
	            String provinceCity,
	            boolean isRushDelivery,
                String rushDeliveryTime,
	            double totalAmount,
	            double deliveryFee,
	            List<OrderItem> orderItems,
	            List<OrderItem> rushItems,
	            String paymentMethod) throws SQLException {
	
	String orderSql = "INSERT INTO orders (user_id, recipient_name, email, phone_number, delivery_address, province_city, delivery_fee, is_rush_delivery, rush_delivery_time, total_amount, status, created_at) " +
	  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING', CURRENT_TIMESTAMP) RETURNING order_id";
	
	String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price, is_rush) VALUES (?, ?, ?, ?, ?)";
	
	String txnSql = "INSERT INTO transactions (order_id, transaction_vnpay_id, amount, content) VALUES (?, ?, ?, ?)";
	
	try (Connection conn = DatabaseConnection.getConnection()) {
		conn.setAutoCommit(false);
		
		int orderId;
        		                                            
		try (PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
			if (userId != null) {
			    orderStmt.setInt(1, userId);
			} else {
			    orderStmt.setNull(1, java.sql.Types.INTEGER);
			}
			orderStmt.setString(2, recipientName);
			orderStmt.setString(3, email);
			orderStmt.setString(4, phoneNumber);
			orderStmt.setString(5, deliveryAddress);
			orderStmt.setString(6, provinceCity);
			orderStmt.setDouble(7, deliveryFee);
			try {
			    if (rushDeliveryTime != null && !rushDeliveryTime.isEmpty()) {
			    	
			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // định dạng phù hợp với dữ liệu
			        Date parsedDate = dateFormat.parse(rushDeliveryTime+":00");
			        Timestamp timestamp = new Timestamp(parsedDate.getTime());
			        orderStmt.setTimestamp(9, timestamp);
			    } else {
			        orderStmt.setNull(9, java.sql.Types.TIMESTAMP);
			    }
			} catch (ParseException e) {
			    throw new SQLException("Lỗi định dạng thời gian giao hàng nhanh", e);
			}
			orderStmt.setBoolean(8, isRushDelivery);
			orderStmt.setDouble(10, totalAmount);
		
			try (ResultSet rs = orderStmt.executeQuery()) {
				if (!rs.next()) throw new SQLException("Failed to insert order");
				orderId = rs.getInt("order_id");
			}
		}
		
		try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
			for (OrderItem item : orderItems) {
				boolean isRush = isRushDelivery && rushItems.stream().anyMatch(r -> r.getProduct().getProductId() == item.getProduct().getProductId());
				itemStmt.setInt(1, orderId);
				itemStmt.setInt(2, item.getProduct().getProductId());
				itemStmt.setInt(3, item.getQuantity());
				itemStmt.setDouble(4, item.getPrice());
				itemStmt.setBoolean(5, isRush);
				itemStmt.addBatch();
			}
			itemStmt.executeBatch();
		}
		
		if (paymentMethod.equalsIgnoreCase("VNPay")) {
			try (PreparedStatement transactionStmt = conn.prepareStatement(txnSql)) {
				String txnId = UUID.randomUUID().toString();
				String content = "Thanh toan don hang AIMS";
				transactionStmt.setInt(1, orderId);
				transactionStmt.setString(2, txnId);
				transactionStmt.setDouble(3, totalAmount);
				transactionStmt.setString(4, content);
				transactionStmt.executeUpdate();
			}
		}
		
		String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ? AND stock_quantity >= ?";
		for (OrderItem item : orderItems) {
			try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
				stockStmt.setInt(1, item.getQuantity());
				stockStmt.setInt(2, item.getProduct().getProductId());
				stockStmt.setInt(3, item.getQuantity());
				int rows = stockStmt.executeUpdate();
				if (rows == 0) throw new SQLException("Not enough stock for product: " + item.getProduct().getTitle());
			}
		}

		String deleteCartSql = "DELETE FROM cart WHERE session_id = ?";
		try (PreparedStatement deleteStmt = conn.prepareStatement(deleteCartSql)) {
		        deleteStmt.setString(1, sessionId);
		        deleteStmt.executeUpdate();
		}

		conn.commit();
		} catch (SQLException ex) {
		throw ex;
		}
	}
    
    public void mergeCart(String oldSessionId, String newSessionId) {
        cartDAO.mergeCart(oldSessionId, newSessionId);
        if (view != null) {
            view.loadProductList();
        }
    }
    

}