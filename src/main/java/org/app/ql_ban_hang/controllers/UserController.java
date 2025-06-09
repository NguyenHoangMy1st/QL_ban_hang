package org.app.ql_ban_hang.controllers;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.entities.*;
import org.app.ql_ban_hang.services.CartService;
import org.app.ql_ban_hang.services.ProductService;
import org.app.ql_ban_hang.services.UserService;
import org.app.ql_ban_hang.services.OrderService;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "UserController", urlPatterns = {"/home", "/products/*", "/product/*", "/cart/*", "/checkout", "/orders", "/profile", "/buy"})
public class UserController extends BaseController {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    private boolean checkUserLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("idUserLogin") == null) {
            addNoCacheHeaders(resp);
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNoCacheHeaders(resp);

        String requestURI = req.getRequestURI().substring(req.getContextPath().length());

        System.out.println("UserController doGet - Request URI: " + requestURI);

        if (requestURI.startsWith("/product/")) {
            handleProductDetails(req, resp);
        } else if (requestURI.equals("/home")) {
            showHomePage(req, resp);
        } else if (requestURI.equals("/products")) {
            showAllProductsPage(req, resp);
        } else if(requestURI.equals("/products/search")){
            handleProductSearch(req, resp);
        } else if (requestURI.equals("/cart")) {
            if (!checkUserLogin(req, resp)) {
                return;
            }
            showCartPage(req, resp);
        } else if (requestURI.equals("/checkout")) {
            if (!checkUserLogin(req, resp)) {
                return;
            }
            showCartPage(req, resp);
        } else if (requestURI.equals("/orders")) {
            if (!checkUserLogin(req, resp)) {
                return;
            }
            showUserOrdersPage(req, resp);
        } else if (requestURI.equals("/profile")) {
            if (!checkUserLogin(req, resp)) {
                return;
            }
            showProfilePage(req, resp);
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNoCacheHeaders(resp);

        String requestURI = req.getRequestURI().substring(req.getContextPath().length());

        System.out.println("UserController doPost - Request URI: " + requestURI);

        switch (requestURI) {
            case "/cart/add":
                if (!checkUserLogin(req, resp)) {
                    return;
                }
                addToCart(req, resp);
                break;
            case "/cart/update":
                if (!checkUserLogin(req, resp)) {
                    return;
                }
                updateCart(req, resp);
                break;
            case "/cart/remove":
                if (!checkUserLogin(req, resp)) {
                    return;
                }
                removeFromCart(req, resp);
                break;
            case "/checkout":
                if (!checkUserLogin(req, resp)) {
                    return;
                }
                processCheckout(req, resp);
                break;
            case "/profile/update":
                if (!checkUserLogin(req, resp)) {
                    return;
                }
                updateProfile(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private void showHomePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid page parameter: " + pageParam);
                currentPage = 1;
            }
        }

        List<Product> products = ProductService.getProductsPaged(currentPage);
        int totalPages = ProductService.getTotalPages();

        List<Category> categories = ProductService.getCategories();

        if (products == null) {
            products = Collections.emptyList();
            System.err.println("Warning: ProductService.getProductsPaged() returned null. Initializing with empty list.");
        }
        if (categories == null) { // Tương tự
            categories = Collections.emptyList();
            System.err.println("Warning: ProductService.getCategories() returned null. Initializing with empty list.");
        }

        req.setAttribute("productList", products);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", currentPage); // Truyền trang hiện tại
        req.setAttribute("totalPages", totalPages);   // Truyền tổng số trang

        renderView("/views/users/home.jsp", req, resp);
    }
    private void showAllProductsPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid page parameter: " + pageParam);
                currentPage = 1;
            }
        }
        Integer selectedCategoryId = null; // Mặc định là không lọc
        String categoryParam = req.getParameter("category");
        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                selectedCategoryId = Integer.parseInt(categoryParam);
                // Nếu categoryId là 0 hoặc âm, coi như không lọc
                if (selectedCategoryId <= 0) {
                    selectedCategoryId = null;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid category parameter: " + categoryParam);
                selectedCategoryId = null; // Không lọc nếu tham số không hợp lệ
            }
        }
        List<Product> products = ProductService.getProductsPagedAndFiltered(currentPage, selectedCategoryId);
        int totalPages = ProductService.getTotalPagesFiltered(selectedCategoryId); // Lấy tổng số trang có lọc

        List<Category> categories = ProductService.getCategories();

        if (products == null) {
            products = Collections.emptyList();
        }
        if (categories == null) {
            categories = Collections.emptyList();
        }

        req.setAttribute("productList", products);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);

        renderView("/views/users/products.jsp", req, resp); // Giả sử bạn có products.jsp
    }
    private void handleProductSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchTerm = req.getParameter("name");

        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid page parameter for search: " + pageParam);
                currentPage = 1;
            }
        }

        List<Product> products = Collections.emptyList();
        int totalPages = 1;
        String errorMessage = null;
        String successMessage = null;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            errorMessage = "Vui lòng nhập tên sản phẩm để tìm kiếm.";
        } else {
            products = ProductService.searchProducts(searchTerm, currentPage);
            totalPages = ProductService.getTotalSearchPages(searchTerm);

            if (products.isEmpty()) {
                errorMessage = "Không tìm thấy sản phẩm nào khớp với: '" + searchTerm + "'.";
            } else {
                successMessage = "Kết quả tìm kiếm cho: '" + searchTerm + "'.";
            }
        }

        List<Category> categories = ProductService.getCategories();
        if (categories == null) {
            categories = Collections.emptyList();
        }

        req.setAttribute("productList", products);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("searchTerm", searchTerm);
        req.setAttribute("errorMessage", errorMessage);
        req.setAttribute("successMessage", successMessage);

        renderView("/views/users/products.jsp", req, resp);
    }
    private void handleProductDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdParam = req.getParameter("id");
        if (productIdParam == null || productIdParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không được để trống.");
            return;
        }

        try {
            Product product = ProductService.getProductById(req);
            if (product != null) {
                req.setAttribute("product", product);
                renderView("/views/users/product_detail.jsp", req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không tìm thấy.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ.");
        }
    }

    private void showCartPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userIdObj = (session != null) ? (Integer) session.getAttribute("idUserLogin") : null;
        if (userIdObj == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login"); // Đã có checkUserLogin ở doGet
            return;
        }

        try {
            int userId = userIdObj;
            List<Cart> cartItems = CartService.getCartItemsForCurrentUser(req, 1);

            User user = UserService.getUserById(userId);

            if (cartItems == null) {
                cartItems = Collections.emptyList();
                System.err.println("Warning: CartService.getCartItemsForCurrentUser() returned null. Initializing with empty list.");
            }
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("user", user);
            renderView("/views/users/cart.jsp", req, resp);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing userId from session: " + userIdObj);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy xuất thông tin người dùng.");
        }
    }



    private void showUserOrdersPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userIdObj = (session != null) ? (Integer) session.getAttribute("idUserLogin") : null;

        if (userIdObj == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        try {
            int userId = userIdObj.intValue();
            List<Order> userOrders = OrderService.getUserOrders(userId);

            req.setAttribute("userOrders", userOrders);
            renderView("/views/users/orders_history.jsp", req, resp);
        } catch (ClassCastException e) {
            System.err.println("Error: idUserLogin in session is not an Integer. " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi định dạng ID người dùng trong phiên.");
        }
    }

    private void showProfilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("idUserLogin");
        renderView("/views/users/profile.jsp", req, resp);
    }

    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdParam = req.getParameter("productId");
        String quantityParam = req.getParameter("quantity");
        int productId = 0;
        int quantity = 0;
        String redirectUrl = req.getContextPath() + "/cart";

        try {
            productId = Integer.parseInt(productIdParam);
            quantity = Integer.parseInt(quantityParam);

            if (quantity <= 0) {
                req.setAttribute("errorMessage", "Số lượng sản phẩm phải lớn hơn 0.");
                resp.sendRedirect(req.getContextPath() + "/product/detail?id=" + productId + "&error=invalid_quantity");
                return;
            }

            boolean success = CartService.addProductToCart(req, productId, quantity);

            if (success) {
                redirectUrl += "?success=added";
                resp.sendRedirect(redirectUrl);
            } else {
                req.setAttribute("errorMessage", "Không thể thêm sản phẩm vào giỏ hàng. Vui lòng thử lại.");
                resp.sendRedirect(req.getContextPath() + "/product/detail?id=" + productId + "&error=cart_add_failed");
            }

        } catch (NumberFormatException e) {
            System.err.println("Lỗi: productId hoặc quantity không hợp lệ: " + productIdParam + ", " + quantityParam);
            req.setAttribute("errorMessage", "Dữ liệu sản phẩm hoặc số lượng không hợp lệ.");
            resp.sendRedirect(req.getContextPath() + "/home?error=invalid_input");
        }
    }

    private void updateCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cartIdParam = req.getParameter("cartId");
        String newQuantityParam = req.getParameter("quantity");

        int cartId = 0;
        int newQuantity = 0;
        String redirectUrl = req.getContextPath() + "/cart";

        try {
            cartId = Integer.parseInt(cartIdParam);
            newQuantity = Integer.parseInt(newQuantityParam);

            if (newQuantity <= 0) {
                resp.sendRedirect(redirectUrl + "?error=invalid_quantity");
                return;
            }

            boolean success = CartService.updateCartItem(req, cartId, newQuantity);

            if (success) {
                redirectUrl += "?success=updated";
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendRedirect(redirectUrl + "?error=update_failed");
            }

        } catch (NumberFormatException e) {
            System.err.println("Lỗi: cartId hoặc newQuantity không hợp lệ: " + cartIdParam + ", " + newQuantityParam);
            resp.sendRedirect(redirectUrl + "?error=invalid_input");
        }
    }

    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cartIdParam = req.getParameter("cartId");

        int cartId = 0;
        String redirectUrl = req.getContextPath() + "/cart";

        try {
            cartId = Integer.parseInt(cartIdParam);

            boolean success = CartService.deleteCartItem(req, cartId);

            if (success) {
                redirectUrl += "?success=removed";
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendRedirect(redirectUrl + "?error=remove_failed");
            }

        } catch (NumberFormatException e) {
            System.err.println("Lỗi: cartId không hợp lệ: " + cartIdParam);
            resp.sendRedirect(redirectUrl + "?error=invalid_input");
        }
    }

    private void processCheckout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userIdObj = (session != null) ? (Integer) session.getAttribute("idUserLogin") : null;

        if (userIdObj == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + URLEncoder.encode(req.getRequestURI(), "UTF-8"));
            return;
        }

        try {
            int userId = userIdObj;

            String customerName = req.getParameter("name");
            String deliveryAddress = req.getParameter("address");
            String contactPhone = req.getParameter("phone");
            int paymentMethod = Integer.parseInt(req.getParameter("paymentMethod"));
            double totalMoney = Double.parseDouble(req.getParameter("totalMoney"));

            if (customerName == null || customerName.trim().isEmpty() ||
                    deliveryAddress == null || deliveryAddress.trim().isEmpty() ||
                    contactPhone == null || contactPhone.trim().isEmpty() || totalMoney <= 0) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=invalid_input");
                return;
            }

            List<Cart> cartItems = CartService.getCartItemsForCurrentUser(req, 1);

            if (cartItems.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=empty_cart_checkout");
                return;
            }

            User user = UserService.getUserById(userId);
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=user_not_found");
                return;
            }

            Order order = new Order(customerName, deliveryAddress, contactPhone, paymentMethod, 1, totalMoney, user);

            boolean orderSuccess = OrderService.createOrderFromCart(order, cartItems);

            if (orderSuccess) {
                CartService.clearCart(userId);
                resp.sendRedirect(req.getContextPath() + "/cart?success=order_placed");
            } else {
                resp.sendRedirect(req.getContextPath() + "/cart?error=order_failed");
            }

        } catch (NumberFormatException e) {
            System.err.println("Lỗi định dạng số khi đặt hàng: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart?error=invalid_input");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi SQL khi đặt hàng: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("stock_exceeded")) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=stock_exceeded");
            } else if (e.getMessage() != null && e.getMessage().contains("cart_item_not_found")) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=cart_item_not_found");
            } else {
                resp.sendRedirect(req.getContextPath() + "/cart?error=order_failed");
            }
        }
    }
    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/profile?updated=true");
    }
}