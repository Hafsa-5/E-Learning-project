package com.example.elearningproject;

import com.example.elearningproject.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleInscription() {
        String email = emailField.getText();
        String password = passwordField.getText();

        connecterUtilisateur(email, password);
    }

    private void connecterUtilisateur(String email, String password) {
        String url = "jdbc:mysql://localhost:3306/elearning";
        String utilisateur = "root";
        String motDePasse = "";

        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse)) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int id_user = resultSet.getInt("id");
                    String email_user = resultSet.getString("email");
                    String password_user = resultSet.getString("password");
                    String role_user = resultSet.getString("role");

                    User user = new User(id_user,email_user,password_user,role_user);

                    //Fermer la feenetre de connexion
                   Stage stage = (Stage) emailField.getScene().getWindow();
                   stage.close();

                    if ("prof".equals(role_user)) {
                        ouvrirNouvelleFenetre(user);
                    } else {
                        ouvrirNouvelleFenetreStudent(user);
                    }
                } else {
                    System.out.println("Échec de la connexion. Vérifiez vos identifiants.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void ouvrirNouvelleFenetre(User user) throws IOException {
        Stage nouvelleFenetre = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profHome.fxml"));
        Parent root = loader.load(); // Load the FXML and get the root

        ProfHomeController controller = loader.getController();
        controller.setStage(nouvelleFenetre);
        controller.setUser(user);

        controller.initialize();

        Scene scene = new Scene(root, 1000, 500);
        nouvelleFenetre.setScene(scene);
        nouvelleFenetre.show();
    }

    private void ouvrirNouvelleFenetreStudent(User user) throws IOException {
        Stage nouvelleFenetre = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentHome.fxml"));
        Parent root = loader.load(); // Load the FXML and get the root
        StudentHomeController controller = loader.getController();
        controller.setStage(nouvelleFenetre);
        controller.setUser(user);
        controller.initialize(); // Now you can safely call initialize on the controller

        Scene scene = new Scene(root, 1000, 500);
        nouvelleFenetre.setScene(scene);
        nouvelleFenetre.show();
    }


}
