package com.github.hoang2111.hoangsapp;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsManager {

    private Stage primaryStage;
    private DatabaseManager databaseManager;
    private MainApp mainApp;

    public AnalyticsManager(Stage primaryStage, DatabaseManager databaseManager, MainApp mainApp) {
        this.primaryStage = primaryStage;
        this.databaseManager = databaseManager;
        this.mainApp = mainApp;
    }

    public void showAnalytics() {
        VBox analyticsLayout = new VBox(20);

        // Date range filter
        DatePicker startDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
        DatePicker endDatePicker = new DatePicker(LocalDate.now());
        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> updateCharts(analyticsLayout, startDatePicker.getValue(), endDatePicker.getValue()));

        analyticsLayout.getChildren().addAll(startDatePicker, endDatePicker, filterButton);

        // Initial chart update
        updateCharts(analyticsLayout, startDatePicker.getValue(), endDatePicker.getValue());

        Scene analyticsScene = new Scene(analyticsLayout, 800, 600);
        primaryStage.setScene(analyticsScene);
    }

    private void updateCharts(VBox layout, LocalDate startDate, LocalDate endDate) {
        List<SessionData> sessions = databaseManager.getAllSessions().stream()
                .filter(session -> !session.getSessionDate().toLocalDate().isBefore(startDate) && !session.getSessionDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());

        layout.getChildren().clear();

        // Bar chart: Total meditation time
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Total Meditation Time");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (SessionData session : sessions) {
            series.getData().add(new XYChart.Data<>(session.getSessionDate().toString(), session.getDuration()));
        }
        barChart.getData().add(series);

        // Line chart: Daily meditation time
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Daily Meditation Time");

        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        sessions.forEach(session -> lineSeries.getData().add(new XYChart.Data<>(session.getSessionDate().toString(), session.getDuration())));
        lineChart.getData().add(lineSeries);

        // Pie chart: Session types
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Session Types");

        sessions.stream().collect(Collectors.groupingBy(SessionData::getSessionType, Collectors.counting()))
                .forEach((type, count) -> pieChart.getData().add(new PieChart.Data(type, count)));

        // Add charts to layout
        layout.getChildren().addAll(barChart, lineChart, pieChart);

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));
        layout.getChildren().add(backButton);
    }
}