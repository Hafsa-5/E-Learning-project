package com.example.elearningproject.Model;

public class ReponseEtudiant {

    private int id;
    private String reponse;
    private String titre;
    private int idExercice;
    private int idEtudiant;

    private String question;

    private int resultat;

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ReponseEtudiant(int id, String reponse, int idExercice, int idEtudiant) {
        this.id = id;
        this.reponse = reponse;
        this.idExercice = idExercice;
        this.idEtudiant = idEtudiant;
    }



    public ReponseEtudiant(int id, String question, String reponse,Integer resultat,String titre) {
        this.id = id;
        this.question = question;
        this.reponse = reponse;
        this.resultat = resultat;
        this.titre=titre;

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "ReponseEtudiant{" +
                "id=" + id +
                ", reponse='" + reponse + '\'' +
                ", idExercice=" + idExercice +
                ", idEtudiant=" + idEtudiant +
                ", question='" + question + '\'' +
                ", resultat=" + resultat +
                '}';
    }
}
