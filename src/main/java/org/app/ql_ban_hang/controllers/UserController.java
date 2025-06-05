package org.app.ql_ban_hang.controllers;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.entities.Cart;
import org.app.ql_ban_hang.entities.Category;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.services.CartService;
import org.app.ql_ban_hang.services.ProductService;
import org.app.ql_ban_hang.services.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "UserController", urlPatterns = {"/home", "/products", "/product/*", "/cart", "/checkout", "/orders", "/profile"})
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
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (requestURI.equals("/cart")) {
            if (!checkUserLogin(req, resp)) {
                return;
            }
            showCartPage(req, resp);
        } else if (requestURI.equals("/checkout")) {
            if (!checkUserLogin(req, resp)) {
                return;
            }
            showCheckoutPage(req, resp);
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
        List<Product> products = ProductService.getProducts();
        List<Category> categories = ProductService.getCategories();
        if (products == null) {
            products = Collections.emptyList();
            System.err.println("Warning: ProductService.getProducts() returned null. Initializing with empty list.");
        }
        if (categories == null) {
            categories = Collections.emptyList();
            System.err.println("Warning: ProductService.getCategories() returned null. Initializing with empty list.");
        }
        req.setAttribute("productList", products);
        req.setAttribute("categories", categories);

        renderView("/views/users/home.jsp", req, resp);
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
        List<Cart> cartItems = CartService.getCartItemsForCurrentUser(req, 1);
        for (int i = 0; i < cartItems.size(); i++) {
            Cart item = cartItems.get(i);
            System.out.println("--- Thông tin CartItem #" + (i + 1) + " ---");
            System.out.println("Cart ID: " + item.getId());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Status: " + item.getStatus());
            System.out.println("Price Total (Cart Item): " + item.getPriceTotal());
        }

            req.setAttribute("cartItems", cartItems);
        renderView("/views/users/cart.jsp", req, resp);
    }

    private void showCheckoutPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView("/views/users/checkout.jsp", req, resp);
    }

    private void showUserOrdersPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("idUserLogin");
        renderView("/views/users/orders_history.jsp", req, resp);
    }

    private void showProfilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("idUserLogin");
        renderView("/views/users/profile.jsp", req, resp);
    }

    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        String quantity = req.getParameter("quantity");
        resp.sendRedirect(req.getContextPath() + "/cart?added=" + productId);
    }

    private void updateCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        String newQuantity = req.getParameter("quantity");
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void processCheckout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean success = true;
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/order/success");
        } else {
            resp.sendRedirect(req.getContextPath() + "/checkout?error=true");
        }
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/profile?updated=true");
    }
}