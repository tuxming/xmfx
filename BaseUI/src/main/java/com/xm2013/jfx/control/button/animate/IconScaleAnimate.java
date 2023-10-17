package com.xm2013.jfx.control.button.animate;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * 图标放大动画
 */
public class IconScaleAnimate implements  XmButtonIconAnimate{

    private Node icon;
    private ScaleTransition transition;

    public IconScaleAnimate(Node icon){
        transition = new ScaleTransition();
        transition.setNode(icon);
        transition.setDuration(Duration.millis(100));
        transition.setFromX(1);
        transition.setFromY(1);
        transition.setToX(1.35);
        transition.setToY(1.35);
    }

    @Override
    public void start() {
        transition.setRate(1);
        transition.play();
    }

    @Override
    public void reverse() {
        transition.setRate(-1);
        transition.play();
    }

    @Override
    public void dispose() {

    }
}
