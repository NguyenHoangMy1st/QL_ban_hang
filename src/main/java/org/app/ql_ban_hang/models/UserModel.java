package org.app.ql_ban_hang.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    private static Connection conn = DatabaseModel.getConnection();

    public static ResultSet getUserByEmailPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? and password = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        return preparedStatement.executeQuery();
    }

    public static void destroy(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
}
