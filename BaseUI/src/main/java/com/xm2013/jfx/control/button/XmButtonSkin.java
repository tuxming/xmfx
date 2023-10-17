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
package com.xm2013.jfx.control.button;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ClickShadowAnimate;
import com.xm2013.jfx.control.animate.ResetNodePositionCallback;
import com.xm2013.jfx.control.base.BorderType;
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.SkinInfo;
import com.xm2013.jfx.control.button.animate.XmButtonHoverAnimate;
import com.xm2013.jfx.control.button.animate.XmButtonIconAnimate;
import com.xm2013.jfx.control.button.animate.XmButtonLoadingAnimate;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;


public class XmButtonSkin extends ButtonSkin {

    private XmButton control;
//    private XmButtonType type;
    private Node icon;
    private Text text;

    private Text textCopy;

    private XmButtonHoverAnimate displayTypeAnimate;
    private XmButtonLoadingAnimate loadingAnimate;

    private XmButtonIconAnimate iconAnimate;

    private ClickAnimate clickAnimate;

    private Map<Integer, SkinInfo> skins = new HashMap<Integer, SkinInfo>();

    public XmButtonSkin(XmButton control) {
        super(control);

        this.control = control;

        icon = control.getGraphic();
        //获取text节点
        ObservableList<Node> children = this.getChildren();
        for (Node child : children) {
            if(child instanceof Text && child != icon){
                text = (Text) child;
                break;
            }
        }

        if(control.isDisable()){
            control.setOpacity(0.65);
        }

        updateSkin();

        control.hoverProperty().addListener(hoverChangeListener);
        control.focusedProperty().addListener(focusListenter);
        control.loadingProperty().addListener(loadingListener);

        control.contentDisplayProperty().addListener(typeListener);
        control.colorTypeProperty().addListener(typeListener);
        control.hueTypeProperty().addListener(typeListener);
        control.roundTypeProperty().addListener(typeListener);
        control.sizeTypeProperty().addListener(typeListener);
        control.displayTypeProperty().addListener(typeListener);
        control.loadingTypeProperty().addListener(typeListener);
        control.iconAnimateTypeProperty().addListener(typeListener);
        control.clickAnimateTypeProperty().addListener(typeListener);
        control.borderTypeProperty().addListener(typeListener);

        control.disabledProperty().addListener(disableListener);

        control.addEventFilter(MouseEvent.MOUSE_CLICKED, clickEventHandler);
        control.addEventFilter(MouseEvent.MOUSE_PRESSED, pressEventHandler);
        control.addEventFilter(MouseEvent.MOUSE_RELEASED, releaseEventHandler);
        control.addEventFilter(KeyEvent.KEY_RELEASED, keyEventHandler);

        textCopy = new Text();
        if(text != null){
            textCopy.fontProperty().bind(text.fontProperty());
            textCopy.fillProperty().bind(text.fillProperty());
            textCopy.lineSpacingProperty().bind(text.lineSpacingProperty());
        }
        textCopy.textProperty().bind(control.contentProperty());


//      1
    }

    /* -------------------override-------------------*/

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        double width = control.getFinalWidth();
        String log = "computeWidth: ";
//        if(width>0){
//            log+="-a";
//            return width;
//        }

        double width =  leftInset+rightInset;

        XmButtonType.BtnDisplayType displayType = control.getDisplayType();
        ContentDisplay display = control.getContentDisplay();

        if(XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(displayType)
                || XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(displayType)){
            width +=  textCopy.prefWidth(-1);
            log += "-1";
        }else if(XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(displayType)){
            if(ContentDisplay.TOP.equals(display) || ContentDisplay.BOTTOM.equals(display)){
                width += textCopy.prefWidth(-1);
                log += "-2";
            }else{
                width += icon.prefWidth(-1);
                log += "-3";
            }
        }else if( XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(displayType)){
            width += textCopy.prefWidth(-1);
            log += "-4";
        }else {
            width = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
            log += "-5";
        }
        return width;


//        double width = 0;
//        double textWidth = text.prefWidth(-1);
//        double iconWidth = icon!=null ? icon.prefWidth(-1) : 0d;
//        ContentDisplay display = control.getContentDisplay();
//        XmButtonType.BtnDisplayType btnDisplay = control.getDisplayType();
//
//        if(ContentDisplay.LEFT.equals(display) || ContentDisplay.RIGHT.equals(display)){
//            if(XmButtonType.BtnDisplayType.FULL.equals(btnDisplay)){
//                width = iconWidth + textWidth;
//            }else if(XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(btnDisplay)){
//                width = iconWidth;
//            }else{
//                width = textWidth;
//            }
////            else if(XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(btnDisplay)){
////                width = textWidth;
////            }
////            else if(XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(btnDisplay)){
////                width = textWidth;
////            }else if(XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(btnDisplay)){
////                width = textWidth;
////            }
//        }else if(ContentDisplay.TOP.equals(display) || ContentDisplay.BOTTOM.equals(display)) {
////            if(XmButtonType.BtnDisplayType.FULL.equals(btnDisplay)){
////                width = textWidth;
////            }else if(XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(btnDisplay)) {
////                width = iconWidth;
////            }
////            else if(XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(btnDisplay)){
////                width = textWidth;
////            }
////            else if(XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(btnDisplay)){
////                width = textWidth;
////            }else if(XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(btnDisplay)){
////                width = textWidth;
////            }
//            width = textWidth;
//        }else if(ContentDisplay.GRAPHIC_ONLY.equals(display)){
//            width = iconWidth;
//        }else if(ContentDisplay.TEXT_ONLY.equals(display)){
//            width = textWidth;
//        }
//
//        Insets padding = control.getPadding();
//        if(padding != null){
//            width += padding.getLeft() + padding.getRight();
//        }
//
//        return width;

    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {

        XmButtonType.BtnDisplayType displayType = control.getDisplayType();
        ContentDisplay display = control.getContentDisplay();

        double height;
        if(ContentDisplay.BOTTOM.equals(display)
                || ContentDisplay.TOP.equals(display)){
            if(XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(displayType)
            ){
                double textHeight = textCopy.prefHeight(-1) + topInset+bottomInset;
                double iconHeight = (this.getChildren().contains(icon) && icon != null)? icon.prefHeight(-1) : 0;
                height = Math.max(textHeight, iconHeight);
            }else{
                height = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
            }
        }else {
            height = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        }
        return height;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        XmButtonType.BtnDisplayType displayType = control.getDisplayType();
        ContentDisplay display = control.getContentDisplay();

        double height;

        if(ContentDisplay.BOTTOM.equals(display)
                || ContentDisplay.TOP.equals(display)){
            if(XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(displayType)
            ){
                double textHeight = textCopy.prefHeight(-1) + topInset+bottomInset;
                double iconHeight = (this.getChildren().contains(icon) && icon != null)? icon.prefHeight(-1) : 0;
                height = Math.max(textHeight, iconHeight);
            }else{
                height = super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
            }
        }else {
            height = super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
        }

        return height;
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        positionIcon(y);
    }

    @Override
    public void dispose() {
        super.dispose();

        control.hoverProperty().removeListener(hoverChangeListener);
        control.focusedProperty().removeListener(focusListenter);
        control.loadingProperty().removeListener(loadingListener);

        control.contentDisplayProperty().removeListener(typeListener);
        control.colorTypeProperty().removeListener(typeListener);
        control.hueTypeProperty().removeListener(typeListener);
        control.roundTypeProperty().removeListener(typeListener);
        control.sizeTypeProperty().removeListener(typeListener);
        control.displayTypeProperty().removeListener(typeListener);
        control.loadingTypeProperty().removeListener(typeListener);
        control.iconAnimateTypeProperty().removeListener(typeListener);
        control.clickAnimateTypeProperty().removeListener(typeListener);
        control.borderProperty().removeListener(typeListener);
        control.disabledProperty().removeListener(disableListener);
        control.removeEventFilter(MouseEvent.MOUSE_CLICKED, clickEventHandler);
        control.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressEventHandler);
        control.removeEventFilter(MouseEvent.MOUSE_RELEASED, releaseEventHandler);
        control.removeEventFilter(KeyEvent.KEY_RELEASED, keyEventHandler);
        textCopy.fontProperty().unbind();
        textCopy.fillProperty().unbind();
        textCopy.lineSpacingProperty().unbind();
        textCopy.textProperty().unbind();
    }

    /*-------------------listener-------------------*/
    private EventHandler<KeyEvent> keyEventHandler = e -> {
        if(control.isFocused() && (e.getCode().equals(KeyCode.ENTER)|| e.getCode().equals(KeyCode.SPACE))){
            getClickAnimate()
                    .setPoint(control.prefWidth(-1)/2, control.prefHeight(-1)/2)
                    .play();
        }
    };

    /**
     * 设置disable时候的状态
     */
    private ChangeListener<Boolean> disableListener = (ob, ov, nv) -> {
        if(nv){
            control.setOpacity(0.65);
        }else{
            control.setOpacity(1);
        }
    };

    /**
     * focus监听
     */
    private ChangeListener<Boolean> focusListenter = (ob, ov, nv) -> {
        /*
         * status: 1 ： 默认状态下的颜色
         * status: 2:  hover-out focus
         * status: 3 : hover-focus状态下的颜色
         * status: 4 : out-hover focus状态的颜色
         */
        updateColor(nv?(control.isHover()?3:4):(control.isHover()?2:1));
//        if(nv){
//            updateColor(control.isHover()?3:4);
//        }else{
//            updateColor(control.isHover()?2:1);
//        }
    };

    /**
     * hover属性变化监听
     */
    private ChangeListener<Boolean> hoverChangeListener = (ob, ov, nv) -> {

        /*
         * status: 1 ： 默认状态下的颜色
         * status: 2:  hover-out focus
         * status: 3 : hover-focus状态下的颜色
         * status: 4 : out-hover focus状态的颜色
         */

        if(nv){
            if(control.isFocused()){
                updateColor(3);
            }else{
                updateColor(2);
            }
        }else{
            if(control.isFocused()){
                updateColor(4);
            }else{
                updateColor(1);
            }
        }

        XmButtonType.BtnDisplayType displayType = control.getDisplayType();
        //在当前存在图标的情况下，显示图标动画
        if(icon!=null){
            boolean isIcon = XmButtonType.BtnDisplayType.FULL.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(displayType);

            if(isIcon){
                if(this.iconAnimate == null){
                    this.iconAnimate = IconAnimateFactory.get(control, icon);
                }
                if(this.iconAnimate != null){
                    if(nv){
                        this.iconAnimate.start();
                    }else{
                        this.iconAnimate.reverse();
                    }
                }
            }

            if(!XmButtonType.BtnDisplayType.FULL.equals(displayType)){
                if(!control.isLoading() && this.control.getContent()!=null){
                    if(this.displayTypeAnimate == null){
                        this.displayTypeAnimate  = OverAnimateFactory.getAnimate(this,icon, text);
                    }

                    if(this.displayTypeAnimate != null){
                        if(nv){
                            displayTypeAnimate.moveIn();
                        }else{
                            displayTypeAnimate.moveOut();
                        }
                    }
                }
            }

        }
    };

    /**
     * 监听是否加载中
     */
    private ChangeListener<Boolean> loadingListener = (ob, ov, nv) -> {
        if(nv){
            showLoading();
        }
    };

    private ChangeListener<Object> typeListener = (ob, ov, nv) -> {
        clickAnimate = null;
        iconAnimate = null;
        this.skins.clear();
        updateSkin();
    };

    /**
     * 监听鼠标点击事件
     */
    public EventHandler<MouseEvent> clickEventHandler = e -> {
        getClickAnimate().setPoint(e.getX(), e.getY()).play();
    };

    public EventHandler<MouseEvent> pressEventHandler = e -> {
        if(icon!=null){
            icon.setScaleY(0.95);
            icon.setScaleX(0.95);
        }
        if(text!=null){
            text.setScaleX(0.95);
            text.setScaleY(0.95);
        }
    };

    public EventHandler<MouseEvent> releaseEventHandler = e->{
        if(icon != null){
            icon.setScaleX(1);
            icon.setScaleY(1);
        }
        if(text != null){
            text.setScaleX(1);
            text.setScaleY(1);
        }
    };

    /*----------------------methods-------------------------*/
    private ClickAnimate getClickAnimate(){
        if(clickAnimate == null){
            ClickAnimateType type = control.getClickAnimateType();
            if(ClickAnimateType.SHADOW.equals(type)){
                clickAnimate = new ClickShadowAnimate(control, control.getColorType());
            }else if(ClickAnimateType.RIPPER.equals(type)){
                clickAnimate = new ClickRipperAnimate(control, control.getColorType(), control.getHueType());
            }

            clickAnimate.setNodePosition(new ResetNodePositionCallback() {
                @Override
                public void call(Node node, double width, double height, double x, double y) {
                    positionInArea1(node, x, y, width, height);
                }
            });

            clickAnimate.setAddNode(new CallBack<Node>() {
                @Override
                public void call(Node node) {
                    getChildren().add(0, node);
                }
            });

            clickAnimate.setRemoveNode(new CallBack<Node>() {
                @Override
                public void call(Node node) {
                    getChildren().remove(node);
                }
            });

        }
        return clickAnimate;
    }

    private SkinInfo getSkinInfo(int status){
        SkinInfo info = skins.get(status);
        if(info == null){
            info = new SkinInfo(control.getColorType(), control.getSizeType(), control.getRoundType(), control.getHueType(), control.getBorderType());
            info.compute(status);
            skins.put(status, info);
        }
        return info;
    }

    /**
     * 当属性发生变化的时候，更新button的外观
     */
    private void updateSkin(){
//        System.out.println("update skin");

        XmButtonType.BtnDisplayType displayType = control.getDisplayType();

        //重置宽高，确保触发布局更新
        double width = this.control.getFinalWidth();
        if(width<=0){
            width = -1;
        }

        double height = this.control.getFinalHeight();
        if(height<=0){
            height = -1;
        }

        this.control.setPrefHeight(width);
        this.control.setPrefWidth(height);

        //更新布局
        //布局只有在text / graphic都存在的情况下，才有变化，默认是没有布局一说的，就是默认居中
        if(icon != null && text != null){

            boolean isIcon = XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(displayType);
            boolean isText = XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(displayType)
                    || XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(displayType);

            if(isIcon){
                this.control.setGraphic(null);
                this.control.setText(this.control.getContent());
            }else if(isText){
                this.control.setText(null);
                this.control.setGraphic(icon);
                positionIcon(0);
            } else{
//            else if(!init){
                this.control.setText(this.control.getContent());
                this.control.setGraphic(this.icon);
            }
        }

        //更新尺寸
        SkinInfo info = getSkinInfo(1);
        Font font = control.getFont();
        Insets padding = info.getPadding();
        double fontSize = info.getFontSize();

        //isAutoTextSize 默认是开启的，意思就跟随设置，如果是关闭，则文字的大小需要自己设置
        if(control.isAutoTextSize()){
            control.setFont(Font.font(font.getFamily(), fontSize));
        }

        //isAutoGraphicSize默认是开启的，默认是开启的，意思就跟随设置，如果是关闭，则图标的大小需要自己设置
        //这里图标是使用的XmIcon，如果是想使用自定义图标，见实现XmIcon接口，不然图标的大小不会根据设置而发生变化
        if(control.isAutoGraphicSize()){
            Node icon = control.getGraphic();
            if(icon instanceof XmIcon){
                ((XmIcon) icon).setSize(fontSize+2);
            }
        }

        control.setPadding(padding);

        //更新颜色和圆角
        updateColor(1);

        this.resetDisplayTypeAnimate(false);
        this.resetLoadingAnimate();
        this.resetClickAnimate();
        this.resetIconAnimate();

    }

    /**
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover-out focus
     * status: 3 : hover-focus状态下的颜色
     * status: 4 : out-hover focus状态的颜色
     */
    private void updateColor(int status){

        SkinInfo info = getSkinInfo(status);

        //构建边框
        Border border = null;
        if(BorderType.FULL.equals(control.getBorderType())){
            //外边框
            BorderStroke stroke1 = new BorderStroke(info.getBorderOutColor(),BorderStrokeStyle.SOLID,
                    new CornerRadii(info.getRadiusWidth()),
                    new BorderWidths(info.getOuterBorderWidth()),
                    info.getOuterBorderInsets());
            //内边框
            BorderStroke stroke2 = new BorderStroke(info.getInnerBorderColor(), BorderStrokeStyle.SOLID,
                    new CornerRadii(info.getRadiusWidth()),
                    new BorderWidths(info.getInnerBorderWidth()),
                    info.getInnerBorderInsets());
            border = new Border(stroke1, stroke2);
        }

        //构建背景色背景色
        BackgroundFill fill = new BackgroundFill(info.getBackgroundColor(), new CornerRadii(info.getRadiusWidth()), new Insets(0,0,0,0));
        Background background = new Background(fill);

        //设置
        control.setBackground(background);
        control.setBorder(border);
        control.setTextFill(info.getFontColor());
        Node icon = control.getGraphic();
        if(icon instanceof XmIcon){
            ((XmIcon) icon).setColor(info.getFontColor());
        }
    }

    private void positionIcon(double y){
        XmButtonType.BtnDisplayType displayType = control.getDisplayType();

        boolean isBottom = ContentDisplay.BOTTOM.equals(control.getContentDisplay());

        //当时在over-show-text是，在上下布局中， icon不居中显示
        boolean needPosition = XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(displayType)
                && (isBottom || ContentDisplay.TOP.equals(control.getContentDisplay()))
                && control.getText()==null;

        if(XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(displayType) || needPosition
        ){
            double iw = this.icon.prefWidth(-1),
                    ih = this.icon.prefHeight(-1),
                    width = this.control.prefWidth(-1),
                    height = this.control.prefHeight(-1);

            if(y == 0){
                y = (height - ih)/2;
            }

            layoutInArea(this.icon, (width - iw)/2, y, iw, ih, 0, HPos.CENTER, VPos.CENTER);
        }
    }

    private void resetDisplayTypeAnimate(boolean resetContentDisplay){
        if(this.displayTypeAnimate!=null){
            this.displayTypeAnimate.dispose(resetContentDisplay);
            this.displayTypeAnimate = null;
        }
    }

    public void resetLoadingAnimate(){
        if(this.loadingAnimate != null){
            this.loadingAnimate.dispose();
            this.loadingAnimate = null;
        }
    }
    public void resetClickAnimate(){
        if(this.clickAnimate != null){
            this.clickAnimate.dispose();
            this.clickAnimate = null;
        }
    }

    public void resetIconAnimate(){
        if(this.iconAnimate != null){
            this.iconAnimate.dispose();
            this.iconAnimate = null;
        }
    }

    public void positionInArea1(Node node, double x, double y, double width, double height){
//        super.positionInArea(node, 0, x, y, width, height, Insets.EMPTY, HPos.CENTER, VPos.CENTER);
        super.layoutInArea(node, x, y, width, height, 0, HPos.CENTER, VPos.CENTER);
    }

    /**
     * 显示加载动画
     */
    private void showLoading() {
        if(loadingAnimate == null){
            loadingAnimate = LoadingAnimateFactory.getLoadingAnimate(this, control,text, icon);
        }
        loadingAnimate.showLoading();
    }
}
