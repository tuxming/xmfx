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
package com.xm2013.jfx.control.selector;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.XmControl;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据下拉的选择框
 */
public class XmSelector<T> extends XmControl {

    private static final String USER_AGENT_STYLESHEET = FxKit.USER_AGENT_STYLESHEET;
    private static final String DEFAULT_STYLE_CLASS = "xm-selector";

    /* ---------------------------- constructor / override -------------------------------------*/

    public XmSelector(){
        this(null);
    }

    public XmSelector(ObservableList<T> items){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TEXT_FIELD);
        this.getStylesheets().add(USER_AGENT_STYLESHEET);
        if(items != null && items.size()>0){
            this.items.set(items);
        }
        setHueType(HueType.LIGHT);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmSelectorSkin<>(this);
    }

    /* ------------------------------- properties -------------------------------- */

    /**
     * 默认转换器
     * @return
     * @param <T>
     */
    private static <T> SelectorConvert<T>  defaultStringConverter() {
        return new SelectorConvert<T>() {
            @Override
            public List<T> getChildren(T t) {
                return null;
            }

            @Override
            public String toString(T object) {
                return object !=null ? object.toString() : null;
            }

            @Override public T fromString(String string) {
                return string!= null ? (T) string : null;
            }
        };
    }

    /**
     * 选中的值
     */
    private ObjectProperty<ObservableList<T>> values = new SimpleObjectProperty<>(FXCollections.<T>observableArrayList());
    public ObservableList<T> getValues() {
        return valuesProperty().get();
    }
    public T getValue(){
        ObservableList<T> vs = getValues();
        if(vs.size()>0){
            return vs.get(vs.size()-1);
        }else{
            return null;
        }
    }
    public void setValues(T... t){
        values.get().setAll(t);
    }
    public void setValue(T t){
        values.get().setAll(t);
    }
    public ObjectProperty<ObservableList<T>> valuesProperty(){
        return values;
    }

    /**
     * 设置选选择模式, 单选还是多选, 默认单选模式
     */
    private BooleanProperty multiple;
    public boolean isMultiple() {
        return multipleProperty().get();
    }
    public BooleanProperty multipleProperty() {
        if(multiple == null){
            multiple = FxKit.newBooleanProperty(false, StyleableProperties.MULTIPLE, this, "multiple");
        }
        return multiple;
    }
    public void setMultiple(boolean multiple) {
        this.multipleProperty().set(multiple);
    }

    /**
     * 待选择的数据
     */
    private ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<ObservableList<T>>(this, "items", FXCollections.observableArrayList());
    public final void setItems(T ...t) {
        items.get().setAll(t);
    }
    public final void setItems(ObservableList<T> items) {
        this.items.set(items);
    }
    public final ObservableList<T> getItems() {return items.get(); }
    public ObjectProperty<ObservableList<T>> itemsProperty() { return items; }
    public void addItems(T ...t){
        items.get().addAll(t);
    }

    /**
     * page目前不支持，后续根据需要进行修改
     * 选择框类型：LIST/GRID/TREE/PAGE
     */
    private ObjectProperty<SelectorType> selectorType;
    public SelectorType getSelectorType() {
        return selectorTypeProperty().get();
    }
    public ObjectProperty<SelectorType> selectorTypeProperty() {
        if(selectorType == null)
            selectorType = FxKit.newProperty(SelectorType.LIST, XmSelector.StyleableProperties.SELECTOR_TYPE, this, "selectorType");
        return selectorType;
    }
    public void setSelectorType(SelectorType selectorType) {
        this.selectorTypeProperty().set(selectorType);
    }

    /**
     * 下拉箭头，可以自定义
     */
    private XmSVGIcon svgIcon = new XmSVGIcon("M0,0L2,0L5,5L8,0L10,0L5,7Z", Color.BLACK, 16);
    /**
     * 右侧的图标，默认是下拉箭头， 只在单选模式下生效
     */
    private ObjectProperty<Node> arrowIcon;
    public Node getArrowIcon() {
        return arrowIconProperty().get()==null?svgIcon: arrowIconProperty().get();
    }
    public ObjectProperty<Node> arrowIconProperty() {
        if(arrowIcon == null){
            arrowIcon = new SimpleObjectProperty<>(svgIcon);
        }
        return arrowIcon;
    }
    public void setArrowIcon(Node arrowIcon) {
        this.arrowIconProperty().set(arrowIcon);
    }

    /**
     * 前缀图标，只在当选模式下生效
     */
    private ObjectProperty<Node> prefix;
    public Node getPrefix() {
        return prefixProperty().get();
    }
    public ObjectProperty<Node> prefixProperty() {
        if(prefix == null){
            prefix = new SimpleObjectProperty<>(null);
        }
        return prefix;
    }
    public void setPrefix(Node prefix) {
        this.prefixProperty().set(prefix);
    }

    /**
     * 转换器，将对象T转换成可显示的有意义的文本
     * @return ObjectProperty
     */
    public ObjectProperty<SelectorConvert<T>> converterProperty() { return converter; }
    private ObjectProperty<SelectorConvert<T>> converter =
            new SimpleObjectProperty<SelectorConvert<T>>(this, "converter", XmSelector.<T>defaultStringConverter());
    public final void setConverter(SelectorConvert<T> value) {
        converterProperty().set(value != null ? value : XmSelector.<T>defaultStringConverter());
    }
    public final SelectorConvert<T> getConverter() {return converterProperty().get(); }

    /**
     * 是否可以关闭标签
     */
    private BooleanProperty closeable;
    public boolean isCloseable() {
        return closeableProperty().get();
    }
    public BooleanProperty closeableProperty() {
        if(closeable == null){
            closeable = FxKit.newBooleanProperty(false, XmSelector.StyleableProperties.CLOSEABLE, this, "closeable");
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
            editable = FxKit.newBooleanProperty(false, XmSelector.StyleableProperties.EDITABLE, this, "editable");
        return editable;
    }
    public void setEditable(boolean editable) {
        this.editableProperty().set(editable);
    }

    /**
     * 设置列表每个cell的回调, 可以通过回调做一些自定义操作
     */
    public ObjectProperty<SelectorCellFactory<T>> cellFactory = new SimpleObjectProperty<>(null);

    public SelectorCellFactory<T> getCellFactory() {
        return cellFactory.get();
    }

    public ObjectProperty<SelectorCellFactory<T>> cellFactoryProperty() {
        return cellFactory;
    }

    public void setCellFactory(SelectorCellFactory<T> cellFactory) {
        this.cellFactory.set(cellFactory);
    }


    /**
     * 下拉按钮是填充还是，边框，默认是填充
     */
    private BooleanProperty fillArrow;
    public boolean isFillArrow() {
        return fillArrowProperty().get();
    }
    public BooleanProperty fillArrowProperty() {
        if(fillArrow == null){
            fillArrow = FxKit.newBooleanProperty(false, StyleableProperties.FILL_ARROW, this, "fillIcon");
        }
        return fillArrow;
    }
    public void setFillArrow(boolean fillArrow) {
        this.fillArrowProperty().set(fillArrow);
    }

    /**
     * 多选情况下，显示的最大条目数
     */
    private IntegerProperty maxTagCount;
    public int getMaxTagCount() {
        return maxTagCountProperty().get();
    }
    public IntegerProperty maxTagCountProperty() {
        if(maxTagCount == null){
            maxTagCount = FxKit.newIntegerProperty(-1, StyleableProperties.MAX_TAG_COUNT, this, "maxTagCount");
        }
        return maxTagCount;
    }
    public void setMaxTagCount(int maxTagCount) {
        this.maxTagCountProperty().set(maxTagCount);
    }

    /**
     * 默认显示的文本
     */
    private StringProperty promptText;
    public String getPromptText() {
        return promptTextProperty().get();
    }
    public StringProperty promptTextProperty() {
        if(promptText == null){
            promptText = new SimpleStringProperty(this, "promptText", null) {
                @Override protected void invalidated() {
                    // Strip out newlines
                    String txt = get();
                    if (txt != null && txt.contains("\n")) {
                        txt = txt.replace("\n", "");
                        set(txt);
                    }
                }
            };
        }
        return promptText;
    }
    public void setPromptText(String promptText) {
        this.promptTextProperty().set(promptText);
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
            hueType = FxKit.newProperty(HueType.DARK, XmSelector.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }

    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
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
                    XmSelector.StyleableProperties.CLICK_ANIMATE_TYPE, this, "clickAnimateType");
        return clickAnimateType;
    }
    public void setClickAnimateType(ClickAnimateType clickAnimateType) {
        this.clickAnimateTypeProperty().set(clickAnimateType);
    }


    /* ----------------------------------- style -------------------------------------- */

    private static class StyleableProperties {

        /**
         * 选择框类型，LIST/TREE/GRID/PAGE
         * -fx-type-selector: list | grid | tree | page
         */
        final static CssMetaData<XmSelector, SelectorType> SELECTOR_TYPE =
                new CssMetaData<XmSelector, SelectorType>(CssKeys.PropTypeHue,
                        new EnumConverter<SelectorType>(SelectorType.class),
                        SelectorType.LIST, true) {
                    @Override
                    public boolean isSettable(XmSelector styleable) {
                        return styleable.selectorType == null || !styleable.selectorType.isBound();
                    }

                    @Override
                    public StyleableProperty<SelectorType> getStyleableProperty(XmSelector styleable) {
                        return (StyleableProperty<SelectorType>)styleable.selectorTypeProperty();
                    }
                };

        /**
         * -fx-closeable: true | false
         */
        private static final CssMetaData<XmSelector,Boolean> CLOSEABLE = new CssMetaData<XmSelector, Boolean>(
                "-fx-closeable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmSelector styleable) {
                return styleable.closeable == null || !styleable.closeable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmSelector styleable) {
                return (StyleableProperty<Boolean>) styleable.closeableProperty();
            }
        };
        /**
         * -fx-editable: true | false
         */
        private static final CssMetaData<XmSelector,Boolean> EDITABLE = new CssMetaData<XmSelector, Boolean>(
                "-fx-editable", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmSelector styleable) {
                return styleable.editable == null || !styleable.editable.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmSelector styleable) {
                return (StyleableProperty<Boolean>) styleable.editableProperty();
            }
        };

        /**
         * -fx-multiple: true | false
         */
        private static final CssMetaData<XmSelector,Boolean> MULTIPLE = new CssMetaData<XmSelector, Boolean>(
                "-fx-multiple", BooleanConverter.getInstance(), true, true
        ) {
            @Override
            public boolean isSettable(XmSelector styleable) {
                return styleable.multiple == null || !styleable.multiple.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmSelector styleable) {
                return (StyleableProperty<Boolean>) styleable.multipleProperty();
            }
        };

        /**
         * -fx-fill-arrow: true | false
         */
        private static final CssMetaData<XmSelector,Boolean> FILL_ARROW = new CssMetaData<XmSelector, Boolean>(
                "-fx-fill-arrow", BooleanConverter.getInstance(), false, true
        ) {
            @Override
            public boolean isSettable(XmSelector styleable) {
                return styleable.fillArrow == null || !styleable.fillArrow.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(XmSelector styleable) {
                return (StyleableProperty<Boolean>) styleable.fillArrowProperty();
            }
        };

        /**
         * -fx-max-tag-count: -1 不限制
         */
        private static final CssMetaData<XmSelector,Number> MAX_TAG_COUNT =
                new CssMetaData<XmSelector,Number>("-fx-max-tag-count",
                        SizeConverter.getInstance(), 5.0) {

                    @Override
                    public boolean isSettable(XmSelector node) {
                        return node.maxTagCount == null ||
                                !node.maxTagCount.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(XmSelector node) {
                        return (StyleableProperty<Number>)node.maxTagCountProperty();
                    }
                };

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmSelector, HueType> HUE_TYPE =
                new CssMetaData<XmSelector, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.DARK, true) {
                    @Override
                    public boolean isSettable(XmSelector styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmSelector styleable) {
                        return (StyleableProperty<HueType>)styleable.hueTypeProperty();
                    }
                };

        /**
         * 按钮点击动画定义
         * -fx-type-click-animate: ripper | shadow
         */
        final static CssMetaData<XmSelector, ClickAnimateType> CLICK_ANIMATE_TYPE =
                new CssMetaData<XmSelector, ClickAnimateType>(CssKeys.PropTypeClickAnimate,
                        new EnumConverter<ClickAnimateType>(ClickAnimateType.class),
                        ClickAnimateType.RIPPER, true) {
                    @Override
                    public boolean isSettable(XmSelector styleable) {
                        return styleable.clickAnimateType == null || !styleable.clickAnimateType.isBound();
                    }

                    @Override
                    public StyleableProperty<ClickAnimateType> getStyleableProperty(XmSelector styleable) {
                        return (StyleableProperty<ClickAnimateType>)styleable.clickAnimateTypeProperty();
                    }
                };


        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            Collections.addAll(styleables
                    ,SELECTOR_TYPE
                    ,EDITABLE
                    ,CLOSEABLE
                    ,MULTIPLE
                    ,FILL_ARROW
                    ,MAX_TAG_COUNT
                    ,HUE_TYPE
                    ,CLICK_ANIMATE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmSelector.StyleableProperties.STYLEABLES;
    }

}
