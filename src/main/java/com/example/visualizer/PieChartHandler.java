package com.example.visualizer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class PieChartHandler {
    @FXML
    private PieChart pie;

    public void setPieChartData(ObservableList<PieChart.Data> pieChartData) {
        pie.setData(pieChartData);
    }
}
