package com.xm2013.example.example.page;

import com.xm2013.example.example.Product;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.tableview.*;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class TableViewPage extends BasePage{


    private Pane tablePane;
    private XmTableView<Product> tableView;
    private String javaCodePrev;
    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    public TableViewPage(){
        this.setTitle("表格视图（XmTableView）", new XmFontIcon("\ue78c"));
        this.setComponentTitle("属性");

        buildTableView();

        setColor();
        setHue();
        setSize();

        getJavaText().setText(javaCodePrev);
        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text = javaCodePrev;

                for (String key : javaCodes.keySet()) {
                    text += javaCodes.get(key) + "\n";
                }

                getJavaText().setText(text);
            }
        });

    }

    private void buildTableView() {

        tableView = new XmTableView<>();
        tableView.getStyleClass().add(".my-table");
        tableView.setPrefWidth(400);
        tableView.setPrefHeight(400);
        tablePane = new Pane(tableView);
        tableView.setItems(FXCollections.observableArrayList(
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
        ));

        tableView.setRowFactory(param -> new XmTableRow<>());

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setCellFactory(param -> new XmCheckBoxTableCell<>());

        TableColumn<Product, String> codeCol = new TableColumn<>("编号");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setCellFactory(XmTextFieldTableCell.forTableColumn());
        tableView.setEditable(true);
        tableView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                TablePosition pos = tableView.getFocusModel().getFocusedCell();
                tableView.edit(pos.getRow(), codeCol); // 编辑名称
            }
        });
        codeCol.setOnEditCommit(event -> {
            // 处理编辑确认事件
            Product person = event.getRowValue();
            person.setCode(event.getNewValue());
        });

        TableColumn<Product, String> nameCol = new TableColumn<>("标题");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameCol.setCellFactory(param -> new XmTableCell<>(){
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
        });

        tableView.getColumns().addAll(idCol, codeCol, nameCol);

        javaCodePrev  = "XmTableView<Product> tableView = new XmTableView<>();\n" +
                "tableView.getStyleClass().add(\".my-table\");\n" +
                "tableView.setPrefWidth(400);\n" +
                "tableView.setPrefHeight(400);\n" +
                "tablePane = new Pane(tableView);\n" +
                "tableView.setItems(FXCollections.observableArrayList(\n" +
                "        new Product(1, \"AED42A4BEDB1\", \"无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g\", 5900l, \" 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否\", \"/images/girls/3_3.jpg\", \"食用农产品\")\n" +
                "        ,new Product(2, \"FZVPRAUYBDMA\", \"304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子\", 1080l, \"材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star\", \"/images/girls/3_4.jpg\", \"餐具\")\n" +
                "        ,new Product(3, \"AED42A4BEDB1\", \"床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆\", 1700l, \"品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）\", \"/images/girls/3_5.jpg\", \"布料\")\n" +
                "        ,new Product(4, \"A19B9530EA9A\", \"百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈\", 20800l, \"食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒\", \"/images/girls/3_56.jpg\", \"礼品\")\n" +
                "        ,new Product(5, \"AED42A4BEDB1\", \"无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g\", 5900l, \" 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否\", \"/images/girls/3_3.jpg\", \"食用农产品\")\n" +
                "        ,new Product(6, \"FZVPRAUYBDMA\", \"304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子\", 1080l, \"材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star\", \"/images/girls/3_4.jpg\", \"餐具\")\n" +
                "        ,new Product(7, \"AED42A4BEDB1\", \"床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆\", 1700l, \"品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）\", \"/images/girls/3_5.jpg\", \"布料\")\n" +
                "        ,new Product(8, \"A19B9530EA9A\", \"百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈\", 20800l, \"食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒\", \"/images/girls/3_56.jpg\", \"礼品\")\n" +
                "        ,new Product(9, \"AED42A4BEDB1\", \"无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g\", 5900l, \" 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否\", \"/images/girls/3_3.jpg\", \"食用农产品\")\n" +
                "        ,new Product(10, \"FZVPRAUYBDMA\", \"304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子\", 1080l, \"材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star\", \"/images/girls/3_4.jpg\", \"餐具\")\n" +
                "        ,new Product(11, \"AED42A4BEDB1\", \"床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆\", 1700l, \"品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）\", \"/images/girls/3_5.jpg\", \"布料\")\n" +
                "        ,new Product(12, \"A19B9530EA9A\", \"百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈\", 20800l, \"食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒\", \"/images/girls/3_56.jpg\", \"礼品\")\n" +
                "        ,new Product(13, \"AED42A4BEDB1\", \"无硫姬松茸干货旗舰店云南特产鸡松茸菌菇巴西蘑菇食用菌98元500g\", 5900l, \" 食用农产品, 250g/袋, 包装种类: 袋装, 储存条件: 常温, 是否为有机食品: 否\", \"/images/girls/3_3.jpg\", \"食用农产品\")\n" +
                "        ,new Product(14, \"FZVPRAUYBDMA\", \"304不锈钢小勺子汤匙铁勺子匙羹调羹宫廷勺学生加深吃饭勺子\", 1080l, \"材质：304不锈钢, 适用场景：日常送礼, 刀叉勺产品：主餐更, 品牌：Buyer Star\", \"/images/girls/3_4.jpg\", \"餐具\")\n" +
                "        ,new Product(15, \"AED42A4BEDB1\", \"床头柜盖布新款红色喜庆巾冰箱空调防尘罩欧式桌布艺婚庆刺绣喜庆\", 1700l, \"品牌：花谷朵, 具体规格：58*58厘米（一片）无中心花 68*68厘米（一片） 85*85厘米（一片）\", \"/images/girls/3_5.jpg\", \"布料\")\n" +
                "        ,new Product(16, \"A19B9530EA9A\", \"百草味纯坚果礼盒2096g每日干果大礼包零食健康休闲食品送长辈\", 20800l, \"食品口味：【坚果有礼】︱14袋/2096g（100%坚果）·百草味坚果礼盒\", \"/images/girls/3_56.jpg\", \"礼品\")\n" +
                "));\n" +
                "\n" +
                "tableView.setRowFactory(param -> new XmTableRow<>());\n" +
                "\n" +
                "TableColumn<Product, Integer> idCol = new TableColumn<>(\"ID\");\n" +
                "idCol.setCellValueFactory(new PropertyValueFactory<>(\"id\"));\n" +
                "idCol.setCellFactory(param -> new XmCheckBoxTableCell<>());\n" +
                "\n" +
                "TableColumn<Product, String> codeCol = new TableColumn<>(\"编号\");\n" +
                "codeCol.setCellValueFactory(new PropertyValueFactory<>(\"code\"));\n" +
                "codeCol.setCellFactory(XmTextFieldTableCell.forTableColumn());\n" +
                "tableView.setEditable(true);\n" +
                "tableView.setOnMouseClicked(event -> {\n" +
                "    if(event.getClickCount() == 2) {\n" +
                "        TablePosition pos = tableView.getFocusModel().getFocusedCell();\n" +
                "        tableView.edit(pos.getRow(), codeCol); // 编辑名称\n" +
                "    }\n" +
                "});\n" +
                "codeCol.setOnEditCommit(event -> {\n" +
                "    // 处理编辑确认事件\n" +
                "    Product person = event.getRowValue();\n" +
                "    person.setCode(event.getNewValue());\n" +
                "});\n" +
                "\n" +
                "TableColumn<Product, String> nameCol = new TableColumn<>(\"标题\");\n" +
                "\n" +
                "nameCol.setCellValueFactory(new PropertyValueFactory<>(\"title\"));\n" +
                "nameCol.setCellFactory(param -> new XmTableCell<>(){\n" +
                "    @Override\n" +
                "    protected void updateItem(String item, boolean empty) {\n" +
                "        super.updateItem(item, empty);\n" +
                "        if(empty){\n" +
                "            setGraphic(null);\n" +
                "            setText(null);\n" +
                "        }else{\n" +
                "            setGraphic(null);\n" +
                "            setText(item);\n" +
                "        }\n" +
                "    }\n" +
                "});\n" +
                "\n" +
                "tableView.getColumns().addAll(idCol, codeCol, nameCol);\n";

        setShowComponent(tablePane);

    }

    /**
     * 设置颜色类型
     */
    private void setColor() {

        XmSelector<ColorType> colorSelector = new XmSelector<ColorType>();
        colorSelector.setSizeType(SizeType.SMALL);
        colorSelector.setPrefWidth(200);
        colorSelector.setConverter(new SelectorConvert<ColorType>() {
            @Override
            public String toString(ColorType object) {
                return object.getColor();
            }

            @Override
            public ColorType fromString(String string) {
                return ColorType.other(string);
            }
        });

        colorSelector.setSizeType(SizeType.SMALL);

        colorSelector.setSelectorType(SelectorType.LIST);
        colorSelector.getItems().addAll(
                ColorType.primary(),
                ColorType.secondary(),
                ColorType.danger(),
                ColorType.warning(),
                ColorType.success(),
                ColorType.other("#ff00ff")
        );
        colorSelector.getValues().add(ColorType.primary());

        colorSelector.setCellFactory(new SelectorCellFactory<ColorType>() {
            @Override
            public void updateItem(IndexedCell<ColorType> cell, ColorType item, boolean empty) {
                if(item == null || empty){
                    cell.setText(null);
                    cell.setGraphic(null);
                }else{
                    cell.setText(colorSelector.getConverter().toString(item));
                    Rectangle rectangle = new Rectangle(15,15, item.getPaint());
                    cell.setGraphic(rectangle);
                }
            }
        });

        colorSelector.getValues().addListener(new ListChangeListener<ColorType>() {
            @Override
            public void onChanged(Change<? extends ColorType> c) {
                ObservableList<ColorType> values = colorSelector.getValues();
                if(values.size() == 0){
                    return;
                }

                ColorType item = colorSelector.getValues().get(0);
                Rectangle rectangle = new Rectangle(20,20, item.getPaint());
                colorSelector.setPrefix(rectangle);

                if(item.getLabel().equals("other")){
                    colorField.setVisible(true);
                    colorField.setManaged(true);
                    tableView.setColorType(ColorType.other(colorField.getText()));
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    tableView.setColorType(item);

                    javaCodes.put("colorType","tableView.setColorType(ColorType.get(\""+item+"\"));\r\n");
                    cssCodes.put("-fx-type-color",item.toString().toLowerCase()+";");
                }
            }
        });

//        selector.setPrefix(new XmFontIcon("\ue69a"));

        XmLabel label = new XmLabel("控件颜色：");

        HBox box = new HBox(label, colorSelector);

        box.setAlignment(Pos.TOP_LEFT);
        this.addActionComponent(box);

        setMyDefineColor();
    }

    /**
     * 设置自定义颜色
     */
    private void setMyDefineColor(){
        colorField = new XmTextField("自定义颜色");
        colorField.setLabel("自定义颜色：");
        colorField.setVisible(false);
        colorField.setManaged(false);
        colorField.setText("linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #23d0f3ff 0.0%, #d791f9ff 50.0%, #fe7b84ff 100.0%)");
//        labelField.setLabelWidth(115);
        colorField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        colorField.setSizeType(SizeType.SMALL);

        colorField.setOnKeyReleased(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                tableView.setColorType(ColorType.other(colorField.getText().trim()));
                javaCodes.put("setMyColor", "listView.setColorType(ColorType.other(\""+ colorField.getText()+"\"));");
                cssCodes.put("-fx-type-color", colorField.getText().trim()+";");
            }
        });

        this.addActionComponent(colorField);
    }

    /**
     * 设置色调类型
     */
    private void setHue() {
        XmLabel label = new XmLabel("控件色调：");

        XmCheckBox<HueType> darkCb = new XmCheckBox<HueType>();
        darkCb.setValue(HueType.DARK);
        darkCb.setText("DARK");
        darkCb.setSizeType(SizeType.SMALL);

        XmCheckBox<HueType> lightCb = new XmCheckBox<HueType>();
        lightCb.setValue(HueType.LIGHT);
        lightCb.setText("LIGHT");
        lightCb.setSizeType(SizeType.SMALL);
        lightCb.setSelected(true);

        XmToggleGroup<HueType> tg = new XmToggleGroup<>();
        darkCb.setToggleGroup(tg);
        lightCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, darkCb, lightCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            buildTableView();
            tableView.setHueType(nv.getValue());

            if(HueType.LIGHT.equals(nv.getValue())){
                tablePane.setStyle("-fx-background-color: transparent;");
            }else{
                tablePane.setStyle("-fx-background-color: black;");
            }


            this.javaCodes.put("hueType", "tableView.setHueType(HueType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-hue", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置尺寸
     */
    private void setSize() {

        XmLabel label = new XmLabel("控件尺寸：");

        XmCheckBox<SizeType> smallCb = new XmCheckBox<SizeType>();
        smallCb.setValue(SizeType.SMALL);
        smallCb.setText("SMALL");
        smallCb.setSizeType(SizeType.SMALL);

        XmCheckBox<SizeType> mediumCb = new XmCheckBox<SizeType>();
        mediumCb.setValue(SizeType.MEDIUM);
        mediumCb.setText("MEDIUM");
        mediumCb.setSizeType(SizeType.SMALL);
        mediumCb.setSelected(true);

        XmCheckBox<SizeType> largeCb = new XmCheckBox<SizeType>();
        largeCb.setValue(SizeType.LARGE);
        largeCb.setText("LARGE");
        largeCb.setSizeType(SizeType.SMALL);

        XmToggleGroup<SizeType> tg = new XmToggleGroup<>();
        smallCb.setToggleGroup(tg);
        mediumCb.setToggleGroup(tg);
        largeCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, smallCb, mediumCb, largeCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            tableView.setSizeType(nv.getValue());
            this.javaCodes.put("sizeType", "tableView.setSizeType(SizeType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-size", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

}
