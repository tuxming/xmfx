# 表格视图（XmTableView）

![button](../images/tableView.gif)



## 使用

1,  准备数据

```java
ObservableList<Product> data = FXCollections.observableArrayList(
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
        )
```

2，初始化表格视图

```java
tableView = new XmTableView<>();
tableView.getStyleClass().add(".my-table");
tableView.setPrefWidth(400);
tableView.setPrefHeight(400);
tableView.setItems(data);
```

3，设置行

```java
tableView.setRowFactory(param -> new XmTableRow<>());
```

4， 设置列, 这里只实现checkbox, textfield, 普通的cell

```java
TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
idCol.setCellValueFactory(new PropertyValueFactory<>("id"));  //绑定到id这个字段
idCol.setCellFactory(param -> new XmCheckBoxTableCell<>());	  //设置为XmCheckBoxTableCell

TableColumn<Product, String> codeCol = new TableColumn<>("编号");  
codeCol.setCellValueFactory(new PropertyValueFactory<>("code")); //绑定到code这个字段
codeCol.setCellFactory(XmTextFieldTableCell.forTableColumn());	 //设置为XmTextFieldTableCell
//要使这个tabelCell能够编辑，需要做如下设置
//code这个字段需要是property并且有getter, setter方法
/*
private StringProperty code =  new SimpleStringProperty()
public String getCode(){return code.get();}
public void setCode(String code){this.code.set(code);}
public StringProperty codeProperty(){return code;}
*/
//表格设置为可编辑
tableView.setEditable(true);
//监听双击事件
tableView.setOnMouseClicked(event -> {
    if(event.getClickCount() == 2) {
        //判断
        TablePosition pos = tableView.getFocusModel().getFocusedCell();
        tableView.edit(pos.getRow(), codeCol); // 编辑名称
    }
});
//处理编辑完成后的提交事件
codeCol.setOnEditCommit(event -> {
    // 处理编辑确认事件
    Product person = event.getRowValue();
    person.setCode(event.getNewValue());
});


TableColumn<Product, String> nameCol = new TableColumn<>("标题");
nameCol.setCellValueFactory(new PropertyValueFactory<>("title")); //绑定到title这个字段
nameCol.setCellFactory(param -> new XmTableCell<>(){		//设置为XmTableCell
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
```



## 取值

```java
//如果有XmTextFieldTableCell，可以用这个取值
tableView.getCheckedValues();
//获取当前选中值
tableView.getSelectionModel().getSelectedItem();
```



[实例代码(TestTable)](../../Example/src/main/java/com/xm2013/example/test/TestTable.java)

[实例代码(TableViewPage)](../../Example/src/main/java/com/xm2013/example/example/page/TableViewPage.java)





