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

import com.xm2013.example.test.FileTree;
import com.xm2013.example.test.MyDMenu;
import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.base.XmStringConverter;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.listview.XmCheckBoxListCell;
import com.xm2013.jfx.control.listview.XmListCell;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.treeview.XmCheckBoxTreeCell;
import com.xm2013.jfx.control.treeview.XmMenuTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeView;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class TreeViewPage extends BasePage{

    private XmTreeView<MyDMenu> treeView;
    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    private XmCheckBox checkBoxCell = new XmCheckBox();
    private Pane showPane;
    private XmToggleGroup<Integer> tg;
    private XmCheckBox checkBoxShowArrow;

    public TreeViewPage(){
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        this.setTitle("Tree（XmTreeView）", new XmFontIcon("\ue892"));
        this.setComponentTitle("属性");
        buildTreeView();

        setShowComponent(showPane);
        setColor();
        setHue();
        setSize();

        showCheckBoxCell();
        showArrow();


    }


    public void buildTreeView(){

        MyDMenu rootMenu = FileTree.build();
        CheckBoxTreeItem<MyDMenu> rootItem = new CheckBoxTreeItem<>(rootMenu);
        rootItem.setGraphic(rootMenu.getIcon());
        rootItem.setExpanded(true);

        buildMenu(rootItem, rootMenu);

        treeView = new XmTreeView<>(rootItem);
        treeView.getStyleClass().add(".my-treeview");
        treeView.setHueType(HueType.LIGHT);
        setListCellFactory();

        showPane = new Pane(treeView);

        String javaCodePrev = "MyDMenu rootMenu = FileTree.build();\n" +
                "CheckBoxTreeItem<MyDMenu> rootItem = new CheckBoxTreeItem<>(rootMenu);\n" +
                "rootItem.setGraphic(rootMenu.getIcon());\n" +
                "rootItem.setExpanded(true);\n" +
                "\n" +
                "buildMenu(rootItem, rootMenu);\n" +
                "\n" +
                "treeView = new XmTreeView<>(rootItem);\n" +
                "treeView.getStyleClass().add(\".my-treeview\");\n" +
                "treeView.setHueType(HueType.LIGHT);\n" +
                "setListCellFactory();\r\n";
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
            treeView.getStylesheets().remove(css);
            setListCellFactory();
        });

        getRunBtn().setOnAction(e -> {
            treeView.getStylesheets().remove(css);
            css = ".my-treeview{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");

                setListCellFactory();
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            treeView.getStylesheets().add(css);
        });

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

    public void setListCellFactory(){
        Integer value = 1;
        if(tg!=null && tg.getValue()!=null){
            value = tg.getValue();
        }
        if(value == null || value == 1){
            treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {
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

            javaCodes.put("cellFactory",
                    "treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {\n" +
                            "   @Override\n" +
                            "   public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {\n" +
                            "       return new XmTreeCell<>(){\n" +
                            "           @Override\n" +
                            "           public void updateItem(MyDMenu item, boolean empty) {\n" +
                            "               super.updateItem(item, empty);\n" +
                            "               if(empty){\n" +
                            "                   setText(null);\n" +
                            "                   setGraphic(null);\n" +
                            "               }else{\n" +
                            "                   setText(item.getLabelName());\n" +
                            "               }\n" +
                            "           }\n" +
                            "       };\n" +
                            "   }\n" +
                            "});\n");

        }else if(value == 2){
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
            javaCodes.put("cellFactory",
                    "treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {\n" +
                            "   @Override\n" +
                            "   public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {\n" +
                            "       return new XmCheckBoxTreeCell<>(new Callback<TreeItem<MyDMenu>, ObservableValue<Boolean>>() {\n" +
                            "           @Override\n" +
                            "           public ObservableValue<Boolean> call(TreeItem<MyDMenu> param) {\n" +
                            "               if (param instanceof CheckBoxTreeItem<?>) {\n" +
                            "                   return ((CheckBoxTreeItem<?>) param).selectedProperty();\n" +
                            "               }\n" +
                            "               return null;\n" +
                            "           }\n" +
                            "      }, new StringConverter<TreeItem<MyDMenu>>() {\n" +
                            "           @Override\n" +
                            "           public String toString(TreeItem<MyDMenu> object) {\n" +
                            "               return object.getValue().getLabelName();\n" +
                            "           }\n" +
                            "\n" +
                            "           @Override\n" +
                            "           public TreeItem<MyDMenu> fromString(String string) {\n" +
                            "               return new TreeItem<>(new MyDMenu(string));\n" +
                            "           }\n" +
                            "       });\n" +
                            "   }\n" +
                            "});\n");

        }else if(value == 3){
            treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {
                @Override
                public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {
                    return new XmMenuTreeCell<>(){
                        @Override
                        public void init() {
                            setVisibleArrow(checkBoxShowArrow.isSelected());

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

            javaCodes.put("cellFactory",
                    "treeView.setCellFactory(new Callback<TreeView<MyDMenu>, TreeCell<MyDMenu>>() {\n" +
                    "    @Override\n" +
                    "    public TreeCell<MyDMenu> call(TreeView<MyDMenu> param) {\n" +
                    "        return new XmMenuTreeCell<>(){\n" +
                    "            @Override\n" +
                    "            public void init() {\n" +
                    "                setVisibleArrow("+checkBoxShowArrow.isSelected()+");\n" +
                    "\n" +
                    "                setUpdateDefaultSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {\n" +
                    "                    @Override\n" +
                    "                    public void call(XmMenuTreeCell<MyDMenu> cell) {\n" +
                    "                        cell.setStyle(\"-fx-background-color: transparent; -fx-text-fill: #aa00aa; -fx-border-width:0 0 0 5; -fx-border-color: transparent;\");\n" +
                    "                        try {\n" +
                    "                            XmFontIcon icon = (XmFontIcon) getItem().getIcon();\n" +
                    "                            icon.setColor(Color.web(\"#aa00aa\"));\n" +
                    "                        }catch (Exception e){\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                });\n" +
                    "\n" +
                    "                setUpdateOddSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {\n" +
                    "                    @Override\n" +
                    "                    public void call(XmMenuTreeCell<MyDMenu> cell) {\n" +
                    "                        cell.setStyle(\"-fx-background-color: transparent; -fx-text-fill: #aa00aa; -fx-border-width:0 0 0 5; -fx-border-color: transparent;\");\n" +
                    "                        try {\n" +
                    "                            XmFontIcon icon = (XmFontIcon) getItem().getIcon();\n" +
                    "                            icon.setColor(Color.web(\"#aa00aa\"));\n" +
                    "                        }catch (Exception e){\n" +
                    "\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                });\n" +
                    "\n" +
                    "                setUpdateHoverSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {\n" +
                    "                    @Override\n" +
                    "                    public void call(XmMenuTreeCell<MyDMenu> cell) {\n" +
                    "                        cell.setStyle(\"-fx-background-color: #ff00ff22; -fx-text-fill: #aa00aa; -fx-border-width:0 0 0 5; -fx-border-color: transparent;\");\n" +
                    "                        try {\n" +
                    "                            XmFontIcon icon = (XmFontIcon) getItem().getIcon();\n" +
                    "                            icon.setColor(Color.web(\"#aa00aa\"));\n" +
                    "                        }catch (Exception e){\n" +
                    "\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                });\n" +
                    "\n" +
                    "                setUpdateSelectedSkin(new CallBack<XmMenuTreeCell<MyDMenu>>() {\n" +
                    "                    @Override\n" +
                    "                    public void call(XmMenuTreeCell<MyDMenu> cell) {\n" +
                    "                        cell.setStyle(\"-fx-background-color: #aa00aa; -fx-text-fill: white;  -fx-border-width:0 0 0 5; -fx-border-color: #ff00ff;\");\n" +
                    "                        try {\n" +
                    "                            XmFontIcon icon = (XmFontIcon) getItem().getIcon();\n" +
                    "                            icon.setColor(Color.WHITE);\n" +
                    "                        }catch (Exception e){\n" +
                    "\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                });\n" +
                    "\n" +
                    "            }\n" +
                    "\n" +
                    "            @Override\n" +
                    "            public void updateItem(MyDMenu item, boolean empty) {\n" +
                    "                super.updateItem(item, empty);\n" +
                    "                if(empty){\n" +
                    "                    setText(null);\n" +
                    "                    setGraphic(null);\n" +
                    "                }else{\n" +
                    "                    setText(item.getLabelName());\n" +
                    "                }\n" +
                    "            }\n" +
                    "        };\n" +
                    "    }\n" +
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
                    treeView.setColorType(ColorType.other(colorField.getText()));
                    setListCellFactory();
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    treeView.setColorType(item);
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
                treeView.setColorType(ColorType.other(colorField.getText().trim()));
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
            treeView.setHueType(nv.getValue());

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
            treeView.setSizeType(nv.getValue());
            this.javaCodes.put("sizeType", "treeView.setSizeType(SizeType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-size", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    private void showCheckBoxCell() {

        XmLabel label = new XmLabel("Cell类型：");
        XmCheckBox<Integer> normalCb = new XmCheckBox<Integer>();
        normalCb.setConverter(new XmStringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return "普通";
            }
        });
        normalCb.setValue(1);
        normalCb.setSelected(true);
        normalCb.setSizeType(SizeType.SMALL);

        XmCheckBox<Integer> checkBoxCb = new XmCheckBox<Integer>();
        checkBoxCb.setValue(2);
        checkBoxCb.setConverter(new XmStringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return "复选框";
            }
        });
        checkBoxCb.setSizeType(SizeType.SMALL);

        XmCheckBox<Integer> menuCb = new XmCheckBox<Integer>();
        menuCb.setValue(3);
        menuCb.setConverter(new XmStringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return "菜单";
            }
        });
        menuCb.setSizeType(SizeType.SMALL);

        tg = new XmToggleGroup<>();
        normalCb.setToggleGroup(tg);
        checkBoxCb.setToggleGroup(tg);
        menuCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, normalCb, checkBoxCb, menuCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            setListCellFactory();
        });

        this.addActionComponent(box);
    }

    private void showArrow() {
        checkBoxShowArrow = new XmCheckBox();
        checkBoxShowArrow.setSizeType(SizeType.SMALL);
        checkBoxShowArrow.selectedProperty().addListener((ob, ov, nv)->{
            setListCellFactory();
            treeView.setVisibleArrow(nv);
            javaCodes.put("visibleArrow", "treeView.setVisibleArrow("+nv+");");
        });
        XmLabel label = new XmLabel("是否显示箭头：");
        HBox box = new HBox(label, checkBoxShowArrow);
        box.setAlignment(Pos.TOP_LEFT);

        this.addActionComponent(box);
    }

}
