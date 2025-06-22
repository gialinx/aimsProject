package com.aims.entity;

public class OrderItem {
    private int orderItemId;
    private String title;

	private int orderId;
    private int productId;
    private int quantity;
    private double price;
    private Product product;
    private boolean isRush;
	
    // Constructor
    public OrderItem() {}

    public OrderItem(int orderItemId, int orderId, int productId, int quantity, double price) {
		super();
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
	
    // Getters and Setters
    public String getTitle() {
		return title;
	}
	public boolean isRush() {
		return isRush;
	}
	public void setRush(boolean isRush) {
		this.isRush = isRush;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
    
    
}
