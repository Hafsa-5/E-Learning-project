package com.example.elearningproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AjouterExerciceController {

    private int coursId;
    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    private int profId;
    public void setProfId(int profId) {
        this.profId = profId;
    }

    public void initialize() {
        System.out.println(coursId);
    }

    @FXML
    private TextField questionTextField;

    @FXML
    private TextArea reponseTextArea;

    public void ajouterExercice() {
        String question = questionTextField.getText();
        String reponse = reponseTextArea.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sqlExercice = "INSERT INTO exercice (question, reponse, cours_id, professeur_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatementExercice = connection.prepareStatement(sqlExercice, Statement.RETURN_GENERATED_KEYS);
            preparedStatementExercice.setString(1, question);
            preparedStatementExercice.setString(2, reponse);
            preparedStatementExercice.setInt(3, coursId);
            preparedStatementExercice.setInt(4, profId);
            int rowsUpdatedExercice = preparedStatementExercice.executeUpdate();

            if (rowsUpdatedExercice > 0) {
                System.out.println("L'exercice a été ajouté avec succès.");

                ResultSet generatedKeys = preparedStatementExercice.getGeneratedKeys();
                int idExercice = -1;
                if (generatedKeys.next()) {
                    idExercice = generatedKeys.getInt(1);
                }

                if (idExercice != -1) {
                    List<Integer> idEtudiants = new ArrayList<>();
                    String sqlEtudiants = "SELECT id FROM users WHERE role = 'etudiant'";
                    PreparedStatement preparedStatementEtudiants = connection.prepareStatement(sqlEtudiants);
                    ResultSet resultSetEtudiants = preparedStatementEtudiants.executeQuery();
                    while (resultSetEtudiants.next()) {
                        int idEtudiant = resultSetEtudiants.getInt("id");
                        idEtudiants.add(idEtudiant);
                    }

                    String sqlReponseEtudiant = "INSERT INTO reponseEtudiant (reponse, idExercice, idEtudiant) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatementReponseEtudiant = connection.prepareStatement(sqlReponseEtudiant);
                    for (int idEtudiant : idEtudiants) {
                        preparedStatementReponseEtudiant.setString(1, "repondre a cette question");
                        preparedStatementReponseEtudiant.setInt(2, idExercice);
                        preparedStatementReponseEtudiant.setInt(3, idEtudiant);
                        preparedStatementReponseEtudiant.executeUpdate();
                    }

                    System.out.println("Les réponses des étudiants ont été ajoutées avec succès.");

                    Stage stage = (Stage) questionTextField.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Impossible de récupérer l'ID de l'exercice.");
                }
            } else {
                System.out.println("Impossible d'ajouter l'exercice.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
// this class is a controller responsible for adding exercices in our project ,here a breakdown of what the code does look above
