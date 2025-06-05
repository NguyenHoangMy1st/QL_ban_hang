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
import java.util.List;
import java.util.UUID;
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
                String pImage = resultSet.getString("image");
                int pQuantity = resultSet.getInt("quantity");
                Product product = new Product(pID, pName, pPrice, pDescription,pImage, pQuantity);
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
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName; // Tạo tên file duy nhất

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

            // Tạo đối tượng Product và gán Category đã tìm thấy
            Product product = new Product(name, price, description, imagePath, quantity);
            product.setCategory(category); // Gán đối tượng Category trực tiếp

            ProductModel.create(product); // Tạo sản phẩm trong DB
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
    public static Product getProductById(HttpServletRequest request) { // Bỏ HttpServletResponse resp vì không cần thiết
        try{
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                System.out.println("Error: Product ID is missing for getProductById.");
                return null;
            }
            int id = Integer.parseInt(idParam);
            return ProductModel.getProductById(id); // Gọi phương thức mới trong ProductModel
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid Product ID format: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in ProductService.getProductById: " + e.getMessage());
        }
        return null;
    }
    // Overload cho phương thức getProductById(int id) để tiện gọi từ các phương thức khác trong Service
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

            Product existingProduct = ProductById(id); // Gọi overload của getProductById
            String currentImage = (existingProduct != null) ? existingProduct.getImage() : null;

            Part filePart = request.getPart("new_image"); // Tên input file cho ảnh mới trong form update
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

            // Giả định CategoryModel.getCategoryById trả về một đối tượng Category
            Category category = CategoryModel.getCategoryById(categoryId);
            if (category != null) {
                product.setCategory(category);
            } else {
                product.setCategory(null); // Nếu category không tìm thấy, đặt là null
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

    public static Product findProductByName(HttpServletRequest request) { // Bỏ HttpServletResponse resp
        try {
            String fName = request.getParameter("name");
            if (fName == null || fName.trim().isEmpty()) {
                System.out.println("Error: Product name is missing for findProductByName.");
                return null;
            }
            // Gọi ProductModel.findProductByName() đã được cập nhật để trả về đối tượng Product
            Product product = ProductModel.findProductByName(fName);

            // ProductModel.findProductByName() đã xử lý việc lấy Category, nên không cần làm lại ở đây
            return product;

        } catch (SQLException e) { // Bắt SQLException
            e.printStackTrace();
            System.out.println("Error in ProductService.findProductByName: " + e.getMessage());
        }
        return null;
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
            // Xử lý lỗi nếu 'id' không phải là số
            System.err.println("Lỗi: ID danh mục không hợp lệ. " + e.getMessage());
            // Có thể thêm response.sendError(HttpServletResponse.SC_BAD_REQUEST) ở đây nếu cần
            return null;
        } catch (Exception e) {
            // Bắt các lỗi khác (ví dụ: SQLException từ CategoryModel.getCategoryById)
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
