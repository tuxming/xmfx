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
package com.xm2013.jfx.control.data;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmTreeView<T> extends TreeView<T> {
    private static final String DEFAULT_CLASS = "xm-tree-view";
    public static String STYLESHEETS = FxKit.getResourceURL("/css/treeview.css");

    public XmTreeView(){
        super();
        this.init();
    }

    public XmTreeView(TreeItem<T> root) {
        super(root);
        this.init();
    }

    private void init() {
        this.getStyleClass().add(DEFAULT_CLASS);
        this.getStylesheets().add(STYLESHEETS);
    }


    @Override protected Skin<?> createDefaultSkin() {
        return new XmTreeViewSkin<T>(this);
    }

    /**
     * 获取checkboxcell的选中值，如果是常规的，请用getSelectionModel().getSelectionItem();
     * @param treeView  TreeView
     * @param <T> Object
     * @return List
     */
    public static <T> List<T> getSelectValues(TreeView<T> treeView){
        List<T> values = new ArrayList<>();
        traverseTree((CheckBoxTreeItem<T>)treeView.getRoot(), values);
        return values;
    }

    private static <T> void traverseTree(CheckBoxTreeItem<T> item, List<T> values) {
        if (item.isSelected()) {
            values.add(item.getValue());
        }
        for (TreeItem<T> child : item.getChildren()) {
            if (child instanceof CheckBoxTreeItem<?>) {
                traverseTree((CheckBoxTreeItem<T>) child, values);
            }
        }
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
            colorType = FxKit.newProperty(ColorType.primary(), XmTreeView.StyleableProperties.COLOR_TYPE, this, "colorType");
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
            sizeType = FxKit.newProperty(SizeType.MEDIUM, XmTreeView.StyleableProperties.SIZE_TYPE, this, "sizeType");
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
            hueType = FxKit.newProperty(HueType.DARK, XmTreeView.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }
    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {

        /**
         * 控件颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmTreeView, ColorType> COLOR_TYPE =
                new CssMetaData<XmTreeView, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmTreeView styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmTreeView styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };

        /**
         * 大小尺寸
         * -fx-type-color: small, medium, large
         */
        final static CssMetaData<XmTreeView, SizeType> SIZE_TYPE =
                new CssMetaData<XmTreeView, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        SizeType.MEDIUM, true) {
                    @Override
                    public boolean isSettable(XmTreeView styleable) {
                        return styleable.sizeType == null || !styleable.sizeType.isBound();
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(XmTreeView styleable) {
                        return (StyleableProperty<SizeType>) styleable.sizeTypeProperty();
                    }
                };

        /**
         * 控件色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmTreeView, HueType> HUE_TYPE =
                new CssMetaData<XmTreeView, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.DARK, true) {
                    @Override
                    public boolean isSettable(XmTreeView styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmTreeView styleable) {
                        return null;
                    }
                };


        // 创建一个CSS样式的表
        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(TreeView.getClassCssMetaData());
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
        return XmTreeView.StyleableProperties.STYLEABLES;
    }

}
