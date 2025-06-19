package com.aims.entity;

import java.sql.Timestamp;

public class Transaction {
	private int transactionId;
    private int orderId;
    private String transactionVnpayId;
    private double amount;
    private String content;
    private Timestamp createdAt;
    
    // Constructor
    public Transaction() {}
    
    public Transaction(int transactionId, int orderId, String transactionVnpayId, double amount, String content,
			Timestamp createdAt) {
		super();
		this.transactionId = transactionId;
		this.orderId = orderId;
		this.transactionVnpayId = transactionVnpayId;
		this.amount = amount;
		this.content = content;
		this.createdAt = createdAt;
	}


	// Getters and Setters
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getTransactionVnpayId() {
		return transactionVnpayId;
	}
	public void setTransactionVnpayId(String transactionVnpayId) {
		this.transactionVnpayId = transactionVnpayId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
    
}
