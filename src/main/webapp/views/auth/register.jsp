<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/24/2025
  Time: 8:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Đăng Ký Tài Khoản</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .register-container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px; /* Tăng chiều rộng để phù hợp với các trường mới */
        }
        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .btn-primary {
            width: 100%;
            padding: 10px;
            font-size: 1.1em;
        }
        .alert {
            margin-bottom: 20px;
        }
        .text-center.mb-3 p {
            margin-bottom: 10px;
        }
        .social-buttons .btn {
            margin: 0 5px;
        }
    </style>
</head>
<body>
<div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="register-container">

        <h2 class="mb-4">Đăng Ký Tài Khoản Mới</h2>

        <%-- Hiển thị thông báo lỗi --%>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${param.message}"/>
            </div>
        </c:if>
        <%-- Hiển thị thông báo thành công --%>
        <c:if test="${not empty param.success && param.success eq 'register'}">
            <div class="alert alert-success" role="alert">
                Đăng ký tài khoản thành công! Vui lòng <a href="${pageContext.request.contextPath}/auth/login">đăng nhập</a>.
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/register" method="post">
            <div class="text-center mb-3 social-buttons">
                <p>Đăng ký với:</p>
                <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-facebook-f"></i>
                </button>
                <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-google"></i>
                </button>
                <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-twitter"></i>
                </button>
                <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-github"></i>
                </button>
            </div>

            <p class="text-center">Hoặc:</p>

            <div class="form-group mb-4">
                <label class="form-label" for="registerName">Họ và Tên</label>
                <input type="text" name="name" id="registerName" class="form-control" required
                       value="${param.name != null ? param.name : ''}"/>
            </div>

            <div class="form-group mb-4">
                <label class="form-label" for="registerEmail">Email</label>
                <input type="email" name="email" id="registerEmail" class="form-control" required
                       value="${param.email != null ? param.email : ''}"/>
            </div>

            <div class="form-group mb-4">
                <label class="form-label" for="registerPhone">Số điện thoại</label>
                <input type="tel" name="phone" id="registerPhone" class="form-control"
                       value="${param.phone != null ? param.phone : ''}"/>
            </div>

            <div class="form-group mb-4">
                <label class="form-label" for="registerAddress">Địa chỉ</label>
                <input type="text" name="address" id="registerAddress" class="form-control"
                       value="${param.address != null ? param.address : ''}"/>
            </div>

            <div class="form-group mb-4">
                <label class="form-label" for="registerPassword">Mật khẩu</label>
                <input type="password" name="password" id="registerPassword" class="form-control" required />
            </div>

            <div class="form-group mb-4">
                <label class="form-label" for="registerConfirmPassword">Xác nhận mật khẩu</label>
                <input type="password" name="confirm_password" id="registerConfirmPassword" class="form-control" required />
            </div>

            <button type="submit" class="btn btn-primary btn-block mb-4">Đăng Ký</button>

            <div class="text-center">
                Bạn đã có tài khoản? <a href="${pageContext.request.contextPath}/auth/login">Đăng nhập</a>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>