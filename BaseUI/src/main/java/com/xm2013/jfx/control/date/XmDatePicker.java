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
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class XmDatePicker extends XmControl {

    private static final String USER_AGENT_STYLESHEET = FxKit.USER_AGENT_STYLESHEET;
    private static final String DEFAULT_STYLE_CLASS = "xm-date-picker";
    
    private static final String DEFAULT_MONTH_PATTERN = "yyyy-MM";
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public XmDatePicker(){
        this(null);
    }

    /**
     * @param date 要显示的日期
     */
    public XmDatePicker(Date date){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.DATE_PICKER);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmDatePickerSkin(this);
    }

    /**
     * 设置选择的日期，如果没设置，默认为当前月
     */
    private ObjectProperty<LocalDateTime> selectedDate;
    public LocalDateTime getSelectedDate() {
        return selectedDateProperty().get() == null?LocalDateTime.now(): selectedDateProperty().get();
    }
    public ObjectProperty<LocalDateTime> selectedDateProperty() {
        if(selectedDate == null){
            selectedDate = new SimpleObjectProperty<>(null);
        }
        return selectedDate;
    }
    public void setSelectedDate(LocalDateTime selectedDate) {
        this.selectedDateProperty().set(selectedDate);
    }

    /**
     * 设置要显示的日期，如果没设置，默认为当前月
     */
    private ObjectProperty<LocalDateTime> date;
    public LocalDateTime getDate() {
        return dateProperty().get() == null?LocalDateTime.now():dateProperty().get();
    }
    public ObjectProperty<LocalDateTime> dateProperty() {
        if(date == null){
            date = new SimpleObjectProperty<>(null);
        }
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.dateProperty().set(date);
    }

    /**
     * 显示格式
     */
    private StringProperty formatPattern = null;
    public String getFormatPattern() {
        String pattern =  formatPattern.get();
        if(pattern == null){
            DateType type = getDateType();
            if(type.equals(DateType.MONTH)){
                return DEFAULT_MONTH_PATTERN;
            }else if(type.equals(DateType.DATETIME)){
                return DEFAULT_DATETIME_PATTERN;
            }else{
                return DEFAULT_DATE_PATTERN;
            }
        }
        return pattern;
    }
    public StringProperty formatPatternProperty() {
        if(formatPattern == null){
            formatPattern = new SimpleStringProperty(null);
        }
        return formatPattern;
    }
    public void setFormatPattern(String formatPattern) {
        this.formatPattern.set(formatPattern);
    }

    /**
     * 日期类型
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

    private static class StyleableProperties {

        /**
         * 选择框类型，DATE/MONTH/YEAR/DATETIME
         * -fx-type-date: date | month | year | datetime
         */
        final static CssMetaData<XmDatePicker, DateType> DATE_TYPE =
                new CssMetaData<XmDatePicker, DateType>("-fx-type-date",
                        new EnumConverter<DateType>(DateType.class),
                        DateType.DATE, true) {
                    @Override
                    public boolean isSettable(XmDatePicker styleable) {
                        return styleable.dateType == null || !styleable.dateType.isBound();
                    }

                    @Override
                    public StyleableProperty<DateType> getStyleableProperty(XmDatePicker styleable) {
                        return (StyleableProperty<DateType>)styleable.dateTypeProperty();
                    }
                };


        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Pane.getClassCssMetaData());
            Collections.addAll(styleables
                    , DATE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmDatePicker.StyleableProperties.STYLEABLES;
    }

}
