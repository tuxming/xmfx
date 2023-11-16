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

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BasePage extends ScrollPane {
    //主要内容布局
    private VBox contentPane = null;

    //页面标题
    private Label titleLabel = null;

    //自定义组件标题
    private Label componentTitleLabel = null;

    private HBox componentPane = null;

    private VBox componentSettingPane;
    private VBox componentShowPane;

    private TextArea javaText, cssText;
    private XmButton runBtn, clearBtn;

    public BasePage(){
        contentPane = new VBox();
        this.setContent(contentPane);
        this.setFitToWidth(true);

        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight:bold; -fx-padding: 15px 15px 10px 15px; -fx-text-fill: #333333;");
        Separator hs = new Separator();
        hs.setOrientation(Orientation.HORIZONTAL);

        componentTitleLabel = new Label();
        componentTitleLabel.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-padding: 10px 15px 15px 25px; -fx-text-fill: #555555;");

        componentPane = new HBox();
        componentPane.setStyle("-fx-padding: 0px 25px;");
        componentSettingPane = new VBox();
        componentSettingPane.setFillWidth(true);
        componentSettingPane.setAlignment(Pos.TOP_LEFT);
        componentSettingPane.setSpacing(10);

        componentShowPane = new VBox();
        HBox.setHgrow(componentShowPane, Priority.ALWAYS);
        componentShowPane.setAlignment(Pos.CENTER);
        componentShowPane.setFillWidth(false);
//        componentShowPane.setStyle("-fx-alignment: center;");
//        componentShowPane.setStyle("-fx-background-color: red;");

        componentPane.getChildren().addAll(componentSettingPane, componentShowPane);

        Label codeLabel = new Label("代码");
        codeLabel.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-padding: 10px 15px 15px 25px; -fx-text-fill: #555555;");

        javaText = new TextArea();
        cssText = new TextArea();

        runBtn = new XmButton("运行");
        runBtn.setHueType(HueType.LIGHT);
        runBtn.setColorType(ColorType.success());
        runBtn.setSizeType(SizeType.SMALL);

        clearBtn = new XmButton("清除");
        clearBtn.setColorType(ColorType.secondary());
        clearBtn.setSizeType(SizeType.SMALL);

        HBox btnBox = new HBox(runBtn, clearBtn);
        btnBox.setPadding(new Insets(5, 0, 0, 0));
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        VBox cssBox = new VBox(cssText, btnBox);

        HBox codePane = new HBox(javaText, cssBox);
        codePane.setStyle("-fx-padding: 0px 15px;");
        codePane.setSpacing(15);

        contentPane.getChildren().addAll(titleLabel, hs, componentTitleLabel, componentPane, codeLabel, codePane);

    }

    protected void setTitle(String text, XmFontIcon icon) {
        icon.setSize(30);
        this.titleLabel.setText(text);
        this.titleLabel.setGraphic(icon);
    }


    protected void setComponentTitle(String text) {
        this.componentTitleLabel.setText(text);
    }

    protected void setShowComponent(Node node) {
        this.componentShowPane.getChildren().setAll(node);
    }

    protected VBox getComponentShowPane(){
        return componentShowPane;
    }

    public TextArea getJavaText() {
        return javaText;
    }

    public TextArea getCssText() {
        return cssText;
    }

    public XmButton getRunBtn() {
        return runBtn;
    }

    public XmButton getClearBtn() {
        return clearBtn;
    }

    protected void addActionComponent(Node node) {
        componentSettingPane.getChildren().add(node);
    }

    public VBox getComponentSettingPane(){
        return componentSettingPane;
    }

}
