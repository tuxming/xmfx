package com.xm2013.example.example.page;

import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.gridview.GridCell;
import com.xm2013.jfx.control.gridview.GridView;
import com.xm2013.jfx.control.gridview.XmCheckBoxGridCell;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import javafx.collections.*;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class GridViewPage extends BasePage{

    private GridView<String> grid;

    private String prevCode = "";
    private XmCheckBox useCheckBoxCell = new XmCheckBox();

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    public GridViewPage(){
        this.setTitle("网格视图（GridView）", new XmFontIcon("\ue755"));
        this.setComponentTitle("属性");

        buildGridView();
        setColor();

        useCheckBoxCell.setSizeType(SizeType.SMALL);
        useCheckBoxCell.selectedProperty().addListener((ob, ov, nv)->{
            setCellFactory();
        });
        XmLabel label = new XmLabel("使用CheckBoxCell：");
        HBox box = new HBox(label, useCheckBoxCell);
        box.setAlignment(Pos.TOP_LEFT);

        addActionComponent(box);

        getJavaText().setText(prevCode);
        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text = prevCode;

                for (String key : javaCodes.keySet()) {
                    text += javaCodes.get(key) + "\n";
                }

                getJavaText().setText(text);
            }
        });
    }


    private void buildGridView() {

        ObservableList<String> list  = FXCollections.observableArrayList();
        list.addAll("\ue8d0" ,"\ue78c" ,"\ue755" ,"\ue892"  ,"\ue6fd" ,"\uebcc"
                ,"\ueb45" ,"\ue69a" ,"\uec17" ,"\ueabe" ,"\ue663" ,"\ue61c" ,"\ue6de"  ,"\ue65c"
                ,"\ue751" ,"\ue8fc" ,"\ue686" ,"\ue61b" ,"\ue6f7" ,"\ue66d" ,"\ue8ad" ,"\ue646"
                ,"\ue661" ,"\ue609" ,"\ue6ac" ,"\ue8ac" ,"\ue66c" ,"\ue77c" ,"\ue787" ,"\ue758"
                ,"\ue8b8" ,"\ue67c","\ue773","\ue645","\ue71f","\ue65e" ,"\ueb94" ,"\ue6da"
                ,"\ue668","\ue640" ,"\ue67b","\ue7cd","\ue60d","\ue6fc"  ,"\ue62e");

        grid = new GridView<>(list);
        grid.setStyle("-fx-border-color: #bbbbbb; -fx-border-width: 1px; -fx-border-style:solid;");
        grid.setPrefWidth(320);
        grid.setPrefHeight(320);
        grid.setCellHeight(75);
        grid.setCellWidth(75);
        grid.setHorizontalCellSpacing(0);
        grid.setVerticalCellSpacing(0);
        setCellFactory();

        prevCode = "ObservableList<String> list  = FXCollections.observableArrayList();\n" +
                "list.addAll(\"\\ue8d0\" ,\"\\ue78c\" ,\"\\ue755\" ,\"\\ue892\"  ,\"\\ue6fd\" ,\"\\uebcc\"\n" +
                "        ,\"\\ueb45\" ,\"\\ue69a\" ,\"\\uec17\" ,\"\\ueabe\" ,\"\\ue663\" ,\"\\ue61c\" ,\"\\ue6de\"  ,\"\\ue65c\"\n" +
                "        ,\"\\ue751\" ,\"\\ue8fc\" ,\"\\ue686\" ,\"\\ue61b\" ,\"\\ue6f7\" ,\"\\ue66d\" ,\"\\ue8ad\" ,\"\\ue646\"\n" +
                "        ,\"\\ue661\" ,\"\\ue609\" ,\"\\ue6ac\" ,\"\\ue8ac\" ,\"\\ue66c\" ,\"\\ue77c\" ,\"\\ue787\" ,\"\\ue758\"\n" +
                "        ,\"\\ue8b8\" ,\"\\ue67c\",\"\\ue773\",\"\\ue645\",\"\\ue71f\",\"\\ue65e\" ,\"\\ueb94\" ,\"\\ue6da\"\n" +
                "        ,\"\\ue668\",\"\\ue640\" ,\"\\ue67b\",\"\\ue7cd\",\"\\ue60d\",\"\\ue6fc\"  ,\"\\ue62e\");\n" +
                "\n" +
                "grid = new GridView<>(list);\n" +
                "grid.setStyle(\"-fx-border-color: #bbbbbb; -fx-border-width: 1px; -fx-border-style:solid;\");\n" +
                "grid.setPrefWidth(320);\n" +
                "grid.setPrefHeight(320);\n" +
                "grid.setCellHeight(75);\n" +
                "grid.setCellWidth(75);\n" +
                "grid.setHorizontalCellSpacing(0);\n" +
                "grid.setVerticalCellSpacing(0);";

        setShowComponent(grid);

    }

    public void setCellFactory(){

        if(useCheckBoxCell.isSelected()){
            grid.setCellFactory(new Callback<GridView<String>, GridCell<String>>() {
                @Override
                public GridCell<String> call(GridView<String> param) {
                    return buildCheckBoxCell(param);
                }
            });
        }else{
            grid.setCellFactory(new Callback<GridView<String>, GridCell<String>>() {
                @Override
                public GridCell<String> call(GridView<String> param) {
                    return buildNormalCell(param);
                }
            });
        }
    }

    private GridCell<String> buildNormalCell(GridView<String> param){
        GridCell<String> cell =  new GridCell<>(){
            private XmLabel label ;
            private PseudoClass selected = PseudoClass.getPseudoClass("selected");
            private XmFontIcon icon = new XmFontIcon();
            @Override
            public void init() {
                super.init();
                //重写init方法可以实现一些基础设置

                label = new XmLabel();
                //lable可能会阻止事件冒泡，需要设置为不响应鼠标事件
                label.setMouseTransparent(true);

                itemProperty().addListener((ob, ov, nv)->{
                    if(nv!=null){
                        icon.setIcon(nv);
                    }
                });

                label.setGraphic(icon);
                label.setContentDisplay(ContentDisplay.TOP);
                label.setAlignment(XmAlignment.CENTER);

                Color borderColor = Color.web("#bbbbbb");
                Color fontColor = Color.web("#444444");
                Paint hoverColor = param.getColorType().getPaint();
                Border border = new Border(new BorderStroke(
                        Color.TRANSPARENT, borderColor, borderColor, Color.TRANSPARENT,
                        BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                        CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY
                ));

                this.setBorder(border);

                this.hoverProperty().addListener((ob, ov, nv)->{
                    if (nv) {
                        if(selectedStateProperty().get()){
                            label.setTextFill(Color.WHITE);
                            icon.setColor(Color.WHITE);
                        }else{
                            label.setTextFill(hoverColor);
                            icon.setColor(hoverColor);
                        }
                    }else{
                        if(selectedStateProperty().get()){
                            label.setTextFill(Color.WHITE);
                            icon.setColor(Color.WHITE);
                        }else{
                            label.setTextFill(fontColor);
                            icon.setColor(fontColor);
                        }
                    }
                });

                selectedStateProperty().addListener((ob, ov, nv) -> {
                    updateSkin(nv);
                });
            }

            private void updateSkin(boolean selected){
                Color fontColor = Color.web("#444444");
                Paint hoverColor = param.getColorType().getPaint();
                if(selected){
                    setStyle("-fx-background-color: "+hoverColor.toString().replace("0x", "#"));
                    label.setTextFill(Color.WHITE);
                    icon.setColor(Color.WHITE);
                }else{
                    setStyle("-fx-background-color: transparent;");
                    if (isHover()) {
                        label.setTextFill(hoverColor);
                        icon.setColor(hoverColor);
                    }else{
                        label.setTextFill(fontColor);
                        icon.setColor(fontColor);
                    }
                }
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                if(empty){
                    setGraphic(null);
                    setText(null);
                }else{
                    String s = "\\u" + Integer.toHexString(item.charAt(0) | 0x10000).substring(1);
                    label.setText(s);
                    label.setGraphic(icon);
                    setGraphic(label);
                    setText(null);
                }
                super.updateItem(item, empty);
            }
        };

        javaCodes.put("cellFactory", "grid.setCellFactory(new Callback<GridView<String>, GridCell<String>>() {\n" +
                "    @Override\n" +
                "    public GridCell<String> call(GridView<String> param) {\n" +
                "        return new GridCell<>(){\n" +
                "            private XmLabel label ;\n" +
                "            private PseudoClass selected = PseudoClass.getPseudoClass(\"selected\");\n" +
                "            private XmFontIcon icon = new XmFontIcon();\n" +
                "            @Override\n" +
                "            public void init() {\n" +
                "                super.init();\n" +
                "                //重写init方法可以实现一些基础设置\n" +
                "\n" +
                "                label = new XmLabel();\n" +
                "                //lable可能会阻止事件冒泡，需要设置为不响应鼠标事件\n" +
                "                label.setMouseTransparent(true);\n" +
                "\n" +
                "                itemProperty().addListener((ob, ov, nv)->{\n" +
                "                    if(nv!=null){\n" +
                "                        icon.setIcon(nv);\n" +
                "                    }\n" +
                "                });\n" +
                "\n" +
                "                label.setGraphic(icon);\n" +
                "                label.setContentDisplay(ContentDisplay.TOP);\n" +
                "                label.setAlignment(XmAlignment.CENTER);\n" +
                "\n" +
                "                Color borderColor = Color.web(\"#bbbbbb\");\n" +
                "                Color fontColor = Color.web(\"#444444\");\n" +
                "                Paint hoverColor = param.getColorType().getPaint();\n" +
                "                Border border = new Border(new BorderStroke(\n" +
                "                        Color.TRANSPARENT, borderColor, borderColor, Color.TRANSPARENT,\n" +
                "                        BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,\n" +
                "                        CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY\n" +
                "                ));\n" +
                "\n" +
                "                this.setBorder(border);\n" +
                "\n" +
                "                this.hoverProperty().addListener((ob, ov, nv)->{\n" +
                "                    if (nv) {\n" +
                "                        if(selectedStateProperty().get()){\n" +
                "                            label.setTextFill(Color.WHITE);\n" +
                "                            icon.setColor(Color.WHITE);\n" +
                "                        }else{\n" +
                "                            label.setTextFill(hoverColor);\n" +
                "                            icon.setColor(hoverColor);\n" +
                "                        }\n" +
                "                    }else{\n" +
                "                        if(selectedStateProperty().get()){\n" +
                "                            label.setTextFill(Color.WHITE);\n" +
                "                            icon.setColor(Color.WHITE);\n" +
                "                        }else{\n" +
                "                            label.setTextFill(fontColor);\n" +
                "                            icon.setColor(fontColor);\n" +
                "                        }\n" +
                "                    }\n" +
                "                });\n" +
                "\n" +
                "                selectedStateProperty().addListener((ob, ov, nv) -> {\n" +
                "                    updateSkin(nv);\n" +
                "                });\n" +
                "            }\n" +
                "\n" +
                "            private void updateSkin(boolean selected){\n" +
                "                Color fontColor = Color.web(\"#444444\");\n" +
                "                Paint hoverColor = param.getColorType().getPaint();\n" +
                "                if(selected){\n" +
                "                    setStyle(\"-fx-background-color: \"+hoverColor.toString().replace(\"0x\", \"#\"));\n" +
                "                    label.setTextFill(Color.WHITE);\n" +
                "                    icon.setColor(Color.WHITE);\n" +
                "                }else{\n" +
                "                    setStyle(\"-fx-background-color: transparent;\");\n" +
                "                    if (isHover()) {\n" +
                "                        label.setTextFill(hoverColor);\n" +
                "                        icon.setColor(hoverColor);\n" +
                "                    }else{\n" +
                "                        label.setTextFill(fontColor);\n" +
                "                        icon.setColor(fontColor);\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            protected void updateItem(String item, boolean empty) {\n" +
                "                if(empty){\n" +
                "                    setGraphic(null);\n" +
                "                    setText(null);\n" +
                "                }else{\n" +
                "                    String s = \"\\\\u\" + Integer.toHexString(item.charAt(0) | 0x10000).substring(1);\n" +
                "                    label.setText(s);\n" +
                "                    label.setGraphic(icon);\n" +
                "                    setGraphic(label);\n" +
                "                    setText(null);\n" +
                "                }\n" +
                "                super.updateItem(item, empty);\n" +
                "            }\n" +
                "        };\n" +
                "    }\n" +
                "});\n");

        return cell;
    }

    private GridCell<String> buildCheckBoxCell(GridView<String> param) {
        GridCell<String> cell = new XmCheckBoxGridCell<>(){
            private XmFontIcon icon = new XmFontIcon();
            private XmLabel label;
            @Override
            public void init() {
                //lable可能会阻止事件冒泡，需要设置为不响应鼠标事件
                label = new XmLabel();
                label.setMouseTransparent(true);
                label.setGraphic(icon);
                label.setContentDisplay(ContentDisplay.TOP);
                label.setAlignment(XmAlignment.CENTER);
                label.setPrefWidth(grid.getCellWidth());
                label.setPrefHeight(grid.getCellHeight());

                setAlignment(Pos.CENTER);

                checkBoxProperty().addListener((ob, ov, nv)->{
                    nv.setColorType(grid.getColorType());
                    nv.setStyle("-fx-padding: 6 0 0 6;");
                });

                Color borderColor = Color.web("#bbbbbb");
                Border border = new Border(new BorderStroke(
                        Color.TRANSPARENT, borderColor, borderColor, Color.TRANSPARENT,
                        BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                        CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY
                ));
                setBorder(border);

            }

            @Override
            public void updateItem(String item, boolean empty) {
                if(empty){
                    setGraphic(null);
                    setText(null);
                }else{

                    String s = "\\u" + Integer.toHexString(item.charAt(0) | 0x10000).substring(1);
                    icon.setIcon(item);
                    label.setText(s);
                    label.setGraphic(icon);
                    setGraphic(label);
                    setText(null);

                }
                super.updateItem(item, empty);
            }
        };

        javaCodes.put("cellFactory", "grid.setCellFactory(new Callback<GridView<String>, GridCell<String>>() {\n" +
                "    @Override\n" +
                "    public GridCell<String> call(GridView<String> param) {\n" +
                "        return new XmCheckBoxGridCell<>(){\n" +
                "            private XmFontIcon icon = new XmFontIcon();\n" +
                "            private XmLabel label;\n" +
                "            @Override\n" +
                "            public void init() {\n" +
                "                //lable可能会阻止事件冒泡，需要设置为不响应鼠标事件\n" +
                "                label = new XmLabel();\n" +
                "                label.setMouseTransparent(true);\n" +
                "                label.setGraphic(icon);\n" +
                "                label.setContentDisplay(ContentDisplay.TOP);\n" +
                "                label.setAlignment(XmAlignment.CENTER);\n" +
                "                label.setPrefWidth(grid.getCellWidth());\n" +
                "                label.setPrefHeight(grid.getCellHeight());\n" +
                "\n" +
                "                setAlignment(Pos.CENTER);\n" +
                "\n" +
                "                checkBoxProperty().addListener((ob, ov, nv)->{\n" +
                "                    nv.setColorType(grid.getColorType());\n" +
                "                    nv.setStyle(\"-fx-padding: 6 0 0 6;\");\n" +
                "                });\n" +
                "\n" +
                "                Color borderColor = Color.web(\"#bbbbbb\");\n" +
                "                Border border = new Border(new BorderStroke(\n" +
                "                        Color.TRANSPARENT, borderColor, borderColor, Color.TRANSPARENT,\n" +
                "                        BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,\n" +
                "                        CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY\n" +
                "                ));\n" +
                "                setBorder(border);\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void updateItem(String item, boolean empty) {\n" +
                "                if(empty){\n" +
                "                    setGraphic(null);\n" +
                "                    setText(null);\n" +
                "                }else{\n" +
                "\n" +
                "                    String s = \"\\\\u\" + Integer.toHexString(item.charAt(0) | 0x10000).substring(1);\n" +
                "                    icon.setIcon(item);\n" +
                "                    label.setText(s);\n" +
                "                    label.setGraphic(icon);\n" +
                "                    setGraphic(label);\n" +
                "                    setText(null);\n" +
                "\n" +
                "                }\n" +
                "                super.updateItem(item, empty);\n" +
                "            }\n" +
                "        };\n" +
                "    }\n" +
                "});");

        return cell;
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
                    grid.setColorType(ColorType.other(colorField.getText()));
                    setCellFactory();
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    grid.setColorType(item);
                    setCellFactory();

                    javaCodes.put("colorType","grid.setColorType(ColorType.get(\""+item+"\"));\r\n");
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
                grid.setColorType(ColorType.other(colorField.getText().trim()));
                setCellFactory();
                javaCodes.put("setMyColor", "grid.setColorType(ColorType.other(\""+ colorField.getText()+"\"));");
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

            setCellFactory();

            this.javaCodes.put("hueType", "listView.setHueType(HueType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-hue", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

}
