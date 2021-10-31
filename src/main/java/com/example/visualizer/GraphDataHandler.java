package com.example.visualizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphDataHandler {
    @FXML
    Button generate;

    @FXML
    private Label title;

    @FXML
    private Label labelText;

    @FXML
    private Label valueText;

    @FXML
    private ComboBox<String> type;

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
        ObservableList<String> items = FXCollections.observableArrayList();
        List<String> graphs = List.of("PIE CHART", "LINE GRAPH", "BAR CHART");
        items.addAll(graphs);
        type.setItems(items);
        type.getSelectionModel().select(items.indexOf(getGraphType()));
        ObservableList<String> labels = FXCollections.observableArrayList();
        List<String> headers = data.get("headers");
        setPreviewImage();
        title.setText("You Have Chosen " + getGraphType());
        labels.addAll(getData().get("headers"));
        values.setItems(labels);
        num.setItems(labels);
        type.setOnAction(event -> {
            setGraphType(type.getValue());
            title.setText("You Have Chosen " + getGraphType());
            setPreviewImage();
            if (getGraphType().equals("LINE GRAPH")) {
                labelText.setText("X-Axis");
                valueText.setText("Y-Axis");
            } else if (getGraphType().equals("PIE CHART")) {
                labelText.setText("Label Values");
                valueText.setText("Numeric Values");
            }
        });
        generate.setOnAction(event -> {

            if (values.getValue() == null) {
                System.out.println("Show Error");
            } else if (num.getValue() == null) {
                System.out.println("Show Error");
            } else if (Objects.equals(num.getValue(), values.getValue())) {
                System.out.println("show error");
            } else {
                if (getGraphType().equals("PIE CHART")) {
                    try {
                        pieChart(new ArrayList<>(getEachColumn()), headers.indexOf(values.getValue()), headers.indexOf(num.getValue()));
                    } catch (NumberFormatException | IOException e) {
                        System.out.println("Number Values Only");
                    }
                } else if (getGraphType().equals("LINE CHART")) {
                }
            }
        });
    }

    private void setPreviewImage() {
        switch (graphType) {
            case "PIE CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/pie.png")).toExternalForm()));
            case "LINE GRAPH" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/line.png")).toExternalForm()));
            case "BAR CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/bar.png")).toExternalForm()));
        }
    }

    public void pieChart(List<List<String>> list, int col1, int col2) throws IOException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<String> keysAdded = new ArrayList<>();
        for (List<String> strings : list) {
            if (keysAdded.contains(strings.get(col1))) {
                try {
                    for (PieChart.Data data : pieChartData) {
                        String key = data.getName().trim();
                        double value = data.getPieValue();
                        if (key.equals(strings.get(col1))) {
                            value += Double.parseDouble(strings.get(col2));
                        }
                        data.setName(key);
                        data.setPieValue(value);
                    }
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Please Select Numeric Data For Keys");
                }
            } else {
                try {
                    pieChartData.add(new PieChart.Data(strings.get(col1).trim(), Double.parseDouble(strings.get(col2))));
                    keysAdded.add(strings.get(col1));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Pl/ease Select Numeric Data For Keys");
                }

            }
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chart.fxml"));
        try {
            Parent node = loader.load();
            PieChartHandler controller = loader.getController();
            controller.setPieChartData(pieChartData);
            Stage stage = new Stage();
            Scene graph = new Scene(node);
            stage.setScene(graph);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lineGraph(List<List<String>> list) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));
        series.getData().add(new XYChart.Data<>(6, 36));
        series.getData().add(new XYChart.Data<>(7, 22));
        series.getData().add(new XYChart.Data<>(8, 45));
        series.getData().add(new XYChart.Data<>(9, 43));
        series.getData().add(new XYChart.Data<>(10, 17));
        series.getData().add(new XYChart.Data<>(11, 29));
        series.getData().add(new XYChart.Data<>(12, 25));

        lineChart.getData().add(series);
    }
}

