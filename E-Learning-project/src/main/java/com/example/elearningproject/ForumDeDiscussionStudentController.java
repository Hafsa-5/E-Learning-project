package com.example.elearningproject;

import com.example.elearningproject.Model.Message;
import com.example.elearningproject.Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ForumDeDiscussionStudentController {

    @FXML
    private TableView<Message> messagesTable;

    @FXML
    private TableColumn<Message, String> objetColumn;

    @FXML
    private TableColumn<Message, String> messageColumn;

    @FXML
    private TableColumn<Message, String> responseColumn;

    @FXML
    private TextField objetTextField;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private ComboBox<User> destinataireComboBox;

    private User user = new User();

    public void setUser(User user) {
        this.user = user;

    }

    public void initialize() {

        initializeTable();
        populateDestinataires();
        displayMessages();

    }
    private void initializeTable() {
        objetColumn.setCellValueFactory(new PropertyValueFactory<>("objet"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        responseColumn.setCellValueFactory(new PropertyValueFactory<>("response"));
    }
    private void populateDestinataires() {
        if (!destinataireComboBox.getItems().isEmpty()) {
            return; // ComboBox already populated, no need to populate again
        }
        String roleProf = "prof";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT * FROM users WHERE role = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, roleProf);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                User user = new User(id, email);
                destinataireComboBox.getItems().add(user);

                destinataireComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getEmail());
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void envoyerMessage() {
        String objet = objetTextField.getText();
        String message = messageTextArea.getText();
        User destinataire = destinataireComboBox.getValue();
        int senderId = this.user.getId();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "INSERT INTO message (objet, message, sender_id, recipient_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, objet);
            preparedStatement.setString(2, message);
            preparedStatement.setInt(3, senderId);
            preparedStatement.setInt(4, destinataire.getId());
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Le message a été enyoyé avec succès.");
                displayMessages();
            } else {
                System.out.println("Impossible de menvoyer le message.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void displayMessages() {
        messagesTable.getItems().clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT * FROM message WHERE sender_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int sender_id = resultSet.getInt("sender_id");
                int recipient_id = resultSet.getInt("recipient_id");
                String objet = resultSet.getString("objet");
                String message = resultSet.getString("message");
                String response = resultSet.getString("response");
                Message msg = new Message(id,objet, message,sender_id,recipient_id,response);
                messagesTable.getItems().add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerMessage() {
        Message selectedMessage = messagesTable.getSelectionModel().getSelectedItem();

        if (selectedMessage != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
                String sql = "DELETE FROM message WHERE objet = ? AND message = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, selectedMessage.getObjet());
                preparedStatement.setString(2, selectedMessage.getMessage());
                preparedStatement.executeUpdate();

                // Rafraîchir la table après la suppression du message
                displayMessages();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
