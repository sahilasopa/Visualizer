package com.example.visualizer;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphDataHandler {
    @FXML
    private Label title;

    @FXML
    private Button type;

    @FXML
    private ComboBox<String> values;

    @FXML
    private ComboBox<String> num;

    @FXML
    private ImageView image;

    private String graphType;

    private Map<String, List<String>> data;

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public Map<String, List<String>> getData() {
        return data;
    }

    public void setData(Map<String, List<String>> data) {
        this.data = data;
    }

    @FXML
    public void display(Event event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DataSelector.fxml")));
        Scene scene = new Scene(root);
        num.getItems();

        switch (graphType) {
            case "PIE CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/pie.png")).toExternalForm()));
            case "LINE GRAPH" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/line.png")).toExternalForm()));
            case "BAR CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/bar.png")).toExternalForm()));
        }

        stage.setScene(scene);
        stage.show();
    }
}
