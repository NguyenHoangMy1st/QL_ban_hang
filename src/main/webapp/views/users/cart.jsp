<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="carts" value="${requestScope['cartItems']}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ Hàng Của Bạn</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <style>
        body {
            background-color: #f8f9fa;
        }
        .cart-item-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
        }
        .card-cart {
            border-radius: 1rem;
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.05);
        }
        .quantity-input {
            width: 70px;
            text-align: center;
        }
        /* Style cho thông báo lỗi và thành công */
        .message-box {
            border-radius: .25rem;
            padding: .75rem 1.25rem;
            margin-bottom: 1rem;
            text-align: center;
        }
        .error-message {
            color: #dc3545; /* Đỏ */
            background-color: #f8d7da; /* Nền đỏ nhạt */
            border: 1px solid #f5c6cb;
        }
        .success-message {
            color: #0f5132; /* Xanh lá đậm */
            background-color: #d1e7dd; /* Nền xanh lá nhạt */
            border: 1px solid #badbcc;
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

            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-lg-center">
                <li class="nav-item me-3">
                    <a class="nav-link text-white position-relative" href="${pageContext.request.contextPath}/cart">
                        <i class="fas fa-shopping-cart fa-lg"></i>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                <%-- Tính tổng số lượng sản phẩm trong giỏ hàng từ List<Cart> --%>
                                <c:set var="cartItemCount" value="0"/>
                                <c:if test="${not empty requestScope.cartItems}">
                                    <c:forEach var="item" items="${requestScope.cartItems}">
                                        <c:set var="cartItemCount" value="${cartItemCount + item.quantity}"/>
                                    </c:forEach>
                                </c:if>
                                ${cartItemCount}
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

<div class="container py-4">
    <h2 class="mb-4 text-center">Giỏ Hàng Của Bạn</h2>

    <%-- Hiển thị thông báo thành công hoặc lỗi --%>
    <c:if test="${param.success != null}">
        <div class="message-box success-message">
            <c:choose>
                <c:when test="${param.success eq 'added'}">
                    <i class="fas fa-check-circle me-2"></i> Sản phẩm đã được thêm vào giỏ hàng thành công!
                </c:when>
                <c:when test="${param.success eq 'updated'}">
                    <i class="fas fa-check-circle me-2"></i> Giỏ hàng đã được cập nhật thành công!
                </c:when>
                <c:when test="${param.success eq 'removed'}">
                    <i class="fas fa-check-circle me-2"></i> Sản phẩm đã được xóa khỏi giỏ hàng!
                </c:when>
                <c:when test="${param.success eq 'order_placed'}">
                    <i class="fas fa-check-circle me-2"></i> Đơn hàng của bạn đã được đặt thành công!
                </c:when>
            </c:choose>
        </div>
    </c:if>

    <c:if test="${param.error != null}">
        <div class="message-box error-message">
            <i class="fas fa-exclamation-triangle me-2"></i>
            <c:choose>
                <c:when test="${param.error eq 'invalid_quantity'}">
                    Số lượng không hợp lệ. Vui lòng nhập số lượng lớn hơn 0 và không vượt quá số lượng tồn kho.
                </c:when>
                <c:when test="${param.error eq 'update_failed'}">
                    Không thể cập nhật giỏ hàng. Vui lòng thử lại.
                </c:when>
                <c:when test="${param.error eq 'remove_failed'}">
                    Không thể xóa sản phẩm khỏi giỏ hàng. Vui lòng thử lại.
                </c:when>
                <c:when test="${param.error eq 'invalid_input'}">
                    Dữ liệu đầu vào không hợp lệ.
                </c:when>
                <c:when test="${param.error eq 'empty_cart_checkout'}">
                    Giỏ hàng của bạn đang trống. Không thể tiến hành thanh toán.
                </c:when>
                <c:when test="${param.error eq 'order_failed'}">
                    Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.
                </c:when>
                <c:when test="${param.error eq 'stock_exceeded'}">
                    Số lượng sản phẩm trong giỏ hàng vượt quá số lượng tồn kho. Vui lòng điều chỉnh lại.
                </c:when>
                <c:when test="${param.error eq 'cart_item_not_found'}">
                    Một số sản phẩm trong giỏ hàng không còn tồn tại hoặc đã bị thay đổi. Vui lòng kiểm tra lại giỏ hàng.
                </c:when>
                <c:otherwise>
                    Đã xảy ra lỗi. Vui lòng thử lại.
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <c:set var="totalCartPrice" value="0"/>

    <c:choose>
        <c:when test="${not empty carts and carts.size() > 0}">
            <div class="row">
                <div class="col-lg-8">
                    <div class="card card-cart mb-4">
                        <div class="card-body">
                            <h5 class="card-title mb-4">Sản phẩm trong giỏ hàng</h5>
                            <div class="list-group list-group-flush">
                                <c:forEach var="cartItem" items="${carts}">
                                    <c:set var="product" value="${cartItem.product}"/>

                                    <c:set var="itemTotalPrice" value="${cartItem.priceTotal}"/>
                                    <c:set var="totalCartPrice" value="${totalCartPrice + itemTotalPrice}"/>

                                    <div class="list-group-item d-flex align-items-center py-3">
                                        <img src="${product.image}" class="cart-item-img me-3" alt="${product.name}">
                                        <div class="flex-grow-1">
                                            <h6 class="mb-1"><a href="${pageContext.request.contextPath}/product/detail?id=${product.id}" class="text-decoration-none text-dark">${product.name}</a></h6>
                                            <p class="mb-1 text-muted small">Đơn giá: <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/></p>
                                        </div>
                                        <div class="d-flex align-items-center me-3">
                                            <form action="${pageContext.request.contextPath}/cart/update" method="post" class="d-flex align-items-center me-2">
                                                <input type="hidden" name="cartId" value="${cartItem.id}">
                                                <input type="number" name="quantity" value="${cartItem.quantity}" min="1" max="${product.quantity}" class="form-control quantity-input" onchange="this.form.submit()">
                                            </form>
                                        </div>
                                        <div class="text-nowrap me-3 fw-bold text-danger">
                                            <fmt:formatNumber value="${itemTotalPrice}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                            <input type="hidden" name="cartId" value="${cartItem.id}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?');">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card card-cart">
                        <div class="card-body">
                            <h5 class="card-title mb-4">Tổng cộng</h5>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    Tổng tiền hàng:
                                    <span class="fw-bold text-dark">
                                        <fmt:formatNumber value="${totalCartPrice}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                                    </span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center text-success">
                                    Phí vận chuyển:
                                    <span class="fw-bold">Miễn phí</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-center fs-5 text-primary">
                                    Tổng thanh toán:
                                    <span class="fw-bold">
                                        <fmt:formatNumber value="${totalCartPrice}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                                    </span>
                                </li>
                            </ul>
                            <button class="btn btn-primary btn-lg w-100 mt-4" data-bs-toggle="collapse" data-bs-target="#checkoutForm" aria-expanded="false" aria-controls="checkoutForm">
                                Tiến hành thanh toán
                            </button>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary btn-lg w-100 mt-2">Tiếp tục mua sắm</a>
                        </div>
                    </div>

                    <div class="collapse mt-4" id="checkoutForm">
                        <div class="card card-cart">
                            <div class="card-body">
                                <h5 class="card-title mb-4">Thông tin giao hàng và thanh toán</h5>
                                <form action="${pageContext.request.contextPath}/checkout" method="post">
                                    <div class="mb-3">
                                        <label for="customerName" class="form-label">Họ và tên người nhận</label>
                                        <input type="text" class="form-control" id="customerName" name="name" required
                                               value="${user != null ? user.name : ''}">
                                    </div>
                                    <div class="mb-3">
                                        <label for="address" class="form-label">Địa chỉ giao hàng</label>
                                        <input type="text" class="form-control" id="address" name="address" required
                                               value="${user != null ? user.address : ''}">
                                    </div>
                                    <div class="mb-3">
                                        <label for="phone" class="form-label">Số điện thoại</label>
                                        <input type="tel" class="form-control" id="phone" name="phone" required
                                               value="${user != null ? user.phone : ''}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Phương thức thanh toán</label>
                                        <div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="paymentMethod" id="paymentCod" value="1" checked>
                                                <label class="form-check-label" for="paymentCod">
                                                    Thanh toán khi nhận hàng (COD)
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="paymentMethod" id="paymentBank" value="2">
                                                <label class="form-check-label" for="paymentBank">
                                                    Chuyển khoản ngân hàng
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="hidden" name="totalMoney" value="${totalCartPrice}">
                                    <button type="submit" class="btn btn-success btn-lg w-100">Xác nhận đặt hàng</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center" role="alert">
                Giỏ hàng của bạn đang trống. <a href="${pageContext.request.contextPath}/products">Bắt đầu mua sắm ngay!</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>