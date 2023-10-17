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
package com.xm2013.jfx.control.icon;

import com.xm2013.jfx.common.FxKit;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 定义图标的基础类
 */
public abstract class XmIcon extends Control{

    private static String DEFAULT_STYLE_CLASS="xm-icon";

    protected XmIcon() {
        getStyleClass().add("xm-icon");
        setFocusTraversable(false);
    }

    /**
     * 复制一个图标
     * @return XmIcon
     */
    public abstract XmIcon clone();

    /* --------------------- properties ------------------------*/
    /**
     * 图标尺寸
     */
    private DoubleProperty size;
    public double getSize() {
        return sizeProperty().get();
    }
    /**
     * 图标尺寸
     * @return DoubleProperty
     */
    public DoubleProperty sizeProperty() {
        if(size == null){
            size = FxKit.newDoubleProperty(16d, StyleableProperties.SIZE, this, "size");
        }
        return size;
    }
    /**
     * 图标尺寸
     * @param size double
     */
    public void setSize(double size){
        sizeProperty().set(size);
    }

    /**
     * 图标颜色
     */
    private ObjectProperty<Paint> color;

    /**
     * 图标颜色
     * @return Paint
     */
    public Paint getColor() {
        return colorProperty().get();
    }

    /**
     * 图标颜色
     * @return ObjectProperty
     */
    public ObjectProperty<Paint> colorProperty() {
        if(color== null){
            color = FxKit.newProperty(Color.BLACK, StyleableProperties.COLOR, this, "color");
        }
        return color;
    }
    /**
     * 图标颜色
     * @param color Paint
     */
    public void setColor(Paint color){
        colorProperty().set(color);
    }


    /*---------------------------css style ---------------------*/
    private static class StyleableProperties {

        /**
         * 图标大小的css
         * -fx-size: 16px;
         */
        private static final CssMetaData<XmIcon, Number> SIZE = new CssMetaData<XmIcon, Number>("-fx-size", SizeConverter.getInstance(), 16d) {
            @Override
            public boolean isSettable(XmIcon node) {
                return node.size == null || !node.size.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(XmIcon node) {
                return (StyleableProperty<Number>) node.sizeProperty();
            }
        };

        /**
         * 图标大小的颜色
         * -fx-color:
         */
        private static final CssMetaData<XmIcon, Paint> COLOR = new CssMetaData<XmIcon, Paint>("-fx-color", PaintConverter.getInstance(), Color.BLACK) {
            @Override
            public boolean isSettable(XmIcon node) {
                return node.size == null || !node.size.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(XmIcon node) {
                return (StyleableProperty<Paint>) node.colorProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {

            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(Node.getClassCssMetaData());
            styleables.add(SIZE);
            styleables.add(COLOR);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmIcon.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
