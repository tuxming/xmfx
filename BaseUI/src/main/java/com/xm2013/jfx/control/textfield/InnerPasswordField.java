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
import javafx.scene.AccessibleRole;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 这里不知直接提供使用，建议使用方式
 * XmTextField field = new XmTextField();
 * field.setInputType(XmTextInputType.PASSWORD);
 */
public class InnerPasswordField extends InnerTextField {

    public InnerPasswordField() {
        getStyleClass().add("password-field");
        setAccessibleRole(AccessibleRole.PASSWORD_FIELD);
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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new InnerPasswordFieldSkin(this);
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
            showPassword = FxKit.newBooleanProperty(false, StyleableProperties.SHOW_PASSWORD, this, "showPassword");
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
            echochar = FxKit.newStringProperty("*", StyleableProperties.ECHOCHAR, this, "echochar");
        }
        return echochar;
    }

    public static class StyleableProperties {
        public static final CssMetaData<InnerPasswordField, String> ECHOCHAR = new CssMetaData<InnerPasswordField, String>(
                "-fx-echochar", StringConverter.getInstance(), "*") {

            @Override
            public StyleableProperty<String> getStyleableProperty(InnerPasswordField control) {
                return (StyleableProperty<String>) control.echocharProperty();
            }

            @Override
            public boolean isSettable(InnerPasswordField control) {
                return control.echocharProperty() == null || !control.echocharProperty().isBound();
            }
        };
        // 按钮显示
        private static final CssMetaData<InnerPasswordField, Boolean> SHOW_PASSWORD = new CssMetaData<InnerPasswordField, Boolean>(
                "-fx-show-password", BooleanConverter.getInstance(), false) {
            @Override
            public boolean isSettable(InnerPasswordField control) {
                return control.showPassword == null || !control.showPassword.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(InnerPasswordField control) {
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
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
    
}
