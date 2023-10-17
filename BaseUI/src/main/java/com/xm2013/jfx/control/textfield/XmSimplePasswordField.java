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
package com.xm2013.jfx.control.textfield;

import com.xm2013.jfx.common.FxKit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.StringConverter;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 简单的密码输入框
 */
public class XmSimplePasswordField extends XmSimpleTextField{

    private static final String USER_AGENT_STYLESHEET = FxKit.getResourceURL("/css/control.css");
    private static final String STYLE_CLASS_NAME = "xm-simple-password-field";
    public XmSimplePasswordField(){
        this(null);
    }

    public XmSimplePasswordField(String text){
        super();
        this.init(text);
    }

    public void init(String text){
        this.getStyleClass().add(STYLE_CLASS_NAME);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmSimplePasswordFieldSkin(this);
    }

    /* *************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Does nothing for PasswordField.
     */
    @Override public void cut() {
        // No-op
    }

    /**
     * Does nothing for PasswordField.
     */
    @Override public void copy() {
        // No-op
    }


    /* *************************************************************************
     *                                                                         *
     * Accessibility handling                                                  *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT: return null;
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /**
     * 是否显示密码
     */
    private BooleanProperty showPassword;
    public boolean isShowPassword() {
        return showPasswordProperty().get();
    }
    public void setShowPassword(boolean showPassword) {
        this.showPasswordProperty().set(showPassword);
    }
    public BooleanProperty showPasswordProperty() {
        if(showPassword==null){
            showPassword = FxKit.newBooleanProperty(false, XmSimplePasswordField.StyleableProperties.SHOW_PASSWORD, this, "showPassword");
        }
        return showPassword;
    }

    /**
     * 文本替代字符，默认是 *
     */
    private StringProperty echochar;
    public String getEchochar() {
        return echocharProperty().get();
    }
    public void setEchochar(String echochar) {
        this.echocharProperty().set(echochar);
    }
    public StringProperty echocharProperty() {
        if(echochar == null){
            echochar = FxKit.newStringProperty("*", XmSimplePasswordField.StyleableProperties.ECHOCHAR, this, "echochar");
        }
        return echochar;
    }

    public static class StyleableProperties {
        public static final CssMetaData<XmSimplePasswordField, String> ECHOCHAR = new CssMetaData<XmSimplePasswordField, String>(
                "-fx-echochar", StringConverter.getInstance(), "*") {

            @Override
            public StyleableProperty<String> getStyleableProperty(XmSimplePasswordField control) {
                return (StyleableProperty<String>) control.echocharProperty();
            }

            @Override
            public boolean isSettable(XmSimplePasswordField control) {
                return control.echocharProperty() == null || !control.echocharProperty().isBound();
            }
        };
        // 按钮显示
        private static final CssMetaData<XmSimplePasswordField, Boolean> SHOW_PASSWORD = new CssMetaData<XmSimplePasswordField, Boolean>(
                "-fx-show-password", BooleanConverter.getInstance(), false) {
            @Override
            public boolean isSettable(XmSimplePasswordField control) {
                return control.showPassword == null || !control.showPassword.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmSimplePasswordField control) {
                return (StyleableProperty<Boolean>) control.showPasswordProperty();
            }
        };

        // 创建一个CSS样式的表
        protected static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(PasswordField.getClassCssMetaData());
            Collections.addAll(styleables, ECHOCHAR, SHOW_PASSWORD);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmSimplePasswordField.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    class XmSimplePasswordFieldSkin extends XmSimpleTextFieldSkin {
        private XmSimplePasswordField control;
        public XmSimplePasswordFieldSkin(XmSimplePasswordField control) {
            super(control);
            this.control = control;
        }

        @Override
        protected String maskText(String txt) {
//        System.out.println("exec maskText:"+txt);
            if(control!=null && control.isShowPassword()){
//            System.out.println("111");
                return txt;
            } else {
                int n = txt.length();
                StringBuilder passwordBuilder = new StringBuilder(n);
                String echo = "*";
                if(control!=null){
                    echo = control.getEchochar();
                }
                for (int i = 0; i < n; i++) {

                    if(control == null){

                    }
                    passwordBuilder.append(echo);
                }

//            System.out.println("222:"+passwordBuilder.toString());
                return passwordBuilder.toString();
            }
        }
    }

}
