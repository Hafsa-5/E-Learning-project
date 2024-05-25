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

public class ResultatProfController {

    private User user = new User();
    public void setUser(User user) {
        this.user = user;
    }


    @FXML
    private ComboBox<String> etudiantComboBox;

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

        populateComboBox();
        populateCoursComboBox();
    }

    private void populateComboBox() {
        etudiantComboBox.getItems().clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT DISTINCT u.email " +
                    "FROM reponseetudiant r " +
                    "INNER JOIN users u ON r.idEtudiant = u.id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                etudiantComboBox.getItems().add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateCoursComboBox() {
        coursComboBox.getItems().clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT DISTINCT c.titre " +
                    "FROM cours c " +
                    "INNER JOIN exercice e ON c.id = e.cours_id " +
                    "WHERE e.professeur_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.user.getId());
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
        String selectedEtudiantEmail = etudiantComboBox.getValue();
        String selectedCoursTitre = coursComboBox.getValue();

        if (selectedEtudiantEmail != null && !selectedEtudiantEmail.isEmpty() && selectedCoursTitre != null && !selectedCoursTitre.isEmpty()) {
            reponsesTable.getItems().clear();

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String userIdQuery = "SELECT id FROM users WHERE email = ?";
                PreparedStatement userIdStatement = connection.prepareStatement(userIdQuery);
                userIdStatement.setString(1, selectedEtudiantEmail);
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
                                "WHERE u.email = ? AND c.titre = ? AND c.user_id = ?";

                        PreparedStatement reponseStatement = connection.prepareStatement(reponseQuery);
                        reponseStatement.setString(1, selectedEtudiantEmail);
                        reponseStatement.setString(2, selectedCoursTitre);
                        reponseStatement.setInt(3, this.user.getId());
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

    @FXML
    private void OuvrirFenetreRsultat(ActionEvent event) {
        try {
            ReponseEtudiant selectedItem = reponsesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterResultat.fxml"));
            Parent root = loader.load();
            AjouterResultatController ajouterResultatController = loader.getController();
            System.out.println(selectedItem.toString());
            ajouterResultatController.setResultatId(selectedItem.getId());
            ajouterResultatController.setResultatProfController(this);
            ajouterResultatController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void genererCertificat(ActionEvent event) {
        String nomEtudiant = etudiantComboBox.getValue();
        String cours = coursComboBox.getValue();

        if (nomEtudiant != null && !nomEtudiant.isEmpty() && cours != null && !cours.isEmpty()) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                // Requête pour insérer le certificat dans la table certificat
                String certificatQuery = "INSERT INTO certificat (id_etudiant, id_cours, contenu) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement certificatStatement = connection.prepareStatement(certificatQuery);
                certificatStatement.setInt(1, getIdEtudiant(nomEtudiant)); // Utilisez votre méthode getIdEtudiant pour récupérer l'ID de l'étudiant
                certificatStatement.setInt(2, getIdCours(cours)); // Utilisez votre méthode getIdCours pour récupérer l'ID du cours
                certificatStatement.setString(3, "Ceci certifie que l'étudiant " + nomEtudiant + " a complété avec succès le cours " + cours + ".");

                int rowsInserted = certificatStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Le certificat a été inséré avec succès.");
                } else {
                    System.out.println("Échec de l'insertion du certificat.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner un étudiant et un cours pour générer le certificat.");
        }
    }
    private int getIdEtudiant(String nomEtudiant) throws SQLException {
        int idEtudiant = -1; // Valeur par défaut si l'ID n'est pas trouvé

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String query = "SELECT id FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomEtudiant);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                idEtudiant = resultSet.getInt("id");
            }
        }

        return idEtudiant;
    }

    private int getIdCours(String titreCours) throws SQLException {
        int idCours = -1; // Valeur par défaut si l'ID n'est pas trouvé

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String query = "SELECT id FROM cours WHERE titre = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, titreCours);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                idCours = resultSet.getInt("id");
            }
        }

        return idCours;
    }

}
