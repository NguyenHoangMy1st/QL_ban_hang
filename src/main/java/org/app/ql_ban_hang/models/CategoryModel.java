package org.app.ql_ban_hang.models;

import org.app.ql_ban_hang.entities.Category;

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

    public static Category getCategoryById(int id) throws SQLException {
        Category category = null;
        String sql = "SELECT * FROM categories WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Kiểm tra xem có dòng dữ liệu nào trả về không
                int categoryId = resultSet.getInt("id");
                String categoryName = resultSet.getString("name");
                category = new Category(categoryId, categoryName);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    public static void create(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, category.getName());
        preparedStatement.executeUpdate();
    }
    public static void update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, category.getName());
        preparedStatement.setInt(2, category.getId());
        preparedStatement.executeUpdate();
    }
}
