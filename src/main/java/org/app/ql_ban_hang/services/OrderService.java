package org.app.ql_ban_hang.services;

import org.app.ql_ban_hang.entities.Cart;
import org.app.ql_ban_hang.entities.Order;
import org.app.ql_ban_hang.entities.OrderItem;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.models.OrderModel;
import org.app.ql_ban_hang.models.ProductModel;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class OrderService {
    public static boolean createOrderFromCart(Order orderHeader, List<Cart> cartItems) throws SQLException {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new SQLException("empty_cart_checkout");
        }

        for (Cart item : cartItems) {
            Product productInDb = ProductModel.getProductById(item.getProduct().getId());
            if (productInDb == null) {
                throw new SQLException("cart_item_not_found");
            }
            if (item.getQuantity() > productInDb.getQuantity()) {
                throw new SQLException("stock_exceeded");
            }
        }

        return OrderModel.createOrderWithItems(orderHeader, cartItems);
    }
    public static List<Order> getUserOrders(int userId) {
        try {
            return OrderModel.getOrdersByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in OrderService.getUserOrders: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
