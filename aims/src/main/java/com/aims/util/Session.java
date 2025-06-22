package com.aims.util;

import com.aims.entity.User;
import java.util.UUID;

public class Session {
    private static User currentUser;
    private static String sessionId = UUID.randomUUID().toString(); // Khởi tạo ngẫu nhiên khi chạy chương trình

    public static void login(User user) {
        currentUser = user;
        if (user != null) {
            sessionId = user.getUserId()+""; // Gán session_id bằng username khi đăng nhập
        }
    }

    public static void logout() {
        currentUser = null;
        sessionId = UUID.randomUUID().toString(); // Tạo session_id ngẫu nhiên khi đăng xuất
;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public static String getSessionId() {
        return sessionId; // Không cần kiểm tra null vì luôn có giá trị
    }
}