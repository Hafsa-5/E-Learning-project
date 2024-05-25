package com.example.elearningproject.Model;

public class Lecon {
    private int id;
    private String titre;

    private byte[] fichierPdf;
    private String contenu;
    private int coursId;

    public Lecon(int id, String titre, String contenu, int coursId, byte[] fichierPdf) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.coursId = coursId;
        this.fichierPdf=fichierPdf;
    }

    public Lecon(int id, String titre, String contenu,int coursId) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.coursId=coursId;
    }

    // Getters and setters
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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getCoursId() {
        return coursId;
    }

    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    public byte[] getFichierPdf() {
        return fichierPdf;
    }

    public void setFichierPdf(byte[] fichierPdf) {
        this.fichierPdf = fichierPdf;
    }
}
