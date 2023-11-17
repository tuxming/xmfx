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
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.treeview.XmCheckBoxTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeView;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class TestTreeView extends Application {


    @Override
    public void start(Stage primaryStage) {
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        Pane pane = new Pane();
        MyDMenu rootMenu = FileTree.build();

        CheckBoxTreeItem<MyDMenu> rootItem = new CheckBoxTreeItem<>(rootMenu);
        rootItem.setGraphic(rootMenu.getIcon());
        rootItem.setExpanded(true);

        //构建菜单
        buildMenu(rootItem, rootMenu);

        XmTreeView<MyDMenu> treeView = new XmTreeView<>(rootItem);
        treeView.setColorType(ColorType.danger());
        treeView.setHueType(HueType.LIGHT);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        pane.setStyle("-fx-background-color: black;");

        treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {
            @Override
            public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {
                return new XmCheckBoxTreeCell<>(new Callback<TreeItem<MyDMenu>, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TreeItem<MyDMenu> param) {
                        if (param instanceof CheckBoxTreeItem<?>) {
                            return ((CheckBoxTreeItem<?>) param).selectedProperty();
                        }
                        return null;
                    }
                }, new StringConverter<TreeItem<MyDMenu>>() {
                    @Override
                    public String toString(TreeItem<MyDMenu> object) {
                        return object.getValue().getLabelName();
                    }

                    @Override
                    public TreeItem<MyDMenu> fromString(String string) {
                        return new TreeItem<>(new MyDMenu(string));
                    }
                }){
                    @Override
                    public void updateItem(MyDMenu item, boolean empty) {
                        super.updateItem(item, empty);
                        getCheckBox().setMargin(new Insets(10,0,0,0));
                    }
                };
            }
        });

        XmButton btn = new XmButton("Button");
        btn.setLayoutX(300);
        btn.setLayoutY(50);

        btn.setOnAction(e1 -> {
            System.out.println(XmTreeView.getSelectValues(treeView));
        });

        MyDMenu rootMenu1 = FileTree.build();
        CheckBoxTreeItem<MyDMenu> rootItem1 = new CheckBoxTreeItem<>(rootMenu1);
        rootItem1.setGraphic(rootMenu1.getIcon());
        rootItem1.setExpanded(true);

        //构建菜单
        buildMenu(rootItem1, rootMenu1);

        XmTreeView<MyDMenu> treeView1 = new XmTreeView<>(rootItem1);
        treeView1.setColorType(ColorType.danger());
        treeView1.setHueType(HueType.DARK);
        treeView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView1.setLayoutX(500);
        treeView1.setVisibleArrow(false);

        treeView1.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {
            @Override
            public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {
                return new XmTreeCell<>(){
                    @Override
                    public void updateItem(MyDMenu item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                            setGraphic(null);
                        }else{
                            setText(item.getLabelName());
                        }
                    }
                };
            }
        });

        pane.getChildren().addAll(treeView, treeView1, btn);

        Scene scene = new Scene(pane, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setX(1000);
        primaryStage.setY(600);
        primaryStage.show();
    }

    public void buildMenu(CheckBoxTreeItem<MyDMenu> parent, MyDMenu root){

        ObservableList<DropdownMenuItem> children = root.getChildren();

        if(children==null || children.size()==0){
            parent.getChildren().clear();
            return;
        }

        parent.getChildren().clear();
        for(DropdownMenuItem m : children){
            MyDMenu menu = (MyDMenu) m;
            CheckBoxTreeItem<MyDMenu> menuItem = new CheckBoxTreeItem<>();
            menuItem.setValue(menu);
            menu.getIcon().setMouseTransparent(true);
            menuItem.setGraphic(menu.getIcon());
            parent.getChildren().add(menuItem);
            buildMenu(menuItem, menu);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
