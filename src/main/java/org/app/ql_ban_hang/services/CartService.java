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

            // Tính toán priceTotal cho mặt hàng này (đơn giá * số lượng)
            double priceTotal = product.getPrice() * quantity;

            // Gọi CartModel để thêm vào giỏ hàng
            CartModel.addCart(quantity, 1, priceTotal, product, user); // status 1: đang trong giỏ
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding product to cart for user " + userId + ": " + e.getMessage());
            return false;
        }
    }

    // Các phương thức khác (update, delete) cũng sẽ cần userId hoặc cartId
//    public static boolean updateCartItem(HttpServletRequest request, int cartId, int newQuantity) {
//        Integer userId = getUserIdFromSession(request); // Lấy userId để xác thực (tùy chọn)
//        if (userId == null) {
//            System.out.println("User not logged in. Cannot update cart item.");
//            return false;
//        }
//
//        try {
//            // Lấy thông tin giỏ hàng để có productId và đơn giá sản phẩm
//            // CartModel.getCartItemById(cartId) - bạn cần tạo phương thức này trong CartModel
//            Cart currentCartItem = CartModel.getCartItemById(cartId); // Giả định có phương thức này
//
//            if (currentCartItem == null || currentCartItem.getUser().getId() != userId) {
//                // Đảm bảo mục giỏ hàng thuộc về người dùng hiện tại
//                System.out.println("Cart item not found or does not belong to the current user.");
//                return false;
//            }
//
//            Product associatedProduct = currentCartItem.getProduct();
//            if (associatedProduct == null) {
//                System.err.println("Product associated with cart item " + cartId + " not found.");
//                return false;
//            }
//
//            double newPriceTotal = associatedProduct.getPrice() * newQuantity;
//            CartModel.updateCartItemQuantityAndPrice(cartId, newQuantity, newPriceTotal);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("Error updating cart item " + cartId + ": " + e.getMessage());
//            return false;
//        }
//    }
}
