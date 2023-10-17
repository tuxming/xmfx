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
package com.xm2013.jfx.control.base;

import com.xm2013.jfx.common.FxKit;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.InsetsConverter;
import javafx.geometry.Insets;
import javafx.scene.control.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmControl extends Control {

    /**
     * 颜色
     */
    private ObjectProperty<ColorType> colorType;
    public ColorType getColorType() {
        return colorTypeProperty().get();
    }
    public ObjectProperty<ColorType> colorTypeProperty() {
        if(colorType == null){
            colorType = FxKit.newProperty(ColorType.primary(), XmControl.StyleableProperties.COLOR_TYPE, this, "colorType");
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
     * 尺寸
     */
    private ObjectProperty<SizeType> sizeType;
    public SizeType getSizeType() {
        return sizeTypeProperty().get();
    }

    public ObjectProperty<SizeType> sizeTypeProperty() {
        if(sizeType == null){
            sizeType = FxKit.newProperty(SizeType.MEDIUM, XmControl.StyleableProperties.SIZE_TYPE, this, "sizeType");
        }
        return sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeTypeProperty().set(sizeType);
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
            borderType = FxKit.newProperty(BorderType.FULL, XmControl.StyleableProperties.BORDER_TYPE, this, "borderType");
        }
        return borderType;
    }
    public void setBorderType(BorderType borderType) {
        this.borderTypeProperty().set(borderType);
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
            roundType = FxKit.newProperty(RoundType.NONE, XmControl.StyleableProperties.ROUND_TYPE, this, "roundType");
        }
        return roundType;
    }
    public void setRoundType(RoundType roundType) {
        this.roundTypeProperty().set(roundType);
    }

    /**
     * 外边距
     */
    private ObjectProperty<Insets> margin;
    public Insets getMargin() {
        return marginProperty().get();
    }
    public ObjectProperty<Insets> marginProperty() {
        if(margin == null){
            margin = FxKit.newProperty(Insets.EMPTY, XmControl.StyleableProperties.MARGIN, this, "margin");
        }
        return margin;
    }

    public void setMargin(Insets margin) {
        this.marginProperty().set(margin);
    }

    /*---------------------------css style ---------------------*/
    private static class StyleableProperties {

        /**
         * 按钮颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmControl, ColorType> COLOR_TYPE =
                new CssMetaData<XmControl, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmControl styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmControl styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };

        /**
         * 大小尺寸
         * -fx-type-color: small, medium, large
         */
        final static CssMetaData<XmControl, SizeType> SIZE_TYPE =
                new CssMetaData<XmControl, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        SizeType.MEDIUM, true) {
                    @Override
                    public boolean isSettable(XmControl styleable) {
                        return styleable.sizeType == null || !styleable.sizeType.isBound();
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(XmControl styleable) {
                        return (StyleableProperty<SizeType>) styleable.sizeTypeProperty();
                    }
                };

        /**
         * 圆角类型
         * -fx-type-round: none | small | semicircle | circle
         */
        final static CssMetaData<XmControl, RoundType> ROUND_TYPE =
                new CssMetaData<XmControl, RoundType>(CssKeys.PropTypeRound,
                        new EnumConverter<RoundType>(RoundType.class),
                        RoundType.NONE, true) {
                    @Override
                    public boolean isSettable(XmControl styleable) {
                        return styleable.roundType == null || !styleable.roundType.isBound();
                    }

                    @Override
                    public StyleableProperty<RoundType> getStyleableProperty(XmControl styleable) {
                        return (StyleableProperty<RoundType>) styleable.roundTypeProperty();
                    }
                };

        /**
         * 边框类型
         * -fx-type-border: none | full | bottom
         */
        private final static CssMetaData<XmControl, BorderType> BORDER_TYPE =
                new CssMetaData<XmControl, BorderType>(CssKeys.PropTypeBorder,
                        new EnumConverter<BorderType>(BorderType.class),
                        BorderType.FULL, true) {
                    @Override
                    public boolean isSettable(XmControl styleable) {
                        return styleable.borderType == null || !styleable.borderType.isBound();
                    }

                    @Override
                    public StyleableProperty<BorderType> getStyleableProperty(XmControl styleable) {
                        return (StyleableProperty<BorderType>) styleable.borderTypeProperty();
                    }
                };

        private static final CssMetaData<XmControl,Insets> MARGIN =
                new CssMetaData<XmControl,Insets>("-fx-margin",
                        InsetsConverter.getInstance(), Insets.EMPTY) {

                    @Override public boolean isSettable(XmControl node) {
                        return node.margin == null || !node.margin.isBound();
                    }

                    @Override public StyleableProperty<Insets> getStyleableProperty(XmControl node) {
                        return (StyleableProperty<Insets>)node.paddingProperty();
                    }
                };

        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables
                    ,COLOR_TYPE
                    ,SIZE_TYPE
                    ,ROUND_TYPE
                    ,BORDER_TYPE
                    ,MARGIN
            );
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmControl.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
