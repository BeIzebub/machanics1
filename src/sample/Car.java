package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class Car implements Initializable {
    private static final float TIME_STEP = 0.01f;
    @FXML
    private Label v0Label;
    @FXML
    private Slider v0Slider;
    @FXML
    private Label d0Label;
    @FXML
    private Slider d0Slider;
    @FXML
    private Label lLabel;
    @FXML
    private Slider lSlider;
    @FXML
    private Label tyLabel;
    @FXML
    private Slider tySlider;
    @FXML
    private Label aaLabel;
    @FXML
    private Slider aaSlider;
    @FXML
    private Label adLabel;
    @FXML
    private Slider adSlider;
    @FXML
    private Label result;
    @FXML
    private VBox graphs;

    private float v0;
    private float d0;
    private float l;
    private float ty;
    private float aa;
    private float ad;

    private LineChartWithMarkers<Number, Number> distanceChart;
    private LineChartWithMarkers<Number, Number> speedChart;
    private float regionStart = 0;
    private float regionEnd = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setV0(50);
        v0Slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                v0Label.setText(String.format("%.2f", newValue));
                Car.this.setV0(newValue.floatValue());
            }
        });
        sliderInit(50, v0Slider);
        this.d0 = 30;
        d0Slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                d0Label.setText(String.format("%.2f", newValue));
                d0 = newValue.floatValue();
            }
        });
        sliderInit(this.d0, d0Slider);
        this.l = 30;
        lSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                lLabel.setText(String.format("%.2f", newValue));
                l = newValue.floatValue();
            }
        });
        sliderInit(this.l, lSlider);
        this.ty = 3;
        tySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tyLabel.setText(String.format("%.2f", newValue));
                ty = newValue.floatValue();
            }
        });
        sliderInit(this.ty, tySlider);
        this.aa = 3;
        aaSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                aaLabel.setText(String.format("%.2f", newValue));
                aa = newValue.floatValue();
            }
        });
        sliderInit(this.aa, aaSlider);
        this.ad = 3;
        adSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                adLabel.setText(String.format("%.2f", newValue));
                ad = newValue.floatValue();
            }
        });
        sliderInit(this.ad, adSlider);

        initGraphs();
    }

    private void setV0(float v0) {
        this.v0 = v0 * 1000 / 3600;
    }

    private void initGraphs(){

        initDistanceGraph();
        initSpeedGraph();

        updateGraphs();

        graphs.getChildren().addAll(distanceChart, speedChart);
    }

    private void initDistanceGraph(){
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Distance");
        //creating the chart
        distanceChart = new LineChartWithMarkers<>(xAxis, yAxis);

        distanceChart.setTitle("Distance Time diagram");

    }

    private void initSpeedGraph(){
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Speed");
        //creating the chart
        speedChart = new LineChartWithMarkers<>(xAxis, yAxis);

        speedChart.setTitle("Speed Time diagram");
    }

    public boolean updateDistanceTimeChart() {

        distanceChart.clear();

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Velocity");
        //populating the series with data

        Boolean isAccelerating = isAccelerateSuggested();
        if (isAccelerating == null) {
            System.out.println("Impossible Case");
            return false;
        }

        float t = 0;
        float v = v0;
        float d = 0;

        while (v >= 0 && d <= d0 + l) {
            if (isAccelerating) {
                v += aa * TIME_STEP;
                d = (v0 * t) + ((aa * t * t) / 2);
                series.getData().add(new Data(t, d));
            } else {
                v -= ad * TIME_STEP;
                d = (v0 * t) - ((ad * t * t) / 2);
                series.getData().add(new Data(t, d));
            }

            if(regionStart == 0 && d >= d0){
                regionStart = t;
            }

            if(regionEnd == 0 && d >= d0 + l){
                regionEnd = t;
            }

            t += TIME_STEP;
        }

        distanceChart.getData().add(series);

        Data<Number, Number> verticalMarker1 = new Data<>(regionStart, 0);
        distanceChart.addVerticalValueMarker(verticalMarker1);

        Data<Number, Number> verticalMarker2 = new Data<>(regionEnd, 0);
        distanceChart.addVerticalValueMarker(verticalMarker2);


        //in loop take all series
//        for (XYChart.Series<Number, Number> s : distanceChart.getData()) {
//
//            //for all series, take date, each data has Node (symbol) for representing point
//            for (Data<Number, Number> data : s.getData()) {
//                // this node is StackPane
//                StackPane stackPane = (StackPane) data.getNode();
//                stackPane.setVisible(false);
//            }
//        }

        return true;
    }

    public boolean updateSpeedTimeChart() {

        speedChart.clear();

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Acceleration");
        //populating the series with data

        Boolean isAccelerating = isAccelerateSuggested();
        if (isAccelerating == null) {
            System.out.println("Impossible Case");
            return false;
        }


        float t = 0;
        float v = v0;
        float d = 0;

        while (v >= 0 && d <= d0 + l) {

            if (isAccelerating) {
                v += aa * TIME_STEP;
                d = (v0 * t) + ((aa * t * t) / 2);
                series.getData().add(new Data(t, v));
            } else {
                v -= ad * TIME_STEP;
                d = (v0 * t) - ((ad * t * t) / 2);
                series.getData().add(new Data(t, v));
            }
            t += TIME_STEP;
        }

        speedChart.getData().add(series);

        Data<Number, Number> verticalMarker1 = new Data<>(regionStart, 0);
        speedChart.addVerticalValueMarker(verticalMarker1);

        Data<Number, Number> verticalMarker2 = new Data<>(regionEnd, 0);
        speedChart.addVerticalValueMarker(verticalMarker2);

        //in loop take all series
//        for (XYChart.Series<Number, Number> s : speedChart.getData()) {
//
//            //for all series, take date, each data has Node (symbol) for representing point
//            for (Data<Number, Number> data : s.getData()) {
//                // this node is StackPane
//                StackPane stackPane = (StackPane) data.getNode();
//                stackPane.setVisible(false);
//            }
//        }

        return true;
    }

    private Boolean isAccelerateSuggested() {
        double distance = (v0 * ty) + ((aa * ty * ty) / 2);
        if (distance > (d0 + l)) {
            // accelerate
            System.out.println("accelerate");
            return true;
        } else {
            distance = (v0 * ty) - ((ad * ty * ty) / 2);
            // decelerate
            if (distance <= d0) {
                // accelerate
                System.out.println("decelerate");
                return false;
            }
        }
        return null;
    }

    private void sliderInit(double val, Slider slider) {
        slider.adjustValue(val);
        slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean changing) {
                if (wasChanging) {
                    updateGraphs();
                }
            }
        });
    }

    private void updateGraphs(){

        regionStart = 0;
        regionEnd = 0;

        Boolean accelerationSuggested = isAccelerateSuggested();

        if(accelerationSuggested == null){
            result.setText("Impossible");
            distanceChart.clear();
            speedChart.clear();
        }
        else {
            updateDistanceTimeChart();
            updateSpeedTimeChart();
            if (accelerationSuggested) {
                result.setText("Accelerate");
            } else {
                result.setText("Decelerate");
            }
        }
    }

    private class LineChartWithMarkers<X,Y> extends LineChart {

        private ObservableList<Data<X, Y>> horizontalMarkers;
        private ObservableList<Data<X, Y>> verticalMarkers;

        public LineChartWithMarkers(Axis<X> xAxis, Axis<Y> yAxis) {
            super(xAxis, yAxis);
            horizontalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.YValueProperty()});
            horizontalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());
            verticalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
            verticalMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());
        }

        public void addHorizontalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (horizontalMarkers.contains(marker)) return;
            Line line = new Line();
            marker.setNode(line );
            getPlotChildren().add(line);
            horizontalMarkers.add(marker);
        }

        public void removeHorizontalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
            horizontalMarkers.remove(marker);
        }

        public void addVerticalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (verticalMarkers.contains(marker)) return;
            Line line = new Line();
            marker.setNode(line );
            getPlotChildren().add(line);
            verticalMarkers.add(marker);
        }

        public void removeVerticalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
            verticalMarkers.remove(marker);
        }

        public void clear(){
            getData().clear();
            for (Data<X, Y> marker: verticalMarkers) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
            for (Data<X, Y> marker: horizontalMarkers) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }

            verticalMarkers.clear();
            horizontalMarkers.clear();
        }


        @Override
        protected void layoutPlotChildren() {
            super.layoutPlotChildren();
            for (Data<X, Y> horizontalMarker : horizontalMarkers) {
                Line line = (Line) horizontalMarker.getNode();
                line.setStartX(0);
                line.setEndX(getBoundsInLocal().getWidth());
                line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5); // 0.5 for crispness
                line.setEndY(line.getStartY());
                line.toFront();
            }
            for (Data<X, Y> verticalMarker : verticalMarkers) {
                Line line = (Line) verticalMarker.getNode();
                line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5);  // 0.5 for crispness
                line.setEndX(line.getStartX());
                line.setStartY(0d);
                line.setEndY(getBoundsInLocal().getHeight());
                line.toFront();
            }
        }

    }
}
