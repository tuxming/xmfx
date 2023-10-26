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

import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.pager.XmPager;
import com.xm2013.jfx.control.scroll.XmScrollBar;
import com.xm2013.jfx.control.textfield.XmSimpleTextField;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestPager extends Application {


    @Override
    public void start(Stage primaryStage) {

        XmSimpleTextField stf = new XmSimpleTextField();

        Pane pane = new Pane();

        XmPager pager = new XmPager(10000);
        pager.setShowQuickJumper(true);
        pager.setLayoutY(20);
        pager.setLayoutX(20);

        XmPager pager1 = new XmPager(10000);
        pager1.setSingleLine(false);
        pager1.setLayoutY(100);
        pager1.setSizeType(SizeType.MEDIUM);
        pager1.setHueType(HueType.DARK);
        pager1.setLayoutX(20);

//        XmButton btn = new XmButton("button");
//        btn.setSizeType(SizeType.MEDIUM);
//        btn.setLayoutX(700);
//        btn.setLayoutY(20);
//        btn.setOnAction(e -> {
//            Alert alert = new Alert(Alert.AlertType.WARNING, pager.getCurrIndex() + "", ButtonType.APPLY);
//            alert.show();
//        });


        pane.getChildren().addAll(pager, pager1);

        Scene scene = new Scene(pane, 1000, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}