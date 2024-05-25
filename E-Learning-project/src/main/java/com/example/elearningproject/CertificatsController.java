package com.example.elearningproject;

import com.example.elearningproject.Model.Certificat;
import com.example.elearningproject.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class CertificatsController {

    @FXML
    private VBox certificatsRoot;

    @FXML
    private TableView<Certificat> certificatsTable;

    @FXML
    private TableColumn<Certificat, Integer> idCertificatColumn;

    @FXML
    private TableColumn<Certificat, String> titreCoursColumn;

    @FXML
    private TableColumn<Certificat, String> contenuCertificatColumn;

    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        idCertificatColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreCoursColumn.setCellValueFactory(new PropertyValueFactory<>("titreCours"));
        contenuCertificatColumn.setCellValueFactory(new PropertyValueFactory<>("contenuCertificat"));

        afficherCertificats();
    }

    @FXML
    private void afficherCertificats() {
        certificatsTable.getItems().clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT c.id,c.date_creation,c.id_etudiant, co.titre, ce.contenu " +
                    "FROM certificat c " +
                    "JOIN cours co ON c.id_cours = co.id " +
                    "JOIN certificat ce ON c.id_cours = ce.id_cours " +
                    "WHERE c.id_etudiant = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idCertificat = resultSet.getInt("id");
                int id_etudiant = resultSet.getInt("id_etudiant");
                String titreCours = resultSet.getString("titre");
                String contenuCertificat = resultSet.getString("contenu");
                Timestamp dateCreation = resultSet.getTimestamp("date_creation");

                Certificat certificat = new Certificat(idCertificat, titreCours, contenuCertificat, dateCreation,id_etudiant);
                certificatsTable.getItems().add(certificat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void telechargerCertificat() {
        Certificat certificat = certificatsTable.getSelectionModel().getSelectedItem();
        if (certificat != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                // Ajouter un contenu au document
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                    contentStream.setLeading(20);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700);
                    contentStream.showText("Certificat de Réussite");
                    contentStream.newLine();
                    contentStream.setFont(PDType1Font.HELVETICA, 14);
                    contentStream.showText("Ce certificat est décerné à");
                    contentStream.newLine();
                    contentStream.showText("Nom de l'étudiant : " + getUserEmailById(certificat.getIdEtudiant()));
                    contentStream.newLine();
                    contentStream.showText("Cours : " + certificat.getTitreCours());
                    contentStream.newLine();
                    contentStream.showText("Date : " + certificat.getDateCreation());
                    contentStream.newLine();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.showText("Félicitations !");
                    contentStream.newLine();
                    contentStream.setFont(PDType1Font.HELVETICA, 14);
                    contentStream.showText("Pour avoir complété avec succès le cours.");
                    contentStream.endText();

                    try (InputStream imageStream = getClass().getResourceAsStream("logo.jpg")) {
                        BufferedImage image = ImageIO.read(imageStream);
                        PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);
                        float scale = 0.5f; // Ajustez l'échelle selon vos besoins
                        contentStream.drawImage(pdImage, page.getMediaBox().getWidth() - pdImage.getWidth() * scale, page.getMediaBox().getHeight() - pdImage.getHeight() * scale, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
                    }


                    float borderWidth = 10;
                    contentStream.setStrokingColor(Color.BLACK);
                    contentStream.setLineWidth(borderWidth);
                    contentStream.addRect(borderWidth / 2, borderWidth / 2, page.getMediaBox().getWidth() - borderWidth, page.getMediaBox().getHeight() - borderWidth);
                    contentStream.stroke();
                }

                File file = new File("certificat.pdf");
                document.save(file);
                document.close();

                Desktop.getDesktop().open(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public String getUserEmailById(int userId) {
        System.out.println(userId);
        String userEmail = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/elearning", "root", "")) {
            String sql = "SELECT email FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userEmail = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userEmail;
    }
}
