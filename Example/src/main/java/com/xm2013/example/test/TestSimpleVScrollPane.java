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

import com.xm2013.jfx.container.SimpleVScrollPane;
import com.xm2013.jfx.control.base.ColorType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestSimpleVScrollPane extends Application {


    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        VBox box = new VBox();
        for(int i=1; i<=50; i++){
            Label label = new Label("label text "+i);
            label.setFont(Font.font(16));
            box.getChildren().add(label);
        }

        SimpleVScrollPane spane = new SimpleVScrollPane();
        spane.setViewHeight(100);
        spane.setViewWidth(150);
        spane.setContent(box);
        spane.setLayoutX(50);
        spane.setLayoutY(50);
        spane.getScrollBar().setColorType(ColorType.success());

        pane.getChildren().add(spane);

        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setX(1000);
        primaryStage.setY(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
