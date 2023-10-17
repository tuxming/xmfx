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

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.*;
import javafx.beans.value.WritableValue;
import javafx.css.*;
import javafx.css.converter.*;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;

import java.util.*;

/**
 *  可选择文本
 *  只支持文本
 *
 *  支持自动换行，和 单行超长截断， 多行超长截断
 *
 *  自动换行(wrapText)设置为false, 如果设置了textWidth， 超过的宽度的文本 用 ... 替代
 *  自动换行(wrapText)设置为true,  必须设置textWidth，如果设置了textHeight，超过高度的部分的文本 用...替代
 *  自动换行(wrapText)设置为true,  必须设置textWidth，如果设置了maxRow，超过高度的部分的文本 用...替代
 *
 *  SelectableText text = new SelectableText("这是一个文本，内容不太长，但是演示自动截断应该是够了, 要是不够就在复制一行");
 *  单行超长截断
 *  text.setTextWidth(60);
 *
 *  多行超长自动截断
 *  text.setWrapText(true);
 *  text.setTextWidth(60);
 *  text.setTextHeight(32);
 *
 *  超过指定行数截断
 *  text.setWrapText(true);
 *  text.setTextWidth(60);
 *  text.setTextRow(2);
 *
 */
public class SelectableText extends XmControl {

    private Map<String, Object> properties = new HashMap<>();

    /*------------------- Methods -----------------------*/
    public SelectableText(){
        this(null);
    }
    public SelectableText(String text){
        this.textProperty().set(text);
        this.init();
    }

    private void init() {
        getStylesheets().add(FxKit.USER_AGENT_STYLESHEET);
        getStyleClass().setAll("selectable-text");
        setFocusTraversable(false);
        setAccessibleRole(AccessibleRole.TEXT);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SelectableTextSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return FxKit.USER_AGENT_STYLESHEET;
    }

    /*------------------- Properties -----------------------*/

    /**
     * 文本内容
     */
    private StringProperty text;
    public String getText() {
        return textProperty().get();
    }
    public StringProperty textProperty() {
        if(text==null)
            text = new SimpleStringProperty();
        return text;
    }
    public void setText(String text) {
        this.textProperty().set(text);
    }

    /**
     * 选中的文本内容
     */
    private StringProperty selectedText;
    public String getSelectedText() {
        return selectedTextProperty().get();
    }
    public StringProperty selectedTextProperty() {
        if(selectedText == null){
            selectedText = new SimpleStringProperty();
            properties.put("selectedText", selectedText);
        }
        return selectedText;
    }
    public void setSelectedText(String selectedText) {
        this.selectedTextProperty().set(selectedText);
    }

    /**
     * 选中的起始位置
     */
    private IntegerProperty selectedStart;
    public int getSelectedStart() {
        return selectedStartProperty().get();
    }
    public IntegerProperty selectedStartProperty() {
        if(selectedStart == null){
            selectedStart = new SimpleIntegerProperty();
            properties.put("selectedStart", selectedStart);
        }
        return selectedStart;
    }
    public void setSelectedStart(int selectedStart) {
        this.selectedStartProperty().set(selectedStart);
    }

    /**
     * 选中的结束位置
     */
    private IntegerProperty selectedEnd;
    public int getSelectedEnd() {
        return selectedEndProperty().get();
    }
    public IntegerProperty selectedEndProperty() {
        if(selectedEnd == null){
            selectedEnd = new SimpleIntegerProperty();
            properties.put("selectedStart", selectedStart);
        }
        return selectedEnd;
    }
    public void setSelectedEnd(int selectedEnd) {
        this.selectedEndProperty().set(selectedEnd);
    }

    /**
     * 文本的颜色
     */
    private ObjectProperty<Paint> textFill;
    public Paint getTextFill() {
        return textFillProperty().get();
    }
    public ObjectProperty<Paint> textFillProperty() {
        if (textFill == null) {
            textFill = FxKit.newProperty(Color.BLACK, SelectableText.StyleableProperties.TEXT_FILL, this, "textFill");
//            textFill  = new StyleableObjectProperty<Paint>(Color.BLACK) {
//                @Override
//                public Object getBean() {
//                    return this;
//                }
//
//                @Override
//                public String getName() {
//                    return "textFill";
//                }
//
//                @Override
//                public CssMetaData<? extends Styleable, Paint> getCssMetaData() {
//                    return StyleableProperties.TEXT_FILL;
//                }
//            };
            properties.put("textFill", textFill);

        }

        return textFill;
    }
    public void setTextFill(Paint textFill) {
        this.textFillProperty().set(textFill);
    }

    /**
     * 文本的描边颜色
     */
    private ObjectProperty<Paint> textStroke;
    public Paint getTextStroke() {
        return textStrokeProperty().get();
    }
    public ObjectProperty<Paint> textStrokeProperty() {
        if(textStroke == null){
            textStroke = FxKit.newProperty(null, StyleableProperties.TEXT_STROKE, this, "textStroke");
            properties.put("textStroke", textStroke);
        }
        return textStroke;
    }
    public void setTextStroke(Paint textStroke) {
        this.textStrokeProperty().set(textStroke);
    }

    /**
     * 行间距
     */
    private DoubleProperty lineSpace;
    public double getLineSpace() {
        return lineSpaceProperty().get();
    }
    public DoubleProperty lineSpaceProperty() {
        if(lineSpace == null){
            lineSpace = FxKit.newDoubleProperty(0d, StyleableProperties.TEXT_LINE_SPACE, this, "lineSpace");
            properties.put("lineSpace", lineSpace);
        }
        return lineSpace;
    }

    public void setLineSpace(double lineSpace) {
        this.lineSpaceProperty().set(lineSpace);
    }

    /**
     * 文本描边宽度
     */
    private DoubleProperty textStrokeWidth;
    public Double getTextStrokeWidth() {
        return textStrokeWidth.get();
    }
    public DoubleProperty textStrokeWidthProperty() {
        if (textStrokeWidth == null) {
            textStrokeWidth = FxKit.newDoubleProperty(1d, StyleableProperties.TEXT_STROKE_WIDTH, this, "textStrokeWidth");
            properties.put("textStrokeWidth", textStrokeWidth);
        }
        return textStrokeWidth;
    }
    public void setTextStrokeWidth(Double textStrokeWidth) {
        this.textStrokeWidth.set(textStrokeWidth);
    }

    /**
     * 文本是否添加删除线
     */
    private BooleanProperty textStrikeThrough;
    public boolean isStrikethrough() {
        return textStrikeThroughProperty().get();
    }
    public BooleanProperty textStrikeThroughProperty() {
        if ( textStrikeThrough == null) {
            textStrikeThrough = FxKit.newBooleanProperty(Boolean.FALSE, StyleableProperties.TEXT_STRIKE_THROUGH, this, "strikeThrough");
            properties.put("textStrikeThrough", textStrikeThrough);
        }
        return textStrikeThrough;
    }
    public void setStrikethrough(boolean strikethrough) {
        this.textStrikeThroughProperty().set(strikethrough);
    }

    /**
     * 文本的描边类型
     */
    private ObjectProperty<StrokeType> textStrokeType;
    public StrokeType getTextStrokeType() {
        return textStrokeTypeProperty().get();
    }
    public ObjectProperty<StrokeType> textStrokeTypeProperty() {
        if (textStrokeType == null) {
            textStrokeType = FxKit.newProperty(StrokeType.CENTERED, StyleableProperties.TEXT_STROKE_TYPE, this, "strokeType");
            properties.put("textStrokeType", textStrokeType);
        }
        return textStrokeType;
    }
    public void setTextStrokeType(StrokeType textStrokeType) {
        this.textStrokeTypeProperty().set(textStrokeType);
    }

    /**
     * 文本的描边的dash offset
     */
    private DoubleProperty textStrokeDashOffset;
    public double getTextStrokeDashOffset() {
        return textStrokeDashOffsetProperty().get();
    }
    public DoubleProperty textStrokeDashOffsetProperty() {
        if (textStrokeDashOffset == null) {
            textStrokeDashOffset = FxKit.newDoubleProperty(0d, StyleableProperties.TEXT_STROKE_DASH_OFFSET, this, "textStrokeDashOffset");
            properties.put("textStrokeDashOffset", textStrokeDashOffset);
        }
        return textStrokeDashOffset;
    }
    public void setTextStrokeDashOffset(double textStrokeDashOffset) {
        this.textStrokeDashOffsetProperty().set(textStrokeDashOffset);
    }

    /**
     * 文本的描边的line cap
     */
    private ObjectProperty<StrokeLineCap> textStrokeLineCap;
    public StrokeLineCap getTextStrokeLineCap() {
        return textStrokeLineCapProperty().get();
    }
    public ObjectProperty<StrokeLineCap>  textStrokeLineCapProperty() {
        if (textStrokeLineCap == null) {
            textStrokeLineCap = FxKit.newProperty(StrokeLineCap.SQUARE, StyleableProperties.TEXT_STROKE_LINE_CAP, this, "textStrokeLineCap");
            properties.put("textStrokeLineCap", textStrokeLineCap);
        }
        return textStrokeLineCap;
    }
    public void setTextStrokeLineCap(StrokeLineCap textStrokeLineCap) {
        this.textStrokeLineCapProperty().set(textStrokeLineCap);
    }

    /**
     * 文本平滑类型
     */
    private ObjectProperty<FontSmoothingType> textSmoothingType;
    public FontSmoothingType getTextSmoothingType() {
        return textSmoothingTypeProperty().get();
    }


    public ObjectProperty<FontSmoothingType> textSmoothingTypeProperty() {
        if (textSmoothingType == null) {
            textSmoothingType = FxKit.newProperty(FontSmoothingType.GRAY, StyleableProperties.TEXT_SMOOTHING_TYPE, this, "textSmoothingType");
            properties.put("textSmoothingType", textSmoothingType);
        }
        return textSmoothingType;
    }
    public void setTextSmoothingType(FontSmoothingType textSmoothingType) {
        this.textSmoothingTypeProperty().set(textSmoothingType);
    }

    /**
     * 文本左右对齐方式
     */
    private ObjectProperty<TextAlignment> textAlignment;
    public TextAlignment getTextAlignment() {
        return textAlignmentProperty().get();
    }
    public ObjectProperty<TextAlignment> textAlignmentProperty() {
        if (textAlignment == null) {
            textAlignment = FxKit.newProperty(TextAlignment.LEFT, StyleableProperties.TEXT_ALIGNMENT, this, "textAlignment");
            properties.put("textAlignment", textAlignment);
        }
        return textAlignment;
    }
    public void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignmentProperty().set(textAlignment);
    }

    /**
     * 文本上下对齐方法
     */
  /*  private ObjectProperty<VPos> textOrigin;
    public VPos getTextOrigin() {
        return textOriginProperty().get();
    }
    public ObjectProperty<VPos> textOriginProperty() {
        if(textOrigin == null){
            textOrigin = FxKit.newProperty(VPos.BASELINE, StyleableProperties.TEXT_ORIGIN, this, "textOrigin");
            properties.put("textOrigin", textOrigin);
        }
        return textOrigin;
    }
    public void setTextOrigin(VPos textOrigin) {
        this.textOriginProperty().set(textOrigin);
    }*/

    /**
     * 文本是否具有下滑线
     */
    private BooleanProperty underline;
    public boolean isUnderline() {
        return underlineProperty().get();
    }
    public BooleanProperty underlineProperty() {
        if(underline == null){
            underline = FxKit.newBooleanProperty(Boolean.FALSE, StyleableProperties.TEXT_UNDERLINE, this, "underline");
            properties.put("underline", underline);
        }
        return underline;
    }
    public void setUnderline(boolean underline) {
        this.underlineProperty().set(underline);
    }

    /**
     * 文字效果
     */
    private ObjectProperty<Effect> textEffect;
    public Effect getTextEffect() {
        return textEffectProperty().get();
    }
    public ObjectProperty<Effect> textEffectProperty() {
        if(textEffect == null){
            textEffect = FxKit.newProperty(null, StyleableProperties.TEXT_EFFECT, this, "textEffect");
            properties.put("textEffect", textEffect);
        }
        return textEffect;
    }
    public void setTextEffect(Effect textEffect) {
        this.textEffectProperty().set(textEffect);
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
                @Override public Object getBean() { return SelectableText.this; }
                @Override public String getName() { return "font"; }
                @Override public CssMetaData<SelectableText,Font> getCssMetaData() {
                    return SelectableText.StyleableProperties.FONT;
                }
                @Override public void invalidated() {}
            };
            properties.put("font", font);
        }
        return font;
    }

    /**
     * 是否自动换行
     */
    private BooleanProperty wrapText;
    public boolean isWrapText() {
        return wrapTextProperty().get();
    }
    public BooleanProperty wrapTextProperty() {
        if(wrapText == null){
            wrapText = FxKit.newBooleanProperty(false, StyleableProperties.TEXT_WRAP, this, "textWrap");
            properties.put("wrapText", wrapText);
        }
        return wrapText;
    }
    public void setWrapText(boolean wrapText) {
        this.wrapTextProperty().set(wrapText);
    }

    /**
     * 允许显示的最大行数，超过这个行数，自动截断
     */
    public IntegerProperty maxRow;
    public int getMaxRow() {
        return maxRowProperty().get();
    }
    public IntegerProperty maxRowProperty() {
        if(maxRow == null) maxRow = FxKit.newIntegerProperty(-1, StyleableProperties.MAX_ROW, this, "maxRow");
        properties.put("maxRow", maxRow);
        return maxRow;
    }
    public void setMaxRow(int maxRow) {
        this.maxRowProperty().set(maxRow);
    }

    private StringProperty ellipsisString;
    public final StringProperty ellipsisStringProperty() {
        if (ellipsisString == null) {
            ellipsisString = new StyleableStringProperty("...") {
                @Override public Object getBean() {
                    return SelectableText.this;
                }

                @Override public String getName() {
                    return "ellipsisString";
                }

                @Override public CssMetaData<SelectableText,String> getCssMetaData() {
                    return SelectableText.StyleableProperties.ELLIPSIS_STRING;
                }
            };
        }
        return ellipsisString;
    }
    public String getEllipsisString() {
        return ellipsisStringProperty().get();
    }
    public void setEllipsisString(String ellipsisString) {
        this.ellipsisStringProperty().set(ellipsisString);
    }

    /**
     * 选中后的背景颜色
     */
    private ObjectProperty<Paint> selectedFill;
    public Paint getSelectedFill() {
        return selectedFillProperty().get();
    }
    public ObjectProperty<Paint> selectedFillProperty() {
        if(selectedFill == null){
            selectedFill = FxKit.newProperty(Color.web("#a6d2ff"), StyleableProperties.SELECTED_FILL, this, "selectedFill");
            properties.put("selectedFill", selectedFill);
        }
        return selectedFill;
    }
    public void setSelectedFill(Paint selectedFill) {
        this.selectedFillProperty().set(selectedFill);
    }

    /**
     * 选中后的文字颜色
     */
    private ObjectProperty<Paint> selectedTextFill;
    public Paint getSelectedTextFill() {
        return selectedTextFillProperty().get();
    }
    public ObjectProperty<Paint> selectedTextFillProperty() {
        if(selectedTextFill == null){
            selectedTextFill = FxKit.newProperty(Color.web("#190027"), StyleableProperties.SELECTED_TEXT_FILL, this, "selectedTextFill");
            properties.put("selectedTextFill", selectedTextFill);
        }
        return selectedTextFill;
    }
    public void setSelectedTextFill(Paint selectedTextFill) {
        this.selectedTextFillProperty().set(selectedTextFill);
    }

    /**
     * 文本的宽度，本组件不建议使用prefWidth
     */
    private DoubleProperty textWidth;
    public double getTextWidth() {
        return textWidthProperty().get();
    }
    public DoubleProperty textWidthProperty() {
        if(textWidth == null){
            textWidth = FxKit.newDoubleProperty(-1d, StyleableProperties.TEXT_WIDTH, this, "textWidth");
            properties.put("textWidth", textWidth);
        }
        return textWidth;
    }
    public void setTextWidth(double textWidth) {
        this.textWidthProperty().set(textWidth);
    }

    /**
     * 文本的高度，本组件不建议使用prefHeight
     */
    private DoubleProperty textHeight;
    public double getTextHeight() {
        return textHeightProperty().get();
    }
    public DoubleProperty textHeightProperty() {
        if(textHeight == null){
            textHeight = FxKit.newDoubleProperty(-1d, StyleableProperties.TEXT_HEIGHT, this, "textHeight");
            properties.put("textWidth", textHeight);
        }
        return textHeight;
    }
    public void setTextHeight(double textHeight) {
        this.textHeightProperty().set(textHeight);
    }

    /*---------------------------------css style----------------------------------------*/
    private static class StyleableProperties {

        /**
         * 字体颜色
         * -fx-text-fill: 16px;
         */
        private static final CssMetaData<SelectableText, Paint> TEXT_FILL = newCssMetaData(
                "-fx-text-fill",
                PaintConverter.getInstance(),
                Color.BLACK,
                "textFillProperty"
        );
//                new CssMetaData<SelecteableText, Paint>("-fx-text-fill", PaintConverter.getInstance(), Color.BLACK) {
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textFill == null || !node.textFill.isBound();
//            }
//
//            @Override
//            public StyleableProperty<Paint> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<Paint>) node.textFillProperty();
//            }
//        };

        /**
         * 文本的描边颜色
         * -fx-text-stroke: 16px;
         */
        private static final CssMetaData<SelectableText, Paint> TEXT_STROKE = newCssMetaData(
            "-fx-text-stroke",
                PaintConverter.getInstance(),
                null,
                "textStrokeProperty"
        );
//                new CssMetaData<SelecteableText, Paint>("-fx-text-stroke", PaintConverter.getInstance(), Color.BLACK) {
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textStroke == null || !node.textStroke.isBound();
//            }
//
//            @Override
//            public StyleableProperty<Paint> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<Paint>) node.textStrokeProperty();
//            }
//        };

        /**
         * 文本的描边宽度
         * -fx-stroke-width: 4px;
         */
        private static final CssMetaData<SelectableText, Number> TEXT_STROKE_WIDTH = newCssMetaData(
                "-fx-stroke-width",
                SizeConverter.getInstance(),
                1,
                "textStrokeWidthProperty"
        );
//                new CssMetaData<SelecteableText, Number>("-fx-stroke-width", SizeConverter.getInstance(), 16.0) {
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textStrokeWidth == null || !node.textStrokeWidth.isBound();
//            }
//
//            @Override
//            public StyleableProperty<Number> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<Number>) node.textStrokeWidthProperty();
//            }
//        };

        /**
         * 文本的删除线
         * -fx-strike-through: true
         */
        private static final CssMetaData<SelectableText,Boolean> TEXT_STRIKE_THROUGH = newCssMetaData(
                "-fx-strike-through",
                BooleanConverter.getInstance(),
                Boolean.FALSE,
                "textStrikeThroughProperty"
        );
//                new CssMetaData<SelecteableText,Boolean>("-fx-strike-through", BooleanConverter.getInstance(), Boolean.FALSE) {
//
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textStrikeThrough == null || !node.textStrikeThrough.isBound();
//            }
//
//            @Override
//            public StyleableProperty<Boolean> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<Boolean>)node.textStrikeThroughProperty();
//            }
//        };

        /**
         *  文本的描边类型
         *  -fx-stroke-type: inside;
         */
        private static final CssMetaData<SelectableText, StrokeType> TEXT_STROKE_TYPE = newCssMetaData(
                "-fx-stroke-type",
                new EnumConverter<>(StrokeType.class),
                StrokeType.CENTERED,
                "textStrokeTypeProperty"
        );
//                new CssMetaData<SelecteableText,StrokeType>("-fx-stroke-type", new EnumConverter<StrokeType>(StrokeType.class), StrokeType.CENTERED) {
//
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textStrokeType == null || node.textStrokeType.isBound();
//            }
//
//            @Override
//            public StyleableProperty<StrokeType> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<StrokeType>)node.textStrokeTypeProperty();
//            }
//
//        };

        /**
         * 文本描边的dash offset
         * -fx-stroke-dash-offset: 4px;
         */
        private static final CssMetaData<SelectableText, Number> TEXT_STROKE_DASH_OFFSET = newCssMetaData(
            "-fx-stroke-dash-offset",
                SizeConverter.getInstance(),
                0d,
                "textStrokeDashOffsetProperty"
        );
//                new CssMetaData<SelecteableText, Number>("-fx-stroke-dash-offset", SizeConverter.getInstance(), 16.0) {
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textStrokeDashOffset == null || !node.textStrokeDashOffset.isBound();
//            }
//
//            @Override
//            public StyleableProperty<Number> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<Number>) node.textStrokeDashOffsetProperty();
//            }
//        };

        /**
         * 文本描边的line cap
         * -fx-stroke-dash-offset: 4px;
         */
        private static final CssMetaData<SelectableText, StrokeLineCap> TEXT_STROKE_LINE_CAP = newCssMetaData(
                "-fx-stroke-line-cap",
                new EnumConverter<>(StrokeLineCap.class),
                StrokeLineCap.SQUARE,
                "textStrokeLineCapProperty"
        );
/*        new CssMetaData<SelecteableText, StrokeLineCap>("-fx-stroke-line-cap", new EnumConverter<StrokeLineCap>(StrokeLineCap.class), StrokeLineCap.SQUARE) {
            @Override
            public boolean isSettable(SelecteableText node) {
                return node.textStrokeDashOffset == null || !node.textStrokeDashOffset.isBound();
            }

            @Override
            public StyleableProperty<StrokeLineCap> getStyleableProperty(SelecteableText node) {
                return (StyleableProperty<StrokeLineCap>) node.textStrokeLineCapProperty();
            }
        };*/

        /**
         * 文本的平滑类型
         * -fx-font-smoothing-type: gray | lcd;
         */
        private static final CssMetaData<SelectableText, FontSmoothingType> TEXT_SMOOTHING_TYPE = newCssMetaData(
                "-fx-font-smoothing-type",
                new EnumConverter<>(FontSmoothingType.class),
                FontSmoothingType.GRAY,
                "textSmoothingTypeProperty"
        );
//                new CssMetaData<SelecteableText, FontSmoothingType>("-fx-font-smoothing-type", new EnumConverter<FontSmoothingType>(FontSmoothingType.class), FontSmoothingType.GRAY) {
//            @Override
//            public boolean isSettable(SelecteableText node) {
//                return node.textStrokeDashOffset == null || !node.textStrokeDashOffset.isBound();
//            }
//
//            @Override
//            public StyleableProperty<FontSmoothingType> getStyleableProperty(SelecteableText node) {
//                return (StyleableProperty<FontSmoothingType>) node.textSmoothingTypeProperty();
//            }
//        };

        private static final CssMetaData<SelectableText, Number> MAX_ROW = newCssMetaData(
                "-fx-max-row",
                SizeConverter.getInstance(),
                0d,
                "maxRow"
        );

        /**
         * 文本左右对齐方式
         * -fx-text-align
         */
        private static final CssMetaData<SelectableText, TextAlignment> TEXT_ALIGNMENT = newCssMetaData(
                "-fx-text-align",
                new EnumConverter<>(TextAlignment.class),
                TextAlignment.LEFT,
                "textAlignmentProperty"

        );

        /**
         * 文本的上下对齐方式
         * -fx-text-origin: baseline;
         */
        private static final CssMetaData<SelectableText, VPos> TEXT_ORIGIN = newCssMetaData(
                "-fx-text-origin",
                new EnumConverter<>(VPos.class),
                VPos.TOP,
                "textOriginProperty"
        );

        /**
         * 文本是否具有下划线
         * -fx-text-underline: false
         */
        private static final CssMetaData<SelectableText, Boolean> TEXT_UNDERLINE = newCssMetaData(
            "-fx-text-underline",
                BooleanConverter.getInstance(),
                Boolean.FALSE,
                "underline"
        );

        /**
         * 文本的特效
         * -fx-text-effect
         */
        private static final CssMetaData<SelectableText, Effect> TEXT_EFFECT = newCssMetaData(
            "-fx-text-effect",
                EffectConverter.getInstance(),
                null,
                "textEffect"
        );

        /**
         * 字体
         * -fx-font: "songti"
         */
        private static final CssMetaData<SelectableText,Font> FONT =
                new FontCssMetaData<SelectableText>("-fx-font", Font.getDefault()) {

            @Override
            public boolean isSettable(SelectableText node) {
                return node.font == null || !node.font.isBound();
            }

            @Override
            public StyleableProperty<Font> getStyleableProperty(SelectableText node) {
                return (StyleableProperty<Font>)node.fontProperty();
            }
        };

        /**
         * 是否自动换行
         * -fx-text-wrap
         */
        private static final CssMetaData<SelectableText, Number> TEXT_WRAP = newCssMetaData(
          "-fx-text-wrap",
          BooleanConverter.getInstance(),
          0,
          "textWrap"
        );

        /**
         * 字间距
         * -fx-line-space
         */
        private static final CssMetaData<SelectableText, Number> TEXT_LINE_SPACE = newCssMetaData(
                "-fx-line-space",
                SizeConverter.getInstance(),
                0,
                "lineSpace"
        );

        /**
         * 选中后的文本的背景颜色
         * -fx-selected-fill: #a6d2ff
         */
        private final static CssMetaData<SelectableText, Paint> SELECTED_FILL = newCssMetaData(
                "-fx-selected-fill",
                PaintConverter.getInstance(),
                Color.web("#a6d2ff"),
                "selectedFill"
        );

        /**
         * 选中后文本颜色
         * -fx-selected-text-fill:
         */
        private final static CssMetaData<SelectableText, Paint> SELECTED_TEXT_FILL = newCssMetaData(
                "-fx-selected-text-fill",
                PaintConverter.getInstance(),
                Color.web("#190027"),
                "selectedTextFill"
        );

        /**
         * -fx-text-width
         */
        private static final CssMetaData<SelectableText, Number> TEXT_WIDTH = newCssMetaData(
                "-fx-text-width",
                SizeConverter.getInstance(),
                -1d,
                "textWidth"
        );

        /**
         * -fx-text-height
         */
        private static final CssMetaData<SelectableText, Number> TEXT_HEIGHT = newCssMetaData(
                "-fx-text-height",
                SizeConverter.getInstance(),
                -1d,
                "textHeight"
        );

        private static final CssMetaData<SelectableText,String> ELLIPSIS_STRING =
                new CssMetaData<SelectableText,String>("-fx-ellipsis-string",
                        StringConverter.getInstance(), "...") {

                    @Override public boolean isSettable(SelectableText n) {
                        return n.ellipsisString == null || !n.ellipsisString.isBound();
                    }

                    @Override public StyleableProperty<String> getStyleableProperty(SelectableText n) {
                        return (StyleableProperty<String>)(WritableValue<String>)n.ellipsisStringProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            styleables.add(TEXT_FILL);
            styleables.add(TEXT_STROKE);
            styleables.add(TEXT_STROKE_WIDTH);
            styleables.add(TEXT_STRIKE_THROUGH);
            styleables.add(TEXT_STROKE_TYPE);
            styleables.add(TEXT_STROKE_DASH_OFFSET);
            styleables.add(TEXT_STROKE_LINE_CAP);
            styleables.add(TEXT_SMOOTHING_TYPE);
            styleables.add(TEXT_ALIGNMENT);
            styleables.add(TEXT_ORIGIN);
            styleables.add(TEXT_UNDERLINE);
            styleables.add(FONT);
            styleables.add(TEXT_LINE_SPACE);
            styleables.add(SELECTED_FILL);
            styleables.add(SELECTED_TEXT_FILL);
            styleables.add(TEXT_WRAP);
            styleables.add(ELLIPSIS_STRING);
            styleables.add(TEXT_HEIGHT);
            styleables.add(TEXT_WIDTH);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * 构建一个css
     * type: 1: objectProperty
     *      2: booleanProperty
     *      3: DoubleProperty
     *      4: IntegerProperty
     *      5: StringProperty,
     */
    private static <T> CssMetaData<SelectableText,T> newCssMetaData(String cssName,
                                                                    StyleConverter convertor,
                                                                    T initValue,
                                                                    String propertyName){

        return new CssMetaData<SelectableText, T>(cssName, convertor, initValue) {
            @Override
            public boolean isSettable(SelectableText node) {
//                System.out.println(node.properties.get(propertyName));
//                if(type==1){
//                    ObjectProperty<T> property = (ObjectProperty<T>) node.properties.get(propertyName);
//                    return property == null || !property.isBound();
//                }else if(type==2){
//                    BooleanProperty property = (BooleanProperty) node.properties.get(propertyName);
//                    return property == null || !property.isBound();
//                }

                Property property = (Property) node.properties.get(propertyName);
                return property == null || !property.isBound();
            }

            @Override
            public StyleableProperty<T> getStyleableProperty(SelectableText node) {
                return (StyleableProperty<T>) node.properties.get(propertyName);
            }
        };
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SelectableText.StyleableProperties.STYLEABLES;
    }
}
