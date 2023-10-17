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

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 控件类型样式
 */
public abstract class XmControlTypeCssMetaData<S extends Styleable> extends CssMetaData<S, XmControlType> {

    public XmControlTypeCssMetaData(String property, StyleConverter converter, XmControlType initialValue) {
        super(property, converter, initialValue);
    }

    public XmControlTypeCssMetaData(String property, XmControlType initial) {
        super(property, new XmControlTypeConverter(initial), initial, true,
                createSubProperties(property, initial));
    }

    private static <S extends Styleable> List<CssMetaData<? extends Styleable, ?>>
    createSubProperties(String property, XmControlType initial) {
        final List<CssMetaData<S, ?>> subProperties = new ArrayList<>();

        final XmControlType defaultType = initial != null ? initial : XmControlType.getNullable();

        /**
         * 按钮颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final CssMetaData<S, ColorType> COLOR_TYPE =
                new CssMetaData<S, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        defaultType.getColorType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(COLOR_TYPE);

        /**
         * 控件大小尺寸
         * -fx-type-color: small, medium, large
         */
        final CssMetaData<S, SizeType> SIZE_TYPE =
                new CssMetaData<S, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        defaultType.getSizeType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(SIZE_TYPE);

        /**
         * 控件色调，dark/light
         * -fx-type-round: dark | light
         */
        final CssMetaData<S, HueType> HUE_TYPE =
                new CssMetaData<S, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        defaultType.getHueType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(HUE_TYPE);

        return Collections.<CssMetaData<? extends Styleable, ?>>unmodifiableList(subProperties);
    }
}
