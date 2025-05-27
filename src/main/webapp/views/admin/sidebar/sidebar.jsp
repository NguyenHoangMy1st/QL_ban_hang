<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 5/24/2025
  Time: 6:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    /* Basic Sidebar Styling */
    .sidebar {
        width: 250px;
        background-color: #343a40; /* Dark background like Bootstrap's dark navbar */
        color: #f8f9fa; /* Light text color */
        padding-top: 20px;
        position: fixed; /* Fixed position */
        height: 100%; /* Full height */
        left: 0;
        top: 0;
        overflow-x: hidden; /* Disable horizontal scroll */
        box-shadow: 2px 0 5px rgba(0,0,0,0.2);
        z-index: 1000; /* Ensure it's above other content */
    }

    .sidebar .nav-link {
        color: #adb5bd; /* Lighter text for links */
        padding: 15px 20px;
        display: block;
        transition: background-color 0.3s ease;
    }

    .sidebar .nav-link:hover {
        background-color: #495057; /* Darker on hover */
        color: #ffffff; /* White text on hover */
        text-decoration: none; /* Remove underline */
    }

    .sidebar .nav-link.active {
        background-color: #007bff; /* Primary color for active link */
        color: #ffffff;
        font-weight: bold;
    }

    .sidebar h4 {
        color: #ffffff;
        text-align: center;
        margin-bottom: 30px;
        padding-bottom: 10px;
        border-bottom: 1px solid #495057;
    }
</style>

<div class="sidebar">
    <h4>Admin Panel</h4>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link <c:if test="${requestScope['currentPage'] == 'dashboard'}">active</c:if>" href="/admin/dashboard">
                Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <c:if test="${requestScope['currentPage'] == 'products'}">active</c:if>" href="/admin/products">
                Quản lý Sản phẩm
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <c:if test="${requestScope['currentPage'] == 'categories'}">active</c:if>" href="/admin/categories">
                Quản lý Danh mục
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <c:if test="${requestScope['currentPage'] == 'users'}">active</c:if>" href="/admin/users">
                Quản lý Người dùng
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link logout" href="/auth/logout">
                Đăng xuất
            </a>
        </li>
    </ul>
</div>
