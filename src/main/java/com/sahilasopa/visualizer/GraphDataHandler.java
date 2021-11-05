package com.sahilasopa.visualizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
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
    private final Alert alert = new Alert(Alert.AlertType.WARNING, "Please Select Valid Values For Keys", ButtonType.OK);
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        ObservableList<String> items = FXCollections.observableArrayList();
        List<String> graphs = List.of("PIE CHART", "LINE GRAPH", "BAR CHART");
        items.addAll(graphs);
        type.setItems(items);
        type.getSelectionModel().select(items.indexOf(getGraphType()));
        ObservableList<String> labels = FXCollections.observableArrayList();
        List<String> headers = data.get("headers");
        validateGraphType();
        title.setText("You Have Chosen " + getGraphType());
        labels.addAll(getData().get("headers"));
        values.setItems(labels);
        num.setItems(labels);
        type.setOnAction(event -> validateGraphType());
        generate.setOnAction(event -> {
            if (values.getValue() == null) {
                alert.setContentText("Please Select Some Value From Dropdown");
                alert.show();
            } else if (num.getValue() == null) {
                alert.setContentText("Please Select Some Value From Dropdown");
                alert.show();
            } else if (Objects.equals(num.getValue(), values.getValue())) {
                alert.setContentText("Please Select Different Values From The Dropdown");
                alert.show();
            } else {
                switch (getGraphType()) {
                    case "PIE CHART":
                        try {
                            pieChart(new ArrayList<>(getEachColumn()), headers.indexOf(values.getValue()), headers.indexOf(num.getValue()));
                        } catch (NumberFormatException | IOException e) {
                            alert.setContentText("Please Select Numeric Values For Data");
                            alert.show();
                        }
                        break;
                    case "LINE GRAPH":
                        lineGraph(headers.indexOf(values.getValue()), headers.indexOf(num.getValue()));
                        break;
                    case "BAR CHART":
                        barChart(new ArrayList<>(getEachColumn()), headers.indexOf(values.getValue()), headers.indexOf(num.getValue()));
                        break;
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

    private void validateGraphType() {
        setGraphType(type.getValue());
        title.setText("You Have Chosen " + getGraphType());
        setPreviewImage();
        if (getGraphType().equals("LINE GRAPH") || getGraphType().equals("BAR CHART")) {
            labelText.setText("X-Axis");
            valueText.setText("Y-Axis");
        } else if (getGraphType().equals("PIE CHART")) {
            labelText.setText("Label Values");
            valueText.setText("Numeric Values");
        }
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
                    alert.show();
                }
            } else {
                try {
                    pieChartData.add(new PieChart.Data(strings.get(col1).trim(), Double.parseDouble(strings.get(col2))));
                    keysAdded.add(strings.get(col1));
                } catch (NumberFormatException e) {
                    alert.show();
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

    public void barChart(List<List<String>> list, int col1, int col2) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> barChart =
                new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        List<String> keysAdded = new ArrayList<>();
        for (List<String> strings : list) {
            if (keysAdded.contains(strings.get(col1))) {
                try {
                    for (XYChart.Data<String, Number> stringNumberData : series1.getData()) {
                        if (stringNumberData.getXValue().equals(strings.get(col1))) {
                            double val = Double.parseDouble(String.valueOf(stringNumberData.getYValue()));
                            val += Double.parseDouble(strings.get(col2));
                            stringNumberData.setYValue(val);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                series1.getData().add(new XYChart.Data<>(strings.get(col1), Double.parseDouble(strings.get(col2))));
                keysAdded.add(strings.get(col1));
            }
        }
        Stage stage = new Stage();
        Scene scene = new Scene(barChart);
        barChart.getData().add(series1);
        stage.setScene(scene);
        stage.show();
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> item : series.getData()) {
                Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ":\n" + item.getYValue()));
            }
        }
    }
}