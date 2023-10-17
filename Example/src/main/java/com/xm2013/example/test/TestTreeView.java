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
import com.xm2013.jfx.control.data.XmCheckBoxTreeCell;
import com.xm2013.jfx.control.data.XmTreeView;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

        MyDMenu rootMenu = new MyDMenu("我的电脑", new XmFontIcon("\ue69a"));

        MyDMenu c = new MyDMenu("System(C:)", new XmFontIcon("\ue663"), rootMenu);

        MyDMenu ProgramFiles = new MyDMenu("Program Files", new XmFontIcon("\uec17"), c);
        MyDMenu ProgramData = new MyDMenu("Program Data", new XmFontIcon("\uec17"), c);
        MyDMenu Users = new MyDMenu("Users", new XmFontIcon("\uec17"), c);
        MyDMenu Windows = new MyDMenu("Windows", new XmFontIcon("\uec17"), c);

        c.getChildren().addAll(ProgramFiles, ProgramData, Users, Windows);
        ProgramFiles.getChildren().addAll(
                new MyDMenu("Android", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Application Verifier", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Common Files", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Google", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft.NET", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Windows Defender", new XmFontIcon("\uec17"), ProgramFiles)
        );

        ProgramData.getChildren().addAll(
                new MyDMenu("Android", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Application Verifier", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Common Files", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Google", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft.NET", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Windows Defender", new XmFontIcon("\uec17"), ProgramFiles)
        );

        Users.getChildren().addAll(
                new MyDMenu("Administrator", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Default", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Public", new XmFontIcon("\uec17"), ProgramFiles)
        );

        Windows.getChildren().addAll(
                new MyDMenu("boot", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("fonts", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("System32", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("bootstat.dat", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("notepad.exe", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("regedit.exe", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("system.ini", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("win.ini", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("write.exe", new XmFontIcon("\ueabe"), ProgramFiles)
        );


        MyDMenu d = new MyDMenu("Application(D:)", new XmFontIcon("\ue663"), rootMenu);
        MyDMenu app = new MyDMenu("App", new XmFontIcon("\uec17"), d);
        MyDMenu developApp = new MyDMenu("develop-app", new XmFontIcon("\uec17"), d);
        d.getChildren().addAll(app, developApp);

        app.getChildren().addAll(
                new MyDMenu("7-Zip", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Adobe", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("aDrive", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("aliwangwang", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Apifox", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("baidu", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("bilibili", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Camtasia 2021", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("CocosDashboard_1.2.2", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("DesktopLite", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("DocBox", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("dzclient", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Eziriz", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Ezviz Studio", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Fiddler", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("FileZilla FTP Client", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("foxmail", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Git", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("i4Tools7", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("IQIYI Video", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("leidian", new XmFontIcon("\uec17"), ProgramFiles)
        );

        developApp.getChildren().addAll(
                new MyDMenu("AndroidSDK", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Another-Redis-Desktop-Manager", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("apache-maven-3.8.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("apache-tomcat-10.1.7", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("eclipse", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("exe4j", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("gradle-7.5.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("ideaIC-2022", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Inno Setup 6", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("java", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("javafx-sdk-19", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("mysql-8.0.32-winx64", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Python", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("redis-windows-7.0.8.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("SceneBuilder", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("微信web开发者工具", new XmFontIcon("\uec17"), ProgramFiles)
        );

        MyDMenu e = new MyDMenu("Document(E:)", new XmFontIcon("\ue663"), rootMenu);
        MyDMenu Videos = new MyDMenu("视频", new XmFontIcon("\uec17"), e);
        MyDMenu d3s = new MyDMenu("3D 对象", new XmFontIcon("\uec17"), e);
        MyDMenu Pictures = new MyDMenu("图片", new XmFontIcon("\uec17"), e);
        MyDMenu Downloads = new MyDMenu("下载", new XmFontIcon("\uec17"), e);
        MyDMenu Musics = new MyDMenu("音乐", new XmFontIcon("\uec17"), e);

        e.getChildren().addAll(
                Videos,
                d3s,
                Pictures,
                Downloads,
                Musics
        );

        MyDMenu f = new MyDMenu("Game(F:)", new XmFontIcon("\ue663"), rootMenu);
        MyDMenu AlienShooter = new MyDMenu("AlienShooter", new XmFontIcon("\uec17"), f);
        MyDMenu GenshinImpact = new MyDMenu("Genshin Impact", new XmFontIcon("\uec17"), f);
        MyDMenu KingdomChronicles = new MyDMenu("KingdomChronicles", new XmFontIcon("\uec17"), f);
        MyDMenu Netease = new MyDMenu("Netease", new XmFontIcon("\uec17"), f);
        MyDMenu war3 = new MyDMenu("war3", new XmFontIcon("\uec17"), f);

        f.getChildren().addAll(
                AlienShooter,
                GenshinImpact,
                KingdomChronicles,
                Netease,
                war3
        );

        rootMenu.getChildren().addAll(c, d, e, f);

        CheckBoxTreeItem<MyDMenu> rootItem = new CheckBoxTreeItem<>(rootMenu);
        rootItem.setGraphic(rootMenu.getIcon());
        rootItem.setExpanded(true);

        //构建菜单
        buildMenu(rootItem, rootMenu);

        XmTreeView<MyDMenu> treeView = new XmTreeView<>(rootItem);
        treeView.setColorType(ColorType.danger());
        treeView.setHueType(HueType.DARK);

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
                });
            }
        });

//        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        treeView.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
//            System.out.println("called selectedItem");
//        });

        XmButton btn = new XmButton("Button");
        btn.setLayoutX(300);
        btn.setLayoutY(50);

        btn.setOnAction(e1 -> {
            System.out.println(XmTreeView.getSelectValues(treeView));
        });

        pane.getChildren().addAll(treeView, btn);

        Scene scene = new Scene(pane, 600, 400);
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
