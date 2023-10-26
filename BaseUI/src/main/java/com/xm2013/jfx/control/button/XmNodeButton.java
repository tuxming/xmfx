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
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ClickShadowAnimate;
import com.xm2013.jfx.control.animate.ResetNodePositionCallback;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.css.*;
import javafx.css.converter.EnumConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.w3c.dom.events.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 这个按钮只支持一个节点，如果想要节点根据按钮状态一起变化，请使用XmIcon, String
 */
public class XmNodeButton extends Region {
    private Node node;
    private Object value;

    /**
     * 选中状态， 颜色填充
     */
    private static PseudoClass FILLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("filled");

    /**
     * 激活状态， 描边状态
     */
    private static PseudoClass ACTIVE_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("active");

    /**
     * outside状态, 外观呈现disable状态，但是响应时间
     */
    private static PseudoClass OUTSIDE_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("outside");

    private ObjectProperty<Paint> backgroundColor = new SimpleObjectProperty<>(null),
            borderColor =  new SimpleObjectProperty<>(),
            textColor = new SimpleObjectProperty<>();

    private ClickAnimate clickAnimate;

    public XmNodeButton(String text){
        this(new Text(text));
    }

    public XmNodeButton(Node node){
        this.node = node;
        
        if(node instanceof Text){
            setText(((Text) node).getText());
            ((Text) node).textProperty().bind(textProperty());
        }

        this.getStyleClass().add("node-button");
        this.getChildren().add(node);
        this.setPadding(new Insets(4));

        setStatus();

        activeProperty().addListener(statusChangeListener);
        hoverProperty().addListener(statusChangeListener);
        filledProperty().addListener(statusChangeListener);
        outsideProperty().addListener(statusChangeListener);
        this.setOnMouseClicked(e -> {
            if (!isDisabled()) {
                getClickAnimate().setPoint(e.getX(), e.getY()).play();
                fireEvent(new ActionEvent());
            }
        });

        colorTypeProperty().addListener(statusChangeListener);

    }

    private ChangeListener<Object> statusChangeListener = (ob, ov, nv) ->{
        setStatus();
    };

    private void setStatus(){
        if(isHover()){
            if(isOutside()){
                updateSkin(6);
            }else if(isFilled()){
                updateSkin(4);
            }else if(isActive()){
                updateSkin(1);
            }else{
                updateSkin(2);
            }
        }else{
            if(isOutside()){
                updateSkin(5);
            }else if(isFilled()){
                updateSkin(4);
            }else if(isActive()){
                updateSkin(3);
            }else {
                updateSkin(0);
            }
        }
    }

    /**
     * 设置外观颜色
     * @param status
     * 0: default
     * 1: active
     * 2: hover
     * 3: active-hover
     * 4: filled
     * 5: outside
     * 6: outside-hover
     */
    private void updateSkin(int status) {

        Paint paint = getColorType().getPaint(),
            textColor, borderColor, backgroundColor;
        if(status == 1){ // 1: active
            //0: 默认，改变背景色 1: 提高文字亮度  2：显示为主题色
            textColor = getTextStyle() == 1 ? paint : Color.web("#333333");
            borderColor = paint;
            backgroundColor = Color.TRANSPARENT;
        }else if(status == 2){ //2: hover
            //0: 默认，改变背景色 1: 提高文字亮度  2：显示为主题色
            int hoverStyle1 = getHoverStyle();
            int textStyle1 = getTextStyle();
            if(hoverStyle1 == 1){
                textColor = textStyle1 == 0 ? Color.BLACK:FxKit.derivePaint(paint, 0.3);
                backgroundColor = Color.TRANSPARENT;
            }else if(hoverStyle1 == 2){
                textColor = paint;
                backgroundColor = Color.TRANSPARENT;
            }else{
                textColor = textStyle1 == 1 ? paint : Color.web("#333333");
                backgroundColor = FxKit.getOpacityPaint(FxKit.derivePaint(paint, -0.5), 0.05);
            }

            borderColor = Color.TRANSPARENT;
        }else if(status == 3){ //3: active-hover

            //0: 默认，改变背景色 1: 提高文字亮度  2：显示为主题色
            int hoverStyle1 = getHoverStyle();
            int textStyle1 = getTextStyle();
            if(hoverStyle1 == 1){
                textColor = textStyle1 == 0 ? Color.BLACK:FxKit.derivePaint(paint, 0.3);
                backgroundColor = Color.TRANSPARENT;
            }else if(hoverStyle1 == 2){
                textColor = paint;
                backgroundColor = Color.TRANSPARENT;
            }else{
                textColor = textStyle1 == 1 ? paint : Color.web("#333333");
                backgroundColor = FxKit.getOpacityPaint(FxKit.derivePaint(paint, -0.5), 0.05);
            }

            borderColor = paint;
        }else if(status == 4){ //4: filled
            textColor = Color.WHITE;
            borderColor = Color.TRANSPARENT;
            backgroundColor = paint;
        }else if(status == 5){ //5: outside
            textColor = Color.web("#999999");
            borderColor = Color.TRANSPARENT;
            backgroundColor = Color.TRANSPARENT;
        }else if(status == 6){ //6: outside-hover
            textColor = Color.web("#999999");
            borderColor = Color.TRANSPARENT;
            backgroundColor =  FxKit.getOpacityPaint(FxKit.derivePaint(paint, -0.5), 0.05);
        }else{ //0: default
            if(getTextStyle() == 1){
                textColor = paint;
            }else{
                textColor = Color.web("#333333");;
            }
            borderColor = Color.TRANSPARENT;
            backgroundColor = Color.TRANSPARENT;
        }

        if(node instanceof XmIcon){
            ((XmIcon) node).setColor(textColor);
        }else if(node instanceof Text){
            ((Text) node).setFill(textColor);
        }

        setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(1.5), Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID,
                new CornerRadii(1.5), new BorderWidths(0.5), Insets.EMPTY)));

        this.backgroundColor.set(backgroundColor);
        this.borderColor.set(borderColor);
        this.textColor.set(textColor);

    }

    /**
     * 获取点击动画
     * @return
     */
    private ClickAnimate getClickAnimate(){
        if(clickAnimate == null){
            if(getClickAnimateType().equals(ClickAnimateType.SHADOW)){
                clickAnimate = new ClickShadowAnimate(this, getColorType());
                clickAnimate.setAddNode(new CallBack<Node>() {
                    @Override
                    public void call(Node node) {
                        getChildren().add(0, node);
                    }
                });

            }else{
                clickAnimate = new ClickRipperAnimate(this, getColorType(), HueType.LIGHT);
                clickAnimate.setAddNode(new CallBack<Node>() {
                    @Override
                    public void call(Node node) {
                        getChildren().add(node);
                    }
                });

            }

            clickAnimate.setNodePosition(new ResetNodePositionCallback() {
                @Override
                public void call(Node node, double width, double height, double x, double y) {
                    double w = prefWidth(-1),
                            h = prefHeight(-1);
                    layoutInArea(node, x, y, w, h, 0, HPos.CENTER, VPos.CENTER);
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

//    @Override
//    protected double computePrefWidth(double height) {
//        double width = getPrefWidth();
//        if(width == -1){
//            width = textNode.prefWidth(-1);
//        }
//
//        Insets padding = getPadding();
//        if(padding != null){
//            width += padding.getLeft() + padding.getRight();
//        }
//
//        return width;
//    }

    /**
     * 如果自己添加了新节，需要调用这个方法设置节点在该控件中的位置，不然不会显示
     * @param node Node
     * @param x double
     * @param y double
     * @param width double
     * @param height double
     * @param hpos HPos
     * @param vpos VPos
     */
    public void setLayoutInArea(Node node, double x, double y, double width, double height, HPos hpos, VPos vpos){
        layoutInArea(node, x, y, width, height, 0, hpos, vpos);
    }

    @Override
    protected void layoutChildren() {

        double width = this.prefWidth(-1),
            height = this.prefHeight(-1),
            textWidth = node.prefWidth(-1),
            textHeight = node.prefHeight(-1)
        ;

        Insets padding = getPadding();
        if(padding == null){
            padding = Insets.EMPTY;
        }

        double top = snapSpaceY(padding.getTop());
        double left = snapSpaceX(padding.getLeft());
        double bottom = snapSpaceY(padding.getBottom());
        double right = snapSpaceX(padding.getRight());

        double x, y;
        HPos hpos = getTextAlign().getHpos();
        VPos vpos = getTextAlign().getVpos();
        if(hpos.equals(HPos.RIGHT)){
            x = width - textWidth - right;
        }else if(hpos.equals(HPos.CENTER)){
            x = (width - textWidth) / 2;
        }else{
            x = left;
        }

        if(vpos.equals(VPos.CENTER)){
            y = (height - textHeight)/2+Yoffset;
        }else if(vpos.equals(VPos.BOTTOM)){
            y =  height - textHeight - bottom;
        }else {
            y = top;
        }

        layoutInArea(node, x, y, textWidth, textHeight, 0, HPos.CENTER, VPos.CENTER);

    }


    /* ------------------------- properties --------------------*/
    public Node getNode(){
        return node;
    }
    public void setNode(Node node){
        getChildren().remove(this.node);
        getChildren().add(node);
        this.node = node;
    }

    /**
     * 文本所在位置
     */
    private ObjectProperty<Pos> textAlign = null;
    public Pos getTextAlign() {
        return textAlignProperty().get();
    }
    public ObjectProperty<Pos> textAlignProperty() {
        if(textAlign == null){
            textAlign = FxKit.newProperty(Pos.CENTER, StyleableProperties.TEXT_ALIGNMENT, this, "text-align");
        }
        return textAlign;
    }
    public void setTextAlign(Pos textAlign) {
        this.textAlignProperty().set(textAlign);
    }

    /**
     * 按钮内容
     */
    private StringProperty text = null;
    public String getText() {
        return textProperty().get();
    }
    public StringProperty textProperty() {
        if(text == null) text = new SimpleStringProperty(null);
        return text;
    }
    public void setText(String text) {
        this.textProperty().set(text);
    }

    /**
     * 点击回调事件
     * @return ObjectProperty
     */
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return XmNodeButton.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };

    /**
     * 是否填充
     */
    private BooleanProperty filled;
    public final void setFilled(boolean value) {
        filledProperty().set(value);
    }
    public final boolean isFilled() {
        return filledProperty().get();
    }
    private BooleanProperty filledProperty() {
        if (filled == null) {
            filled = new SimpleBooleanProperty() {

                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(FILLED_PSEUDOCLASS_STATE, get());
                    if(get()){
                        pseudoClassStateChanged(OUTSIDE_PSEUDOCLASS_STATE, false);
                        pseudoClassStateChanged(ACTIVE_PSEUDOCLASS_STATE, false);
                    }
                }

                @Override
                public Object getBean() {
                    return XmNodeButton.this;
                }

                @Override
                public String getName() {
                    return "filled";
                }
            };
        }
        return filled;
    }

    /**
     * 是否激活
     */
    private BooleanProperty active;
    public boolean isActive() {
        return activeProperty().get();
    }
    public BooleanProperty activeProperty() {
        if(active == null){
            active = new SimpleBooleanProperty() {

                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(ACTIVE_PSEUDOCLASS_STATE, get());
                    if(get()){
                        pseudoClassStateChanged(OUTSIDE_PSEUDOCLASS_STATE, false);
                        pseudoClassStateChanged(FILLED_PSEUDOCLASS_STATE, false);
                    }
                }

                @Override
                public Object getBean() {
                    return XmNodeButton.this;
                }

                @Override
                public String getName() {
                    return "active";
                }
            };
        }
        return active;
    }

    public void setActive(boolean active) {
        this.activeProperty().set(active);
    }

    /**
     * outside,类似于enable，只是外观相似，能相应事件
     */
    private BooleanProperty outside;

    public boolean isOutside() {
        return outsideProperty().get();
    }

    public BooleanProperty outsideProperty() {
        if(outside == null){
            outside = new SimpleBooleanProperty() {

                @Override
                protected void invalidated() {
                    pseudoClassStateChanged(OUTSIDE_PSEUDOCLASS_STATE, get());
                    if(get()){
                        pseudoClassStateChanged(ACTIVE_PSEUDOCLASS_STATE, false);
                        pseudoClassStateChanged(FILLED_PSEUDOCLASS_STATE, false);
                    }
                }

                @Override
                public Object getBean() {
                    return XmNodeButton.this;
                }

                @Override
                public String getName() {
                    return "outside";
                }
            };
        }
        return outside;
    }

    public void setOutside(boolean outside) {
        this.outsideProperty().set(outside);
    }

    /**
     * 颜色
     */
    private ObjectProperty<ColorType> colorType;
    public ColorType getColorType() {
        return colorTypeProperty().get();
    }
    public ObjectProperty<ColorType> colorTypeProperty() {
        if(colorType == null){
            colorType = FxKit.newProperty(ColorType.primary(), XmNodeButton.StyleableProperties.COLOR_TYPE, this, "colorType");
        }
        return colorType;
    }
    public void setColorType(ColorType colorType) {
        this.colorTypeProperty().set(colorType);
    }

    /**
     * 以下是兼容scenebuilder属性面板的属性设置
     * @param colorType String
     */
    public void setScColorType(String colorType){
        this.colorTypeProperty().set(ColorType.get(colorType));
    }
    public String getScColorType(){
        return this.colorTypeProperty().get().toString();
    }

    /**
     * hover时，按钮的状态
     * 0: 默认，改变背景色
     * 1: 提高文字亮度
     * 2：显示为主题色
     */
    private IntegerProperty hoverStyle;
    public int getHoverStyle() {
        return hoverStyleProperty().get();
    }
    public IntegerProperty hoverStyleProperty() {
        if(hoverStyle == null){
            hoverStyle = FxKit.newIntegerProperty(0, StyleableProperties.HOVER_STYLE, this, "hoverStyle");
        }
        return hoverStyle;
    }
    public void setHoverStyle(int hoverStyle) {
        this.hoverStyleProperty().set(hoverStyle);
    }

    /**
     * 节点初始初始颜色，如果是文字，则是文字的初始颜色
     * 0: 默认为灰色
     * 1：主题色
     */
    private IntegerProperty textStyle;
    public int getTextStyle() {
        return textStyleProperty().get();
    }
    public IntegerProperty textStyleProperty() {
        if(textStyle == null){
            textStyle = FxKit.newIntegerProperty(0, StyleableProperties.TEXT_STYLE, this, "textStyle");
        }
        return textStyle;
    }
    public void setTextStyle(int textStyle) {
        this.textStyleProperty().set(textStyle);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    private double Yoffset = 0;

    public void setYoffset(double Yoffset) {
        this.Yoffset = Yoffset;
    }

    /**
     * 点击控件后的动画效果
     */
    private ObjectProperty<ClickAnimateType> clickAnimateType;

    public ClickAnimateType getClickAnimateType() {
        return clickAnimateTypeProperty().get();
    }
    public ObjectProperty<ClickAnimateType> clickAnimateTypeProperty() {
        if(clickAnimateType==null)
            clickAnimateType = FxKit.newProperty(ClickAnimateType.RIPPER,
                    XmNodeButton.StyleableProperties.CLICK_ANIMATE_TYPE, this, "clickAnimateType");
        return clickAnimateType;
    }
    public void setClickAnimateType(ClickAnimateType clickAnimateType) {
        clickAnimate = null;
        this.clickAnimateTypeProperty().set(clickAnimateType);
    }

    /**
     * 获取颜色值，用于同步绑定用于其他操作
     * @return Paint
     */
    public Paint getBackgroundColor() {
        return backgroundColor.get();
    }
    public ObjectProperty<Paint> backgroundColorProperty() {
        return backgroundColor;
    }
    public Paint getBorderColor() {
        return borderColor.get();
    }
    public ObjectProperty<Paint> borderColorProperty() {
        return borderColor;
    }
    public Paint getTextColor() {
        return textColor.get();
    }
    public ObjectProperty<Paint> textColorProperty() {
        return textColor;
    }

    /* ------------------------- css style ---------------------- */
    private static class StyleableProperties {

        /**
         * -fx-text-align: top-left | top-center | top-right | center-left | center | center-right | bottom-left| bottom-center | bottom-right
         */
        private static final CssMetaData<XmNodeButton,Pos> TEXT_ALIGNMENT =
                new CssMetaData<XmNodeButton,Pos>("-fx-text-align",
                        new EnumConverter<Pos>(Pos.class),
                        Pos.CENTER) {

                    @Override
                    public boolean isSettable(XmNodeButton node) {
                        return node.textAlign == null || !node.textAlign.isBound();
                    }

                    @Override
                    public StyleableProperty<Pos> getStyleableProperty(XmNodeButton node) {
                        return (StyleableProperty<Pos>)node.textAlignProperty();
                    }

                };

        /**
         * 按钮颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmNodeButton, ColorType> COLOR_TYPE =
                new CssMetaData<XmNodeButton, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmNodeButton styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmNodeButton styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };

        /**
         * 鼠标放上去后的样式：
         * -fx-text-style
         * 文字出得初始颜色
         *      0: 默认为灰色
         *      1：主题色
         */
        private static final CssMetaData<XmNodeButton,Number> TEXT_STYLE =
                new CssMetaData<XmNodeButton,Number>("-fx-text-style",
                        StyleConverter.getSizeConverter(),0) {

                    @Override
                    public boolean isSettable(XmNodeButton node) {
                        return node.textStyle == null || !node.textStyle.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(XmNodeButton node) {
                        return (StyleableProperty<Number>)node.hoverStyleProperty();
                    }

                };

        /**
         * 鼠标放上去后的样式：
         * -fx-hover-style
         *      0: 默认，改变背景色
         *      1: 提高文字亮度
         *      2：显示为主题色
         */
        private static final CssMetaData<XmNodeButton,Number> HOVER_STYLE =
                new CssMetaData<XmNodeButton,Number>("-fx-text-style",
                        StyleConverter.getSizeConverter(),0) {

                    @Override
                    public boolean isSettable(XmNodeButton node) {
                        return node.hoverStyle == null || !node.hoverStyle.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(XmNodeButton node) {
                        return (StyleableProperty<Number>)node.hoverStyleProperty();
                    }

                };

        /**
         * 按钮点击动画定义
         * -fx-type-click-animate: ripper | shadow
         */
        final static CssMetaData<XmNodeButton, ClickAnimateType> CLICK_ANIMATE_TYPE =
                new CssMetaData<XmNodeButton, ClickAnimateType>(CssKeys.PropTypeClickAnimate,
                        new EnumConverter<ClickAnimateType>(ClickAnimateType.class),
                        ClickAnimateType.RIPPER, true) {
                    @Override
                    public boolean isSettable(XmNodeButton styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<ClickAnimateType> getStyleableProperty(XmNodeButton styleable) {
                        return null;
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<CssMetaData<? extends Styleable, ?>>(Pane.getClassCssMetaData());
            styleables.add(TEXT_ALIGNMENT);
            styleables.add(COLOR_TYPE);
            styleables.add(HOVER_STYLE);
            styleables.add(TEXT_STYLE);
            styleables.add(CLICK_ANIMATE_TYPE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the
     * {@code CssMetaData} of its superclasses.
     * @return the {@code CssMetaData}
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmNodeButton.StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     *
     * @since JavaFX 8.0
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

}