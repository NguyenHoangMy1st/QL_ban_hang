package org.app.ql_ban_hang.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.entities.Cart;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.entities.User;
import org.app.ql_ban_hang.models.ProductModel;
import org.app.ql_ban_hang.models.UserModel;
import org.app.ql_ban_hang.models.CartModel;

import java.sql.SQLException;
import java.util.List;

public class CartService {
    public static final int CART_STATUS_IN_CART = 1;     // Sản phẩm đang trong giỏ hàng (chưa thanh toán)
    public static final int CART_STATUS_ORDERED = 0;     // Sản phẩm đã được đặt hàng / đã thanh toán (không còn trong giỏ hoạt động)

    private static Integer getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Integer) session.getAttribute("idUserLogin");
        }
        return null;
    }
    public static List<Cart> getCartItemsForCurrentUser(HttpServletRequest request, int status) {
        Integer userId = getUserIdFromSession(request);
        if (userId == null) {
            System.out.println("User not logged in. Cannot retrieve cart items.");
            return null;
        }
        try {
            return CartModel.getCartItemsByUserIdAndStatus(userId, status);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving cart items for user " + userId + ": " + e.getMessage());
            return null;
        }
    }
    private static Cart getExistingCartItem(int userId, int productId, int status) throws SQLException {
        List<Cart> userCart = CartModel.getCartItemsByUserIdAndStatus(userId, status);
        for (Cart item : userCart) {
            if (item.getProduct() != null && item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }
    public static boolean addProductToCart(HttpServletRequest request, int productId, int quantity) {
        Integer userId = getUserIdFromSession(request);
        if (userId == null) {
            System.out.println("User not logged in. Cannot add product to cart.");
            return false;
        }

        try {
            Product product = ProductModel.getProductById(productId);
            User user = UserModel.getUserById(userId);

            if (product == null || user == null) {
                System.out.println("Product or User not found for adding to cart.");
                return false;
            }
            if (quantity > product.getQuantity()) {
                System.out.println("Requested quantity (" + quantity + ") exceeds product stock (" + product.getQuantity() + ") for product ID " + productId);
                return false;
            }
            double priceTotal = product.getPrice() * quantity;
            Cart existingCartItem = getExistingCartItem(userId, productId, CART_STATUS_IN_CART);

            if (existingCartItem != null) {
                int newQuantity = existingCartItem.getQuantity() + quantity;
                double newPriceTotal = existingCartItem.getPriceTotal() + priceTotal;

                if (newQuantity > product.getQuantity()) {
                    System.out.println("Updated quantity (" + newQuantity + ") exceeds product stock (" + product.getQuantity() + ") for product ID " + productId);
                    return false;
                }
                CartModel.updateCartItemQuantityAndPrice(existingCartItem.getId(), newQuantity, newPriceTotal);
                System.out.println("Updated existing cart item for product ID " + productId + ". New quantity: " + newQuantity);
            } else {
                CartModel.addCart(quantity, CART_STATUS_IN_CART, priceTotal, product, user);
                System.out.println("Added new cart item for product ID " + productId + ". Quantity: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding product to cart for user " + userId + ": " + e.getMessage());
            return false;
        }
        return true;
    }
    public static boolean updateCartItem(HttpServletRequest request, int cartId, int newQuantity) {
        Integer userId = getUserIdFromSession(request);
        if (userId == null) {
            System.out.println("User not logged in. Cannot update cart item.");
            return false;
        }

        try {
            Cart existingCartItem = CartModel.getCartById(cartId);

            if (existingCartItem == null) {
                System.out.println("Cart item with ID " + cartId + " not found.");
                return false;
            }

            if (existingCartItem.getUser() == null || existingCartItem.getUser().getId() != userId) {
                System.out.println("Attempt to update cart item " + cartId + " by unauthorized user " + userId);
                return false;
            }

            Product product = existingCartItem.getProduct();
            if (product == null) {
                System.out.println("Product associated with cart item " + cartId + " is null.");
                return false;
            }

            if (newQuantity > product.getQuantity()) {
                System.out.println("Requested new quantity (" + newQuantity + ") exceeds product stock (" + product.getQuantity() + ") for product ID " + product.getId());
                return false;
            }

            double newPriceTotal = product.getPrice() * newQuantity;

            CartModel.updateCartItemQuantityAndPrice(cartId, newQuantity, newPriceTotal);
            System.out.println("Updated cart item " + cartId + ". New quantity: " + newQuantity + ", New price total: " + newPriceTotal);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating cart item " + cartId + " for user " + userId + ": " + e.getMessage());
            return false;
        }
    }
    public static boolean deleteCartItem(HttpServletRequest request, int cartId) {
        Integer userId = getUserIdFromSession(request);
        if (userId == null) {
            System.out.println("User not logged in. Cannot delete cart item.");
            return false;
        }
        try {
            Cart cartToDelete = CartModel.getCartById(cartId);
            if (cartToDelete == null) {
                System.out.println("Cart item with ID " + cartId + " not found for deletion.");
                return false;
            }
            if (cartToDelete.getUser() == null || cartToDelete.getUser().getId() != userId) {
                System.out.println("Unauthorized attempt to delete cart item " + cartId + " by user " + userId);
                return false;
            }

            CartModel.deleteCartItem(cartId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting cart item " + cartId + " for user " + userId + ": " + e.getMessage());
            return false;
        }
    }
}
