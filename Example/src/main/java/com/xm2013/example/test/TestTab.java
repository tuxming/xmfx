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

import com.xm2013.example.example.Menu;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.scroll.XmScrollBar;
import com.xm2013.jfx.control.tab.XmTab;
import com.xm2013.jfx.control.tab.XmTabPane;
import com.xm2013.jfx.control.tableview.XmTableView;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TestTab extends Application {


    @Override
    public void start(Stage primaryStage) {
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        Pane pane = new Pane();
//        XmFontIcon icon = new XmFontIcon("\ue8fc");
//        XmTab tab1 = new XmTab("系统设置", icon, null);
//        tab1.setHueType(HueType.DARK);
//        tab1.setFontColor(Color.web("#444444"));
//        tab1.setRoundType(RoundType.SMALL);
//        tab1.setCloseable(true);
//
//        tab1.setLayoutX(20);
//        tab1.setLayoutY(20);
//
//        XmFontIcon icon1 = new XmFontIcon("\ue8fc");
//        XmTab tab = new XmTab("学习园地", icon1, null);
//        tab.setHueType(HueType.LIGHT);
////        tab.setFontColor(Color.web("#444444"));
//        tab.setRoundType(RoundType.SMALL);
//        tab.setCloseable(true);
//        tab.setSelected(true);
//
//        tab.setLayoutX(200);
//        tab.setLayoutY(20);

        List<Menu> menus = new ArrayList<>();

        menus.add(new Menu("\ue65c", "标签（XmLabel）", "com.xm2013.example.example.page.LabelPage", 12));
        menus.add(new Menu("\ue6fc", "按钮（XmButton）", "com.xm2013.example.example.page.ButtonPage", 13));
        menus.add(new Menu("\ue77c", "文本/密码框（XmTextField）", "com.xm2013.example.example.page.TextFieldPage", 14));
        menus.add(new Menu("\ue6da", "单选/复选（CheckBox）", "com.xm2013.example.example.page.CheckBoxPage", 15));
        menus.add(new Menu("\ue751", "Tag", "com.xm2013.example.example.page.TagPage", 16));
        menus.add(new Menu("\ue61c", "下拉菜单", "com.xm2013.example.example.page.DropdownMenuPage", 17));
        menus.add(new Menu("\ueb94", "下拉选择框", "com.xm2013.example.example.page.SelectorPage", 18));
        menus.add(new Menu("\ue65e", "日期选择框", "com.xm2013.example.example.page.DateSelectorPage", 19));
        menus.add(new Menu("\ue65e", "日期范围选择框", "com.xm2013.example.example.page.DateRangeSelectorPage", 20));
        menus.add(new Menu("\ue6fd", "分页", "com.xm2013.example.example.page.PagerPage", 21));
        menus.add(new Menu("\ue8fc", "列表视图", "com.xm2013.example.example.page.ListViewPage", 22));
        menus.add(new Menu("\ue892", "树形视图", "com.xm2013.example.example.page.TreeViewPage", 23));
        menus.add(new Menu("\ue755", "网格视图", "com.xm2013.example.example.page.GridViewPage", 24));
        menus.add(new Menu("\ue78c", "表格视图", "com.xm2013.example.example.page.TableViewPage", 25));
        menus.add(new Menu("\ue8d0", "Switch开关", "com.xm2013.example.example.page.SwitchPage", 26));

        List<XmTab> tabs = new ArrayList<>();
        for (Menu menu : menus) {
            XmTab t = new XmTab(menu.getLabel(), new XmFontIcon(menu.getIconStr()), menu.getPage());
            t.setKey(menu.getId()+"");
            t.setHueType(HueType.DARK);
            t.setFontColor(Color.web("#444444"));
            tabs.add(t);
        }

        XmTabPane tabPane = new XmTabPane(tabs);
        tabPane.select(tabs.get(2));
        tabPane.setTabMaxWidth(600);
        tabPane.setPrefWidth(600);
        tabPane.setPrefHeight(400);
//        tabPane.setLayoutX(10);
//        tabPane.setLayoutY(80);

        pane.getChildren().addAll(tabPane);

        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}