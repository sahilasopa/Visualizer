package com.example.visualizer;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class HomeHandler {
    private final Alert alert = new Alert(Alert.AlertType.WARNING, "URL Is Invalid", ButtonType.OK);
    @FXML
    private TextField url;

    public void handleUrl() throws IOException, InterruptedException {
        boolean isValid = true;
        final String URL = url.getText().trim();
        try {
            URL u = new URL(URL);
            u.toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            isValid = false;
        }
        if (URL.isEmpty() || !isValid) {
            alert.setContentText("Please Enter A Valid URL");
            alert.setTitle("Invalid URL");
            if (alert.getResult() == ButtonType.OK) {
                alert.hide();
            }
            alert.showAndWait();
        } else {
            FileHandler handler = new FileHandler();
            handler.apiHandler(URL);
        }
    }

    public void handleFile(Event event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick Data Source");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Data Formats", "*.csv", "*.json", "*.xlsx"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            FileHandler handler = new FileHandler(event);
            handler.setFile(file);
            handler.open();
        } else {
            alert.setContentText("Please Upload A Valid File");
            alert.setTitle("No File Found");
            alert.show();
        }
    }
}
