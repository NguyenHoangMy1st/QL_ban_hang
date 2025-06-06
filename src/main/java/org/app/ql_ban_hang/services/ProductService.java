package org.app.ql_ban_hang.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.app.ql_ban_hang.entities.Category;
import org.app.ql_ban_hang.entities.Product;
import org.app.ql_ban_hang.models.CategoryModel;
import org.app.ql_ban_hang.models.ProductModel;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
public class ProductService {
    private static final int PRODUCTS_PER_PAGE = 20;
    public static List<Product> getProductsPaged(int page) {
        int offset = (page - 1) * PRODUCTS_PER_PAGE;
        try {
            return ProductModel.getProductsPaged(PRODUCTS_PER_PAGE, offset);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting paged products: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public static int getTotalPages() {
        try {
            int totalProducts = ProductModel.getTotalProductCount();
            return (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting total product count: " + e.getMessage());
            return 1;
        }
    }
    public static List<Product> getProductsPagedAndFiltered(int page, Integer categoryId) {
        int offset = (page - 1) * PRODUCTS_PER_PAGE;
        try {
            return ProductModel.getProductsPagedAndFiltered(PRODUCTS_PER_PAGE, offset, categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting paged and filtered products from model: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public static int getTotalPagesFiltered(Integer categoryId) {
        try {
            int totalProducts = ProductModel.getTotalProductCountFiltered(categoryId);
            return (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting total filtered pages from model: " + e.getMessage());
            return 1;
        }
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
    private static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    public static void createProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            Double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryId = Integer.parseInt(request.getParameter("category_id"));

            String imagePath = null;
            Part filePart = request.getPart("image");

            if (filePart != null && filePart.getSize() > 0 && getFileName(filePart) != null && !getFileName(filePart).isEmpty()) {
                String fileName = getFileName(filePart);
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

                String uploadDirectory = request.getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + "products";
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String filePath = uploadDirectory + File.separator + uniqueFileName;
                filePart.write(filePath);
                imagePath = request.getContextPath() + "/uploads/products/" + uniqueFileName;
            } else {
                imagePath = "/images/default_product.jpg";
            }

            Category category = CategoryModel.getCategoryById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Danh mục sản phẩm không hợp lệ.");
            }

            Product product = new Product(name, price, description, imagePath, quantity);
            product.setCategory(category);

            ProductModel.create(product);
            response.sendRedirect(request.getContextPath() + "/admin/products?success=create");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in createProduct: " + e.getMessage());
            String errorMessage = "Có lỗi xảy ra khi tạo sản phẩm: " + (e.getMessage() != null ? e.getMessage() : "Lỗi không xác định.");
            try {
                response.sendRedirect(request.getContextPath() + "/admin/products/create?error=true&message=" + java.net.URLEncoder.encode(errorMessage, "UTF-8")
                        + "&name=" + (request.getParameter("name") != null ? java.net.URLEncoder.encode(request.getParameter("name"), "UTF-8") : "")
                        + "&price=" + (request.getParameter("price") != null ? java.net.URLEncoder.encode(request.getParameter("price"), "UTF-8") : "")
                        + "&quantity=" + (request.getParameter("quantity") != null ? java.net.URLEncoder.encode(request.getParameter("quantity"), "UTF-8") : "")
                        + "&description=" + (request.getParameter("description") != null ? java.net.URLEncoder.encode(request.getParameter("description"), "UTF-8") : "")
                        + "&category_id=" + (request.getParameter("category_id") != null ? java.net.URLEncoder.encode(request.getParameter("category_id"), "UTF-8") : "")
                );
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
    public static Product getProductById(HttpServletRequest request) {
        try{
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                System.out.println("Error: Product ID is missing for getProductById.");
                return null;
            }
            int id = Integer.parseInt(idParam);
            return ProductModel.getProductById(id);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid Product ID format: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in ProductService.getProductById: " + e.getMessage());
        }
        return null;
    }
    public static Product ProductById(int id) {
        try {
            return ProductModel.getProductById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in ProductService.getProductById(int id): " + e.getMessage());
        }
        return null;
    }
    public static void updateProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            Double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryId = Integer.parseInt(request.getParameter("category_id"));

            Product existingProduct = ProductById(id);
            String currentImage = (existingProduct != null) ? existingProduct.getImage() : null;

            Part filePart = request.getPart("new_image");
            if (filePart != null && filePart.getSize() > 0 && getFileName(filePart) != null && !getFileName(filePart).isEmpty()) {
                if (currentImage != null && !currentImage.isEmpty() && !currentImage.endsWith("/images/default_product.jpg")) {
                    String oldFilePath = request.getServletContext().getRealPath("") + currentImage.replace(request.getContextPath(), "");
                    File oldFile = new File(oldFilePath);
                    if (oldFile.exists() && oldFile.delete()) {
                        System.out.println("Deleted old image: " + oldFilePath);
                    } else {
                        System.out.println("Failed to delete old image or old image not found: " + oldFilePath);
                    }
                }

                String fileName = getFileName(filePart);
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
                String uploadDirectory = request.getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + "products";
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String filePath = uploadDirectory + File.separator + uniqueFileName;
                filePart.write(filePath);
                currentImage = request.getContextPath() + "/uploads/products/" + uniqueFileName;
            }

            Product product = new Product(id, name, price, description, currentImage, quantity);

            Category category = CategoryModel.getCategoryById(categoryId);
            if (category != null) {
                product.setCategory(category);
            } else {
                product.setCategory(null);
            }

            ProductModel.update(product);
            response.sendRedirect(request.getContextPath() + "/admin/products?success=update");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error for UpdateProduct: " + e.getMessage());
            String errorMessage = "Có lỗi xảy ra khi cập nhật sản phẩm: " + (e.getMessage() != null ? e.getMessage() : "Lỗi không xác định.");
            try {
                response.sendRedirect(request.getContextPath() + "/admin/products/edit?id=" + request.getParameter("id") + "&error=true&message=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static List<Product> searchProducts(String searchTerm, int page) {
        int offset = (page - 1) * PRODUCTS_PER_PAGE;
        try {
            return ProductModel.searchProducts(searchTerm != null ? searchTerm : "", PRODUCTS_PER_PAGE, offset);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in ProductService.searchProducts: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static int getTotalSearchPages(String searchTerm) {
        try {
            int totalProducts = ProductModel.getTotalProductCountBySearchTerm(searchTerm != null ? searchTerm : "");
            return (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in ProductService.getTotalSearchPages: " + e.getMessage());
            return 1;
        }
    }
    public static void getProductSearch(HttpServletRequest request, HttpServletResponse response) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getProductSearch");
        }
    }
    public static Category getCategoryById(HttpServletRequest request, HttpServletResponse response) {
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            Category category = CategoryModel.getCategoryById(id);
            return category;
        } catch (NumberFormatException e) {
            System.err.println("Lỗi: ID danh mục không hợp lệ. " + e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy danh mục theo ID: " + e.getMessage());
            return null;
        }
    }
    public static void createCategory(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty.");
            }
            Category category = new Category(name);

            CategoryModel.create(category);
            response.sendRedirect( "/admin/categories?success=create");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in createCategory: " + e.getMessage());
            String errorMessage = "Có lỗi xảy ra khi tạo danh mục: " + (e.getMessage() != null ? e.getMessage() : "Lỗi không xác định.");
            try {
                response.sendRedirect(request.getContextPath() + "/admin/categories/create?error=true&message=" + java.net.URLEncoder.encode(errorMessage, "UTF-8")
                        + "&name=" + (request.getParameter("name") != null ? java.net.URLEncoder.encode(request.getParameter("name"), "UTF-8") : "")
                );
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    public static void updateCategory(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty.");
            }

            Category category = new Category(id, name);

            CategoryModel.update(category);
            response.sendRedirect(request.getContextPath() + "/admin/categories?success=update");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in updateCategory: " + e.getMessage());
            String errorMessage = "Có lỗi xảy ra khi cập nhật danh mục: " + (e.getMessage() != null ? e.getMessage() : "Lỗi không xác định.");
            try {
                response.sendRedirect(request.getContextPath() + "/admin/categories/edit?id=" + request.getParameter("id") + "&error=true&message=" + java.net.URLEncoder.encode(errorMessage, "UTF-8")
                        + "&name=" + (request.getParameter("name") != null ? java.net.URLEncoder.encode(request.getParameter("name"), "UTF-8") : "")
                );
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
