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

public class OverShowTextNAnimate implements XmButtonHoverAnimate{

    private XmButtonSkin skin;
    private XmButton control;
    private Text text;
    private Text showText;
    private Node icon;

    private double prevX, prevY;
    private boolean isShow, rate, displayAnimated;
    private final SequentialTransition transition;
    private Effect prevEffect;

    public OverShowTextNAnimate(XmButtonSkin skin, Text text, Node icon){
        this.skin = skin;
        this.text = text;
        this.icon = icon;
        this.control = (XmButton) this.skin.getSkinnable();

        this.prevX = this.control.getLayoutX();
        this.prevY = this.control.getLayoutY();
        this.prevEffect = text.getEffect();

        showText = new Text(control.getContent());
        showText.fontProperty().bind(control.fontProperty());
        showText.fillProperty().bind(control.textFillProperty());
        showText.underlineProperty().bind(control.underlineProperty());

        double width = this.control.prefWidth(-1);
        double textWidth = this.showText.prefWidth(-1)+this.control.getGraphicTextGap();
        double height = this.control.prefHeight(-1);
        double textHeight = showText.prefHeight(-1)+this.control.getGraphicTextGap()+5;

        double offset = 1;
        if(ContentDisplay.RIGHT.equals(this.control.getContentDisplay()) || ContentDisplay.BOTTOM.equals(this.control.getContentDisplay())){
            offset = -1;
        }

        DisplacementMap textDm = new DisplacementMap();
//        iconDm.setOffsetX(-offset);
        text.setEffect(textDm);

        boolean isH = ContentDisplay.LEFT.equals(this.control.getContentDisplay()) || ContentDisplay.RIGHT.equals(this.control.getContentDisplay());

        Timeline timeline1 = null;
        if(isH){
            timeline1 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf1", new KeyValue(this.control.prefWidthProperty(), width)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX)
                    ),
                    new KeyFrame(Duration.millis(100), "kf2", new KeyValue(this.control.prefWidthProperty(), width+textWidth)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX - iconWidth)
                    )
            );
        }else{
            timeline1 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf1", new KeyValue(this.control.prefHeightProperty(), height)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX)
                    ),
                    new KeyFrame(Duration.millis(100), "kf2", new KeyValue(this.control.prefHeightProperty(), height+textHeight)
//                            ,new KeyValue(this.control.layoutXProperty(), prevX - iconWidth)
                    )
            );
        }

        timeline1.setOnFinished(e->{
            if(rate){
                this.control.setText(this.control.getContent());
            }else{
                if(!this.control.isHover()){
                    this.control.setText(null);
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
                    new KeyFrame(Duration.millis(0), "kf3", new KeyValue(textDm.offsetXProperty(), offset)),
                    new KeyFrame(Duration.millis(100), "kf4", new KeyValue(textDm.offsetXProperty(), 0))
            );
        }else{
            timeline2 = new Timeline(
                    new KeyFrame(Duration.millis(0), "kf3", new KeyValue(textDm.offsetYProperty(), offset)),
                    new KeyFrame(Duration.millis(100), "kf4", new KeyValue(textDm.offsetYProperty(), 0))
            );
        }
        timeline2.setOnFinished(e -> {

            if(rate){
                if(!this.control.isHover()){
                    this.control.setText(null);
                }

                displayAnimated = false;
                if(isShow && !this.control.isHover()){
                    moveOut();
                }
            }else{
                this.control.setText(this.control.getContent());
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
        this.text.setEffect(prevEffect);
        this.control.setLayoutX(prevX);
        this.control.setLayoutY(prevY);

        showText.fontProperty().unbind();
        showText.fillProperty().unbind();
        showText.underlineProperty().unbind();
    }
}
