//package com.example.graph.plot;
//
//import javafx.scene.Node;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.TextField;
//import javafx.scene.paint.Color;
//
//import java.util.*;
//
//public class ColorPickerRand {
//    private static final ArrayList<Color> alreadyChosen = new ArrayList<>();
//    public static Color assignColor(ArrayList<XYChart.Series<Number, Number>> seriesArr, TextField tf, Map<TextField,Color> colorMap) {
//        Color seriesColor = colorMap.computeIfAbsent(tf, k-> getRandomLineColor(new Random()));
//
//        for (XYChart.Series<Number, Number> s : seriesArr) {
//            Node currLine = s.getNode().lookup(".chart-series-line");
//            if (currLine != null) {
//                String rgb = String.format("%d, %d, %d",
//                        (int) (seriesColor.getRed() * 255),
//                        (int) (seriesColor.getGreen() * 255),
//                        (int) (seriesColor.getBlue() * 255));
//                currLine.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
//            }
//        }
//        return seriesColor;
//    }
//    private static Color getRandomLineColor(Random rand) {
//        List<Color> colorList = Arrays.asList(Color.AQUA, Color.CHOCOLATE, Color.MEDIUMPURPLE, Color.DEEPPINK,
//                Color.DARKCYAN, Color.INDIGO, Color.DARKMAGENTA, Color.MEDIUMBLUE, Color.LIGHTPINK,
//                Color.DARKGREEN, Color.LIGHTSALMON, Color.TOMATO, Color.RED, Color.LIGHTSEAGREEN,
//                Color.FIREBRICK, Color.PLUM);
//
//        int randIndex;
//        Color randColor;
//        do {
//            randIndex = rand.nextInt(colorList.size());
//            randColor = colorList.get(randIndex);
//            if (alreadyChosen.size() == colorList.size()) alreadyChosen.clear();
//        } while (alreadyChosen.contains(randColor));
//
//        alreadyChosen.add(randColor);
//        return randColor;
//    }
//
//}
