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

import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.date.XmDateRangeSelector;
import com.xm2013.jfx.control.gridview.GridCell;
import com.xm2013.jfx.control.gridview.GridView;
import com.xm2013.jfx.control.gridview.XmCheckBoxGridCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.Random;

public class TestGridView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

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
        myGrid.setPrefHeight(400);
        myGrid.setCellFactory(new Callback<GridView<Color>, GridCell<Color>>() {
            public GridCell<Color> call(GridView<Color> gridView) {
                return new XmCheckBoxGridCell<>(){
                    @Override
                    public void init() {
                        checkBoxProperty().addListener((ob,ov, nv)->{
                            nv.setSelectedColor(Color.WHITE);
                            nv.setStyle("-fx-padding: 6 0 0 6");
                        });
                    }

                    @Override
                    public void updateItem(Color item, boolean empty) {

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
//        fileGrid.setMultiple(false);
        fileGrid.setPrefHeight(400);
        fileGrid.setPrefWidth(800);
        fileGrid.setLayoutX(400);
        fileGrid.setCellWidth(150);
        fileGrid.setCellHeight(150);

        fileGrid.setCellFactory(new Callback<GridView<File>, GridCell<File>>() {
            @Override
            public GridCell<File> call(GridView<File> param) {
                return new XmCheckBoxGridCell<>(){
                    @Override
                    public void updateItem(File item, boolean empty) {

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
        btn.setLayoutY(600);
        btn.setLayoutX(100);
        btn.setOnAction(e -> {
            System.out.println(myGrid.getCheckedValues());
        });

        pane.getChildren().addAll(myGrid, fileGrid, btn);

        Scene scene = new Scene(pane, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}