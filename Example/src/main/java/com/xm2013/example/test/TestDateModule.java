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

import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.date.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class TestDateModule extends Application {


    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        XmNodeButton defultBtn = new XmNodeButton("10");
        defultBtn.setTextAlign(Pos.CENTER);
        defultBtn.setPrefWidth(30);
        defultBtn.setPrefHeight(30);
        defultBtn.setClickAnimateType(ClickAnimateType.SHADOW);

        defultBtn.setOnAction(e -> {
            System.out.println(e);
        });

        XmNodeButton activeBtn = new XmNodeButton("11");
        activeBtn.setTextAlign(Pos.CENTER);
        activeBtn.setActive(true);
        activeBtn.setPrefWidth(30);
        activeBtn.setPrefHeight(30);

        XmNodeButton filledBtn = new XmNodeButton("12");
        filledBtn.setTextAlign(Pos.CENTER);
        filledBtn.setFilled(true);
        filledBtn.setPrefWidth(30);
        filledBtn.setPrefHeight(30);
        filledBtn.setClickAnimateType(ClickAnimateType.SHADOW);

        XmNodeButton outSideBtn = new XmNodeButton("12");
        outSideBtn.setTextAlign(Pos.CENTER);
        outSideBtn.setOutside(true);
        outSideBtn.setPrefWidth(30);
        outSideBtn.setPrefHeight(30);

        HBox hbox = new HBox(defultBtn, activeBtn, filledBtn, outSideBtn);
        hbox.setSpacing(10);
        hbox.setLayoutY(30);
        hbox.setLayoutX(30);

        LocalDateTime now = LocalDateTime.now();

        DateBodyPane dateBodyPane = new DateBodyPane(LocalDateTime.now(), now);
        dateBodyPane.setTitleAlign(HPos.CENTER);
        dateBodyPane.setLayoutY(90);
        dateBodyPane.setLayoutX(30);
//        dateBodyPane.setCellUpdateCallback(cell -> {
//            cell.setTextAlign(Pos.TOP_RIGHT);
//            cell.getTextNode().setFont(Font.font(18));
//        });
        dateBodyPane.setOnMonthTextAction(e->{
            System.out.println("clicked month text");
        });
        dateBodyPane.setOnYearTextAction(e -> {
            System.out.println("clicked year text");
        });

        MonthBodyPane monthBodyPane = new MonthBodyPane();
        monthBodyPane.setLayoutY(90);
        monthBodyPane.setLayoutX(350);
        monthBodyPane.setOnTextAction(e -> {
            System.out.println("clickText："+e);
        });
        monthBodyPane.selectedMonthProperty().addListener((ob, ov, nv)->{
            System.out.println(nv);
        });

        YearBodyPane yearBodyPane = new YearBodyPane();
        yearBodyPane.setLayoutY(90);
        yearBodyPane.setLayoutX(650);
        yearBodyPane.selectedYearProperty().addListener((ob, ov, nv)->{
            System.out.println(nv);
        });

        TimePane timePane = new TimePane();
        timePane.setLayoutY(90);
        timePane.setLayoutX(1000);
        timePane.setColorType(ColorType.other("linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #7ee8ecff 0.0%, #4a4ae3ff 100.0%)"));
        timePane.timeProperty().addListener((ob, ov, nv)->{
            System.out.println(nv);
        });

        pane.getChildren().addAll(hbox, dateBodyPane, monthBodyPane, yearBodyPane, timePane);
//        pane.getChildren().addAll(dateBodyPane);


        Scene scene = new Scene(pane, 1300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

//        dateBodyPane.prefWidthProperty().bind(scene.widthProperty());
//        dateBodyPane.prefHeightProperty().bind(scene.heightProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }

}