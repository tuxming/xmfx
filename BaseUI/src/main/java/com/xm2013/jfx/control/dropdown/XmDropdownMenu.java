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
package com.xm2013.jfx.control.dropdown;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.event.Event;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xm2013.jfx.control.dropdown.XmDropdownMenuSkin.ON_HIDING;
import static com.xm2013.jfx.control.dropdown.XmDropdownMenuSkin.ON_SHOWING;

public class XmDropdownMenu extends XmControl {

    private static final String USER_AGENT_STYLESHEET = FxKit.getResourceURL("/css/control.css");
    private static final String STYLE_CLASS_NAME = "xm-dropdown-menu";

    private static final PseudoClass PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");

    /*------------------------------ Constructor / init -----------------------------*/

    public XmDropdownMenu(){
        this(null, null, null);
    }

    public XmDropdownMenu(String text){
        this(text, null,null);
    }

    public XmDropdownMenu(String text, Node icon){
        this(text, icon, null);
    }

    public XmDropdownMenu(String text, Node icon, List<DropdownMenuItem> items){
        super();
        init(text, icon, items);
    }

    private void init(String text, Node icon, List<DropdownMenuItem> items){

        this.textProperty().set(text);

        if(icon!=null){
            iconProperty().set(icon);
        }

        if(items!=null){
            this.items.addAll(items);
        }

        this.getStyleClass().add(STYLE_CLASS_NAME);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);
        this.setAccessibleRole(AccessibleRole.COMBO_BOX);
    }


    /* -------------------------------- property --------------------------*/
    /**
     * 显示的文本
     */
    private ObjectProperty<String> text;
    public String getText() {
        return textProperty().get();
    }
    public ObjectProperty<String> textProperty() {
        if(text == null){
            text = new SimpleObjectProperty<String>(null);
        }
        return text;
    }
    public void setText(String text) {
        this.textProperty().set(text);
    }

    /**
     * 选中的项
     */
    private ObjectProperty<DropdownMenuItem> selectedItem;
    public DropdownMenuItem getSelectedItem() {
        return selectedItemProperty().get();
    }
    public ObjectProperty<DropdownMenuItem> selectedItemProperty() {
        if(selectedItem == null){
            selectedItem = new SimpleObjectProperty<>(null);
        }
        return selectedItem;
    }
    public void setSelectedItem(DropdownMenuItem selectedItem) {
        this.selectedItemProperty().set(selectedItem);
    }

    private static XmSVGIcon svgIcon = new XmSVGIcon("M0,0L2,0L5,5L8,0L10,0L5,7Z");
    /**
     * 右侧的图标，默认是下拉箭头
     */
    private ObjectProperty<Node> icon;
    public Node getIcon() {
        return iconProperty().get()==null?svgIcon:iconProperty().get();
    }
    public ObjectProperty<Node> iconProperty() {
        if(icon == null){
            icon = new SimpleObjectProperty<>(svgIcon);
        }
        return icon;
    }
    public void setIcon(Node icon) {
        this.iconProperty().set(icon);
    }


    /**
     * 下拉菜单显示的数据， 如果items里面具有子节点，那么会以子菜单的形式展示，
     * 如果设置了useGroup为true, 父节点作为groupName的文本展示，不可以被选中。
     */
    private ObservableList<DropdownMenuItem> items = FXCollections.observableArrayList();
    public ObservableList<DropdownMenuItem> getItems() {
        return items;
    }
    public void setItems(ObservableList<DropdownMenuItem> items) {
        this.items = items;
    }
    public void addItems(DropdownMenuItem...item){
        items.addAll(item);
    }

    /**
     * 如果useGroup为true，则以分组形式展示，最多支持三级，第三级是具体要显示的数据
     * 如果useGroup为false, 则以子菜单的形式展示，最多支持三级，
     */
    private BooleanProperty useGroup;
    public boolean isUseGroup() {
        return useGroupProperty().get();
    }
    public BooleanProperty useGroupProperty() {
        if(useGroup == null){
            useGroup = FxKit.newBooleanProperty(true, StyleableProperties.USE_GROUP, this, "useGroup");
        }
        return useGroup;
    }
    public void setUseGroup(boolean useGroup) {
        this.useGroupProperty().set(useGroup);
    }

    /**
     * 选中后的回调事件
     */
    private ObjectProperty<CallBack<DropdownMenuItem>> selectedCallback;
    public CallBack<DropdownMenuItem> getSelectedCallback() {
        return selectedCallbackProperty().get();
    }
    public ObjectProperty<CallBack<DropdownMenuItem>> selectedCallbackProperty() {
        if(selectedCallback == null){
            selectedCallback = new SimpleObjectProperty<>(null);
        }
        return selectedCallback;
    }
    public void setSelectedCallback(CallBack<DropdownMenuItem> selectedCallback) {
        this.selectedCallbackProperty().set(selectedCallback);
    }

    /**
     * 点击的回调事件
     */
    private ObjectProperty<CallBack<MouseEvent>> clickCallback;
    public CallBack<MouseEvent> getClickCallback() {
        return clickCallbackProperty().get();
    }
    public ObjectProperty<CallBack<MouseEvent>> clickCallbackProperty() {
        if(clickCallback == null){
            clickCallback = new SimpleObjectProperty<>(null);
        }
        return clickCallback;
    }
    public void setClickCallback(CallBack<MouseEvent> clickCallback) {
        this.clickCallbackProperty().set(clickCallback);
    }

    /**
     * 是否显示下拉的状态
     */
    public BooleanProperty showing;
    public Boolean isShowing() {
        return showingProperty().get();
    }
    public BooleanProperty showingProperty() {
        if(showing == null){
            showing = new BooleanPropertyBase(false) {
                @Override protected void invalidated() {
                    pseudoClassStateChanged(PSEUDO_CLASS_SHOWING, get());
                    notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
                }

                @Override
                public Object getBean() {
                    return XmDropdownMenu.this;
                }

                @Override
                public String getName() {
                    return "showing";
                }
            };
        }
        return showing;
    }

    /**
     * 显示与隐藏
     * @param showing Boolean
     */
    public void setShowing(Boolean showing) {
        Event.fireEvent(this, showing ? new Event(ON_SHOWING) :
                new Event(ON_HIDING));
        this.showingProperty().set(showing);
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
            hueType = FxKit.newProperty(HueType.DARK, XmDropdownMenu.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }
    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }

    /**
     * font
     */
    private ObjectProperty<Font> font;
    public void setFont(Font value) {
        fontProperty().set(value);
    }
    public Font getFont() {
        return font == null ? Font.getDefault() : font.get();
    }
    public final ObjectProperty<Font> fontProperty() {
        if (font == null) {
            font = new StyleableObjectProperty<Font>(Font.getDefault()) {
                @Override public Object getBean() { return XmDropdownMenu.this; }
                @Override public String getName() { return "font"; }
                @Override public CssMetaData<XmDropdownMenu,Font> getCssMetaData() {
                    return XmDropdownMenu.StyleableProperties.FONT;
                }
                @Override public void invalidated() {}
            };
        }
        return font;
    }

    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     */
    private BooleanProperty autoTextSize;
    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     * @return boolean
     */
    public boolean isAutoTextSize() {
        return autoTextSizeProperty().get();
    }
    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     * @return BooleanProperty
     */
    public BooleanProperty autoTextSizeProperty() {
        if(autoTextSize == null){
            autoTextSize = FxKit.newBooleanProperty(true, XmDropdownMenu.StyleableProperties.AUTO_TEXT_SIZE, this, "autoTextSize");
        }
        return autoTextSize;
    }
    /**
     * 是否自己文本设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置文本尺寸
     * false: 需要自己设置文本尺寸
     * @param autoTextSize boolean
     */
    public void setAutoTextSize(boolean autoTextSize) {
        this.autoTextSizeProperty().set(autoTextSize);
    }

    /**
     * 是否自己图标设置尺寸
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     */
    private BooleanProperty autoGraphicSize;

    /**
     * 是否自己图标设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     * @return boolean
     */
    public boolean isAutoGraphicSize() {
        return autoGraphicSizeProperty().get();
    }

    /**
     * 是否自己图标设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     * @return BooleanProperty
     */
    public BooleanProperty autoGraphicSizeProperty() {
        if(autoGraphicSize == null){
            autoGraphicSize = FxKit.newBooleanProperty(true, XmDropdownMenu.StyleableProperties.AUTO_GRAPHIC_SIZE, this, "autoGraphicSize");
        }
        return autoGraphicSize;
    }

    /**
     * 是否自己图标设置尺寸， 默认true
     * true: 控件根据SIZE_TYPE自己设置图标尺寸
     * false: 需要自己设置图标尺寸
     * @param autoGraphicSize boolean
     */
    public void setAutoGraphicSize(boolean autoGraphicSize) {
        this.autoGraphicSizeProperty().set(autoGraphicSize);
    }

    /**
     * 是否自己设置边框尺寸
     * true: 控件根据SIZE_TYPE自己设置边框尺寸
     * false: 需要自己设置边框尺寸
     */
    private BooleanProperty autoPadding;
    public boolean isAutoPadding() {
        return autoPaddingProperty().get();
    }
    public BooleanProperty autoPaddingProperty() {
        if(autoPadding == null){
            autoPadding = FxKit.newBooleanProperty(true, XmDropdownMenu.StyleableProperties.AUTO_PADDING, this, "autoPadding");
        }
        return autoPadding;
    }
    public void setAutoPadding(boolean autoPadding) {
        this.autoGraphicSizeProperty().set(autoPadding);
    }

    /**
     * 触发方式，默认是hover,
     * hover , click
     */
    private ObjectProperty<TriggerType> triggerType;
    public TriggerType getTriggerType() {
        return triggerTypeProperty().get();
    }
    public ObjectProperty<TriggerType> triggerTypeProperty() {
        if(triggerType == null){
            triggerType = FxKit.newProperty(TriggerType.HOVER, XmDropdownMenu.StyleableProperties.TRIGGER_TYPE, this, "triggerType");
        }
        return triggerType;
    }
    public void setTriggerType(TriggerType triggerType) {
        this.triggerTypeProperty().set(triggerType);
    }

    /**
     * 下拉窗口的偏移量
     */
    private ObjectProperty<XmPoint> offset;
    public XmPoint getOffset() {
        return offsetProperty().get();
    }
    public ObjectProperty<XmPoint> offsetProperty() {
        if(offset == null){
            offset = new SimpleObjectProperty<>(new XmPoint(0,0));
        }
        return offset;
    }
    public void setOffset(XmPoint offset) {
        this.offsetProperty().set(offset);
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
                    XmDropdownMenu.StyleableProperties.CLICK_ANIMATE_TYPE, this, "clickAnimateType");
        return clickAnimateType;
    }
    public void setClickAnimateType(ClickAnimateType clickAnimateType) {
        this.clickAnimateTypeProperty().set(clickAnimateType);
    }

    /* ------------------------------------- override -------------------------------- */

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case EXPANDED: return isShowing();
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmDropdownMenuSkin(this);
    }

    /** {@inheritDoc} */
//    @Override
//    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
//        switch (action) {
//            case EXPAND: show(); break;
//            case COLLAPSE: hide(); break;
//            default: super.executeAccessibleAction(action); break;
//        }
//    }

    /* ------------------------- style ------------------------------- */
    private static class StyleableProperties {
        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmDropdownMenu, HueType> HUE_TYPE =
                new CssMetaData<XmDropdownMenu, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.DARK, true) {
                    @Override
                    public boolean isSettable(XmDropdownMenu styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmDropdownMenu styleable) {
                        return (StyleableProperty<HueType>)styleable.hueTypeProperty();
                    }
                };

        /**
         * 字体
         * -fx-font: "songti"
         */
        private static final CssMetaData<XmDropdownMenu,Font> FONT =
                new FontCssMetaData<XmDropdownMenu>("-fx-font", Font.getDefault()) {

                    @Override
                    public boolean isSettable(XmDropdownMenu node) {
                        return node.font == null || !node.font.isBound();
                    }

                    @Override
                    public StyleableProperty<Font> getStyleableProperty(XmDropdownMenu node) {
                        return (StyleableProperty<Font>)node.fontProperty();
                    }
                };

        /**
         * -fx-auto-text-size
         */
        private static final CssMetaData<XmDropdownMenu,Boolean> AUTO_TEXT_SIZE =
                new CssMetaData<XmDropdownMenu,Boolean>("-fx-auto-graphic-text", BooleanConverter.getInstance(), Boolean.TRUE) {

                    @Override
                    public boolean isSettable(XmDropdownMenu node) {
                        return node.autoTextSize == null || !node.autoTextSize.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(XmDropdownMenu node) {
                        return (StyleableProperty<Boolean>)node.autoTextSizeProperty();
                    }
                };

        /**
         * -fx-auto-graphic-size
         */
        private static final CssMetaData<XmDropdownMenu,Boolean> AUTO_GRAPHIC_SIZE =
                new CssMetaData<XmDropdownMenu,Boolean>("-fx-auto-graphic-size", BooleanConverter.getInstance(), Boolean.TRUE) {

                    @Override
                    public boolean isSettable(XmDropdownMenu node) {
                        return node.autoGraphicSize == null || !node.autoGraphicSize.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(XmDropdownMenu node) {
                        return (StyleableProperty<Boolean>)node.autoGraphicSizeProperty();
                    }
                };

        /**
         * -fx-auto-padding
         */
        private static final CssMetaData<XmDropdownMenu,Boolean> AUTO_PADDING =
                new CssMetaData<XmDropdownMenu,Boolean>("-fx-auto-padding", BooleanConverter.getInstance(), Boolean.TRUE) {

                    @Override
                    public boolean isSettable(XmDropdownMenu node) {
                        return node.autoPadding == null || !node.autoPadding.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(XmDropdownMenu node) {
                        return (StyleableProperty<Boolean>)node.autoPaddingProperty();
                    }
                };

        /**
         * 按钮色调，hover/click
         * -fx-type-trigger: hover | click
         */
        final static CssMetaData<XmDropdownMenu, TriggerType> TRIGGER_TYPE =
                new CssMetaData<XmDropdownMenu, TriggerType>(CssKeys.PropTypeTrigger,
                        new EnumConverter<TriggerType>(TriggerType.class),
                        TriggerType.HOVER, true) {
                    @Override
                    public boolean isSettable(XmDropdownMenu styleable) {
                        return styleable.triggerType == null || !styleable.triggerType.isBound();
                    }

                    @Override
                    public StyleableProperty<TriggerType> getStyleableProperty(XmDropdownMenu styleable) {
                        return (StyleableProperty<TriggerType>)styleable.triggerTypeProperty();
                    }
                };

        /**
         * 按钮点击动画定义
         * -fx-type-click-animate: ripper | shadow
         */
        final static CssMetaData<XmDropdownMenu, ClickAnimateType> CLICK_ANIMATE_TYPE =
                new CssMetaData<XmDropdownMenu, ClickAnimateType>(CssKeys.PropTypeClickAnimate,
                        new EnumConverter<ClickAnimateType>(ClickAnimateType.class),
                        ClickAnimateType.RIPPER, true) {
                    @Override
                    public boolean isSettable(XmDropdownMenu styleable) {
                        return styleable.clickAnimateType == null || !styleable.clickAnimateType.isBound();
                    }

                    @Override
                    public StyleableProperty<ClickAnimateType> getStyleableProperty(XmDropdownMenu styleable) {
                        return (StyleableProperty<ClickAnimateType>)styleable.clickAnimateTypeProperty();
                    }
                };

        /**
         * -fx-use-group
         */
        private static final CssMetaData<XmDropdownMenu,Boolean> USE_GROUP =
                new CssMetaData<XmDropdownMenu,Boolean>("-fx-use-group", BooleanConverter.getInstance(), Boolean.TRUE) {

                    @Override
                    public boolean isSettable(XmDropdownMenu node) {
                        return node.useGroup == null || !node.useGroup.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(XmDropdownMenu node) {
                        return (StyleableProperty<Boolean>)node.useGroupProperty();
                    }
                };

        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            Collections.addAll(styleables
                    ,HUE_TYPE
                    ,FONT
                    ,AUTO_TEXT_SIZE
                    ,AUTO_GRAPHIC_SIZE
                    ,AUTO_PADDING
                    ,TRIGGER_TYPE
                    ,CLICK_ANIMATE_TYPE
                    ,USE_GROUP
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmDropdownMenu.StyleableProperties.STYLEABLES;
    }

}
