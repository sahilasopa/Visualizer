package com.example.visualizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class PieChartHandler {
    @FXML
    private PieChart pie;
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    public ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

    public void setPieChartData(ObservableList<PieChart.Data> pieChartData) {
        this.pieChartData = pieChartData;
        pie.setData(pieChartData);
    }
}
