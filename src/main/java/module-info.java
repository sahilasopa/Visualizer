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
    requires javafx.graphics;

    opens com.sahilasopa.visualizer to javafx.fxml;
    exports com.sahilasopa.visualizer;
}