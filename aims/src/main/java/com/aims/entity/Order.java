package com.aims.entity;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private Integer userId;
    private String recipientName;
    private String email;
    private String phoneNumber;
    private String deliveryAddress;
    private String provinceCity;
    private double deliveryFee;
    private boolean rushDelivery;
    private LocalDateTime rushDeliveryTime;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public boolean isRushDelivery() {
        return rushDelivery;
    }

    public void setRushDelivery(boolean rushDelivery) {
        this.rushDelivery = rushDelivery;
    }

    public LocalDateTime getRushDeliveryTime() {
        return rushDeliveryTime;
    }

    public void setRushDeliveryTime(LocalDateTime rushDeliveryTime) {
        this.rushDeliveryTime = rushDeliveryTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}