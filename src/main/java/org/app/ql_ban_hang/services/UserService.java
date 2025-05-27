package org.app.ql_ban_hang.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.app.ql_ban_hang.entities.User;
import org.app.ql_ban_hang.models.UserModel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    public static List<User> getUsers() {
        try{
            ResultSet resultSet = UserModel.getAllUsers();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                int uID = resultSet.getInt("id");
                String uName = resultSet.getString("name");
                String uEmail = resultSet.getString("email");
                String uPhone = resultSet.getString("phone");
                String uAddress = resultSet.getString("address");

                User u = new User(uName, uEmail, uPhone, uAddress);
                u.setId(uID);
                users.add(u);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getUsers()");
        }
        return null;
    }
    public static void createUser(HttpServletRequest request, HttpServletResponse response) {
        try{
            String uName = request.getParameter("name");
            String uEmail = request.getParameter("email");
            String uPassword = request.getParameter("password");
            String uPhone = request.getParameter("phone");
            String uAddress = request.getParameter("address");
            User user = new User(uName, uPassword, uEmail, uPhone, uAddress);
            UserModel.create(user);
            response.sendRedirect("/admin/users");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in createUser()");
        }
    }
    public static void updateUser(HttpServletRequest request, HttpServletResponse response) {
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            String uName = request.getParameter("name");
            String uEmail = request.getParameter("email");
            String uPhone = request.getParameter("phone");
            String uAddress = request.getParameter("address");
            User user = new User(uName, uEmail, uPhone, uAddress);
            user.setId(id);
            UserModel.update(user);
            response.sendRedirect("/admin/users");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in updateUser()");
        }
    }
    public static User getUserById(HttpServletRequest request, HttpServletResponse response) {
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            ResultSet resultSet = UserModel.findById(id);
            User userUpdate = null;
            while (resultSet.next()) {
                int uID = resultSet.getInt("id");
                String uName = resultSet.getString("name");
                String uEmail = resultSet.getString("email");
                String uPhone = resultSet.getString("phone");
                String uAddress = resultSet.getString("address");
                userUpdate = new User(uName, uEmail, uPhone, uAddress);
                userUpdate.setId(id);
            }
            return userUpdate;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getUserById()");
        }
        return null;
    }

}
