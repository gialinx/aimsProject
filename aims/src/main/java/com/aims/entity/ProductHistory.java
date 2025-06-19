package com.aims.entity;

import java.sql.Timestamp;

public class ProductHistory {
    private int historyId;
    private int productId;
    private String operation;
    private String description;
    private int userId;
    private Timestamp createdAt;
    
    // Constructor
    public ProductHistory() {}
     
    public ProductHistory(int historyId, int productId, String operation, String description, int userId,
			Timestamp createdAt) {
		super();
		this.historyId = historyId;
		this.productId = productId;
		this.operation = operation;
		this.description = description;
		this.userId = userId;
		this.createdAt = createdAt;
	}

	// Getters and Setters
	public int getHistoryId() {
		return historyId;
	}
	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
    
    
    
}
