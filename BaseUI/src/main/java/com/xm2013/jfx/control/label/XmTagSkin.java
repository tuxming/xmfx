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
package com.xm2013.jfx.control.label;


import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class XmTagSkin extends XmLabelSkin {

    private Pane textPane = null;
    private Text closeIcon;
    private Pane closePane;
    private XmTag control;
    private TextField editField;

    public XmTagSkin(XmTag control) {
        super(control);
        this.control = control;
        textPane = (Pane) this.getChildren().get(0);

        this.control.setFocusTraversable(true);

        initComponents();
        updateSkin();

        //监听属性
        control.closeableProperty().addListener(propertyChangeListener);
        control.editableProperty().addListener(propertyChangeListener);
        control.hueTypeProperty().addListener(propertyChangeListener);
        control.sizeTypeProperty().addListener(propertyChangeListener);
        control.colorTypeProperty().addListener(propertyChangeListener);
        control.roundTypeProperty().addListener(propertyChangeListener);
        control.borderTypeProperty().addListener(propertyChangeListener);

    }

    private void initComponents(){

        if(closeIcon == null){
            //初始化组件
            closeIcon = new Text("+");
            closeIcon.setFont(Font.font(16));
            closeIcon.setRotate(45);
            closeIcon.setTextOrigin(VPos.TOP);
            closeIcon.setLayoutX(2.5);
            closeIcon.setLayoutY(-2.5);
            closePane = new Pane(closeIcon);
            closePane.setMaxWidth(16);
            closePane.setMaxHeight(16);
            closePane.setStyle("-fx-padding: 2px; -fx-background-color: transparent; -fx-background-radius: 20px; -fx-border-radius: 20px; ");
            closePane.addEventFilter(MouseEvent.MOUSE_CLICKED, clickCloseHandler);
            closePane.hoverProperty().addListener(iconHoverListener);

        }

        if(control.isCloseable()){
            if(!this.getChildren().contains(closePane)){
                this.getChildren().add(closePane);
            }
        }else{
            this.getChildren().remove(closePane);
        }

        if(control.isEditable()){
            if(editField == null){
                editField = new TextField();
                editField.getStyleClass().add("xm-tag-field");
                editField.setFont(Font.font(12));
                editField.setPrefWidth(100);
                editField.setVisible(false);

                //当输入enter键的时候，确认输入完成
                editField.addEventFilter(KeyEvent.KEY_RELEASED, editFieldEnterHandler);
                //当失去焦点的时候，确认输入完成
                editField.focusedProperty().addListener(editFieldFocusListener);
                //鼠标双击的时候开启编辑模式
                this.control.addEventFilter(MouseEvent.MOUSE_CLICKED, editClickHandler);
            }

            if(!this.getChildren().contains(editField)){
                this.getChildren().add(editField);
            }
        }else{
            if(editField!=null){
                this.getChildren().remove(editField);
            }
        }


//        if(control.isEditable()){
//            this.getChildren().add(editField);
//        }else{
//            this.getChildren().remove(editField);
//        }

    }

    /**
     * 设置外观形状
     */
    private void updateSkin() {

        Paint backgroundColor = null, borderColor = null, fontColor = null, closeIconColor = null;
        double radius = 0, borderWidth = 0.75, fontSize = 15, padding = 4;

        SizeType sizeType = control.getSizeType();
        RoundType roundType = control.getRoundType();
        BorderType borderType = control.getBorderType();
        HueType hueType = control.getHueType();
        ColorType colorType = control.getColorType();
        Paint paint = colorType.getPaint();

        //设置字体大小
        if(SizeType.SMALL.equals(sizeType)){
            fontSize = 11;
            padding = 0;
        }else if(SizeType.MEDIUM.equals(sizeType)){
            fontSize = 13;
            padding = 2;
        }

        //圆角
        if(RoundType.SMALL.equals(roundType)){
            radius = 4;
        }else if(RoundType.SEMICIRCLE.equals(roundType) || RoundType.CIRCLE.equals(roundType)){
            radius = 30;
        }

        //边框颜色
        if(BorderType.FULL.equals(borderType)){
            if(HueType.DARK.equals(hueType)){
                borderColor = FxKit.getLightPaint(paint, 0.7);
            }else if(HueType.LIGHT.equals(hueType)){
                borderColor = paint;
            }

        }

        //颜色
        if(HueType.DARK.equals(hueType)){
            backgroundColor = FxKit.getLightPaint(paint, 0.9);
            closeIconColor = fontColor = paint;
        }else if(HueType.LIGHT.equals(hueType)){
            backgroundColor = paint;
            closeIconColor = fontColor = Color.WHITE;
        }

//        Border border = null;
        String styles = "";
        if(BorderType.FULL.equals(borderType)){
//            border = new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, new CornerRadii(radius), new BorderWidths(borderWidth)));
            styles = "-fx-border-color: "+borderColor.toString().replace("0x", "#")
                        + "; -fx-border-width: 1;"
                        + " -fx-border-radius: "+radius+";";
        }

//        Background background = null;
        if(!HueType.NONE.equals(hueType)){
//            background = new Background(new BackgroundFill(backgroundColor, new CornerRadii(radius),  new Insets(0)));
            styles += "-fx-background-color: "+backgroundColor.toString().replace("0x", "#")
                    +"; -fx-background-radius: "+radius;
        }
//        System.out.println(styles);

//        control.setBorder(border);
//        control.setBackground(background);
        control.setPadding(new Insets(1d, padding, 1d, padding));

        control.setStyle(styles);

        control.setTextFill(fontColor);
        control.setFont(Font.font(fontSize));

        if(control.isCloseable()){
            closeIcon.setFill(closeIconColor);
        }
    }

    private ChangeListener<Object> propertyChangeListener = (ob, ov, nv) -> {
//        System.out.println(nv);
        initComponents();
        updateSkin();
    };

    /**
     * 当输入enter键的时候，退出编辑模式
     */
    private EventHandler<KeyEvent> editFieldEnterHandler = e -> {
        if(e.getCode().equals(KeyCode.ENTER)){
        	String text = editField.getText().replace("\n", "");
        	if(control.getEditEnterCallback()!=null) {
        		control.getEditEnterCallback().call(text);
        	}
            control.setText(text);
            editField.setVisible(false);
            textPane.setVisible(true);
        }
    };

    /**
     * 当编辑框失去焦点以后，退出编辑模式
     */
    private ChangeListener<Boolean> editFieldFocusListener = (ob, ov, nv) -> {
        if(!nv){
            control.setText(editField.getText().replace("\n", ""));
            editField.setVisible(false);
            textPane.setVisible(true);
        }
    };

    /**
     * 点击关闭图标，删除自身节点
     */
    private EventHandler<MouseEvent> clickCloseHandler = e ->{

        try{
            Pane parent = (Pane) control.getParent();
            if(parent!=null){
                parent.getChildren().remove(control);
            }else{
                control.setOpacity(0);
                control.setManaged(false);
                control.setVisible(false);
            }
        }catch (Exception e1){

        }

        if(control.getCloseCallback()!=null){
            control.getCloseCallback().call(null);
        }
    };

    /**
     * 鼠标移出/入到关闭图标的时候，关闭图标的外观变化监听
     */
    private ChangeListener<Boolean> iconHoverListener = (ob, ov, nv) ->{

        String color = "";
        Paint fontColor = null;
        if(nv){
            if(HueType.LIGHT.equals(control.getHueType())){
                color = "#ffffff";
                fontColor = control.getColorType().getPaint();
            }else{
                color = control.getColorType().getColor();
                fontColor = Color.WHITE;
            }
        }else{
            if(HueType.LIGHT.equals(control.getHueType())){
                color = "transparent";
                fontColor = Color.WHITE;
            }else{
                color = "transparent";
                fontColor = control.getColorType().getPaint();
            }
        }

        closePane.setStyle("-fx-padding: 2px; -fx-background-color: "+color+"; -fx-background-radius: 20px; -fx-border-radius: 20px; ");
        closeIcon.setFill(fontColor);
    };

    /**
     * 双击鼠标，进入编辑模式。
     */
    private EventHandler<MouseEvent> editClickHandler = e -> {
        if(e.getClickCount() > 1 && control.isEditable()){
            double width = control.prefWidth(-1),
                    height = control.prefHeight(-1);

//            SizeType sizeType = control.getSizeType();
//            double y = 2;
//            if(sizeType.equals(SizeType.MEDIUM)){
//                y = 2;
//            }else if(sizeType.equals(SizeType.LARGE)){
//                y = 2;
//            }

            this.editField.setText(control.getText());
            this.editField.setVisible(true);
            this.textPane.setVisible(false);


        }
    };

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width = super.computePrefWidth(height, topInset, rightInset,bottomInset,leftInset);

        if(control.isCloseable()){
            width += 4+closePane.prefWidth(-1);
        }

        Insets padding = control.getPadding();
        if(padding == null) padding = Insets.EMPTY;


        width += padding.getLeft() + padding.getRight();

        return width;
    }

//    @Override
//    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
////        double height = textPane.prefHeight(-1);
////        Insets padding = control.getPadding();
////        if(padding!=null){
////            height += padding.getTop() + padding.getBottom();
////        }
////
////        return height;
//
////        double height = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
////        Insets padding = control.getPadding();
////        if(padding == null) padding = Insets.EMPTY;
////
////        height += padding.getTop() + padding.getBottom();
////
////        return height;
//    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        double tX, tY, tW = textPane.prefWidth(-1), tH = textPane.prefHeight(-1);
//        double width = control.prefWidth(-1), height = control.prefHeight(-1);

        if(control.isCloseable()){

            double cX, cY, cW = closePane.prefWidth(-1), cH = closePane.prefHeight(-1);

            tX = (contentWidth - tW)/2 - cW/2;
            tY = contentHeight/2 - tH/2 + 1;

            if(tX < 4)
                tX = 4;

            cX = tX + 4 + tW;
            cY = contentHeight/2 - cH/2;

            layoutInArea(closePane, cX, cY+2, cW, cH, 0, HPos.CENTER, VPos.CENTER);
        }else{
            tX = (contentWidth - tW)/2;
            tY = (contentHeight - tH)/2+1;
        }

        Insets padding = control.getPadding();
        if(padding!=null){
            tX += padding.getLeft();
        }

        layoutInArea(textPane, tX, tY, tW, tH, 0, HPos.CENTER, VPos.CENTER);
        
        if(editField!=null && editField.isVisible()) {
        	 double efHeight = editField.prefHeight(-1);
             layoutInArea(editField, 6d, (contentHeight - efHeight)/2+4,  
            		 tW+6, efHeight, 0,
                     HPos.CENTER, VPos.CENTER);
        }
       
    }

    @Override
    public void dispose() {
        super.dispose();

        if(editField!=null){
            editField.removeEventHandler(KeyEvent.KEY_RELEASED, editFieldEnterHandler);
            editField.focusedProperty().removeListener(editFieldFocusListener);
        }

        this.control.removeEventHandler(MouseEvent.MOUSE_CLICKED, editClickHandler);

        if(closePane!=null){
            closePane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickCloseHandler);
            closePane.hoverProperty().removeListener(iconHoverListener);
        }

        control.closeableProperty().removeListener(propertyChangeListener);
        control.editableProperty().removeListener(propertyChangeListener);
        control.hueTypeProperty().removeListener(propertyChangeListener);
        control.sizeTypeProperty().removeListener(propertyChangeListener);
        control.colorTypeProperty().removeListener(propertyChangeListener);
        control.roundTypeProperty().removeListener(propertyChangeListener);
        control.borderTypeProperty().removeListener(propertyChangeListener);


    }
}
