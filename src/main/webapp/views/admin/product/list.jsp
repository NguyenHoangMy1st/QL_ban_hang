<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/22/2025
  Time: 7:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="productList" value="${requestScope['productList']}" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Quản Lý Sản Phẩm</title>

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
        .table img {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 5px;
        }

        .action-buttons button,
        .action-buttons a {
            margin-right: 5px; /* Add margin for spacing between buttons */
            margin-bottom: 5px; /* Add margin for spacing between buttons vertically on smaller screens */
        }
        .btn-add-product {
            margin-bottom: 20px;
        }

        /* Styles for the detail modal */
        #productDetailModal .modal-body img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            margin-bottom: 15px;
        }
        #productDetailModal .detail-row {
            margin-bottom: 10px;
        }
        #productDetailModal .detail-row strong {
            display: inline-block;
            width: 120px; /* Align labels */
        }
    </style>
</head>
<body>
<jsp:include page="/views/admin/sidebar/sidebar.jsp" />

<div class="main-content">
    <div class="content-container">
        <h2>Quản Lý Sản Phẩm</h2>

        <a href="/admin/products/create" class="btn btn-primary mb-3 btn-add-product">Thêm Sản Phẩm Mới</a>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark bg-blue-300 bg-opacity-50">
                <tr>
                    <th>ID</th>
                    <th>Ảnh</th>
                    <th>Tên Sản Phẩm</th>
                    <th>Giá (VNĐ)</th>
                    <th>Mô Tả</th>
                    <th>Số lượng</th>
                    <th>Danh Mục</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${productList}">
                    <tr>
                        <td><c:out value="${product.getId()}"/></td>
<%--                        <td><img src="${product.getImage()}" alt="${product.getName()}" class="img-fluid"></td>--%>
                        <td><c:out value="${product.getName()}"/></td>
                        <td>
                            <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" maxFractionDigits="0"/>
                        </td>
                        <td><c:out value="${product.getDescription()}"/></td>
                        <td><c:out value="${product.getQuantity()}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${product.getCategory() != null}">
                                    <c:out value="${product.getCategory().getName()}"/>
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="action-buttons">
                            <button type="button" class="btn btn-info btn-sm" title="Xem chi tiết"
                                    data-bs-toggle="modal" data-bs-target="#productDetailModal"
                                    data-product-id="<c:out value="${product.getId()}"/>">
                                <i class="fas fa-eye"></i>
                            </button>
                            <a href="/admin/products/edit?id=<c:out value="${product.getId()}"/>" class="btn btn-warning btn-sm">Sửa</a>
                            <button type="button" class="btn btn-danger btn-sm" onclick="confirmDelete('<c:out value="${product.getId()}"/>')">Xóa</button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty productList}">
                    <tr>
                        <td colspan="8" class="text-center">Không có sản phẩm nào.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="productDetailModal" tabindex="-1" aria-labelledby="productDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="productDetailModalLabel">Chi Tiết Sản Phẩm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="modal-product-content">
                    <p class="text-center">Đang tải dữ liệu...</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
    function confirmDelete(productId) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) {
            window.location.href = "/admin/products/delete?id=" + productId;
        }
    }

    // JavaScript to handle loading product details into the modal
    document.addEventListener('DOMContentLoaded', function() {
        const productDetailModal = document.getElementById('productDetailModal');
        productDetailModal.addEventListener('show.bs.modal', function (event) {
            // Button that triggered the modal
            const button = event.relatedTarget;
            // Extract info from data-product-id attributes
            const productId = button.getAttribute('data-product-id');
            console.log("ProductId được lấy từ button:", productId);

            const modalBodyContent = productDetailModal.querySelector('#modal-product-content');

            modalBodyContent.innerHTML = '<p class="text-center">Đang tải dữ liệu...</p>'; // Reset content

            // Make an AJAX call to your servlet to get product details
            $.ajax({
                url: '/admin/products/detail', // This URL will be handled by your AdminController's doGet
                type: 'GET',
                data: { id: productId },
                success: function(data) {

                    if (data) {
                        console.log("Data", data);

                        const formattedPrice = new Intl.NumberFormat('vi-VN', {
                            style: 'currency', // Có thể dùng 'decimal' nếu bạn chỉ muốn số
                            currency: 'VND',   // Đảm bảo đơn vị tiền tệ là VNĐ
                            minimumFractionDigits: 0 // Không hiển thị số lẻ nếu là số nguyên
                        }).format(parseFloat(data.price)); // Đảm bảo chuyển đổi sang số

                        let detailHtml = `
                            <div class="text-center mb-4">
                            <%--<img src="${product.image || 'https://via.placeholder.com/200?text=No+Image'}" alt="${product.name}" class="product-image">--%>
                            </div>
                            <div class="detail-row"><strong>ID:</strong> <span>\${data.id}</span></div>
                            <div class="detail-row"><strong>Tên:</strong> <span>\${data.name}</span></div>
                            <div class="detail-row"><strong>Giá:</strong> <span>\${formattedPrice}</span></div>
                            <div class="detail-row"><strong>Mô Tả:</strong> <span>\${data.description || 'Không có mô tả'}</span></div>
                            <div class="detail-row"><strong>Số Lượng:</strong> <span>\${data.quantity}</span></div>
                            <div class="detail-row"><strong>Danh Mục:</strong> <span>\${data.category ? data.category.name : 'N/A'}</span></div>
                            `;
                        modalBodyContent.innerHTML = detailHtml
                            .replace(/\${data\.id}/g, data.id)
                            .replace(/\${data\.name}/g, data.name)
                            .replace(/\${data\.description}/g, data.description || 'Không có mô tả')
                            .replace(/\${data\.quantity}/g, data.quantity)
                            .replace(/\${data\.category\.name}/g, data.category ? data.category.name : 'N/A');
                    } else {
                        modalBodyContent.innerHTML = '<p class="text-danger text-center">Không tìm thấy thông tin sản phẩm này.</p>';
                    }
                },
                error: function(xhr, status, error) {
                    console.error("AJAX Error: ", status, error);
                    modalBodyContent.innerHTML = '<p class="text-danger text-center">Đã xảy ra lỗi khi tải dữ liệu sản phẩm.</p>';
                }
            });
        });
    });
</script>
</body>
</html>