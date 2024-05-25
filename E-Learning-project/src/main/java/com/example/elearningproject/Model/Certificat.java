package com.example.elearningproject.Model;

import java.sql.Timestamp;

public class Certificat {
    private int id;
    private int idCours;
    private int idEtudiant;

    private Timestamp dateCreation;

    private String titreCours;
    private String contenuCertificat;

    public Certificat(int id, int idCours, int idEtudiant) {
        this.id = id;
        this.idCours = idCours;
        this.idEtudiant = idEtudiant;
    }

    public Certificat(int id, String titreCours, String contenuCertificat, Timestamp dateCreation, int idEtudiant) {
        this.id = id;
        this.titreCours = titreCours;
        this.contenuCertificat = contenuCertificat;
        this.dateCreation = dateCreation;
        this.idEtudiant = idEtudiant;

    }

    public Certificat(int idCertificat, int idCours) {
        this.id = idCertificat;
        this.idCours = idCours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getTitreCours() {
        return titreCours;
    }

    public void setTitreCours(String titreCours) {
        this.titreCours = titreCours;
    }

    public String getContenuCertificat() {
        return contenuCertificat;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }
    public void setContenuCertificat(String contenuCertificat) {
        this.contenuCertificat = contenuCertificat;
    }
}
