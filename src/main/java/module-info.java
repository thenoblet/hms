module gtp.hms {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens gtp.hms.model to javafx.fxml;
    exports gtp.hms.model;
    exports gtp.hms;
    opens gtp.hms;
}