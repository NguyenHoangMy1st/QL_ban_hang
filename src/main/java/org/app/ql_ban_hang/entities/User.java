package org.app.ql_ban_hang.entities;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String role;

    public User(){
        this.role = "user";
    }
    public User(int id) {
        this.id = id;
        this.role = "user";
    }
    // Constructor khi không có password (ít dùng cho đăng ký)
    public User(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = "user";
    }

    // Constructor đầy đủ cho việc đăng ký
    public User(String name, String password, String email, String phone, String address) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = "user";
    }
    // Constructor đầy đủ bao gồm ID và Role (khi đọc từ DB)
    public User(int id, String name, String password, String email, String phone, String address, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public User(int id, String name, String email, String phone, String address, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}