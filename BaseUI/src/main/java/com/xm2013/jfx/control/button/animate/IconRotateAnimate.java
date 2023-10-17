package com.xm2013.jfx.control.button.animate;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * 图标旋转动画
 */
public class IconRotateAnimate implements  XmButtonIconAnimate{

    private Node icon;
    private RotateTransition transition;

    public IconRotateAnimate(Node icon){

        transition = new RotateTransition();
        transition.setNode(icon);
        transition.setDuration(Duration.millis(100));
        transition.setFromAngle(0);
        transition.setToAngle(180);
        transition.setInterpolator(Interpolator.EASE_IN); // 设置动画插值器（线性）
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
