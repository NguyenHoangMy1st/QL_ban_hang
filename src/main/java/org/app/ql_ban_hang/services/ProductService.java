package org.app.ql_ban_hang.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.app.ql_ban_hang.entities.Category;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.models.CategoryModel;
import org.app.ql_ban_hang.models.ProductModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public static List<Product> getProducts(HttpServletRequest request, HttpServletResponse response) {
        try{
            ResultSet resultSet = ProductModel.getAllProduct();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                int pID = resultSet.getInt("id");
                String pName = resultSet.getString("name");
                double pPrice = resultSet.getDouble("price");
                String pDescription = resultSet.getString("description");
                String pImage = resultSet.getString("image");
                Product product = new Product(pID, pName, pPrice, pDescription, pImage);
                product.setId(pID);

                int cID = resultSet.getInt("category_id");
                if (cID != 0) {
                    String categoryName = resultSet.getString("category_name");
                    Category category = new Category(cID, categoryName);
                    product.setCategory(category);
                }
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductModel.destroy(id);
            response.sendRedirect("/admin/products");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void createProduct(HttpServletRequest request, HttpServletResponse response) {
        try{
            String name = request.getParameter("name");
            Double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");
            String image = request.getParameter("image");
            int categoryId = Integer.parseInt(request.getParameter("category_id"));

            Product product = new Product(name, price, description, image);
            ResultSet resultSet = CategoryModel.getCategoryById(categoryId);
            if (resultSet.next()) {
                String pCategoryName = resultSet.getString("name");
                Category category = new Category(categoryId, pCategoryName);
                product.setCategory(category);
            }else {
                product.setCategory(null);
            }
            ProductModel.createProduct(product);
            response.sendRedirect("/admin/products");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Category> getCategories(HttpServletRequest request, HttpServletResponse response) {
        try{
            ResultSet resultSet = CategoryModel.getAllCategories();
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                int cID = resultSet.getInt("id");
                String cName = resultSet.getString("name");
                Category category = new Category(cID, cName);
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
