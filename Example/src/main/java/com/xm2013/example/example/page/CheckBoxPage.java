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

import com.xm2013.example.example.Menu;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggle;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Predicate;

public class CheckBoxPage extends BasePage{

    private XmSelector<ColorType> colorTypes = null;
    private ComboBox<RoundType> roundTypes = null;
    private ComboBox<SizeType> sizeTypes = null;

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;
    private Map<String, XmCheckBox> checkBoxList = new LinkedHashMap<>();
    private String javaCodePrev;
    private String javaCodePrev1;

    public CheckBoxPage(){

        this.setTitle("单选/复选（XmCheckBox）", new XmFontIcon("\ue6da"));

        this.setComponentTitle("单选");

        XmButton simpleBtn = new XmButton("简单用法");
        simpleBtn.setSizeType(SizeType.SMALL);

        XmButton compBtn = new XmButton("RadioButton的用法");
        compBtn.setSizeType(SizeType.SMALL);

        HBox hbox = new HBox(simpleBtn, compBtn);
        VBox box = new VBox(hbox);

        this.addActionComponent(box);

        simpleBtn.setOnAction(e -> {

            VBox pane = this.getComponentSettingPane();
            pane.getChildren().removeIf(new Predicate<Node>() {
                @Override
                public boolean test(Node node) {
                    return node != box;
                }
            });
            normal();

        });

        compBtn.setOnAction(e -> {

            VBox pane = this.getComponentSettingPane();
            pane.getChildren().removeIf(new Predicate<Node>() {
                @Override
                public boolean test(Node node) {
                    return node != box;
                }
            });
            composite();

        });

        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text = javaCodePrev1;

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
                    text += key+": "+cssCodes.get(key)+"\r\n";
                }

                getCssText().setText(text);
            }
        });

        getClearBtn().setOnAction(e -> {
            for (XmCheckBox xcb : checkBoxList.values()) {
                xcb.getStylesheets().remove(css);
            }
        });

        getRunBtn().setOnAction(e -> {
            for (XmCheckBox xcb : checkBoxList.values()) {
                xcb.getStylesheets().remove(css);
            }
            css = ".my-checkbox{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            for (XmCheckBox xcb : checkBoxList.values()) {
                xcb.getStylesheets().add(css);
            }
        });


    }

    private void normal(){
        checkBoxList.clear();
        XmCheckBox<Menu> checkbox = new XmCheckBox<Menu>();
        checkbox.setConverter(new XmStringConverter<Menu>() {
            @Override
            public String toString(Menu menu) {
                return menu.getLabel();
            }
        });
        checkbox.setValue(new Menu("a", "这是label", 1));
        checkbox.getStyleClass().add("my-checkbox");

        this.setShowComponent(checkbox);
        checkBoxList.put("checkbox",checkbox);

        setColor();
        setRoundTypes();
        setSizeTypes();

//        bo.setConverter();
        XmLabel label1 = new XmLabel("是否启用第三状态：");
        XmCheckBox indeterminateStatus = new XmCheckBox();
        indeterminateStatus.setSizeType(SizeType.SMALL);
        HBox box1 = new HBox();
        box1.setAlignment(Pos.BASELINE_LEFT);
        box1.getChildren().addAll(label1, indeterminateStatus);

        this.addActionComponent(box1);

        indeterminateStatus.selectedProperty().addListener((ob, ov, nv) ->{
            checkbox.setAllowIndeterminate(nv);
            this.javaCodes.put("allowIndeterminate", "checkbox.setAllowIndeterminate("+nv+");");
        });

        javaCodePrev1 = "//简单使用" +
                "XmCheckBox checkbox = new XmCheckBox();\n" +
                "checkbox.setText(\"复选框测试\");\r\n" +
                "\r\n" +
                "//泛型使用\r\n" +
                "XmCheckBox<Menu> checkbox = new XmCheckBox<Menu>();\n" +
                "checkbox.setConverter(new XmStringConverter<Menu>() {\n" +
                "   @Override\n" +
                "   public String toString(Menu menu) {\n" +
                "       return menu.getLabel();\n" +
                "   }\n" +
                "});\n" +
                "checkbox.setValue(new Menu(\"a\", \"这是label\", 1));\r\n\r\n";


    }


    /**
     * 这个展示结合XmToggleGroup使用，模拟radio，以及泛型的综合使用
     */
    private void composite(){
        //定义一个显示文本的转换器
        XmStringConverter<Menu> converter = new XmStringConverter<Menu>() {
            @Override
            public String toString(Menu object) {
                return object.getLabel();
            }
        };

        checkBoxList.clear();

        //使用泛型
        XmCheckBox<Menu> cb = new XmCheckBox<Menu>();
        //设置转换器
        cb.setConverter(converter);
        cb.setValue(new Menu("1", "女", 1));

        XmCheckBox<Menu> cb1 = new XmCheckBox<Menu>();
        cb1.setConverter(converter);
//        cb1.setRadioButton(true);
        cb1.setValue(new Menu("1", "男", 2));

        XmCheckBox<Menu> cb2 = new XmCheckBox<Menu>();
        cb2.setConverter(converter);
//        cb2.setRadioButton(true);
        cb2.setValue(new Menu("1", "保密", 3));

        HBox showBox = new HBox(cb, cb1, cb2);
        showBox.setSpacing(10);

        XmToggleGroup<Menu> tg = new XmToggleGroup<>();
        cb.setToggleGroup(tg);
        cb1.setToggleGroup(tg);
        cb2.setToggleGroup(tg);
        
        cb.getStyleClass().add("my-checkbox");
        cb1.getStyleClass().add("my-checkbox");
        cb2.getStyleClass().add("my-checkbox");

        XmButton getValueButton = new XmButton("获取选中值");
        getValueButton.setSizeType(SizeType.SMALL);
        getValueButton.setColorType(ColorType.success());
        getValueButton.setOnAction(e -> {
            String value = null;
            XmToggle<Menu> menu = tg.getSelectedToggle();
            if(menu != null){
                value = menu.getValue().getLabel();
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("获取到的值");
            alert.setHeaderText(value);
//            alert.setContentText(value);
            alert.showAndWait();
        });

        checkBoxList.put("cb", cb);
        checkBoxList.put("cb1", cb1);
        checkBoxList.put("cb2", cb2);
        this.setShowComponent(showBox);

        setColor();
        setRoundTypes();
        setSizeTypes();

        XmLabel label1 = new XmLabel("RadioButton模式：");
        XmCheckBox radioButtonModel = new XmCheckBox();
        radioButtonModel.setSizeType(SizeType.SMALL);
        HBox box1 = new HBox();
        box1.setAlignment(Pos.BASELINE_LEFT);
        box1.getChildren().addAll(label1, radioButtonModel);

        XmLabel label2 = new XmLabel("启用选中高亮：");
        XmCheckBox highLights = new XmCheckBox();
        highLights.setSizeType(SizeType.SMALL);
        HBox box2 = new HBox();
        box2.setAlignment(Pos.BASELINE_LEFT);
        box2.getChildren().addAll(label2, highLights);

        this.addActionComponent(box1);
        this.addActionComponent(box2);
        this.addActionComponent(getValueButton);

        radioButtonModel.selectedProperty().addListener((ob, ov, nv) ->{
            cb.setRadioButton(nv);
            cb1.setRadioButton(nv);
            cb2.setRadioButton(nv);
            this.javaCodes.put("radioButtonModel",
                    "//设置为RadioButton模式, 这个是确保外观是RadioButton样式, 并且不能启用indeterminate属性，\r\n//如果自己能够确保不会出问题，可以不设置这个属性\r\n"+
                    "cb.setRadioButton("+nv+");\r\ncb1.setRadioButton("+nv+");\r\ncb2.setRadioButton("+nv+");\r\n");
        });

        highLights.selectedProperty().addListener((ob, ov, nv) ->{
            cb.setSelectedHightLight(nv);
            cb1.setSelectedHightLight(nv);
            cb2.setSelectedHightLight(nv);
            this.javaCodes.put("highLights", "cb.setSelectedHightLight("+nv+");\r\ncb1.setSelectedHightLight("+nv+");\r\ncb2.setSelectedHightLight("+nv+");\r\n");
        });

        javaCodePrev = "//RadioButton的用法\r\n" +
                "//定义一个显示文本的转换器\n" +
                "XmStringConverter<Menu> converter = new XmStringConverter<Menu>() {\n" +
                "   @Override\n" +
                "   public String toString(Menu object) {\n" +
                "       return object.getLabel();\n" +
                "   }\n" +
                "};\n" +
                "\n" +
                "//使用泛型\n" +
                "XmCheckBox<Menu> cb = new XmCheckBox<Menu>();\n" +
                "//设置转换器\n" +
                "cb.setConverter(converter);\n" +
                "cb.setValue(new Menu(\"1\", \"女\", 1));\n" +
                "\n" +
                "XmCheckBox<Menu> cb1 = new XmCheckBox<Menu>();\n" +
                "cb1.setConverter(converter);\n" +
                "cb1.setValue(new Menu(\"1\", \"男\", 2));\n" +
                "\n" +
                "XmCheckBox<Menu> cb2 = new XmCheckBox<Menu>();\n" +
                "cb2.setConverter(converter);\n" +
                "cb2.setValue(new Menu(\"1\", \"保密\", 3));\n" +
                "\n" +
                "HBox showBox = new HBox(cb, cb1, cb2);\n" +
                "showBox.setSpacing(10);\n" +
                "\n" +
                "XmToggleGroup<Menu> tg = new XmToggleGroup<>();\n" +
                "cb.setToggleGroup(tg);\n" +
                "cb1.setToggleGroup(tg);\n" +
                "cb2.setToggleGroup(tg);\r\n" +
                "cb.getStyleClass().add(\"my-checkbox\");\n" +
                "cb1.getStyleClass().add(\"my-checkbox\");\n" +
                "cb2.getStyleClass().add(\"my-checkbox\");\r\n";
    }

    /**
     * 设置颜色类型
     */
    private void setColor() {

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
                    for (String name : checkBoxList.keySet()) {
                        checkBoxList.get(name).setColorType(ColorType.other(colorField.getText()));
                    }
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    String code = "";
                    for (String name : checkBoxList.keySet()) {
                        checkBoxList.get(name).setColorType(item);
                        code += name+".setColorType(ColorType.get(\""+item+"\"))\r\n";
                    }

                    javaCodes.put("colorType",code);
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
                String code = "";
                String colorText = colorField.getText().trim();
                for (String name : checkBoxList.keySet()) {
                    checkBoxList.get(name).setColorType(ColorType.other(colorText));
                    code += name+".setColorType(ColorType.get(\""+colorText+"\"))\r\n";
                }

                javaCodes.put("colorType",code);
                cssCodes.put("-fx-type-color",colorText.toLowerCase());
            }
        });

        this.addActionComponent(colorField);
    }


    private void setRoundTypes() {
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


            String code = "";
            for (String name : checkBoxList.keySet()) {
                checkBoxList.get(name).setRoundType(nv.getValue());
                code += name+".setRoundType(RoundType."+nv.getValue()+"))\r\n";
            }

            javaCodes.put("roundType",code);
            this.cssCodes.put(CssKeys.PropTypeRound, nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }


    private void setSizeTypes() {
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
            String code = "";
            for (String name : checkBoxList.keySet()) {
                checkBoxList.get(name).setSizeType(nv.getValue());
                code += name+".setRoundType(setSizeType."+nv.getValue()+"))\r\n";
            }

            javaCodes.put("sizeType",code);
            this.cssCodes.put(CssKeys.PropTypeSize, nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }
}
