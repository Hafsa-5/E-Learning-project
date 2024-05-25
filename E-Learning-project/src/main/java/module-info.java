module com.example.elearningproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens com.example.elearningproject.Model to javafx.base;

    opens com.example.elearningproject to javafx.fxml;
    exports com.example.elearningproject;
}