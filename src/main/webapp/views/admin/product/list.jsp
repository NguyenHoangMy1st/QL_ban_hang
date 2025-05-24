<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản Lý Sản Phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            padding-top: 20px;
        }
        .container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #333;
            margin-bottom: 25px;
            text-align: center;
        }
        .table img {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 5px;
        }
        .action-buttons button {
            margin-right: 5px;
        }
        .btn-add-product {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Quản Lý Sản Phẩm</h2>

    <a href="${pageContext.request.contextPath}/admin/products/create" class="btn btn-primary mb-3 btn-add-product">Thêm Sản Phẩm Mới</a>

    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Ảnh</th>
                <th>Tên Sản Phẩm</th>
                <th>Giá</th>
                <th>Mô Tả</th>
                <th>Danh Mục</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>${product.id}</td>
                    <td><img src="${product.image}" alt="${product.name}" class="img-fluid"></td>
                    <td>${product.name}</td>
                    <td>${product.price}</td>
                    <td>${product.description}</td>
                    <td>
                        <c:choose>
                            <c:when test="${product.category != null}">
                                ${product.category.name}
                            </c:when>
                            <c:otherwise>
                                N/A
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="action-buttons">
                            <%-- Nút Sửa --%>
                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${product.id}" class="btn btn-warning btn-sm">Sửa</a>
                            <%-- Nút Xóa (có xác nhận bằng JavaScript) --%>
                        <button type="button" class="btn btn-danger btn-sm" onclick="confirmDelete('${product.id}')">Xóa</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty products}">
                <tr>
                    <td colspan="7" class="text-center">Không có sản phẩm nào.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<script>
    function confirmDelete(productId) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) {
            // Chuyển hướng đến URL xóa sản phẩm
            window.location.href = "${pageContext.request.contextPath}/admin/products/delete?id=" + productId;
        }
    }
</script>
</body>
</html>