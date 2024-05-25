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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class ListeDesCoursProfController {

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
            int currentUserId = this.user.getId();
            String query = "SELECT c.id, c.titre, c.description, c.niveau, c.fichierPdf, c.user_id, u.email " +
                    "FROM cours c JOIN users u ON c.user_id = u.id " +
                    "WHERE c.user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, currentUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

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
    private void updateCoursInDatabase() {
        Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            selectedCours.setTitre(titreTextField.getText());
            selectedCours.setDescription(descriptionTextField.getText());
            selectedCours.setNiveau(niveauTextField.getText());

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String query = "UPDATE cours SET titre = ?, description = ?, niveau = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedCours.getTitre());
                statement.setString(2, selectedCours.getDescription());
                statement.setString(3, selectedCours.getNiveau());
                statement.setInt(4, selectedCours.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Course updated successfully!");
                    coursesTable.getItems().clear();
                    populateCoursesTable();
                } else {
                    System.out.println("Failed to update course.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No course selected.");
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
    private void deleteCoursFromDatabase() {
        Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String query = "DELETE FROM cours WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedCours.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Course deleted successfully!");
                    coursesTable.getItems().remove(selectedCours);
                } else {
                    System.out.println("Failed to delete course.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No course selected.");
        }
    }

    @FXML
    private void OuvrirFenetreAjouterLecon(ActionEvent event) {
        try {
            Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterLecon.fxml"));
            Parent root = loader.load();
            AjouterLeconController ajouterLeconController = loader.getController();
            ajouterLeconController.setCoursId(selectedCours.getId());
            ajouterLeconController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OuvrirFenetreAjouterExercice(ActionEvent event) {
        try {
            Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterExercice.fxml"));
            Parent root = loader.load();
            AjouterExerciceController ajouterExerciceController = loader.getController();
            ajouterExerciceController.setCoursId(selectedCours.getId());
            ajouterExerciceController.setProfId(this.user.getId());
            ajouterExerciceController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OuvrirFenetreExercicesCours(ActionEvent event) {
        try {
            Cours selectedCours = coursesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExercicesCours.fxml"));
            Parent root = loader.load();
            ExercicesCoursController exercicesCoursController = loader.getController();
            exercicesCoursController.setCoursId(selectedCours.getId());
            exercicesCoursController.setProfId(this.user.getId());
            exercicesCoursController.initialize();

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
