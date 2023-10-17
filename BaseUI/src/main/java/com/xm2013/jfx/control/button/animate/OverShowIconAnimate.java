package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.Effect;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class OverShowIconAnimate implements XmButtonHoverAnimate{

    private XmButtonSkin skin;
    private XmButton control;
    private Text text;
    private Node icon;

    private double prevX, prevY;
    private boolean isShow, rate, displayAnimated;
    private final SequentialTransition transition;
    private Effect prevEffect;

    public OverShowIconAnimate(XmButtonSkin skin, Text text, Node icon){
        this.skin = skin;
        this.text = text;
        this.icon = icon;
        this.control = (XmButton) this.skin.getSkinnable();

        this.prevX = this.control.getLayoutX();
        this.prevY = this.control.getLayoutY();
        this.prevEffect = icon.getEffect();

        double width = this.control.prefWidth(-1);
        double iconWidth = this.icon.prefWidth(-1)+this.control.getGraphicTextGap();
        double height = this.control.prefHeight(-1);
        double iconHeight = icon.prefHeight(-1)+this.control.getGraphicTextGap();

        double offset = -1;
        if(ContentDisplay.RIGHT.equals(this.control.getContentDisplay()) || ContentDisplay.BOTTOM.equals(this.control.getContentDisplay())){
            offset = 1;
        }

        DisplacementMap iconDm = new DisplacementMap();
//        iconDm.setOffsetX(-offset);
        icon.setEffect(iconDm);

        boolean isH = ContentDisplay.LEFT.equals(this.control.getContentDisplay()) || ContentDisplay.RIGHT.equals(this.control.getContentDisplay());

        Timeline timeline1 = null;
        if(isH){
            timeline1 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf1", new KeyValue(this.control.prefWidthProperty(), width)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX)
                    ),
                    new KeyFrame(Duration.millis(100), "kf2", new KeyValue(this.control.prefWidthProperty(), width+iconWidth)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX - iconWidth)
                    )
            );
        }else{
            timeline1 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf1", new KeyValue(this.control.prefHeightProperty(), height)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX)
                    ),
                    new KeyFrame(Duration.millis(100), "kf2", new KeyValue(this.control.prefHeightProperty(), height+iconHeight)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX - iconWidth)
                    )
            );
        }

        timeline1.setOnFinished(e->{
            if(rate){
                this.control.setGraphic(icon);
            }else{
                if(!this.control.isHover()){
                    this.control.setGraphic(null);
                }

                displayAnimated = false;
                if(isShow && !this.control.isHover()){
                    moveOut();
                }
            }
        });

        Timeline timeline2 =null;
        if(isH){
            timeline2 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf3", new KeyValue(iconDm.offsetXProperty(), offset)),
                    new KeyFrame(Duration.millis(100), "kf4", new KeyValue(iconDm.offsetXProperty(), 0))
            );
        }else{
            timeline2 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf3", new KeyValue(iconDm.offsetYProperty(), offset)),
                    new KeyFrame(Duration.millis(100), "kf4", new KeyValue(iconDm.offsetYProperty(), 0))
            );
        }
        timeline2.setOnFinished(e -> {

            if(rate){
                if(!this.control.isHover()){
                    this.control.setGraphic(null);
                }

                displayAnimated = false;
                if(isShow && !this.control.isHover()){
                    moveOut();
                }
            }else{
                this.control.setGraphic(icon);
            }


        });

        transition = new SequentialTransition();
//        transition.getChildren().addAll(timeline1);
        transition.getChildren().addAll(timeline1, timeline2);


    }

    @Override
    public void moveIn() {
        if(displayAnimated){
            return;
        }
        this.isShow = true;
        rate = true;
        transition.setRate(1);
        transition.play();
    }

    @Override
    public void moveOut() {
        if(displayAnimated){
            return;
        }
        this.displayAnimated = true;
        this.isShow = false;
        rate = false;

        transition.setRate(-1);
        transition.play();
    }

    @Override
    public void dispose(boolean resetContentDisplay) {
        this.icon.setEffect(prevEffect);
        this.control.setLayoutX(prevX);
        this.control.setLayoutY(prevY);
    }
}
