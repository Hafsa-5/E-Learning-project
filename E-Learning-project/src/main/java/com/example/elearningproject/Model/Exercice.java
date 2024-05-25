package com.example.elearningproject.Model;

public class Exercice {
    private int id;
    private String question;
    private String reponse;
    private int professeurId;
    private int coursId;

    public Exercice(int id, String question, String reponse, int professeurId, int coursId) {
        this.id = id;
        this.question = question;
        this.reponse = reponse;
        this.professeurId = professeurId;
        this.coursId = coursId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getProfesseurId() {
        return professeurId;
    }

    public void setProfesseurId(int professeurId) {
        this.professeurId = professeurId;
    }

    public int getCoursId() {
        return coursId;
    }

    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }
}
