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
package com.xm2013.jfx.control.scroll;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.ColorTypeConverter;
import com.xm2013.jfx.control.base.CssKeys;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 只是重新定义了一下滚动条的颜色，以及样式
 */
public class XmScrollBar extends ScrollBar {

    protected Skin<?> createDefaultSkin() {
        return new XmScrollBarSkin(this);
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
            colorType = FxKit.newProperty(ColorType.primary(), XmScrollBar.StyleableProperties.COLOR_TYPE, this, "colorType");
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

    private static class StyleableProperties {

        /**
         * 按钮颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmScrollBar, ColorType> COLOR_TYPE =
                new CssMetaData<XmScrollBar, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmScrollBar styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmScrollBar styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };


        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables
                    ,COLOR_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmScrollBar.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

}
