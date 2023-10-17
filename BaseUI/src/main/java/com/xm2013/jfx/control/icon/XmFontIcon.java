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

import com.xm2013.jfx.common.FxKit;
import javafx.beans.property.*;
import javafx.geometry.VPos;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * 字体图标，iconfont，
 * 使用该图标前,必须调用setIconFont方法，设置icon的.ttf文件，
 * 或者在字体实例化之后调用setFont,但是只能使用一次
 *  String path = "";
 *  path = getClass().getResource(path).toExternalForm();
 *  XmFontIcon.setIconFont(path);
 *
 *  XmFontIcon icon = new XmFontIcon("\ue663");
 *  字体样式：
 *      -xm-size:
 *      -xm-color:
 */
public class XmFontIcon extends XmIcon {
    private final static String USER_AGENT_STYLESHEET = FxKit.getResourceURL("/css/control.css");

    private static Font iconFont = null;

    /**
     * @param iconfontPath 要加载的字体路径
     */
    public static void setIconFont(String iconfontPath) {
        iconFont = Font.loadFont(iconfontPath, 20);
    }
    public static Font getIconFont(){return iconFont;}

    public XmFontIcon(){
        this(null);
    }

    public XmFontIcon(String icon){
        getStyleClass().add("xm-icon");
        iconProperty().set(icon);
    }

    /******************styles **********************/

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmFontIconSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

    /*****************Properties********************/
    /**
     * 图标代码，格式如：\ue660,
     */
    private StringProperty icon = null;
    public void setIcon(String icon){
        iconProperty().set(icon);
    }
    public String getIcon(){
        return iconProperty().get();
    }
    public StringProperty iconProperty(){
        if(icon == null){
            icon = new SimpleStringProperty(null);
        }
        return icon;
    }

    /**
     * 图标字体
     */
    private ObjectProperty<Font> font;
    public final void setFont(Font value) { fontProperty().setValue(value); }
    public final Font getFont() { return font == null ? Font.getDefault() : font.getValue(); }
    public final ObjectProperty<Font> fontProperty() {
        if(font==null){
            font = new SimpleObjectProperty<>(iconFont);
        }
        return font;
    }

    private ObjectProperty<VPos> textOrigin;
    public VPos getTextOrigin() {
        return textOriginProperty().get();
    }
    public ObjectProperty<VPos> textOriginProperty() {
        if(textOrigin == null){
            textOrigin = new SimpleObjectProperty<>(VPos.BASELINE);
        }
        return textOrigin;
    }
    public void setTextOrigin(VPos textOrigin) {
        this.textOriginProperty().set(textOrigin);
    }

    @Override
    public XmIcon clone() {
        XmFontIcon icon = new XmFontIcon();
        icon.setIcon(this.getIcon());
//        System.out.println(this.getFont().getFamily());
//        icon.setFont(Font.font(this.getFont().getFamily()));
        icon.setSize(this.getSize());
        return icon;
    }

}
