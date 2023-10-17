package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import com.xm2013.jfx.control.button.XmButtonType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LoadingProgressBarAnimate implements XmButtonLoadingAnimate {

    private XmButtonSkin skin;
    private XmButton control;
    private Text text;
    private Node icon;

    private Pane pane;
    private Pane bar;
    private Timeline timeline;
    private Text label;

    private DoubleProperty width = new SimpleDoubleProperty(0);
    private DoubleProperty height = new SimpleDoubleProperty(0);

    private double times = 0;
    private boolean isAdd = true;

    private ChangeListener<Number> loadingPercentListener = (ob, ov, nv) -> {

        if(label!=null && control.isShowLoadingPercent()){
            label.setText(String.format("%.2f%%", nv.doubleValue() * 100));
        }

    };

    private ChangeListener<Boolean> loadingListener = (ob, ov, nv) -> {
        if(!nv){
            timeline.stop();
            setVisible(false);
        } else{
            control.setLoadingPercent(0);
        }
    };

    public LoadingProgressBarAnimate(XmButtonSkin skin, Text text, Node icon){
        this.skin = skin;
        this.control = (XmButton) skin.getSkinnable();
        this.text = text;
        this.icon = icon;

        init();
    }

    private void init(){

        XmButtonType.BtnLoadingType loadingType = control.getLoadingType();

        double w = control.getPrefWidth();
        if(w <= 0){
            w = control.prefWidth(-1);
        }

        double h = control.getPrefHeight();
        if(h<=0){
            h = control.prefHeight(-1);
        }
        height.set(h);
        width.set(w);

        bar = new Pane();
        bar.prefWidthProperty().bind(control.prefWidthProperty());
        bar.prefHeightProperty().bind(control.prefHeightProperty());

        Color color = getFillColor();
        Color centerColor = FxKit.deriveColor(color, 0.5);

        StackPane sp = new StackPane();

        pane = sp;
        pane.backgroundProperty().bind(control.backgroundProperty());
        pane.borderProperty().bind(control.borderProperty());
        pane.prefWidthProperty().bind(control.prefWidthProperty());
        pane.prefHeightProperty().bind(control.prefHeightProperty());
        pane.getChildren().add(bar);

        if(control.isShowLoadingPercent()){
            label = new Text("loading...");
            label.fillProperty().bind(control.textFillProperty());
            label.fontProperty().bind(control.fontProperty());
            pane.getChildren().add(label);
        }

        skin.getChildren().add(pane);
        skin.positionInArea1(pane, 0, 0, control.prefWidth(-1), control.prefHeight(-1));

        control.loadingPercentProperty().addListener(loadingPercentListener);
        control.loadingProperty().addListener(loadingListener);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), event -> {
            List<Stop> stops = new ArrayList<>();

            double end = control.getLoadingPercent();

            stops.add(new Stop(0, color));
            stops.add(new Stop(times*end, centerColor));
            stops.add(new Stop(end-0.00002, color));
            stops.add(new Stop(end-0.00001, Color.TRANSPARENT));
            stops.add(new Stop(end, Color.TRANSPARENT));

            LinearGradient lg = null;

            if(XmButtonType.BtnLoadingType.H_PROGRESS.equals(loadingType)){
                lg =new LinearGradient(0, 0, 1, 0,
                        true, CycleMethod.NO_CYCLE, stops);
            }else{
                lg = new LinearGradient(0, 1, 0, 0,
                        true, CycleMethod.NO_CYCLE, stops);
            }

            RoundType roundType = control.getRoundType();
            double radii = 0;
            //计算圆角大小
            if(RoundType.SMALL.equals(roundType)){
                radii = 4d;
            }else if(RoundType.SEMICIRCLE.equals(roundType)){
                radii = 50d;
            }else if(RoundType.CIRCLE.equals(roundType)){
//            double raius = Math.max(control.prefHeight(-1), control.prefWidth(-1));
                radii = 1000d;
            }

            BackgroundFill fill = new BackgroundFill(lg, new CornerRadii(radii), Insets.EMPTY);
            bar.setBackground(new Background(fill));

            if(isAdd){
                times+=0.1;
            }else {
                times-=0.1;
            }

            if(times<=0){
                isAdd = true;
            }else if(times>=0.9){
                isAdd = false;
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
    }

    @Override
    public void showLoading() {
        timeline.play();
        setVisible(true);
    }

    private Color getFillColor(){
        Color color = null;
        try{
            Paint paint = control.getBackground().getFills().get(0).getFill();
            if(paint instanceof LinearGradient){
                LinearGradient lg = (LinearGradient) paint;
                color = lg.getStops().get(0).getColor();
            }else if(paint instanceof  RadialGradient){
                RadialGradient rg = (RadialGradient) paint;
                color = rg.getStops().get(0).getColor();
            }

            if(HueType.LIGHT.equals(control.getHueType())){
                color = FxKit.deriveColor(color, 0.1);
            }else{
                color = FxKit.deriveColor(color, -0.1);
            }
        }catch (Exception e){

        }

        if(color == null){
            Paint paint = control.getColorType().getPaint();
            if(paint instanceof LinearGradient){
                LinearGradient lg = (LinearGradient) paint;
                color = lg.getStops().get(0).getColor();
            }else if(paint instanceof RadialGradient){
                RadialGradient rg = (RadialGradient) paint;
                color = rg.getStops().get(0).getColor();
            }else{
                color = (Color) paint;
            }

            if(HueType.LIGHT.equals(control.getHueType())){
                color = FxKit.deriveColor(color, 0.4);
            }else{
                color = FxKit.getLightColor(color, 0.88);
            }
        }

        return color;
    }

    private void setVisible(boolean v){

        if(text != null) text.setVisible(!v);
        if(icon != null) icon.setVisible(!v);
        pane.setVisible(v);

    }

    @Override
    public void dispose() {
        this.skin.getChildren().remove(pane);

        pane.backgroundProperty().unbind();
        pane.borderProperty().unbind();

        control.loadingPercentProperty().removeListener(loadingPercentListener);
        control.loadingProperty().removeListener(loadingListener);

        if(label != null){
            label.fillProperty().unbind();
            label.fontProperty().unbind();
        }

    }
}
