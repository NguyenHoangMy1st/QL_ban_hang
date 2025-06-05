<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/23/2025
  Time: 11:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="categories" value="${requestScope['categories']}" />
<html>
<head>
    <title>Quản Lý Danh Mục</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            padding-top: 0;
        }
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        .content-container {
            background-color:  rgb(117 190 218 / 50%);
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #333;
            margin-bottom: 25px;
            text-align: center;
        }

        .action-buttons button,
        .action-buttons a {
            margin-right: 5px; /* Add margin for spacing between buttons */
            margin-bottom: 5px; /* Add margin for spacing between buttons vertically on smaller screens */
        }
        .btn-add-product {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<jsp:include page="/views/admin/sidebar/sidebar.jsp" />

<div class="main-content">
    <div class="content-container">
        <h2>Quản Lý Danh Mục</h2>

        <a href="/admin/categories/create" class="btn btn-primary mb-3 btn-add-product">Thêm Danh Mục Mới</a>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark bg-blue-300 bg-opacity-50">
                <tr>
                    <th>ID</th>
                    <th>Tên Danh Mục</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="category" items="${categories}">
                    <tr>
                        <td><c:out value="${category.getId()}"/></td>
                        <td><c:out value="${category.getName()}"/></td>
                        <td class="action-buttons">
                            <a href="/admin/categories/edit?id=<c:out value="${category.getId()}"/>" class="btn btn-warning btn-sm">Sửa</a>
                            <button type="button" class="btn btn-danger btn-sm" onclick="confirmDelete('<c:out value="${category.getId()}"/>')">Xóa</button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty categories}">
                    <tr>
                        <td colspan="8" class="text-center">Không có danh mục nào.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
    function confirmDelete(category_id) {
        if (confirm("Bạn có chắc chắn muốn xóa danh mục này không?")) {
            window.location.href = "/admin/categories/delete?id=" + category_id;
        }
    }
</script>

</body>
</html>