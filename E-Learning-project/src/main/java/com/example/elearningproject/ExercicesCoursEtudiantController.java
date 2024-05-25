package com.example.elearningproject;

import com.example.elearningproject.Model.Cours;
import com.example.elearningproject.Model.Exercice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ExercicesCoursEtudiantController {
    @FXML
    public TableView<Exercice> exercicesTable;

    @FXML
    private TableColumn<Exercice, Integer> idColumn;

    @FXML
    private TableColumn<Exercice, String> questionColumn;

    @FXML
    private TableColumn<Exercice, String> reponseColumn;
    @FXML
    private Button repondreButton;

    private int coursId;
    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    private int studentId;
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


    public void initialize() {
        afficherExercicesDuCours();
        exercicesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String reponse = newSelection.getReponse();

                // Désactiver le bouton si la réponse n'est pas "Répondre à cette question"
                if (!"repondre a cette question".equals(reponse)) {
                    repondreButton.setDisable(true);
                } else {
                    repondreButton.setDisable(false); // Activer le bouton si la réponse est "Répondre à cette question"
                }
            }
        });
    }

    public void afficherExercicesDuCours() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT e.id, e.question, r.reponse " +
                    "FROM exercice e " +
                    "JOIN reponseetudiant r ON e.id = r.idExercice " +
                    "WHERE e.cours_id = ? and r.idEtudiant = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coursId);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String question = resultSet.getString("question");
                String reponse = resultSet.getString("reponse");
                Exercice exercice = new Exercice(id, question, reponse, studentId, coursId);
                exercicesTable.getItems().add(exercice);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void OuvrirFenetreReponseEtudiant(ActionEvent event) {
        Exercice selectedItem = exercicesTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReponseEtudiant.fxml"));
            Parent root = loader.load();
            ReponseEtudiantController reponseEtudiantController = loader.getController();
            reponseEtudiantController.setExerciceId(selectedItem.getId());
            reponseEtudiantController.setStudentId(studentId);
            reponseEtudiantController.setExercicesCoursEtudiantController(this);
            reponseEtudiantController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}