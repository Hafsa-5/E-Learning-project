package com.example.elearningproject;

import com.example.elearningproject.Model.Cours;
import com.example.elearningproject.Model.User;
import com.example.elearningproject.Model.Lecon;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class ListeDesLeconsProfController {
    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private TableView<Lecon> leconsTable;

    @FXML
    private TableColumn<Lecon, Integer> idColumn;

    @FXML
    private TableColumn<Lecon, String> titreColumn;

    @FXML
    private TableColumn<Lecon, String> contenuColumn;
    @FXML
    private TableColumn<Lecon, String> coursColumn;


    @FXML
    private TextField titreTextField;

    @FXML
    private TextArea contenuTextField;

    public void initialize() {
        initializeTable();
        populateLeconsTable();
        populateTextFields();
    }
    private void populateTextFields() {
        leconsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titreTextField.setText(newSelection.getTitre());
                contenuTextField.setText(newSelection.getContenu());
            }
        });
    }
    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));

        coursColumn.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getCoursId();
            String coursTitr = getCours(userId).getTitre();
            return new SimpleStringProperty(coursTitr);
        });
    }
    private void populateLeconsTable() {
        if (!leconsTable.getItems().isEmpty()) {
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            Statement statement = connection.createStatement();
            int currentUserId = this.user.getId();
            String query = "SELECT * FROM lecons";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String contenu = resultSet.getString("contenu");
                byte[] fichierPdf = resultSet.getBytes("fichierPdf");
                int coursId = resultSet.getInt("cours_id");
                Lecon lecon = new Lecon(id, titre, contenu,coursId,fichierPdf);
                leconsTable.getItems().add(lecon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cours getCours(int courId) {
        Cours cours = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cours WHERE id = ?");
            preparedStatement.setInt(1, courId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                String niveau = resultSet.getString("niveau");
                cours = new Cours(id, titre, description, niveau);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cours;
    }

    @FXML
    private void deleteLeconFromDatabase() {
        Lecon selectedLecon = leconsTable.getSelectionModel().getSelectedItem();
        if (selectedLecon != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String query = "DELETE FROM lecons WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedLecon.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Lecon deleted successfully!");
                    leconsTable.getItems().remove(selectedLecon);
                } else {
                    System.out.println("Failed to delete lecon.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No Lecon selected.");
        }
    }

    @FXML
    private void telechargerLecon() {
        Lecon selectedLecon = leconsTable.getSelectionModel().getSelectedItem();
        if (selectedLecon != null) {
            byte[] pdfData = selectedLecon.getFichierPdf();
            if (pdfData != null && pdfData.length > 0) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(selectedLecon.getTitre() + ".pdf");
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
                System.out.println("No PDF file associated with the lecon.");
            }
        } else {
            System.out.println("No Lecon selected.");
        }
    }

    @FXML
    private void updateLeconInDatabase() {
        Lecon selectedLecon = leconsTable.getSelectionModel().getSelectedItem();
        if (selectedLecon != null) {
            selectedLecon.setTitre(titreTextField.getText());
            selectedLecon.setContenu(contenuTextField.getText());

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String query = "UPDATE lecons SET titre = ?, contenu = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedLecon.getTitre());
                statement.setString(2, selectedLecon.getContenu());
                statement.setInt(3, selectedLecon.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Lecon updated successfully!");
                    leconsTable.getItems().clear();
                    populateLeconsTable();
                } else {
                    System.out.println("Failed to update lecon.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No lecon selected.");
        }
    }
}
