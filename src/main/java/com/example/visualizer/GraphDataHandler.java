package com.example.visualizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphDataHandler {
    @FXML
    private Button generate;

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

    @FXML
    private Button load;

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
        List<String> graphs = List.of("PIE CHART", "LINE GRAPH");
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
                } else if (getGraphType().equals("LINE GRAPH")) {
                    lineGraph(headers.indexOf(values.getValue()), headers.indexOf(num.getValue()));
                }
            }
        });
        load.setOnAction(event -> {
            AnchorPane home = null;
            try {
                home = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert home != null;
            Scene scene1 = new Scene(home);
            scene1.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            Stage stage = new Stage();
            stage.setScene(scene1);
            stage.show();
        });
    }

    private void setPreviewImage() {
        switch (graphType) {
            case "PIE CHART" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/pie.png")).toExternalForm()));
            case "LINE GRAPH" -> image.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/line.png")).toExternalForm()));
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
                    throw new NumberFormatException("Please Select Numeric Data For Keys");
                }

            }
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("pieChart.fxml"));
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

    public void lineGraph(int x, int y) {
        NumberAxis xAxis = new NumberAxis();

        NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> dataSeries1 = new XYChart.Series<>();

        xAxis.setLabel(getData().get("headers").get(x));
        yAxis.setLabel(getData().get("headers").get(y));
        lineChart.setTitle("Line Chart");
        for (List<String> strings : new ArrayList<>(getEachColumn())) {
            XYChart.Data<Number, Number> data;
            try {
                data = new XYChart.Data<>(Double.parseDouble(strings.get(x)), Double.parseDouble(strings.get(y)));
                data.setNode(new HoveredThresholdNode(Double.parseDouble(strings.get(x)), Double.parseDouble(strings.get(y))));
            } catch (NumberFormatException e) {
                data = new XYChart.Data<>(0, 0);
                data.setNode(new HoveredThresholdNode(0, 0));
            }

            dataSeries1.getData().add(data);
        }

        lineChart.getData().add(dataSeries1);
        AnchorPane.setBottomAnchor(lineChart, 0.0);
        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("lineChart.fxml"));
        try {
            AnchorPane node = loader.load();
            Stage stage = new Stage();
            Scene graph = new Scene(node);
            node.getChildren().add(lineChart);
            stage.setScene(graph);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}