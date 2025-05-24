package org.app.ql_ban_hang.models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseModel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ql_ban_hang";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
