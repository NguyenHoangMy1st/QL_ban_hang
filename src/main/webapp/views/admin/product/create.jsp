<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="categories" value="${requestScope['categories']}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Sản phẩm Mới</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            background-color: #f8f9fa; /* Light gray background */
        }
        .container {
            max-width: 800px;
            margin-top: 50px;
            margin-bottom: 50px;
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #343a40;
            margin-bottom: 30px;
            text-align: center;
        }
        .form-label {
            font-weight: 600;
            color: #495057;
        }
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
            padding: 10px 20px;
            font-size: 1.1rem;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
            border-color: #6c757d;
            padding: 10px 20px;
            font-size: 1.1rem;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
            border-color: #5a6268;
        }
        .form-control:focus {
            box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25);
            border-color: #80bdff;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Tạo Sản phẩm Mới</h2>

    <%-- Hiển thị thông báo thành công/lỗi --%>
    <c:if test="${not empty requestScope.successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${requestScope.successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${requestScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <form action="/admin/products/create" method="post">
        <%-- Trường Tên Sản phẩm --%>
        <div class="mb-3">
            <label for="productName" class="form-label">Tên Sản phẩm <span class="text-danger">*</span></label>
            <input type="text" class="form-control" id="productName" name="name" required
                   placeholder="Nhập tên sản phẩm">
        </div>
        <div class="mb-3">
            <label for="productPrice" class="form-label">Giá <span class="text-danger">*</span></label>
            <input type="number" step="1000" class="form-control" id="productPrice" name="price" required
                   placeholder="Nhập giá sản phẩm">
        </div>
        <div class="mb-3">
            <label for="productPrice" class="form-label">Số lượng <span class="text-danger">*</span></label>
            <input type="number" step="1" class="form-control" id="productQuantity" name="quantity" required
                   placeholder="Nhập số lượng sản phẩm">
        </div>
        <div class="mb-3">
            <label for="productDescription" class="form-label">Mô tả</label>
            <textarea class="form-control" id="productDescription" name="description" rows="4"
                      placeholder="Mô tả chi tiết về sản phẩm"></textarea>
        </div>

<%--        <div class="mb-3">--%>
<%--            <label for="productImage" class="form-label">Ảnh Sản phẩm <span class="text-danger">*</span></label>--%>
<%--            <input type="file" class="form-control" id="productImage" name="image" accept="image/*" required>--%>
<%--        </div>--%>

        <div class="mb-3">
            <label for="productCategory" class="form-label">Danh mục <span class="text-danger">*</span></label>
            <select class="form-select" id="productCategory" name="category_id" required>
                <option selected>Chọn danh mục</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <button type="submit" class="btn btn-primary me-md-2">Tạo Sản phẩm</button>
            <a href="/admin/products" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>