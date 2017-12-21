package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import java.awt.*;

public class Car {

    private static final float TIME_STEP = 0.01f;

    private float v0;
    private float d0;
    private float l;
    private float ty;
    private float aa;
    private float ad;


    public Car(float v0, float d0, float l, float ty, float aa, float ad) {
        setV0(v0);
        this.d0 = d0;
        this.l = l;
        this.ty = ty;
        this.aa = aa;
        this.ad = ad;
    }

    private void setV0(float v0) {
        this.v0 = v0*1000/3600;
    }

    public LineChart getDistanceTimeChart(){
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Distance");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Distance Time diagram");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Velocity");
        //populating the series with data

        Boolean isAccelerating = isAccelerateSuggested();
        if(isAccelerating == null){
            System.out.println("Impossible Case");
            return null;
        }


        float t = 0;
        float v = v0;
        float d = 0;

        while (v >= 0 && d <= d0 + l){

            if(isAccelerating){
                v += aa*TIME_STEP;

                d = (v0 * t) + ((aa * t * t)/2);
                series.getData().add(new XYChart.Data(t, d));
            }
            else {
                v -= ad*TIME_STEP;
                d = (v0 * t) - ((ad * t * t)/2);
                series.getData().add(new XYChart.Data(t, d));
            }

            t += TIME_STEP;
        }

        lineChart.getData().add(series);

        //in loop take all series
        for (XYChart.Series<Number, Number> s : lineChart.getData()) {

            //for all series, take date, each data has Node (symbol) for representing point
            for (XYChart.Data<Number, Number> data : s.getData()) {
                // this node is StackPane
                StackPane stackPane = (StackPane) data.getNode();
                stackPane.setVisible(false);
            }
        }

        return lineChart;
    }

    public LineChart getSpeedTimeChart(){
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Speed");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Speed Time diagram");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Acceleration");
        //populating the series with data

        Boolean isAccelerating = isAccelerateSuggested();
        if(isAccelerating == null){
            System.out.println("Impossible Case");
            return null;
        }


        float t = 0;
        float v = v0;
        float d = 0;

        while (v >= 0 && d <= d0 + l){

            if(isAccelerating){
                v += aa*TIME_STEP;
                d = (v0 * t) + ((aa * t * t)/2);
                series.getData().add(new XYChart.Data(t, v));
            }
            else {
                v -= ad*TIME_STEP;
                d = (v0 * t) - ((ad * t * t)/2);
                series.getData().add(new XYChart.Data(t, v));
            }

            t += TIME_STEP;
        }

        lineChart.getData().add(series);

        //in loop take all series
        for (XYChart.Series<Number, Number> s : lineChart.getData()) {

            //for all series, take date, each data has Node (symbol) for representing point
            for (XYChart.Data<Number, Number> data : s.getData()) {
                // this node is StackPane
                StackPane stackPane = (StackPane) data.getNode();
                stackPane.setVisible(false);
            }
        }

        return lineChart;
    }

    private Boolean isAccelerateSuggested(){
        double distance = (v0 * ty) + ((aa * ty * ty)/2);
        if ( distance > (d0 + l)){
            // accelerate
            System.out.println("accelerate");
            return true;
        }
        else
        {
            distance = (v0 * ty) - ((ad * ty * ty)/2);
            // decelerate
            if ( distance <= d0){
                // accelerate
                System.out.println("decelerate");
                return false;
            }
        }
        return null;
    }
}
