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
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.treeview.XmMenuTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeView;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 菜单玩法
 */
public class TestMenu extends Application {


    @Override
    public void start(Stage primaryStage) {
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        Pane pane = new Pane();

        MyDMenu rootMenu = FileTree.build();

        CheckBoxTreeItem<MyDMenu> rootItem = new CheckBoxTreeItem<>(rootMenu);
        rootItem.setGraphic(rootMenu.getIcon());
        rootItem.setExpanded(true);

        //构建菜单+--

        buildMenu(rootItem, rootMenu);

        XmTreeView<MyDMenu> treeView = new XmTreeView<>(rootItem);
        treeView.setColorType(ColorType.other("#aaaaaa"));
        treeView.setHueType(HueType.DARK);
//        treeView.setStyle("-fx-background-color: transparent;");

        pane.setStyle("-fx-background-color: black;");

        treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {
            @Override
            public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {
                return new XmMenuTreeCell<>(){
                    @Override
                    public void init() {
                        setVisibleArrow(false);

                        setUpdateDefaultSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<MyDMenu> cell) {
                                cell.setStyle("-fx-background-color: transparent; -fx-text-fill: #aa00aa; -fx-border-width:0 0 0 5; -fx-border-color: transparent;");
                                try {
                                    XmFontIcon icon = (XmFontIcon) getItem().getIcon();
                                    icon.setColor(Color.web("#aa00aa"));
                                }catch (Exception e){
                                }
                            }
                        });

                        setUpdateOddSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<MyDMenu> cell) {
                                cell.setStyle("-fx-background-color: transparent; -fx-text-fill: #aa00aa; -fx-border-width:0 0 0 5; -fx-border-color: transparent;");
                                try {
                                    XmFontIcon icon = (XmFontIcon) getItem().getIcon();
                                    icon.setColor(Color.web("#aa00aa"));
                                }catch (Exception e){

                                }
                            }
                        });

                        setUpdateHoverSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<MyDMenu> cell) {
                                cell.setStyle("-fx-background-color: #ff00ff22; -fx-text-fill: #aa00aa; -fx-border-width:0 0 0 5; -fx-border-color: transparent;");
                                try {
                                    XmFontIcon icon = (XmFontIcon) getItem().getIcon();
                                    icon.setColor(Color.web("#aa00aa"));
                                }catch (Exception e){

                                }
                            }
                        });

                        setUpdateSelectedSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<MyDMenu> cell) {
                                cell.setStyle("-fx-background-color: #aa00aa; -fx-text-fill: white;  -fx-border-width:0 0 0 5; -fx-border-color: #ff00ff;");
                                try {
                                    XmFontIcon icon = (XmFontIcon) getItem().getIcon();
                                    icon.setColor(Color.WHITE);
                                }catch (Exception e){

                                }
                            }
                        });

                    }

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

        pane.getChildren().addAll(treeView);

        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void buildMenu(TreeItem<MyDMenu> parent, MyDMenu root){

        ObservableList<DropdownMenuItem> children = root.getChildren();

        if(children==null || children.size()==0){
            parent.getChildren().clear();
            return;
        }

        parent.getChildren().clear();
        for(DropdownMenuItem m : children){
            MyDMenu menu = (MyDMenu) m;
            TreeItem<MyDMenu> menuItem = new TreeItem<>();
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