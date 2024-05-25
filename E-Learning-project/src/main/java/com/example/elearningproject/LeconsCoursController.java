package com.example.elearningproject;

import com.example.elearningproject.Model.Lecon;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class LeconsCoursController {
    @FXML
    private TableView<Lecon> leconTableView;

    @FXML
    private TableColumn<Lecon, Integer> idColumn;

    @FXML
    private TableColumn<Lecon, String> contenuColumn;

    @FXML
    private TableColumn<Lecon, String> titreColumn;

    private int coursId;
    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    private int profId;
    public void setProfId(int profId) {
        this.profId = profId;
    }


    public void initialize() {
        afficherLeconsDuCours();
    }

    private void afficherLeconsDuCours() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT * FROM lecons WHERE cours_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coursId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String contenu = resultSet.getString("contenu");
                Lecon lecon = new Lecon(id, titre, contenu,coursId);
                leconTableView.getItems().add(lecon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}