package com.xm2013.jfx.control.tableview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.scene.control.Skin;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmTableView<S> extends TableView<S> {

    private static final String DEFAULT_CLASS = "xm-list-view";

    public XmTableView(){
        super();
        this.init();
    }

    public XmTableView(ObservableList<S> items) {
        super(items);
        this.init();
    }

    private void init() {
        this.getStyleClass().add(DEFAULT_CLASS);
//        this.getStylesheets().add(STYLESHEETS);
    }

    @Override protected Skin<?> createDefaultSkin() {
        return new XmTableViewSkin<S>(this);
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
            colorType = FxKit.newProperty(ColorType.primary(), XmTableView.StyleableProperties.COLOR_TYPE, this, "colorType");
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
            sizeType = FxKit.newProperty(SizeType.MEDIUM, XmTableView.StyleableProperties.SIZE_TYPE, this, "sizeType");
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
            hueType = FxKit.newProperty(HueType.LIGHT, XmTableView.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }
    public void setHueType(HueType hueType) {
        this.hueTypeProperty().set(hueType);
    }

    /**
     * 表头背景色
     */
    private ObjectProperty<Paint> headerBgColor = new SimpleObjectProperty<>(Color.color(0,0,0, 0.06));
    public Paint getHeaderBgColor() {
        return headerBgColor.get();
    }
    public ObjectProperty<Paint> headerBgColorProperty() {
        return headerBgColor;
    }
    public void setHeaderBgColor(Paint headerBgColor) {
        this.headerBgColor.set(headerBgColor);
    }

    /**
     * 表头字体颜色
     */
    private ObjectProperty<Paint> headerFontColor = new SimpleObjectProperty<>(Color.web("#666666"));
    public Paint getHeaderFontColor() {
        return headerFontColor.get();
    }
    public ObjectProperty<Paint> headerFontColorProperty() {
        return headerFontColor;
    }
    public void setHeaderFontColor(Paint headerFontColor) {
        this.headerFontColor.set(headerFontColor);
    }

    /**
     * 表头字体
     */
    private ObjectProperty<Font> headerFont = new SimpleObjectProperty<>();
    public Font getHeaderFont() {
        return headerFont.get();
    }
    public ObjectProperty<Font> headerFontProperty() {
        return headerFont;
    }
    public void setHeaderFont(Font headerFont) {
        this.headerFont.set(headerFont);
    }

    public ObjectProperty<Paint> borderColor = new SimpleObjectProperty<>(Color.web("#eeeeee"));
    public Paint getBorderColor() {
        return borderColor.get();
    }
    public ObjectProperty<Paint> borderColorProperty() {
        return borderColor;
    }
    public void setBorderColor(Paint borderColor) {
        this.borderColor.set(borderColor);
    }

    /**
     * 设置表格内容的文本颜色，如果文本颜色为空，则根据HueType自定义颜色，
     * 如果设置了颜色，则使用设置的颜色
     */
    private ObjectProperty<Paint> cellTextColor = new SimpleObjectProperty<>();
    public Paint getCellTextColor() {
        return cellTextColor.get();
    }
    public ObjectProperty<Paint> cellTextColorProperty() {
        return cellTextColor;
    }
    public void setCellTextColor(Paint cellTextColor) {
        this.cellTextColor.set(cellTextColor);
    }

    private ObservableList<S> checkedValues = FXCollections.observableArrayList();
    public ObservableList<S> getCheckedValues() {
        return checkedValues;
    }
    public void setCheckedValues(ObservableList<S> checkedValues) {
        this.checkedValues.addAll(checkedValues);
    }
    public void addCheckedValue(S value){
        this.checkedValues.add(value);
    }

    /*----------------------------css style ------------------*/
    private static class StyleableProperties {

        /**
         * 控件颜色
         * -fx-type-color: webColor(#fff | #ffffff | #ffffffff | rgba(100,100,100,0.5)) | primary | secondary | success | warning | danger
         */
        final static CssMetaData<XmTableView, ColorType> COLOR_TYPE =
                new CssMetaData<XmTableView, ColorType>(CssKeys.PropTypeColor,
                        new ColorTypeConverter(),
                        ColorType.primary(), true) {
                    @Override
                    public boolean isSettable(XmTableView styleable) {
                        return styleable.colorType == null || !styleable.colorType.isBound();
                    }

                    @Override
                    public StyleableProperty<ColorType> getStyleableProperty(XmTableView styleable) {
                        return (StyleableProperty<ColorType>) styleable.colorTypeProperty();
                    }
                };

        /**
         * 大小尺寸
         * -fx-type-color: small, medium, large
         */
        final static CssMetaData<XmTableView, SizeType> SIZE_TYPE =
                new CssMetaData<XmTableView, SizeType>(CssKeys.PropTypeSize,
                        new EnumConverter<SizeType>(SizeType.class),
                        SizeType.MEDIUM, true) {
                    @Override
                    public boolean isSettable(XmTableView styleable) {
                        return styleable.sizeType == null || !styleable.sizeType.isBound();
                    }

                    @Override
                    public StyleableProperty<SizeType> getStyleableProperty(XmTableView styleable) {
                        return (StyleableProperty<SizeType>) styleable.sizeTypeProperty();
                    }
                };

        /**
         * 控件色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmTableView, HueType> HUE_TYPE =
                new CssMetaData<XmTableView, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.LIGHT, true) {
                    @Override
                    public boolean isSettable(XmTableView styleable) {
                        return false;
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmTableView styleable) {
                        return null;
                    }
                };


        // 创建一个CSS样式的表
        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(TableView.getClassCssMetaData());
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
        return XmTableView.StyleableProperties.STYLEABLES;
    }

}
