package org.app.ql_ban_hang.models;

import org.app.ql_ban_hang.entities.Category;
import org.app.ql_ban_hang.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {
    private static Connection conn = DatabaseModel.getConnection();

    public static ResultSet getAllProduct() throws SQLException {
        String sql = "SELECT products.*, categories.name as 'category_name'\n" +
                "FROM products\n" +
                "left join categories\n" +
                "ON products.category_id = categories.id";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public static void destroy(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
    public static void create(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, price, description,image, quantity, category_id) VALUES (?,?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setDouble(2, product.getPrice());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setString(4, product.getImage());
        preparedStatement.setInt(5, product.getQuantity());
        if (product.getCategory() != null) {
            preparedStatement.setInt(6, product.getCategory().getId());
        }else{
            preparedStatement.setNull(6, java.sql.Types.INTEGER);
        }
        preparedStatement.executeUpdate();
    }

    public static Product getProductById(int id) throws SQLException {
        Product product = null;
        String sql = "SELECT p.*, c.name as category_name " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "WHERE p.id = ?";
        try (Connection conn = DatabaseModel.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = null;
                    int categoryId = resultSet.getInt("category_id");
                    if (!resultSet.wasNull()) { // Kiểm tra nếu category_id không phải NULL
                        String categoryName = resultSet.getString("category_name");
                        category = new Category(categoryId, categoryName);
                    }
                    product = new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getString("description"),
                            resultSet.getString("image"),
                            category,
                            resultSet.getInt("quantity")
                    );
                }
            }
        }
        return product;
    }

    public static void update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, price = ?, description = ?, image = ?,quantity = ?, category_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setDouble(2, product.getPrice());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setString(4, product.getImage());
        preparedStatement.setInt(5, product.getQuantity());
        if (product.getCategory() != null) {
            preparedStatement.setInt(6, product.getCategory().getId());
        } else {
            preparedStatement.setNull(6, java.sql.Types.INTEGER);
        }
        preparedStatement.setInt(7, product.getId());
        preparedStatement.executeUpdate();
    }


    public static Product findProductByName(String name) throws SQLException {
        Product product = null;
        String sql = "SELECT p.*, c.name as category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.name = ?";
        try (Connection conn = DatabaseModel.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = null;
                    int categoryId = resultSet.getInt("category_id");
                    if (!resultSet.wasNull()) {
                        String categoryName = resultSet.getString("category_name");
                        category = new Category(categoryId, categoryName);
                    }
                    product = new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getString("description"),
                            resultSet.getString("image"),
                            category,
                            resultSet.getInt("quantity")
                    );
                }
            }
        }
        return product;
    }
    public static List<Product> getProductsByCategory(int categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT products.*, categories.name as 'category_name' " +
                "FROM products " +
                "LEFT JOIN categories ON products.category_id = categories.id " +
                "WHERE products.category_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Category category = null;
                    if (rs.getObject("category_id") != null) {
                        category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                    }
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("description"),
                            rs.getString("image"),
                            category,
                            rs.getInt("quantity")
                    );
                    products.add(product);
                }
            }
        }
        return products;
    }

}
