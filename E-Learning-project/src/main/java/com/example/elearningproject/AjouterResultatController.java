package com.example.elearningproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjouterResultatController {
    @FXML
    private TextField resultatTextField;

    private ResultatProfController resultatProfController;

    public void setResultatProfController(ResultatProfController resultatProfController) {
        this.resultatProfController = resultatProfController;
    }

    private int resultatId; // ID of the reponseetudiant to update

    public void setResultatId(int resultatId) {
        this.resultatId = resultatId;
    }

    @FXML
    private void updateResultat() {
        String newResultat = resultatTextField.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "UPDATE reponseetudiant SET resultat = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newResultat);
            preparedStatement.setInt(2, resultatId);
            preparedStatement.executeUpdate();

            Stage stage = (Stage) resultatTextField.getScene().getWindow();
            stage.close();
            resultatProfController.reponsesTable.getItems().clear();
            resultatProfController.afficherReponses();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
    }
}
