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
import com.xm2013.jfx.control.base.XmAlignment;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmLabel extends SelectableText {

    /*--------------------Constructor-------------------*/
    public XmLabel() {
        initialize(null, null);
    }

    public XmLabel(String text) {
        initialize(text, null);
    }

    public XmLabel(String text, Node graphic) {
        initialize(text, graphic);
    }

    private void initialize(String text, Node graphic) {
        getStyleClass().setAll("xm-text");
        setAccessibleRole(AccessibleRole.TEXT);
        ((StyleableProperty<Boolean>)(WritableValue<Boolean>)focusTraversableProperty()).applyStyle(null, Boolean.FALSE);

        setText(text);
        setGraphic(graphic);

    }

    @Override protected Skin<?> createDefaultSkin() {
        return new XmLabelSkin(this);
    }

    public String getUserAgentStylesheet() {
        return FxKit.USER_AGENT_STYLESHEET;
    }

    /*--------------------Properties-------------------*/

    /**
     * 要显示在标签中的文本。文本可以为空。
     * @return
     */
//    private StringProperty text;
//    public void setText(String value) { textProperty().setValue(value); }
//    public String getText() { return text == null ? "" : text.getValue(); }
//    public final StringProperty textProperty() {
//        if (text == null) {
//            text = new SimpleStringProperty(this, "text", "");
//        }
//        return text;
//    }

    /**
     * 当设置了 宽，高以后，实际类容小于宽高的时候，设置实际内容相对于容器的对齐方式
     * 默认： XmAlignment.LEFT
     * @return ObjectProperty
     */
    public final ObjectProperty<XmAlignment> alignmentProperty() {
        if (alignment == null) {
            alignment = FxKit.newProperty(XmAlignment.LEFT, StyleableProperties.ALIGNMENT, this, "alignment");
        }
        return alignment;
    }
    private ObjectProperty<XmAlignment> alignment;
    public final void setAlignment(XmAlignment value) { alignmentProperty().set(value); }
    public final XmAlignment getAlignment() { return alignment == null ? XmAlignment.LEFT : alignment.get(); }

    /**
     * 设置文本的对齐方式，文本中包含换行的时候，可以看出文本的对齐方式
     * TextAlignment.LEFT
     * @return
     */
//    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
//        if (textAlignment == null) {
//            textAlignment = FxKit.newProperty(TextAlignment.LEFT, StyleableProperties.TEXT_ALIGNMENT, this, "textAlignment");
//        }
//        return textAlignment;
//    }
//    private ObjectProperty<TextAlignment> textAlignment;
//    public final void setTextAlignment(TextAlignment value) { textAlignmentProperty().setValue(value); }
//    public final TextAlignment getTextAlignment() { return textAlignment == null ? TextAlignment.LEFT : textAlignment.getValue(); }

    /**
     * 要显示的文本截断时要显示的符号
     * defaultValue: "..."
     * @return
     */
//    public final StringProperty ellipsisStringProperty() {
//        if (ellipsisString == null) {
//            ellipsisString = FxKit.newStringProperty("...", StyleableProperties.ELLIPSIS_STRING, this, "ellipsisString");
//        }
//        return ellipsisString;
//    }
//    private StringProperty ellipsisString;
//    public final void setEllipsisString(String value) { ellipsisStringProperty().set((value == null) ? "" : value); }
//    public final String getEllipsisString() { return ellipsisString == null ? "..." : ellipsisString.get(); }

    /**
     * 文本是否自动换行，如果为true, 又设置prefWidth, 文本的宽超过了prefWidth，则会自动换行
     * defaultValue: false
     * @return
     */
//    public final BooleanProperty wrapTextProperty() {
//        if (wrapText == null) {
//            wrapText = FxKit.newBooleanProperty(false, StyleableProperties.WRAP_TEXT, this, "wrapText");
//        }
//        return wrapText;
//    }
//    private BooleanProperty wrapText;
//    public final void setWrapText(boolean value) { wrapTextProperty().setValue(value); }
//    public final boolean isWrapText() { return wrapText == null ? false : wrapText.getValue(); }

//    public final ObjectProperty<Font> fontProperty() {
//        if (font == null) {
//            font = new StyleableObjectProperty<Font>(Font.getDefault()) {
//                private boolean fontSetByCss = false;
//                @Override
//                public void applyStyle(StyleOrigin newOrigin, Font value) {
//                    try {
//                        fontSetByCss = true;
//                        super.applyStyle(newOrigin, value);
//                    } catch(Exception e) {
//                        throw e;
//                    } finally {
//                        fontSetByCss = false;
//                    }
//                }
//                @Override
//                public void set(Font value) {
//
//                    final Font oldValue = get();
//                    if (value != null ? !value.equals(oldValue) : oldValue != null) {
//                        super.set(value);
//                    }
//                }
//                @Override
//                protected void invalidated() {
//                }
//                @Override
//                public CssMetaData<XmLabel,Font> getCssMetaData() {
//                    return XmLabel.StyleableProperties.FONT;
//                }
//                @Override
//                public Object getBean() {
//                    return XmLabel.this;
//                }
//                @Override
//                public String getName() {
//                    return "font";
//                }
//            };
//        }
//        return font;
//    }
//    private ObjectProperty<Font> font;
//    public final void setFont(Font value) { fontProperty().setValue(value); }
//    public final Font getFont() { return font == null ? Font.getDefault() : font.getValue(); }


    /**
     * 可选图标。这可以通过使用setContentDisplay相对于文本进行定位。为该变量指定的节点不能出现在场景图的其他位置
     * @return ObjectProperty
     */
    public final ObjectProperty<Node> graphicProperty() {
        if (graphic == null) {
            graphic = new SimpleObjectProperty<>(null);
        }
        return graphic;
    }
    private ObjectProperty<Node> graphic;
    public final void setGraphic(Node value) {
        graphicProperty().setValue(value);
    }
    public final Node getGraphic() { return graphic == null ? null : graphic.getValue(); }

    /**
     * 此标签中所有文本的下划线属性
     * @return
     */
//    public final BooleanProperty underlineProperty() {
//        if (underline == null) {
//            underline = FxKit.newBooleanProperty(false, StyleableProperties.UNDERLINE, this, "underline");
//        }
//        return underline;
//    }
//    private BooleanProperty underline;
//    public final void setUnderline(boolean value) { underlineProperty().setValue(value); }
//    public final boolean isUnderline() { return underlineProperty().get(); }

    /**
     * 字间距
     * @return
     */
//    public final DoubleProperty lineSpacingProperty() {
//        if (lineSpacing == null) {
//            lineSpacing = FxKit.newDoubleProperty(0d, StyleableProperties.LINE_SPACING, this, "lineSpacing");
//        }
//        return lineSpacing;
//    }
//    private DoubleProperty lineSpacing;
//    public final void setLineSpacing(double value) { lineSpacingProperty().setValue(value); }
//    public final double getLineSpacing() { return lineSpacing == null ? 0 : lineSpacing.getValue(); }

    /**
     * 指定图形相对于文字的位置
     * @return ObjectProperty
     */
    public final ObjectProperty<ContentDisplay> contentDisplayProperty() {
        if (contentDisplay == null) {
            contentDisplay = FxKit.newProperty(ContentDisplay.LEFT, StyleableProperties.CONTENT_DISPLAY, this, "contentDisplay");
        }
        return contentDisplay;
    }
    private ObjectProperty<ContentDisplay> contentDisplay;
    public final void setContentDisplay(ContentDisplay value) { contentDisplayProperty().setValue(value); }
    public final ContentDisplay getContentDisplay() { return contentDisplay == null ? ContentDisplay.LEFT : contentDisplay.getValue(); }

    /**
     * 图形和文本之间的距离
     * @return DoubleProperty
     */
    public final DoubleProperty graphicTextGapProperty() {
        if (graphicTextGap == null) {
            graphicTextGap = FxKit.newDoubleProperty(4d, StyleableProperties.GRAPHIC_TEXT_GAP, this, "graphicTextGap");
        }
        return graphicTextGap;
    }
    private DoubleProperty graphicTextGap;
    public final void setGraphicTextGap(double value) { graphicTextGapProperty().setValue(value); }
    public final double getGraphicTextGap() { return graphicTextGap == null ? 4 : graphicTextGap.getValue(); }

//    private ObjectProperty<Paint> textFill; // TODO for now change this
//
//    public final void setTextFill(Paint value) {
//        textFillProperty().set(value);
//    }
//
//    public final Paint getTextFill() {
//        return textFill == null ? Color.BLACK : textFill.get();
//    }
//
//    public final ObjectProperty<Paint> textFillProperty() {
//        if (textFill == null) {
//            textFill = FxKit.newProperty(Color.BLACK, StyleableProperties.TEXT_FILL, this, "textFill");
//        }
//        return textFill;
//    }

    @Override public String toString() {
        StringBuilder builder =
                new StringBuilder(super.toString())
                        .append("'").append(getText()).append("'");
        return builder.toString();
    }

    /*----------------------css-------------------------------*/
    protected XmAlignment getInitialAlignment() {
        return XmAlignment.LEFT;
    }

    private static class StyleableProperties {
//        private static final FontCssMetaData<XmLabel> FONT =
//                new FontCssMetaData<XmLabel>("-fx-font", Font.getDefault()) {
//
//                    @Override
//                    public boolean isSettable(XmLabel n) {
//                        return n.font == null || !n.font.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<Font> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<Font>)(WritableValue<Font>)n.fontProperty();
//                    }
//                };

        private static final CssMetaData<XmLabel,XmAlignment> ALIGNMENT =
                new CssMetaData<XmLabel,XmAlignment>("-fx-alignment",
                        new EnumConverter<XmAlignment>(XmAlignment.class), XmAlignment.LEFT ) {

                    @Override
                    public boolean isSettable(XmLabel n) {
                        return n.alignment == null || !n.alignment.isBound();
                    }

                    @Override
                    public StyleableProperty<XmAlignment> getStyleableProperty(XmLabel n) {
                        return (StyleableProperty<XmAlignment>)(WritableValue<XmAlignment>)n.alignmentProperty();
                    }

                    @Override
                    public XmAlignment getInitialValue(XmLabel n) {
                        return n.getInitialAlignment();
                    }
                };

//        private static final CssMetaData<XmLabel, TextAlignment> TEXT_ALIGNMENT =
//                new CssMetaData<XmLabel,TextAlignment>("-fx-text-alignment",
//                        new EnumConverter<TextAlignment>(TextAlignment.class),
//                        TextAlignment.LEFT) {
//
//                    @Override
//                    public boolean isSettable(XmLabel n) {
//                        return n.textAlignment == null || !n.textAlignment.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<TextAlignment> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<TextAlignment>)(WritableValue<TextAlignment>)n.textAlignmentProperty();
//                    }
//                };

//        private static final CssMetaData<XmLabel, Paint> TEXT_FILL =
//                new CssMetaData<XmLabel,Paint>("-fx-text-fill",
//                        PaintConverter.getInstance(), Color.BLACK) {
//
//                    @Override
//                    public boolean isSettable(XmLabel n) {
//                        return n.textFill == null || !n.textFill.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<Paint> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<Paint>)(WritableValue<Paint>)n.textFillProperty();
//                    }
//                };

//        private static final CssMetaData<XmLabel,String> ELLIPSIS_STRING =
//                new CssMetaData<XmLabel,String>("-fx-ellipsis-string",
//                        StringConverter.getInstance(), "...") {
//
//                    @Override public boolean isSettable(XmLabel n) {
//                        return n.ellipsisString == null || !n.ellipsisString.isBound();
//                    }
//
//                    @Override public StyleableProperty<String> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<String>)(WritableValue<String>)n.ellipsisStringProperty();
//                    }
//                };

//        private static final CssMetaData<XmLabel,Boolean> WRAP_TEXT =
//                new CssMetaData<XmLabel,Boolean>("-fx-wrap-text",
//                        BooleanConverter.getInstance(), false) {
//
//                    @Override
//                    public boolean isSettable(XmLabel n) {
//                        return n.wrapText == null || !n.wrapText.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<Boolean> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<Boolean>)(WritableValue<Boolean>)n.wrapTextProperty();
//                    }
//                };

//        private static final CssMetaData<XmLabel,Boolean> UNDERLINE =
//                new CssMetaData<XmLabel,Boolean>("-fx-underline",
//                        BooleanConverter.getInstance(), Boolean.FALSE) {
//
//                    @Override
//                    public boolean isSettable(XmLabel n) {
//                        return n.underline == null || !n.underline.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<Boolean> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<Boolean>)(WritableValue<Boolean>)n.underlineProperty();
//                    }
//                };

//        private static final CssMetaData<XmLabel,Number> LINE_SPACING =
//                new CssMetaData<XmLabel,Number>("-fx-line-spacing",
//                        SizeConverter.getInstance(), 0) {
//
//                    @Override
//                    public boolean isSettable(XmLabel n) {
//                        return n.lineSpacing == null || !n.lineSpacing.isBound();
//                    }
//
//                    @Override
//                    public StyleableProperty<Number> getStyleableProperty(XmLabel n) {
//                        return (StyleableProperty<Number>)(WritableValue<Number>)n.lineSpacingProperty();
//                    }
//                };

        private static final CssMetaData<XmLabel,ContentDisplay> CONTENT_DISPLAY =
                new CssMetaData<XmLabel,ContentDisplay>("-fx-content-display",
                        new EnumConverter<ContentDisplay>(ContentDisplay.class),
                        ContentDisplay.LEFT) {

                    @Override
                    public boolean isSettable(XmLabel n) {
                        return n.contentDisplay == null || !n.contentDisplay.isBound();
                    }

                    @Override
                    public StyleableProperty<ContentDisplay> getStyleableProperty(XmLabel n) {
                        return (StyleableProperty<ContentDisplay>)(WritableValue<ContentDisplay>)n.contentDisplayProperty();
                    }
                };

        private static final CssMetaData<XmLabel,Number> GRAPHIC_TEXT_GAP =
                new CssMetaData<XmLabel,Number>("-fx-graphic-text-gap",
                        SizeConverter.getInstance(), 4.0) {

                    @Override
                    public boolean isSettable(XmLabel n) {
                        return n.graphicTextGap == null || !n.graphicTextGap.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(XmLabel n) {
                        return (StyleableProperty<Number>)(WritableValue<Number>)n.graphicTextGapProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<CssMetaData<? extends Styleable, ?>>(SelectableText.getClassCssMetaData());
            Collections.addAll(styleables,
//                    FONT,
                    ALIGNMENT,
//                    TEXT_ALIGNMENT,
//                    TEXT_FILL,
//                    ELLIPSIS_STRING,
//                    WRAP_TEXT,
//                    UNDERLINE,
//                    LINE_SPACING,
                    CONTENT_DISPLAY,
                    GRAPHIC_TEXT_GAP
            );
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
