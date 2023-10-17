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

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import javafx.beans.property.*;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmButton extends Button{
    /*-------------------Constructor ------------------------*/

    public XmButton() {
        this(null);
    }

    public XmButton(String text) {
        this(text, null);
    }

    public XmButton(String text, Node graphic) {
        super(text, graphic);
        init(text);
    }

    private void init(String text) {
        //初始化按钮
        setAccessibleRole(AccessibleRole.BUTTON);
        setAlignment(Pos.CENTER);
        getStylesheets().add(0, FxKit.USER_AGENT_STYLESHEET);
        getStyleClass().addAll("xm-button");
        setContentDisplay(ContentDisplay.LEFT);
        this.setContent(text);

    }
    /*----------------------properties---------------------*/
    /**
     * 按钮的文本内容，当设置为在鼠标放上去的时候在显示文本的时候，文本内容需要一个地方缓存
     */
    private StringProperty content;
    public String getContent() {
        return contentProperty().get();
    }
    public StringProperty contentProperty() {
        if(content == null){
            content = new SimpleStringProperty();
        }
        return content;
    }
    public void setContent(String content) {
        this.contentProperty().set(content);
        this.textProperty().set(content);
    }

    /**
     * 按钮的自定义外观类型
     */
    /**
     * 颜色
     */
    private ObjectProperty<ColorType> colorType;
    public ColorType getColorType() {
        return colorTypeProperty().get();
    }
    public ObjectProperty<ColorType> colorTypeProperty() {
        if(colorType == null){
            colorType = FxKit.newProperty(ColorType.primary(), XmButton.StyleableProperties.COLOR_TYPE, this, "colorType");
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
            sizeType = FxKit.newProperty(SizeType.MEDIUM, XmButton.StyleableProperties.SIZE_TYPE, this, "sizeType");
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
            borderType = FxKit.newProperty(BorderType.FULL, XmButton.StyleableProperties.BORDER_TYPE, this, "borderType");
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
            roundType = FxKit.newProperty(RoundType.NONE, XmButton.StyleableProperties.ROUND_TYPE, this, "roundType");
        }
        return roundType;
    }
    public void setRoundType(RoundType roundType) {
        this.roundTypeProperty().set(roundType);
    }

    /**
     * 显示类型
     */
    private ObjectProperty<XmButtonType.BtnDisplayType> displayType;
    public XmButtonType.BtnDisplayType getDisplayType() {
        return displayTypeProperty().get();
    }
    public ObjectProperty<XmButtonType.BtnDisplayType> displayTypeProperty() {
        if(displayType == null){
            displayType = FxKit.newProperty(XmButtonType.BtnDisplayType.FULL, StyleableProperties.DISPLAY_TYPE, this, "displayType");
        }
        return displayType;
    }
    public void setDisplayType(XmButtonType.BtnDisplayType displayType) {
        this.displayTypeProperty().set(displayType);
    }

    /**
     * 色调类型，在亮色和暗色背景下面，控件的样式
     */
    private ObjectProperty<HueType> hueType;
    public HueType getHueType() {
        return hueTypeProperty().get();
    }
    public ObjectProperty<HueType> hueTypeProperty() {
        if(hueType == null)
            hueType = FxKit.newProperty(HueType.DARK, XmButton.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }
    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
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
                    XmButton.StyleableProperties.CLICK_ANIMATE_TYPE, this, "clickAnimateType");
        return clickAnimateType;
    }
    public void setClickAnimateType(ClickAnimateType clickAnimateType) {
        this.clickAnimateTypeProperty().set(clickAnimateType);
    }

    /**
     * 加载中的动画效果
     */
    private ObjectProperty<XmButtonType.BtnLoadingType> loadingType;
    public XmButtonType.BtnLoadingType getLoadingType() {
        return loadingTypeProperty().get();
    }
    public ObjectProperty<XmButtonType.BtnLoadingType> loadingTypeProperty() {
        if(loadingType == null)
            loadingType = FxKit.newProperty(XmButtonType.BtnLoadingType.NONE, XmButton.StyleableProperties.LOADING_TYPE, this, "loadingType");
        return loadingType;
    }
    public void setLoadingType(XmButtonType.BtnLoadingType loadingType) {
        this.loadingTypeProperty().set(loadingType);
    }

    /**
     * 鼠标移入到控件时，空间的图标动画效果
     */
    private ObjectProperty<XmButtonType.BtnIconAnimationType> iconAnimateType;
    public XmButtonType.BtnIconAnimationType getIconAnimateType() {
        return iconAnimateTypeProperty().get();
    }
    public ObjectProperty<XmButtonType.BtnIconAnimationType> iconAnimateTypeProperty() {
        if(iconAnimateType == null)
            iconAnimateType = FxKit.newProperty(XmButtonType.BtnIconAnimationType.NONE, XmButton.StyleableProperties.ICON_ANIMATE_TYPE, this, "iconAnimateType");
        return iconAnimateType;
    }
    public void setIconAnimateType(XmButtonType.BtnIconAnimationType iconAnimateType) {
        this.iconAnimateTypeProperty().set(iconAnimateType);
    }
    //    private ObjectProperty<XmButtonType> type;
//    public XmButtonType getType() {
//        return typeProperty().get();
//    }
//    public ObjectProperty<XmButtonType> typeProperty() {
//        if (type == null) {
//            type = new StyleableObjectProperty<XmButtonType>(new XmButtonType()) {
//
//                private boolean fontSetByCss = false;
//
//                @Override
//                public void applyStyle(StyleOrigin newOrigin, XmButtonType value) {
//                    try {
//                        fontSetByCss = true;
//                        super.applyStyle(newOrigin, value);
//                    } catch(Exception e) {
//                        throw e;
//                    } finally {
//                        fontSetByCss = false;
//                    }
//                }
//
//                @Override
//                public void set(XmButtonType value) {
//
//                    final XmButtonType old = get();
//                    if (!Objects.equals(old, value)) {
//                        super.set(value);
//                    }
//                }
//
//                @Override
//                protected void invalidated() {
//                    if(fontSetByCss == false) {
//                        XmButton.this.requestLayout();
//                    }
//                }
//
//                @Override
//                public CssMetaData<XmButton , XmButtonType> getCssMetaData() {
//                    return XmButton.StyleableProperties.TYPE;
//                }
//
//                @Override
//                public Object getBean() {
//                    return XmButton.this;
//                }
//
//                @Override
//                public String getName() {
//                    return "font";
//                }
//            };
//        }
//        return type;
//    }
//
//    public void setType(XmButtonType type) {
//        this.typeProperty().set(type);
//    }
//
    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终宽度
     */
    private DoubleProperty finalWidth;
    public double getFinalWidth() {
        return finalWidthProperty().get();
    }

    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终宽度
     * @return DoubleProperty
     */
    public DoubleProperty finalWidthProperty() {
        if(finalWidth == null){
            finalWidth = new SimpleDoubleProperty(0);
        }
        return finalWidth;
    }

    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终宽度
     * @param prevWidth double
     */
    public void setFinalWidth(double prevWidth) {
        this.finalWidthProperty().set(prevWidth);
    }

    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终高度
     */
    private DoubleProperty finalHeight;

    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终高度
     * @return double
     */
    public double getFinalHeight() {
        return finalHeightProperty().get();
    }

    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终高度
     * @return DoubleProperty
     */
    public DoubleProperty finalHeightProperty() {
        if(finalHeight == null){
            finalHeight = new SimpleDoubleProperty(0);
        }
        return finalHeight;
    }

    /**
     * 当DisplayType的类型不是FULL的时候，按钮的最终高度
     * @param finalHeight double
     */
    public void setFinalHeight(double finalHeight) {
        this.finalHeightProperty().set(finalHeight);
    }

    /**
     * 是否自己图标设置尺寸
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     */
    private BooleanProperty autoGraphicSize;

    /**
     * 是否自己图标设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     * @return boolean
     */
    public boolean isAutoGraphicSize() {
        return autoGraphicSizeProperty().get();
    }

    /**
     * 是否自己图标设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     * @return BooleanProperty
     */
    public BooleanProperty autoGraphicSizeProperty() {
        if(autoGraphicSize == null){
            autoGraphicSize = FxKit.newBooleanProperty(true, StyleableProperties.AUTO_GRAPHIC_SIZE, this, "autoGraphicSize");
        }
        return autoGraphicSize;
    }

    /**
     * 是否自己图标设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     * @param autoGraphicSize boolean
     */
    public void setAutoGraphicSize(boolean autoGraphicSize) {
        this.autoGraphicSizeProperty().set(autoGraphicSize);
    }

    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     */
    private BooleanProperty autoTextSize;

    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     * @return boolean
     */
    public boolean isAutoTextSize() {
        return autoTextSizeProperty().get();
    }
    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     * @return BooleanProperty
     */
    public BooleanProperty autoTextSizeProperty() {
        if(autoTextSize == null){
            autoTextSize = FxKit.newBooleanProperty(true, StyleableProperties.AUTO_TEXT_SIZE, this, "autoTextSize");
        }
        return autoTextSize;
    }

    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     * @param autoTextSize boolean
     */
    public void setAutoTextSize(boolean autoTextSize) {
        this.autoTextSizeProperty().set(autoTextSize);
    }

    /**
     * 当前经度的百分比，当进度达到1=100%的时候，动画执行完毕
     */
    private DoubleProperty loadingPercent;
    /**
     * 当前经度的百分比，当进度达到1=100%的时候，动画执行完毕
     * @return double
     */
    public double getLoadingPercent() {
        return loadingPercentProperty().get();
    }
    /**
     * 当前经度的百分比，当进度达到1=100%的时候，动画执行完毕
     * @return DoubleProperty
     */
    public DoubleProperty loadingPercentProperty() {
        if(loadingPercent == null){
            loadingPercent = new SimpleDoubleProperty(0);
        }
        return loadingPercent;
    }
    /**
     * 当前经度的百分比，当进度达到1=100%的时候，动画执行完毕
     * @param loadingPercent boolean
     */
    public void setLoadingPercent(double loadingPercent) {
        this.loadingPercentProperty().set(loadingPercent);
    }

    /**
     * 是否加载中....
     */
    private BooleanProperty loading;
    /**
     * 是否加载中....
     * @return boolean
     */
    public boolean isLoading() {
        return loadingProperty().get();
    }
    /**
     * 是否加载中....
     * @return BooleanProperty
     */
    public BooleanProperty loadingProperty() {
        if(loading == null){
            loading = new SimpleBooleanProperty(false);
        }
        return loading;
    }
    /**
     * 是否加载中....
     * @param loading boolean
     */
    public void setLoading(boolean loading) {
        this.loadingProperty().set(loading);
    }

    /**
     * 是否显示加载的百分比进度
     */
    private BooleanProperty showLoadingPercent;
    public boolean isShowLoadingPercent() {
        return showLoadingPercentProperty().get();
    }
    public BooleanProperty showLoadingPercentProperty() {
        if(showLoadingPercent == null) showLoadingPercent = new SimpleBooleanProperty(true);
        return showLoadingPercent;
    }
    public void setShowLoadingPercent(boolean showLoadingPercent) {
        this.showLoadingPercentProperty().set(showLoadingPercent);
    }

    /*----------------------------override -------------------*/
    protected Skin<?> createDefaultSkin() {
        return new XmButtonSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return FxKit.USER_AGENT_STYLESHEET;
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {
//        /**
//         * -fx-type
//         */
//        private static final XmButtonTypeCssMetaData<XmButton> TYPE =
//                new XmButtonTypeCssMetaData<XmButton>("-fx-type", XmButtonType.getDefault()) {
//
//                    @Override
//                    public boolean isSettable(XmButton n) {
//                        return n.type == null || !n.type.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<XmButtonType> getStyleableProperty(XmButton n) {
//                        return (StyleableProperty<XmButtonType>)(WritableValue<XmButtonType>)n.typeProperty();
//                    }
//                };

        /**
         * -fx-auto-graphic-size
         */
        private static final CssMetaData<XmButton,Boolean> AUTO_GRAPHIC_SIZE =
                new CssMetaData<XmButton,Boolean>("-fx-auto-graphic-size", BooleanConverter.getInstance(), Boolean.TRUE) {

            @Override
            public boolean isSettable(XmButton node) {
                return node.autoGraphicSize == null || !node.autoGraphicSize.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmButton node) {
                return (StyleableProperty<Boolean>)node.autoGraphicSizeProperty();
            }
        };

        /**
         * -fx-auto-text-size
         */
        private static final CssMetaData<XmButton,Boolean> AUTO_TEXT_SIZE =
                new CssMetaData<XmButton,Boolean>("-fx-auto-graphic-text", BooleanConverter.getInstance(), Boolean.TRUE) {

                    @Override
                    public boolean isSettable(XmButton node) {
                        return node.autoTextSize == null || !node.autoTextSize.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(XmButton node) {
                        return (StyleableProperty<Boolean>)node.autoTextSizeProperty();
                    }
                };

        /**
         * 按钮颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmButton, ColorType> COLOR_TYPE =
                new CssMetaData<XmButton, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmButton styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };

        /**
         * 大小尺寸
         * -fx-type-color: small, medium, large
         */
        final static CssMetaData<XmButton, SizeType> SIZE_TYPE =
                new CssMetaData<XmButton, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        SizeType.MEDIUM, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return styleable.sizeType == null || !styleable.sizeType.isBound();
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(XmButton styleable) {
                        return (StyleableProperty<SizeType>) styleable.sizeTypeProperty();
                    }
                };

        /**
         * 圆角类型
         * -fx-type-round: none | small | semicircle | circle
         */
        final static CssMetaData<XmButton, RoundType> ROUND_TYPE =
                new CssMetaData<XmButton, RoundType>(CssKeys.PropTypeRound,
                        new EnumConverter<RoundType>(RoundType.class),
                        RoundType.NONE, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return styleable.roundType == null || !styleable.roundType.isBound();
                    }

                    @Override
                    public StyleableProperty<RoundType> getStyleableProperty(XmButton styleable) {
                        return (StyleableProperty<RoundType>) styleable.roundTypeProperty();
                    }
                };

        /**
         * 边框类型
         * -fx-type-border: none | full | bottom
         */
        private final static CssMetaData<XmButton, BorderType> BORDER_TYPE =
                new CssMetaData<XmButton, BorderType>(CssKeys.PropTypeBorder,
                        new EnumConverter<BorderType>(BorderType.class),
                        BorderType.FULL, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return styleable.borderType == null || !styleable.borderType.isBound();
                    }

                    @Override
                    public StyleableProperty<BorderType> getStyleableProperty(XmButton styleable) {
                        return (StyleableProperty<BorderType>) styleable.borderTypeProperty();
                    }
                };
        /**
         * 按钮显示方式
         * -fx-type-display: over-show-icon | over-show-text | over-switch-icon | over-switch-text | full
         */
        final static CssMetaData<XmButton, XmButtonType.BtnDisplayType> DISPLAY_TYPE =
                new CssMetaData<XmButton, XmButtonType.BtnDisplayType>(CssKeys.PropTypeDisplay,
                        new EnumConverter<XmButtonType.BtnDisplayType>(XmButtonType.BtnDisplayType.class),
                        XmButtonType.BtnDisplayType.FULL, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<XmButtonType.BtnDisplayType> getStyleableProperty(XmButton styleable) {
                        return null;
                    }
                };

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmButton, HueType> HUE_TYPE =
                new CssMetaData<XmButton, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.DARK, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmButton styleable) {
                        return null;
                    }
                };

        /**
         * 按钮点击动画定义
         * -fx-type-click-animate: ripper | shadow
         */
        final static CssMetaData<XmButton, ClickAnimateType> CLICK_ANIMATE_TYPE =
                new CssMetaData<XmButton, ClickAnimateType>(CssKeys.PropTypeClickAnimate,
                        new EnumConverter<ClickAnimateType>(ClickAnimateType.class),
                        ClickAnimateType.RIPPER, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<ClickAnimateType> getStyleableProperty(XmButton styleable) {
                        return null;
                    }
                };

        /**
         * 按钮加载状态中的动画定义
         * -fx-type-loading
         */
        final static CssMetaData<XmButton, XmButtonType.BtnLoadingType> LOADING_TYPE =
                new CssMetaData<XmButton, XmButtonType.BtnLoadingType>(CssKeys.PropTypeLoading,
                        new EnumConverter<XmButtonType.BtnLoadingType>(XmButtonType.BtnLoadingType.class),
                        XmButtonType.BtnLoadingType.NONE, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<XmButtonType.BtnLoadingType> getStyleableProperty(XmButton styleable) {
                        return null;
                    }
                };

        /**
         * 按钮hover图标动画定义
         * -fx-type-click-animate: none | rotate | jitter | overturn | flash | scale
         */
        final static CssMetaData<XmButton, XmButtonType.BtnIconAnimationType> ICON_ANIMATE_TYPE =
                new CssMetaData<XmButton, XmButtonType.BtnIconAnimationType>(CssKeys.PropTypeIconAnimate,
                        new EnumConverter<XmButtonType.BtnIconAnimationType>(XmButtonType.BtnIconAnimationType.class),
                        XmButtonType.BtnIconAnimationType.NONE, true) {
                    @Override
                    public boolean isSettable(XmButton styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<XmButtonType.BtnIconAnimationType> getStyleableProperty(XmButton styleable) {
                        return null;
                    }
                };

        // 创建一个CSS样式的表
        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Button.getClassCssMetaData());
            Collections.addAll(styleables
//                    ,TYPE
                    ,AUTO_GRAPHIC_SIZE
                    ,AUTO_TEXT_SIZE
                    ,ROUND_TYPE
                    ,SIZE_TYPE
                    ,COLOR_TYPE
                    ,BORDER_TYPE
                    ,DISPLAY_TYPE
                    ,HUE_TYPE
                    ,CLICK_ANIMATE_TYPE
                    ,LOADING_TYPE
                    ,ICON_ANIMATE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

}
