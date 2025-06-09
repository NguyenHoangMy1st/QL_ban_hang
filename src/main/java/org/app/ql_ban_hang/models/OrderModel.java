package org.app.ql_ban_hang.models;

import org.app.ql_ban_hang.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    public static boolean createOrderWithItems(Order orderHeader, List<Cart> cartItems) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtOrderItem = null;
        PreparedStatement pstmtUpdateProduct = null;
        boolean success = false;

        try {
            conn = DatabaseModel.getConnection();
            conn.setAutoCommit(false);

            String sqlOrder = "INSERT INTO orders (user_id, customer_name, address, phone, total_money, payment_method, status, order_date) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
            pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setInt(1, orderHeader.getUser().getId());
            pstmtOrder.setString(2, orderHeader.getName());
            pstmtOrder.setString(3, orderHeader.getAddress());
            pstmtOrder.setString(4, orderHeader.getPhone());
            pstmtOrder.setDouble(5, orderHeader.getTotalMoney());
            pstmtOrder.setInt(6, orderHeader.getPaymentMethod());
            pstmtOrder.setInt(7, orderHeader.getStatus());
            pstmtOrder.executeUpdate();


            int orderId = -1;
            try (ResultSet rs = pstmtOrder.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }

            if (orderId == -1) {
                throw new SQLException("Failed to get generated order ID.");
            }
            orderHeader.setId(orderId);

            // Thêm từng OrderItem vào bảng `order_items` và cập nhật số lượng sản phẩm
            String sqlOrderItem = "INSERT INTO order_items (order_id, product_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";
            String sqlUpdateProduct = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

            pstmtOrderItem = conn.prepareStatement(sqlOrderItem);
            pstmtUpdateProduct = conn.prepareStatement(sqlUpdateProduct);

            for (Cart cartItem : cartItems) {
                Product product = cartItem.getProduct();

                pstmtOrderItem.setInt(1, orderId);
                pstmtOrderItem.setInt(2, product.getId());
                pstmtOrderItem.setInt(3, cartItem.getQuantity());
                pstmtOrderItem.setDouble(4, product.getPrice()); // Lưu giá tại thời điểm đặt hàng (SNAPSHOT)
                pstmtOrderItem.addBatch();

                pstmtUpdateProduct.setInt(1, cartItem.getQuantity());
                pstmtUpdateProduct.setInt(2, product.getId());
                pstmtUpdateProduct.setInt(3, cartItem.getQuantity());
                pstmtUpdateProduct.addBatch();
            }

            pstmtOrderItem.executeBatch();
            int[] updateCounts = pstmtUpdateProduct.executeBatch();

            for (int count : updateCounts) {
                if (count == 0) {
                    conn.rollback();
                    throw new SQLException("stock_exceeded");
                }
            }

            conn.commit();
            success = true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (pstmtOrder != null) pstmtOrder.close();
            if (pstmtOrderItem != null) pstmtOrderItem.close();
            if (pstmtUpdateProduct != null) pstmtUpdateProduct.close();
            if (conn != null) conn.close();
        }
        return success;
    }
    public static List<OrderItem> getOrderItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        String sql = "SELECT oi.id, oi.product_id, oi.quantity, oi.price_at_order, " + // Sử dụng price_at_order
                "p.name AS product_name, p.image AS product_image_url, p.description, p.category_id " + // Thêm các cột cần thiết cho Product entity
                "FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";

        try {
            conn = DatabaseModel.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double priceAtPurchase = rs.getDouble("price_at_order");

                String productName = rs.getString("product_name");
                String productImage = rs.getString("product_image_url");
                String productDescription = rs.getString("description");
                int productCategoryId = rs.getInt("category_id");

                Product productOrder = new Product(productId, productName, rs.getDouble("price_at_order"),
                        productDescription, productImage, productCategoryId);

                OrderItem item = new OrderItem(id, productOrder, quantity, priceAtPurchase);
                orderItems.add(item);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        return orderItems;
    }
    public static List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, user_id, customer_name, address, phone, total_money, payment_method, status, order_date FROM orders WHERE user_id = ? ORDER BY order_date DESC";

        try {
            conn = DatabaseModel.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");

                User tempUser = new User(userId);

                String customerName = rs.getString("customer_name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                double totalMoney = rs.getDouble("total_money");
                int paymentMethod = rs.getInt("payment_method");
                int status = rs.getInt("status");

                Timestamp orderDateTimestamp = rs.getTimestamp("order_date");
                java.util.Date orderDate = new java.util.Date(orderDateTimestamp.getTime());

                List<OrderItem> orderItems = OrderModel.getOrderItemsByOrderId(id);

                Order order = new Order(id, customerName, address, phone, paymentMethod, status, totalMoney, tempUser, orderItems);

                orders.add(order);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        return orders;
    }
}