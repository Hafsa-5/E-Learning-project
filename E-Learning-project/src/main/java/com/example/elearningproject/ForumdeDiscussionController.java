package com.example.elearningproject;

import com.example.elearningproject.Model.Cours;
import com.example.elearningproject.Model.Message;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.*;
import com.example.elearningproject.Model.User;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ForumdeDiscussionController {

    private User user = new User();

    @FXML
    public TableView<Message> messagesTable;

    @FXML
    private TableColumn<Message, String> objetColumn;

    @FXML
    private TableColumn<Message, String> messageColumn;

    @FXML
    private TableColumn<Message, Integer> senderColumn;

    @FXML
    private TableColumn<Message, String> responseColumn;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        initializeTable();
        populateMessagesTable();
    }

    private void initializeTable() {
        objetColumn.setCellValueFactory(new PropertyValueFactory<>("objet"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        senderColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Message, Integer> param) {
                return new SimpleIntegerProperty(param.getValue().getSenderId()).asObject();
            }
        });
        responseColumn.setCellValueFactory(new PropertyValueFactory<>("response"));

    }

    public void populateMessagesTable() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM message WHERE recipient_id = ?");
            preparedStatement.setInt(1, this.user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String objet = resultSet.getString("objet");
                String messagedescription = resultSet.getString("message");
                String response = resultSet.getString("response");
                int sender = resultSet.getInt("sender_id");
                int recipient = resultSet.getInt("recipient_id");
                Message message = new Message(id,objet, messagedescription, sender,recipient, response);
                messagesTable.getItems().add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OuvrirFenetreReepondre(ActionEvent event) {
        try {
            Message selectedMssagd = messagesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RepondreMessage.fxml"));
            Parent root = loader.load();
            RepondreMessageController repondreMessageController = loader.getController();
            repondreMessageController.setMessageId(selectedMssagd.getId());
            repondreMessageController.setForumdeDiscussionController(this);
            repondreMessageController.initialize();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
