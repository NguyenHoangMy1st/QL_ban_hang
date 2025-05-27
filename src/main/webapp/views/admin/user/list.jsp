<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="users" value="${requestScope['users']}" />


<html>
<head>
    <title>Quản lí người dùng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

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
        .summary-container{
            margin-top: 30px;
            flex: 1;
            background-color: rgba(117, 190, 218, 0.2);
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

    </style>
</head>
<body>
<jsp:include page="/views/admin/sidebar/sidebar.jsp" />

<div class="main-content">
    <div class="content-container">
        <h2>Quản Lý Người Dùng</h2>

        <a href="/admin/users/create" class="btn btn-primary mb-3 btn-add-product">Thêm Người Dùng</a>

        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>STT</th>
                <th>Họ Tên</th>
                <th>Email</th>
                <th>Điện Thoại</th>
                <th>Địa Chỉ</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}</td>
                    <td><c:out value="${user.getName()}"/></td>
                    <td><c:out value="${user.getEmail()}"/></td>
                    <td><c:out value="${user.getPhone()}"/></td>
                    <td><c:out value="${user.getAddress()}"/></td>
                    <td class="action-buttons">
                        <a href="/admin/users/edit?id=<c:out value="${user.getId()}"/>" class="btn btn-warning btn-sm">Sửa</a>
                        <button type="button" onclick="confirmDelete('<c:out value="${user.getId()}"/>')"  class="btn btn-danger btn-sm" data-mdb-ripple-init href="/admin/users/delete?id=<c:out value="${user.getId()}" />">Delete</button>                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty users}">
                <tr>
                    <td colspan="6" class="text-center">Không có người dùng nào.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <!-- Tổng người dùng hoặc ảnh minh họa -->
    <div class="summary-container text-center">
        <h2>Tổng Người Dùng</h2>
        <h3 class="text-primary fw-bold">${fn:length(users)}</h3>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
