package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Histogram {

    @FXML
    private CategoryAxis categoryax;

    @FXML
    private TextField  title;

    @FXML
    private NumberAxis numberax;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(javafx.event.ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }

    XYChart.Series<LocalDate, Number> series = new XYChart.Series<>();
    public static List<Date> dates = new ArrayList<>();
    public static List<Integer> numbers = new ArrayList<>();
    public static Map<LocalDate, Integer> dateCounts = new HashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML
    public void initialize() {
        updateBarChart();
        title.setEditable(false);
        if (Distresscalloption.type_calls == 0)
            title.setText("My Community Distress Calls");
        else  title.setText("All Distress Calls");
    }

    // Function to populate the bar chart based on dateCounts
    private void updateBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        dateCounts.forEach((date, count) -> {
            String dateString = formatter.format(date); // Convert LocalDate to String
            series.getData().add(new XYChart.Data<>(dateString, count));
        });
        barChart.getData().clear();  // Clear previous data
        barChart.getData().add(series);  // Add new data
    }

    // Example setter method to update dateCounts and refresh the chart
    public void setDateCounts(Map<LocalDate, Integer> newCounts) {
        dateCounts = new HashMap<>(newCounts);  // Update the counts
        updateBarChart();  // Refresh the chart
    }

    @FXML
    void handleAction(javafx.event.ActionEvent actionEvent) {
        // Handle any action events here
    }

    @FXML
    void bbb(ActionEvent event) {

    }

    public void bbb(javafx.event.ActionEvent actionEvent) throws IOException {
        App.setRoot("Distresscalloption");
    }
}