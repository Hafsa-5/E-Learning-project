package com.example.elearningproject;

import com.example.elearningproject.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfilController {


    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Label statusLabel;

    private User user = new User(0,"","","");

    public void setUser(User user) {
        this.user = user;
    }


    @FXML
    public void initialize() {
        emailLabel.setText(this.user.getEmail());
        roleLabel.setText(this.user.getRole());
        changePasswordButton.setOnAction(event -> changePassword());
    }

    private void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setText("New password and confirm password do not match!");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/elearning";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Assuming you have a table named "users" with columns "id" and "password"
            String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                int userId = this.user.getId();
                stmt.setString(1, newPassword);
                stmt.setInt(2, userId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel.setText("Password changed successfully!");
                } else {
                    statusLabel.setText("Failed to change password. Please try again.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
