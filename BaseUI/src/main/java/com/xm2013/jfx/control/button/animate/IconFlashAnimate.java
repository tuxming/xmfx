package com.xm2013.jfx.control.button.animate;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * 图标翻转动画
 */
public class IconFlashAnimate implements  XmButtonIconAnimate{

    private Node icon;
    private FadeTransition transition;

    public IconFlashAnimate(Node icon){

        FadeTransition fade = new FadeTransition();
        fade.setFromValue(0.3);
        fade.setToValue(1);
        fade.setCycleCount(6);
        fade.setDuration(Duration.millis(200));
        fade.setNode(icon);
        transition = fade;

//        ScaleTransition scale = new ScaleTransition();
//        scale.setFromX(0.75);
//        scale.setFromY(0.75);
//        scale.setToX(1);
//        scale.setToY(1);
//        scale.setCycleCount(3);
//        fade.setDuration(Duration.millis(100));
//
//        ParallelTransition transition = new ParallelTransition();
//        transition.getChildren().addAll(fade, scale);
//        transition.setNode(icon);

    }

    @Override
    public void start() {
        transition.setRate(1);
        transition.play();
    }

    @Override
    public void reverse() {

    }

    @Override
    public void dispose() {

    }
}
