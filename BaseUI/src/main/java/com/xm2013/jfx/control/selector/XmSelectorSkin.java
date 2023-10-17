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
package com.xm2013.jfx.control.selector;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ClickShadowAnimate;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.icon.XmIcon;
import com.xm2013.jfx.control.label.XmTag;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmSelectorSkin<T> extends SkinBase<XmSelector<T>> {

    private Pane backgroundPane;

    /**
     * 文本编辑框
     */
    private TextField textField = null;
    /**
     * 内容容器
     */
    private Pane iconPane = null;

    private final List<XmTag> selectTags = new ArrayList<>();

    private XmSelector<T> control = null;
    
    //组件外观信息缓存，用于动态切换组件时缓存改变之前的外观信息
    private final Map<Integer, SkinInfo> skins = new HashMap<Integer, SkinInfo>();

    private ClickAnimate iconClickAnimate;
    private SelectorPopup<T> popup;
    
    private List<T> items = new ArrayList<T>();

    public XmSelectorSkin(XmSelector<T> control) {
        super(control);
        this.control = control;

        backgroundPane = new Pane();
        iconPane = new Pane();

        textField = new TextField();
        textField.getStyleClass().add(FxKit.INPUT_STYLE_CLASS);

        if(control.isMultiple()){
            getChildren().addAll(backgroundPane, textField);
        }else{
            getChildren().addAll(backgroundPane, iconPane, control.getArrowIcon(), textField);
        }

        if(!control.isMultiple() && control.getPrefix()!=null){
            getChildren().add(control.getPrefix());
        }

        updateValues();

        //设置组件布局，颜色，位置
        updateColor(1,0);

        iconPane.setFocusTraversable(!control.isMultiple());

        //监听组件属性，和事件
//        textField.editableProperty().bind(control.editableProperty());
        this.control.focusedProperty().addListener(valuePaneFocusListener);

        iconPane.focusedProperty().addListener(iconPaneFocusListener);
        iconPane.hoverProperty().addListener(iconPaneHover);
        iconPane.addEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);


        textField.focusedProperty().addListener(valuePaneFocusListener);
        textField.addEventFilter(KeyEvent.KEY_PRESSED, enterTextListener);
        backgroundPane.addEventFilter(MouseEvent.MOUSE_CLICKED, backgroundPaneClickHandler);
        control.valuesProperty().get().addListener(valuesChangeListener);

        //监听关闭弹窗
        this.control.getScene().getWindow().focusedProperty().addListener(windowFocusListener);
        this.control.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, windowClickFilter);

        //监听组件位置变化，来调整弹窗的位置
        this.control.layoutXProperty().addListener(layoutChangeListener);
        this.control.layoutYProperty().addListener(layoutChangeListener);
        this.control.heightProperty().addListener(layoutChangeListener);
        this.control.widthProperty().addListener(layoutChangeListener);

        control.getArrowIcon().hoverProperty().addListener(iconPaneHover);
        control.getArrowIcon().addEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);

        registerChangeListener(control.editableProperty(), e -> {
            if(control.isEditable()){
                if(control.isMultiple()){
                    textField.setVisible(true);
                    textField.setManaged(true);
                }else{
                    textField.setEditable(true);
                }
            }else{
                if(control.isMultiple()){
                    textField.setVisible(false);
                    textField.setManaged(false);
                }else{
                    textField.setEditable(false);
                }
            }
        });
        registerChangeListener(control.multipleProperty(), e-> {

            ObservableList<Node> children = getChildren();
            if(control.isMultiple()){
                children.remove(iconPane);
                children.remove(control.getArrowIcon());
                textField.setText(null);

                control.getArrowIcon().hoverProperty().removeListener(iconPaneHover);
                control.getArrowIcon().removeEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);

            }else{
                if(!children.contains(control.getArrowIcon())){
                    children.add(control.getArrowIcon());

                    control.getArrowIcon().hoverProperty().addListener((ob, ov, nv)->{
                        System.out.println(nv);
                    });
                    control.getArrowIcon().addEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);
                }
                if(!children.contains(iconPane)){
                    children.add(iconPane);
                }
            }
            updateValues();
            updateColor(1,0);
            popup = null;
        });
        registerChangeListener(control.colorTypeProperty(), e -> {
            skins.clear();
            updateColor(1,0);
            updateValues();
            popup = null;
        });
        registerChangeListener(control.sizeTypeProperty(), e->{
            updateValues();
            updateColor(1,0);
            if(control.isMultiple()){
                textField.setText(null);
            }
        });
        registerChangeListener(control.roundTypeProperty(), e->{
            skins.clear();
            updateColor(1,0);
        });
        registerChangeListener(control.hueTypeProperty(), e->{
            skins.clear();
            updateColor(1,0);
            updateValues();
            popup = null;
        });

        registerChangeListener(control.itemsProperty(), e->{
        	items.clear();
            popup = null;
        });

        control.prefixProperty().addListener(prefixListener);
        registerChangeListener(control.fillArrowProperty(), e->updateColor(1,0));
    }

    /* ----------------------------- override ----------------------------- */
    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {

        double width = control.getPrefWidth();
        if(width<=0){
            width = 300;
        }

        return width;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        Insets padding = control.getPadding();
        double height =  control.getPrefHeight();
        if(height<=0){
            if(control.isMultiple()){

                width = control.prefWidth(-1);
                if(padding!=null){
                    width  = width - padding.getLeft() - padding.getRight();
                }

                double lineHeight = this.textField.prefHeight(-1);
                double currWidth = 0;

                for(int i=0; i<selectTags.size(); i++){
                    XmTag tag = selectTags.get(i);
                    double tagWidth = tag.prefWidth(-1);

                    if(tagWidth >= width){
                        if(currWidth == 0){
                            height += lineHeight + 3;
                        }else{
                            height += lineHeight *2 + 6;
                            currWidth = 0;
                        }
                    }else{
                        currWidth += tagWidth +6;
                        
//                        System.out.println(tag.getText()+", tagWidth:"+tagWidth+", currWidth:"+currWidth+", width:"+width);
                        
                        if(currWidth >= width){
                            height += lineHeight + 3;
                            currWidth = tagWidth;
                        }
                        if(i == selectTags.size()-1){
                            height += lineHeight+3;
                        }
                    }
                }

                if(control.isEditable()){
                    double textFieldWidth = 120;

                    if(currWidth == 0){
                        if(selectTags.size() == 0){
                            height = 44 - padding.getTop() - padding.getBottom();
                        }else{
                            height += lineHeight + 3;
                        }
                    }else{
                        if(width-currWidth<textFieldWidth){
                            height += lineHeight;
                        }
                    }
                }else if(selectTags.size() == 0){
                	height = this.textField.prefHeight(-1);
                }

            }else{
                height = this.textField.prefHeight(-1);
            }
        }

        height += padding.getTop() + padding.getBottom();

        return height;

    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {

        if(control.getMinHeight()>-1){
            return control.getMinHeight();
        }

        double height;
        SizeType sizeType = control.getSizeType();
        if(sizeType.equals(SizeType.SMALL)){
            height = 24.6;
        }else if(sizeType.equals(SizeType.MEDIUM)){
            height = 44.1;
        }else {
            height = 52.8;
        }

        return height;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        if(control.isMultiple()){
            control.getArrowIcon().setVisible(false);

            
            Insets padding = control.getPadding();
            double x = padding.getLeft(), y = padding.getTop(),
                width = control.prefWidth(-1) - padding.getLeft() - padding.getRight();

            double tagHeight = textField.prefHeight(-1);

            for(int i=0; i<selectTags.size(); i++){
                XmTag tag = selectTags.get(i);

                double tagWidth = tag.prefWidth(-1);
//                System.out.println(i+":"+tagWidth);

                if(x + 3 + tagWidth > width && x != padding.getLeft()){
                    x = padding.getLeft();
                    y = y + tagHeight + 3;
                }

                layoutInArea(tag, x, y, tagWidth, tagHeight, 0, HPos.CENTER, VPos.CENTER);

                x += 3 + tagWidth;
                if(x > width){
                    x = padding.getLeft();
                    y = y + tagHeight + 3;
                }
            }

            if(control.isEditable()){
                double fieldWidth = 120;
                double fieldHeight = textField.prefHeight(-1);

                double balanceWidth = width - x - 3;
                if(balanceWidth>fieldWidth){
                    fieldWidth = balanceWidth-10;
                }else{
                    fieldWidth = width - 30;
                }

                if(x + 3 + fieldWidth > width && x != padding.getLeft()){
                    x = padding.getLeft();
                    y = y + fieldHeight + 3;
                }

                textField.setPrefWidth(fieldWidth);
                layoutInArea(textField, x, y, fieldWidth, textField.prefHeight(-1), 0, HPos.CENTER, VPos.CENTER);
            }

            layoutInArea(backgroundPane, 0, 0, contentWidth, control.prefHeight(-1), 0, HPos.CENTER, VPos.CENTER);

        }else{
            Insets padding = control.getPadding();
            double prefixX = padding.getLeft(), prefixY = 0, prefixWidth = 0, prefixHeight = 0;
            Node prefix = control.getPrefix();
            if(prefix!=null){
                prefixWidth = prefix.prefWidth(-1);
                prefixHeight = prefix.prefHeight(-1);
                prefixY = (contentHeight - prefixHeight) / 2;
            }

            contentHeight = control.prefHeight(-1);
            Node arrowIcon = control.getArrowIcon();
            double bgWidth = this.control.prefWidth(-1) - contentHeight;
            double iconPaneX = bgWidth;
            double iconWidth = arrowIcon.prefWidth(-1),
                    iconHeight = arrowIcon.prefHeight(-1),
                    iconX = iconPaneX,
                    iconY = (contentHeight - iconHeight) / 2;
            double textFieldWidth = bgWidth - padding.getLeft() - padding.getRight() - prefixWidth,
                    textFieldHeight = textField.prefHeight(-1),
                    textFieldX = prefixX + prefixWidth,
                    textFieldY = (contentHeight - textFieldHeight) / 2;

            textField.setPrefWidth(textFieldWidth);

//            System.out.println(String.format("icon(x:%f, y:%f, w:%f, h:%f), content:(x:%f, y:%f, w:%f, h:%f), background(x:%f, y:%f, w:%f, h:%f)" +
//                            ", textField(x:%f, y:%f, w:%f, h:%f), iconPane(x:%f, y:%f, w:%f, h:%f)"
//                    , iconX, iconY, iconWidth, iconHeight
//                    , contentX, contentY, contentWidth, contentHeight
//                    , 0d, 0d, bgWidth, contentHeight
//                    , textFieldX, textFieldY, textFieldWidth, textFieldHeight
//                    , iconPaneX, 0d, contentHeight, contentHeight
//                    ));

            layoutInArea(backgroundPane, 0, 0, bgWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
            layoutInArea(textField, textFieldX, textFieldY, textFieldWidth, textFieldHeight, 0, HPos.CENTER, VPos.CENTER);
            layoutInArea(iconPane, iconPaneX, 0, contentHeight, contentHeight, 0, HPos.CENTER, VPos.CENTER);
//            layoutInArea(arrowIcon, iconX, iconY, iconWidth, iconHeight, 0, HPos.CENTER, VPos.CENTER);

            arrowIcon.setTranslateX(iconX);
            arrowIcon.setLayoutY(iconY);
            arrowIcon.setManaged(false);
            control.getArrowIcon().setVisible(true);

            if(prefix!=null){
                layoutInArea(prefix, prefixX, prefixY, prefixWidth, prefixHeight, 0, HPos.CENTER, VPos.CENTER);
            }

        }



        //重置图标的宽高，让他看起来居中，
        if(!control.isMultiple()){
            Node icon = control.getArrowIcon();
            double iconHeight = icon.prefHeight(-1),
                    iconWidth = icon.prefWidth(-1);
            // double height = rootPane.prefHeight(-1);
            double top = (contentHeight-iconHeight)/2,
                    left = (contentHeight - iconWidth)/2;
            icon.setLayoutX(left);
            icon.setLayoutY(top);
            iconPane.setMinWidth(contentHeight);

        }
    }

    @Override
    public void dispose() {
        super.dispose();

        this.control.focusedProperty().removeListener(valuePaneFocusListener);
        iconPane.focusedProperty().removeListener(iconPaneFocusListener);
        iconPane.hoverProperty().removeListener(iconPaneHover);
        iconPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);
        textField.focusedProperty().removeListener(valuePaneFocusListener);
        textField.removeEventFilter(KeyEvent.KEY_PRESSED, enterTextListener);
        backgroundPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, backgroundPaneClickHandler);
        control.valuesProperty().get().removeListener(valuesChangeListener);
        this.control.getScene().getWindow().focusedProperty().removeListener(windowFocusListener);
        this.control.getScene().removeEventFilter(MouseEvent.MOUSE_PRESSED, windowClickFilter);
        this.control.layoutBoundsProperty().removeListener(layoutChangeListener);
        unregisterChangeListeners(control.editableProperty());
        unregisterChangeListeners(control.multipleProperty());
        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.roundTypeProperty());
        unregisterChangeListeners(control.hueTypeProperty());
        control.prefixProperty().removeListener(prefixListener);
        unregisterChangeListeners(control.fillArrowProperty());

    }

    /* ----------------------------- listener / event ----------------------*/

    private ChangeListener<Node> prefixListener = (ob, ov, nv)->{
        if(ov!=null){
            getChildren().remove(ov);
        }
        if(nv!=null && !control.isMultiple()){
            getChildren().add(nv);
        }
    };

    private EventHandler<MouseEvent> backgroundPaneClickHandler = e->{
        if(getChildren().contains(textField) && textField.isVisible()){
            textField.requestFocus();
        }
        updateColor(3, 1);
    };

    /**
     * 监听control位置变化
     */
    private ChangeListener<Object> layoutChangeListener = (ob, ov, nv)->{
        Bounds nodePoint = control.localToScreen(control.getLayoutBounds());
        double x = nodePoint.getMinX()-30;
        double y = nodePoint.getMaxY()-27;
        getPopup().setPosition(x,y);
    };

    /**
     * 监听选择值的变化
     */
    private ListChangeListener<T> valuesChangeListener = e -> {
        if(control.isMultiple()){
            updateValues();
        }else{
            if(control.getValues().size()==0){
                textField.setText(null);
            }else{
                T t = control.getValues().get(control.getValues().size() - 1);
                textField.setText(control.getConverter().toString(t));
            }
        }
    };

    /**
     * 文本框输入enter后，确定选择
     */
    private EventHandler<KeyEvent> enterTextListener = e->{
        if(e.getCode().equals(KeyCode.ENTER) ) {
            String text = textField.getText();
            if(text == null || text.trim().length() == 0){
                return;
            }
            text = text.replace("\n", "");
            textField.setText(null);
            if(text.trim().length()>0){
                T obj = control.getConverter().fromString(text);
                if(control.isMultiple()){
                    control.getValues().add(obj);
                }else{
                    control.getValues().setAll(obj);
                }

            }
        }
    };

    /**
     * 点击下拉按钮，弹出选择框
     */
    private EventHandler<MouseEvent> iconPaneClickHandler = e -> {
        iconPane.requestFocus();
        getIconClickAnimate().setPoint(e.getX(), e.getY()).play();
        show();
    };

    /**
     * 点击主窗体的时候，隐藏下拉框
     */
    private final EventHandler<MouseEvent> windowClickFilter = e -> {
        //判断是不是点击iconPane
        if(isControl((Node) e.getTarget(), 1) == 0){
            getPopup().doHide();
        }
    };

    /**
     * 窗体失去焦点的时候，隐藏下拉弹框
     */
    private final ChangeListener<Boolean> windowFocusListener = (ob, ov, nv) -> {
        if (!nv) {
            getPopup().doHide();
        }
    };

    /**
     * textPane focus
     */
    private ChangeListener<Boolean> valuePaneFocusListener = (ob, ov, nv) -> {
        if(nv){
            show();
            if(backgroundPane.isHover()){
                updateColor(3,1);
            }else{
                updateColor(4,1);
            }
        }else{
            getPopup().doHide();
            if(backgroundPane.isHover()){
                updateColor(2,1);
            }else {
                updateColor(1, 1);
            }
        }
    };

    /**
     * textPane hover
     */
    private ChangeListener<Boolean> textPaneHover = (ob, ov, nv) -> {
        if(nv){
            if(backgroundPane.isFocused() || textField.isFocused()){
                updateColor(3,1);
            }else{
                updateColor(2,1);
            }
        }else{
            if(backgroundPane.isFocused() || textField.isFocused()){
                updateColor(4,1);
            }else{
                updateColor(1, 1);
            }
        }
    };

    /**
     * iconPane focus
     */
    private ChangeListener<Boolean> iconPaneFocusListener = (ob, ov, nv) -> {
        if(nv){
            if(iconPane.isHover()){
                updateColor(3,2);
            }else{
                updateColor(4,2);
            }
        }else{
            if(iconPane.isHover()){
                updateColor(2,2);
            }else{
                updateColor(1, 2);
            }
        }
    };

    /**
     * iconPane hover
     */
    private ChangeListener<Boolean> iconPaneHover = (ob, ov, nv) -> {
        if(nv){
            if(iconPane.isFocused() ){
                updateColor(3,2);
            }else{
                updateColor(2,2);
            }
        }else{
            if(iconPane.isFocused() ){
                updateColor(4,2);
            }else{
                updateColor(1, 2);
            }
        }
    };


    /* -------------------------------- private method ----------------------------- */

    /**
     * 判断是不是组件里面的内容
     * @param node Node
     * @param index int
     * @return int
     */
    public int isControl(Node node, int index){

        if(node == null || index == 4) return 0;

        if(node.equals(control.getArrowIcon())){
            return 1;
        }else if(node.equals(iconPane)){
            return 1;
        }else if(node.equals(textField)){
            return 1;
        }else if(node.equals(backgroundPane)){
            return 1;
        }else if(selectTags.contains(node)){
            return 2;
        }else{
            return isControl(node.getParent(), ++index);
        }
    }

    /**
     * 显示下拉框
     */
    private void show(){


        SelectorPopup<T> popup2 = getPopup();
        if(!popup2.isShowing()) {
        	
        	Bounds nodePoint = control.localToScreen(control.getLayoutBounds());

            //因为popup我给添加dropshadow效果，会导致实际尺寸发生变化，所以，这里需要手动调整去除掉effect的变化
            double x = nodePoint.getMinX()-30;
            double y = nodePoint.getMaxY()-27;
        	popup2.doShow(control, control.getValues(),x,y);
        }
    }

    /**
     * 获取下拉框
     * @return
     */
    private SelectorPopup<T> getPopup() {

        if(popup == null){
            SelectorType selectorType = control.getSelectorType();
            if(SelectorType.TREE.equals(selectorType)){
                popup = new SelectorTreePopup<>(control );
            }else if(SelectorType.GRID.equals(selectorType)){
                popup = new SelectorGridPopup<>(control);
            }else{
                popup = new SelectorListPopup<>(control);
            }

        }
        return popup;
    }

    /**
     * 获取点击动画
     * @return
     */
    private ClickAnimate getIconClickAnimate(){
        if(iconClickAnimate == null){

            ClickAnimateType type = control.getClickAnimateType();
            if(ClickAnimateType.SHADOW.equals(type)){
                iconClickAnimate = new ClickShadowAnimate(iconPane, control.getColorType());
            }else if(ClickAnimateType.RIPPER.equals(type)){
                iconClickAnimate = new ClickRipperAnimate(iconPane, control.getColorType(), control.getHueType());
            }

            //设置阴影的动画节点所在的位置
            iconClickAnimate.setNodePosition((node, width, height, x, y) -> {

                Insets margin = control.getMargin();
                double left = 0, top = 0;
                if(margin!=null){
                    left = margin.getLeft();
                    top = margin.getTop();
                }

                double w = backgroundPane.prefHeight(-1);

                left += w;

//                System.out.println(String.format("left:%f, top:%f, height:%f, width:%f", left, top, iconPane.minWidth(-1), valuePane.prefHeight(-1) ));
                layoutInArea(node, left, top, iconPane.minWidth(-1), iconPane.getHeight(),
                        0, HPos.CENTER, VPos.CENTER);

            });

            //将动画节点添加控件中
            iconClickAnimate.setAddNode(node -> {
                if(ClickAnimateType.SHADOW.equals(type)){
                    getChildren().add(0, node);
                }else if(ClickAnimateType.RIPPER.equals(type)){
                    //node.setStyle("-fx-background-color: red;");
                    getChildren().add(node);
                }

            });

            //动画播放完成后，将动画节点从控件中移除
            iconClickAnimate.setRemoveNode(node -> getChildren().remove(node));
        }
        return iconClickAnimate;
    }

    /**
     * 获取外观样式信息
     * @param status
     * @return
     */
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
     * 设置外观颜色
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover|out focus
     * status: 3 : hover|focus状态下的颜色
     * status: 4 : out hover | focus状态的颜色
     * controlIndex: 0: all, 1: text, 2: icon
     */
    private void updateColor(int status, int controlIndex){
        BorderType borderType = control.getBorderType();
        SkinInfo info = getSkinInfo(status);

        Paint borderOutColor = info.getBorderOutColor(),
                backgroundColor = info.getBackgroundColor(),
                borderColor = info.getInnerBorderColor(),
                fontColor = info.getInnerBorderColor();

        double radii = info.getRadiusWidth(),
                widths1 = info.getOuterBorderWidth(),
                widths2 = info.getInnerBorderWidth();

        Insets insets1 = info.getOuterBorderInsets(),
                insets2 = info.getInnerBorderInsets();

        boolean isMultiple = control.isMultiple();
        boolean isArrowFill = control.isFillArrow();

        //构建边框
        Border valueBorder = null, iconBorder = null;
        if(BorderType.FULL.equals(borderType)){
            //外边框,
            BorderStroke stroke1 = new BorderStroke(
                    borderOutColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(radii, isMultiple?radii:0, isMultiple?radii:0, radii, false),
                    new BorderWidths(widths1), insets1);
            BorderStroke stroke2 = new BorderStroke(borderOutColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(isMultiple?radii:0, radii, radii, isMultiple?radii:0, false),
                    new BorderWidths(widths1), insets1);

            //内边框
            Paint rightColor = borderColor;
            if(HueType.LIGHT.equals(control.getHueType())){
                rightColor = FxKit.getLightPaint(control.getColorType().getPaint(), 0.5);
            }

            if(status == 1){
                borderColor = Color.web("#999999");
            }

            BorderStroke stroke11 = new BorderStroke(borderColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(radii, isMultiple?radii:0, isMultiple?radii:0, radii, false),
                    new BorderWidths(widths2), insets2);
            BorderStroke stroke21 = new BorderStroke(
                    borderColor, borderColor, borderColor, rightColor,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,
                    new CornerRadii(isMultiple?radii:0, radii, radii, isMultiple?radii:0, false),
                    new BorderWidths(widths2, widths2, widths2, isMultiple?widths2:0), insets2);
            valueBorder = new Border(stroke1, stroke11);
            iconBorder = new Border(stroke2, stroke21);
        }

        if(!isArrowFill){
            backgroundColor = Color.TRANSPARENT;
        }

        //构建背景色背景色
        BackgroundFill fill2 = new BackgroundFill(backgroundColor, new CornerRadii(0, radii, radii, 0, false),
                new Insets(0,0,0,0));
        Background background2 = new Background(fill2);

        fontColor = borderColor;

        Insets padding = info.getPadding();
        Insets npadding = padding;
        SizeType sizeType = control.getSizeType();
        if(sizeType.equals(SizeType.LARGE)){
            npadding = new Insets(9, 6, 9, 6);
        }else if(sizeType.equals(SizeType.MEDIUM)){
            npadding = new Insets(7, 6, 6.5, 6);
        }else if(sizeType.equals(SizeType.SMALL)){
            npadding = new Insets(0, 6, 0, 6);
        }

        //设置
        if(controlIndex == 1){
            //设置
            backgroundPane.setBorder(valueBorder);
            textField.setFont(Font.font(textField.getFont().getFamily(), info.getFontSize()-2));
            control.setPadding(npadding);
        }else if(controlIndex == 2){
            setArrowBtnColor(background2, iconBorder, fontColor);
        }else{
            control.setPadding(npadding);
            backgroundPane.setBorder(valueBorder);
            textField.setFont(Font.font(textField.getFont().getFamily(), info.getFontSize()-2));
            setArrowBtnColor(background2, iconBorder, fontColor);
        }
    }

    /**
     * 设置下拉按钮图标样式
     */
    private void setArrowBtnColor(Background bg, Border border, Paint fontColor){
        if(control.isMultiple()) {
            return;
        }
        Node icon = control.getArrowIcon();
        iconPane.setBackground(bg);
        iconPane.setBorder(border);

        if(icon instanceof XmIcon){
            ((XmIcon) icon).setColor(fontColor);
        }
    }

    /**
     * 更新选择的值到组件
     */
    private void updateValues(){
        ObservableList<T> values = control.getValues();

        if(selectTags.size()>0){
            for (XmTag tag : selectTags) {
            	removeTag(tag);
            }
            selectTags.clear();
        }

        if(!control.isMultiple() && values.size()>0){
            textField.setText(control.getConverter().toString(values.get(0)));
            return;
        }

        int currShowItem = 0;
        
        int size = values.size();
        for(int i=0; i<size; i++){
        	currShowItem += i;
            //如果设置最大显示条目，
            int maxTagCount = control.getMaxTagCount();
            if(maxTagCount>0 && currShowItem>=maxTagCount){
                XmTag tag = new XmTag("+"+(size-i)+"...");
                tag.setEditable(false);
                tag.setCloseable(false);
                tag.colorTypeProperty().bind(control.colorTypeProperty());
                tag.hueTypeProperty().bind(control.hueTypeProperty());
                tag.getStyleClass().add("value-tag");
                tag.setSizeType(SizeType.SMALL);
                tag.setFont(Font.font(12));

                getChildren().add(tag);

                selectTags.add(tag);

                break;
            }
            
            T val = values.get(i);
            boolean isSelectedItem = false;
            if(SelectorType.TREE.equals(control.getSelectorType())) {
            	if(items.size() == 0) {
            		buildItems(control.getItems());
            	}
            	isSelectedItem = items.contains(val);
            }else {
            	isSelectedItem = control.getItems().contains(val);
            }
            
            XmTag tag = buildTag(values.get(i), isSelectedItem);
            getChildren().add(tag);
            selectTags.add(tag);

        }
    }
    
    private void buildItems(List<T> items2) {
    	
    	SelectorConvert<T> converter = control.getConverter();
    	for (T t : items2) {
			items.add(t);
			List<T> children = converter.getChildren(t);
	        if(children!=null && children.size()>0){
	            buildItems(children);
	        }
		}
	}

	private void removeTag(XmTag tag) {
    	tag.closeableProperty().unbind();
        tag.editableProperty().unbind();
        tag.colorTypeProperty().unbind();
        tag.hueTypeProperty().unbind();
        getChildren().remove(tag);
    }
    
    private XmTag buildTag(T val, boolean isSelectedValue) {
    	
        XmTag tag = new XmTag(control.getConverter().toString(val));
        tag.editableProperty().bind(control.editableProperty());
        tag.closeableProperty().bind(control.closeableProperty());
        tag.setEditEnterCallback(s -> {
        	if(isSelectedValue) {
        		ObservableList<T> values = control.getValues();
        		T v = control.getConverter().fromString(s);
        		int idx = values.indexOf(val);
        		if(idx>-1) {
        			values.remove(idx);
        			if(idx>values.size()-1) {
        				values.add(v);
        			}else {
        				values.add(idx, v);
        			}
        		}else {
        			values.add(v);
        		}
        	}
        });
        tag.getStyleClass().add("value-tag");
        tag.setSizeType(SizeType.SMALL);
        tag.setCloseCallback(e -> {
            getChildren().remove(tag);
        	selectTags.remove(tag);
            control.getValues().remove(val);
            getPopup().removeItem(val);
        });
        tag.setTextWidth(control.getPrefWidth());
        if(val instanceof SelectorItem){
            SelectorItem item = (SelectorItem) val;
            ColorType itemColorType = item.getSelectedColorType();
            if(itemColorType!=null){
                tag.colorTypeProperty().bind(new SimpleObjectProperty<ColorType>(itemColorType));;
            }else{
                tag.colorTypeProperty().bind(control.colorTypeProperty());
            }

            HueType itemHueType = item.getSelectedHueType();
            if(itemHueType!=null){
                tag.setHueType(itemHueType);
            }else{
                tag.hueTypeProperty().bind(control.hueTypeProperty());
            }

        }else{
            tag.colorTypeProperty().bind(control.colorTypeProperty());
            tag.hueTypeProperty().bind(control.hueTypeProperty());
        }
        
        return tag;
    }
    
}
