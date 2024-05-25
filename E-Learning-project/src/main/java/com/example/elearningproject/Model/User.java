package com.example.elearningproject.Model;

public class User {
    private int id;
    private String email;
    private String password;
    private String role;

    public User(int id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    @Override
    public String toString() {
        return email;
    }

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String display() {
        return "User{" +
                "id=" + this.getId() +
                ", email='" + this.getEmail() + '\'' +
                ", password='" + this.getPassword()  + '\'' +
                ", role='" + this.getRole() + '\'' +
                '}';
    }
}
