package com.example.elearningproject;

import com.example.elearningproject.Model.ReponseEtudiant;
import com.example.elearningproject.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ResultatEtudiantController {

    private User user = new User();
    public void setUser(User user) {
        this.user = user;
    }




    @FXML
    private ComboBox<String> coursComboBox;

    @FXML
    public TableView<ReponseEtudiant> reponsesTable;

    @FXML
    private TableColumn<ReponseEtudiant, String> questionColumn;

    @FXML
    private TableColumn<ReponseEtudiant, String> titreColumn;

    @FXML
    private TableColumn<ReponseEtudiant, String> reponseColumn;

    @FXML
    private TableColumn<ReponseEtudiant, Integer> resultatColumn;

    public void initialize() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        resultatColumn.setCellValueFactory(new PropertyValueFactory<>("resultat"));

        populateCoursComboBox();
    }



    private void populateCoursComboBox() {
        coursComboBox.getItems().clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT DISTINCT c.titre " +
                    "FROM cours c " +
                    "INNER JOIN exercice e ON c.id = e.cours_id ";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String titre = resultSet.getString("titre");
                coursComboBox.getItems().add(titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void afficherReponses() {
        String selectedCoursTitre = coursComboBox.getValue();

        if (selectedCoursTitre != null && !selectedCoursTitre.isEmpty()) {
            reponsesTable.getItems().clear();

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String userIdQuery = "SELECT id FROM users WHERE email = ?";
                PreparedStatement userIdStatement = connection.prepareStatement(userIdQuery);
                userIdStatement.setString(1, this.user.getEmail());
                ResultSet userIdResult = userIdStatement.executeQuery();

                if (userIdResult.next()) {
                    int selectedUserId = userIdResult.getInt("id");

                    String coursIdQuery = "SELECT id FROM cours WHERE titre = ?";
                    PreparedStatement coursIdStatement = connection.prepareStatement(coursIdQuery);
                    coursIdStatement.setString(1, selectedCoursTitre);
                    ResultSet coursIdResult = coursIdStatement.executeQuery();

                    if (coursIdResult.next()) {
                        int selectedCoursId = coursIdResult.getInt("id");

                        String reponseQuery = "SELECT r.id, r.reponse, r.resultat, e.question, c.titre " +
                                "FROM reponseetudiant r " +
                                "JOIN exercice e ON r.idExercice = e.id " +
                                "JOIN cours c ON e.cours_id = c.id " +
                                "JOIN users u ON r.idEtudiant = u.id " +
                                "WHERE u.email = ? AND c.titre = ? ";

                        PreparedStatement reponseStatement = connection.prepareStatement(reponseQuery);
                        reponseStatement.setString(1, this.user.getEmail());
                        reponseStatement.setString(2, selectedCoursTitre);
                        ResultSet resultSet = reponseStatement.executeQuery();

                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String question = resultSet.getString("question");
                            String reponse = resultSet.getString("reponse");
                            String titre = resultSet.getString("titre");
                            int resultat = resultSet.getInt("resultat");
                            ReponseEtudiant reponseEtudiant = new ReponseEtudiant(id, question, reponse, resultat, titre);
                            reponsesTable.getItems().add(reponseEtudiant);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
