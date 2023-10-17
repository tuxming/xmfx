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
package com.xm2013.jfx.control.pager;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页组件
 */
public class XmPager extends XmControl {

    public XmPager(){
        this(50);
    }

    /**
     * 总条数
     * @param total int
     */
    public XmPager(int total) {
        super();
        this.init(total);
    }

    private void init(int total){
        setTotal(total);
        getStyleClass().add("xm-pager");
        getStylesheets().add(FxKit.USER_AGENT_STYLESHEET);
        setAccessibleRole(AccessibleRole.PAGINATION);
        setHueType(HueType.LIGHT);
        setSizeType(SizeType.SMALL);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmPagerSkin(this);
    }

    private IntegerProperty total = new SimpleIntegerProperty(500);
    public int getTotal() {
        return total.get();
    }
    public IntegerProperty totalProperty() {
        return total;
    }
    public void setTotal(int total) {
        this.total.set(total);
    }

    private IntegerProperty length = new SimpleIntegerProperty(10);
    public int getLength() {
        return length.get();
    }
    public IntegerProperty lengthProperty() {
        return length;
    }
    public void setLength(int length) {
        this.length.set(length);
    }

    private IntegerProperty currIndex = new SimpleIntegerProperty(0);
    public int getCurrIndex() {
        return currIndex.get();
    }
    public IntegerProperty currIndexProperty() {
        return currIndex;
    }
    public void setCurrIndex(int currIndex) {
        this.currIndex.set(currIndex);
    }

    /**
     * 每页显示的条数
     */
    private ObservableList<Integer> sizes = FXCollections.observableArrayList(10,20, 50,100,200,500,1000,2000,5000,10000);
    public ObservableList<Integer> getSizes() {
        return sizes;
    }
    public void setSizes(ObservableList<Integer> sizes) {
        this.sizes = sizes;
    }

    /**
     * 是不是在一行显示
     */
    private BooleanProperty singleLine;
    public boolean isSingleLine() {
        return singleLineProperty().get();
    }
    public BooleanProperty singleLineProperty() {
        if(singleLine == null){
            singleLine = FxKit.newBooleanProperty(true, StyleableProperties.SINGLE_LINE, this, "singleLine");
        }
        return singleLine;
    }
    public void setSingleLine(boolean singleLine) {
        this.singleLineProperty().set(singleLine);
    }

    /**
     * 是不是显示快速跳转
     */
    private BooleanProperty showQuickJumper;
    public boolean isShowQuickJumper() {
        return showQuickJumperProperty().get();
    }
    public BooleanProperty showQuickJumperProperty() {
        if(showQuickJumper == null){
            showQuickJumper = FxKit.newBooleanProperty(true, StyleableProperties.SHOW_QUICK_JUMPER, this, "showQuickJumper");
        }
        return showQuickJumper;
    }
    public void setShowQuickJumper(boolean showQuickJumper) {
        this.showQuickJumperProperty().set(showQuickJumper);
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
            hueType = FxKit.newProperty(HueType.LIGHT, XmPager.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }

    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }

    /* ----------------------------------- style -------------------------------------- */

    private static class StyleableProperties {

        /**
         * -fx-single-line: true | false
         */
        private static final CssMetaData<XmPager,Boolean> SINGLE_LINE = new CssMetaData<XmPager, Boolean>(
                "-fx-single-line", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmPager styleable) {
                return styleable.singleLine == null || !styleable.singleLine.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmPager styleable) {
                return (StyleableProperty<Boolean>) styleable.singleLineProperty();
            }
        };

        /**
         * -fx-show-quick-jumper: true | false
         */
        private static final CssMetaData<XmPager,Boolean> SHOW_QUICK_JUMPER = new CssMetaData<XmPager, Boolean>(
                "-fx-show-quick-jumper", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmPager styleable) {
                return styleable.showQuickJumper == null || !styleable.showQuickJumper.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmPager styleable) {
                return (StyleableProperty<Boolean>) styleable.showQuickJumperProperty();
            }
        };

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmPager, HueType> HUE_TYPE =
                new CssMetaData<XmPager, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.LIGHT, true) {
                    @Override
                    public boolean isSettable(XmPager styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmPager styleable) {
                        return (StyleableProperty<HueType>)styleable.hueTypeProperty();
                    }
                };

        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            Collections.addAll(styleables
                    , SINGLE_LINE
                    , SHOW_QUICK_JUMPER
                    , HUE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmPager.StyleableProperties.STYLEABLES;
    }

}
