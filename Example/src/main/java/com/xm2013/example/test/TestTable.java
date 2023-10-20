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

import com.xm2013.example.example.Product;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.tableview.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class TestTable extends Application {


    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        XmTableView table = buildTable();
        table.setHueType(HueType.LIGHT);
        table.setLayoutX(20);
        table.setLayoutY(20);
//        table.getCheckedValues().addListener((new ListChangeListener() {
//            @Override
//            public void onChanged(Change c) {
//                System.out.println(table.getCheckedValues());
//            }
//        }));

        XmTableView table1 = buildTable();
        table1.setHueType(HueType.DARK);
        table1.setHeaderBgColor(Color.web("#eeeeee66"));
        table1.setHeaderFontColor(Color.WHITE);
        table1.setLayoutX(650);
        table1.setLayoutY(20);

        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop(0, Color.WHITE));
        stops.add(new Stop(0.48, Color.WHITE));
        stops.add(new Stop(0.481, Color.BLACK));
        stops.add(new Stop(1, Color.BLACK));

        LinearGradient lg = new LinearGradient(0.0,0, 1,0, true, CycleMethod.NO_CYCLE,stops);
        pane.setBackground(new Background(new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY)));

        pane.getChildren().addAll(table, table1);

        Scene scene = new Scene(pane, 1300, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private XmTableView<Product> buildTable(){
        XmTableView table = new XmTableView();
        table.setPrefHeight(400);
        table.setPrefWidth(600);
        table.setItems(buildData());

        table.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView param) {
                return new XmTableRow();
            }
        });

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setCellFactory(new Callback<TableColumn<Product, Integer>, TableCell<Product, Integer>>() {
            @Override
            public TableCell<Product, Integer> call(TableColumn<Product, Integer> param) {
                XmCheckBoxTableCell<Product, Integer> cell = new XmCheckBoxTableCell<>();
                return cell;
            }
        });

        TableColumn<Product, String> codeCol = new TableColumn<>("编号");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setCellFactory(XmTextFieldTableCell.forTableColumn());
        table.setEditable(true);
        table.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                TablePosition pos = table.getFocusModel().getFocusedCell();
                table.edit(pos.getRow(), codeCol); // 编辑名称
            }
        });
        codeCol.setOnEditCommit(event -> {
            // 处理编辑确认事件
            Product person = event.getRowValue();
            person.setCode(event.getNewValue());
        });

        TableColumn<Product, String> nameCol = new TableColumn<>("标题");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameCol.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(TableColumn<Product, String> param) {
                return new XmTableCell<>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                            setText(null);
                        }else{
                            setGraphic(null);
                            setText(item);
                        }
                    }
                };
            }
        });

        table.getColumns().addAll(idCol, codeCol, nameCol);

        return table;
    }

    private ObservableList<Product> buildData(){
        return FXCollections.observableArrayList(
                new Product(1, "AED42A4BEDB1", "无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g", 5900l, " 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否", "/images/girls/3_3.jpg", "食用农产品")
                ,new Product(2, "FZVPRAUYBDMA", "304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子", 1080l, "材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star", "/images/girls/3_4.jpg", "餐具")
                ,new Product(3, "AED42A4BEDB1", "床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆", 1700l, "品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）", "/images/girls/3_5.jpg", "布料")
                ,new Product(4, "A19B9530EA9A", "百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈", 20800l, "食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒", "/images/girls/3_56.jpg", "礼品")
                ,new Product(5, "AED42A4BEDB1", "无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g", 5900l, " 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否", "/images/girls/3_3.jpg", "食用农产品")
                ,new Product(6, "FZVPRAUYBDMA", "304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子", 1080l, "材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star", "/images/girls/3_4.jpg", "餐具")
                ,new Product(7, "AED42A4BEDB1", "床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆", 1700l, "品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）", "/images/girls/3_5.jpg", "布料")
                ,new Product(8, "A19B9530EA9A", "百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈", 20800l, "食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒", "/images/girls/3_56.jpg", "礼品")
                ,new Product(9, "AED42A4BEDB1", "无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g", 5900l, " 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否", "/images/girls/3_3.jpg", "食用农产品")
                ,new Product(10, "FZVPRAUYBDMA", "304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子", 1080l, "材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star", "/images/girls/3_4.jpg", "餐具")
                ,new Product(11, "AED42A4BEDB1", "床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆", 1700l, "品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）", "/images/girls/3_5.jpg", "布料")
                ,new Product(12, "A19B9530EA9A", "百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈", 20800l, "食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒", "/images/girls/3_56.jpg", "礼品")
                ,new Product(13, "AED42A4BEDB1", "无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g", 5900l, " 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否", "/images/girls/3_3.jpg", "食用农产品")
                ,new Product(14, "FZVPRAUYBDMA", "304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子", 1080l, "材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star", "/images/girls/3_4.jpg", "餐具")
                ,new Product(15, "AED42A4BEDB1", "床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆", 1700l, "品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）", "/images/girls/3_5.jpg", "布料")
                ,new Product(16, "A19B9530EA9A", "百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈", 20800l, "食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒", "/images/girls/3_56.jpg", "礼品")
        );
    }

    public static void main(String[] args) {
        launch(args);
    }

}