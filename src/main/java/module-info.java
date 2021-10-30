module com.example.visualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.compress;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.net.http;
    requires commons.csv;
    requires commons.io;

    opens com.example.visualizer to javafx.fxml;
    exports com.example.visualizer;
}