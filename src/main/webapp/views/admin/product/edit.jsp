<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="product" value="${requestScope['product']}" /> <%-- Lấy đối tượng sản phẩm từ request --%>
<c:set var="categories" value="${requestScope['categories']}" /> <%-- Lấy danh sách danh mục từ request --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh Sửa Sản phẩm</title>
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
    <h2>Chỉnh Sửa Sản phẩm</h2>

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

    <form action="/admin/products/edit" method="post">
        <%-- Hidden field cho ID sản phẩm --%>
        <input type="hidden" name="id" value="${product.getId()}">

        <%-- Trường Tên Sản phẩm --%>
        <div class="mb-3">
            <label for="productName" class="form-label">Tên Sản phẩm <span class="text-danger">*</span></label>
            <input type="text" class="form-control" id="productName" name="name" required
                   placeholder="Nhập tên sản phẩm" value="<c:out value="${product.getName()}"/> ">
        </div>
        <div class="mb-3">
            <label for="productPrice" class="form-label">Giá <span class="text-danger">*</span></label>
            <input type="number" step="1000" class="form-control" id="productPrice" name="price" required
                   placeholder="Nhập giá sản phẩm" value="<c:out value="${product.getPrice()}"/> ">
        </div>
        <div class="mb-3">
            <label for="productQuantity" class="form-label">Số lượng <span class="text-danger">*</span></label>
            <input type="number" step="1" class="form-control" id="productQuantity" name="quantity" required
                   placeholder="Nhập số lượng sản phẩm" value="<c:out value="${product.getQuantity()}"/> ">
        </div>
        <div class="mb-3">
            <label for="productDescription" class="form-label">Mô tả</label>
            <textarea class="form-control" id="productDescription" name="description" rows="4"
                      placeholder="Mô tả chi tiết về sản phẩm" value="<c:out value="${product.getDescription()}"/> ">${product.getDescription()}</textarea>
        </div>

        <%-- Phần này dành cho hiển thị ảnh hiện tại và cho phép upload ảnh mới (nếu bạn muốn) --%>
        <%-- Nếu bạn đã quyết định không chèn ảnh, bạn có thể bỏ qua phần này hoặc để trống --%>
<%--        <div class="mb-3">--%>
<%--            <label class="form-label">Ảnh hiện tại:</label>--%>
<%--            <c:if test="${not empty product.getImage()}">--%>
<%--                <img src="${product.getImage()}" alt="${product.getName()}" style="max-width: 150px; display: block; margin-bottom: 10px;">--%>
<%--            </c:if>--%>
<%--            <c:if test="${empty product.getImage()}">--%>
<%--                <p>Không có ảnh.</p>--%>
<%--            </c:if>--%>
<%--            &lt;%&ndash; Nếu muốn cho phép upload ảnh mới, uncomment dòng dưới và thêm enctype="multipart/form-data" vào form &ndash;%&gt;--%>
<%--            &lt;%&ndash; <label for="newProductImage" class="form-label">Thay đổi Ảnh Sản phẩm (Tùy chọn)</label>--%>
<%--            <input type="file" class="form-control" id="newProductImage" name="new_image" accept="image/*"> &ndash;%&gt;--%>
<%--        </div>--%>

        <div class="mb-3">
            <label for="productCategory" class="form-label">Danh mục <span class="text-danger">*</span></label>
            <select class="form-select" id="productCategory" name="category_id" required>
                <option selected>Chọn danh mục</option> <%-- Giá trị rỗng cho tùy chọn mặc định --%>
                <c:forEach var="category" items="${categories}">
                    <c:if test="${product.getCategory().getId() == category.getId()}">
                        <option selected value="${category.getId()}">${category.getName()}</option>
                    </c:if>
                    <c:if test="${product.getCategory().getId() != category.getId()}">
                        <option value="${category.id}">${category.name}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>

        <%-- Thêm các trường status và sold nếu bạn muốn chỉnh sửa chúng --%>
<%--        <div class="mb-3">--%>
<%--            <label for="productStatus" class="form-label">Trạng thái <span class="text-danger">*</span></label>--%>
<%--            <select class="form-select" id="productStatus" name="status" required>--%>
<%--                <option value="1" <c:if test="${product.getStatus() == 1}">selected</c:if>>Hiển thị</option>--%>
<%--                <option value="0" <c:if test="${product.getStatus() == 0}">selected</c:if>>Ẩn</option>--%>
<%--            </select>--%>
<%--        </div>--%>
<%--        <div class="mb-3">--%>
<%--            <label for="productSold" class="form-label">Số lượng đã bán</label>--%>
<%--            <input type="number" step="1" class="form-control" id="productSold" name="sold"--%>
<%--                   placeholder="Nhập số lượng đã bán" value="${product.getSold()}">--%>
<%--        </div>--%>


        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <button type="submit" class="btn btn-primary me-md-2">Cập nhật Sản phẩm</button>
            <a href="/admin/products" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>