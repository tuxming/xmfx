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

import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.date.XmDateRangeSelector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestDateRangeSelector  extends Application {


    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        XmDateRangeSelector selector = new XmDateRangeSelector();
        selector.setPrefWidth(400);
        selector.setLayoutY(20);
        selector.setLayoutX(20);
        selector.setSizeType(SizeType.MEDIUM);

        XmButton btn = new XmButton("btn");
        btn.setLayoutY(20);
        btn.setLayoutX(430);
        btn.setSizeType(SizeType.MEDIUM);

        pane.getChildren().addAll(selector, btn);

        Scene scene = new Scene(pane, 900, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
