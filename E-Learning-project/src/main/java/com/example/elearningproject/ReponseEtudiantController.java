package com.example.elearningproject;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReponseEtudiantController {

    private int exerciceId;
    public void setExerciceId(int exerciceId) {
        this.exerciceId = exerciceId;
    }

    private int studentId;
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    private ExercicesCoursEtudiantController exercicesCoursEtudiantController;

    public void setExercicesCoursEtudiantController(ExercicesCoursEtudiantController exercicesCoursEtudiantController) {
        this.exercicesCoursEtudiantController = exercicesCoursEtudiantController;
    }


    public void initialize() {
        System.out.println(studentId);
    }
    @FXML
    private TextArea reponseTextArea;

    public void enregistrerReponse() {
        String reponse = reponseTextArea.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "UPDATE reponseEtudiant SET reponse = ? WHERE idExercice = ? AND idEtudiant = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, reponse);
            preparedStatement.setInt(2, exerciceId);
            preparedStatement.setInt(3, studentId);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Réponse de l'étudiant enregistrée avec succès.");
                closeWindow();
                exercicesCoursEtudiantController.exercicesTable.getItems().clear();
                exercicesCoursEtudiantController.afficherExercicesDuCours();
            } else {
                System.out.println("Aucune réponse n'a été enregistrée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de la réponse de l'étudiant : " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) reponseTextArea.getScene().getWindow();
        stage.close();
    }

}
