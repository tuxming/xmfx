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
package com.xm2013.jfx.control.tab;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.*;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.PaintConverter;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * tab头
 */
public class XmTab extends XmControl {
    private static PseudoClass active = PseudoClass.getPseudoClass("active");
    private static String DEFAULT_CLASS = "xm-tab";

    public XmTab(String text, Node graphic){
        this(text, graphic, null);
    }

    public XmTab(String text, Node graphic, Node content){
        this.init(text, graphic, content);
    }

    private void init(String text, Node graphic, Node content){
        setText(text);
        setGraphic(graphic);
        setContent(content);
        setAccessibleRole(AccessibleRole.TAB_ITEM);
        getStyleClass().add(DEFAULT_CLASS);
        getStylesheets().add(FxKit.USER_AGENT_STYLESHEET);

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmTabSkin(this);
    }

    /* ------------------------------------ property ----------------------------- */

    /**
     * tab的id,根据key来判断tab的唯一性
     */
    private StringProperty key;
    public String getKey() {
        return keyProperty().get();
    }
    public StringProperty keyProperty() {
        if(key == null){
            key = new SimpleStringProperty();
        }
        return key;
    }
    public void setKey(String key) {
        this.keyProperty().set(key);
    }

    /**
     * 要显示的文本
     */
    private StringProperty text;
    public String getText() {
        return textProperty().get();
    }
    public StringProperty textProperty() {
        if(text == null) text = new SimpleStringProperty(null);
        return text;
    }
    public void setText(String text) {
        this.textProperty().set(text);
    }

    /**
     * 图标
     */
    private ObjectProperty<Node> graphic;
    public Node getGraphic() {
        return graphicProperty().get();
    }
    public ObjectProperty<Node> graphicProperty() {
        if(graphic == null) graphic = new SimpleObjectProperty<>(null);
        return graphic;
    }
    public void setGraphic(Node graphic) {
        this.graphicProperty().set(graphic);
    }

    /**
     * 是否可以关闭
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
     * 是否选中
     */
    private BooleanProperty selected;
    public boolean isSelected() {
        return selectedProperty().get();
    }
    public BooleanProperty selectedProperty() {
        if(selected == null){
            selected = new StyleableBooleanProperty(false) {
                @Override
                public Object getBean() {
                    return XmTab.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }

                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SELECTED;
                }

                @Override
                protected void invalidated() {
                    super.invalidated();
                    pseudoClassStateChanged(active, get());
                }
            };
        }
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selectedProperty().set(selected);
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
            hueType = FxKit.newProperty(HueType.LIGHT, XmTab.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }

    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }

    /**
     * 文本颜色
     */
    private ObjectProperty<Paint> fontColor;
    public Paint getFontColor() {
        return fontColorProperty().get();
    }
    public ObjectProperty<Paint> fontColorProperty() {
        if(fontColor == null){
            fontColor = FxKit.newProperty(null, StyleableProperties.FONT_COLOR, this, "fontColor");
        }
        return fontColor;
    }
    public void setFontColor(Paint fontColor) {
        this.fontColorProperty().set(fontColor);
    }

    /**
     * 点击关闭的回调
     */
    private ObjectProperty<CallBack<MouseEvent>> closeAction;
    public CallBack<MouseEvent> getCloseAction() {
        return closeActionProperty().get();
    }
    public ObjectProperty<CallBack<MouseEvent>> closeActionProperty() {
        if(closeAction == null){
            closeAction = new SimpleObjectProperty<>(null);
        }
        return closeAction;
    }
    public void setCloseAction(CallBack<MouseEvent> closeAction) {
        this.closeActionProperty().set(closeAction);
    }

    /**
     * 点击回调
     */
    private ObjectProperty<CallBack<MouseEvent>> clickAction;
    public CallBack<MouseEvent> getClickAction() {
        return clickActionProperty().get();
    }
    public ObjectProperty<CallBack<MouseEvent>> clickActionProperty() {
        if(clickAction == null){
            clickAction = new SimpleObjectProperty<>();
        }
        return clickAction;
    }

    public void setClickAction(CallBack<MouseEvent> clickAction) {
        this.clickActionProperty().set(clickAction);
    }

    /**
     * 显示页面
     */
    private ObjectProperty<Node> content;
    public Node getContent() {
        return contentProperty().get();
    }
    public ObjectProperty<Node> contentProperty() {
        if(content == null){
            content = new SimpleObjectProperty<>(null);
        }
        return content;
    }
    public void setContent(Node content) {
        this.contentProperty().set(content);
    }

    /* ----------------------- style / css --------------------------------------- */
    private static class StyleableProperties {

        /**
         * -fx-closeable: true | false
         */
        private static final CssMetaData<XmTab,Boolean> CLOSEABLE = new CssMetaData<XmTab, Boolean>(
                "-fx-closeable", BooleanConverter.getInstance(), false, true
        ) {
            @Override
            public boolean isSettable(XmTab styleable) {
                return styleable.closeable == null || !styleable.closeable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmTab styleable) {
                return (StyleableProperty<Boolean>) styleable.closeableProperty();
            }
        };

        /**
         * -fx-selected: true | false
         */
        private static final CssMetaData<XmTab,Boolean> SELECTED = new CssMetaData<XmTab, Boolean>(
                "-fx-selected", BooleanConverter.getInstance(), false, true
        ) {
            @Override
            public boolean isSettable(XmTab styleable) {
                return styleable.selected == null || !styleable.selected.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmTab styleable) {
                return (StyleableProperty<Boolean>) styleable.selectedProperty();
            }
        };

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmTab, HueType> HUE_TYPE =
                new CssMetaData<XmTab, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.LIGHT, true) {
                    @Override
                    public boolean isSettable(XmTab styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmTab styleable) {
                        return (StyleableProperty<HueType>)styleable.hueTypeProperty();
                    }
                };

        final static CssMetaData<XmTab, Paint> FONT_COLOR = new CssMetaData<XmTab, Paint>("-fx-font-color", PaintConverter.getPaintConverter(), null) {
            @Override
            public boolean isSettable(XmTab styleable) {
                return styleable.fontColor == null || !styleable.fontColor.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(XmTab styleable) {
                return (StyleableProperty<Paint>) styleable.fontColorProperty();
            }
        };

        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            Collections.addAll(styleables
                    , CLOSEABLE
                    , SELECTED
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
        return XmTab.StyleableProperties.STYLEABLES;
    }

}
