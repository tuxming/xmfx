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
package com.xm2013.jfx.control.icon;

import com.xm2013.jfx.control.svg.XmSVGView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

/**
 * svg的xml文件内容，请注意svg目前只支持path节点，非path节点请不要上传
 */
public class XmSVGIcon extends XmIcon{

    public XmSVGIcon(){
        super();
    }

    public XmSVGIcon(String content){
        super();
        setContent(content);
    }

    public XmSVGIcon(String content, Color color){
        super();
        setColor(color);
        setContent(content);
    }

    public XmSVGIcon(String content, double size){
        super();
        setContent(content);
        setSize(size);
    }

    public XmSVGIcon(String content, Color color, double size){
        super();
        setSize(size);
        setColor(color);
        setContent(content);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmSvgIconSkin(this);
    }

    @Override
    public XmIcon clone() {
        XmIcon icon = new XmSVGIcon(contentProperty().get());
        icon.setColor(this.getColor());
        icon.setSize(this.getSize());
        return icon;
    }

    private ObjectProperty<XmSVGView> svgView = null;

    /**
     * svg的path内容
     */
    private StringProperty content;
    public String getContent() {
        return contentProperty().get();
    }
    public StringProperty contentProperty() {
        if(content == null){
            content = new SimpleStringProperty();
        }
        return content;
    }

    public void setContent(String content) {
        svgViewProperty().get().setContent(content);
        this.contentProperty().set(content);
    }

    public XmSVGView getSvgView(){
        return svgViewProperty().get();
    }

    public ObjectProperty<XmSVGView> svgViewProperty(){
        if(svgView == null){
            svgView = new SimpleObjectProperty<>(new XmSVGView());
        }
        return svgView;
    }
}
