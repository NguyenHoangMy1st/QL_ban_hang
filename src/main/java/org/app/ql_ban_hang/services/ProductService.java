package org.app.ql_ban_hang.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
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
    public static List<Product> getProducts() {
        try{
            ResultSet resultSet = ProductModel.getAllProduct();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                int pID = resultSet.getInt("id");
                String pName = resultSet.getString("name");
                double pPrice = resultSet.getDouble("price");
                String pDescription = resultSet.getString("description");
//                String pImage = resultSet.getString("image");
                int pQuantity = resultSet.getInt("quantity");
                Product product = new Product(pID, pName, pPrice, pDescription, pQuantity);
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
            System.out.println("Error in getProducts");
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
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryId = Integer.parseInt(request.getParameter("category_id"));

            Product product = new Product(name, price, description, quantity);
            ResultSet resultSet = CategoryModel.getCategoryById(categoryId);
            if (resultSet.next()) {
                String pCategoryName = resultSet.getString("name");
                Category category = new Category(categoryId, pCategoryName);
                product.setCategory(category);
            }else {
                product.setCategory(null);
            }
            ProductModel.create(product);
            response.sendRedirect("/admin/products");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in createProduct");
        }
    }
    public static List<Category> getCategories() {
        try{
            ResultSet resultSet = CategoryModel.getAllCategories();
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                int cID = resultSet.getInt("id");
                String cName = resultSet.getString("name");
                Category category = new Category(cID, cName);
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Product getProductById(HttpServletRequest request, HttpServletResponse response) {
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            ResultSet resultSetProduct = ProductModel.getProductById(id);
            Product productUpdate = null;
            if (resultSetProduct.next()) {
                int idProduct = resultSetProduct.getInt("id");
                String name = resultSetProduct.getString("name");
                Double price = resultSetProduct.getDouble("price");
                String description = resultSetProduct.getString("description");
                int quantity = resultSetProduct.getInt("quantity");
                String category_id = resultSetProduct.getString("category_id");

                productUpdate = new Product(name, price, description, quantity);
                productUpdate.setId(idProduct);
                if (category_id != null) {
                    int categoryId = Integer.parseInt(category_id);

                    ResultSet resultCategorySet = CategoryModel.getCategoryById(categoryId);
                    if (resultCategorySet.next()) {
                        String dName = resultCategorySet.getString("name");
                        Category category = new Category(categoryId, dName);
                        productUpdate.setCategory(category);
                    }
                }
            }
            return productUpdate;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getProductById");
        }
        return null;
    }
    public static void updateProduct(HttpServletRequest request, HttpServletResponse response) {
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            Double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryId = Integer.parseInt(request.getParameter("category_id"));

            Product product = new Product(name, price, description, quantity);
            ResultSet resultSet = CategoryModel.getCategoryById(categoryId);
            if (resultSet.next()) {
                String pCategoryName = resultSet.getString("name");
                Category category = new Category(categoryId, pCategoryName);
                product.setCategory(category);
            }else {
                product.setCategory(null);
            }
            product.setId(id);
            ProductModel.update(product);
            response.sendRedirect("/admin/products");

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error for UpdateProduct");
        }
    }
}
