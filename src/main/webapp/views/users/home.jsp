<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Dùng để định dạng số, tiền tệ --%>
<c:set var="productList" value="${requestScope['productList']}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Cửa Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <style>
        /* Custom CSS (giữ nguyên hoặc điều chỉnh theo ý muốn) */
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        main {
            flex: 1;
        }
        .product-item .card-img-top {
            height: 200px;
            object-fit: cover;
        }
        .product-item .card-body h5 {
            height: 48px; /* Tùy chỉnh để hiển thị 2 dòng tiêu đề */
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
        }
        .product-item .card-body .product-description {
            height: 60px; /* Tùy chỉnh để hiển thị khoảng 3-4 dòng mô tả */
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3; /* Giới hạn 3 dòng */
            -webkit-box-orient: vertical;
            font-size: 0.9em;
            color: #6c757d;
            margin-bottom: 10px; /* Thêm khoảng cách dưới */
        }
        .price-filter-display {
            font-weight: bold;
            color: #007bff;
            text-align: center;
            margin-top: 5px;
        }
        .form-range::-webkit-slider-thumb {
            background-color: #0d6efd;
        }
        .form-range::-moz-range-thumb {
            background-color: #0d6efd;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fs-4 fw-bold" href="${pageContext.request.contextPath}/home">YourShop</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <form class="d-flex mx-auto my-2 my-lg-0 flex-grow-1" role="search" style="max-width: 500px;">
                <input class="form-control me-2" type="search" placeholder="Tìm kiếm sản phẩm..." aria-label="Search">
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

<main class="container-fluid py-4">
    <div class="row">
        <div class="col-md-3 col-lg-2">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-primary text-white fs-5 fw-semibold">
                    Danh mục sản phẩm
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none text-dark fw-bold">Tất cả danh mục</a></li>
                        <c:forEach var="category" items="${categories}">
                            <li class="list-group-item">
                                <a href="${pageContext.request.contextPath}/products?category=${category.id}" class="text-decoration-none text-dark">
                                    <c:out value="${category.name}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white fs-5 fw-semibold">
                    Lọc theo giá
                </div>
                <div class="card-body">
                    <label for="price-range" class="form-label">Giá từ: <span id="current-price" class="price-filter-display">0 VNĐ</span></label>
                    <input type="range" class="form-range" id="price-range" min="0" max="10000000" value="0" step="100000">
                    <button class="btn btn-primary w-100 mt-3">Áp dụng</button>
                </div>
            </div>
        </div>

        <div class="col-md-9 col-lg-10">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2>Danh sách sản phẩm</h2>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary">
                    <i class="fas fa-eye me-2"></i>Xem tất cả sản phẩm
                </a>
            </div>

            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                <c:choose>
                    <c:when test="${not empty productList}">
                        <c:forEach var="product" items="${productList}" begin="0" end="19"> <%-- Hiển thị tối đa 20 sản phẩm --%>
                            <div class="col">
                                <div class="card h-100 shadow-sm product-item">
                                    <img src="${product.image}" class="card-img-top" alt="${product.name}">
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title mb-2">
                                            <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}" class="text-decoration-none text-dark">
                                                <c:out value="${product.name}"/>
                                            </a>
                                        </h5>
                                        <p class="product-description">
                                            <c:out value="${product.description != null ? product.description : 'Không có mô tả.'}"/>
                                        </p>
                                        <p class="card-text text-danger fs-5 fw-bold mb-3">
                                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                                        </p>
                                            <%-- Đã xóa nút "Thêm vào giỏ" --%>
                                        <div class="mt-auto">
                                            <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}" class="btn btn-info w-100">Xem chi tiết</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <div class="alert alert-info" role="alert">
                                Chưa có sản phẩm nào để hiển thị.
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- Logic ẩn/hiện phân trang --%>
            <c:if test="${not empty productList and productList.size() > 20}">
                <nav aria-label="Page navigation example" class="mt-4">
                    <ul class="pagination justify-content-center">
                        <li class="page-item disabled">
                            <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Trước</a>
                        </li>
                        <li class="page-item active"><a class="page-link" href="#">1</a></li>
                        <li class="page-item"><a class="page-link" href="#">2</a></li>
                        <li class="page-item"><a class="page-link" href="#">3</a></li>
                        <li class="page-item"><a class="page-link" href="#">...</a></li>
                        <li class="page-item"><a class="page-link" href="#">10</a></li>
                        <li class="page-item">
                            <a class="page-link" href="#">Tiếp</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
    const priceRange = document.getElementById('price-range');
    const currentPriceDisplay = document.getElementById('current-price');

    function formatCurrency(value) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    }

    priceRange.oninput = function() {
        currentPriceDisplay.textContent = formatCurrency(this.value);
    };

    currentPriceDisplay.textContent = formatCurrency(priceRange.value);
</script>
</body>
</html>