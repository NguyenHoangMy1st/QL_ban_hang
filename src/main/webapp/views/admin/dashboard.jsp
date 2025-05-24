<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            padding-top: 20px;
        }
        .main-content {
            margin-left: 250px; /* Bằng chiều rộng của sidebar */
            padding: 20px;
        }
        .container-fluid { /* Dùng container-fluid để rộng hơn trong main-content */
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
    </style>
</head>
<body>
<jsp:include page="/views/admin/sidebar/sidebar.jsp" />

<div class="main-content">
    <div class="container-fluid">
        <h2>Dashboard</h2>
        <p>Chào mừng bạn đến với trang quản trị.</p>
        <div class="row">
            <div class="col-md-4">
                <div class="card text-white bg-primary mb-3">
                    <div class="card-header">Tổng Sản Phẩm</div>
                    <div class="card-body">
                        <h5 class="card-title">1200</h5>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-success mb-3">
                    <div class="card-header">Tổng Danh Mục</div>
                    <div class="card-body">
                        <h5 class="card-title">15</h5>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-info mb-3">
                    <div class="card-header">Tổng Người Dùng</div>
                    <div class="card-body">
                        <h5 class="card-title">50</h5>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
