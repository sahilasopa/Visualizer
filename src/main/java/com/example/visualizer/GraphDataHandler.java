package com.example.visualizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphDataHandler {
    @FXML
    private Label title;

    @FXML
    private PieChart pie;

    @FXML
    private Button type;

    @FXML
    private ComboBox<String> values;

    @FXML
    private ComboBox<String> num;

    @FXML
    private ImageView image;

    private String graphType;

    private List<List<String>> eachColumn;
    private Map<String, List<String>> data;

    public List<List<String>> getEachColumn() {
        return eachColumn;
    }

    public void setEachColumn(List<List<String>> eachColumn) {
        this.eachColumn = eachColumn;
    }

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

    public void display() throws IOException {
        ObservableList<String> labels = FXCollections.observableArrayList();
        List<String> headers = data.get("headers");
        List<String> main = data.get("data");
        switch (graphType) {
            case "PIE CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/pie.png")).toExternalForm()));
            case "LINE GRAPH" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/line.png")).toExternalForm()));
            case "BAR CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/bar.png")).toExternalForm()));
        }
        title.setText("You Have Chosen " + getGraphType());
        labels.addAll(getData().get("headers"));
        values.setItems(labels);
        validateData(new ArrayList<>(getEachColumn()), 1, 4);
    }

    public void validateData(List<List<String>> list, int col1, int col2) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<String> keysAdded = new ArrayList<>();
        for (List<String> strings : list) {
            if (keysAdded.contains(strings.get(col1))) {
                for (PieChart.Data data : pieChartData) {
                    String key = data.getName();
                    double value = data.getPieValue();
                    if (key.equals(strings.get(col1))) {
                        value += Double.parseDouble(strings.get(col2));
                    }
                    data.setPieValue(value);
                }
            } else {
                pieChartData.add(new PieChart.Data(strings.get(col1), Double.parseDouble(strings.get(col2))));
                keysAdded.add(strings.get(col1));
            }
        }
        System.out.println(pieChartData.sorted());
        pie.setData(pieChartData.sorted());
        image.setVisible(false);
        pie.setVisible(true);
    }
}
//                    if(s.contains(strings.get(col1))){
//                            double num=Double.parseDouble(s.get(1));
//                            num+=Double.parseDouble(strings.get(col2));
//                            pieChartData.removeIf(data->Objects.equals(data.getName(),strings.get(col1)));
//                            pieChartData.add(new PieChart.Data(strings.get(col1),num));
//                            break;
//                            }

