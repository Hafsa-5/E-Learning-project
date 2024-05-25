package com.example.elearningproject;

import com.example.elearningproject.Model.Lecon;
import com.example.elearningproject.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjouterLeconController {

    private int coursId;

    private File pdfFile;

    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    public void initialize() {
        System.out.println(coursId);
    }

    @FXML
    private void uploadPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        pdfFile = fileChooser.showOpenDialog(null);
    }
    @FXML
    private TextField titreTextField;

    @FXML
    private TextArea contenuTextArea;

    @FXML
    private void ajouterLecon() {
        String titre = titreTextField.getText();
        String contenu = contenuTextArea.getText();



        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String query = "INSERT INTO lecons (titre, contenu, cours_id, fichierPdf) VALUES (?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, titre);
            preparedStatement.setString(2, contenu);
            preparedStatement.setInt(3, this.coursId);
            FileInputStream inputStream = new FileInputStream(pdfFile);
            preparedStatement.setBinaryStream(4, inputStream);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Leçon ajoutée : " + titre);
            } else {
                System.out.println("Échec de l'ajout de la leçon.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
