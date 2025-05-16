package utils;

import java.sql.*;
import java.util.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/chatapp";
    private static final String USER = "root";
    private static final String PASSWORD = "Niharika23#";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
   }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveUser(String username, String avatar) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT IGNORE INTO users (username, avatar) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, avatar);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveMessage(String username, String message) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO messages (username, content) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> getRecentMessages(int limit) {
        List<String> messages = new ArrayList<>();
        String sql = "SELECT username, content, timestamp FROM messages ORDER BY timestamp DESC LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String user = rs.getString("username");
                String content = rs.getString("content");
                Timestamp time = rs.getTimestamp("timestamp");

                messages.add("[ " + time.toLocalDateTime().toLocalTime().withSecond(0) + " ] " + user + ": " + content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.reverse(messages); 
        return messages;
    }
    
    public static boolean registerUser(String username, String avatar, String password) {
        String hashed = PasswordUtil.hash(password);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO users (username, avatar, password_hash) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, avatar);
            stmt.setString(3, hashed);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;  
        }
    }
    
    public static boolean validateLogin(String username, String password) {
        String hashed = PasswordUtil.hash(password);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM users WHERE username = ? AND password_hash = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
