package com.example.elearningproject;


import com.example.elearningproject.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjouterUnCoursController {
    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField levelField;

    @FXML
    private Label statusLabel;

    private File pdfFile;
    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        System.out.println(user.toString());
    }
    @FXML
    private void uploadPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        pdfFile = fileChooser.showOpenDialog(null);
    }

    @FXML
    private void addCourse() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String level = levelField.getText();

        if (title.isEmpty() || description.isEmpty() || level.isEmpty() || pdfFile == null) {
            statusLabel.setText("Please fill in all fields and upload a PDF file.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String query = "INSERT INTO cours (titre, description, niveau, fichierPdf,user_id) VALUES (?, ?, ?, ?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, title);
                statement.setString(2, description);
                statement.setString(3, level);

                FileInputStream inputStream = new FileInputStream(pdfFile);
                statement.setBinaryStream(4, inputStream);
                statement.setInt(5, this.user.getId());
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    statusLabel.setText("Course added successfully!");
                } else {
                    statusLabel.setText("Failed to add course.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error occurred while adding course.");
        }
    }
}
