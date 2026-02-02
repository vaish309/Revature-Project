package main.java.com.revhire.app.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConfig {

    private static final String URL =
            "jdbc:mysql://localhost:3306/revhire?useSSL=false&serverTimezone=UTC";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed");
        }
    }
}
