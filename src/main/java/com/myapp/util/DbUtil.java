package com.myapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private static final String JDBC_URL =
    		"jdbc:mysql://localhost:3306/crs_db?useSSL=false&serverTimezone=UTC";

    private static final String JDBC_USER = "crs_user";
    private static final String JDBC_PASSWORD = "crs12345";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                JDBC_URL,
                JDBC_USER,
                JDBC_PASSWORD
        );
    }
}
