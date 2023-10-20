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
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.listview.XmCheckBoxListCell;
import com.xm2013.jfx.control.listview.XmListCell;
import com.xm2013.jfx.control.listview.XmListView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TestListView extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        ObservableList<MyDMenu> menus = FXCollections.observableArrayList();
        menus.addAll(new MyDMenu("我的电脑"),
                new MyDMenu("网络设置"),
                new MyDMenu("我的文档"),
                new MyDMenu("系统设置"),
                new MyDMenu("回收站"),
                new MyDMenu("Microsoft Edge"),
                new MyDMenu("Photoshop"),
                new MyDMenu("我的电脑1"),
                new MyDMenu("网络设置1"),
                new MyDMenu("我的文档1"),
                new MyDMenu("系统设置1"),
                new MyDMenu("回收站1"),
                new MyDMenu("Microsoft Edge1"),
                new MyDMenu("Photoshop1"),
                new MyDMenu("我的电脑2"),
                new MyDMenu("网络设置2"),
                new MyDMenu("我的文档2"),
                new MyDMenu("系统设置2"),
                new MyDMenu("回收站2"),
                new MyDMenu("Microsoft Edge2"),
                new MyDMenu("Photoshop2"),
                new MyDMenu("我的电脑3"),
                new MyDMenu("网络设置3"),
                new MyDMenu("我的文档3"),
                new MyDMenu("系统设置3"),
                new MyDMenu("回收站3"),
                new MyDMenu("Microsoft Edge3"),
                new MyDMenu("Photoshop3")
            );

        XmListView<MyDMenu> listView = new XmListView<MyDMenu>(menus);
        listView.setCellFactory(new Callback<ListView<MyDMenu>, ListCell<MyDMenu>>() {
            @Override
            public ListCell<MyDMenu> call(ListView<MyDMenu> param) {
                return new XmCheckBoxListCell(){
                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null){
                            setText(null);
                        }else{
                            setText(((MyDMenu)item).getLabelName());
                        }
                    }
                };
            }
        });

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setSizeType(SizeType.MEDIUM);
        listView.setHueType(HueType.DARK);
//        listView.setColorType(ColorType.other("#666666"));
        listView.setColorType(ColorType.danger());
//        listView.setColorType(ColorType.other("#dd00dd"));
//        listView.setPrefWidth(500);

        XmButton btn = new XmButton("Button");
        btn.setLayoutX(400);
        btn.setLayoutY(20);

        XmListView<MyDMenu> listView1 = new XmListView<>(menus);
        listView1.setCellFactory(new Callback<ListView<MyDMenu>, ListCell<MyDMenu>>() {
            @Override
            public ListCell<MyDMenu> call(ListView<MyDMenu> param) {
                return new XmListCell<>(){
                    @Override
                    public void updateItem(MyDMenu item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null){
                            setText(null);
                        }else{
                            setText(item.getLabelName());
                        }
                    }
                };
            }
        });
        listView1.setSizeType(SizeType.MEDIUM);
        listView1.setHueType(HueType.LIGHT);
        listView1.setLayoutX(300);

        pane.setStyle("-fx-background-color: black;");
        pane.getChildren().addAll(listView, listView1);

        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
