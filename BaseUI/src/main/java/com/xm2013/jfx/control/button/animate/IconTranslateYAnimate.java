package com.xm2013.jfx.control.button.animate;

import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * 图标翻转动画
 */
public class IconTranslateYAnimate implements  XmButtonIconAnimate{

    private Node icon;
    private RotateTransition transition;

    public IconTranslateYAnimate(Node icon){

        transition = new RotateTransition();
        transition.setNode(icon);
        transition.setDuration(Duration.millis(100));
        transition.setAxis(new Point3D(0,1,0));
        transition.setFromAngle(0);
        transition.setToAngle(180);

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
