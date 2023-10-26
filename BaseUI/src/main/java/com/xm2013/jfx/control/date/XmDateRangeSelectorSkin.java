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
package com.xm2013.jfx.control.date;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ClickShadowAnimate;
import com.xm2013.jfx.control.base.BorderType;
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SkinInfo;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class XmDateRangeSelectorSkin extends SkinBase {
    private Text arrow;  //两个文本框中的分隔线
    private Pane fieldPane;
    private TextField startField, endField;  //开始日期显示框， 结束日期的显示框

    private XmDateRangeSelector control;

    private XmSVGIcon icon;     //右边图标
    private Pane iconPane;      //右边的图标容器
    private ClickAnimate iconClickAnimate;  //右边的图标点击动画

    //组件外观信息缓存，用于动态切换组件时缓存改变之前的外观信息
    private final Map<Integer, SkinInfo> skins = new HashMap<Integer, SkinInfo>();

    private DateSelectorPopup popup;  //下拉框组件

    private Line focusLine;     //聚焦时的下划线

    private TextField prefFocusField;  //上一个聚焦的文本

    public XmDateRangeSelectorSkin(XmDateRangeSelector control) {
        super(control);
        this.control = control;

        //初始化
        startField = new TextField();
        startField.getStyleClass().add(FxKit.INPUT_STYLE_CLASS);
        startField.setPromptText(control.getPromptText());
        startField.setPadding(Insets.EMPTY);

        endField = new TextField();
        endField.getStyleClass().add(FxKit.INPUT_STYLE_CLASS);
        endField.setPromptText(control.getPromptText());
        endField.setPadding(Insets.EMPTY);

        arrow = new Text("-");
        arrow.setTextOrigin(VPos.BASELINE);
        arrow.setFont(Font.font(18));

        fieldPane = new Pane();

        if(this.control.getPrefWidth()<=0){
            this.control.setPrefWidth(300);
        }

        icon = new XmSVGIcon(FxKit.DATE_PATH);
        iconPane = new Pane(icon);

        focusLine = new Line();
        focusLine.setStroke(control.getColorType().getPaint());
        focusLine.setStrokeWidth(1.5);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2);
        shadow.setOffsetX(0);
        shadow.setColor(control.getColorType().getFxColor());
        shadow.setRadius(1);
        focusLine.setEffect(shadow);
        focusLine.setVisible(false);

//        control.heightProperty().addListener((ob, ov, nv)->{
//            System.out.println(nv);
//        });

        getChildren().addAll(fieldPane, startField, arrow, endField, iconPane, focusLine);

        iconPane.setFocusTraversable(true);

        //更新初始化外观
        updateSkin(1,0);


        //添加时间和监听
        startField.addEventFilter(KeyEvent.KEY_RELEASED, startFieldKeyReleaseHandler);
        endField.addEventFilter(KeyEvent.KEY_RELEASED, endFieldKeyReleaseHandler);

        iconPane.focusedProperty().addListener(iconStatusListener);
        iconPane.hoverProperty().addListener(iconStatusListener);
        iconPane.addEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);
        iconPane.addEventFilter(KeyEvent.KEY_RELEASED, iconPaneKeyReleaseHandler);

        this.control.getScene().getWindow().focusedProperty().addListener(windowFocusListener);
        this.control.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, windowClickFilter);
        this.fieldPane.hoverProperty().addListener(fieldPaneListener);

        this.control.selectedStartDateProperty().addListener(selectedStartDateListener);
        this.control.selectedEndDateProperty().addListener(selectedEndDateListener);

        registerChangeListener(startField.focusedProperty(), e ->startFieldStatusListener());
        registerChangeListener(startField.hoverProperty(), e ->startFieldStatusListener());
        registerChangeListener(endField.focusedProperty(), e ->endFieldStatusListener());
        registerChangeListener(endField.hoverProperty(), e ->endFieldStatusListener());
        registerChangeListener(control.hueTypeProperty(), e->skinChange());
        registerChangeListener(control.colorTypeProperty(), e->skinChange());
        registerChangeListener(control.sizeTypeProperty(), e->skinChange());
        registerChangeListener(control.roundTypeProperty(), e->skinChange());
        registerChangeListener(control.fillIconProperty(),  e->skinChange());

    }

    /* -------------------------------- event handler / listener --------------------------*/

    /**
     * 更新皮肤的监听
     */
    private void skinChange(){
        focusLine.setStroke(control.getColorType().getPaint());

        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2);
        shadow.setOffsetX(0);
        shadow.setColor(control.getColorType().getFxColor());
        shadow.setRadius(1);
        focusLine.setEffect(shadow);
        focusLine.setVisible(false);

        skins.clear();
        updateSkin(1, 0);
    }

    /**
     * 开始日期变化监听
     */
    private ChangeListener<LocalDateTime> selectedStartDateListener = (ob, ov, nv)->{

        if(nv!=null){
            String pattern = control.getFormatPattern();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formattedDate = nv.format(formatter);
            this.startField.setText(formattedDate);
            this.startField.end();
        }
    };

    /**
     * 结束日期变化监听
     */
    private ChangeListener<LocalDateTime> selectedEndDateListener = (ob, ov, nv) -> {
        String pattern = control.getFormatPattern();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = nv.format(formatter);
        this.endField.setText(formattedDate);
        this.endField.end();
    };

    /**
     * 开始日期文本框，输入处理
     */
    public EventHandler<KeyEvent> startFieldKeyReleaseHandler = e ->{
        if(e.getCode() == KeyCode.ENTER){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(control.getFormatPattern());
                LocalDateTime date = LocalDateTime.parse(this.startField.getText(), formatter);
                this.control.setSelectedStartDate(date);
                this.startField.end();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    /**
     * 结束日期文本框输入处理
     */
    public EventHandler<KeyEvent> endFieldKeyReleaseHandler = e -> {
        if(e.getCode() == KeyCode.ENTER){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(control.getFormatPattern());
                LocalDateTime date = LocalDateTime.parse(this.endField.getText(), formatter);
                this.control.setSelectedEndDate(date);
                this.endField.end();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    /**
     * 点击主窗体的时候，隐藏下拉框
     */
    private final EventHandler<MouseEvent> windowClickFilter = e -> {
        //判断是不是点击iconPane
        if(isControl((Node) e.getTarget(), 1) == 0){
            getPopup().hide();
        }
    };

    /**
     * 窗体失去焦点的时候，隐藏下拉弹框
     */
    private final ChangeListener<Boolean> windowFocusListener = (ob, ov, nv) -> {
        if (!nv) {
            getPopup().hide();
        }
    };

    /**
     * 播放按钮动画，显示下拉框
     */
    private EventHandler<MouseEvent> iconPaneClickHandler = (e)->{
        iconPane.requestFocus();
        getIconClickAnimate().setPoint(e.getX(), e.getY()).play();
        showFocusLine(startField);
        getPopup().setDate(control.getSelectedStartDate()).show(this.control);
    };

    /**
     * 按下enter, space键，出发icon的点击事件
     */
    private EventHandler<KeyEvent> iconPaneKeyReleaseHandler = e -> {
        if(iconPane.isFocused() && (e.getCode().equals(KeyCode.ENTER)|| e.getCode().equals(KeyCode.SPACE))){
            getIconClickAnimate().setPoint(iconPane.prefWidth(-1)/2, iconPane.prefHeight(-1)/2).play();
            showFocusLine(startField);
            getPopup().setDate(control.getSelectedStartDate()).show(this.control);
        }
    };

    /**
     * 设置外观，显示下拉框
     */
    private ChangeListener<Boolean> fieldPaneListener =  (ob, ov, nv) ->{
        if(startField.isFocused() || endField.isFocused()){
            updateSkin((fieldPane.isHover() || arrow.isHover() || startField.isHover() || endField.isHover())?3:4, 1);
        }else{
            updateSkin((fieldPane.isHover() || arrow.isHover()  || startField.isHover() || endField.isHover())?2: 1, 1);
        }
    };

    /**
     * 设置外观，显示下拉框
     */
    private void startFieldStatusListener(){
        if(startField.isFocused()){
            updateSkin(startField.isHover()?3:4, 1);
//            getPopup().show(this.control);
            showFocusLine(startField);

        }else{
            updateSkin(startField.isHover()?2: 1, 1);

            if(!endField.isFocused()){
                focusLine.setVisible(false);
            }

        }
    };

    /**
     * 设置外观
     */
    private void endFieldStatusListener () {
        if(endField.isFocused()){
            updateSkin(endField.isHover()?3:4, 1);
//            getPopup().show(this.control);
            showFocusLine(endField);

        }else{
            updateSkin(endField.isHover()?2: 1, 1);

            if(!startField.isFocused()){
                focusLine.setVisible(false);
            }
        }
    };

    /**
     * 图标的状态变化监听
     */
    private ChangeListener<Boolean> iconStatusListener = (ob, ov, nv) -> {
        if(iconPane.isFocused()){
            updateSkin(iconPane.isHover()?3:4, 2);
        }else{
            updateSkin(iconPane.isHover()?2: 1, 2);
        }
    };

    /* ------------------------------- method -------------------------- */

    /**
     * 显示focusLine
     * @param currField
     */
    private void showFocusLine(TextField currField) {

        double y = control.prefHeight(-1)-3;
        focusLine.setStartY(y);
        focusLine.setEndY(y);

        Bounds layoutBounds = currField.getLayoutBounds();
        Bounds bounds = currField.localToParent(layoutBounds);
        if(!focusLine.isVisible() || prefFocusField == null){

            double startX = bounds.getMinX(),
                    endX = bounds.getMaxX(),
                    beginX = (endX - startX) / 2;

            focusLine.setStartX(beginX);
            focusLine.setEndX(beginX);

            KeyValue kv11 = new KeyValue(focusLine.startXProperty(), beginX);
            KeyValue kv12 = new KeyValue(focusLine.endXProperty(), beginX);
            KeyValue kv13 = new KeyValue(focusLine.opacityProperty(), 0);

            KeyValue kv21 = new KeyValue(focusLine.startXProperty(), startX);
            KeyValue kv22 = new KeyValue(focusLine.endXProperty(), endX);
            KeyValue kv23 = new KeyValue(focusLine.opacityProperty(), 1);

            focusLine.setVisible(true);
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(0), "kf1", kv11, kv12, kv13),
                    new KeyFrame(Duration.millis(150), "kf2", kv21, kv22, kv23));
            tl.play();
        }else{

            double startX = bounds.getMinX(),
                    endX = bounds.getMaxX();

            KeyValue kv11 = new KeyValue(focusLine.startXProperty(), focusLine.getStartX());
            KeyValue kv12 = new KeyValue(focusLine.endXProperty(), focusLine.getEndX());


            KeyValue kv21 = new KeyValue(focusLine.startXProperty(), startX);
            KeyValue kv22 = new KeyValue(focusLine.endXProperty(), endX);

            Timeline tl = new Timeline(new KeyFrame(Duration.millis(0), "kf1", kv11, kv12),
                    new KeyFrame(Duration.millis(150), "kf2", kv21, kv22));

            tl.play();
        }

        prefFocusField = currField;
    }

    /**
     * 获取点击动画，如果没有初始化，则初始化
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

                double w = this.control.prefWidth(-1),
                    iconPaneWidth = this.iconPane.prefWidth(-1);

                left += w - iconPaneWidth;

//                System.out.println(String.format("left:%f, top:%f, height:%f, width:%f", left, top, iconPane.minWidth(-1), fieldPane.prefHeight(-1) ));
                layoutInArea(node, left, top, iconPaneWidth, iconPane.prefHeight(-1),
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
     * 获取弹窗，如果没有初始化，并初始化
     * @return DateSelectorPopup
     */
    public DateSelectorPopup getPopup(){
        if(popup == null){
            popup = new DateSelectorPopup(control.colorTypeProperty(), control.dateTypeProperty(), true);
            popup.setEnterCallback((popup, date) -> {
                if (prefFocusField != null) {
                    if (prefFocusField == startField) {
                        control.setSelectedStartDate(date);
                        showFocusLine(endField);
                    } else {
                        LocalDateTime startDate = control.getSelectedStartDate();
                        if(startDate.isAfter(date)){
                            control.setSelectedStartDate(date);
                            control.setSelectedEndDate(startDate);
                        }else{
                            control.setSelectedEndDate(date);
                        }

                        popup.hide();
                    }
                }else{
                    control.setSelectedStartDate(date);
                    showFocusLine(endField);
                }
            });
        }
        return popup;
    }

    /**
     * 判断是不是组件里面的内容
     * @param node Node
     * @param index int
     * @return int
     */
    public int isControl(Node node, int index){

        if(node == null || index == 4) return 0;

        if(node.equals(iconPane)){
            return 1;
        }else if(node.equals(arrow)){
            return 1;
        }else if(node.equals(endField)){
            return 1;
        }else if(node.equals(startField)){
            return 1;
        }else if(node.equals(control)){
            return 1;
        }else{
            return isControl(node.getParent(), ++index);
        }
    }

    /**
     * 设置外观颜色
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover|out focus
     * status: 3 : hover|focus状态下的颜色
     * status: 4 : out hover | focus状态的颜色
     * controlIndex: 0: all, 1: text, 2: icon
     */
    private void updateSkin(int status, int controlIndex){
        BorderType borderType = control.getBorderType();
        SkinInfo info = getSkinInfo(status);

        Paint borderOutColor = info.getBorderOutColor(),
                backgroundColor = info.getBackgroundColor(),
                borderColor = info.getInnerBorderColor(),
                fontColor;

        double radii = info.getRadiusWidth(),
                widths1 = info.getOuterBorderWidth(),
                widths2 = info.getInnerBorderWidth();

        Insets insets1 = info.getOuterBorderInsets(),
                insets2 = info.getInnerBorderInsets();

        boolean isArrowFill = control.getFillIcon();

        //构建边框
        Border iconBorder = null
                ,valueBorder = null
                ;
        if(BorderType.FULL.equals(borderType)){
            //外边框,
            BorderStroke stroke1 = new BorderStroke(
                    borderOutColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(radii, 0, 0, radii, false),
                    new BorderWidths(widths1), insets1);
            BorderStroke stroke2 = new BorderStroke(borderOutColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(0, radii, radii, 0, false),
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
                    new CornerRadii(radii, 0, 0, radii, false),
                    new BorderWidths(widths2), insets2);
            BorderStroke stroke21 = new BorderStroke(
                    borderColor, borderColor, borderColor, rightColor,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,
                    new CornerRadii(0, radii, radii, 0, false),
                    new BorderWidths(widths2, widths2, widths2, 0), insets2);

            valueBorder = new Border(stroke1, stroke11);
            iconBorder = new Border(stroke2, stroke21);
        }

        if(!isArrowFill){
            backgroundColor = Color.TRANSPARENT;
            fontColor = borderColor;
        }else{
            fontColor = Color.WHITE;
            backgroundColor = control.getColorType().getPaint();
        }

        //构建背景色背景色
        BackgroundFill fill2 = new BackgroundFill(backgroundColor, new CornerRadii(0, radii, radii, 0, false),
                new Insets(0,0,0,0));
        Background background2 = new Background(fill2);

        //设置
        if(controlIndex == 1){
            //设置
            fieldPane.setBorder(valueBorder);
            control.setPadding(info.getPadding());
            Font font = Font.font(startField.getFont().getFamily(), info.getFontSize());
            startField.setFont(font);
            endField.setFont(font);
        }else if(controlIndex == 2){
            setArrowBtnColor(background2, iconBorder, fontColor);
        }else{
            fieldPane.setBorder(valueBorder);
            control.setPadding(info.getPadding());

            Font font = Font.font(startField.getFont().getFamily(), info.getFontSize());
            startField.setFont(font);
            endField.setFont(font);
            icon.setSize(info.getFontSize()-2);
            setArrowBtnColor(background2, iconBorder, fontColor);
        }
    }

    /**
     * 获取外观样式信息
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
     * 设置下拉按钮图标样式
     */
    private void setArrowBtnColor(Background bg, Border border, Paint fontColor){
        iconPane.setBackground(bg);
        iconPane.setBorder(border);
        icon.setColor(fontColor);
    }

    /* --------------------------- overvide -------------------------------------- */

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {

        double width = this.control.getPrefWidth();
        if(width<=0){
            // leftInset + startField(120) + space(10) + arrow + space(10) + endField(120) + rightInset
            width = 120*2 + arrow.prefWidth(-1)+20+control.prefHeight(-1)+leftInset+rightInset;
        }

        Insets padding = control.getPadding();
        if(padding!=null){
            width += padding.getLeft() + padding.getRight();
        }
        return width;
    }


    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {

        double height = startField.prefHeight(-1);
        Insets padding = control.getPadding();
        if(padding!=null){
            height += padding.getTop() + padding.getBottom();
        }

//        height += topInset + bottomInset + 1;

        return height;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

//        System.out.println(contentHeight+"," + this.control.prefHeight(-1));

        Insets padding = this.control.getPadding();
        if(padding == null){
            padding = Insets.EMPTY;
        }

        double width = this.control.prefWidth(-1),
                height = this.control.prefHeight(-1),
                iconPaneH = height,
                fieldPaneW = width - iconPaneH,
                fieldPaneH = height,
                fieldPaneX = 0,
                fieldPaneY = 0,
                arrowW = arrow.prefWidth(-1),
                arrowH = arrow.prefHeight(-1),
                fieldW = (width - arrowW - 20 - height - padding.getLeft() - padding.getRight()) / 2,
                fieldH = startField.prefWidth(-1),
                startFieldX = contentX,
                startFieldY = (height - fieldH) / 2,
                arrowX = startFieldX + fieldW + 10,
                arrowY = (height - arrowH) / 2,
                endFieldX = arrowX + arrowW + 10,
                endFieldY = startFieldY,
                iconPaneX = endFieldX + fieldW + padding.getRight(),
                iconPaneY = 0,
                iconW = icon.prefWidth(-1),
                iconH = icon.prefHeight(-1),
                iconX = (height - iconW) / 2,
                iconY = (iconPaneH - iconH) / 2;

        startField.setPrefWidth(fieldW);
        startField.setMaxWidth(fieldW);
        endField.setPrefWidth(fieldW);
        endField.setMaxWidth(fieldW);
        iconPane.setPrefWidth(height);
        iconPane.setPrefHeight(iconPaneH);
        icon.setLayoutX(iconX);
        icon.setLayoutY(iconY);

//        System.out.println(String.format("iconPane(w:%f, h:%f, x:%f, y:%f)\r\n" +
//                "startField(w:%f, h:%f, x:%f, y:%f)\n" +
//                "arrow(w:%f, h:%f, x:%f, y:%f)\n" +
//                "endField(w:%f, h:%f, x:%f, y:%f)\n" +
//                "iconPane(w:%f, h:%f, x:%f, y:%f)\n" +
//                "icon(w:%f, h:%f, x:%f, y:%f)\r\n\r\n",
//                height, iconPaneH, iconPaneX, iconPaneY,
//                fieldW, fieldH, startFieldX, startFieldY,
//                arrowW, arrowH, arrowX, arrowY,
//                fieldW, fieldH, endFieldX, endFieldY,
//                height, iconPaneH, iconPaneX, iconPaneY,
//                iconW, iconH, iconX, iconY));

        layoutInArea(fieldPane, fieldPaneX, fieldPaneY, fieldPaneW, fieldPaneH, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(startField, startFieldX, startFieldY, fieldW, fieldH, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(arrow, arrowX, arrowY, arrowW, arrowH, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(endField, endFieldX, endFieldY, fieldW, fieldH, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(iconPane, iconPaneX, iconPaneY, iconPaneH, iconPaneH, 0, HPos.CENTER, VPos.CENTER);

    }

    @Override
    public void dispose() {
        super.dispose();

        startField.removeEventFilter(KeyEvent.KEY_RELEASED, startFieldKeyReleaseHandler);
        endField.removeEventFilter(KeyEvent.KEY_RELEASED, endFieldKeyReleaseHandler);

        iconPane.focusedProperty().removeListener(iconStatusListener);
        iconPane.hoverProperty().removeListener(iconStatusListener);
        iconPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);
        iconPane.removeEventFilter(KeyEvent.KEY_RELEASED, iconPaneKeyReleaseHandler);

        //监听关闭弹窗
        this.control.getScene().getWindow().focusedProperty().removeListener(windowFocusListener);
        this.control.getScene().removeEventFilter(MouseEvent.MOUSE_PRESSED, windowClickFilter);
        this.fieldPane.hoverProperty().removeListener(fieldPaneListener);

        this.control.selectedStartDateProperty().removeListener(selectedStartDateListener);
        this.control.selectedEndDateProperty().addListener(selectedEndDateListener);

        unregisterChangeListeners(startField.focusedProperty());
        unregisterChangeListeners(startField.hoverProperty());
        unregisterChangeListeners(endField.focusedProperty());
        unregisterChangeListeners(endField.hoverProperty());
        unregisterChangeListeners(control.hueTypeProperty());
        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.roundTypeProperty());
        unregisterChangeListeners(control.fillIconProperty());

    }
}
