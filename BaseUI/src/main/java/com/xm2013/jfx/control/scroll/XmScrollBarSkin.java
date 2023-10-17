/*
 * MIT License
 *
 * Copyright (c) 2023 tuxming@sina.com / wechat: t5x5m5
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.xm2013.jfx.control.scroll;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.skin.ScrollBarSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class XmScrollBarSkin extends ScrollBarSkin {
    private StackPane trackBackground;
    private Region incrementBtn;
    private Region decrementBtn;
    private StackPane track;
    private StackPane thumb;

    private boolean pressed = false;
    private XmScrollBar control;
    private Region incrementArrow;
    private Region decrementArrow;

    public XmScrollBarSkin(XmScrollBar control) {
        super(control);
        this.control = control;

        //获取所有子节点
        findChildren(getChildren());
        updateSkin();

        //监听按下状态
        control.addEventFilter(MouseEvent.MOUSE_PRESSED, controlPressHandler);
        control.addEventFilter(MouseEvent.MOUSE_RELEASED, controlReleaseHandler);

        control.orientationProperty().addListener(orientationListener);
        control.colorTypeProperty().addListener(colorListener);
        control.hoverProperty().addListener(hoverListener);
        incrementBtn.hoverProperty().addListener(incHoverListener);
        incrementBtn.addEventFilter(MouseEvent.MOUSE_PRESSED, incPressHandler);
        incrementBtn.addEventFilter(MouseEvent.MOUSE_RELEASED, incReleaseHandler);

        decrementBtn.hoverProperty().addListener(decHoverListener);
        decrementBtn.addEventFilter(MouseEvent.MOUSE_PRESSED, decPressHandler);
        decrementBtn.addEventFilter(MouseEvent.MOUSE_RELEASED, decReleaseHandler);

    }

    /* ------------------------------- listener / event --------------------------------*/
    private EventHandler<MouseEvent> controlPressHandler =  e -> {
        pressed = true;
    };

    private EventHandler<MouseEvent> controlReleaseHandler = e -> {
        pressed = false;
    };

    private ChangeListener<Orientation> orientationListener = (ob, ov, nv) -> {
        setStatusSkin(control.getColorType().getPaint());
    };

    private ChangeListener<ColorType> colorListener = (ob, ov, nv)->{
        if(nv!=null){
            updateSkin();
        }
    };

    private ChangeListener<Boolean> hoverListener = (ob, ov, nv) -> {
        if(nv){
            thumb.setOpacity(0.9);
        }else{
            if(!pressed){
                thumb.setOpacity(0.6);
            }
        }
    };

    private ChangeListener<Boolean> incHoverListener = (ob, ov, nv) -> {
        if(nv){
            incrementBtn.setOpacity(1);
        }else{
            incrementBtn.setOpacity(0.6);
        }
    };

    private EventHandler<MouseEvent> incPressHandler = e->{
        incrementBtn.setScaleX(0.8);
        incrementBtn.setScaleY(0.8);
    };

    private EventHandler<MouseEvent> incReleaseHandler = e->{
        incrementBtn.setScaleX(1);
        incrementBtn.setScaleY(1);
    };

    private ChangeListener<Boolean> decHoverListener = (ob, ov, nv) -> {
        if(nv){
            decrementBtn.setOpacity(1);
        }else{
            decrementBtn.setOpacity(0.6);
        }
    };

    private EventHandler<MouseEvent> decPressHandler = e->{
        decrementBtn.setScaleX(0.8);
        decrementBtn.setScaleY(0.8);
    };

    private EventHandler<MouseEvent> decReleaseHandler = e -> {
        decrementBtn.setScaleX(1);
        decrementBtn.setScaleY(1);
    };

    /* ------------------------------- method --------------------------------- */
    private void updateSkin() {
        Paint paint = control.getColorType().getPaint();

        Paint trackColor = FxKit.getLightPaint(paint, 0.95)
                , btnColor = Color.TRANSPARENT
                , thumbColor = paint
        ;

        CornerRadii corner = new CornerRadii(0);
        Insets insets = new Insets(0);
        trackBackground.setBackground(new Background(new BackgroundFill(trackColor, corner, insets)));
        trackBackground.setOpacity(0.1);
        track.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new Insets(0))));

        thumb.setOpacity(0.3);

        incrementBtn.setBackground(new Background(new BackgroundFill(btnColor, corner, insets)));
        incrementBtn.setOpacity(0.6);
        incrementArrow.setStyle("-fx-background-color: "+ thumbColor.toString().replace("0x", "#")+";");

        decrementBtn.setBackground(new Background(new BackgroundFill(btnColor, corner, insets)));
        decrementBtn.setOpacity(0.6);
        decrementArrow.setStyle("-fx-background-color: "+thumbColor.toString().replace("0x", "#")+";");

        control.setBackground(new Background(new BackgroundFill(FxKit.getOpacityPaint(paint, 0.05), corner, insets)));

        setStatusSkin(thumbColor);
    }

    private void setStatusSkin(Paint thumbColor){

        SVGPath incPath = new SVGPath(), decPath = new SVGPath();
        Background background;
        if(control.getOrientation().equals(Orientation.HORIZONTAL)){
            incPath.setContent("M10,0L10,6L15,3Z");
            decPath.setContent("M0,3L5,6L5,0Z");
            background=new Background(new BackgroundFill(thumbColor, new CornerRadii(10), new Insets(2,0,2,0)));
        }else{
            incPath.setContent("M0,0L10,0L5,3Z");
            decPath.setContent("M0,3L10,3L5,0Z");
            background = new Background(new BackgroundFill(thumbColor, new CornerRadii(10), new Insets(0,2,0,2)));
        }

        incrementArrow.setShape(incPath);
        decrementArrow.setShape(decPath);
        thumb.setBackground(background);

    }

    private void findChildren(ObservableList<Node> children) {
        for (Node child : children) {
            setVar(child);
            if(child instanceof Pane){
                Pane pane = (Pane) child;
                findChildren(pane.getChildren());
            }
        }
    }

    /**
     * 获取到节点
     * @param node
     */
    private void setVar(Node node){
        ObservableList<String> styleClass = node.getStyleClass();
        if(styleClass.contains("track-background")){
            trackBackground = (StackPane) node;
        }else if(styleClass.contains("increment-button")){
            incrementBtn = (Region) node;
            incrementArrow = (Region) incrementBtn.getChildrenUnmodifiable().get(0);
        }else if(styleClass.contains("decrement-button")){
            decrementBtn = (Region) node;
            decrementArrow = (Region) decrementBtn.getChildrenUnmodifiable().get(0);
        }else if(styleClass.contains("track")){
            track = (StackPane) node;
        }else if(styleClass.contains("thumb")){
            thumb = (StackPane) node;
        }
    }

    @Override
    public void dispose() {
        control.removeEventFilter(MouseEvent.MOUSE_PRESSED, controlPressHandler);
        control.removeEventFilter(MouseEvent.MOUSE_RELEASED, controlReleaseHandler);

        control.orientationProperty().removeListener(orientationListener);
        control.colorTypeProperty().removeListener(colorListener);
        control.hoverProperty().removeListener(hoverListener);
        incrementBtn.hoverProperty().removeListener(incHoverListener);
        incrementBtn.removeEventFilter(MouseEvent.MOUSE_PRESSED, incPressHandler);
        incrementBtn.removeEventFilter(MouseEvent.MOUSE_RELEASED, incReleaseHandler);

        decrementBtn.hoverProperty().removeListener(decHoverListener);
        decrementBtn.removeEventFilter(MouseEvent.MOUSE_PRESSED, decPressHandler);
        decrementBtn.removeEventFilter(MouseEvent.MOUSE_RELEASED, decReleaseHandler);

        super.dispose();
    }
}
