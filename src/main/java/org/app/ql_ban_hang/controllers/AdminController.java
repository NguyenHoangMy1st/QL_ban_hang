package org.app.ql_ban_hang.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.app.ql_ban_hang.entities.Category;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.models.CategoryModel;
import org.app.ql_ban_hang.services.AdminService;
import org.app.ql_ban_hang.services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminController", urlPatterns = {"/admin/*"})
public class AdminController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "";
        }

        switch (path) {
            case "/dashboard":
                System.out.println("Admin dashboard");
                showDashboard(req, resp);
                break;
            case "/products":
                showProducts(req, resp);
                break;
            case "/products/edit":
                showFormEditProduct(req, resp);
                break;
            case "/products/create":
                showFormCreateProduct(req, resp);
                break;
            case "/products/delete":
                ProductService.deleteProduct(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            path = "";
        }

        switch (path) {
            case "/products/create":
                ProductService.createProduct(req, resp);
                break;
            case "/products/edit":
//                ProductService.updateProduct(req, resp);
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
    private void showProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> productList = ProductService.getProducts(req, resp);
        req.setAttribute("productList", productList);
        System.out.println(productList);
        List<Category> categories = ProductService.getCategories(req, resp);
        req.setAttribute("categories", categories);
        renderView("/views/admin/product/list.jsp", req, resp);
    }
    private void showFormCreateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView("/views/admin/product/create.jsp", req, resp);
    }
    private void showFormEditProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView("/views/admin/product/edit.jsp", req, resp);
    }
}
