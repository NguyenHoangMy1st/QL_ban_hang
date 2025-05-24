package org.app.ql_ban_hang.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.app.ql_ban_hang.models.DatabaseModel;
import org.app.ql_ban_hang.services.AuthService;

import java.io.IOException;

@WebServlet(name = "AuthController", value = {"/auth/*"})
public class AuthController extends BaseController {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        switch (path) {
            case "/login":
                renderView("/views/auth/login.jsp", req, resp);
                break;
            case "/register":
                // Handle registration
                break;
            case "/logout":
                req.getSession().invalidate();
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
                // Handle login
                if (AuthService.checkAccount(req, resp)){
                    String role = (String) req.getSession().getAttribute("roleUserLogin");
                    if("admin".equals(role)){
                        resp.sendRedirect("/admin/dashboard");
                    }else {
                        resp.sendRedirect("/home");
                    }
                }else {
                    resp.sendRedirect("/auth/login?error=1");
                }
                break;
            case "/register":
                // Handle registration
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