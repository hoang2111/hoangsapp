module com.github.hoang2111.hoangsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.sql;


    opens com.github.hoang2111.hoangsapp to javafx.fxml;
    exports com.github.hoang2111.hoangsapp;
}