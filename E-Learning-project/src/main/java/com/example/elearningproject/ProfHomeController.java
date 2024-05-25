package com.example.elearningproject;

import com.example.elearningproject.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfHomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox dynamicContent;
    private Stage stage;

    private User user = new User(0,"","","");


    public void initialize() {
        welcomeLabel.setText("Welcome "+user.getEmail());
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void changeContent(javafx.event.ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(clickedButton.getId() + ".fxml"));
        System.out.println(clickedButton.getId());
        Parent root = loader.load();

        Object controller = loader.getController();

        if (controller instanceof ProfilController) {
            ProfilController profilController = (ProfilController) controller;
            profilController.setUser(this.user);
            profilController.initialize();
        } else if (controller instanceof AjouterUnCoursController) {
            AjouterUnCoursController ajouterUnCoursController = (AjouterUnCoursController) controller;
            ajouterUnCoursController.setUser(this.user);
            ajouterUnCoursController.initialize();

        } else if (controller instanceof ResultatProfController) {
            ResultatProfController resultatProfController = (ResultatProfController) controller;
            resultatProfController.setUser(this.user);
            resultatProfController.initialize();
        }else if (controller instanceof ListeDesCoursProfController) {
            ListeDesCoursProfController listeDesCoursProfController = (ListeDesCoursProfController) controller;
            listeDesCoursProfController.setUser(this.user);
            listeDesCoursProfController.initialize();
        }else if (controller instanceof ListeDesLeconsProfController) {
            ListeDesLeconsProfController listeDesLeconsProfController = (ListeDesLeconsProfController) controller;
            listeDesLeconsProfController.setUser(this.user);
            listeDesLeconsProfController.initialize();
        }else if (controller instanceof ForumdeDiscussionController) {
            ForumdeDiscussionController forumdeDiscussionController = (ForumdeDiscussionController) controller;
            forumdeDiscussionController.setUser(this.user);
            forumdeDiscussionController.initialize();
        }
        dynamicContent.getChildren().setAll(root);
    }

    @FXML
    private void handleLogout() throws IOException {
       stage.close();
        ouvrirFenetreConnexion();
    }

    private void ouvrirFenetreConnexion() throws IOException {
        Stage fenetreConnexion = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        HelloController controller = loader.getController();

        Scene scene = new Scene(loader.load(),1000, 500);
        fenetreConnexion.setScene(scene);
        fenetreConnexion.show();
    }
}
