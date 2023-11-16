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
package com.xm2013.example.test;

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmSwitch;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.scroll.XmScrollBar;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestSwitch extends Application {


    @Override
    public void start(Stage primaryStage) {
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        Pane pane = new Pane();

        XmSwitch xs = new XmSwitch();
        xs.setSizeType(SizeType.SMALL);
        xs.setLayoutX(20);
        xs.setLayoutY(20);

        XmFontIcon inActiveNode = new XmFontIcon("\ueb45");
        inActiveNode.setLayoutX(32);
        inActiveNode.setLayoutY(11);
        inActiveNode.setColor(Color.web("#777777"));
        xs.setInactiveNode(inActiveNode);

        XmFontIcon activeNode = new XmFontIcon("\uebcc");
        activeNode.setLayoutY(11);
        activeNode.setLayoutX(10);
        activeNode.setColor(Color.WHITE);
        xs.setActiveNode(activeNode);

        XmSwitch xs1 = new XmSwitch();
        xs1.setHueType(HueType.DARK);
        xs1.setColorType(ColorType.danger());
        xs1.setActiveText("on");
        xs1.setInactiveText("off");
        xs1.setSizeType(SizeType.MEDIUM);
        xs1.setLayoutX(70);
        xs1.setLayoutY(20);

        XmSwitch xs2 = new XmSwitch();
        xs2.setColorType(ColorType.success());
        xs2.setSizeType(SizeType.LARGE);
        xs2.setLayoutX(130);
        xs2.setLayoutY(20);

        pane.getChildren().addAll(xs, xs1, xs2);
        pane.setStyle("-fx-background-color: white;");


        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}