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
  <title>Login</title>
  <link
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
          rel="stylesheet"
  />
  <link
          href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"
          rel="stylesheet"
  />
  <link
          href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/8.0.0/mdb.min.css"
          rel="stylesheet"
  />
</head>
<body>
<div class="container d-flex justify-content-center">
  <div class="col-md-6">
    <ul class="nav nav-pills nav-justified mb-3" id="ex1" role="tablist">
      <li class="nav-item" role="presentation">
        <a class="nav-link active" id="tab-login" href="#pills-login" role="tab"
           aria-controls="pills-login" aria-selected="true">Login</a>
      </li>
    </ul>
    <div class="tab-content">
      <div class="tab-pane fade show active" id="pills-login" role="tabpanel" aria-labelledby="tab-login">
        <form action="${pageContext.request.contextPath}/auth/login" method="post">
          <div class="text-center mb-3">
            <p>Sign in with:</p>
            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
              <i class="fab fa-facebook-f"></i>
            </button>

            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
              <i class="fab fa-google"></i>
            </button>

            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
              <i class="fab fa-twitter"></i>
            </button>

            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
              <i class="fab fa-github"></i>
            </button>
          </div>

          <p class="text-center">or:</p>

          <c:if test="${not empty error}">
            <div class="alert alert-danger text-center" role="alert">
              <c:out value="${error}" />
            </div>
          </c:if>

          <div data-mdb-input-init class="form-outline mb-4">
            <input type="email" name="email" id="loginName" class="form-control" />
            <label class="form-label" for="loginName">Email or username</label>
          </div>

          <div data-mdb-input-init class="form-outline mb-4">
            <input type="password" name="password" id="loginPassword" class="form-control" />
            <label class="form-label" for="loginPassword">Password</label>
          </div>

          <div class="row mb-4">
            <div class="col-md-6 d-flex justify-content-center">
              <div class="form-check mb-3 mb-md-0">
                <input class="form-check-input" type="checkbox" value="" id="loginCheck" checked />
                <label class="form-check-label" for="loginCheck"> Remember me </label>
              </div>
            </div>

            <div class="col-md-6 d-flex justify-content-center">
              <a href="#!">Forgot password?</a>
            </div>
          </div>

          <button type="submit" class="btn btn-primary mb-4">Sign in</button>

          <div class="text-center">
            <p>Not a member? <a href="${pageContext.request.contextPath}/auth/register">Register</a></p>
          </div>
        </form>
      </div>
    </div>
  </div>

</div>
<script
        type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/8.0.0/mdb.umd.min.js"
></script>
</body>
</html>