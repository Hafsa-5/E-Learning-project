package com.example.elearningproject.Model;

import java.util.Arrays;

public class Cours {
    private int id;
    private String titre;
    private String description;
    private String niveau;
    private byte[] fichierPdf;
    private String userEmail;
    private int userId;

    public Cours(int id, String titre, String description, String niveau, byte[] fichierPdf) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.niveau = niveau;
        this.fichierPdf = fichierPdf;
    }

    public Cours() {

    }

    public Cours(int id, String titre, String description, String niveau, byte[] fichierPdf, int userId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.niveau = niveau;
        this.fichierPdf = fichierPdf;
        this.userId = userId;
    }

    public Cours(int id, String titre, String description, String niveau) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.niveau = niveau;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public byte[] getFichierPdf() {
        return fichierPdf;
    }

    public void setFichierPdf(byte[] fichierPdf) {
        this.fichierPdf = fichierPdf;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", niveau='" + niveau + '\'' +
                ", fichierPdf=" + Arrays.toString(fichierPdf) +
                ", userId=" + userId +
                '}';
    }
}
