package org.app.ql_ban_hang.entities;

public class User {
    private int id;
    private String username;
    private String password;
    private String rePassword;
    private String phoneNumber;
    private String email;
    private String address;
    private String role;

    public User(String username, String password, String rePassword, String phoneNumber, String email, String address) {
        this.username = username;
        this.password = password;
        this.rePassword = rePassword;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.role = "user";
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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