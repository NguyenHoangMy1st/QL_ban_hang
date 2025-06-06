<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="product" value="${requestScope['product']}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - Chi tiết sản phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        main {
            flex: 1;
        }
        .product-image-container {
            width: 100%;
            padding-bottom: 75%; /* Tỷ lệ 4:3 (height / width * 100). Ví dụ: 3/4 = 0.75 => 75% */
            position: relative;
            overflow: hidden;
            border-radius: .3rem;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
        }
        .product-image-container img {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: contain; /* Giữ nguyên tỷ lệ, vừa với khung hình */
            background-color: #ffffff; /* Nền trắng cho ảnh */
        }
        .product-description-full {
            white-space: pre-wrap; /* Giữ định dạng xuống dòng, khoảng trắng */
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fs-4 fw-bold" href="${pageContext.request.contextPath}/home">KinShop</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <form class="d-flex mx-auto my-2 my-lg-0 flex-grow-1" role="search" style="max-width: 500px;"
                  action="${pageContext.request.contextPath}/products/search" method="get">
                <input class="form-control me-2" type="search" placeholder="Tìm kiếm sản phẩm..." aria-label="Search" name="name"
                       value="${searchTerm != null ? searchTerm : ''}">
                <button class="btn btn-outline-light" type="submit"><i class="fas fa-search"></i></button>
            </form>

            <ul class="navbar-nav mb-2 mb-lg-0 align-items-lg-center ms-auto">
                <li class="nav-item me-3">
                    <a class="nav-link text-white position-relative" href="${pageContext.request.contextPath}/cart">
                        <i class="fas fa-shopping-cart fa-lg"></i>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                0 <%-- Ví dụ: Số lượng sản phẩm trong giỏ --%>
                                <span class="visually-hidden">sản phẩm trong giỏ hàng</span>
                            </span>
                    </a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fa-solid fa-user fa-xl text-white me-2"></i>
                        <span class="d-none d-lg-inline text-white">
                                <c:choose>
                                    <c:when test="${sessionScope.nameUserLogin != null}">
                                        Xin chào, ${sessionScope.nameUserLogin}
                                    </c:when>
                                    <c:otherwise>
                                        Tài khoản
                                    </c:otherwise>
                                </c:choose>
                            </span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg">
                        <c:choose>
                            <c:when test="${sessionScope.idUserLogin != null}">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="fas fa-user me-2"></i>Profile</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders"><i class="fas fa-box-open me-2"></i>Đơn hàng của tôi</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/auth/logout"><i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a class="dropdown-item text-primary" href="${pageContext.request.contextPath}/auth/login"><i class="fas fa-sign-in-alt me-2"></i>Đăng nhập</a></li>
                                <li><a class="dropdown-item text-success" href="${pageContext.request.contextPath}/auth/register"><i class="fas fa-user-plus me-2"></i>Đăng ký</a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">
    <c:if test="${not empty product}">
        <div class="row">
            <div class="col-md-6 mb-4">
                <div class="product-image-container">
                    <img src="${product.image}" class="img-fluid" alt="${product.name}">
                </div>
            </div>
            <div class="col-md-6">
                <h1 class="mb-3">${product.name}</h1>
                <p class="text-muted small mb-2">
                    Danh mục:
                    <c:choose>
                        <c:when test="${product.category != null}">
                            <a href="${pageContext.request.contextPath}/products?category=${product.category.id}" class="text-decoration-none">${product.category.name}</a>
                        </c:when>
                        <c:otherwise>
                            Không xác định
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="text-danger fs-3 fw-bold mb-3">
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                </p>
                <p class="mb-3">
                    <span class="fw-semibold">Số lượng còn lại:</span>
                    <c:choose>
                        <c:when test="${product.quantity > 0}">
                            <span class="text-success">${product.quantity} sản phẩm</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-danger">Hết hàng</span>
                        </c:otherwise>
                    </c:choose>
                </p>

                <h4 class="mt-4 mb-2">Mô tả sản phẩm</h4>
                <div class="card card-body bg-light product-description-full">
                    <p><c:out value="${product.description != null ? product.description : 'Sản phẩm này chưa có mô tả chi tiết.'}"/></p>
                </div>

                <h4 class="mt-4 mb-2">Thêm vào giỏ hàng</h4>
                <c:if test="${product.quantity > 0}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-flex align-items-center">
                        <input type="hidden" name="productId" value="${product.id}">
                        <label for="quantity" class="form-label me-3 mb-0">Số lượng:</label>
                        <input type="number" id="quantity" name="quantity" class="form-control me-3" value="1" min="1" max="${product.quantity}" required style="width: 100px;">
                        <c:if test="${param.error eq 'invalid_quantity'}">
                            <div class="alert alert-danger" role="alert">Số lượng không hợp lệ hoặc vượt quá số lượng tồn kho.</div>
                        </c:if>
                        <c:if test="${param.error eq 'cart_add_failed'}">
                            <div class="alert alert-danger" role="alert">Thêm sản phẩm vào giỏ hàng thất bại. Vui lòng thử lại.</div>
                        </c:if>
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-cart-plus me-2"></i>Thêm vào giỏ
                        </button>
                    </form>
                </c:if>
                <c:if test="${product.quantity <= 0}">
                    <div class="alert alert-warning mt-3" role="alert">
                        Sản phẩm này hiện đang hết hàng.
                    </div>
                </c:if>
            </div>
        </div>
    </c:if>
    <c:if test="${empty product}">
        <div class="alert alert-warning text-center" role="alert">
            <h4 class="alert-heading">Sản phẩm không tìm thấy!</h4>
            <p>Rất tiếc, chúng tôi không thể tìm thấy sản phẩm bạn đang tìm kiếm.</p>
            <hr>
            <p class="mb-0">Vui lòng quay lại <a href="${pageContext.request.contextPath}/home" class="alert-link">trang chủ</a> để khám phá các sản phẩm khác.</p>
        </div>
    </c:if>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>