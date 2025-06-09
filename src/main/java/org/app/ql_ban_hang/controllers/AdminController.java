package org.app.ql_ban_hang.controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.entities.Category;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.entities.User;
import org.app.ql_ban_hang.models.CategoryModel;
import org.app.ql_ban_hang.services.AdminService;
import org.app.ql_ban_hang.services.ProductService;
import org.app.ql_ban_hang.services.UserService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "AdminController", urlPatterns = {"/admin/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AdminController extends BaseController {

    private boolean checkAdminLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("idUserLogin") == null || session.getAttribute("roleUserLogin") == null) {
            addNoCacheHeaders(resp); // Thêm header trước khi chuyển hướng
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return false;
        }

        String role = (String) session.getAttribute("roleUserLogin");
        if (!"admin".equals(role)) {
            addNoCacheHeaders(resp); // Thêm header trước khi chuyển hướng
            resp.sendRedirect(req.getContextPath() + "/home?error=access_denied");
            return false;
        }
        return true;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNoCacheHeaders(resp);
        if (!checkAdminLogin(req, resp)) {
            return;
        }
        String path = req.getPathInfo();
        if (path == null) {
            path = "";
        }

        switch (path) {
            case "/dashboard":
                req.setAttribute("currentPage", "dashboard");
                showDashboard(req, resp);
                break;
            case "/products":
                req.setAttribute("currentPage", "products");
                showAllProductsPage(req, resp);
                break;
            case "/products/detail":
                showDetailProduct(req, resp);
                break;
            case "/products/edit":
                req.setAttribute("currentPage", "products");
                showFormEditProduct(req, resp);
                break;
            case "/products/create":
                req.setAttribute("currentPage", "products");
                showFormCreateProduct(req, resp);
                break;
            case "/products/delete":
                ProductService.deleteProduct(req, resp);
                break;
            case "/users":
                req.setAttribute("currentPage", "users");
                showUsers(req, resp);
                break;
            case "/users/edit":
                req.setAttribute("currentPage", "users");
                showFormEditUser(req, resp);
                break;
            case "/users/create":
                req.setAttribute("currentPage", "users");
                showFormCreateUser(req, resp);
                break;
            case "/users/delete":
//                ProductService.deleteUser(req, resp);
                break;
            case "/categories":
                req.setAttribute("currentPage", "categories");
                showCategory(req, resp);
                break;
            case "/categories/edit":
                req.setAttribute("currentPage", "categories");
                showFormEditCategory(req, resp);
                break;
            case "/categories/create":
                req.setAttribute("currentPage", "categories");
                showFormCreateCategory(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNoCacheHeaders(resp);

        if (!checkAdminLogin(req, resp)) {
            return;
        }
        String path = req.getPathInfo();
        if (path == null) {
            path = "";
        }

        switch (path) {
            case "/products/create":
                ProductService.createProduct(req, resp);
                break;
            case "/products/edit":
                ProductService.updateProduct(req, resp);
                break;
//            case "/products/search":
//                handleProductSearch(req, resp);
//                break;
            case "/users/create":
                UserService.createUser(req, resp);
                break;
            case "/users/edit":
                UserService.updateUser(req, resp);
                break;
            case "/categories/create":
                ProductService.createCategory(req, resp);
                break;
            case "/categories/edit":
                ProductService.updateCategory(req, resp);
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

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView("/views/admin/dashboard.jsp", req, resp);
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

        List<Product> products = ProductService.getProductsPaged(currentPage);
        int totalPages = ProductService.getTotalPages();

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

        renderView("/views/admin/product/list.jsp", req, resp);
    }

    private void showDetailProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Product product = ProductService.getProductById(req);

            Gson gson = new Gson();
            String jsonOutput;

            if (product != null) {
                jsonOutput = gson.toJson(product);
                resp.getWriter().write(jsonOutput); // Ghi JSON vào response
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{ \"error\": \"Sản phẩm không tìm thấy.\" }");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write("{ \"error\": \"ID sản phẩm không hợp lệ.\" }");
            e.printStackTrace();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write("{ \"error\": \"Lỗi máy chủ khi lấy chi tiết sản phẩm: " + e.getMessage() + "\" }");
            e.printStackTrace();
        }
    }

    private void showFormCreateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = ProductService.getCategories();
        req.setAttribute("categories", categories);
        renderView("/views/admin/product/create.jsp", req, resp);
    }
    private void showFormEditProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = ProductService.getCategories();
        Product currentProduct = ProductService.getProductById(req);
        req.setAttribute("product", currentProduct);
        req.setAttribute("categories", categories);
        renderView("/views/admin/product/edit.jsp", req, resp);
    }
    private void showUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList = UserService.getUsers();
        req.setAttribute("users", userList);
        renderView("/views/admin/user/list.jsp", req, resp);
    }
    private void showFormCreateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView("/views/admin/user/create.jsp", req, resp);
    }
    private void showFormEditUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            req.setAttribute("errorMessage", "Không tìm thấy ID người dùng để chỉnh sửa.");
            renderView("/views/admin/user/list.jsp", req, resp);
            return;
        }
        try{
            int userId  = Integer.parseInt(idParam);
            User currentUser = UserService.getUserById(userId);
            if (currentUser != null) {
                req.setAttribute("user", currentUser);
                renderView("/views/admin/user/edit.jsp", req, resp);
            } else {
                req.setAttribute("errorMessage", "Không tìm thấy người dùng với ID: " + userId);
                renderView("/views/admin/user/list.jsp", req, resp);
            }
        }catch(Exception e){
            req.setAttribute("errorMessage", e.getMessage());
            renderView("/views/admin/user/list.jsp", req, resp);
        }
    }
    public void showCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = ProductService.getCategories();
        req.setAttribute("categories", categories);
        renderView("/views/admin/category/list.jsp", req, resp);
    }
    public void showFormCreateCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView("/views/admin/category/create.jsp", req, resp);
    }
    public void showFormEditCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category currentCategory = ProductService.getCategoryById(req, resp);
        req.setAttribute("category", currentCategory);
        renderView("/views/admin/category/edit.jsp", req, resp);
    }
}
