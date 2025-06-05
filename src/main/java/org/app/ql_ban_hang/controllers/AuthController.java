package org.app.ql_ban_hang.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.models.DatabaseModel;
import org.app.ql_ban_hang.services.AuthService;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "AuthController", value = {"/auth/*"})
public class AuthController extends BaseController {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addNoCacheHeaders(resp);
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("idUserLogin") != null);
        String role = (isLoggedIn) ? (String) session.getAttribute("roleUserLogin") : null;

        switch (path) {
            case "/login":
                if (isLoggedIn) {
                    if ("admin".equals(role)) {
                        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/home");
                    }
                    return;
                } else {
                    renderView("/views/auth/login.jsp", req, resp);
                }
                break;
            case "/register":
                if (isLoggedIn) {
                    if ("admin".equals(role)) {
                        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/home");
                    }
                    return;
                } else {
                    renderView("/views/auth/register.jsp", req, resp);
                }
                break;
            case "/logout":
                if (session != null) {
                    session.invalidate();
                }
                resp.sendRedirect(req.getContextPath() + "/auth/login");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/login":
                if (AuthService.checkAccount(req, resp)){
                    String role = (String) req.getSession().getAttribute("roleUserLogin");
                    addNoCacheHeaders(resp);
                    if("admin".equals(role)){
                        resp.sendRedirect("/admin/dashboard");
                    }else {
                        resp.sendRedirect("/home");
                    }
                }else {
                    req.setAttribute("error", "Email hoặc mật khẩu không đúng.");
                    resp.sendRedirect("/auth/login?error=1");
                }
                break;
            case "/register":
                if (AuthService.registerAccount(req, resp)) {
                    addNoCacheHeaders(resp);
                    resp.sendRedirect(req.getContextPath() + "/auth/login?success=register");
                } else {
                    String errorMessage = (String) req.getAttribute("errorMessage");
                    if (errorMessage == null || errorMessage.isEmpty()) {
                        errorMessage = "Đã xảy ra lỗi không xác định khi đăng ký.";
                    }
                    String name = req.getParameter("name") != null ? URLEncoder.encode(req.getParameter("name"), "UTF-8") : "";
                    String email = req.getParameter("email") != null ? URLEncoder.encode(req.getParameter("email"), "UTF-8") : "";
                    String phone = req.getParameter("phone") != null ? URLEncoder.encode(req.getParameter("phone"), "UTF-8") : "";
                    String address = req.getParameter("address") != null ? URLEncoder.encode(req.getParameter("address"), "UTF-8") : "";

                    resp.sendRedirect(req.getContextPath() + "/auth/register?error=true&message=" + URLEncoder.encode(errorMessage, "UTF-8")
                            + "&name=" + name
                            + "&email=" + email
                            + "&phone=" + phone
                            + "&address=" + address);
                }
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}