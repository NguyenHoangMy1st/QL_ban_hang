package org.app.ql_ban_hang.models;

import org.app.ql_ban_hang.entities.User;

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
    public static boolean checkEmailExists(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1)> 0;
        }
        return false;
    }
    public static void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password, email, phone, address, role) VALUES (?, ?, ?, ? , ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPhone());
        preparedStatement.setString(5, user.getAddress());
        preparedStatement.setString(6, "user");
        preparedStatement.executeUpdate();
    }

    public static void destroy(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
    public static ResultSet getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users WHERE role = 'user'";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }
    public static void create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password, email, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPhone());
        preparedStatement.setString(5, user.getAddress());
        preparedStatement.setString(6, user.getRole());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public static void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, phone = ?, address = ?, role = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPhone());
        preparedStatement.setString(4, user.getAddress());
        preparedStatement.setString(5, user.getRole());
        preparedStatement.setInt(6, user.getId());
        preparedStatement.executeUpdate();
    }
    public static ResultSet findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }
    public static User getUserById(int id) throws SQLException {
        User user = null;
        String sql = "SELECT id, name, email, phone, address, role FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseModel.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return user;
    }
}
