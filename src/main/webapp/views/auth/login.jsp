<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Đăng Nhập</title>
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
    .login-container {
      background-color: #ffffff;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 400px; /* Chiều rộng phù hợp cho form login */
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
  <div class="login-container">

    <h2 class="mb-4">Đăng Nhập</h2>

    <%-- Hiển thị thông báo lỗi --%>
    <c:if test="${not empty param.error}">
      <div class="alert alert-danger" role="alert">
        <c:out value="${param.message}"/>
      </div>
    </c:if>
    <%-- Hiển thị thông báo thành công (ví dụ từ đăng ký thành công) --%>
    <c:if test="${not empty param.success && param.success eq 'register'}">
      <div class="alert alert-success" role="alert">
        Đăng ký tài khoản thành công! Vui lòng đăng nhập.
      </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/login" method="post">
      <div class="text-center mb-3 social-buttons">
        <p>Đăng nhập với:</p>
        <button  type="button" class="btn btn-link btn-floating mx-1">
          <i class="fab fa-facebook-f"></i>
        </button>

        <button  type="button" class="btn btn-link btn-floating mx-1">
          <i class="fab fa-google"></i>
        </button>

        <button  type="button" class="btn btn-link btn-floating mx-1">
          <i class="fab fa-twitter"></i>
        </button>

        <button  type="button" class="btn btn-link btn-floating mx-1">
          <i class="fab fa-github"></i>
        </button>
      </div>

      <p class="text-center">Hoặc:</p>

      <div class="form-group mb-4">
        <label class="form-label" for="loginEmail">Email</label>
        <input type="email" name="email" id="loginEmail" class="form-control" required
               value="${param.email != null ? param.email : ''}"/></div>

      <div class="form-group mb-4">
        <label class="form-label" for="loginPassword">Mật khẩu</label>
        <input type="password" name="password" id="loginPassword" class="form-control" required />
      </div>

      <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="form-check">
          <input class="form-check-input" type="checkbox" value="" id="loginCheck" checked />
          <label class="form-check-label" for="loginCheck"> Nhớ mật khẩu </label>
        </div>
        <a href="#!">Quên mật khẩu?</a>
      </div>

      <button type="submit" class="btn btn-primary mb-4">Đăng Nhập</button>

      <div class="text-center">
        <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/auth/register">Đăng ký ngay</a></p>
      </div>
    </form>
  </div>

</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>