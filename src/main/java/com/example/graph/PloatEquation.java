
package  com.example.graph;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.Map;

import static com.example.graph.Graph.LOWER_BOUND;
import static com.example.graph.Graph.UPPER_BOUND;

public class PloatEquation{
    public static void plotEquation(HBox equationBox, String equation, Map<TextField, ArrayList<XYChart.Series<Number, Number>>> equationMap, LineChart<Number, Number> lineChart,Map<TextField, Color> colorMap,Map<TextField, Label> errorMap) {
        Circle c = (Circle) equationBox.getChildren().get(0);
        TextField tf = (TextField) equationBox.getChildren().get(2);


        try {
            EquationSolver EQ = new EquationSolver();
            ArrayList<XYChart.Series<Number, Number>> seriesArr = equationMap.get(tf);

            // Remove old series for this TextField only
            if (seriesArr != null) {
                lineChart.getData().removeAll(seriesArr);
                seriesArr.clear();
            } else {
                seriesArr = new ArrayList<>();
                equationMap.put(tf, seriesArr);
            }

            // Prepare first series
            XYChart.Series<Number, Number> series = new XYChart.Series<>();

            for (int i = 0; i <= (UPPER_BOUND - LOWER_BOUND) * 25; i++) {
                double x = LOWER_BOUND + i * 0.04;
                double y = EQ.parseEquation(equation, x);

                // If value is valid, add to current series
                if (!Double.isNaN(y) && y != Double.POSITIVE_INFINITY && y != Double.NEGATIVE_INFINITY) {
                    series.getData().add(new XYChart.Data<>(x, y));
                } else {
                    // Invalid value, save current series segment and start a new one
                    if (!series.getData().isEmpty()) seriesArr.add(series);
                    series = new XYChart.Series<>();
                }
            }

            // Add last series segment
            if (!series.getData().isEmpty()) seriesArr.add(series);

            // Add all series for this TextField
            lineChart.getData().addAll(seriesArr);

            // Assign color for this equation
            Color color = ColorPickerRand.assignColor(seriesArr,tf,colorMap);
            c.setFill(color);

            // Remove previous error message
            VBox parentVBox = (VBox) equationBox.getParent();
            if (errorMap.get(tf) != null) parentVBox.getChildren().remove(errorMap.get(tf));
            errorMap.remove(tf);

        } catch (Exception e) {
            c.setFill(Color.WHITE);
            displayErrorMessage("Not a valid equation.", tf,errorMap);
        }
    }

   public static void displayErrorMessage(String error, TextField tf,Map<TextField, Label> errorMap) {
        if (errorMap.get(tf) != null) return;
        Label errorLabel = new Label(error);
//        errorLabel.setStyle("-fx-text-fill: #FF0000;");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(new Font(15));
        errorLabel.setTranslateX(100);
        errorLabel.setTranslateY(-20);

        errorMap.put(tf, errorLabel);
        VBox parentVBox = (VBox) tf.getParent().getParent();
        parentVBox.getChildren().add(errorLabel);
        // hi testing
    }
}




