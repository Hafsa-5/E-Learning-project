package com.example.elearningproject;



import com.example.elearningproject.Model.Cours;
import com.example.elearningproject.Model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/elearning";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";


    public static void saveCourse(String title, String description, String level, File selectedFile) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Cours (titre, description, niveau, fichierPdf) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, level);

            if (selectedFile != null) {
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    preparedStatement.setBinaryStream(4, fis, (int) selectedFile.length());
                    preparedStatement.executeUpdate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                preparedStatement.setNull(4, java.sql.Types.BLOB);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Cours> getAllCourses() {
        List<Cours> coursList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Cours");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("titre");
                String description = resultSet.getString("description");
                String level = resultSet.getString("niveau");

                // If your fichierPdf is stored as a BLOB, you can retrieve it here
                // InputStream pdfInputStream = resultSet.getBinaryStream("fichierPdf");

                Cours cours = new Cours(id, title, description, level, null); // Pass the retrieved data
                coursList.add(cours);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coursList;
    }

    public static boolean deleteCoursFromDatabase(Cours cours) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM cours WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, cours.getId()); // Assuming getId() returns the ID of the Cours
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; // Return true if at least one row was deleted
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately based on your application's requirements
            return false;
        }
    }

    public static boolean updateCoursInDatabase(Cours cours) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE cours SET titre = ?, description = ?, niveau = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, cours.getTitre());
                preparedStatement.setString(2, cours.getDescription());
                preparedStatement.setString(3, cours.getNiveau());
                preparedStatement.setInt(4, cours.getId());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; // Return true if at least one row was updated
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately based on your application's requirements
            return false;
        }
    }
}
