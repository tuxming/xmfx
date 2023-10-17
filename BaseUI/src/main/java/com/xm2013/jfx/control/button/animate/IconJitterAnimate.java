package com.xm2013.jfx.control.button.animate;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * 图标跳动动画
 */
public class IconJitterAnimate implements  XmButtonIconAnimate{

    private Node icon;
    private TranslateTransition transition;

    public IconJitterAnimate(Node icon){

        transition = new TranslateTransition();
        transition.setNode(icon);
        transition.setAutoReverse(true);
        transition.setCycleCount(6);
        transition.setDuration(Duration.millis(150));
        transition.setFromY(0);
        transition.setToY(-6);
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
