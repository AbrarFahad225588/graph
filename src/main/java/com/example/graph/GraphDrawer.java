
package com.example.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.function.Function;

public class GraphDrawer extends Canvas {
    private final double scaleX = 50, scaleY = 50;
    private final double centerX, centerY;

    public GraphDrawer(double width, double height) {
        super(width, height);
        centerX = width / 2;
        centerY = height / 2;
    }

    public void drawAxes() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeLine(0, centerY, getWidth(), centerY);
        gc.strokeLine(centerX, 0, centerX, getHeight());
    }

    public void plot(Function<Double, Double> func, Color color) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(2);

        double prevX = -getWidth() / (2 * scaleX);
        double prevY = func.apply(prevX);

        for (double x = prevX; x <= getWidth() / (2 * scaleX); x += 0.01) {
            double y = func.apply(x);
            gc.strokeLine(
                    centerX + prevX * scaleX, centerY - prevY * scaleY,
                    centerX + x * scaleX, centerY - y * scaleY
            );
            prevX = x;
            prevY = y;
        }
    }

    public void clear() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }
}
