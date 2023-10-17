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

import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.textfield.XmTextInputType;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class TextFieldPage extends BasePage {
    private final HBox showPasswordBox;
    private XmSelector<String> prefixs = null;
    private XmSelector<String> suffixs = null;
    private XmTextField labelField = null;
    private XmTextField echocharField = null;
    private XmSelector<XmAlignment> alignments = null;
    private XmSelector<XmFieldDisplayType> displays = null;
    private XmSelector<ColorType> colorTypes = null;
    private XmToggleGroup<RoundType> roundTypes = null;
    private XmToggleGroup<SizeType> sizeTypes = null;
    private XmToggleGroup<BorderType> borderTypes = null;
    private XmToggleGroup<XmTextInputType> inputTypes = null;
    private XmCheckBox<Boolean> cleanables = null;
    private XmCheckBox<Boolean> showPasswords = null;
    private XmTextField field;
    double percent = 0.0;

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField colorField;

    public TextFieldPage() {

        this.setTitle("文本框（XmTextField）", new XmFontIcon("\ue77c"));
        this.setComponentTitle("自定义文本框");

        this.getComponentShowPane().setStyle("-fx-padding: 0px 0px 0px 50px;");

        labelField = new XmTextField("测试标签：");
        labelField.setLabel("标签名：");
//        labelField.setLabelWidth(115);
        labelField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        labelField.setSizeType(SizeType.SMALL);
        this.addActionComponent(labelField);

        prefixs = setPrefix();
        suffixs = setSuffix();

        alignments = setAlignments();
        displays = setDisplayTypes();
        colorTypes = setColorTypes();
        roundTypes = setRoundTypes();
        sizeTypes = setSizeTypes();
        borderTypes = setBorderTypes();
        cleanables = setClearables();
        inputTypes = setInputTypes();

        field = generateField();
        field.setPrefWidth(300);
        this.setShowComponent(field);

        labelField.textProperty().addListener(changeListener);

        showPasswordBox = new HBox();
        showPasswordBox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("是否显示密码:  ");

        showPasswords = new XmCheckBox<>();
        showPasswords.setSizeType(SizeType.SMALL);

        showPasswordBox.getChildren().addAll(label, showPasswords);

        this.addActionComponent(showPasswordBox);

        showPasswordBox.setVisible(false);

        echocharField = new XmTextField("*");
        echocharField.setLabel("密码替代字符：");
        echocharField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        echocharField.setSizeType(SizeType.SMALL);
        echocharField.setVisible(false);
        this.addActionComponent(echocharField);

        showPasswords.selectedProperty().addListener((ob, ov, nv)->{
            this.field.setShowPassword(nv);
            this.field.setText(this.field.getText());
            this.javaCodes.put("showPassword", "field.setShowPassword("+nv+");");
            cssCodes.put("-fx-show-password", nv+"");
        });

        echocharField.textProperty().addListener((ob, ov, nv)->{
            this.field.setEchochar(echocharField.getText());
            this.field.setText(this.field.getText());
            this.javaCodes.put("echoChar", "field.setEchochar(\""+echocharField.getText()+"\");");
            cssCodes.put("-fx-echochar", "'"+nv+"'");
        });

        String javaCodePrev = "field = new XmTextField();\r\n"
                +"field.getStyleClass().add(\"my-field\");\r\n"
                +"field.setLabelWidth(100);\r\n";

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
                    text += key+":"+cssCodes.get(key)+";\r\n";
                }

                getCssText().setText(text);
            }
        });

        getClearBtn().setOnAction(e -> {
            field.getStylesheets().remove(css);
        });

        getRunBtn().setOnAction(e -> {
            field.getStylesheets().remove(css);
            css = ".my-field{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");

//                field = generateCssField();
                field.getStylesheets().add(css);
//                this.setShowComponent(field);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

//            field.getStylesheets().add(css);
        });

    }

    public ChangeListener<Object> changeListener = (ob, ov, nv) -> {
        this.field = generateField();
        this.setShowComponent(field);
    };

    private XmTextField generateCssField(){
        XmTextField nfield = new XmTextField();
        if(field!=null){
            nfield.setText(field.getText());
        }
        nfield.setLabelWidth(100);
        nfield.getStyleClass().add("my-field");

        String prefix = prefixs.getValue();
        if(prefix != null && !prefix.equals("空")){
            nfield.setPrefix(new XmFontIcon(prefix));
        }

        String suffix = suffixs.getValue();
        if(suffix != null && !suffix.equals("空")){
            nfield.setSuffix(new XmFontIcon(suffix));
        }

        String label = labelField.getText();
        if(label != null){
            nfield.setLabel(label);
        }
        return nfield;
    }

    private XmTextField generateField(){
        XmTextField nfield = new XmTextField();
        if(field!=null){
            nfield.setText(field.getText());
        }
        nfield.setLabelWidth(100);
        nfield.getStyleClass().add("my-field");

        String prefix = prefixs.getValue();
        if(prefix != null && !prefix.equals("空")){
            System.out.println("11");
            nfield.setPrefix(new XmFontIcon(prefix));
            String s = "\\u" + Integer.toHexString(prefix.charAt(0) | 0x10000).substring(1);
            javaCodes.put("prefix", "field.setPrefix(new XmFontIcon(\""+s+"\"));");
        }

        String suffix = suffixs.getValue();
        if(suffix != null && !suffix.equals("空")){
            nfield.setSuffix(new XmFontIcon(suffix));
            String s = "\\u" + Integer.toHexString(suffix.charAt(0) | 0x10000).substring(1);
            javaCodes.put("prefix", "field.setSuffix(new XmFontIcon(\""+s+"\"));");
        }

        String label = labelField.getText();
        if(label != null){
            nfield.setLabel(label);
            javaCodes.put("label", "field.setLabel(\""+label+"\"));");
        }

        XmAlignment align = alignments.getValue();
        if(align!=null){
            nfield.setAlignment(align);
            javaCodes.put("align", "field.setLabelAlignment(XmAlignment."+align+"));");
            cssCodes.put("-fx-label-align", align.name().toLowerCase());
        }

        XmFieldDisplayType display = displays.getValue();
        if(display!=null){
            nfield.setDisplayType(display);
            javaCodes.put("display", "field.setDisplayType(XmFieldDisplayType."+display+"));");
            cssCodes.put("-fx-type-display", display.name().toLowerCase().replace("_", "-"));
        }

        ColorType color = colorTypes.getValue();
        if(color!=null){

            if(color.getLabel().equals("other")){
                color = ColorType.other(colorField.getText());
            }

            nfield.setColorType(color);
            javaCodes.put("color","field.setColorType(ColorType.get(\""+color+"\"));");
            cssCodes.put("-fx-type-color",color.toString().toLowerCase());
        }

        RoundType round = roundTypes.getValue();
        if(round!=null){
            nfield.setRoundType(round);
            javaCodes.put("round", "field.setRoundType(RoundType."+round+"));");
            cssCodes.put("-fx-type-round", round.name().toLowerCase().replace("_", "-"));
        }

        SizeType size = sizeTypes.getValue();
        if(size!=null){
            nfield.setSizeType(size);
            javaCodes.put("size", "field.setSizeType(SizeType."+size+"));");
            cssCodes.put("-fx-type-size", size.name().toLowerCase().replace("_", "-"));
        }

        BorderType border = borderTypes.getValue();
        if(border != null){
            nfield.setBorderType(border);
            javaCodes.put("border", "field.setBorderType(BorderType."+border+"));");
            cssCodes.put("-fx-type-round", border.name().toLowerCase().replace("_", "-"));
        }

        XmTextInputType inputType = inputTypes.getValue();
        if(inputType != null){
            nfield.setInputType(inputType);
            javaCodes.put("inputType", "field.setInputType(XmTextType."+inputType+"));");
            cssCodes.put("-fx-type-input", inputType.name().toLowerCase().replace("_", "-"));
        }

        Boolean cleanable = cleanables.isSelected();
        if(cleanable!= null){
            nfield.setCleanable(cleanable);
            javaCodes.put("cleanable", "field.setCleanable("+cleanable+"));");
            cssCodes.put("-fx-cleanable", cleanable.toString());
        }


        return nfield;
    }

    /**
     * 设置文本框的前缀图标
     */
    private XmSelector<String> setPrefix() {

        XmSelector<String> prefixSelector = new XmSelector<String>();
        prefixSelector.setPrefWidth(200);
        prefixSelector.setSizeType(SizeType.SMALL);
        prefixSelector.setValue("空");
        prefixSelector.setPrefix(new Text("空"));
        prefixSelector.setConverter(new SelectorConvert<String>() {
            @Override
            public String toString(String object) {
                if(object.equals("空")){
                    return object;
                }
                return "\\u" + Integer.toHexString(object.charAt(0) | 0x10000).substring(1);
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });

        prefixSelector.setSizeType(SizeType.SMALL);

        prefixSelector.setSelectorType(SelectorType.LIST);
        prefixSelector.getItems().addAll("空", "\ue8fc", "\ue6f7", "\ue8ac", "\ue65e", "\ue67b", "\ue668");
        prefixSelector.setValue("空");

        prefixSelector.setCellFactory(new SelectorCellFactory<String>() {
            @Override
            public void updateItem(IndexedCell<String> cell, String item, boolean empty) {
                if(item == null || empty){
                    cell.setText(null);
                    cell.setGraphic(null);
                }else{
                    if(item.equals("空")){
                        cell.setText(item);
                        cell.setGraphic(new Text(item));
                    }else{
                        String s = "\\u" + Integer.toHexString(item.charAt(0) | 0x10000).substring(1);
                        cell.setText(s);
                        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());
                        XmFontIcon icon = new XmFontIcon(item);
                        cell.setGraphic(icon);
                    }
                }
            }
        });

        prefixSelector.getValues().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                String value = prefixSelector.getValue();
                if(value == null){
                    return;
                }
                if(value.equals("空")){
                    prefixSelector.setPrefix(new Text(value));
                }else{
                    prefixSelector.setPrefix(new XmFontIcon(value));
                }

                field = generateField();
                setShowComponent(field);
            }
        });

//        selector.setPrefix(new XmFontIcon("\ue69a"));

        XmLabel label = new XmLabel("前缀图标：");

        HBox box = new HBox(label, prefixSelector);

        box.setAlignment(Pos.TOP_LEFT);
        this.addActionComponent(box);
        return prefixSelector;
    }


    /**
     * 设置文本款的后最图标
     */
    private XmSelector<String> setSuffix() {

        XmSelector<String> suffixSelector = new XmSelector<String>();
        suffixSelector.setPrefWidth(200);
        suffixSelector.setSizeType(SizeType.SMALL);
        suffixSelector.setValue("空");
        suffixSelector.setPrefix(new Text("空"));
        suffixSelector.setConverter(new SelectorConvert<String>() {
            @Override
            public String toString(String object) {
                if(object.equals("空")){
                    return object;
                }
                return "\\u" + Integer.toHexString(object.charAt(0) | 0x10000).substring(1);
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });

        suffixSelector.setSizeType(SizeType.SMALL);

        suffixSelector.setSelectorType(SelectorType.LIST);
        suffixSelector.getItems().addAll("空", "\ue8fc", "\ue6f7", "\ue8ac", "\ue65e", "\ue67b", "\ue668");
        suffixSelector.setValue("空");

        suffixSelector.setCellFactory(new SelectorCellFactory<String>() {
            @Override
            public void updateItem(IndexedCell<String> cell, String item, boolean empty) {
                if(item == null || empty){
                    cell.setText(null);
                    cell.setGraphic(null);
                }else{
                    if(item.equals("空")){
                        cell.setText(item);
                        cell.setGraphic(new Text(item));
                    }else{
                        String s = "\\u" + Integer.toHexString(item.charAt(0) | 0x10000).substring(1);
                        cell.setText(s);
                        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());
                        XmFontIcon icon = new XmFontIcon(item);
                        cell.setGraphic(icon);
                    }
                }
            }
        });

        suffixSelector.getValues().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                String value = suffixSelector.getValue();
                if(value == null){
                    return;
                }
                if(value.equals("空")){
                    suffixSelector.setPrefix(new Text(value));
                }else{
                    suffixSelector.setPrefix(new XmFontIcon(value));
                }
                field = generateField();
                setShowComponent(field);
            }
        });

//        selector.setPrefix(new XmFontIcon("\ue69a"));

        XmLabel label = new XmLabel("后缀图标：");

        HBox box = new HBox(label, suffixSelector);

        box.setAlignment(Pos.TOP_LEFT);
        this.addActionComponent(box);
        return suffixSelector;
    }

    private XmSelector<XmAlignment> setAlignments() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("标签对齐方式: ");

        XmSelector<XmAlignment> alignments = new XmSelector<>();
        alignments.setPrefWidth(250);
        alignments.setSizeType(SizeType.SMALL);
        alignments.getItems().addAll(XmAlignment.LEFT, XmAlignment.CENTER, XmAlignment.RIGHT, XmAlignment.JUSTIFY);
        alignments.setValue(XmAlignment.LEFT);
        alignments.getValues().addListener(new ListChangeListener<XmAlignment>() {
            @Override
            public void onChanged(Change<? extends XmAlignment> c) {
                field = generateField();
                setShowComponent(field);
            }
        });

        box.getChildren().addAll(label, alignments);

        this.addActionComponent(box);

        return alignments;
    }

    private XmSelector<XmFieldDisplayType> setDisplayTypes() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("排版方式: ");

        XmSelector<XmFieldDisplayType> fieldDisplaySelector = new XmSelector<XmFieldDisplayType>();
        fieldDisplaySelector.setPrefWidth(250);
        fieldDisplaySelector.setSizeType(SizeType.SMALL);
        fieldDisplaySelector.setValue(XmFieldDisplayType.VERTICAL_INLINE);
        fieldDisplaySelector.getItems().addAll(XmFieldDisplayType.HORIZONTAL_OUTLINE, XmFieldDisplayType.HORIZONTAL_INLINE, XmFieldDisplayType.VERTICAL_INLINE, XmFieldDisplayType.VERTICAL_OUTLINE);
        fieldDisplaySelector.getValues().addListener(new ListChangeListener<XmFieldDisplayType>() {
            @Override
            public void onChanged(Change<? extends XmFieldDisplayType> c) {
                field = generateField();
                setShowComponent(field);
            }
        });

        box.getChildren().addAll(label, fieldDisplaySelector);

        this.addActionComponent(box);

        return fieldDisplaySelector;
    }



    private XmSelector<ColorType> setColorTypes() {

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
                }else{
                    colorField.setVisible(false);
                    colorField.setManaged(false);
                }

                field = generateField();
                setShowComponent(field);
            }
        });

//        selector.setPrefix(new XmFontIcon("\ue69a"));

        XmLabel label = new XmLabel("控件颜色：");

        HBox box = new HBox(label, colorSelector);

        box.setAlignment(Pos.TOP_LEFT);
        this.addActionComponent(box);

        setMyDefineColor();
        return colorSelector;
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

        colorField.textProperty().addListener((ob, ov, nv) -> {
            field = generateField();
            setShowComponent(field);
        });

        this.addActionComponent(colorField);
    }

    private XmToggleGroup<RoundType> setRoundTypes() {
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
            field = generateField();
            setShowComponent(field);
        });

        this.addActionComponent(box);
        return tg;
    }

    private XmToggleGroup<SizeType> setSizeTypes() {
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
            field = generateField();
            setShowComponent(field);
        });

        this.addActionComponent(box);
        return tg;
    }

    private XmToggleGroup<BorderType> setBorderTypes() {
        XmLabel label = new XmLabel("控件边框：");

        XmCheckBox<BorderType> noneCb = new XmCheckBox<BorderType>();
        noneCb.setValue(BorderType.NONE);
        noneCb.setText("NONE");
        noneCb.setSizeType(SizeType.SMALL);

        XmCheckBox<BorderType> bottomCb = new XmCheckBox<BorderType>();
        bottomCb.setValue(BorderType.BOTTOM);
        bottomCb.setText("BOTTOM");
        bottomCb.setSizeType(SizeType.SMALL);
        bottomCb.setSelected(true);

        XmCheckBox<BorderType> fullCb = new XmCheckBox<BorderType>();
        fullCb.setValue(BorderType.FULL);
        fullCb.setText("SMALL");
        fullCb.setSizeType(SizeType.SMALL);
        fullCb.setSelected(true);

        XmToggleGroup<BorderType> tg = new XmToggleGroup<>();
        noneCb.setToggleGroup(tg);
        bottomCb.setToggleGroup(tg);
        fullCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, noneCb, bottomCb, fullCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            field = generateField();
            setShowComponent(field);
        });

        this.addActionComponent(box);
        return tg;
    }
    private XmCheckBox<Boolean> setClearables() {

        XmLabel label = new XmLabel("显示清除图标：");

        XmCheckBox<Boolean> clearCb = new XmCheckBox<>();
        clearCb.setSelected(true);
        clearCb.setSizeType(SizeType.SMALL);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, clearCb);

        clearCb.selectedProperty().addListener((ob, ov, nv)->{
            field = generateField();
            setShowComponent(field);
        });

        this.addActionComponent(box);
        return clearCb;

    }

    private XmToggleGroup<XmTextInputType> setInputTypes() {

        XmLabel label = new XmLabel("输入类型：");

        XmCheckBox<XmTextInputType> textCb = new XmCheckBox<XmTextInputType>();
        textCb.setValue(XmTextInputType.TEXT);
        textCb.setText("NONE");
        textCb.setSizeType(SizeType.SMALL);
        textCb.setSelected(true);

        XmCheckBox<XmTextInputType> passwordCb = new XmCheckBox<XmTextInputType>();
        passwordCb.setValue(XmTextInputType.PASSWORD);
        passwordCb.setText("BOTTOM");
        passwordCb.setSizeType(SizeType.SMALL);
        passwordCb.setSelected(true);

        XmToggleGroup<XmTextInputType> tg = new XmToggleGroup<>();
        passwordCb.setToggleGroup(tg);
        textCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label,textCb, passwordCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            field = generateField();
            setShowComponent(field);

            if(nv.getValue().equals(XmTextInputType.PASSWORD)){
                showPasswordBox.setVisible(true);
                echocharField.setVisible(true);
            }else{
                showPasswordBox.setVisible(false);
                echocharField.setVisible(false);
            }

        });

        this.addActionComponent(box);
        return tg;
    }


}
