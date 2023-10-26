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

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.RoundType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.date.DateType;
import com.xm2013.jfx.control.date.XmDateRangeSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DateRangeSelectorPage extends BasePage {

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private XmTextField colorField;
    private String defaultJavaCode = "";
    private XmDateRangeSelector selector;
    private String css;

    public DateRangeSelectorPage(){

        this.setTitle("日期范围选择框（XmDateRangeSelector）", new XmFontIcon("\ue65e"));
        this.setComponentTitle("属性");

        dateSelector();

        defaultJavaCode += "XmDateRangeSelector selector = new XmDateRangeSelector();\n" +
                "XmDateRangeSelector.getStyleClass().add(\"my-date-range-selector\");\r\n" +
                "selector.setFormatPattern(\""+FxKit.DATETIME_PATTERN+"\");\r\n";

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
            css = ".my-date-range-selector{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            selector.getStylesheets().add(css);
        });

    }

    private void dateSelector() {

        XmDateRangeSelector selector = new XmDateRangeSelector();
        selector.setFormatPattern(FxKit.DATETIME_PATTERN);
        selector.getStyleClass().add("my-date-selector");

        this.setShowComponent(selector);
        this.setSettings(selector);
        this.selector = selector;

    }

    private void setSettings(XmDateRangeSelector selector) {
        //设置类型
        setDateType(selector);
        //设置颜色类型
        setColor(selector);
        //设置尺寸
        setSize(selector);
        //设置圆角类型
        setRound(selector);
        //设置色调类型
        setHue(selector);
        //设置是否填充图标按钮
        setFillArrow(selector);
    }

    /**
     * 设置选择类型
     * @param selector
     */
    private void setDateType(XmDateRangeSelector selector) {
        XmLabel label = new XmLabel("选择框类型：");

        XmCheckBox<DateType> dateTimeCb = new XmCheckBox<DateType>();
        dateTimeCb.setValue(DateType.DATETIME);
        dateTimeCb.setText("DATETIME");
        dateTimeCb.setSizeType(SizeType.SMALL);
        dateTimeCb.setSelected(true);

        XmCheckBox<DateType> dateCb = new XmCheckBox<DateType>();
        dateCb.setValue(DateType.DATE);
        dateCb.setText("DATE");
        dateCb.setSizeType(SizeType.SMALL);

        XmCheckBox<DateType> yearCb = new XmCheckBox<DateType>();
        yearCb.setValue(DateType.YEAR);
        yearCb.setText("YEAR");
        yearCb.setSizeType(SizeType.SMALL);

        XmCheckBox<DateType> monthCb = new XmCheckBox<DateType>();
        monthCb.setValue(DateType.MONTH);
        monthCb.setText("LARGE");
        monthCb.setSizeType(SizeType.SMALL);

        XmCheckBox<DateType> timeCb = new XmCheckBox<DateType>();
        timeCb.setValue(DateType.TIME);
        timeCb.setText("TIME");
        timeCb.setSizeType(SizeType.SMALL);

        XmToggleGroup<DateType> tg = new XmToggleGroup<>();
        dateTimeCb.setToggleGroup(tg);
        dateCb.setToggleGroup(tg);
        timeCb.setToggleGroup(tg);
        monthCb.setToggleGroup(tg);
        yearCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, dateTimeCb, dateCb, yearCb, monthCb, timeCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{

            DateType dt = nv.getValue();
            selector.setDateType(dt);

            String pattern = null;
            if(dt.equals(DateType.DATE)){
                pattern = FxKit.DATE_PATTERN;
            }else if(dt.equals(DateType.YEAR)){
                pattern = FxKit.YEAR_PATTERN;
            }else if(dt.equals(DateType.MONTH)){
                pattern = FxKit.MONTH_PATTERN;
            }else if(dt.equals(DateType.TIME)){
                pattern = FxKit.TIME_PATTERN;
            }else{
                pattern = FxKit.DATETIME_PATTERN;
            }

            selector.setFormatPattern(pattern);
//            System.out.println(dt+", "+pattern);

            this.javaCodes.put("dateType", "selector.setDateType(DateType."+nv.getValue()+");");
            this.javaCodes.put("datePattern", "selector.setFormatPattern(\""+pattern+"\");");
            this.cssCodes.put("-fx-type-date", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置颜色类型
     * @param selector
     */
    private void setColor(XmDateRangeSelector selector) {

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
    private void setMyDefineColor(XmDateRangeSelector selector){
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
    private void setSize(XmDateRangeSelector selector) {

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
    private void setRound(XmDateRangeSelector selector) {

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
    private void setHue(XmDateRangeSelector selector) {
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
    private void setFillArrow(XmDateRangeSelector selector) {
        XmLabel label = new XmLabel("下拉箭头是否填充：");
        XmCheckBox checkbox = new XmCheckBox();
        checkbox.setSizeType(SizeType.SMALL);
        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.getChildren().addAll(label, checkbox);

        checkbox.selectedProperty().addListener((ob, ov, nv) ->{
            selector.setFillIcon(nv);
            this.javaCodes.put("fillIcon", "selector.setFillIcon("+nv+");");
            this.cssCodes.put("-fx-fill-icon", nv+";");
        });

//        box.visibleProperty().bind(selector.multipleProperty().not());
//        box.managedProperty().bind(selector.multipleProperty().not());

        this.addActionComponent(box);
    }

}
