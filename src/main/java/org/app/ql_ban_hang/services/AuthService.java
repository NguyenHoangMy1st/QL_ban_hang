package org.app.ql_ban_hang.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.entities.User;
import org.app.ql_ban_hang.models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    public static boolean checkAccount(HttpServletRequest request, HttpServletResponse response) {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            // For example, check the username and password against a database
            ResultSet resultSet = UserModel.getUserByEmailPassword(email, password);
            //Luu lai id va name cua user login vao session
            HttpSession session = request.getSession(true);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString( "name");
                String role = resultSet.getString("role");

                System.out.println("Login success: " + resultSet.getString("name"));

                session.setAttribute("idUserLogin", id);
                session.setAttribute("nameUserLogin", name);
                session.setAttribute("roleUserLogin", role);
                return true;
            }else {
                request.setAttribute("errorMessage", "Email hoặc mật khẩu không đúng.");
                System.out.println("Login failed: Invalid email or password.");
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkAccount: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi kiểm tra tài khoản.");
            return false;
        }
    }

    public static boolean registerAccount(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm_password");

            // 1. Xác thực đầu vào
            if (name == null || name.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc (Họ tên, Email, Mật khẩu, Xác nhận mật khẩu).");
                return false;
            }

            // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp không
            if (!password.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp. Vui lòng nhập lại.");
                return false;
            }

            // 2. Kiểm tra email đã tồn tại trong cơ sở dữ liệu chưa
            if (UserModel.checkEmailExists(email)) {
                request.setAttribute("errorMessage", "Email này đã được sử dụng. Vui lòng chọn email khác.");
                return false;
            }

            // 3. Bve mật khẩu

            User newUser = new User(name, password, email, phone, address);

            System.out.println(newUser);
            UserModel.registerUser(newUser);
//            response.sendRedirect("/auth/login");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi đăng ký tài khoản: " + e.getMessage()); // In lỗi ra console
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi đăng ký tài khoản. Vui lòng thử lại sau.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi không xác định khi đăng ký tài khoản: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không xác định khi đăng ký tài khoản. Vui lòng thử lại.");
            return false;
        }
    }
}
