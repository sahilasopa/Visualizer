package com.sahilasopa.visualizer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

public class PieChartHandler {
    @FXML
    private PieChart pie;

    public void setPieChartData(ObservableList<PieChart.Data> pieChartData) {
        pie.setData(pieChartData);
        pie.getData().forEach(d -> {
            Tooltip tip = new Tooltip();
            tip.setText(d.getPieValue() + "");
            Tooltip.install(d.getNode(), tip);
            System.out.println("LOL");
        });
    }
}
