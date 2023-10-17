package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.Effect;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 鼠标移动到按钮上时，运行的动画, 图标与文本水平排列时
 * 前置条件： XmBtnDisplayType.OVER_SWITCH_ICON,
 * ContentDisplay.LEFT | ContentDisplay.RIGHT | ContentDisplay.TOP | ContentDisplay.BOTTOM
 */
public class OverSwitchIconAnimate implements XmButtonHoverAnimate {
    private final DisplacementMap textDm;
    private XmButtonSkin skin;
    private XmButton control;
    private Node icon;
    private Text text;
    private boolean isShow, displayAnimated;
    private Timeline timeline;


    private ContentDisplay prevDisplay;
    private Effect prevTextEffect, prevIconEffect;

    public OverSwitchIconAnimate(XmButtonSkin skin, Node icon, Text text){
        this.skin = skin;
        this.control = (XmButton) skin.getSkinnable();
        this.icon = icon;
        this.text = text;

        prevDisplay = this.control.getContentDisplay();
        prevIconEffect = this.icon.getEffect();
        prevTextEffect = this.text.getEffect();

        double iconOffsetX=0, iconOffsetY=0, endIconOffsetX=0, endIconOffsetY=0;
        double textOffsetX=0, textOffsetY=0, endTextOffsetX=0, endTextOffsetY=0;
        if(ContentDisplay.LEFT.equals(prevDisplay)){
            iconOffsetX = 1;
            endTextOffsetX = -1;
        }else if(ContentDisplay.RIGHT.equals(prevDisplay)){
            iconOffsetX = -1;
            endTextOffsetX = 1;
        }else if(ContentDisplay.TOP.equals(prevDisplay)){
            iconOffsetY = 1;
            endTextOffsetY = -1;
        }else if(ContentDisplay.BOTTOM.equals(prevDisplay)){
            iconOffsetY = -1;
            endTextOffsetY = 1;
        }

        this.control.setContentDisplay(ContentDisplay.CENTER);

        DisplacementMap iconDm = new DisplacementMap();
        this.icon.setEffect(iconDm);

        textDm = new DisplacementMap();
        this.text.setEffect(textDm);

        this.control.setGraphic(icon);

        KeyValue kv11 = null, kv12=null, kv13=null, kv14=null, kv21=null, kv22=null, kv23=null, kv24=null;

        kv11 = new KeyValue(iconDm.offsetXProperty(), iconOffsetX);
        kv12 = new KeyValue(textDm.offsetXProperty(), textOffsetX);
        kv13 = new KeyValue(iconDm.offsetYProperty(), iconOffsetY);
        kv14 = new KeyValue(textDm.offsetYProperty(), textOffsetY);

        kv21 = new KeyValue(iconDm.offsetXProperty(), endIconOffsetX);
        kv22 = new KeyValue(textDm.offsetXProperty(), endTextOffsetX);
        kv23 = new KeyValue(iconDm.offsetYProperty(), endIconOffsetY);
        kv24 = new KeyValue(textDm.offsetYProperty(), endTextOffsetY);

        KeyFrame kf1 = new KeyFrame(Duration.seconds(0),"kf1", kv11, kv12, kv13, kv14);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.1),"kf2", kv21, kv22, kv23, kv24);

        timeline = new Timeline();
        timeline.getKeyFrames().addAll(kf1, kf2);

        timeline.setOnFinished(e -> {

            displayAnimated = false;
            if(isShow && !this.control.isHover()){
                moveOut();
            }
        });
    }

    @Override
    public void moveIn() {
        if(displayAnimated){
            return;
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
        if(resetContentDisplay){
            this.control.setContentDisplay(prevDisplay);
        }
        this.text.setEffect(prevTextEffect);
        this.icon.setEffect(prevIconEffect);
    }
}
