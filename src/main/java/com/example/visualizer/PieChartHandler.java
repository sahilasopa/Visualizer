package com.example.visualizer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

public class PieChartHandler {
    @FXML
    private PieChart pie;

    public void setPieChartData(ObservableList<PieChart.Data> pieChartData) {
        pie.setData(pieChartData);
        pie.getData().forEach(data -> {
            String percentage = String.format("% 2f%%", data.getPieValue() / 100);
            Tooltip tooltip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), tooltip);
        });
    }
}
