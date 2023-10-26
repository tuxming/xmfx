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

import com.xm2013.example.test.MyDMenu;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.listview.XmCheckBoxListCell;
import com.xm2013.jfx.control.listview.XmListCell;
import com.xm2013.jfx.control.listview.XmListView;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class ListViewPage extends BasePage{

    private XmListView<MyDMenu> listView;
    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    private XmCheckBox checkBoxCell = new XmCheckBox();
    private Pane showPane;

    public ListViewPage(){
        this.setTitle("列表视图（XmListView）", new XmFontIcon("\ue8fc"));
        this.setComponentTitle("属性");
        buildListView();

        setShowComponent(showPane);
        setColor();
        setHue();
        setSize();

        checkBoxCell.setSizeType(SizeType.SMALL);
        checkBoxCell.selectedProperty().addListener((ob, ov, nv)->{
            setListCellFactory();
        });
        XmLabel label = new XmLabel("CheckBoxCell：");
        HBox box = new HBox(label, checkBoxCell);
        box.setAlignment(Pos.TOP_LEFT);

        this.addActionComponent(box);

    }

    public void buildListView(){
        ObservableList<MyDMenu> menus = FXCollections.observableArrayList();
        menus.addAll(new MyDMenu("我的电脑"),
                new MyDMenu("网络设置"),
                new MyDMenu("我的文档"),
                new MyDMenu("系统设置"),
                new MyDMenu("回收站"),
                new MyDMenu("Microsoft Edge"),
                new MyDMenu("Photoshop"),
                new MyDMenu("我的电脑1"),
                new MyDMenu("网络设置1"),
                new MyDMenu("我的文档1"),
                new MyDMenu("系统设置1"),
                new MyDMenu("回收站1"),
                new MyDMenu("Microsoft Edge1"),
                new MyDMenu("Photoshop1"),
                new MyDMenu("我的电脑2"),
                new MyDMenu("网络设置2"),
                new MyDMenu("我的文档2"),
                new MyDMenu("系统设置2"),
                new MyDMenu("回收站2"),
                new MyDMenu("Microsoft Edge2"),
                new MyDMenu("Photoshop2"),
                new MyDMenu("我的电脑3"),
                new MyDMenu("网络设置3"),
                new MyDMenu("我的文档3"),
                new MyDMenu("系统设置3"),
                new MyDMenu("回收站3"),
                new MyDMenu("Microsoft Edge3"),
                new MyDMenu("Photoshop3")
        );

        listView = new XmListView<MyDMenu>(menus);
        listView.getStyleClass().add(".my-listview");
        listView.setHueType(HueType.LIGHT);
        setListCellFactory();

        showPane = new Pane(listView);


        String javaCodePrev = "ObservableList<MyDMenu> menus = FXCollections.observableArrayList();\n" +
                "menus.addAll(new MyDMenu(\"我的电脑\"),\n" +
                "    new MyDMenu(\"网络设置\"),\n" +
                "    new MyDMenu(\"我的文档\"),\n" +
                "    new MyDMenu(\"系统设置\"),\n" +
                "    new MyDMenu(\"回收站\"),\n" +
                "    new MyDMenu(\"Microsoft Edge\"),\n" +
                "    new MyDMenu(\"Photoshop\"),\n" +
                "    new MyDMenu(\"我的电脑1\"),\n" +
                "    new MyDMenu(\"网络设置1\"),\n" +
                "    new MyDMenu(\"我的文档1\"),\n" +
                "    new MyDMenu(\"系统设置1\"),\n" +
                "    new MyDMenu(\"回收站1\"),\n" +
                "    new MyDMenu(\"Microsoft Edge1\"),\n" +
                "    new MyDMenu(\"Photoshop1\"),\n" +
                "    new MyDMenu(\"我的电脑2\"),\n" +
                "    new MyDMenu(\"网络设置2\"),\n" +
                "    new MyDMenu(\"我的文档2\"),\n" +
                "    new MyDMenu(\"系统设置2\"),\n" +
                "    new MyDMenu(\"回收站2\"),\n" +
                "    new MyDMenu(\"Microsoft Edge2\"),\n" +
                "    new MyDMenu(\"Photoshop2\"),\n" +
                "    new MyDMenu(\"我的电脑3\"),\n" +
                "    new MyDMenu(\"网络设置3\"),\n" +
                "    new MyDMenu(\"我的文档3\"),\n" +
                "    new MyDMenu(\"系统设置3\"),\n" +
                "    new MyDMenu(\"回收站3\"),\n" +
                "    new MyDMenu(\"Microsoft Edge3\"),\n" +
                "    new MyDMenu(\"Photoshop3\")\n" +
                ");\r\n" +
                "listView = new XmListView<MyDMenu>(menus);\n" +
                "listView.getStyleClass().add(\".my-listview\");\n" +
                "listView.setHueType(HueType.LIGHT);\r\n";
        getJavaText().setText(javaCodePrev);
        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text =javaCodePrev;

                for(String key: javaCodes.keySet()){
                    text += javaCodes.get(key)+"\n";
                }

                getJavaText().setText(text);
            }
        });

        cssCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text = "";
                for(String key: cssCodes.keySet()){
                    text += key+": "+cssCodes.get(key)+"\r\n";
                }

                getCssText().setText(text);
            }
        });

        getClearBtn().setOnAction(e -> {
            listView.getStylesheets().remove(css);
            setListCellFactory();
        });

        getRunBtn().setOnAction(e -> {
            listView.getStylesheets().remove(css);
            css = ".my-listview{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");

                setListCellFactory();
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            listView.getStylesheets().add(css);
        });

    }

    public void setListCellFactory(){
        if(!checkBoxCell.isSelected()){
            listView.setCellFactory(new Callback<ListView<MyDMenu>, ListCell<MyDMenu>>() {
                @Override
                public ListCell<MyDMenu> call(ListView<MyDMenu> param) {
                    return new XmListCell<>(){
                        @Override
                        protected void updateItem(MyDMenu item, boolean empty) {
                            super.updateItem(item, empty);
                            if(empty || item == null){
                                setText(null);
                            }else{
                                setText(item.getLabelName());
                            }
                        }
                    };
                }
            });
            javaCodes.put("cellFactory",
                    "listView.setCellFactory(new Callback<ListView<MyDMenu>, ListCell<MyDMenu>>() {\n" +
                    "   @Override\n" +
                    "   public ListCell<MyDMenu> call(ListView<MyDMenu> param) {\n" +
                    "       return new ListCell<>(){\n" +
                    "           @Override\n" +
                    "           protected void updateItem(MyDMenu item, boolean empty) {\n" +
                    "               super.updateItem(item, empty);\n" +
                    "               if(empty || item == null){\n" +
                    "       setText(null);\n" +
                    "               }else{\n" +
                    "       setText(item.getLabelName());\n" +
                    "               }\n" +
                    "           }\n" +
                    "       };\n" +
                    "   }\n" +
                    "});\n");
        }else{
            listView.setCellFactory(new Callback<ListView<MyDMenu>, ListCell<MyDMenu>>() {
                @Override
                public ListCell<MyDMenu> call(ListView<MyDMenu> param) {
                    return new XmCheckBoxListCell(){
                        @Override
                        public void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if(empty || item == null){
                                setText(null);
                            }else{
                                setText(((MyDMenu)item).getLabelName());
                            }
                        }
                    };
                }
            });

            javaCodes.put("cellFactory",
                    "listView.setCellFactory(new Callback<ListView<MyDMenu>, ListCell<MyDMenu>>() {\n" +
                    "   @Override\n" +
                    "   public ListCell<MyDMenu> call(ListView<MyDMenu> param) {\n" +
                    "       return new XmCheckBoxListCell(){\n" +
                    "           @Override\n" +
                    "           public void updateItem(Object item, boolean empty) {\n" +
                    "               super.updateItem(item, empty);\n" +
                    "               if(empty || item == null){\n" +
                    "       setText(null);\n" +
                    "               }else{\n" +
                    "       setText(((MyDMenu)item).getLabelName());\n" +
                    "               }\n" +
                    "           }\n" +
                    "       };\n" +
                    "   }\n" +
                    "});\n");

        }
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
                    listView.setColorType(ColorType.other(colorField.getText()));
                    setListCellFactory();
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    listView.setColorType(item);
                    setListCellFactory();

                    javaCodes.put("colorType","listView.setColorType(ColorType.get(\""+item+"\"));\r\n");
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
                listView.setColorType(ColorType.other(colorField.getText().trim()));
                setListCellFactory();
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
            listView.setHueType(nv.getValue());

            if(HueType.LIGHT.equals(nv.getValue())){
                showPane.setStyle("-fx-background-color: transparent;");
            }else{
                showPane.setStyle("-fx-background-color: black;");
            }

            setListCellFactory();

            this.javaCodes.put("hueType", "listView.setHueType(HueType."+nv.getValue()+");");
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
            listView.setSizeType(nv.getValue());
            this.javaCodes.put("sizeType", "listView.setSizeType(SizeType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-size", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

}
