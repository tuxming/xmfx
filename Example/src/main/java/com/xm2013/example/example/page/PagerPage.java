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

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.pager.XmPager;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.IndexedCell;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class PagerPage extends BasePage{

    private final XmPager pager;

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    public PagerPage(){
        this.setTitle("分页器（XmPager）", new XmFontIcon("\ue6fc"));
        this.setComponentTitle("属性");

        pager = new XmPager(13420);
        pager.setCurrIndex(44);
        pager.setSingleLine(false);
        pager.getStyleClass().add("my-pager");
        setShowComponent(pager);

        setColor();
        setSize();
        setHue();

        XmLabel label1 = new XmLabel("单行模式：");
        XmCheckBox singleLineModel = new XmCheckBox();
        singleLineModel.setSizeType(SizeType.SMALL);
        HBox box1 = new HBox();
        box1.setAlignment(Pos.BASELINE_LEFT);
        box1.getChildren().addAll(label1, singleLineModel);

        singleLineModel.selectedProperty().addListener((ob, ov, nv)->{
            pager.setSingleLine(nv);
            javaCodes.put("singleLine", "pager.setSingleLine("+nv+"));\r\n");
            cssCodes.put("-fx-single-line", nv+";");
        });

        XmLabel label2 = new XmLabel("快速跳转：");
        XmCheckBox showJumperCb = new XmCheckBox();
        showJumperCb.setSizeType(SizeType.SMALL);
        showJumperCb.setSelected(true);
        HBox box2 = new HBox();
        box2.setAlignment(Pos.BASELINE_LEFT);
        box2.getChildren().addAll(label2, showJumperCb);

        showJumperCb.selectedProperty().addListener((ob, ov, nv)->{
            pager.setShowQuickJumper(nv);
            javaCodes.put("ShowQuickJumper", "pager.setShowQuickJumper("+nv+"));\n");
            cssCodes.put("-fx-show-quick-jumper", nv+";");
        });

        addActionComponent(box1);
        addActionComponent(box2);

        String javaCodePrev = "pager = new XmPager(13420);\n" +
                "pager.setCurrIndex(44);\n" +
                "pager.getStyleClass().add(\"my-pager\");\r\n";
        getJavaText().setText(javaCodePrev);
        javaCodes.addListener(new MapChangeListener<String, String>() {
            @Override
            public void onChanged(Change<? extends String, ? extends String> change) {
                String text =javaCodePrev;

                for(String key: javaCodes.keySet()){
                    text += javaCodes.get(key);
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
            pager.getStylesheets().remove(css);
        });

        getRunBtn().setOnAction(e -> {
            pager.getStylesheets().remove(css);
            css = ".my-pager{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            pager.getStylesheets().add(css);
        });

    }

    /**
     * 设置颜色类型
     */
    private void setColor() {

        XmSelector<ColorType> colorSelector = new XmSelector<ColorType>();
        colorSelector.setSizeType(SizeType.SMALL);
        colorSelector.setPrefWidth(150);
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
                    pager.setColorType(ColorType.other(colorField.getText()));
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);

                    pager.setColorType(item);

                    javaCodes.put("colorType","pager.setColorType(ColorType.get(\""+item+"\"));\r\n");
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

        colorField.textProperty().addListener((ob, ov, nv) -> {
            pager.setColorType(ColorType.other(colorField.getText().trim()));
            javaCodes.put("setMyColor", "pager.setColorType(ColorType.other(\""+ colorField.getText()+"\"));");
            cssCodes.put("-fx-type-color:", colorField.getText().trim()+";");
        });

        this.addActionComponent(colorField);
    }

    /**
     * 设置色调类型
     */
    private void setHue() {
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
            pager.setHueType(nv.getValue());
            this.javaCodes.put("hueType", "pager.setHueType(HueType."+nv.getValue()+");");
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

        XmToggleGroup<SizeType> tg = new XmToggleGroup<>();
        smallCb.setToggleGroup(tg);
        mediumCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, smallCb, mediumCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            pager.setSizeType(nv.getValue());
            this.javaCodes.put("sizeType", "pager.setSizeType(SizeType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-size", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }



}
