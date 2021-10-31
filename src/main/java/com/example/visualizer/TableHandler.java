package com.example.visualizer;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TableHandler {
    ObservableList<String> items = FXCollections.observableArrayList();

    public void PlotTable(Event event, HashMap<String, List<String>> data) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AnchorPane root = new AnchorPane();
        root.setPrefSize(650, 650);
        Scene scene = new Scene(root);
        Parent home = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
        ComboBox<String> comboBox = new ComboBox<>(); // Dropdown
        TableView<List<String>> tableView = new TableView<>(); // Table
        Label label = new Label("Graph/Chat: ");
        Button generate = new Button("Generate");
        Button load = new Button("Load");

        List<String> headers = data.get("headers");
        List<String> main = data.get("data");
        List<String> graphs = List.of("PIE CHART", "LINE GRAPH", "BAR CHART");
        items.addAll(graphs);
        comboBox.setItems(items);

        int j = 0;
        for (String header : headers) {
            TableColumn<List<String>, String> heading = new TableColumn<>(header);
            heading.setCellValueFactory(new PropertyValueFactory<>(header));
            int finalJ = j;
            heading.setCellValueFactory(da -> new ReadOnlyStringWrapper(da.getValue().get(finalJ)));
            tableView.getColumns().add(heading);
            j++;
        }

        List<String> listOfStrings = new ArrayList<>();
        List<List<String>> lists = new ArrayList<>();
        int currentCol = 0;
        for (int k = 0; k < main.size() / headers.size(); k++) {
            for (int i = 0; i < headers.size(); i++) {
                listOfStrings.add(main.get(currentCol).replace("\"", "").replace(",", ""));
                currentCol++;
            }
            lists.add(new ArrayList<>(listOfStrings));
            listOfStrings.clear();
        }
        List<List<String>> eachColumn = new ArrayList<>(lists);
        while (!lists.isEmpty()) {
            for (int n = 0; n < lists.size(); n++) {
                tableView.getItems().add(lists.get(n));
                lists.remove(lists.get(n));
            }
        }
        tableView.setPrefSize(650, 600);
        tableView.setEditable(true);

        comboBox.setLayoutX(100);
        comboBox.setLayoutY(605);

        label.setLayoutX(10);
        label.setLayoutY(608);

        generate.setLayoutX(240);
        generate.setLayoutY(605);
        generate.setOnAction(event12 -> {
            if (comboBox.getValue() == null) {
                System.out.println("Show Some Error");
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("DataSelector.fxml"));
                try {
                    AnchorPane node = loader.load();
                    GraphDataHandler controller = loader.getController();
                    controller.setData(data);
                    controller.setGraphType(comboBox.getValue());
                    controller.setEachColumn(eachColumn);
                    controller.display();
                    Scene scene1 = new Scene(node);
                    stage.setScene(scene1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        load.setLayoutX(570);
        load.setLayoutY(605);
        load.setOnAction(event1 -> {
            Scene scene1 = new Scene(home);
            scene1.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene1);
        });

        root.getChildren().add(comboBox);
        root.getChildren().add(tableView);
        root.getChildren().add(label);
        root.getChildren().add(generate);
        root.getChildren().add(load);

        stage.setScene(scene);
        stage.setResizable(false);
    }
}
