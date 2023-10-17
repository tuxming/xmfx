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
import com.xm2013.jfx.control.base.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 只添加了颜色的支持
 */
public class XmSimpleTextField extends TextField {
    private static final String USER_AGENT_STYLESHEET =
            FxKit.getResourceURL("/css/control.css");
    private static final String STYLE_CLASS_NAME = "xm-simple-text-field";
    public XmSimpleTextField(){
        this(null);
    }

    public XmSimpleTextField(String text){
        super();
        this.init(text);
    }

    public void init(String text){
        this.getStyleClass().add(STYLE_CLASS_NAME);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);
//        this.textField.setText(text);

        this.setStyle("-fx-background-color: transparent; ");

        this.setText(text);
    }

    @Override
    public String getUserAgentStylesheet() {
        return FxKit.USER_AGENT_STYLESHEET;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmSimpleTextFieldSkin(this);
    }

    /* ------------------------ properties --------------------------- */
    private boolean enableSkin = false;
    public void setEnableSkin(boolean enableSkin){
        this.enableSkin = enableSkin;
    }
    public boolean isEnableSkin(){
        return enableSkin;
    }

    /**
     * 后缀图标
     */
    private ObjectProperty<Node> suffixIcon = new SimpleObjectProperty<>();
    public Node getSuffixIcon() {
        return suffixIcon.get();
    }

    public ObjectProperty<Node> suffixIconProperty() {
        return suffixIcon;
    }

    public void setSuffixIcon(Node suffixIcon) {
        this.suffixIcon.set(suffixIcon);
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
            colorType = FxKit.newProperty(ColorType.primary(), XmSimpleTextField.StyleableProperties.COLOR_TYPE, this, "colorType");
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
     * 边框
     */
    private ObjectProperty<BorderType> borderType;
    public BorderType getBorderType() {
        return borderTypeProperty().get();
    }
    public ObjectProperty<BorderType> borderTypeProperty() {
        if(borderType == null){
            borderType = FxKit.newProperty(BorderType.FULL, XmSimpleTextField.StyleableProperties.BORDER_TYPE, this, "borderType");
        }
        return borderType;
    }
    public void setBorderType(BorderType borderType) {
        this.borderTypeProperty().set(borderType);
    }

    /**
     * 尺寸
     */
    private ObjectProperty<SizeType> sizeType;
    public SizeType getSizeType() {
        return sizeTypeProperty().get();
    }

    public ObjectProperty<SizeType> sizeTypeProperty() {
        if(sizeType == null){
            sizeType = FxKit.newProperty(SizeType.SMALL, StyleableProperties.SIZE_TYPE, this, "sizeType");
        }
        return sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeTypeProperty().set(sizeType);
    }

    /**
     * 圆角类型
     */
    private ObjectProperty<RoundType> roundType;
    public RoundType getRoundType() {
        return roundTypeProperty().get();
    }
    public ObjectProperty<RoundType> roundTypeProperty() {
        if(roundType == null){
            roundType = FxKit.newProperty(RoundType.NONE, XmSimpleTextField.StyleableProperties.ROUND_TYPE, this, "roundType");
        }
        return roundType;
    }
    public void setRoundType(RoundType roundType) {
        this.roundTypeProperty().set(roundType);
    }

    /**
     * 是否可以清除
     */
    private BooleanProperty cleanable;
    public boolean isCleanable() {
        return cleanableProperty().get();
    }

    public BooleanProperty cleanableProperty() {
        if(cleanable == null){
            cleanable = FxKit.newBooleanProperty(true, XmSimpleTextField.StyleableProperties.CLEANABLE, this, "cleanable");
        }
        return cleanable;
    }
    public void setCleanable(boolean cleanable){
        this.cleanableProperty().set(cleanable);
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {


        /**
         * 按钮颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmSimpleTextField, ColorType> COLOR_TYPE =
                new CssMetaData<XmSimpleTextField, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmSimpleTextField styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmSimpleTextField styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };


        /**
         * 圆角类型
         * -fx-type-round: none | small | semicircle | circle
         */
        final static CssMetaData<XmSimpleTextField, RoundType> ROUND_TYPE =
                new CssMetaData<XmSimpleTextField, RoundType>(CssKeys.PropTypeRound,
                        new EnumConverter<RoundType>(RoundType.class),
                        RoundType.NONE, true) {
                    @Override
                    public boolean isSettable(XmSimpleTextField styleable) {
                        return styleable.roundType == null || !styleable.roundType.isBound();
                    }

                    @Override
                    public StyleableProperty<RoundType> getStyleableProperty(XmSimpleTextField styleable) {
                        return (StyleableProperty<RoundType>) styleable.roundTypeProperty();
                    }
                };

        /**
         * 边框类型
         * -fx-type-border: none | full | bottom
         */
        private final static CssMetaData<XmSimpleTextField, BorderType> BORDER_TYPE =
                new CssMetaData<XmSimpleTextField, BorderType>(CssKeys.PropTypeBorder,
                        new EnumConverter<BorderType>(BorderType.class),
                        BorderType.FULL, true) {
                    @Override
                    public boolean isSettable(XmSimpleTextField styleable) {
                        return styleable.borderType == null || !styleable.borderType.isBound();
                    }

                    @Override
                    public StyleableProperty<BorderType> getStyleableProperty(XmSimpleTextField styleable) {
                        return (StyleableProperty<BorderType>) styleable.borderTypeProperty();
                    }
                };

        private static final CssMetaData<XmSimpleTextField,Boolean> CLEANABLE = new CssMetaData<XmSimpleTextField, Boolean>(
                "-fx-cleanable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmSimpleTextField styleable) {
                return styleable.cleanable == null || !styleable.cleanable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmSimpleTextField styleable) {
                return (StyleableProperty<Boolean>) styleable.cleanableProperty();
            }
        };

        /**
         * 大小尺寸
         * -fx-type-color: small, medium, large
         */
        final static CssMetaData<XmSimpleTextField, SizeType> SIZE_TYPE =
                new CssMetaData<XmSimpleTextField, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        SizeType.SMALL, true) {
                    @Override
                    public boolean isSettable(XmSimpleTextField styleable) {
                        return styleable.sizeType == null || !styleable.sizeType.isBound();
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(XmSimpleTextField styleable) {
                        return (StyleableProperty<SizeType>) styleable.sizeTypeProperty();
                    }
                };

        // 创建一个CSS样式的表
        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(TextField.getClassCssMetaData());
            Collections.addAll(styleables
                    ,ROUND_TYPE
                    ,COLOR_TYPE
                    ,BORDER_TYPE
                    ,SIZE_TYPE
                    ,CLEANABLE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmSimpleTextField.StyleableProperties.STYLEABLES;
    }

}
