package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
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
        graphs.getChildren().addAll(getDistanceTimeChart(), getSpeedTimeChart());
    }

    private void setV0(float v0) {
        this.v0 = v0 * 1000 / 3600;
    }

    public LineChart getDistanceTimeChart() {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Distance");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Distance Time diagram");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Velocity");
        //populating the series with data

        Boolean isAccelerating = isAccelerateSuggested();
        if (isAccelerating == null) {
            System.out.println("Impossible Case");
            return null;
        }


        float t = 0;
        float v = v0;
        float d = 0;

        while (v >= 0 && d <= d0 + l) {
            if (isAccelerating) {
                v += aa * TIME_STEP;
                d = (v0 * t) + ((aa * t * t) / 2);
                series.getData().add(new XYChart.Data(t, d));
            } else {
                v -= ad * TIME_STEP;
                d = (v0 * t) - ((ad * t * t) / 2);
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

    public LineChart getSpeedTimeChart() {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Speed");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Speed Time diagram");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Acceleration");
        //populating the series with data

        Boolean isAccelerating = isAccelerateSuggested();
        if (isAccelerating == null) {
            System.out.println("Impossible Case");
            return null;
        }


        float t = 0;
        float v = v0;
        float d = 0;

        while (v >= 0 && d <= d0 + l) {

            if (isAccelerating) {
                v += aa * TIME_STEP;
                d = (v0 * t) + ((aa * t * t) / 2);
                series.getData().add(new XYChart.Data(t, v));
            } else {
                v -= ad * TIME_STEP;
                d = (v0 * t) - ((ad * t * t) / 2);
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
        //karen
        System.out.println("boobs");
    }
}
