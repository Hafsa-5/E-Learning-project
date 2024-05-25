package com.example.elearningproject.Model;

public class Message {
    private int id;
    private String objet;
    private String message;
    private int senderId;
    private int recipientId;
    private String response;

    public Message(int id, String objet, String message, int senderId, int recipientId, String response) {
        this.id = id;
        this.objet = objet;
        this.message = message;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.response = response;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
