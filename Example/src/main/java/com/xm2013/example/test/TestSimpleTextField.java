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
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.textfield.XmSimplePasswordField;
import com.xm2013.jfx.control.textfield.XmSimpleTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestSimpleTextField extends Application {


    @Override
    public void start(Stage primaryStage) {
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        Pane pane = new Pane();

        XmSimpleTextField field = new XmSimpleTextField("this text field");
        field.setLayoutX(10);
        field.setLayoutY(10);
        field.setCleanable(true);
        field.setRoundType(RoundType.CIRCLE);
        field.setPadding(new Insets(10, 10d, 10, 10d ));
        field.setColorType(ColorType.other("linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #23d0f3ff 0.0%, #d791f9ff 50.0%, #fe7b84ff 100.0%)"));

        XmSimpleTextField field1 = new XmSimpleTextField("this");
        field1.setLayoutY(50);
        field1.setLayoutX(10);
        XmFontIcon icon = new XmFontIcon("\ue69a");
        field1.setSuffixIcon(icon);
        field1.setPadding(new Insets(10, 8, 10,8));

        XmSimplePasswordField field2 = new XmSimplePasswordField("this");
        field2.setLayoutY(100);
        field2.setLayoutX(10);
        XmFontIcon icon1 = new XmFontIcon("\uebcc");
        icon1.setCursor(Cursor.HAND);
        icon1.setOnMouseClicked(e -> {
            boolean visible = !field2.isShowPassword();
            icon1.setIcon(visible?"\uebcc":"\ueb45");
            field2.setShowPassword(visible);
            String text = field2.getText();;
            field2.setText(text+"a");
            field2.setText(text);
        });

        field2.setSuffixIcon(icon1);
        field2.setPadding(new Insets(10, 8, 10,8));

        pane.getChildren().addAll(field, field1, field2);

        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}