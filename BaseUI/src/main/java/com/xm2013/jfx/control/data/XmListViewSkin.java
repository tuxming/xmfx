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
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.paint.Paint;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class XmListViewSkin<T> extends ListViewSkin<T> {
    private String cssTemplate = "" +
            ".xm-list-view{" +
            "   -fx-background-color: %s, %s;" +    //设置背景色
            "   -fx-fixed-cell-size: %f;" +         //设置行高
            "}" +
            ".xm-list-view:focused {" +
            "    -fx-background-color: %s, %s, %s;" +   //设置获取到焦点以后，listview整体会有个边框
            "}" +
            ".xm-list-view:focused > .virtual-flow > .clipped-container > .sheet > .list-cell:filled:selected{" +
            "    -fx-background-color: %s;" +       //设置选中后的行的背景颜色
            "    -fx-text-fill: %s;" +              //设置选中后的行的文本颜色
            "}" +
            ".xm-list-view .list-cell:filled:selected:focused," +
            ".xm-list-view .list-cell:filled:selected," +
            ".xm-list-view > .virtual-flow > .clipped-container > .sheet > .list-cell:filled:selected," +
            ".xm-list-view > .virtual-flow > .clipped-container > .sheet > .list-cell:filled:selected:hover {" +
            "    -fx-background-color: %s;" +       //设置在失去焦点的情况下，选中后行的背景颜色
            "    -fx-text-fill: %s;" +              //设置在失去焦点的情况下，选中后行的文本颜色
            "}" +
            ".xm-list-view > .virtual-flow > .clipped-container > .sheet > .list-cell:filled:hover {" +
            "    -fx-background-color: %s;" +       //设置鼠标所在行的颜色
            "}" +
            ".list-view > .virtual-flow > .scroll-bar .increment-button:hover > .increment-arrow," +
            ".list-view > .virtual-flow > .scroll-bar .decrement-button:hover > .decrement-arrow{" +
            "    -fx-background-color: %s;" +   //设置鼠标放在滚动条两边的箭头上的箭头颜色
            "}" +
            ".list-view > .virtual-flow > .scroll-bar .track-background{" +
            "    -fx-background-color: %s;" +   //设置滚动条滑道的颜色
            "}" +
            ".list-view > .virtual-flow > .scroll-bar .thumb{" +
            "    -fx-background-color: %s;" +   //设置滚动条的滑块的颜色
            "}" +
            ".list-view > .virtual-flow > .scroll-bar .thumb:hover{" +
            "    -fx-background-color: %s;" +       //设置鼠标放在滚动条的滑块上的颜色
            "}" +
            ".list-view > .virtual-flow > .scroll-bar .increment-button > .increment-arrow," +
            ".list-view > .virtual-flow > .scroll-bar .decrement-button > .decrement-arrow{" +
            "    -fx-background-color: %s;" +      //设置鼠标放在滚动条两边的箭头上的箭头颜色
            "}" +
            ".xm-list-view .list-cell{" +
            "    -fx-background-color: %s;" +       //设置listview每一行的背景颜色
            "    -fx-text-fill: %s;" +              //设置listview每一行的字体颜色
            "}" +
            ".xm-list-view .list-cell:hover{" +
            "    -fx-text-fill: %s;" +              //设置鼠标放在每一行上面时的字体颜色
            "}" +
            ".xm-list-view .list-cell:odd {" +
            "    -fx-background-color: %s;" +       //设置偶数行的背景颜色
            "}";

    private XmListView<T> control;

    public XmListViewSkin(XmListView<T> control) {
        super(control);
        this.control = control;

        updateSkin();

        registerChangeListener(control.sizeTypeProperty(), e -> updateSkin());
        registerChangeListener(control.colorTypeProperty(), e->updateSkin());
        registerChangeListener(control.hueTypeProperty(),  e->updateSkin());

    }

    private void updateSkin(){
        Paint color = control.getColorType().getPaint();
        Paint hoverColor = FxKit.getOpacityPaint(color, 0.1),
                scrollBarArrowHoverColor = FxKit.getOpacityPaint(color, 0.5),
                scrollBarArrowColor = FxKit.getOpacityPaint(color, 0.85),
                scrollBarThumbHoverColor = FxKit.getOpacityPaint(color, 0.5),
                scrollBarThumbColor =  FxKit.getOpacityPaint(color, 0.85)
        ;

        double size =  35;
        if(SizeType.SMALL.equals(control.getSizeType())){
            size = 20;
        }else if(SizeType.LARGE.equals(control.getSizeType())){
            size = 45;
        }

        String scrollBarBgColor = FxKit.getLightPaint(color, 0.95).toString().replace("0x", "#");
        String bgColor = "white";
        String cellBgColor =  "white";
        String cellOddBgColor =
                FxKit.getOpacityPaint(FxKit.derivePaint(color, -0.3), 0.05)
                        .toString().replace("0x", "#");
        String textColor = "#333333";
        String textHoverColor = "#333333";

        if(HueType.DARK.equals(control.getHueType())){
            bgColor = "transparent";
            cellBgColor = "transparent";
            textColor = "white";
            textHoverColor = "white";
            hoverColor = FxKit.derivePaint(color,-0.5);
            scrollBarBgColor = "#000000ee";
        }

        String selectedColor = color.toString().replace("0x", "#"),
                selectedTextColor = "white";

        String css = String.format(cssTemplate,
                bgColor, bgColor, size,
                bgColor, bgColor, bgColor,
                selectedColor, selectedTextColor,
                selectedColor, selectedTextColor,
                hoverColor.toString().replace("0x", "#"),
                scrollBarArrowColor.toString().replace("0x", "#"),
                scrollBarBgColor,
                scrollBarThumbHoverColor.toString().replace("0x", "#"),
                scrollBarThumbColor.toString().replace("0x", "#"),
                scrollBarArrowHoverColor.toString().replace("0x", "#"),
                cellBgColor, textColor,
                textHoverColor,
                cellOddBgColor
            );

        control.getStylesheets().clear();
        control.getStylesheets().addAll(
                XmListView.STYLESHEETS,
                "data:text/css;charset=utf-8;base64,"+
                Base64.getEncoder().encodeToString(css.getBytes(StandardCharsets.UTF_8))
        );

    }

    @Override
    public void dispose() {
        super.dispose();

        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.hueTypeProperty());
    }
}
