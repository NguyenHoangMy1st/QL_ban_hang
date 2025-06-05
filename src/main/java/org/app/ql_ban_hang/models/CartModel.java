package org.app.ql_ban_hang.models;

import org.app.ql_ban_hang.entities.Cart;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartModel {
    private static Connection conn = DatabaseModel.getConnection();
    private static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static ResultSet getAllCarts() throws SQLException, SQLException {
        String sql = "select * from carts";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public static List<Cart> getCartItemsByUserIdAndStatus(int userId, int status) throws SQLException {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT c.id, c.quantity, c.status, c.priceTotal, " +
                "p.id AS product_id, p.name AS product_name, p.price AS product_unit_price, p.description AS product_description, p.image AS product_image, p.quantity AS product_quantity_in_stock, " +
                "u.id AS user_id, u.name AS name " +
                "FROM carts c " +
                "JOIN products p ON c.product_id = p.id " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE c.user_id = ? AND c.status = ?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseModel.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, status);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("product_name"));
                product.setPrice(resultSet.getDouble("product_unit_price"));
                product.setDescription(resultSet.getString("product_description"));
                product.setImage(resultSet.getString("product_image"));
                product.setQuantity(resultSet.getInt("product_quantity_in_stock"));

                User user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setName(resultSet.getString("name"));

                Cart cartItem = new Cart(
                        resultSet.getInt("quantity"),
                        resultSet.getInt("status"),
                        resultSet.getDouble("priceTotal"), // priceTotal từ bảng carts
                        product,
                        user
                );
                cartItem.setId(resultSet.getInt("id"));
                cartItems.add(cartItem);
            }
        } finally {
            closeResources(conn, preparedStatement, resultSet);
        }
        return cartItems;
    }
    public static void addCart(int quantity, int status, double priceTotal ,Product product, User user) throws SQLException, SQLException {
        String sql = "INSERT INTO carts (quantity, status, priceTotal, product_id, user_id) values(?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, quantity);
        preparedStatement.setInt(2, 1); // <-- Luôn đặt status là 1 (inCart)
        preparedStatement.setDouble(3, priceTotal);
        preparedStatement.setInt(4, product.getId());
        preparedStatement.setInt(5, user.getId());
        preparedStatement.executeUpdate();
    }
    // Xóa một mặt hàng khỏi bảng carts theo ID của nó
    public static void deleteCartItem(int cartId) throws SQLException {
        String sql = "DELETE FROM carts WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, cartId);
            preparedStatement.executeUpdate();
        }
    }
    // Cập nhật số lượng và tổng giá của một mục giỏ hàng theo ID của nó trong bảng carts
    public static void updateCartItemQuantityAndPrice(int cartId, int newQuantity,  double newPriceTotal) throws SQLException, SQLException {
        String sql = "UPDATE carts SET quantity = ?, priceTotal = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, newQuantity);
        preparedStatement.setDouble(2, newPriceTotal);
        preparedStatement.setInt(3, cartId);
        preparedStatement.executeUpdate();
    }


}
