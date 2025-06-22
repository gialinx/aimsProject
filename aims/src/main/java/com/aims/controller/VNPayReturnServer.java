package com.aims.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.sun.net.httpserver.HttpServer;

public class VNPayReturnServer {

	public static void startVNPayReturnServer(String expectedOrderId, double expectedAmount, Runnable onSuccess) throws IOException {
	    int port = 8080;
	    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

	    server.createContext("/vnpay_return", exchange -> {
	        String query = exchange.getRequestURI().getQuery();
	        Map<String, String> params = parseQuery(query);

	        String responseCode = params.get("vnp_ResponseCode");
	        String transactionStatus = params.get("vnp_TransactionStatus");
	        String vnpTxnRef = params.get("vnp_TxnRef");

	        String response;
	        if ("00".equals(responseCode) && "00".equals(transactionStatus) && expectedOrderId.equals(vnpTxnRef)) {
	            response = "Thanh toán thành công! Bạn có thể đóng cửa sổ này.";
	            onSuccess.run();
	        } else {
	            response = "Thanh toán thất bại hoặc bị hủy.";
	            
	        }

	        exchange.sendResponseHeaders(200, response.getBytes().length);
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	        server.stop(1);
	    });

	    new Thread(server::start).start();
	}
	
	
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query != null) {
            for (String pair : query.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) map.put(parts[0], parts[1]);
            }
        }
        return map;
    }

    
    public static void completePurchase(String orderId) {
        System.out.println("✅ Giao dịch thành công. Hoàn tất đơn hàng: " + orderId);
        // Gọi code xử lý đơn hàng tại đây
    }
}
