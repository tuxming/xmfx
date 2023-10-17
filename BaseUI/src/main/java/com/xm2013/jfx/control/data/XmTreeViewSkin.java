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
package com.xm2013.jfx.control.data;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import javafx.scene.control.skin.TreeViewSkin;
import javafx.scene.paint.Color;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class XmTreeViewSkin<T> extends TreeViewSkin<T> {

    private String cssTemplate = "" +
            ".xm-tree-view{" +
            "   -fx-background-color: %s, %s;" +    //设置背景色
            "   -fx-fixed-cell-size: %f;" +         //设置行高
            "}" +
            ".xm-tree-view:focused {" +
            "    -fx-background-color: %s, %s, %s;" +   //设置获取到焦点以后，listview整体会有个边框
            "}" +
            ".xm-tree-view > .virtual-flow > .scroll-bar .increment-button:hover > .increment-arrow," +
            ".xm-tree-view > .virtual-flow > .scroll-bar .decrement-button:hover > .decrement-arrow{" +
            "    -fx-background-color: %s;" +   //设置鼠标放在滚动条两边的箭头上的箭头颜色
            "}" +
            ".xm-tree-view > .virtual-flow > .scroll-bar .track-background{" +
            "    -fx-background-color: %s;" +   //设置滚动条滑道的颜色
            "}" +
            ".xm-tree-view > .virtual-flow > .scroll-bar .thumb{" +
            "    -fx-background-color: %s;" +   //设置滚动条的滑块的颜色
            "}" +
            ".xm-tree-view > .virtual-flow > .scroll-bar .thumb:hover{" +
            "    -fx-background-color: %s;" +       //设置鼠标放在滚动条的滑块上的颜色
            "}" +
            ".xm-tree-view > .virtual-flow > .scroll-bar .increment-button > .increment-arrow," +
            ".xm-tree-view > .virtual-flow > .scroll-bar .decrement-button > .decrement-arrow{" +
            "    -fx-background-color: %s;" +      //设置鼠标放在滚动条两边的箭头上的箭头颜色
            "}"
            +".xm-tree-view .tree-cell{" +
            "    -fx-background-color: transparent;" +       //设置listview每一行的背景颜色
            "    -fx-text-fill: %s;" +              //设置listview每一行的字体颜色
            "}"
            +
            ".xm-tree-view .tree-cell:hover{" +
            "    -fx-text-fill: %s;" +              //设置鼠标放在每一行上面时的字体颜色
            "}"
            +
            ".xm-tree-view .tree-cell:selected{" +
            "    -fx-text-fill: %s;" +              //设置鼠标放在每一行上面时的字体颜色
            "}"
            +
            ".xm-tree-view .tree-cell:selected:hover{" +
            "    -fx-text-fill: %s;" +              //设置鼠标放在每一行上面时的字体颜色
            "}"
          ;

    private final XmTreeView<T> control;
    public XmTreeViewSkin(XmTreeView<T> control) {
        super(control);
        this.control = control;

        updateSkin();
        registerChangeListener(control.sizeTypeProperty(), e -> updateSkin());
        registerChangeListener(control.colorTypeProperty(), e->updateSkin());
        registerChangeListener(control.hueTypeProperty(),  e->updateSkin());
    }

    private void updateSkin(){
        boolean isDark = HueType.DARK.equals(control.getHueType());

        Color color = (Color) control.getColorType().getPaint();
        Color scrollBarArrowHoverColor = FxKit.getOpacityColor(color, isDark?1:0.85),
                scrollBarArrowColor = FxKit.getOpacityColor(color, isDark?0.75: 0.5),
                scrollBarThumbHoverColor =  FxKit.getOpacityColor(color, isDark?1:0.85),
                scrollBarThumbColor = FxKit.getOpacityColor(color, isDark?0.75: 0.5)
        ;

        double size =  35;
        if(SizeType.SMALL.equals(control.getSizeType())){
            size = 20;
        }else if(SizeType.LARGE.equals(control.getSizeType())){
            size = 45;
        }

        String scrollBarBgColor = FxKit.formatHexString(FxKit.getLightColor(color, 0.95));
        String bgColor = "white";
        String selectedColor = "white";
        String textColor = "#333333";

        if(isDark){
            bgColor = "transparent";
            textColor = "white";
            scrollBarBgColor = "#000000ee";
        }


        String css = String.format(cssTemplate,
                bgColor, bgColor, size,
                bgColor, bgColor, bgColor,
                FxKit.formatHexString(scrollBarArrowHoverColor),
                scrollBarBgColor,
                FxKit.formatHexString(scrollBarThumbColor),
                FxKit.formatHexString(scrollBarThumbHoverColor),
                FxKit.formatHexString(scrollBarArrowColor),
                textColor,
                control.getColorType().getColor(),
                selectedColor,
                selectedColor
        );

        control.getStylesheets().clear();
        control.getStylesheets().addAll(
                XmTreeView.STYLESHEETS,
                "data:text/css;charset=utf-8;base64,"+
                        Base64.getEncoder().encodeToString(css.getBytes(StandardCharsets.UTF_8))
        );

    }


}
