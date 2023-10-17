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
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.dropdown.TriggerType;
import com.xm2013.jfx.control.dropdown.DropdownMenu;
import com.xm2013.jfx.control.dropdown.DropdownMenuItem;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DropdownMenuPage extends BasePage {

    private XmTextField labelField;
    private DropdownMenu dropdownMenu;

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    public DropdownMenuPage(){
        this.setTitle("下拉菜单（XmDropdownMenu）", new XmFontIcon("\ue61c"));
        this.setComponentTitle("属性");
        this.buildShowComp();

        //设置显示文本
        buildShowText();

        //设置是否使用分组显示
        buildUseGroup();

        //设置触发方式
        buildTrigerType();

        //设置动画类型
        buildAnimateType();

        //设置颜色
        buildColorType();

        //设置尺寸
        buildSizeType();

        //设置色调类型
        buildHueType();

        //设置圆角类型
        buildRoundType();

        //设置边框类型
        buildBorderType();

        //生成代码显示在文本框中
        buildCodes();

    }

    /**
     * 设置边框类型
     */
    private void buildBorderType() {

        XmLabel label = new XmLabel("边框类型：");
        XmCheckBox<String> cb1 = new XmCheckBox<>(BorderType.NONE.name());
        cb1.setSizeType(SizeType.SMALL);
        cb1.setRadioButton(true);
        cb1.setSelectedHightLight(true);

        XmCheckBox<String> cb2 = new XmCheckBox<>(BorderType.FULL.name());
        cb2.setSizeType(SizeType.SMALL);
        cb2.setRadioButton(true);
        cb2.setSelectedHightLight(true);
        cb2.setMargin(new Insets(0,0,0,10));
        cb2.setSelected(true);

        XmToggleGroup<String> tg = new XmToggleGroup<>();
        cb1.setToggleGroup(tg);
        cb2.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, cb1, cb2);

        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            if(nv!=null){
                dropdownMenu.setBorderType(BorderType.valueOf(nv.getText()));
                javaCodes.put("borderType",  "dropdownMenu.setBorderType(BorderType."+nv.getText()+"));");
                cssCodes.put("-fx-type-border", nv.getText().toLowerCase());
            }
        });


        this.addActionComponent(box);

    }

    /**
     * 生成代码显示在文本框中
     */
    private void buildCodes() {

        String javaCodePrev = "XmDropdownMenuItem group1 = new XmDropdownMenuItem(\"分组一\", new XmFontIcon(\"\\ue65c\"));\n" +
                "XmDropdownMenuItem group11 = new XmDropdownMenuItem(\"子节点1-3\");\n" +
                "\n" +
                "group11.getChildren().addAll(\n" +
                "       new MyDMenu(\"子节点1-3-1\", Color.DIMGRAY, Color.CHOCOLATE),,\n" +
                "       new XmDropdownMenuItem(\"子节点1-3-2\", true),\n" +
                "       new XmDropdownMenuItem(\"子节点1-3-3\")\n" +
                "   );\n" +
                "\n" +
                " group1.getChildren().addAll(\n" +
                "       new XmDropdownMenuItem(\"子节点1-1\"),\n" +
                "       new XmDropdownMenuItem(\"子节点1-2\"),\n" +
                "       group11,\n" +
                "       new XmDropdownMenuItem(\"子节点1-4\")\n" +
                "   );\n" +
                "\n" +
                "XmDropdownMenuItem group2 = new XmDropdownMenuItem(\"分组二\", new XmFontIcon(\"\\ue65c\"));\n" +
                "group2.getChildren().addAll(\n" +
                "       new XmDropdownMenuItem(\"子节点2-1\"),\n" +
                "       new XmDropdownMenuItem(\"子节点2-2\"),\n" +
                "       new XmDropdownMenuItem(\"子节点2-3\"),\n" +
                "       new XmDropdownMenuItem(\"子节点2-4\")\n" +
                "   );\n" +
                "\n" +
                "XmDropdownMenu dropdownMenu = new XmDropdownMenu(\"11111aa\");\n" +
                "dropdownMenu.getStyleClass().add(\"my-dmenu\");\n" +
                "dropdownMenu.addItems(group1, group2);\n";

        getJavaText().setText(javaCodePrev);
        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text =javaCodePrev;

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
                    text += key+" : "+cssCodes.get(key)+";\r\n";
                }

                getCssText().setText(text);
            }
        });

        getClearBtn().setOnAction(e -> {
            dropdownMenu.getStylesheets().remove(css);
        });

        getRunBtn().setOnAction(e -> {
            dropdownMenu.getStylesheets().remove(css);
            css = ".my-dmenu{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            dropdownMenu.getStylesheets().add(css);
        });

    }

    private void buildRoundType(){
        XmLabel label = new XmLabel("圆角类型：");
        XmCheckBox<String> cb1 = new XmCheckBox<>(RoundType.NONE.name());
        cb1.setSizeType(SizeType.SMALL);
        cb1.setRadioButton(true);
        cb1.setSelectedHightLight(true);
        cb1.setSelected(true);

        XmCheckBox<String> cb2 = new XmCheckBox<>(RoundType.SMALL.name());
        cb2.setSizeType(SizeType.SMALL);
        cb2.setRadioButton(true);
        cb2.setSelectedHightLight(true);
        cb2.setMargin(new Insets(0,0,0,10));

        XmCheckBox<String> cb3 = new XmCheckBox<>(RoundType.SEMICIRCLE.name());
        cb3.setSizeType(SizeType.SMALL);
        cb3.setRadioButton(true);
        cb3.setSelectedHightLight(true);
        cb3.setMargin(new Insets(0,0,0,10));

        XmToggleGroup<String> tg = new XmToggleGroup<>();
        cb1.setToggleGroup(tg);
        cb2.setToggleGroup(tg);
        cb3.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, cb1, cb2, cb3);

        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            if(nv!=null){
                dropdownMenu.setRoundType(RoundType.valueOf(nv.getText()));
                javaCodes.put("roundType",  "dropdownMenu.setRoundType(RoundType."+nv.getText()+"));");
                cssCodes.put("-fx-type-round", nv.getText().toLowerCase());
            }
        });

        this.addActionComponent(box);
    }

    /**
     * 设置色调类型
     */
    private void buildHueType() {
        XmLabel label = new XmLabel("明暗类型：");
        XmCheckBox<String> cb1 = new XmCheckBox<>(HueType.LIGHT.name());
        cb1.setSizeType(SizeType.SMALL);
        cb1.setRadioButton(true);
        cb1.setSelectedHightLight(true);

        XmCheckBox<String> cb2 = new XmCheckBox<>(HueType.DARK.name());
        cb2.setSizeType(SizeType.SMALL);
        cb2.setRadioButton(true);
        cb2.setSelectedHightLight(true);
        cb2.setMargin(new Insets(0,0,0,10));
        cb2.setSelected(true);

        XmCheckBox<String> cb3 = new XmCheckBox<>(HueType.NONE.name());
        cb3.setSizeType(SizeType.SMALL);
        cb3.setRadioButton(true);
        cb3.setSelectedHightLight(true);
        cb3.setMargin(new Insets(0,0,0,10));

        XmToggleGroup<String> tg = new XmToggleGroup<>();
        cb1.setToggleGroup(tg);
        cb2.setToggleGroup(tg);
        cb3.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, cb1, cb2, cb3);

        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            if(nv!=null){
                dropdownMenu.setHueType(HueType.valueOf(nv.getText()));
                javaCodes.put("hueType",  "dropdownMenu.setHueType(HueType."+nv.getText()+"));");
                cssCodes.put("-fx-type-hue", nv.getText().toLowerCase());
            }
        });

        this.addActionComponent(box);
    }

    /**
     * 设置尺寸
     */
    private void buildSizeType() {

        XmLabel label = new XmLabel("尺寸：");
        XmCheckBox<String> cb1 = new XmCheckBox<>("SMALL");
        cb1.setSizeType(SizeType.SMALL);
        cb1.setRadioButton(true);
        cb1.setSelectedHightLight(true);

        XmCheckBox<String> cb2 = new XmCheckBox<>("MEDIUM");
        cb2.setSizeType(SizeType.SMALL);
        cb2.setRadioButton(true);
        cb2.setSelectedHightLight(true);
        cb2.setMargin(new Insets(0,0,0,10));
        cb2.setSelected(true);

        XmCheckBox<String> cb3 = new XmCheckBox<>("LARGE");
        cb3.setSizeType(SizeType.SMALL);
        cb3.setRadioButton(true);
        cb3.setSelectedHightLight(true);
        cb3.setMargin(new Insets(0,0,0,10));

        XmToggleGroup<String> tg = new XmToggleGroup<>();
        cb1.setToggleGroup(tg);
        cb2.setToggleGroup(tg);
        cb3.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, cb1, cb2, cb3);

        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            if(nv!=null){
                dropdownMenu.setSizeType(SizeType.valueOf(nv.getText()));
                javaCodes.put("sizeType",  "dropdownMenu.setSizeType(SizeType."+nv.getText()+"));");
                cssCodes.put("-fx-type-size", nv.getText().toLowerCase());
            }
        });

        this.addActionComponent(box);

    }

    private void buildColorType() {

        XmSelector<ColorType> colorSelector = new XmSelector<ColorType>();
        colorSelector.setSizeType(SizeType.SMALL);
        colorSelector.setPrefWidth(250);
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
                    dropdownMenu.setColorType(ColorType.other(colorField.getText()));
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    dropdownMenu.setColorType(item);

                    javaCodes.put("colorType","dropdownMenu.setColorType(ColorType.get(\""+item+"\"));\r\n");
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
//        colorField.setLabelWidth(115);
        colorField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        colorField.setSizeType(SizeType.SMALL);

        colorField.textProperty().addListener((ob, ov, nv) -> {
            dropdownMenu.setColorType(ColorType.other(colorField.getText().trim()));
            javaCodes.put("setMyColor", "dropdownMenu.setColorType(ColorType.other(\""+colorField.getText()+"\"));");
            cssCodes.put("-fx-type-color:", colorField.getText().trim()+";");
        });

        this.addActionComponent(colorField);
    }
    /**
     * 设置动画类型
     */
    private void buildAnimateType() {
        XmLabel label = new XmLabel("动画类型：");
        XmCheckBox<String> cb1 = new XmCheckBox<>("RIPPER");
        cb1.setSizeType(SizeType.SMALL);
        cb1.setRadioButton(true);
        cb1.setSelected(true);
        cb1.setSelectedHightLight(true);

        XmCheckBox<String> cb2 = new XmCheckBox<>("SHADOW");
        cb2.setSizeType(SizeType.SMALL);
        cb2.setRadioButton(true);
        cb2.setSelectedHightLight(true);
        cb2.setMargin(new Insets(0,0,0,10));

        XmToggleGroup<String> tg = new XmToggleGroup<>();
        cb1.setToggleGroup(tg);
        cb2.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, cb1, cb2);

        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            if(nv!=null){
                dropdownMenu.setClickAnimateType(ClickAnimateType.valueOf(nv.getText()));
                javaCodes.put("triggerType",  "dropdownMenu.setClickAnimateType(ClickAnimateType."+nv.getText()+"));");
                cssCodes.put("-fx-type-click-animate", nv.getText().toLowerCase());
            }
        });

        this.addActionComponent(box);
    }

    /**
     * 设置触发方式
     */
    private void buildTrigerType() {

        XmLabel label = new XmLabel("触发方式：");
        XmCheckBox<String> hoverCb = new XmCheckBox<>("HOVER");
        hoverCb.setSizeType(SizeType.SMALL);
        hoverCb.setRadioButton(true);
        hoverCb.setSelected(true);
        hoverCb.setSelectedHightLight(true);

        XmCheckBox<String> clickCb = new XmCheckBox<>("CLICK");
        clickCb.setSizeType(SizeType.SMALL);
        clickCb.setRadioButton(true);
        clickCb.setSelectedHightLight(true);
        clickCb.setMargin(new Insets(0,0,0,10));

        XmToggleGroup<String> tg = new XmToggleGroup<>();
        hoverCb.setToggleGroup(tg);
        clickCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, hoverCb, clickCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv)->{
            if(nv!=null){
                dropdownMenu.setTriggerType(TriggerType.valueOf(nv.getText()));
                javaCodes.put("triggerType",  "dropdownMenu.setTriggerType(TriggerType."+nv.getText()+"));");
                cssCodes.put("-fx-type-trigger", nv.getText().toLowerCase());
            }
        });

        this.addActionComponent(box);

    }

    /**
     * 设置是否使用分组显示
     */
    private void buildUseGroup() {

        XmLabel label1 = new XmLabel("是否分组显示：");
        XmCheckBox useGroup = new XmCheckBox();
        useGroup.setSizeType(SizeType.SMALL);
        useGroup.setSelected(true);
        useGroup.setSelectedHightLight(true);
        HBox box1 = new HBox();
        box1.setAlignment(Pos.BASELINE_LEFT);
        box1.getChildren().addAll(label1, useGroup);

        useGroup.selectedProperty().addListener((ob, ov, nv)->{
            dropdownMenu.setUseGroup(nv);
            javaCodes.put("useGroup", "dropdownMenu.setUseGroup("+nv+");");
            cssCodes.put("-fx-use-group", "true");
        });

        this.addActionComponent(box1);
    }


    /**
     * 设置显示文本
     */
    private void buildShowText(){
        labelField = new XmTextField("中国城市");
        labelField.setLabel("显示内容：");
//        labelField.setLabelWidth(115);
        labelField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        labelField.setSizeType(SizeType.SMALL);

        labelField.textProperty().addListener((ob, ov, nv) -> {
            dropdownMenu.setText(nv);
            javaCodes.put("setText", "dropdownMenu.setText(\""+nv+"\");");
        });

        this.addActionComponent(labelField);
    }

    private void buildShowComp() {

        DropdownMenuItem group1 = new DropdownMenuItem("分组一", new XmFontIcon("\ue65c"));
        DropdownMenuItem group11 = new DropdownMenuItem("子节点1-3");

        group11.getChildren().addAll(
                new MyDMenu("子节点1-3-1", Color.DIMGRAY, Color.CHOCOLATE),
                new DropdownMenuItem("子节点1-3-2", true),
                new DropdownMenuItem("子节点1-3-3")
        );

        group1.getChildren().addAll(
                new DropdownMenuItem("子节点1-1"),
                new DropdownMenuItem("子节点1-2"),
                group11,
                new DropdownMenuItem("子节点1-4")
        );

        DropdownMenuItem group2 = new DropdownMenuItem("分组二", new XmFontIcon("\ue65c"));
        group2.getChildren().addAll(
                new DropdownMenuItem("子节点2-1"),
                new DropdownMenuItem("子节点2-2"),
                new DropdownMenuItem("子节点2-3"),
                new DropdownMenuItem("子节点2-4")
        );

        dropdownMenu = new DropdownMenu("11111aa");
        dropdownMenu.getStyleClass().add("my-dmenu");
        dropdownMenu.addItems(group1, group2);
        this.setShowComponent(dropdownMenu);
    }


}
