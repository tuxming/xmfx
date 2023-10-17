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
package com.xm2013.example.example.page;

import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class LabelPage extends BasePage {

    public LabelPage() {

        this.setTitle("标签（XmLabel）", new XmFontIcon("\ue6fc"));
        this.setComponentTitle("可选标签");

        XmLabel label = new XmLabel("这个一个根据TextField可选中原理自己实现的一个可选中的文本标签，单行省略显示，多行省略显示");
        label.setPadding(new Insets(0, 0, 0, 20));

        this.setShowComponent(label);

        XmTextField singleField = new XmTextField("100");
        singleField.setSizeType(SizeType.SMALL);
        singleField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        singleField.setLabel("单行省略显示：");
        XmLabel suffix = new XmLabel("GO");
        singleField.setSuffix(suffix);
        this.addActionComponent(singleField);

        suffix.setOnMouseClicked(e -> {
            int width = -1;
            try{
                width = Integer.parseInt(singleField.getText());
            }catch (Exception e1){

            }
            label.setTextWidth(width);
            label.setWrapText(true);
            label.setMaxRow(1);

            getJavaText().setText("XmLabel label = new XmLabel(\"这个一个根据TextField可选中原理自己实现的一个可选中的文本标签，单行省略显示，多行省略显示\");\r\n" +
                    "label.setTextWidth("+width+");\r\n" +
                    "label.setWrapText(true);\r\n" +
                    "label.setMaxRow(1);");
        });

        VBox multiBox = new VBox();
        XmLabel multiLabel = new XmLabel("多行显示:");

        XmTextField widthField = new XmTextField("100");
        widthField.setSizeType(SizeType.SMALL);
        widthField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        widthField.setLabel("行宽：");

        XmTextField rowField = new XmTextField("2");
        rowField.setSizeType(SizeType.SMALL);
        rowField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        rowField.setLabel("行数：");

        XmButton btn = new XmButton("GO");
        btn.setSizeType(SizeType.SMALL);

        multiBox.setSpacing(10);
        multiBox.getChildren().addAll(multiLabel, widthField, rowField, btn);
        this.addActionComponent(multiBox);

        btn.setOnMouseClicked(e -> {
            int width = -1;
            try{
                width = Integer.parseInt(widthField.getText());
            }catch (Exception e1){

            }

            int row = 1;
            try{
                row = Integer.parseInt(rowField.getText());
            }catch (Exception e1){

            }

            label.setTextWidth(width);
            label.setWrapText(true);
            label.setMaxRow(row);

            getJavaText().setText("XmLabel label = new XmLabel(\"这个一个根据TextField可选中原理自己实现的一个可选中的文本标签，单行省略显示，多行省略显示\");\r\n" +
                    "label.setTextWidth("+width+");\r\n" +
                    "label.setWrapText(true);\r\n" +
                    "label.setMaxRow("+row+");");
        });


    }

}
