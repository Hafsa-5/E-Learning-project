package com.example.elearningproject;

import com.example.elearningproject.Model.Exercice;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ExercicesCoursController {
    @FXML
    private TableView<Exercice> exercicesTable;

    @FXML
    private TableColumn<Exercice, Integer> idColumn;

    @FXML
    private TableColumn<Exercice, String> questionColumn;

    @FXML
    private TableColumn<Exercice, String> reponseColumn;

    private int coursId;
    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    private int profId;
    public void setProfId(int profId) {
        this.profId = profId;
    }


    public void initialize() {
        afficherExercicesDuCours();
    }

    private void afficherExercicesDuCours() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT * FROM exercice WHERE cours_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coursId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String question = resultSet.getString("question");
                String reponse = resultSet.getString("reponse");
                Exercice exercice = new Exercice(id, question, reponse,profId,coursId);
                exercicesTable.getItems().add(exercice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}