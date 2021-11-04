module com.sahilasopa.visualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.compress;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.net.http;
    requires commons.csv;
    requires org.apache.commons.io;

    opens com.sahilasopa.visualizer to javafx.fxml;
    exports com.sahilasopa.visualizer;
}