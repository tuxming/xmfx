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

import com.xm2013.jfx.control.base.*;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 按钮类型样式
 */
public abstract class XmButtonTypeCssMetaData<S extends Styleable> extends CssMetaData<S, XmButtonType> {

    public XmButtonTypeCssMetaData(String property, StyleConverter converter, XmButtonType initialValue) {
        super(property, converter, initialValue);
    }

    public XmButtonTypeCssMetaData(String property, XmButtonType initial) {
        super(property, new XmButtonTypeConverter(initial), initial, true,
                createSubProperties(property, initial));
    }

    private static <S extends Styleable> List<CssMetaData<? extends Styleable, ?>>
    createSubProperties(String property, XmButtonType initial) {
        final List<CssMetaData<S, ?>> subProperties = new ArrayList<>();

        final XmButtonType defaultType = initial != null ? initial : XmButtonType.getDefault();

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
         * 按钮大小尺寸
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
         * 按钮圆角
         * -fx-type-round: none | small | semicircle | circle
         */
        final CssMetaData<S, RoundType> ROUND_TYPE =
                new CssMetaData<S, RoundType>(CssKeys.PropTypeRound,
                        new EnumConverter<RoundType>(RoundType.class),
                        defaultType.getRoundType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<RoundType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(ROUND_TYPE);

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
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

        /**
         * 按钮边框
         * -fx-type-border: none | full | bottom
         */
        final CssMetaData<S, BorderType> BORDER_TYPE =
                new CssMetaData<S, BorderType>(CssKeys.PropTypeBorder,
                        new EnumConverter<BorderType>(BorderType.class),
                        defaultType.getBorderType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<BorderType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(BORDER_TYPE);

        /**
         * 按钮显示方式
         * -fx-type-display: over-show-icon | over-show-text | over-switch-icon | over-switch-text | full
         */
        final CssMetaData<S, XmButtonType.BtnDisplayType> DISPLAY_TYPE =
                new CssMetaData<S, XmButtonType.BtnDisplayType>(CssKeys.PropTypeDisplay,
                        new EnumConverter<XmButtonType.BtnDisplayType>(XmButtonType.BtnDisplayType.class),
                        defaultType.getDisplayType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<XmButtonType.BtnDisplayType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(DISPLAY_TYPE);

        /**
         * 按钮加载状态中的动画定义
         * -fx-type-loading
         */
        final CssMetaData<S, XmButtonType.BtnLoadingType> LOADING_TYPE =
                new CssMetaData<S, XmButtonType.BtnLoadingType>(CssKeys.PropTypeLoading,
                        new EnumConverter<XmButtonType.BtnLoadingType>(XmButtonType.BtnLoadingType.class),
                        defaultType.getLoadingType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<XmButtonType.BtnLoadingType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(LOADING_TYPE);

        /**
         * 按钮点击动画定义
         * -fx-type-click-animate: ripper | shadow
         */
        final CssMetaData<S, ClickAnimateType> CLICK_ANIMATE_TYPE =
                new CssMetaData<S, ClickAnimateType>(CssKeys.PropTypeClickAnimate,
                        new EnumConverter<ClickAnimateType>(ClickAnimateType.class),
                        defaultType.getClickAnimateType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<ClickAnimateType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(CLICK_ANIMATE_TYPE);

        /**
         * 按钮hover图标动画定义
         * -fx-type-click-animate: none | rotate | jitter | overturn | flash | scale
         */
        final CssMetaData<S, XmButtonType.BtnIconAnimationType> ICON_ANIMATE_TYPE =
                new CssMetaData<S, XmButtonType.BtnIconAnimationType>(CssKeys.PropTypeIconAnimate,
                        new EnumConverter<XmButtonType.BtnIconAnimationType>(XmButtonType.BtnIconAnimationType.class),
                        defaultType.getHoverAnimateType(), true) {
                    @Override
                    public boolean isSettable(S styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<XmButtonType.BtnIconAnimationType> getStyleableProperty(S styleable) {
                        return null;
                    }
                };
        subProperties.add(ICON_ANIMATE_TYPE);


        return Collections.<CssMetaData<? extends Styleable, ?>>unmodifiableList(subProperties);
    }
}
