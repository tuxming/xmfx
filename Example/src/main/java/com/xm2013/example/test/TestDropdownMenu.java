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

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.dropdown.DropdownMenu;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.dropdown.TriggerType;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestDropdownMenu extends Application {

    int i=0;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        DropdownMenuItem group1 = new DropdownMenuItem("分组一", new XmFontIcon("\ue65c"));
        DropdownMenuItem group11 = new DropdownMenuItem("子节点1-3");

        group11.getChildren().addAll(
                new MyDMenu("子节点1-3-1"),
                new DropdownMenuItem("子节点1-3-2"),
                new DropdownMenuItem("子节点1-3-3")
        );

        group1.getChildren().addAll(
                new DropdownMenuItem("子节点1-1"),
                new DropdownMenuItem("子节点1-2"),
                group11,
                new DropdownMenuItem("子节点1-4")
        );

        DropdownMenuItem group2 = new DropdownMenuItem("分组二", new XmFontIcon("\ue65c"));
        group2.getChildren().addAll(
                new DropdownMenuItem("子节点2-1"),
                new DropdownMenuItem("子节点2-2"),
                new DropdownMenuItem("子节点2-3"),
                new DropdownMenuItem("子节点2-4")
        );

        DropdownMenu dropdownMenu = new DropdownMenu("11111aa");
        dropdownMenu.addItems(group1, group2);
        dropdownMenu.setUseGroup(true);
        dropdownMenu.setColorType(ColorType.primary());
//        dropdownMenu.setRoundType(RoundType.CIRCLE);
        dropdownMenu.setMargin(new Insets(10));
        Label icon = new Label("…");
        icon.setTextFill(Color.RED);
        icon.setFont(Font.font(16d));
        dropdownMenu.setIcon(icon);
        dropdownMenu.setTriggerType(TriggerType.CLICK);
        dropdownMenu.setClickAnimateType(ClickAnimateType.SHADOW);
        dropdownMenu.setRoundType(RoundType.CIRCLE);
        dropdownMenu.setSelectedCallback(new CallBack<DropdownMenuItem>() {
            @Override
            public void call(DropdownMenuItem o) {
                dropdownMenu.setText(o.getLabelName());
            }
        });

        dropdownMenu.selectedItemProperty().addListener((ob, ov, nv)->{
            DropdownMenuItem item = (DropdownMenuItem) nv;
            if(nv!=null){
                System.out.println(item.getLabelName());
            }
        });
        dropdownMenu.setClickCallback(new CallBack<MouseEvent>() {
            @Override
            public void call(MouseEvent e) {
                System.out.println("click text");
            }
        });

        XmButton btn = new XmButton("text");
        btn.setOnAction( e-> {
            dropdownMenu.setShowing(!dropdownMenu.isShowing());
        });

        HBox box = new HBox(dropdownMenu, btn);
//        box.setLayoutX(20);
//        box.setLayoutY(20);
//        box.setStyle("-fx-background-color: red;");
        box.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(box);

        Scene scene = new Scene(pane, 600, 200);
        primaryStage.setScene(scene);
//        primaryStage.setX(1000);
//        primaryStage.setY(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}