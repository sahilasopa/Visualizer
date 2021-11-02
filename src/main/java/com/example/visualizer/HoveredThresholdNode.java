package com.example.visualizer;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HoveredThresholdNode extends AnchorPane {

    public HoveredThresholdNode(Object x, Object y) {

        final Label label = createDataThresholdLabel(x, y);

        setOnMouseEntered(mouseEvent -> {
            getChildren().setAll(label);
            setCursor(Cursor.NONE);
            toFront();
        });
        setOnMouseExited(mouseEvent -> getChildren().clear());
    }

    private Label createDataThresholdLabel(Object x, Object y) {
        final Label label = new Label(String.valueOf(x).concat("," + y));
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
