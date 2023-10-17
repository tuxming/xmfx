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
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.XmAlignment;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.*;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.css.converter.StringConverter;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自对应文本框，是基于TextField重新布局的组件，
 * 要设置边距请使用 margin属性
 */
public class XmTextField extends XmControl {

    private static final String USER_AGENT_STYLESHEET = FxKit.getResourceURL("/css/control.css");
    private static final String STYLE_CLASS_NAME = "xm-text-field";

    public XmTextField(){
        this(null);
    }

    public XmTextField(String text){
        super();
        this.init(text);
    }

    public void init(String text){
        this.getStyleClass().add(STYLE_CLASS_NAME);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);
//        this.textField.setText(text);
        this.getField().setText(text);
    }

    /*----------------------override---------------------------*/

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmTextFieldSkin(this);
    }
    /*----------------------properties-------------------------*/
    private ObjectProperty<InnerTextField> field;
    public ObjectProperty<InnerTextField> fieldProperty() {
        if(field == null){
            if(getInputType().equals(XmTextInputType.PASSWORD)){
                field = new SimpleObjectProperty<>(new InnerPasswordField());
            }else{
                field = new SimpleObjectProperty<>(new InnerTextField());
            }
        }
        return field;
    }
    public void setField(InnerTextField field) {
        this.fieldProperty().set(field);
    }
    public TextField getField(){
        return fieldProperty().get();
    }
    public String getText(){
        return getField().getText();
    }
    public void setText(String text){ getField().setText(text);};
    public StringProperty textProperty(){
        return getField().textProperty();
    }

    /**
     * 前缀图标
     */
    private ObjectProperty<Node> prefix;
    public Node getPrefix() {
        return prefixProperty().get();
    }
    public ObjectProperty<Node> prefixProperty() {
        if(prefix == null){
            prefix = new SimpleObjectProperty<>();
        }
        return prefix;
    }
    public void setPrefix(Node prefix) {
        this.prefixProperty().set(prefix);
    }

    /**
     * 后缀图标
     */
    private ObjectProperty<Node> suffix;
    public Node getSuffix() {
        return suffixProperty().get();
    }

    public ObjectProperty<Node> suffixProperty() {
        if(suffix == null) suffix = new SimpleObjectProperty<>();
        return suffix;
    }

    public void setSuffix(Node suffix) {
        this.suffixProperty().set(suffix);
    }

    /**
     * label
     */
    private StringProperty label;
    public String getLabel() {
        return labelProperty().get();
    }

    public StringProperty labelProperty() {
        if(label == null) label = new SimpleStringProperty();
        return label;
    }

    public void setLabel(String label) {
        this.labelProperty().set(label);
    }

    /**
     * label的宽度
     */
    private DoubleProperty labelWidth;
    public double getLabelWidth() {
        return labelWidthProperty().get();
    }

    public DoubleProperty labelWidthProperty() {
        if(labelWidth == null) labelWidth = FxKit.newDoubleProperty(-1d, StyleableProperties.LABEL_WIDTH, this, "labelWidth");
        return labelWidth;
    }

    /**
     * 占位符
     */
    private StringProperty placeHolder;
    public String getPlaceHolder() {
        return placeHolderProperty().get();
    }
    public StringProperty placeHolderProperty() {
        if(placeHolder==null)
            placeHolder = new SimpleStringProperty(null);
        return placeHolder;
    }
    public void setPlaceHolder(String placeHolder) {
        this.placeHolderProperty().set(placeHolder);
    }

    private ObjectProperty<Node> labelIcon;
    public Node getLabelIcon() {
        return labelIconProperty().get();
    }
    public ObjectProperty<Node> labelIconProperty() {
        if(labelIcon == null){
            labelIcon = new SimpleObjectProperty<>(null);
        }
        return labelIcon;
    }
    public void setLabelIcon(Node labelIcon) {
        this.labelIconProperty().set(labelIcon);
    }

    public void setLabelWidth(double labelWidth) {
        this.labelWidthProperty().set(labelWidth);
    }

    /**
     * label 当设置了 宽，高以后，实际类容小于宽高的时候，设置实际内容相对于容器的对齐方式
     * 默认： XmAlignment.LEFT
     * @return ObjectProperty
     */
    public final ObjectProperty<XmAlignment> labelAlignmentProperty() {
        if (labelAlignment == null) {
            labelAlignment = FxKit.newProperty(XmAlignment.LEFT, StyleableProperties.LABEL_ALIGNMENT, this, "alignment");
        }
        return labelAlignment;
    }
    private ObjectProperty<XmAlignment> labelAlignment;
    public final void setAlignment(XmAlignment value) { labelAlignmentProperty().set(value); }
    public final XmAlignment getAlignment() { return labelAlignment == null ? XmAlignment.LEFT : labelAlignment.get(); }

    /**
     * 是否可以清除
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
     * 风格
     */
    private ObjectProperty<XmFieldDisplayType> displayType;
    public XmFieldDisplayType getDisplayType() {
        return displayTypeProperty().get();
    }
    public ObjectProperty<XmFieldDisplayType> displayTypeProperty() {
        if(displayType == null){
            displayType = FxKit.newProperty(XmFieldDisplayType.HORIZONTAL_INLINE, StyleableProperties.DISPLAY_TYPE, this, "displayType");
        }
        return displayType;
    }
    public void setDisplayType(XmFieldDisplayType displayType) {
        this.displayTypeProperty().set(displayType);
    }

    /**
     * 文本框类型， TEXT(文本)， PASSWORD(密码)， NUMBER(数字)
     */
    private ObjectProperty<XmTextInputType> inputType;
    public XmTextInputType getInputType() {
        return inputTypeProperty().get();
    }
    public ObjectProperty<XmTextInputType> inputTypeProperty() {
        if(inputType == null){
            inputType = FxKit.newProperty(XmTextInputType.TEXT, StyleableProperties.INPUT_TYPE, this, "inputType");
        }
        return inputType;
    }
    public void setInputType(XmTextInputType inputType) {
        this.inputTypeProperty().set(inputType);
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
            showPassword = FxKit.newBooleanProperty(false, XmTextField.StyleableProperties.SHOW_PASSWORD, this, "showPassword");
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
            echochar = FxKit.newStringProperty("*", XmTextField.StyleableProperties.ECHOCHAR, this, "echochar");
        }
        return echochar;
    }
    
    public void setMargin(Insets margin) {
        this.marginProperty().set(margin);
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {


        /**
         * 排版类型
         * -fx-type-display: horizontal-inline | horizontal-outline | vertical-inline | vertical-outline
         */
        final static CssMetaData<XmTextField, XmFieldDisplayType> DISPLAY_TYPE =
                new CssMetaData<XmTextField, XmFieldDisplayType>(CssKeys.PropTypeDisplay,
                        new EnumConverter<XmFieldDisplayType>(XmFieldDisplayType.class),
                        XmFieldDisplayType.HORIZONTAL_OUTLINE, true) {
                    @Override
                    public boolean isSettable(XmTextField styleable) {
                        return styleable.displayType == null || !styleable.displayType.isBound();
                    }

                    @Override
                    public StyleableProperty<XmFieldDisplayType> getStyleableProperty(XmTextField styleable) {
                        return (StyleableProperty<XmFieldDisplayType>) styleable.displayTypeProperty();
                    }
                };

        private static final CssMetaData<XmTextField,Boolean> CLEANABLE = new CssMetaData<XmTextField, Boolean>(
                "-fx-cleanable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmTextField styleable) {
                return styleable.cleanable == null || !styleable.cleanable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmTextField styleable) {
                return (StyleableProperty<Boolean>) styleable.cleanableProperty();
            }
        };

        /**
         * 标签宽度
         * -fx-label-width
         */
        private static final CssMetaData<XmTextField, Number> LABEL_WIDTH =
                new CssMetaData<XmTextField,Number>("-fx-label-width",
                        SizeConverter.getInstance(), USE_COMPUTED_SIZE){

                    @Override public boolean isSettable(XmTextField node) {
                        return node.labelWidth == null ||
                                !node.labelWidth.isBound();
                    }

                    @Override public StyleableProperty<Number> getStyleableProperty(XmTextField node) {
                        return (StyleableProperty<Number>)node.labelWidthProperty();
                    }
                };

        /**
         * 标签的对齐方式
         * -fx-alignment: left, center, right, justify
         */
        private static final CssMetaData<XmTextField,XmAlignment> LABEL_ALIGNMENT =
                new CssMetaData<XmTextField,XmAlignment>("-fx-label-align",
                        new EnumConverter<XmAlignment>(XmAlignment.class), XmAlignment.LEFT ) {

                    @Override
                    public boolean isSettable(XmTextField n) {
                        return n.labelAlignment == null || !n.labelAlignment.isBound();
                    }

                    @Override
                    public StyleableProperty<XmAlignment> getStyleableProperty(XmTextField n) {
                        return (StyleableProperty<XmAlignment>)(WritableValue<XmAlignment>)n.labelAlignmentProperty();
                    }

                    @Override
                    public XmAlignment getInitialValue(XmTextField n) {
                        return n.labelAlignmentProperty().get();
                    }
                };

        /**
         * 输入类型
         * -fx-type-input: text | password | number
         */
        final static CssMetaData<XmTextField, XmTextInputType> INPUT_TYPE =
                new CssMetaData<XmTextField, XmTextInputType>(CssKeys.PropTypeInput,
                        new EnumConverter<XmTextInputType>(XmTextInputType.class),
                        XmTextInputType.TEXT, true) {
                    @Override
                    public boolean isSettable(XmTextField styleable) {
                        return styleable.inputType == null || !styleable.inputType.isBound();
                    }

                    @Override
                    public StyleableProperty<XmTextInputType> getStyleableProperty(XmTextField styleable) {
                        return (StyleableProperty<XmTextInputType>) styleable.inputTypeProperty();
                    }
                };

        public static final CssMetaData<XmTextField, String> ECHOCHAR = new CssMetaData<XmTextField, String>(
                "-fx-echochar", StringConverter.getInstance(), "*") {

            @Override
            public StyleableProperty<String> getStyleableProperty(XmTextField control) {
                return (StyleableProperty<String>) control.echocharProperty();
            }

            @Override
            public boolean isSettable(XmTextField control) {
                return control.echocharProperty() == null || !control.echocharProperty().isBound();
            }
        };
        // 按钮显示
        private static final CssMetaData<XmTextField, Boolean> SHOW_PASSWORD = new CssMetaData<XmTextField, Boolean>(
                "-fx-show-password", BooleanConverter.getInstance(), false) {
            @Override
            public boolean isSettable(XmTextField control) {
                return control.showPassword == null || !control.showPassword.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmTextField control) {
                return (StyleableProperty<Boolean>) control.showPasswordProperty();
            }
        };
        
        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables
                    ,DISPLAY_TYPE
                    ,CLEANABLE
                    ,LABEL_ALIGNMENT
                    ,LABEL_WIDTH
                    ,INPUT_TYPE
                    ,SHOW_PASSWORD
                    ,ECHOCHAR
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmTextField.StyleableProperties.STYLEABLES;
    }

}
