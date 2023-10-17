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
package com.xm2013.jfx.control.label;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmTag extends XmLabel {
    private static final String USER_AGENT_STYLESHEET = FxKit.getResourceURL("/css/control.css");

    public XmTag(){
        this(null);
    }

    public XmTag(String text){
        this(text, null);
    }

    public XmTag(String text, Node graphic){
        super(text, graphic);
        init();
    }

    private void init(){
        getStyleClass().setAll("xm-tag");
        setAccessibleRole(AccessibleRole.TEXT);
        getStylesheets().add(USER_AGENT_STYLESHEET);
    }

    protected Skin<?> createDefaultSkin() {
        return new XmTagSkin(this);
    }

    /**
     * 点击关闭的时候的回调函数
     */
    private CallBack closeCallback;
    public void setCloseCallback(CallBack callback){
        this.closeCallback = callback;
    }
    public CallBack getCloseCallback(){
        return this.closeCallback;
    }
    
    private CallBack<String> editEnterCallback;
    public CallBack<String> getEditEnterCallback() {
		return editEnterCallback;
	}

	public void setEditEnterCallback(CallBack<String> editEnterCallback) {
		this.editEnterCallback = editEnterCallback;
	}

	/**
     * 是否可以关闭标签
     */
    private BooleanProperty closeable;
    public boolean isCloseable() {
        return closeableProperty().get();
    }
    public BooleanProperty closeableProperty() {
        if(closeable == null){
            closeable = FxKit.newBooleanProperty(false, StyleableProperties.CLOSEABLE, this, "closeable");
        }
        return closeable;
    }
    public void setCloseable(boolean closeable) {
        this.closeableProperty().set(closeable);
    }

    /**
     * 是否可以编辑
     */
    private BooleanProperty editable;
    public boolean isEditable() {
        return editableProperty().get();
    }
    public BooleanProperty editableProperty() {
        if(editable == null)
            editable = FxKit.newBooleanProperty(false, StyleableProperties.EDITABLE, this, "editable");
        return editable;
    }
    public void setEditable(boolean editable) {
        this.editableProperty().set(editable);
    }

    private ObjectProperty<HueType> hueType;
    public HueType getHueType() {
        return hueTypeProperty().get();
    }

    public ObjectProperty<HueType> hueTypeProperty() {
        if(hueType == null)
            hueType = FxKit.newProperty(HueType.DARK, StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }

    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {

        /**
         * -fx-closeable: true | false
         */
        private static final CssMetaData<XmTag,Boolean> CLOSEABLE = new CssMetaData<XmTag, Boolean>(
                "-fx-closeable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmTag styleable) {
                return styleable.closeable == null || !styleable.closeable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmTag styleable) {
                return (StyleableProperty<Boolean>) styleable.closeableProperty();
            }
        };
        /**
         * -fx-editable: true | false
         */
        private static final CssMetaData<XmTag,Boolean> EDITABLE = new CssMetaData<XmTag, Boolean>(
                "-fx-editable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmTag styleable) {
                return styleable.editable == null || !styleable.editable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmTag styleable) {
                return (StyleableProperty<Boolean>) styleable.editableProperty();
            }
        };


        /**
         * 标签的对齐方式
         * -fx-type-hue: dark | light | none
         */
        private static final CssMetaData<XmTag, HueType> HUE_TYPE =
                new CssMetaData<XmTag,HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class), HueType.DARK ) {

                    @Override
                    public boolean isSettable(XmTag n) {
                        return n.hueType == null || !n.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmTag n) {
                        return (StyleableProperty<HueType>)(WritableValue<HueType>)n.hueTypeProperty();
                    }

                    @Override
                    public HueType getInitialValue(XmTag n) {
                        return n.hueTypeProperty().get();
                    }
                };


        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmLabel.getClassCssMetaData());
            Collections.addAll(styleables
                    ,CLOSEABLE
                    ,EDITABLE
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
        return XmTag.StyleableProperties.STYLEABLES;
    }

}
