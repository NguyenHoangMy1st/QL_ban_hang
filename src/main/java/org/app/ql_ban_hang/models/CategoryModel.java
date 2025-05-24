package org.app.ql_ban_hang.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryModel {
    private static Connection conn = DatabaseModel.getConnection();

    public static ResultSet getAllCategories() throws SQLException, SQLException {
        String sql = "SELECT * FROM categories";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public static ResultSet getCategoryById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }
}
