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
package com.xm2013.jfx.control.textfield;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.BorderType;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.svg.XmSVGView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

import static com.xm2013.jfx.common.FxKit.CLEAN_PATH;

public class XmTextFieldSkin extends SkinBase<XmTextField> {
    private static String ENABLE_PATH = "M512 398.336c-75.776 0-137.216 61.44-137.216 137.216s61.44 " +
            "137.216 137.216 137.216 137.216-61.44 137.216-137.216-61.44-137.216-137.216-137.216z m0 " +
            "205.824c-37.888 0-68.608-30.72-68.608-68.608s30.72-68.608 68.608-68.608 68.608 30.72 68.608 " +
            "68.608c0.512 37.376-30.72 68.608-68.608 68.608z m0-369.664c-278.016 0-479.744 179.712-479.744 " +
            "300.544 0 118.784 215.04 300.544 479.744 300.544 262.144 0 474.624-181.76 474.624-300.544S774.144 " +
            "234.496 512 234.496z m0 532.992c-237.568 0-411.136-162.816-411.136-232.448 0-79.36 173.568-232.448 " +
            "411.136-232.448 235.008 0 406.528 162.816 406.528 232.448s-171.52 232.448-406.528 232.448z";
    private static String DISABLE_PATH = "M989.184 533.504c0 52.736-42.496 118.272-114.176 175.104-6.144 " +
            "5.12-13.824 7.68-21.504 7.68-10.24 0-19.968-4.608-26.624-12.8-11.776-14.848-9.216-36.352 " +
            "5.12-48.128 60.928-48.64 88.576-97.28 88.576-121.856 0-69.12-171.52-232.448-406.528-232.448-26.624 " +
            "0-53.248 2.048-79.872 5.632-18.432 3.072-35.84-10.24-38.912-28.672-2.56-18.432 10.24-35.84 " +
            "28.672-38.912 29.696-4.608 59.904-6.656 89.6-6.656 263.168 0 475.648 182.272 475.648 301.056z " +
            "m-136.192 272.896c13.312 13.312 13.312 34.816 0 48.128-6.656 6.656-15.36 10.24-24.064 10.24-8.704 " +
            "0-17.408-3.584-24.064-10.24l-66.56-66.56c-71.168 29.696-147.968 45.568-223.744 45.568-264.704 " +
            "0-479.744-181.76-479.744-300.544 0-82.432 86.016-178.176 209.92-238.592L175.616 225.28a33.9968 " +
            "33.9968 0 0 1 0-48.128c13.312-13.312 34.816-13.312 48.128 0l104.96 104.96 524.288 524.288zM445.44 " +
            "533.504c0 37.888 30.72 68.608 68.608 68.608 10.752 0 20.992-2.56 30.72-7.168l-92.16-92.16c-4.608 " +
            "9.216-7.168 19.456-7.168 30.72z m240.64 202.24l-91.136-91.136c-23.04 16.896-50.688 26.112-80.384 " +
            "26.112-75.776 0-137.216-61.44-137.216-137.216 0-29.184 9.728-56.832 26.112-79.872L296.96 346.624c" +
            "-119.808 52.224-193.536 135.168-193.536 186.88 0 69.12 173.568 232.448 411.136 232.448 57.856-0.512" +
            " 116.224-10.752 171.52-30.208z";
    private XmTextField control;

    /**
     * 文本框的标签
     */
    private XmLabel label;
    /**
     * 后缀图标的容器，clear, suffix
     */
    private HBox suffixPane;
    /**
     * 清除图标按钮
     */
    private XmSVGView cleanIcon;

    /**
     * 显示密码按钮
     */
    private XmSVGView showPwdIcon;
    private XmSVGView enablePwdIcon;
    private Pane showPwdIconPane;

    //背景
    private Pane fieldBackground;

    //VERTICAL_INLINE状态下，获得/失去焦点后，label变化的动画
    private Timeline labelTimeline = new Timeline();

    //是否需要需要更新布局
    private boolean layoutUpdated = false;
    //VERTICAL_INLINE状态下， label当前是否放大
    private boolean labelZoomIn = false;
    private boolean labelAnimatePlaying = false;

    private double paddingLeft = 0, paddingRight = 0, paddingTop = 0, paddingBottom = 0,
        hgap = 6, vgap = 6;

    public XmTextFieldSkin(XmTextField control) {
        super(control);
        this.control = control;

        if(control.getInputType().equals(XmTextInputType.PASSWORD)){
            if(control.getField() instanceof PasswordField == false){
                InnerPasswordField field = new InnerPasswordField();
                field.showPasswordProperty().bind(control.showPasswordProperty());
                field.echocharProperty().bind(control.echocharProperty());
                field.promptTextProperty().bind(control.placeHolderProperty());

                String text = control.getText();
                control.setField(field);
                control.setText(text);
            }
        }

//        ObservableList<Node> children = this.getChildren();
        //构建标签
        label = new XmLabel();
        label.setAlignment(control.getAlignment());

        label.getStyleClass().add("field-label");
        label.textProperty().bind(control.labelProperty());
        label.prefWidthProperty().bind(control.labelWidthProperty());
        label.graphicProperty().bind(control.labelIconProperty());

        control.setPadding(control.getMargin());
        control.paddingProperty().bind(this.control.marginProperty());

        //构建后缀
        suffixPane = new HBox();
        suffixPane.setSpacing(3);
        suffixPane.getStyleClass().add("field-suffix-pane");

        //是否删除的文本按钮
        cleanIcon=new XmSVGView(CLEAN_PATH);
        cleanIcon.getStyleClass().add("field-clean");
        cleanIcon.setSize(14);
        cleanIcon.setColor(Color.web(ColorType.DANGER));
        cleanIcon.setOpacity(0);

        showPwdIcon = new XmSVGView();
        showPwdIcon.getStyleClass().add("field-show-pwd");
        showPwdIcon.setSize(14);
        showPwdIcon.setColor(Color.web(ColorType.SECONDARY));
        showPwdIcon.setContent(DISABLE_PATH);

        enablePwdIcon = new XmSVGView();
        enablePwdIcon.getStyleClass().add("field-show-pwd");
        enablePwdIcon.setSize(14);
        enablePwdIcon.setColor(Color.web(ColorType.SECONDARY));
        enablePwdIcon.setContent(ENABLE_PATH);

        showPwdIconPane = new Pane();
        showPwdIconPane.setOpacity(0);
        if(control.isShowPassword())
        {
            showPwdIconPane.getChildren().setAll(enablePwdIcon);
        }else {
            showPwdIconPane.getChildren().setAll(showPwdIcon);
        }
        //背景
        fieldBackground = new Pane();
        fieldBackground.getStyleClass().add("field-background");

        //构建属性
        setFocusTraversable();
        updateChildren();
        updateColor(1);
        updateStyle();

        //添加事件，监听属性
        cleanIcon.addEventFilter(MouseEvent.MOUSE_CLICKED, clearEvent);
        cleanIcon.addEventFilter(MouseEvent.MOUSE_PRESSED, clearPressEvent);
        cleanIcon.addEventFilter(MouseEvent.MOUSE_RELEASED, clearReleaseEvent);

        showPwdIconPane.addEventFilter(MouseEvent.MOUSE_CLICKED, showPwdEvent);
        
        control.addEventFilter(MouseEvent.MOUSE_CLICKED, controlClickEvent);
        control.hoverProperty().addListener(hoverListener);
        control.getField().focusedProperty().addListener(focusListener);

        registerChangeListener(control.colorTypeProperty(), e -> {
            updateColor(1);
        });

        registerChangeListener(control.sizeTypeProperty(), e -> {
            updateStyle();
            layoutUpdated = false;
        });

        registerChangeListener(control.roundTypeProperty(), e -> {
            layoutUpdated = false;
            updateColor(1);
        });
        registerChangeListener(control.displayTypeProperty(), e->{
            layoutUpdated = false;
            updateStyle();
        });
        registerChangeListener(control.borderTypeProperty(), e->layoutUpdated = false);
        registerChangeListener(control.labelAlignmentProperty(), e-> layoutUpdated = false);
        registerChangeListener(control.marginProperty(), e->layoutUpdated = false);
        registerChangeListener(control.cleanableProperty(), e->{
            updateChildren();
            layoutUpdated = false;
        });

        //设置事件监听
        control.inputTypeProperty().addListener(inputTypeChangeListener);

        if(control.isDisable()){
            control.setOpacity(0.65);
        }

        control.disabledProperty().addListener(disableListener);
    }

    /*------------------------ Listener/Event -----------------------------*/
    /**
     * 文本框变化监听事件
     */
    private ChangeListener<XmTextInputType> inputTypeChangeListener = (ob, ov, nv) -> {
        String text = control.getField().getText();

        if(ov.equals(XmTextInputType.PASSWORD) && control.getField() instanceof InnerPasswordField){
            InnerPasswordField field = (InnerPasswordField) control.getField();
            field.showPasswordProperty().unbind();
            field.echocharProperty().unbind();
            field.promptTextProperty().unbind();
        }

        if(nv.equals(XmTextInputType.PASSWORD)){
            InnerPasswordField field = new InnerPasswordField();

            field.showPasswordProperty().bind(control.showPasswordProperty());
            field.echocharProperty().bind(control.echocharProperty());

            control.setField(field);
        }else{
            control.setField(new InnerTextField());
        }

        control.getField().setText(text);
        control.getField().promptTextProperty().bind(control.placeHolderProperty());
        updateChildren();
        layoutUpdated = false;
    };

    private EventHandler<MouseEvent> showPwdEvent = e -> {

        control.setShowPassword(!control.isShowPassword());

        if(control.isShowPassword()){
            showPwdIconPane.getChildren().setAll(enablePwdIcon);
        }else {
            showPwdIconPane.getChildren().setAll(showPwdIcon);
        }

        String t = control.getField().getText();
//        control.getField().setText(t.substring(0, t.length()-1));
        control.getField().setText(t);
        control.getField().end();
    };

    /**
     * 点击清除图标，清除文本框内容
     */
    private EventHandler<MouseEvent> clearEvent = e -> {
        this.control.getField().clear();
    };

    /**
     * 在清除图标上面，鼠标按下事件
     */
    private EventHandler<MouseEvent> clearPressEvent = e->{
        cleanIcon.setOpacity(1);
    };

    /**
     * 在清除图标上面，鼠标松开事件
     */
    private EventHandler<MouseEvent> clearReleaseEvent = e -> {
        if(control.isHover()){
            cleanIcon.setOpacity(0.8);
            showPwdIcon.setOpacity(0.8);
        }else{
            cleanIcon.setOpacity(0);
            showPwdIcon.setOpacity(0);
        }
    };

    /**
     * 点击组件事件， VERTICAL_INLINE状态下， label收缩
     */
    private EventHandler<MouseEvent> controlClickEvent = e->{
        control.getField().requestFocus();
        String text = control.getField().getText();
        if(labelTimeline!=null && (text == null || "".equals(text)) && !labelZoomIn && labelAnimatePlaying== false){
            labelTimeline.setRate(1);
            labelTimeline.playFrom(Duration.millis(0));
            labelZoomIn = true;
        }
    };

    /**
     * 监听hover属性，显示/隐藏 清除图标， 组件颜色变换
     */
    private ChangeListener<Boolean> hoverListener = (ob, ov, nv) -> {
        boolean isShowPwd = XmTextInputType.PASSWORD.equals(control.getInputType());
        if(nv){
            if(control.isCleanable()){
                cleanIcon.setOpacity(0.8);
            }

            if(isShowPwd){
                showPwdIconPane.setOpacity(0.8);
            }

            if(control.isFocused()){
                updateColor(2);
            }else{
                updateColor(3);
            }
        }else{
            if(control.isFocused()){
                updateColor(1);
            }else{
                updateColor(4);
            }
            if(control.isCleanable()){
                cleanIcon.setOpacity(0);
            }

            if(isShowPwd){
                showPwdIconPane.setOpacity(0);
            }

        }
    };

    /**
     * 获得/失去
     * VERTICAL_INLINE状态下，焦点 label的变化，
     * 组件颜色变换
     */
    private ChangeListener<Boolean> focusListener = (ob, ov, nv) -> {

        String text = control.getField().getText();
        if(nv){
            if(labelTimeline!=null && (text == null || "".equals(text))){
                labelTimeline.setRate(1);
                labelTimeline.playFrom(Duration.millis(0));
                labelAnimatePlaying = true;
                labelZoomIn = true;
            }
            updateColor(3);
        }else{
            if(labelTimeline !=null && (text == null || "".equals(text))){
                labelTimeline.setRate(-1);
                labelTimeline.playFrom(Duration.millis(150));
                labelAnimatePlaying = true;
                labelZoomIn = false;
            }
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
    private ChangeListener<Boolean> disableListener = (ob, ov, nv) -> {
        if(nv){
            control.setOpacity(0.65);
        }else{
            control.setOpacity(1);
        }
    };

    /* --------------------------- override ----------------------------*/

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {

        XmFieldDisplayType type = control.getDisplayType();

        boolean isHorizontal = XmFieldDisplayType.HORIZONTAL_INLINE.equals(type)
                || XmFieldDisplayType.HORIZONTAL_OUTLINE.equals(type);

        double width = control.getField().prefWidth(-1);
        width += suffixPane.prefWidth(-1);
        if(control.getPrefix()!=null){
            width += control.getPrefix().prefWidth(-1);
        }

        if(isHorizontal){
            width += label.prefWidth(-1);
        }

        if(XmFieldDisplayType.HORIZONTAL_OUTLINE.equals(type)){
            width += hgap;
        }
        width = width+leftInset+rightInset + paddingLeft + paddingRight;
        return width;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        XmFieldDisplayType type = control.getDisplayType();

        boolean isHorizontal = XmFieldDisplayType.HORIZONTAL_INLINE.equals(type)
                || XmFieldDisplayType.HORIZONTAL_OUTLINE.equals(type);

        double height = 0;

        double prevHeight = 0;
        if(control.getPrefix() != null){
            prevHeight = control.getPrefix().prefHeight(-1);
        }

        double fieldHeight = control.getField().prefHeight(-1);
        height = Math.max(fieldHeight, prevHeight);

        double suffixHeight = suffixPane.prefHeight(-1);
        height = Math.max(suffixHeight, height);

        double labelHeight = label.prefHeight(-1);
        if(isHorizontal){
            height = Math.max(height, labelHeight);
        }else{
            height += labelHeight;
            if(XmFieldDisplayType.VERTICAL_OUTLINE.equals(type)){
                height += vgap;
            }
        }

        height = height+topInset+bottomInset+paddingTop+paddingBottom;

        return height;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
        XmFieldDisplayType type = control.getDisplayType();
        Node prefix = control.getPrefix();
        TextField field = control.getField();

        double marginTop=0, marginRight=0, marginBottom=0, marginLeft=0,
                height = this.control.prefHeight(-1),
                width = this.control.prefWidth(-1),
                labelX=0, labelY=0, labelWidth = label.prefWidth(-1), labelHeight = label.prefHeight(-1),
                preX = 0, preY = 0, preWidth = 0, preHeight=0,
                fieldX = 0, fieldY = 0, fieldWidth = field.prefWidth(-1), fieldHeight = field.prefHeight(-1),
                suffixX = 0, suffixY = 0, suffixWidth = suffixPane.prefWidth(-1), suffixHeight=suffixPane.prefHeight(-1);

        if(prefix!=null){
            preWidth = prefix.prefWidth(-1);
            preHeight = prefix.prefHeight(-1);
        }

        Insets margin = this.control.getPadding();
        if(margin!=null){
            marginTop = margin.getTop();
            marginRight = margin.getRight();
            marginBottom = margin.getBottom();
            marginLeft = margin.getLeft();
        }

        boolean isHorizontal = XmFieldDisplayType.HORIZONTAL_INLINE.equals(type)
                || XmFieldDisplayType.HORIZONTAL_OUTLINE.equals(type);

        if(isHorizontal){
            labelX = marginLeft+paddingLeft;
            labelY =  (height - labelHeight) / 2;
//            System.out.println(String.format("height=%f, labelHeight=%f, (height-labelHeight)/2=%s", height, labelHeight, labelY));

            preX = labelX + labelWidth;


            fieldWidth = width - marginLeft - marginRight - labelWidth - preWidth - suffixWidth - paddingLeft - paddingRight;
            if(XmFieldDisplayType.HORIZONTAL_OUTLINE.equals(type)){
                preX += hgap;
                fieldWidth -= hgap;
            }

            if(prefix != null){
                preY = (height - preHeight) / 2;
            }

            fieldX = preX + preWidth;
            fieldY = (height - fieldHeight) / 2;

            suffixX = width - suffixWidth - marginRight - paddingRight;
            suffixY = (height - suffixHeight) / 2;



        }else{

            fieldWidth = width - preWidth - suffixWidth - marginLeft - marginRight - paddingRight - paddingLeft;

            if(XmFieldDisplayType.VERTICAL_INLINE.equals(type)){
                preX = marginLeft+paddingLeft;
                if(prefix != null){
                    preY = (height - preHeight) / 2;
                }

                double totalHeight = labelHeight + fieldHeight;

                labelX = preX + preWidth + 10;
                labelY = marginTop + paddingTop;

                fieldWidth -= 10;

                double startY = labelY + labelHeight;
//                double balanceHeight = Math.max(suffixHeight, Math.max(preHeight, fieldHeight));

                fieldX = preX+preWidth;
                fieldY = startY + 4;

                suffixX = width - suffixWidth - marginRight - paddingRight;
                suffixY = (height - suffixHeight) / 2;
            }else{
                labelX = marginLeft + paddingLeft;
                labelY = marginTop + paddingTop;

                double startY = labelY + labelHeight - vgap/2 - 1;
                double balanceHeight = height - startY;

                preX = marginLeft+paddingLeft;
                if(prefix != null){
                    preY = startY + (balanceHeight - preHeight) / 2;
//                    preY = height - marginBottom - paddingBottom - preHeight;
                }

                fieldX = preX + preWidth;
                fieldY = startY + (balanceHeight - fieldHeight) / 2 + 4;
//                fieldY = height - marginBottom - paddingBottom - fieldHeight;

                suffixX = width - suffixWidth - marginRight - paddingRight;
                suffixY = startY + (balanceHeight - suffixHeight) / 2;
//                suffixY = height - marginBottom - paddingBottom - suffixHeight;
            }
        }

        layoutInArea(label, labelX, labelY, labelWidth, labelHeight, 0, HPos.CENTER, VPos.CENTER);
        if(prefix != null){
            layoutInArea(prefix, preX, preY, preWidth, preHeight, 0, HPos.CENTER, VPos.CENTER);
        }

//        field.setPrefWidth(fieldWidth);
//        field.setPrefHeight(fieldHeight);
        layoutInArea(field, fieldX, fieldY, fieldWidth, fieldHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(suffixPane, suffixX, suffixY, suffixWidth, suffixHeight, 0, HPos.CENTER, VPos.CENTER);

        if(!layoutUpdated){
            updateLayout(width, height, labelWidth, labelHeight, marginTop, marginRight, marginBottom, marginLeft);
        }

    }

    @Override
    public void dispose() {
        super.dispose();

        cleanIcon.removeEventFilter(MouseEvent.MOUSE_CLICKED, clearEvent);
        cleanIcon.removeEventFilter(MouseEvent.MOUSE_PRESSED, clearPressEvent);
        cleanIcon.removeEventFilter(MouseEvent.MOUSE_RELEASED, clearReleaseEvent);
        control.removeEventFilter(MouseEvent.MOUSE_CLICKED, controlClickEvent);
        control.hoverProperty().removeListener(hoverListener);
        control.getField().focusedProperty().removeListener(focusListener);
        control.disabledProperty().removeListener(disableListener);

        label.textProperty().unbind();
        label.prefWidthProperty().unbind();
        label.graphicProperty().unbind();

        showPwdIconPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, showPwdEvent);
        control.inputTypeProperty().removeListener(inputTypeChangeListener);

        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.roundTypeProperty());
        unregisterChangeListeners(control.displayTypeProperty());
        unregisterChangeListeners(control.borderTypeProperty());
        unregisterChangeListeners(control.labelAlignmentProperty());
        unregisterChangeListeners(control.marginProperty());
        unregisterChangeListeners(control.cleanableProperty());
    }

    /* -------------------------- method -----------------------------*/

    /**
     * 更新节点
     */
    public void updateChildren(){
        suffixPane.getChildren().clear();
        this.getChildren().clear();

        this.getChildren().setAll(control.getField(), suffixPane,label);
        if(control.isCleanable()){
            suffixPane.getChildren().add(0, cleanIcon);
        }

        if(XmTextInputType.PASSWORD.equals(control.getInputType())){
            suffixPane.getChildren().add(0, showPwdIconPane);
        }

        if(control.getSuffix() != null){
            suffixPane.getChildren().add(control.getSuffix());
        }

        if(control.getPrefix() != null){
            this.getChildren().add(1, control.getPrefix());
        }

        this.getChildren().add(0, fieldBackground);
    }

    /**
     * 更新颜色
     * 1: 正常
     * 2: hover
     * 3: focus
     * 4: outhover-focus
     * @param status int
     */
    public void updateColor(int status){

        Paint paint = control.getColorType().getPaint();

        Paint borderColor = null, outColor = null;
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
            outColor = Color.TRANSPARENT;
            borderColor = Color.web("#999999");
        }

        BorderType borderType = control.getBorderType();
        RoundType roundType = control.getRoundType();
        Insets insets1, insets2;
        double widths1, widths2, round = 0;
        Paint topColor = outColor, rightColor = outColor, bottomColor = outColor, leftColor=outColor,
                topBorderColor = borderColor, rightBorderColor = borderColor, bottomBorderColor = borderColor, leftBorderColor = borderColor;
        if(BorderType.BOTTOM.equals(borderType)){
            insets1 = new Insets(0,0,0,0);
            insets2 = new Insets(0,0,-3.5,0);
            widths1 = 2;
            widths2 = 3.5;
            topColor = rightColor = leftColor = topBorderColor = rightBorderColor = leftBorderColor = Color.TRANSPARENT;
        }else {
            insets1 = new Insets(0);
            insets2 = new Insets(-3.5);
            widths1 = 0.75;
            widths2 = 3.5;

            if(RoundType.SMALL.equals(roundType)){
                round = 4;
            }else if(RoundType.SEMICIRCLE.equals(roundType)){
                round = 30;
            }else if(RoundType.CIRCLE.equals(roundType)){
                round = 30;
            }

        }

        BorderStroke stroke1 = new BorderStroke(topBorderColor, rightBorderColor, bottomBorderColor, leftBorderColor,
                BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,
                new CornerRadii(round), new BorderWidths(widths1), insets1);

        BorderStroke stroke2 = new BorderStroke(topColor, rightColor, bottomColor, leftColor,
                BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,BorderStrokeStyle.SOLID ,
                new CornerRadii(round), new BorderWidths(widths2), insets2);

        Border border = new Border(stroke1, stroke2);

//        this.control.setBorder(border);
        this.fieldBackground.setBorder(border);
    }

    /**
     * 更新边距，字体大小， 背景色
     */
    private void updateStyle(){

        SizeType sizeType = this.control.getSizeType();
        double fontSize = 0;
        double inputSize = 12;
        if(XmFieldDisplayType.VERTICAL_INLINE.equals(control.getDisplayType())){
            if(SizeType.SMALL.equals(sizeType)){
                fontSize = 12;
                inputSize = 12;
                paddingTop = 0;
                paddingRight = 5;
                paddingBottom = 0;
                paddingLeft = 5;
            }else if(SizeType.LARGE.equals(sizeType)){
                fontSize = 16;
                inputSize = 15;
                paddingTop = 6;
                paddingRight = 10;
                paddingBottom = 6;
                paddingLeft = 10;
            }else{
                fontSize = 14;
                inputSize =  14;
                paddingTop = 3;
                paddingRight = 10;
                paddingBottom = 3;
                paddingLeft = 10;
            }
        }else{
            if(SizeType.SMALL.equals(sizeType)){
                fontSize = 12;
//            padding = new Insets(3, 5, 3,5);
                inputSize = 12;
                paddingTop = 0;
                paddingRight = 5;
                paddingBottom = 0;
                paddingLeft = 5;
            }else if(SizeType.LARGE.equals(sizeType)){
                fontSize = 16;
                inputSize = 15;
//            padding = new Insets(9, 10, 9, 10);
                paddingTop = 10;
                paddingRight = 10;
                paddingBottom = 10;
                paddingLeft = 10;
            }else{
                fontSize = 14;
                inputSize =  14;
                paddingTop = 7;
                paddingRight = 10;
                paddingBottom = 7;
                paddingLeft = 10;
//            padding = new Insets(6, 10, 6, 10);
            }
        }


//        this.control.setPadding(padding);
        this.control.getField().setFont(Font.font(this.control.getField().getFont().getFamily(), inputSize));
        label.setFont(Font.font(label.getFont().getFamily(), fontSize));
    }

    /**
     * 更新布局
     */
    private void updateLayout(double width, double height, double labelWidth, double labelHeight,
                              double marginTop, double marginRight, double marginBottom, double marginLeft){

        XmFieldDisplayType type = control.getDisplayType();
//        boolean isHorizontal = XmFieldDisplayType.VERTICAL_INLINE.equals(type)
//                || XmFieldDisplayType.VERTICAL_OUTLINE.equals(type);

        if(XmFieldDisplayType.VERTICAL_INLINE.equals(type)){
            //更新标签位置
            double scaleX = 1.3, scaleY = 1.3,
                    x = 10, y = (height - labelHeight*2)/2;
            ;

            label.setScaleX(scaleX);
            label.setScaleY(scaleY);
            label.setTranslateX(x);
            label.setTranslateY(y);
            label.setFont(Font.font(label.getFont().getFamily(), 11));

            labelTimeline = new Timeline(new KeyFrame(Duration.millis(0),
                    new KeyValue(label.scaleXProperty(), scaleX),
                    new KeyValue(label.scaleYProperty(), scaleY),
                    new KeyValue(label.translateXProperty(), x),
                    new KeyValue(label.translateYProperty(), y)
            ),
                    new KeyFrame(Duration.millis(150),
                            new KeyValue(label.scaleXProperty(), 1),
                            new KeyValue(label.scaleYProperty(), 1),
                            new KeyValue(label.translateXProperty(), 0),
                            new KeyValue(label.translateYProperty(), 3)
                    )
            );

            labelTimeline.setOnFinished(e -> {
                labelAnimatePlaying = false;
            });

            labelZoomIn = false;
        }else{
            label.setScaleX(1);
            label.setScaleY(1);
            label.setTranslateY(0);
            label.setTranslateY(0);
            labelTimeline = null;
        }

        //更新背景宽高, 位置
        double bgWidth = width, bgHeight = height, x=0, y=0;
        if(XmFieldDisplayType.HORIZONTAL_INLINE.equals(type)){
            x = marginLeft;
            y = marginTop;
            bgWidth = width - marginLeft - marginRight;
            bgHeight = height - marginTop - marginBottom;
        }else if(XmFieldDisplayType.HORIZONTAL_OUTLINE.equals(type)){
            x = marginLeft + paddingLeft + labelWidth  ;
            y = marginTop;
            bgWidth = width - x - marginRight;
            bgHeight = height - marginTop - marginBottom;
        }else if(XmFieldDisplayType.VERTICAL_INLINE.equals(type)){
            x = marginLeft;
            y = marginTop;
            bgWidth = width - marginLeft - marginRight;
            bgHeight = height - marginTop - marginBottom;
        }else if(XmFieldDisplayType.VERTICAL_OUTLINE.equals(type)){
            x = marginLeft;
            y = marginTop + paddingTop + labelHeight + vgap / 2;
            bgHeight = height - y - marginBottom;
            bgWidth = width - marginLeft - marginRight;
        }

        this.fieldBackground.setPrefWidth(bgWidth);
        this.fieldBackground.setPrefHeight(bgHeight);

        layoutInArea(fieldBackground, x, y, bgWidth, bgHeight, 0, HPos.CENTER, VPos.CENTER);

        layoutUpdated = true;
    }

    /**
     * 设置tab键获取焦点
     */
    private void setFocusTraversable() {
        control.setFocusTraversable(false);
        label.setFocusTraversable(false);
        suffixPane.setFocusTraversable(false);
        cleanIcon.setFocusTraversable(false);
        fieldBackground.setFocusTraversable(false);
        control.getField().setFocusTraversable(true);
        if(control.getPrefix()!=null){
            control.getPrefix().setFocusTraversable(false);
        }

        if(control.getSuffix()!=null){
            control.getSuffix().setFocusTraversable(false);
        }

    }
}
