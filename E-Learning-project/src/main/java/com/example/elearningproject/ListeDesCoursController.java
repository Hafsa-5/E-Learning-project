package com.example.elearningproject;

import com.example.elearningproject.Model.Cours;
import com.example.elearningproject.Model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;

public class ListeDesCoursController {

    @FXML
    private TableView<Cours> coursesTable;

    @FXML
    private TableColumn<Cours, Integer> idColumn;

    @FXML
    private TableColumn<Cours, String> titreColumn;

    @FXML
    private TableColumn<Cours, String> descriptionColumn;

    @FXML
    private TableColumn<Cours, String> niveauColumn;

    @FXML
    private TableColumn<Cours, String> userColumn;

    @FXML
    private TextField titreTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField niveauTextField;

    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }


    public void initialize() {
        initializeTable();
        populateCoursesTable();
        populateTextFields();
    }

    private void populateTextFields() {
        coursesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titreTextField.setText(newSelection.getTitre());
                descriptionTextField.setText(newSelection.getDescription());
                niveauTextField.setText(newSelection.getNiveau());
            }
        });
    }
    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        niveauColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));

        // Set cell value factory for userColumn to display email
        userColumn.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUserId();
            String userEmail = getUserEmail(userId); // Replace getUserEmail with your method to retrieve email by user ID
            return new SimpleStringProperty(userEmail);
        });
    }

    private String getUserEmail(int userId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM users WHERE id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void populateCoursesTable() {
        if (!coursesTable.getItems().isEmpty()) {
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT c.id, c.titre, c.description, c.niveau, c.fichierPdf, c.user_id, u.email " +
                    "FROM cours c JOIN users u ON c.user_id = u.id");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                String niveau = resultSet.getString("niveau");
                byte[] fichierPdf = resultSet.getBytes("fichierPdf");
                int userId = resultSet.getInt("user_id");
                String userEmail = resultSet.getString("email");
                Cours course = new Cours(id, titre, description, niveau, fichierPdf, userId);
                course.setUserEmail(userEmail);
                coursesTable.getItems().add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void telechargerCours() {
        Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            byte[] pdfData = selectedCours.getFichierPdf();
            if (pdfData != null && pdfData.length > 0) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(selectedCours.getTitre() + ".pdf");
                    File savedFile = fileChooser.showSaveDialog(null);

                    if (savedFile != null) {
                        FileOutputStream fileOutputStream = new FileOutputStream(savedFile);
                        fileOutputStream.write(pdfData);
                        fileOutputStream.close();
                        System.out.println("PDF downloaded successfully!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No PDF file associated with the course.");
            }
        } else {
            System.out.println("No course selected.");
        }
    }

    @FXML
    private void OuvrirFenetreExercicesCours(ActionEvent event) {
        try {
            Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExercicesCoursEtudiant.fxml"));
            Parent root = loader.load();
            ExercicesCoursEtudiantController exercicesCoursEtudiantController = loader.getController();
            exercicesCoursEtudiantController.setCoursId(selectedCours.getId());
            exercicesCoursEtudiantController.setStudentId(this.user.getId());
            exercicesCoursEtudiantController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OuvrirFenetreLeconsCours(ActionEvent event) {
        try {
            Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LeconsCours.fxml"));
            Parent root = loader.load();
            LeconsCoursController leconsCoursController = loader.getController();
            leconsCoursController.setCoursId(selectedCours.getId());
            leconsCoursController.setProfId(this.user.getId());
            leconsCoursController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
