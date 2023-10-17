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
package com.xm2013.jfx.control.svg;

/*
 * MIT License
 *
 * Copyright (c) 2021 LeeWyatt
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

import com.xm2013.jfx.common.FxKit;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LeeWyatt
 * QQ: 9670453
 * QQ群: 518914410
 *
 * SVG组件. 可以显示多条路径, 已经分别填充颜色
 * XmSVGView view = new XmSVGView("&lt;svg path=""&gt;&lt;/svg&gt;");
 * XmSVGView view = new XmSVGView("M0,0L10,10Z");
 */
public class XmSVGView extends Control {
    private static final String DEFAULT_STYLE_CLASS = "xm-svg-view";

    private ObservableList<SVGPath> paths = FXCollections.observableArrayList();
    private boolean needParse = true;

    /* ----------------constructors--------------------*/
    public XmSVGView(){
        init();
    }
    public XmSVGView(String content){
        needParse = true;
        setContent(content);
        init();
    }

    public XmSVGView(String content, double size){
        needParse = true;
        setSize(size);
        setContent(content);
        init();
    }


    public XmSVGView(SVGPath path){
        needParse = false;
        setContent(path.getContent());
        paths.add(path);
        init();
    }

    public void init(){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /*----------------override------------------------*/

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmSVGViewSkin(this);
    }

    /* ------------------- properties --------------------*/

    private StringProperty content;
    /**
     * 设置svg的内容
     * 1: svg的path内容
     * 2: svg的xml文档内容
     * @param value String
     */
    public final void setContent(String value) {

        if(value == null || value.trim().length()==0){
            content.setValue(null);
            paths.clear();
            return;
        }

        if(needParse == false){
            contentProperty().set(value);
            return;
        }

        //如果是svg的xml文档
        if(value.endsWith("</svg>")){
            ArrayList<PathInfo> pathInfos = FxKit.parseSvg(value);
            for (PathInfo info : pathInfos) {
                SVGPath path = new SVGPath();
                path.setContent(info.getPathD());
                path.setFill(Color.valueOf(info.getPathFill()));
                path.getStyleClass().addAll("xm-svg", info.getPathId());
                path.setPickOnBounds(false);
                paths.add(path);
            }
        }else{//单个svg内容节点
            SVGPath path = new SVGPath();
            path.setContent(value);
            path.setFill(Color.BLACK);
            path.getStyleClass().addAll("xm-svg");
            path.setPickOnBounds(false);
            paths.add(path);
        }

        contentProperty().set(value);
    }

    /**
     * svg的内容
     * @return String
     */
    public final String getContent() {
        return content == null ? "" : content.get();
    }

    /**
     * 设置svg的内容
     * @param path SVGPath
     */
    public void setContent(SVGPath path){
        this.paths.add(path);
        needParse = false;
        setContent(path.getContent());
    }

    public final StringProperty contentProperty() {
        if (content == null) {
            content = new StringPropertyBase("") {
                @Override
                public Object getBean() {
                    return XmSVGView.this;
                }
                @Override
                public String getName() {
                    return "content";
                }
            };
        }
        return content;
    }

    /**
     * 获取svg的节点对象列表
     * @return ObservableList
     */
    public final ObservableList<SVGPath> getPaths(){
        return paths;
    }

    /**
     * svg大小
     */
    private DoubleProperty size = null;
    /**
     * 获取svg的大小
     * @return double
     */
    public double getSize() {
        return sizeProperty().get();
    }
    /**
     * 设置size的property
     * @return DoubleProperty
     */
    public DoubleProperty sizeProperty() {
        if(size == null){
            size = FxKit.newDoubleProperty(20d, StyleableProperties.SIZE, this, "size");
        }
        return size;
    }

    /**
     * 设置svg的大小
     * @param size double
     */
    public void setSize(double size) {
        this.sizeProperty().set(size);
    }

    /**
     * svg的颜色，请注意，这里设置颜色会将所有的svg的节点设置同一个颜色，所以先要保持svg的多色属性，请使用
     * getPaths获取SVGPath来单独为每一个设置节点
     */
    private ObjectProperty<Paint> color;

    /**
     * svg的颜色，请注意，这里设置颜色会将所有的svg的节点设置同一个颜色，所以先要保持svg的多色属性，请使用
     * getPaths获取SVGPath来单独为每一个设置节点
     * @return Paint
     */
    public Paint getColor() {
        return colorProperty().get();
    }

    /**
     * svg的颜色，请注意，这里设置颜色会将所有的svg的节点设置同一个颜色，所以先要保持svg的多色属性，请使用
     * getPaths获取SVGPath来单独为每一个设置节点
     * @return ObjectProperty
     */
    public ObjectProperty<Paint> colorProperty() {
        if(color== null){
            color = FxKit.newProperty(Color.BLACK, XmSVGView.StyleableProperties.COLOR, this, "color");
        }
        return color;
    }
    /**
     * svg的颜色，请注意，这里设置颜色会将所有的svg的节点设置同一个颜色，所以先要保持svg的多色属性，请使用
     * getPaths获取SVGPath来单独为每一个设置节点
     * @param color Paint
     */
    public void setColor(Paint color){
        colorProperty().set(color);
    }

    /*---------------------------css style ---------------------*/
    private static class StyleableProperties {

        /**
         * 图标大小的css
         * -fx-size: 16px;
         */
        private static final CssMetaData<XmSVGView, Number> SIZE = new CssMetaData<XmSVGView, Number>("-fx-size", SizeConverter.getInstance(), 20d) {
            @Override
            public boolean isSettable(XmSVGView node) {
                return node.size == null || !node.size.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(XmSVGView node) {
                return (StyleableProperty<Number>) node.sizeProperty();
            }
        };

        /**
         * 图标大小的颜色
         * -fx-color:
         */
        private static final CssMetaData<XmSVGView, Paint> COLOR = new CssMetaData<XmSVGView, Paint>("-fx-color", PaintConverter.getInstance(), null) {
            @Override
            public boolean isSettable(XmSVGView node) {
                return node.color == null || !node.color.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(XmSVGView node) {
                return (StyleableProperty<Paint>) node.colorProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {

            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(Node.getClassCssMetaData());
            styleables.add(SIZE);
            styleables.add(COLOR);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmSVGView.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

}
