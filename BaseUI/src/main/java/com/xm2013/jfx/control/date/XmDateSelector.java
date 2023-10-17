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
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.*;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmDateSelector extends XmControl {

    private static final String USER_AGENT_STYLESHEET = FxKit.USER_AGENT_STYLESHEET;
    private static final String DEFAULT_STYLE_CLASS = "xm-date-selector";

    /* ---------------------------- constructor / override -------------------------------------*/

    public XmDateSelector(){
        this(LocalDateTime.now());
    }
    public XmDateSelector(LocalDateTime date){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.DATE_PICKER);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);
        setHueType(HueType.LIGHT);
        setSelectedDate(date);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmDateSelectorSkin(this);
    }

    /* ------------------------------- properties ---------------------------------- */

    /**
     * 选中的日期
     */
    private ObjectProperty<LocalDateTime> selectedDate = new SimpleObjectProperty<>();
    public LocalDateTime getSelectedDate() {
        return selectedDate.get() == null ? null : selectedDate.get();
    }

    public ObjectProperty<LocalDateTime> selectedDateProperty() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDateTime selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    /**
     * 获取格式化后的字符类型的日期
     * @return String
     */
    public String getFormateSelectedDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getFormatPattern());
        return getSelectedDate().format(formatter);
    }

    public StringProperty formatPattern = new SimpleStringProperty();
    public String getFormatPattern() {
        String pattern = formatPattern.get();
        if(pattern == null){
            DateType dt = getDateType();
            if(DateType.YEAR.equals(dt)){
                pattern = FxKit.YEAR_PATTERN;
            }else if(DateType.MONTH.equals(dt)){
                pattern = FxKit.MONTH_PATTERN;
            }else if(DateType.TIME.equals(dt)){
                pattern = FxKit.TIME_PATTERN;
            }else if(DateType.DATETIME.equals(dt)){
                pattern = FxKit.DATETIME_PATTERN;
            }else{
                pattern = FxKit.DATE_PATTERN;
            }
        }
        return pattern;
    }
    public StringProperty formatPatternProperty() {
        return formatPattern;
    }
    public void setFormatPattern(String formatPattern) {
        this.formatPattern.set(formatPattern);
    }

    /**
     * 是否显示清除内容图标
     */
    private BooleanProperty cleanable;

    public boolean isCleanable() {
        return cleanableProperty().get();
    }

    public BooleanProperty cleanableProperty() {
        if(cleanable == null){
            cleanable = FxKit.newBooleanProperty(true, StyleableProperties.CLEANABLE, this, "cleanable");
        }
        return cleanable;
    }

    public void setCleanable(boolean cleanable) {
        this.cleanableProperty().set(cleanable);
    }

    /**
     * 选择类型
     */
    private ObjectProperty<DateType> dateType;
    public DateType getDateType() {
        return dateTypeProperty().get();
    }
    public ObjectProperty<DateType> dateTypeProperty() {
        if(dateType == null){
            dateType = FxKit.newProperty(DateType.DATETIME, StyleableProperties.DATE_TYPE, this, "dateType");
        }
        return dateType;
    }
    public void setDateType(DateType dateType) {
        this.dateTypeProperty().set(dateType);
    }

    /**
     * 下拉按钮是填充还是，边框，默认是填充
     */
    private BooleanProperty fillIcon;
    public boolean getFillIcon() {
        return fillIconProperty().get();
    }
    public BooleanProperty fillIconProperty() {
        if(fillIcon == null){
            fillIcon = FxKit.newBooleanProperty(false, StyleableProperties.FILL_ICON, this, "fillArrow");
        }
        return fillIcon;
    }
    public void setFillIcon(boolean fillIcon) {
        this.fillIconProperty().set(fillIcon);
    }

    /**
     * 默认显示的文本
     */
    private StringProperty promptText;
    public String getPromptText() {
        return promptTextProperty().get();
    }
    public StringProperty promptTextProperty() {
        if(promptText == null){
            promptText = new SimpleStringProperty(this, "promptText", null) {
                @Override protected void invalidated() {
                    // Strip out newlines
                    String txt = get();
                    if (txt != null && txt.contains("\n")) {
                        txt = txt.replace("\n", "");
                        set(txt);
                    }
                }
            };
        }
        return promptText;
    }
    public void setPromptText(String promptText) {
        this.promptTextProperty().set(promptText);
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
            hueType = FxKit.newProperty(HueType.DARK, XmDateSelector.StyleableProperties.HUE_TYPE, this, "hueType");
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
                    XmDateSelector.StyleableProperties.CLICK_ANIMATE_TYPE, this, "clickAnimateType");
        return clickAnimateType;
    }
    public void setClickAnimateType(ClickAnimateType clickAnimateType) {
        this.clickAnimateTypeProperty().set(clickAnimateType);
    }

    /* ----------------------------------- style -------------------------------------- */

    private static class StyleableProperties {

        /**
         * -fx-cleanable: true | false
         */
        private static final CssMetaData<XmDateSelector,Boolean> CLEANABLE = new CssMetaData<XmDateSelector, Boolean>(
                "-fx-cleanable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmDateSelector styleable) {
                return styleable.cleanable == null || !styleable.cleanable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmDateSelector styleable) {
                return (StyleableProperty<Boolean>) styleable.cleanableProperty();
            }
        };

        /**
         * 选择框类型，DATE/MONTH/YEAR/DATETIME
         * -fx-type-date: date | month | year | datetime
         */
        final static CssMetaData<XmDateSelector, DateType> DATE_TYPE =
                new CssMetaData<XmDateSelector, DateType>("-fx-type-date",
                        new EnumConverter<DateType>(DateType.class),
                        DateType.DATE, true) {
                    @Override
                    public boolean isSettable(XmDateSelector styleable) {
                        return styleable.dateType == null || !styleable.dateType.isBound();
                    }

                    @Override
                    public StyleableProperty<DateType> getStyleableProperty(XmDateSelector styleable) {
                        return (StyleableProperty<DateType>)styleable.dateTypeProperty();
                    }
                };

        /**
         * -fx-fill-arrow: true | false
         */
        private static final CssMetaData<XmDateSelector,Boolean> FILL_ICON = new CssMetaData<XmDateSelector, Boolean>(
                "-fx-fill-icon", BooleanConverter.getInstance(), false, true
        ) {
            @Override
            public boolean isSettable(XmDateSelector styleable) {
                return styleable.fillIcon == null || !styleable.fillIcon.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmDateSelector styleable) {
                return (StyleableProperty<Boolean>) styleable.fillIconProperty();
            }
        };

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmDateSelector, HueType> HUE_TYPE =
                new CssMetaData<XmDateSelector, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.DARK, true) {
                    @Override
                    public boolean isSettable(XmDateSelector styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmDateSelector styleable) {
                        return (StyleableProperty<HueType>)styleable.hueTypeProperty();
                    }
                };

        /**
         * 按钮点击动画定义
         * -fx-type-click-animate: ripper | shadow
         */
        final static CssMetaData<XmDateSelector, ClickAnimateType> CLICK_ANIMATE_TYPE =
                new CssMetaData<XmDateSelector, ClickAnimateType>(CssKeys.PropTypeClickAnimate,
                        new EnumConverter<ClickAnimateType>(ClickAnimateType.class),
                        ClickAnimateType.RIPPER, true) {
                    @Override
                    public boolean isSettable(XmDateSelector styleable) {
                        return styleable.clickAnimateType == null || !styleable.clickAnimateType.isBound();
                    }

                    @Override
                    public StyleableProperty<ClickAnimateType> getStyleableProperty(XmDateSelector styleable) {
                        return (StyleableProperty<ClickAnimateType>)styleable.clickAnimateTypeProperty();
                    }
                };

        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            Collections.addAll(styleables
                    , CLEANABLE
                    , DATE_TYPE
                    , FILL_ICON
                    ,HUE_TYPE
                    ,CLICK_ANIMATE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmDateSelector.StyleableProperties.STYLEABLES;
    }

}
