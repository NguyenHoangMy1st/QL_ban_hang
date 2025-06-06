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


    public static List<Product> getProductsPaged(int limit, int offset) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.price, p.description, p.image, p.quantity, " +
                "c.id AS category_id, c.name AS category_name " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "LIMIT ? OFFSET ?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseModel.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setDescription(resultSet.getString("description"));
                product.setImage(resultSet.getString("image"));
                product.setQuantity(resultSet.getInt("quantity"));

                int categoryId = resultSet.getInt("category_id");
                if (categoryId != 0) {
                    Category category = new Category();
                    category.setId(categoryId);
                    category.setName(resultSet.getString("category_name"));
                    product.setCategory(category);
                }
                products.add(product);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }
        return products;
    }

    public static int getTotalProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            conn = DatabaseModel.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }
        return count;
    }

    public static List<Product> getProductsPagedAndFiltered(int limit, int offset, Integer categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, p.name, p.price, p.description, p.image, p.quantity, ");
        sql.append("c.id AS category_id, c.name AS category_name ");
        sql.append("FROM products p ");
        sql.append("LEFT JOIN categories c ON p.category_id = c.id ");

        if (categoryId != null && categoryId != 0) {
            sql.append("WHERE p.category_id = ? ");
        }
        sql.append("ORDER BY p.id DESC LIMIT ? OFFSET ?");

        try (Connection conn = DatabaseModel.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (categoryId != null && categoryId != 0) {
                preparedStatement.setInt(paramIndex++, categoryId);
            }
            preparedStatement.setInt(paramIndex++, limit);
            preparedStatement.setInt(paramIndex, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDescription(resultSet.getString("description"));
                    product.setImage(resultSet.getString("image"));
                    product.setQuantity(resultSet.getInt("quantity"));

                    int catId = resultSet.getInt("category_id");
                    if (!resultSet.wasNull() && catId != 0) {
                        Category category = new Category();
                        category.setId(catId);
                        category.setName(resultSet.getString("category_name"));
                        product.setCategory(category);
                    }
                    products.add(product);
                }
            }
        }
        return products;
    }

    public static int getTotalProductCountFiltered(Integer categoryId) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products ");
        if (categoryId != null && categoryId != 0) {
            sql.append("WHERE category_id = ?");
        }

        try (Connection conn = DatabaseModel.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {

            if (categoryId != null && categoryId != 0) {
                preparedStatement.setInt(1, categoryId);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
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


    public static List<Product> searchProducts(String searchTerm, int limit, int offset) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, p.name, p.price, p.description, p.image, p.quantity, ");
        sql.append("c.id AS category_id, c.name AS category_name ");
        sql.append("FROM products p ");
        sql.append("LEFT JOIN categories c ON p.category_id = c.id ");
        sql.append("WHERE LOWER(p.name) LIKE LOWER(?) OR LOWER(p.description) LIKE LOWER(?) ");
        sql.append("ORDER BY p.id DESC LIMIT ? OFFSET ?");

        try (Connection conn = DatabaseModel.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {

            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setInt(3, limit);
            preparedStatement.setInt(4, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDescription(resultSet.getString("description"));
                    product.setImage(resultSet.getString("image"));
                    product.setQuantity(resultSet.getInt("quantity"));

                    int categoryId = resultSet.getInt("category_id");
                    if (!resultSet.wasNull() && categoryId != 0) {
                        Category category = new Category();
                        category.setId(categoryId);
                        category.setName(resultSet.getString("category_name"));
                        product.setCategory(category);
                    } else {
                        product.setCategory(null);
                    }
                    products.add(product);
                }
            }
        }
        return products;
    }
    public static int getTotalProductCountBySearchTerm(String searchTerm) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)";
        try (Connection conn = DatabaseModel.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

}
