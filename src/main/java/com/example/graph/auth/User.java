package com.example.graph.auth;

public class User {
    private final int id;
    private  String username;
    private  String password;
    private  String email;
    private  String phone;

    public User(int id, String username, String password,String email,String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email=email;
        this.phone=phone;

    }
    private User() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.email = "";
        this.phone = "";
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public void setUsername(String username ) { this.username=username; }
    public void setPassword(String password) { this.password=password; }
    public void setEmail(String email) {
        this.email= email;
    }
    public void setPhone(String phone) {
        this.phone= phone;
    }
}
