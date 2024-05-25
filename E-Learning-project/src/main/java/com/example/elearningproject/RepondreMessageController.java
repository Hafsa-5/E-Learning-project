package com.example.elearningproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepondreMessageController {
    @FXML
    private TextArea responseTextArea;

    private int messageId;
    private ForumdeDiscussionController forumdeDiscussionController;
    public void initialize() {
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setForumdeDiscussionController(ForumdeDiscussionController forumdeDiscussionController) {
        this.forumdeDiscussionController = forumdeDiscussionController;
    }

    @FXML
    private void updateResponse() {
        String response = responseTextArea.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE message SET response = ? WHERE id = ?");
            preparedStatement.setString(1, response);
            preparedStatement.setInt(2, messageId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("La réponse du message a été mise à jour avec succès.");
                forumdeDiscussionController.messagesTable.getItems().clear();
                forumdeDiscussionController.populateMessagesTable();

                responseTextArea.getScene().getWindow().hide();
            } else {
                System.out.println("Impossible de mettre à jour la réponse du message.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}