package com.aims.model;

import com.aims.entity.Order;
import com.aims.entity.OrderItem;
import java.util.List;

public class ShippingFeeCalculator {
    public static double calculateShippingFee(Order order, List<OrderItem> items) {
        double totalWeight = 0;
        for (OrderItem item : items) {
            totalWeight += item.getProduct().getWeight() * item.getQuantity();
        }
        
        boolean isHanoiOrHCM = order.getProvinceCity().equalsIgnoreCase("Hanoi") || 
                               order.getProvinceCity().equalsIgnoreCase("Ha Noi") ||
                               order.getProvinceCity().equalsIgnoreCase("Hà Nội") ||
                               order.getProvinceCity().equalsIgnoreCase("Ho Chi Minh City") ||
                               order.getProvinceCity().equalsIgnoreCase("Ho Chi Minh") ||
                               order.getProvinceCity().equalsIgnoreCase("Hồ Chí Minh");
        double fee = 0;
        
        if (isHanoiOrHCM) {
            fee = totalWeight <= 3 ? 22000 : 22000 + Math.ceil((totalWeight - 3) / 0.5) * 2500;
        } else {
            fee = totalWeight <= 0.5 ? 30000 : 30000 + Math.ceil((totalWeight - 0.5) / 0.5) * 2500;
        }
        
        if (order.isRushDelivery()) {
            fee += items.size() * 10000; // Phí giao hàng nhanh
        }
        
        // Miễn phí giao hàng cho đơn trên 100,000 VND (trừ giao hàng nhanh)
        double totalProductPrice = items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        if (!order.isRushDelivery() && totalProductPrice > 100000) {
            fee = Math.max(0, fee - 25000);
        }
        
        return fee;
    }
}