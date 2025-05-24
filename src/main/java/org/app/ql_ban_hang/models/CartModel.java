package org.app.ql_ban_hang.models;

import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartModel {
    private static Connection conn = DatabaseModel.getConnection();

    public static ResultSet getAllCarts() throws SQLException, SQLException {
        String sql = "select * from carts";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public static ResultSet getCart(int id) throws SQLException, SQLException {
        String sql = "select * from carts where id=? AND status = 1";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }
    public static void addCart(int buy_count, int status, double price ,Product product, User user) throws SQLException, SQLException {
        String sql = "INSERT INTO carts (buy_count, status, price, product_id, user_id) values(?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, buy_count);
        preparedStatement.setInt(2, 1); // <-- Luôn đặt status là 1 (inCart)
        preparedStatement.setDouble(3, price);
        preparedStatement.setInt(4, product.getId());
        preparedStatement.setInt(5, user.getId());
    }
    public static void removeCart(int id) throws SQLException, SQLException {
        String sql = "DELETE FROM carts WHERE id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
    public static void updateCart(int id, int buy_count, int status, double price, Product product, User user) throws SQLException, SQLException {
        String sql = "UPDATE carts SET buy_count = ?, status = ?, price = ?, product_id = ?, user_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, buy_count);
        preparedStatement.setInt(3, status);
        preparedStatement.setDouble(4, price);
        preparedStatement.setInt(5, product.getId());
        preparedStatement.setInt(6, user.getId());
        preparedStatement.executeUpdate();
    }


}
