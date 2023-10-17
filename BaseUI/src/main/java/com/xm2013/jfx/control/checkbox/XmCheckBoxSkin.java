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
package com.xm2013.jfx.control.checkbox;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.label.SelectableTextSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;

public class XmCheckBoxSkin<T> extends SelectableTextSkin {

    private final static String SELECTED_PATH = "M0,7L3,4L8,9L17,0L20,3L8,15Z";
    private final static String SELECTED_CIRCLE_PATH = "M 0 8 C 0 12.417969 3.582031 16 8 16 C 12.417969 16 16 12.417969 16 8 C 16 3.582031 12.417969 0 8 0 C 3.582031 0 0 3.582031 0 8 Z M 0 8 ";
    private final static String SELECTED_RECTANGLE_PATH = "M0,0H15V15H0Z";
    private final static String INDETERMINATE_PATH = "M0,0H15V5H0Z";
    private final StackPane box;

    private SVGPath selectedMark;
    private SVGPath indeterminateMark;

    private final Pane textPane;

    private XmCheckBox control;
    private double gap = 4;

    private final Region fillRegion;
    private Circle circle;
    private Timeline timeline;
    private ScaleTransition selectedMarkTrans, indeterminateMarkTrans;

    private final DoubleProperty sizeScale = new SimpleDoubleProperty(1);
    public XmCheckBoxSkin(XmCheckBox<T> control) {
        super(control);
        this.control = control;

        //构建组件结构
        textPane = (Pane) this.getChildren().get(0);

        box = new StackPane();
        box.getStyleClass().add("box");

        selectedMark = new SVGPath();
        selectedMark.setVisible(false);

        indeterminateMark = new SVGPath();
        indeterminateMark.setVisible(false);
        indeterminateMark.setContent(INDETERMINATE_PATH);

        fillRegion = new Region();
        fillRegion.setManaged(true);

        box.getChildren().addAll(fillRegion, selectedMark, indeterminateMark);

        this.getChildren().add(box);

        //初始状态，外观
        if(control.isRadioButton()){
            selectedMark.setContent(SELECTED_CIRCLE_PATH);
        }else{
            selectedMark.setContent(SELECTED_PATH);
        }
        this.control.setFocusTraversable(true);
        setBoxSize();
        updateColor(1);
        initAnimate();
        updateSelected();
        if(control.isDisable()){
            control.setOpacity(0.65);
        }
        if(this.control.getValue()!=null){
            this.control.setText(this.control.getConverter().toString(this.control.getValue()));
        }

        //添加事件，监听
        control.hoverProperty().addListener(hoverListener);
        control.focusedProperty().addListener(focusListener);
        control.indeterminateProperty().addListener(selectedListener);
        control.selectedProperty().addListener(selectedListener);
        control.disabledProperty().addListener(disableListener);
        control.sizeTypeProperty().addListener(sizeTypeListener);
        control.roundTypeProperty().addListener(roundTypeListener);
        control.valueProperty().addListener(valueListener);
        control.colorTypeProperty().addListener(colorTypeListener);
        control.radioButtonProperty().addListener(radioModelListener);
        control.addEventFilter(MouseEvent.MOUSE_CLICKED, clickHandler);
        control.addEventFilter(KeyEvent.KEY_RELEASED, keyEventHandler);
    }

    /*--------------------------Event / Listener ----------------------*/
    private final ChangeListener<Boolean> radioModelListener = (ob, ov, nv) -> {
        if(control.isRadioButton()){
            selectedMark.setContent(SELECTED_CIRCLE_PATH);
        }else{
            selectedMark.setContent(SELECTED_PATH);
        }
    };

    private final ChangeListener<ColorType> colorTypeListener = (ob, ov, nv )->{
        updateColor(1);
    };

    private final ChangeListener<Object> sizeTypeListener = (ob, ov, nv) -> setBoxSize();
    private final ChangeListener<T> valueListener = new ChangeListener<T>() {
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            control.setText(control.getConverter().toString(newValue));
        }
    };

    private final ChangeListener<Object> roundTypeListener = (ob, ov, nv) -> updateColor(1);

    /**
     * 点击切换选中状态
     */
    private final EventHandler<MouseEvent> clickHandler = e -> {
        control.requestFocus();
        setSelectStatus();
    };

    /**
     * 在有焦点的情况下，键盘可以控制是否选中的事件
     */
    private final EventHandler<KeyEvent> keyEventHandler = e->{
        if(control.isFocused() && (e.getCode().equals(KeyCode.ENTER)|| e.getCode().equals(KeyCode.SPACE))){
            setSelectStatus();
        }
    };

    /**
     * 显示对应的状态外观，并且在显示的时候播放动画
     */
    private final ChangeListener<Boolean> selectedListener = (ob, ov, nv) -> {
        updateSelected();
    };

    /**
     * 监听hover属性，显示/隐藏 清除图标， 组件颜色变换
     */
    private final ChangeListener<Boolean> hoverListener = (ob, ov, nv) -> {
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
    };

    /**
     * 获得/失去
     * VERTICAL_INLINE状态下，焦点 label的变化，
     * 组件颜色变换
     */
    private final ChangeListener<Boolean> focusListener = (ob, ov, nv) -> {

        if(nv){
            updateColor(3);
        }else{
            if(control.isHover()){
                updateColor(2);
            }else{
                updateColor(1);
            }
        }
    };

    /**
     * 设置disable时候的状态
     */
    private final ChangeListener<Boolean> disableListener = (ob, ov, nv) -> {
        if(nv){
            control.setOpacity(0.65);
        }else{
            control.setOpacity(1);
        }
    };

    /* -------------------- private method ----------------------------- */
    private void updateSelected() {

        boolean allow = this.control.isAllowIndeterminate();
        boolean selected = this.control.isSelected();
        boolean indeterminate = this.control.isIndeterminate();

        timeline.playFrom(Duration.seconds(0));

        //以下逻辑是treeView测试下，才能够正常使用
        if(indeterminate){
            selectedMark.setVisible(false);
            indeterminateMark.setVisible(true);
            indeterminateMarkTrans.playFrom(Duration.seconds(0));
        }else{
            if(selected){
                selectedMarkTrans.setRate(1);
                selectedMarkTrans.playFrom(Duration.seconds(0));
                selectedMark.setVisible(true);
                indeterminateMark.setVisible(false);
            }else{
                indeterminateMark.setVisible(false);
                selectedMarkTrans.setRate(-1);
                selectedMarkTrans.play();
            }
        }

        //以下个人认为是比较严谨的逻辑
//        if(allow){
//            boolean indeterminate = this.control.isIndeterminate();
//
//            if(selected){
//                selectedMarkTrans.setRate(1);
//                selectedMarkTrans.playFrom(Duration.seconds(0));
//                selectedMark.setVisible(true);
//                indeterminateMark.setVisible(false);
//            }else{
//                if(indeterminate){
//                    selectedMark.setVisible(false);
//                    indeterminateMark.setVisible(true);
//                    indeterminateMarkTrans.playFrom(Duration.seconds(0));
//                }else{
////                    selectedMark.setVisible(false);
//                    indeterminateMark.setVisible(false);
//
//                    selectedMarkTrans.setRate(-1);
//                    selectedMarkTrans.play();
//                }
//            }
//        }else{
//            if(selected){
//                selectedMarkTrans.setRate(1);
//                selectedMarkTrans.playFrom(Duration.seconds(0));
//                this.selectedMark.setVisible(true);
//            }else{
//                selectedMarkTrans.setRate(-1);
//                selectedMarkTrans.play();
//            }
//            indeterminateMark.setVisible(false);
//        }

//        System.out.println(control.getValue()+", selected:"+control.isSelected()+", indeterminate: "+control.isIndeterminate());
        //当两个checkbox互斥时，A选中，当点击选中B, 此时的A会变成未选中，此时A的颜色应该变成默认颜色，而不是高亮颜色
        updateColor(1);
//        if(!control.isSelected() && !control.isIndeterminate() && !control.isFocused()){
//        }

    }

    private void initAnimate(){

        fillRegion.backgroundProperty().bind(Bindings.createObjectBinding(()->{

            List<BorderStroke> fillList = box.getBorder().getStrokes();
            BackgroundFill[] fills = new BackgroundFill[fillList.size()];
            for(int i=0; i<fillList.size(); i++){
                BorderStroke fill = fillList.get(i);
                fills[i] = new BackgroundFill(fill.getTopStroke(), fill.getRadii(), fill.getInsets());
            }
            return new Background(fills);

        }, box.borderProperty()));

//        fillRegion.borderProperty().bind(Bindings.createObjectBinding(()->{
//
//
//        }, box.borderProperty() ));

        circle = new Circle();
        circle.centerYProperty().bind(box.widthProperty().divide(2));
        circle.centerXProperty().bind(box.heightProperty().divide(2));
        circle.setRadius(0);

        fillRegion.setClip(circle);

        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),  "kf1",
                        new KeyValue(circle.opacityProperty(),  0.75),
                        new KeyValue(circle.radiusProperty(), 0)
                ),
                new KeyFrame(Duration.millis(400), "kf2",
                        new KeyValue(circle.opacityProperty(), 0),
                        new KeyValue(circle.radiusProperty(), box.getPrefHeight()*2)
                )
        );

        /////
        selectedMarkTrans = new ScaleTransition();
        selectedMarkTrans.setNode(selectedMark);
        selectedMarkTrans.setFromX(0);
        selectedMarkTrans.toXProperty().bind(sizeScale);
//        selectedMarkTrans.setToX(selectedMark.getScaleX());
        selectedMarkTrans.setFromY(0);
//        selectedMarkTrans.setToY(selectedMark.getScaleY());
        selectedMarkTrans.toYProperty().bind(sizeScale);
        selectedMarkTrans.setDuration(Duration.millis(150));
        selectedMarkTrans.setOnFinished(e->{
            if(!control.isSelected()){
                selectedMark.setVisible(false);
            }
        });

        indeterminateMarkTrans = new ScaleTransition();
        indeterminateMarkTrans.setNode(indeterminateMark);
        indeterminateMarkTrans.setFromX(0);
//        indeterminateMarkTrans.setToX(indeterminateMark.getScaleX());
        indeterminateMarkTrans.toXProperty().bind(sizeScale);
        indeterminateMarkTrans.setFromY(0);
//        indeterminateMarkTrans.setToY(indeterminateMark.getScaleY());
        indeterminateMarkTrans.toYProperty().bind(sizeScale);
        indeterminateMarkTrans.setDuration(Duration.millis(150));

    }

    /**
     * 设置组件大小
     */
    private void setBoxSize(){
        SizeType sizeType = control.getSizeType();
        double size = 28;
        double scale = 1;
        double fontSize = 14;
        double padding = 5;
        gap = 6;
        if(SizeType.SMALL.equals(sizeType)){
            size = 20;
            scale = 0.7;
            fontSize =  12;
            padding = 2.5;
            gap = 4;
        }else if(SizeType.LARGE.equals(sizeType)) {
            size = 36;
            scale = 1.2;
            fontSize = 18;
            gap = 8;
        }

        box.setPrefWidth(size);
        box.setMaxWidth(size);
        box.setPrefHeight(size);
        box.setMaxHeight(size);
        box.setMinHeight(size);
        box.setMinWidth(size);

        fillRegion.setPrefWidth(size);
        fillRegion.setPrefHeight(size);

        selectedMark.setScaleX(scale);
        selectedMark.setScaleY(scale);
        indeterminateMark.setScaleX(scale);
        indeterminateMark.setScaleY(scale);
        sizeScale.set(scale);
        control.setFont(Font.font(control.getFont().getFamily(), fontSize));

        control.setPadding(new Insets(padding, 0, padding, 0));
    }

    /**
     * 修改状态
     */
    private void setSelectStatus() {
        boolean allow = this.control.isAllowIndeterminate();
        boolean selected = this.control.isSelected();

        if(allow){
            boolean indeterminate = this.control.isIndeterminate();

            if(selected){
                this.control.setSelected(false);
                this.control.setIndeterminate(false);
            }else{
                if(indeterminate){
                    this.control.setSelected(true);
                    this.control.setIndeterminate(false);
                }else{
                    this.control.setIndeterminate(true);
                    this.control.setSelected(false);
                }
            }
        }else{
            this.control.setSelected(!selected);
            this.control.setIndeterminate(false);
        }

    }

    /**
     * 更新颜色
     * 1: 正常
     * 2: hover
     * 3: focus
     * 4: out hover-focus
     * 5: select 这个设置自定义选中颜色, 主要目前用于XmTreeView选中的时候，
     */
    private void updateColor(int status){

        Paint outColor, borderColor;

        Paint paint = control.getColorType().getPaint();
        if(status == 2){
            outColor = Color.TRANSPARENT;
            borderColor = paint;
        }else if(status == 3){
            outColor = FxKit.getOpacityPaint(paint, 0.17);
            borderColor = paint;
        }else if(status == 4){
            outColor = FxKit.getOpacityPaint(paint, 0.10);
            borderColor = FxKit.derivePaint(paint, 0.1);
        }else{

            //默认是灰色，但是启用了高亮，并且已经选中，那么这个时候的颜色应该是默认颜色，而不是灰色
//            System.out.println(control.getValue());
//            System.out.println(paint);
            outColor = Color.TRANSPARENT;
//            System.out.println("isSelectedHighLight:"+control.isSelectedHighLight());
            if(control.isSelectedHighLight() || (control.isSelected() || control.isIndeterminate())){
                borderColor = paint;
            }else{
                borderColor = Color.web("#999999");
            }
        }

        RoundType roundType = control.getRoundType();
        Insets insets1, insets2;
        double widths1, widths2, round = 0;
        Paint topColor = outColor, rightColor = outColor, bottomColor = outColor, leftColor=outColor,
                topBorderColor = borderColor, rightBorderColor = borderColor, bottomBorderColor = borderColor, leftBorderColor = borderColor;

        insets1 = new Insets(0);
        insets2 = new Insets(-3.5);
        widths1 = 1.25;
        widths2 = 3.5;

        if(RoundType.SMALL.equals(roundType)){
            round = 4;
        }else if(RoundType.SEMICIRCLE.equals(roundType)){
            round = 30;
        }else if(RoundType.CIRCLE.equals(roundType)){
            round = 30;
        }

        BorderStroke stroke1 = new BorderStroke(topBorderColor, rightBorderColor, bottomBorderColor, leftBorderColor,
                BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,
                new CornerRadii(round), new BorderWidths(widths1), insets1);

        BorderStroke stroke2 = new BorderStroke(topColor, rightColor, bottomColor, leftColor,
                BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,
                new CornerRadii(round), new BorderWidths(widths2), insets2);

        Border border = new Border(stroke1, stroke2);

        selectedMark.setFill(borderColor);
        indeterminateMark.setFill(borderColor);

//        this.control.setBorder(border);
        this.box.setBorder(border);

    }

    /* ------------------------- override -------------------------------------*/

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width = textPane.prefWidth(-1)+gap+box.prefWidth(-1);

        Insets margin = control.getMargin();
        if(margin!=null){
            width += margin.getLeft()+margin.getRight();
        }

        Insets padding = control.getPadding();
        if(padding!=null){
            width += padding.getLeft()+padding.getRight();
        }

        width += leftInset + rightInset;
//        System.out.println("computeWidth: "+width);
        return width;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double height = Math.max(textPane.prefHeight(-1), box.prefHeight(-1));

        Insets margin = control.getMargin();
        if(margin!=null){
            height += margin.getTop()+margin.getBottom();
        }

        Insets padding = control.getPadding();
        if(padding!=null){
            height += padding.getTop()+padding.getBottom();
        }

        height += topInset+bottomInset;

        return height;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);

        double height = control.prefHeight(-1);

        double boxX, boxY, boxWidth = box.prefWidth(-1), boxHeight = box.prefHeight(-1),
               textPaneX, textPaneWidth = textPane.prefWidth(-1), textPaneY,  textPaneHeight = textPane.prefHeight(-1);


        Insets margin = control.getMargin();
        Insets padding = control.getPadding();

        if(margin == null){
            margin = Insets.EMPTY;
        }
        if(padding == null){
            padding = Insets.EMPTY;
        }

        boxX = margin.getLeft() + padding.getLeft();
        boxY = (height - boxHeight)/2;

        textPaneX = boxX + boxWidth +gap;
        textPaneY = (height - textPaneHeight)/2;

//        System.out.println(String.format("control(w,h: %f, %f), content(w,h:%f, %f), box(w,h, x, y: %f, %f, %f, %f), text(w, h, x, y: %f, %f, %f, %f)"
//                , width, height, contentWidth, contentHeight, boxWidth, boxHeight, boxX, boxY, textPaneWidth, textPaneHeight, textPaneX, textPaneY));


        layoutInArea(box, boxX, boxY, boxWidth, boxHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(textPane, textPaneX, textPaneY, textPaneWidth, textPaneHeight, 0, HPos.CENTER, VPos.CENTER);

    }

    @Override
    public void dispose() {
        super.dispose();

        control.hoverProperty().removeListener(hoverListener);
        control.focusedProperty().removeListener(focusListener);
        control.indeterminateProperty().removeListener(selectedListener);
        control.selectedProperty().removeListener(selectedListener);
        control.disabledProperty().removeListener(disableListener);
        control.sizeTypeProperty().removeListener(sizeTypeListener);
        control.roundTypeProperty().removeListener(roundTypeListener);
        control.removeEventFilter(MouseEvent.MOUSE_CLICKED, clickHandler);
        control.removeEventFilter(KeyEvent.KEY_RELEASED, keyEventHandler);

        circle.centerYProperty().unbind();
        circle.centerXProperty().unbind();

    }
}
