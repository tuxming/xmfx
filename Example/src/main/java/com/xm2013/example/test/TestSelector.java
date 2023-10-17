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
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.data.XmCheckBoxGridCell;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.selector.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestSelector extends Application {

    int i = 0;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

//        ComboBox cb = new ComboBox();
//        cb.getItems().addAll("aaa", "bbb", "ccc", "ddd");
//
//        ListView lv = new ListView();
//        lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        pane.getChildren().add(cb);

        XmButton btn = new XmButton("button");
        btn.setSizeType(SizeType.MEDIUM);
        btn.setLayoutX(300);
        btn.setLayoutY(10);

        XmSelector<SelectorItem> listSelector = buildListXmSelector();
        listSelector.setLayoutY(10);
        listSelector.setLayoutX(10);
        listSelector.setMultiple(true);
        listSelector.setSizeType(SizeType.LARGE);
        listSelector.setRoundType(RoundType.NONE);

        XmSelector<MySelectorItem> treeSelector = buildTreeXmSelector();
        treeSelector.setLayoutY(100);
        treeSelector.setPrefWidth(400);
        treeSelector.setLayoutX(10);

        XmSelector<File> gridSelector = buildImageXmSelector();
        gridSelector.setLayoutY(200);
        gridSelector.setLayoutX(10.4);
        gridSelector.setMultiple(true);
        gridSelector.setCloseable(false);

//        pane.getChildren().addAll(btn, listSelector);
        pane.getChildren().addAll(btn, listSelector, treeSelector, gridSelector);

        Scene scene = new Scene(pane, 800, 400);
        primaryStage.setScene(scene);
//        primaryStage.setX(1000);
//        primaryStage.setY(600);
        primaryStage.show();

//        System.out.println(btn.prefHeight(-1));

    }

    public static XmSelector<File> buildImageXmSelector(){

        SelectorConvert<File> convert = new SelectorConvert<File>() {
            @Override
            public String toString(File object) {
                return object!=null?object.getName():null;
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        };

        XmSelector<File> selector = new XmSelector<File>();
        selector.setPromptText("请选择");
        selector.setSelectorType(SelectorType.GRID);
        selector.setConverter(convert);
        selector.setRoundType(RoundType.CIRCLE);
        selector.setEditable(true);

        File file = new File(TestSelector.class.getResource("/images/girls").getFile());
        File[] files = file.listFiles();
        for (File file1 : files) {
            selector.getItems().add(file1);
        }

        selector.setCellFactory(new SelectorCellFactory<File>() {
            private boolean isSetSkin = false;
            @Override
            public void updateItem(IndexedCell<File> cell, File item, boolean empty) {

                if(!isSetSkin) {
                    XmCheckBoxGridCell<File> checkCell= (XmCheckBoxGridCell<File>) cell;
                    checkCell.getCheckBox().setSizeType(SizeType.SMALL);
                }

                if (empty || item == null) {
                    cell.setText(null);
                } else {
                    cell.setText(item.getName());
                    ImageView imageView = new ImageView(new Image(item.getAbsolutePath(),
                            80, 80, true,
                            false));
                    imageView.setFitWidth(80);
                    cell.setGraphic(imageView);
                }
            }
        });

        return selector;
    }

    public static XmSelector<SelectorItem> buildListXmSelector(){
        SelectorConvert<SelectorItem> convert = new SelectorConvert<SelectorItem>() {
            @Override
            public String toString(SelectorItem object) {
                return object!=null?object.getLabel():null;
            }

            @Override
            public SelectorItem fromString(String string) {
                return new MySelectorItem(string);
            }
        };


        MySelectorItem item1 = new MySelectorItem("节点一");
        MySelectorItem item2 = new MySelectorItem("节点二");
        MySelectorItem item3 = new MySelectorItem("节点三");
        MySelectorItem item4 = new MySelectorItem("节点四");
        MySelectorItem item5 = new MySelectorItem("节点五");
        MySelectorItem item6 = new MySelectorItem("节点六");

        XmSelector<SelectorItem> selector = new XmSelector<SelectorItem>();
        selector.setPromptText("请选择");
        selector.setConverter(convert);
//        selector.setValues();
        selector.setMultiple(true);
        selector.setEditable(true);
        selector.setRoundType(RoundType.SEMICIRCLE);
        selector.setClickAnimateType(ClickAnimateType.RIPPER);
        selector.setSizeType(SizeType.MEDIUM);
        selector.setHueType(HueType.LIGHT);
        selector.setCloseable(true);
        selector.setMargin(new Insets(10));
        selector.setItems(item1, item2, item3, item4, item5, item6);
        selector.setValues(item2, item4);
//        selector.setPadding(new Insets(10));

        selector.setCellFactory(new SelectorCellFactory<SelectorItem>() {

            @Override
            public void updateItem(IndexedCell<SelectorItem> cell, SelectorItem item, boolean empty) {
                if (empty || item == null) {
                    cell.setText(null);
                } else {
                    String text = selector.getConverter().toString(item);
                    cell.setText(text);
                }
            }
        });

        return selector;
    }

    private static XmSelector<MySelectorItem> buildTreeXmSelector() {

        SelectorConvert<MySelectorItem> convert = new SelectorConvert<MySelectorItem>() {
            @Override
            public List<MySelectorItem> getChildren(MySelectorItem item) {
                List<MySelectorItem> nc = new ArrayList<>();
                ObservableList<SelectorItem> children = item.getChildren();
                if(children!=null && children.size()>0){
                    for (SelectorItem si: children) {
                        nc.add((MySelectorItem) si);
                    }
                }
                return nc;
            }

            @Override
            public Node getIcon(MySelectorItem t) {
                return t.getIcon();
            }

            @Override
            public String toString(MySelectorItem object) {
                return object!=null?object.getLabel():null;
            }

            @Override
            public MySelectorItem fromString(String string) {
                return new MySelectorItem(string);
            }
        };

        MySelectorItem rootMenu = buildTreeData();

        XmSelector<MySelectorItem> selector = new XmSelector<MySelectorItem>();
        selector.setPromptText("请选择");
        selector.setConverter(convert);
//        selector.setValues();
        selector.setMultiple(true);
        selector.setPrefWidth(500);
        selector.setEditable(true);
        selector.setRoundType(RoundType.SEMICIRCLE);
        selector.setClickAnimateType(ClickAnimateType.RIPPER);
        selector.setSizeType(SizeType.MEDIUM);
        selector.setSelectorType(SelectorType.TREE);
        selector.setHueType(HueType.LIGHT);
        selector.setCloseable(true);
        selector.setMargin(new Insets(10));
        selector.setItems(rootMenu);
//        selector.setValues(item2, item4);
//        selector.setPadding(new Insets(10));

        selector.setCellFactory(new SelectorCellFactory<MySelectorItem>() {
            @Override
            public void updateItem(IndexedCell<MySelectorItem> cell, MySelectorItem item, boolean empty) {
                if (empty || item == null) {
                    cell.setText(null);
                } else {
                    String text = convert.toString(item);
                    cell.setText(text);
                }
            }
        });

        return selector;

    }

    public static MySelectorItem buildTreeData(){
        MySelectorItem rootMenu = new MySelectorItem("我的电脑", new XmFontIcon("\ue69a"));

        MySelectorItem c = new MySelectorItem("System(C:)", new XmFontIcon("\ue663"), rootMenu);

        MySelectorItem ProgramFiles = new MySelectorItem("Program Files", new XmFontIcon("\uec17"), c);
        MySelectorItem ProgramData = new MySelectorItem("Program Data", new XmFontIcon("\uec17"), c);
        MySelectorItem Users = new MySelectorItem("Users", new XmFontIcon("\uec17"), c);
        MySelectorItem Windows = new MySelectorItem("Windows", new XmFontIcon("\uec17"), c);

        c.getChildren().addAll(ProgramFiles, ProgramData, Users, Windows);
        ProgramFiles.getChildren().addAll(
                new MySelectorItem("Android", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Application Verifier", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Common Files", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Google", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Microsoft", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Microsoft.NET", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Windows Defender", new XmFontIcon("\uec17"), ProgramFiles)
        );

        ProgramData.getChildren().addAll(
                new MySelectorItem("Android", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Application Verifier", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Common Files", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Google", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Microsoft", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Microsoft.NET", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Windows Defender", new XmFontIcon("\uec17"), ProgramFiles)
        );

        Users.getChildren().addAll(
                new MySelectorItem("Administrator", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Default", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Public", new XmFontIcon("\uec17"), ProgramFiles)
        );

        Windows.getChildren().addAll(
                new MySelectorItem("boot", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("fonts", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("System32", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("bootstat.dat", new XmFontIcon("\ueabe"), ProgramFiles),
                new MySelectorItem("notepad.exe", new XmFontIcon("\ueabe"), ProgramFiles),
                new MySelectorItem("regedit.exe", new XmFontIcon("\ueabe"), ProgramFiles),
                new MySelectorItem("system.ini", new XmFontIcon("\ueabe"), ProgramFiles),
                new MySelectorItem("win.ini", new XmFontIcon("\ueabe"), ProgramFiles),
                new MySelectorItem("write.exe", new XmFontIcon("\ueabe"), ProgramFiles)
        );


        MySelectorItem d = new MySelectorItem("Application(D:)", new XmFontIcon("\ue663"), rootMenu);
        MySelectorItem app = new MySelectorItem("App", new XmFontIcon("\uec17"), d);
        MySelectorItem developApp = new MySelectorItem("develop-app", new XmFontIcon("\uec17"), d);
        d.getChildren().addAll(app, developApp);

        app.getChildren().addAll(
                new MySelectorItem("7-Zip", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Adobe", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("aDrive", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("aliwangwang", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Apifox", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("baidu", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("bilibili", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Camtasia 2021", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("CocosDashboard_1.2.2", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("DesktopLite", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("DocBox", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("dzclient", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Eziriz", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Ezviz Studio", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Fiddler", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("FileZilla FTP Client", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("foxmail", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Git", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("i4Tools7", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("IQIYI Video", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("leidian", new XmFontIcon("\uec17"), ProgramFiles)
        );

        developApp.getChildren().addAll(
                new MySelectorItem("AndroidSDK", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Another-Redis-Desktop-Manager", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("apache-maven-3.8.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("apache-tomcat-10.1.7", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("eclipse", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("exe4j", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("gradle-7.5.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("ideaIC-2022", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Inno Setup 6", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("java", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("javafx-sdk-19", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("mysql-8.0.32-winx64", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("Python", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("redis-windows-7.0.8.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("SceneBuilder", new XmFontIcon("\uec17"), ProgramFiles),
                new MySelectorItem("微信web开发者工具", new XmFontIcon("\uec17"), ProgramFiles)
        );

        MySelectorItem e = new MySelectorItem("Document(E:)", new XmFontIcon("\ue663"), rootMenu);
        MySelectorItem Videos = new MySelectorItem("视频", new XmFontIcon("\uec17"), e);
        MySelectorItem d3s = new MySelectorItem("3D 对象", new XmFontIcon("\uec17"), e);
        MySelectorItem Pictures = new MySelectorItem("图片", new XmFontIcon("\uec17"), e);
        MySelectorItem Downloads = new MySelectorItem("下载", new XmFontIcon("\uec17"), e);
        MySelectorItem Musics = new MySelectorItem("音乐", new XmFontIcon("\uec17"), e);

        e.getChildren().addAll(
                Videos,
                d3s,
                Pictures,
                Downloads,
                Musics
        );

        MySelectorItem f = new MySelectorItem("Game(F:)", new XmFontIcon("\ue663"), rootMenu);
        MySelectorItem AlienShooter = new MySelectorItem("AlienShooter", new XmFontIcon("\uec17"), f);
        MySelectorItem GenshinImpact = new MySelectorItem("Genshin Impact", new XmFontIcon("\uec17"), f);
        MySelectorItem KingdomChronicles = new MySelectorItem("KingdomChronicles", new XmFontIcon("\uec17"), f);
        MySelectorItem Netease = new MySelectorItem("Netease", new XmFontIcon("\uec17"), f);
        MySelectorItem war3 = new MySelectorItem("war3", new XmFontIcon("\uec17"), f);

        f.getChildren().addAll(
                AlienShooter,
                GenshinImpact,
                KingdomChronicles,
                Netease,
                war3
        );

        rootMenu.getChildren().addAll(c, d, e, f);
        return rootMenu;
    }

    public static void main(String[] args) {
        launch(args);
    }
}