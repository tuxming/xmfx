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
package com.xm2013.example.example.page;

import com.xm2013.example.test.MySelectorItem;
import com.xm2013.example.test.TestSelector;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.gridview.XmCheckBoxGridCell;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.selector.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Predicate;

public class SelectorPage extends BasePage{

    private BooleanProperty multiple = new SimpleBooleanProperty(false);
    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private XmTextField colorField;
    private String defaultJavaCode = "";
    private XmSelector selector;
    private String css;

    public SelectorPage(){

        this.setTitle("下拉选择框（XmSelector）", new XmFontIcon("\ueb94"));
        this.setComponentTitle("属性");

        XmButton listBtn = new XmButton("列表选择");
        listBtn.setSizeType(SizeType.SMALL);

        XmButton gridBtn = new XmButton("网格选择");
        gridBtn.setSizeType(SizeType.SMALL);

        XmButton treeBtn = new XmButton("树形选择");
        treeBtn.setSizeType(SizeType.SMALL);

        HBox hbox = new HBox(listBtn, gridBtn, treeBtn);
        VBox box = new VBox(hbox);

        this.addActionComponent(box);

        listBtn.setOnAction(e -> {

            VBox pane = this.getComponentSettingPane();
            pane.getChildren().removeIf(new Predicate<Node>() {
                @Override
                public boolean test(Node node) {
                    return node != box;
                }
            });
            listSelector();

        });

        gridBtn.setOnAction(e -> {

            VBox pane = this.getComponentSettingPane();
            pane.getChildren().removeIf(new Predicate<Node>() {
                @Override
                public boolean test(Node node) {
                    return node != box;
                }
            });
            gridSelector();

        });

        treeBtn.setOnAction(e -> {

            VBox pane = this.getComponentSettingPane();
            pane.getChildren().removeIf(new Predicate<Node>() {
                @Override
                public boolean test(Node node) {
                    return node != box;
                }
            });
            treeSelector();

        });

        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text =defaultJavaCode;

                for(String key: javaCodes.keySet()){
                    text += javaCodes.get(key)+"\r\n";
                }

                getJavaText().setText(text);
            }
        });

        cssCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text = "";
                for(String key: cssCodes.keySet()){
                    text += key+":"+cssCodes.get(key)+"\r\n";
                }

                getCssText().setText(text);
            }
        });

        getClearBtn().setOnAction(e -> {
            selector.getStylesheets().remove(css);
        });

        getRunBtn().setOnAction(e -> {
            selector.getStylesheets().remove(css);
            css = ".my-btn{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            selector.getStylesheets().add(css);
        });

    }

    private void listSelector(){

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

        XmSelector<SelectorItem> selector = new XmSelector<SelectorItem>();
        selector.setMaxTagCount(3);
        selector.setHueType(HueType.LIGHT);
        selector.setPromptText("请选择");
        selector.setConverter(convert);
        selector.setPrefWidth(250);
        for(int i=0; i<50; i++){
            final int index = i;
            selector.getItems().add(new MySelectorItem("节点 "+(i+1)){
                /**
                 * SelectorItemBase 设置了tag颜色，这里自定义颜色
                 * @return
                 */
                @Override
                public ColorType getSelectedColorType() {
                    if(index%2 == 0){
                        return ColorType.success();
                    }else{
                        return null;
                    }
                }

                /**
                 * SelectorItemBase 设置了tag的色调类型，这里自定义颜色
                 * @return
                 */
                @Override
                public HueType getSelectedHueType() {
                    if(index%2 == 0){
                        return HueType.LIGHT;
                    }else{
                        return HueType.DARK;
                    }
                }
            });
        }

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

        this.defaultJavaCode = "SelectorConvert<SelectorItem> convert = new SelectorConvert<SelectorItem>() {\n" +
                "            @Override\n" +
                "            public String toString(SelectorItem object) {\n" +
                "                return object!=null?object.getLabel():null;\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public SelectorItem fromString(String string) {\n" +
                "                return new MySelectorItem(string);\n" +
                "            }\n" +
                "        };\n" +
                "\n" +
                "        XmSelector<SelectorItem> selector = new XmSelector<SelectorItem>();\n" +
                "        selector.setHueType(HueType.LIGHT);\n" +
                "        selector.setPromptText(\"请选择\");\n" +
                "        selector.setConverter(convert);\n" +
                "        selector.setPrefWidth(250);\n" +
                "        for(int i=0; i<50; i++){\n" +
                "            final int index = i;\n" +
                "            selector.getItems().add(new MySelectorItem(\"节点 \"+(i+1)){\n" +
                "                /**\n" +
                "                 * SelectorItemBase 设置了tag颜色，这里自定义颜色\n" +
                "                 * @return\n" +
                "                 */\n" +
                "                @Override\n" +
                "                public ColorType getSelectedColorType() {\n" +
                "                    if(index%2 == 0){\n" +
                "                        return ColorType.success();\n" +
                "                    }else{\n" +
                "                        return null;\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                /**\n" +
                "                 * SelectorItemBase 设置了tag的色调类型，这里自定义颜色\n" +
                "                 * @return\n" +
                "                 */\n" +
                "                @Override\n" +
                "                public HueType getSelectedHueType() {\n" +
                "                    if(index%2 == 0){\n" +
                "                        return HueType.LIGHT;\n" +
                "                    }else{\n" +
                "                        return HueType.DARK;\n" +
                "                    }\n" +
                "                }\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "        selector.setCellFactory(new SelectorCellFactory<SelectorItem>() {\n" +
                "            @Override\n" +
                "            public void updateItem(IndexedCell<SelectorItem> cell, SelectorItem item, boolean empty) {\n" +
                "                if (empty || item == null) {\n" +
                "                    cell.setText(null);\n" +
                "                } else {\n" +
                "                    String text = selector.getConverter().toString(item);\n" +
                "                    cell.setText(text);\n" +
                "                }\n" +
                "            }\n" +
                "        });\r\n\r\n";

        this.setShowComponent(selector);
        this.setSettings(selector);
        this.selector = selector;

    }

    private void gridSelector(){

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
        selector.setPrefWidth(250);

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
                    ((XmCheckBoxGridCell<File>) cell).getGridView().setCellHeight(90);
                    ((XmCheckBoxGridCell<File>) cell).getGridView().setCellWidth(90);
                    isSetSkin = true;
                }

                if (empty || item == null) {
                    cell.setText(null);
                } else {
                    cell.setText(item.getName());
                    ImageView imageView = new ImageView(new Image(item.getAbsolutePath(),
                            90, 90, true,
                            false));

                    imageView.setFitWidth(90);
                    cell.setGraphic(imageView);
                }
            }
        });

        this.defaultJavaCode = "SelectorConvert<File> convert = new SelectorConvert<File>() {\n" +
                "            @Override\n" +
                "            public String toString(File object) {\n" +
                "                return object!=null?object.getName():null;\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public File fromString(String string) {\n" +
                "                return new File(string);\n" +
                "            }\n" +
                "        };\n" +
                "\n" +
                "        XmSelector<File> selector = new XmSelector<File>();\n" +
                "        selector.setPromptText(\"请选择\");\n" +
                "        selector.setSelectorType(SelectorType.GRID);\n" +
                "        selector.setConverter(convert);\n" +
                "        selector.setRoundType(RoundType.CIRCLE);\n" +
                "        selector.setPrefWidth(250);\n" +
                "\n" +
                "        File file = new File(TestSelector.class.getResource(\"/images/girls\").getFile());\n" +
                "        File[] files = file.listFiles();\n" +
                "        for (File file1 : files) {\n" +
                "            selector.getItems().add(file1);\n" +
                "        }\n" +
                "\n" +
                "        selector.setCellFactory(new SelectorCellFactory<File>() {\n" +
                "            private boolean isSetSkin = false;\n" +
                "            @Override\n" +
                "            public void updateItem(IndexedCell<File> cell, File item, boolean empty) {\n" +
                "\n" +
                "                if(!isSetSkin) {\n" +
                "                    XmCheckBoxGridCell<File> checkCell= (XmCheckBoxGridCell<File>) cell;\n" +
                "                    checkCell.getCheckBox().setSizeType(SizeType.SMALL);\n" +
                "                    ((XmCheckBoxGridCell<File>) cell).getGridView().setCellHeight(90);\n" +
                "                    ((XmCheckBoxGridCell<File>) cell).getGridView().setCellWidth(90);\n" +
                "                    isSetSkin = true;\n" +
                "                }\n" +
                "\n" +
                "                if (empty || item == null) {\n" +
                "                    cell.setText(null);\n" +
                "                } else {\n" +
                "                    cell.setText(item.getName());\n" +
                "                    ImageView imageView = new ImageView(new Image(item.getAbsolutePath(),\n" +
                "                            90, 90, true,\n" +
                "                            false));\n" +
                "                    imageView.setFitWidth(90);\n" +
                "                    cell.setGraphic(imageView);\n" +
                "                }\n" +
                "            }\n" +
                "        });\r\n\r\n";

        this.setShowComponent(selector);
        this.setSettings(selector);
        this.selector = selector;

    }

    private void treeSelector(){
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
        selector.setPrefWidth(250);
        selector.setSelectorType(SelectorType.TREE);
        selector.setItems(rootMenu);

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

        this.defaultJavaCode = "SelectorConvert<MySelectorItem> convert = new SelectorConvert<MySelectorItem>() {\n" +
                "            @Override\n" +
                "            public List<MySelectorItem> getChildren(MySelectorItem item) {\n" +
                "                List<MySelectorItem> nc = new ArrayList<>();\n" +
                "                ObservableList<SelectorItem> children = item.getChildren();\n" +
                "                if(children!=null && children.size()>0){\n" +
                "                    for (SelectorItem si: children) {\n" +
                "                        nc.add((MySelectorItem) si);\n" +
                "                    }\n" +
                "                }\n" +
                "                return nc;\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public Node getIcon(MySelectorItem t) {\n" +
                "                return t.getIcon();\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public String toString(MySelectorItem object) {\n" +
                "                return object!=null?object.getLabel():null;\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public MySelectorItem fromString(String string) {\n" +
                "                return new MySelectorItem(string);\n" +
                "            }\n" +
                "        };\n" +
                "\n" +
                "        MySelectorItem rootMenu = buildTreeData();\n" +
                "\n" +
                "        XmSelector<MySelectorItem> selector = new XmSelector<MySelectorItem>();\n" +
                "        selector.setPromptText(\"请选择\");\n" +
                "        selector.setConverter(convert);\n" +
                "        selector.setPrefWidth(250);\n" +
                "        selector.setSelectorType(SelectorType.TREE);\n" +
                "        selector.setItems(rootMenu);\n" +
                "\n" +
                "        selector.setCellFactory(new SelectorCellFactory<MySelectorItem>() {\n" +
                "            @Override\n" +
                "            public void updateItem(IndexedCell<MySelectorItem> cell, MySelectorItem item, boolean empty) {\n" +
                "                if (empty || item == null) {\n" +
                "                    cell.setText(null);\n" +
                "                } else {\n" +
                "                    String text = convert.toString(item);\n" +
                "                    cell.setText(text);\n" +
                "                }\n" +
                "            }\n" +
                "        });\r\n";

        this.setShowComponent(selector);
        this.setSettings(selector);
        this.selector = selector;
    }

    private void setSettings(XmSelector selector){
        //设置是否支持多选
        setSupportMultiple(selector);
        //设置是否关闭标签
        setAllowCloseTag(selector);
        //设置是否可编辑
        setAllowEdit(selector);
        //设置颜色类型
        setColor(selector);
        //设置尺寸
        setSize(selector);
        //设置圆角类型
        setRound(selector);
        //设置色调类型
        setHue(selector);
        //设置下拉箭头背景色是否填充
        setFillArrow(selector);
    }


    /**
     * 设置是否支持多选
     * @param selector
     */
    private void setSupportMultiple(XmSelector selector){
        XmLabel label = new XmLabel("是否多选：");
        XmCheckBox checkbox = new XmCheckBox();
        checkbox.setSizeType(SizeType.SMALL);
        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, checkbox);

        multiple.bind(checkbox.selectedProperty());
        checkbox.selectedProperty().addListener((ob, ov, nv) ->{
            selector.setMultiple(nv);
            this.javaCodes.put("multiple", "selector.setMultiple("+nv+");");
            this.cssCodes.put("-fx-multiple", nv+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置是否关闭标签
     * @param selector
     */
    private void setAllowCloseTag(XmSelector selector) {
        XmLabel label = new XmLabel("是否可关闭：");
        XmCheckBox checkbox = new XmCheckBox();
        checkbox.setSizeType(SizeType.SMALL);
        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, checkbox);

        box.visibleProperty().bind(multiple);
        box.managedProperty().bind(multiple);

        checkbox.selectedProperty().addListener((ob, ov, nv) ->{
            selector.setCloseable(nv);
            this.javaCodes.put("closeable", "selector.setCloseable("+nv+");");
            this.cssCodes.put("-fx-closeable", nv+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置是否可编辑
     * @param selector
     */
    private void setAllowEdit(XmSelector selector) {
        XmLabel label = new XmLabel("是否可编辑：");
        XmCheckBox checkbox = new XmCheckBox();
        checkbox.setSizeType(SizeType.SMALL);
        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, checkbox);

        checkbox.selectedProperty().addListener((ob, ov, nv) ->{
            selector.setEditable(nv);
            this.javaCodes.put("editable", "selector.setEditable("+nv+");");
            this.cssCodes.put("-fx-editable", nv+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置颜色类型
     * @param selector
     */
    private void setColor(XmSelector selector) {

        XmSelector<ColorType> colorSelector = new XmSelector<ColorType>();
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
                    selector.setColorType(ColorType.other(colorField.getText()));
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    selector.setColorType(item);

                    javaCodes.put("colorType","selector.setColorType(ColorType.get(\""+item+"\"));\r\n");
                    cssCodes.put("-fx-type-color",item.toString().toLowerCase());
                }


            }
        });

//        selector.setPrefix(new XmFontIcon("\ue69a"));

        XmLabel label = new XmLabel("控件颜色：");

        HBox box = new HBox(label, colorSelector);

        box.setAlignment(Pos.TOP_LEFT);
        this.addActionComponent(box);

        setMyDefineColor(selector);
    }

    /**
     * 设置自定义颜色
     */
    private void setMyDefineColor(XmSelector selector){
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
                selector.setColorType(ColorType.other(colorField.getText().trim()));
                javaCodes.put("setMyColor", "selector.setColorType(ColorType.other(\""+ colorField.getText()+"\"));");
                cssCodes.put("-fx-type-color:", colorField.getText().trim()+";");
            }
        });

        this.addActionComponent(colorField);
    }

    /**
     * 设置尺寸
     * @param selector
     */
    private void setSize(XmSelector selector) {

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
            selector.setSizeType(nv.getValue());
            this.javaCodes.put("sizeType", "selector.setSizeType(SizeType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-size", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置圆角类型
     * @param selector
     */
    private void setRound(XmSelector selector) {

        XmLabel label = new XmLabel("控件圆角：");

        XmCheckBox<RoundType> smallCb = new XmCheckBox<RoundType>();
        smallCb.setValue(RoundType.NONE);
        smallCb.setText("NONE");
        smallCb.setSizeType(SizeType.SMALL);
        smallCb.setSelected(true);

        XmCheckBox<RoundType> mediumCb = new XmCheckBox<RoundType>();
        mediumCb.setValue(RoundType.SMALL);
        mediumCb.setText("SMALL");
        mediumCb.setSizeType(SizeType.SMALL);

        XmCheckBox<RoundType> largeCb = new XmCheckBox<RoundType>();
        largeCb.setValue(RoundType.SEMICIRCLE);
        largeCb.setText("SEMICIRCLE");
        largeCb.setSizeType(SizeType.SMALL);

        XmToggleGroup<RoundType> tg = new XmToggleGroup<>();
        smallCb.setToggleGroup(tg);
        mediumCb.setToggleGroup(tg);
        largeCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, smallCb, mediumCb, largeCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            selector.setRoundType(nv.getValue());
            this.javaCodes.put("roundType", "selector.setRoundType(RoundType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-round", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置色调类型
     * @param selector
     */
    private void setHue(XmSelector selector) {
        XmLabel label = new XmLabel("控件色调：");

        XmCheckBox<HueType> darkCb = new XmCheckBox<HueType>();
        darkCb.setValue(HueType.LIGHT);
        darkCb.setText("NONE");
        darkCb.setSizeType(SizeType.SMALL);

        XmCheckBox<HueType> lightCb = new XmCheckBox<HueType>();
        lightCb.setValue(HueType.DARK);
        lightCb.setText("SMALL");
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
            selector.setHueType(nv.getValue());
            this.javaCodes.put("hueType", "selector.setHueType(HueType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-hue", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置下拉箭头背景色是否填充
     * @param selector
     */
    private void setFillArrow(XmSelector selector) {
        XmLabel label = new XmLabel("下拉箭头是否填充：");
        XmCheckBox checkbox = new XmCheckBox();
        checkbox.setSelected(true);
        checkbox.setSizeType(SizeType.SMALL);
        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, checkbox);

        checkbox.selectedProperty().addListener((ob, ov, nv) ->{
            selector.setFillArrow(nv);
            this.javaCodes.put("multiple", "selector.setFillArrow("+nv+");");
            this.cssCodes.put("-fx-fill-arrow", nv+";");
        });

        box.visibleProperty().bind(selector.multipleProperty().not());
        box.managedProperty().bind(selector.multipleProperty().not());

        this.addActionComponent(box);
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

}
