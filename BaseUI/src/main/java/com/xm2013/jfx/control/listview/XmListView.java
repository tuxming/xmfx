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
package com.xm2013.jfx.control.listview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmListView<T> extends ListView<T> {
    private static final String DEFAULT_CLASS = "xm-list-view";

    public XmListView(){
        super();
        this.init();
    }

    public XmListView(ObservableList<T> items) {
        super(items);
        this.init();
    }

    private void init() {
        this.getStyleClass().add(DEFAULT_CLASS);
//        this.getStylesheets().add(STYLESHEETS);
    }

    @Override protected Skin<?> createDefaultSkin() {
        return new XmListViewSkin<T>(this);
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
            colorType = FxKit.newProperty(ColorType.primary(), XmListView.StyleableProperties.COLOR_TYPE, this, "colorType");
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
            sizeType = FxKit.newProperty(SizeType.MEDIUM, XmListView.StyleableProperties.SIZE_TYPE, this, "sizeType");
        }
        return sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeTypeProperty().set(sizeType);
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
            hueType = FxKit.newProperty(HueType.DARK, XmListView.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }
    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }


    /**
     * 当使用XmCheckBoxListCell的时候，可通过此方法获取选中的数据
     */
    private ObservableList<T> checkedValues = FXCollections.observableArrayList();
    public ObservableList<T> getCheckedValues() {
        return checkedValues;
    }
    public void setCheckedValues(List<T> checkedValues) {
        this.checkedValues.setAll(checkedValues);
    }
    public void addCheckedValue(T ...value){
        this.checkedValues.addAll(value);
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {

        /**
         * 控件颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmListView, ColorType> COLOR_TYPE =
                new CssMetaData<XmListView, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmListView styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmListView styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };

        /**
         * 大小尺寸
         * -fx-type-color: small, medium, large
         */
        final static CssMetaData<XmListView, SizeType> SIZE_TYPE =
                new CssMetaData<XmListView, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        SizeType.MEDIUM, true) {
                    @Override
                    public boolean isSettable(XmListView styleable) {
                        return styleable.sizeType == null || !styleable.sizeType.isBound();
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(XmListView styleable) {
                        return (StyleableProperty<SizeType>) styleable.sizeTypeProperty();
                    }
                };

        /**
         * 控件色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmListView, HueType> HUE_TYPE =
                new CssMetaData<XmListView, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.DARK, true) {
                    @Override
                    public boolean isSettable(XmListView styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmListView styleable) {
                        return (StyleableProperty<HueType>) styleable.hueTypeProperty();
                    }
                };


        // 创建一个CSS样式的表
        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(ListView.getClassCssMetaData());
            Collections.addAll(styleables
//                    ,TYPE
                    ,SIZE_TYPE
                    ,COLOR_TYPE
                    ,HUE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmListView.StyleableProperties.STYLEABLES;
    }

}
