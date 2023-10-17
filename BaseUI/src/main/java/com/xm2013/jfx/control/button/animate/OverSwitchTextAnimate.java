package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 鼠标移动到按钮上时，运行的动画,
 * 前置条件： XmBtnDisplayType.OVER_SWITCH_TEXT,
 * ContentDisplay.LEFT | ContentDisplay.RIGHT | ContentDisplay.TOP | ContentDisplay.BOTTOM
 */
public class OverSwitchTextAnimate implements XmButtonHoverAnimate {

    private XmButtonSkin skin;
    private XmButton control;
    private Text label = null;
    private Timeline timeline;
    private boolean isShow, displayAnimated;

    public OverSwitchTextAnimate(XmButtonSkin skin, Node icon){
        this.skin = skin;
        this.control = (XmButton) skin.getSkinnable();

        label = new Text(control.getContent());
        label.setFont(this.control.getFont());
        label.fillProperty().bind(this.control.textFillProperty());
        label.underlineProperty().bind(this.control.underlineProperty());
        label.setTextOrigin(VPos.CENTER);
        label.setLineSpacing(control.getLineSpacing());

        ContentDisplay display = this.control.getContentDisplay();

        double offset = -1;
        double textOffset = 1;
        if(ContentDisplay.RIGHT.equals(display) || ContentDisplay.BOTTOM.equals(display)){
            offset = 1;
            textOffset = -1;
        }

        double width = this.control.getPrefWidth();
        if(width<=0){
//            double finalWidth = this.control.getFinalWidth();
//            if(finalWidth == 0 ){
//                width = this.control.prefWidth(-1);
//            }else{
//                width = finalWidth;
//            }
            width = this.control.prefWidth(-1);
        }
        double height = this.control.getPrefHeight();
        if(height<=0){
//            double finalHeight = this.control.getFinalHeight();
//            if(finalHeight == 0){
//                height = this.control.prefHeight(-1);
//            }else{
//                height = finalHeight;
//            }
            height = this.control.prefHeight(-1);
        }

        double endX = (width - label.prefWidth(-1))/2;
        double endY = (height)/2;

//        System.out.println("x,y="+endX+","+endY);

        DisplacementMap labelDm = new DisplacementMap();
        label.setEffect(labelDm);

        DisplacementMap iconDm = new DisplacementMap();
        icon.setEffect(iconDm);

        label.setLayoutY(endY);
        label.setLayoutX(endX);

        KeyValue kv11 = null, kv12=null, kv13=null, kv21=null, kv22=null, kv23=null;
        if(ContentDisplay.LEFT.equals(display)){
            labelDm.setOffsetX(textOffset);
            iconDm.setOffsetX(0);

            kv11 = new KeyValue(iconDm.offsetXProperty(), 0);
            kv21 = new KeyValue(iconDm.offsetXProperty(), offset);

            kv12 = new KeyValue(labelDm.offsetXProperty(), textOffset);
            kv22 = new KeyValue(labelDm.offsetXProperty(), 0);

            kv13 = new KeyValue(label.layoutXProperty(), 0);
            kv23 = new KeyValue(label.layoutXProperty(), endX);

        }else if(ContentDisplay.RIGHT.equals(display)){

            labelDm.setOffsetX(textOffset);
            iconDm.setOffsetX(0);

            kv11 = new KeyValue(iconDm.offsetXProperty(), 0);
            kv21 = new KeyValue(iconDm.offsetXProperty(), offset);

            kv12 = new KeyValue(labelDm.offsetXProperty(), textOffset);
            kv22 = new KeyValue(labelDm.offsetXProperty(), 0);

            kv13 = new KeyValue(label.layoutXProperty(), width);
            kv23 = new KeyValue(label.layoutXProperty(), endX);

        }else if(ContentDisplay.TOP.equals(display)){
            labelDm.setOffsetY(textOffset);
            iconDm.setOffsetY(0);

            kv11 = new KeyValue(iconDm.offsetYProperty(), 0);
            kv21 = new KeyValue(iconDm.offsetYProperty(), offset);

            kv12 = new KeyValue(labelDm.offsetYProperty(), textOffset);
            kv22 = new KeyValue(labelDm.offsetYProperty(), 0);

            kv13 = new KeyValue(label.layoutYProperty(), 0);
            kv23 = new KeyValue(label.layoutYProperty(), endY);

        }else if(ContentDisplay.BOTTOM.equals(display)){
            labelDm.setOffsetY(textOffset);
            iconDm.setOffsetY(0);

            kv11 = new KeyValue(iconDm.offsetYProperty(), 0);
            kv21 = new KeyValue(iconDm.offsetYProperty(), offset);

            kv12 = new KeyValue(labelDm.offsetYProperty(), textOffset);
            kv22 = new KeyValue(labelDm.offsetYProperty(), 0);

            kv13 = new KeyValue(label.layoutYProperty(), height);
            kv23 = new KeyValue(label.layoutYProperty(), endY);
        }

        KeyFrame kf1 = new KeyFrame(Duration.seconds(0),"kf1", kv11, kv12, kv13);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.1),"kf2", kv21, kv22, kv23);

        timeline = new Timeline();
        timeline.getKeyFrames().addAll(kf1, kf2);

        timeline.setOnFinished(e -> {
            if(!this.control.isHover()){
                this.skin.getChildren().remove(label);
            }

            displayAnimated = false;
            if(isShow && !this.control.isHover()){
                moveOut();
            }
        });

    }

    @Override
    public void moveIn() {
        if(!displayAnimated){
            this.skin.getChildren().add(label);
        }

        this.displayAnimated = true;
        isShow = true;
        timeline.setRate(1);
        timeline.play();
    }

    @Override
    public void moveOut() {
        if(displayAnimated){
            return;
        }

        this.displayAnimated = true;
        isShow = false;

        timeline.setRate(-1);
        timeline.play();
    }

    @Override
    public void dispose(boolean resetContentDisplay) {

    }
}
