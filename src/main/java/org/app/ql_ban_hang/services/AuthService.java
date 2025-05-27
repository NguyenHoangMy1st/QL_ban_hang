package org.app.ql_ban_hang.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.app.ql_ban_hang.models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    public static boolean checkAccount(HttpServletRequest request, HttpServletResponse response) {
        // Implement login logic here
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            // For example, check the username and password against a database
            ResultSet resultSet = UserModel.getUserByEmailPassword(email, password);
            //Luu lai id va name cua user login vao session
            HttpSession session = request.getSession(true);
            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString( "name");
                String role = resultSet.getString("role");

                System.out.println("Login success: " + resultSet.getString("name"));

                session.setAttribute("idUserLogin", id);
                session.setAttribute("nameUserLogin", name);
                session.setAttribute("roleUserLogin", role);

            }
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
