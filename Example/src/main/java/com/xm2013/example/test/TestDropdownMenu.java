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
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.data.GridCell;
import com.xm2013.jfx.control.data.GridView;
import com.xm2013.jfx.control.data.XmCheckBoxGridCell;
import com.xm2013.jfx.control.dropdown.DropdownMenu;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.dropdown.TriggerType;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.Random;

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

    public static class TestGridView  extends Application {


        @Override
        public void start(Stage primaryStage) {
            Pane pane = new Pane();

            ObservableList<Color> list  = FXCollections.observableArrayList();

            Random r = new Random(System.currentTimeMillis());
            for(int i=0; i<400; i++){
                list.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0));
            }

            GridView<Color> myGrid = new GridView<>(list);
            myGrid.setPrefWidth(400);
            myGrid.setMultiple(true);
            myGrid.setPrefHeight(400);
            myGrid.setCellFactory(new Callback<GridView<Color>, GridCell<Color>>() {
                public GridCell<Color> call(GridView<Color> gridView) {
                    return new XmCheckBoxGridCell<>(){
                        private boolean isSetSkin = false;
                        @Override
                        protected void updateItem(Color item, boolean empty) {

                            if(!isSetSkin){
                                getCheckBox().setColorType(ColorType.other("#ffffff"));
                                getCheckBox().setSizeType(SizeType.SMALL);
                                isSetSkin = false;
                            }

                            if (empty) {
                                setGraphic(null);
                            } else {
                                Rectangle colorRect = new Rectangle();
                                colorRect.setHeight(80);
                                colorRect.setWidth(80);
                                colorRect.setFill(item);
                                setGraphic(colorRect);
                            }
                            super.updateItem(item, empty);


                        }
                    };
                }
            });

            ObservableList<File> files  = FXCollections.observableArrayList();

            File file = new File(TestSelector.class.getResource("/images/girls").getFile());
            File[] listFiles = file.listFiles();
            for (File file1 : listFiles) {
                files.add(file1);
            }

            GridView<File> fileGrid = new GridView<>(files);
            fileGrid.setPrefWidth(400);
            fileGrid.setMultiple(false);
            fileGrid.setPrefHeight(400);
            fileGrid.setPrefWidth(800);
            fileGrid.setLayoutX(400);
            fileGrid.setCellWidth(150);
            fileGrid.setCellHeight(150);

            fileGrid.setCellFactory(new Callback<GridView<File>, GridCell<File>>() {
                @Override
                public GridCell<File> call(GridView<File> param) {
                    return new XmCheckBoxGridCell<>(){
                        private boolean isSetSkin = false;
                        @Override
                        protected void updateItem(File item, boolean empty) {

                            if(!isSetSkin){
                                getCheckBox().setSizeType(SizeType.SMALL);
                                isSetSkin = true;
                            }

                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getName());
                                ImageView imageView = new ImageView(new Image(item.getAbsolutePath(), 150, 150, false, true));
                                imageView.setFitWidth(150);
                                setGraphic(imageView);
                            }

                            super.updateItem(item, empty);

                        }
                    };
                }
            });


            XmButton btn = new XmButton("获取选择值");
            btn.setLayoutY(50);
            btn.setLayoutX(450);
            btn.setOnAction(e -> {
                System.out.println(myGrid.getValues());
            });

            pane.getChildren().addAll(myGrid, fileGrid);

            Scene scene = new Scene(pane, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }

    }
}