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
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.date.DateType;
import com.xm2013.jfx.control.date.XmDatePicker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class TestDatePane extends Application {


    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        XmCheckBox<DateType> timeCb = new XmCheckBox<>(DateType.TIME.toString());
        timeCb.setValue(DateType.TIME);

        XmCheckBox<DateType> dateCb = new XmCheckBox<>(DateType.DATE.toString());
        dateCb.setValue(DateType.DATE);

        XmCheckBox<DateType> dateTimeCb = new XmCheckBox<>(DateType.DATETIME.toString());
        dateTimeCb.setValue(DateType.DATETIME);

        XmCheckBox<DateType> monthCb = new XmCheckBox<>(DateType.MONTH.toString());
        monthCb.setValue(DateType.MONTH);

        XmCheckBox<DateType> yearCb = new XmCheckBox<>(DateType.YEAR.toString());
        yearCb.setValue(DateType.YEAR);

        XmToggleGroup<DateType> tg = new XmToggleGroup<>();
        timeCb.setToggleGroup(tg);
        dateCb.setToggleGroup(tg);
        dateTimeCb.setToggleGroup(tg);
        monthCb.setToggleGroup(tg);
        yearCb.setToggleGroup(tg);

        timeCb.setLayoutY(260);
        dateCb.setLayoutY(330);
        dateTimeCb.setLayoutY(330);
        monthCb.setLayoutY(330);
        yearCb.setLayoutY(330);

        timeCb.setLayoutX(20);
        dateCb.setLayoutX(100);
        dateTimeCb.setLayoutX(180);
        monthCb.setLayoutX(300);
        yearCb.setLayoutX(400);

        XmDatePicker datePicker = new XmDatePicker();
        datePicker.setStyle("-fx-background-color: white;");
        datePicker.setColorType(ColorType.danger());
        datePicker.setDate(LocalDateTime.of(2009,8,31, 22, 10,1));
        datePicker.setPrefWidth(400);
        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            datePicker.setDateType(nv.getValue());
        });

        pane.getChildren().addAll(datePicker, timeCb, dateCb, dateTimeCb, monthCb, yearCb);
        datePicker.selectedDateProperty().addListener((ob, ov, nv)->{
            System.out.println(nv);
        });

        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
