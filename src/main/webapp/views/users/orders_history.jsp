<%-- /views/users/orders_history.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lịch Sử Đơn Hàng Của Bạn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

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
        .order-history-container {
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .order-history-container h2 {
            text-align: center;
            color: #333;
            margin-bottom: 25px;
        }
        .order-item {
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-bottom: 15px;
            padding: 15px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
        }
        .order-item h3 {
            color: #007bff;
            margin-top: 0;
            margin-bottom: 10px;
            font-size: 1.2em;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .order-item h3 span {
            font-size: 0.9em;
            color: #666;
        }
        .order-detail p {
            margin: 5px 0;
            color: #555;
            font-size: 0.95em;
        }
        .order-detail p strong {
            color: #333;
        }
        .no-orders {
            text-align: center;
            color: #777;
            padding: 30px;
            font-size: 1.1em;
        }
        .order-status {
            font-weight: bold;
            padding: 5px 10px;
            border-radius: 3px;
            display: inline-block;
        }
        .status-pending { background-color: #ffe0b2; color: #fb8c00; }
        .status-processing { background-color: #bbdefb; color: #2196f3; }
        .status-shipped { background-color: #c8e6c9; color: #43a047; }
        .status-delivered { background-color: #e0f2f7; color: #00bcd4; }
        .status-cancelled { background-color: #ffcdd2; color: #e53935; }
        .total-money {
            font-weight: bold;
            color: #dc3545;
            font-size: 1.1em;
        }
        /* CSS cho danh sách sản phẩm trong đơn hàng */
        .order-items-list {
            border-top: 1px solid #eee;
            padding-top: 10px;
            margin-top: 15px;
        }
        .order-item-product {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
            background-color: #f0f0f0; /* Nền nhẹ cho mỗi sản phẩm */
            padding: 8px;
            border-radius: 4px;
        }
        .order-item-product img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            margin-right: 15px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        .product-info {
            flex-grow: 1;
        }
        .product-info p {
            margin: 0;
            font-size: 0.9em;
            color: #333;
        }
        .product-info .product-name {
            font-weight: bold;
            font-size: 1em;
            color: #212529;
        }
        .product-info .product-price-qty {
            color: #666;
        }
        .product-description { /* Thêm CSS cho mô tả sản phẩm */
            font-size: 0.85em;
            color: #777;
            margin-top: 3px;
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
<main class="order-history-container">
    <h2>Lịch Sử Đơn Hàng Của Bạn</h2>

    <c:choose>
        <c:when test="${empty userOrders}">
            <p class="no-orders">Bạn chưa có đơn hàng nào.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="order" items="${userOrders}">
                <div class="order-item">
                    <h3>
                        Mã Đơn Hàng: #${order.id}
<%--                        <span>Ngày đặt: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></span>--%>
                    </h3>
                    <div class="order-detail">
                        <p><strong>Người nhận:</strong> ${order.name}</p>
                        <p><strong>Địa chỉ:</strong> ${order.address}</p>
                        <p><strong>Điện thoại:</strong> ${order.phone}</p>
                        <p><strong>Phương thức thanh toán:</strong> ${order.paymentMethodName}</p>
                        <p><strong>Trạng thái:</strong>
                            <span class="order-status status-${order.statusName.toLowerCase()}">
                                    ${order.statusName}
                            </span>
                        </p>
                        <p><strong>Tổng tiền:</strong> <span class="total-money"><fmt:formatNumber value="${order.totalMoney}" type="currency" currencySymbol="VND" /></span></p>

                        <div class="order-items-list">
                            <h4>Sản phẩm đã đặt:</h4>
                            <c:choose>
                                <c:when test="${empty order.orderItems}">
                                    <p>Không có sản phẩm nào trong đơn hàng này.</p>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${order.orderItems}">
                                        <div class="order-item-product">
                                            <c:if test="${item.product != null}">
                                                <img src="${item.product.getImage()}" alt="${item.product.name}">
                                            </c:if>
                                            <div class="product-info">
                                                <p class="product-name">
                                                    <c:if test="${item.product != null}">
                                                        ${item.product.name}
                                                    </c:if>
                                                    <c:if test="${item.product == null}">
                                                        (Sản phẩm không tồn tại)
                                                    </c:if>
                                                </p>
<%--                                                <p class="product-description">--%>
<%--                                                    <c:if test="${item.product != null}">--%>
<%--                                                        ${item.product.description}--%>
<%--                                                    </c:if>--%>
<%--                                                </p>--%>
                                                <p class="product-price-qty">
                                                    <fmt:formatNumber value="${item.priceAtPurchase}" maxFractionDigits="0" minFractionDigits="0" groupingUsed="true" /> VND x ${item.quantity} =
                                                    <fmt:formatNumber value="${item.getSubtotal()}" maxFractionDigits="0" minFractionDigits="0" groupingUsed="true" /> VND
                                                </p>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

</body>
</html>