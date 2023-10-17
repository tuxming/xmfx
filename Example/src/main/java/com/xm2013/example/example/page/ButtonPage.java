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
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.checkbox.XmToggleGroup;
import com.xm2013.jfx.control.textfield.XmFieldDisplayType;
import com.xm2013.jfx.control.textfield.XmTextField;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.selector.SelectorCellFactory;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.SelectorType;
import com.xm2013.jfx.control.selector.XmSelector;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;

public class ButtonPage extends BasePage {
    private XmButton btn;
    double percent = 0.0;

    private ObservableMap<String, String> javaCodes = FXCollections.observableHashMap();
    private ObservableMap<String, String> cssCodes = FXCollections.observableHashMap();
    private String css;
    private XmTextField labelField;

    public ButtonPage() {

        this.setTitle("按钮（XmButton）", new XmFontIcon("\ue6fc"));
        this.setComponentTitle("自定义按钮");

//        btn = new XmButton("自定义按钮", new XmFontIcon("\ue66c"));
        XmSVGIcon icon = new XmSVGIcon("<svg t=\"1687375074977\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"1472\" width=\"64\" height=\"64\"><path d=\"M512 72c59.4 0 117 11.6 171.2 34.5 52.4 22.2 99.4 53.9 139.9 94.3 40.4 40.4 72.2 87.5 94.3 139.9C940.4 395 952 452.6 952 512s-11.6 117-34.5 171.2c-22.2 52.4-53.9 99.4-94.3 139.9-40.4 40.4-87.5 72.2-139.9 94.3C629 940.4 571.4 952 512 952s-117-11.6-171.2-34.5c-52.4-22.2-99.4-53.9-139.9-94.3-40.4-40.4-72.2-87.5-94.3-139.9C83.6 629 72 571.4 72 512s11.6-117 34.5-171.2c22.2-52.4 53.9-99.4 94.3-139.9 40.4-40.4 87.5-72.2 139.9-94.3C395 83.6 452.6 72 512 72m0-72C229.2 0 0 229.2 0 512s229.2 512 512 512 512-229.2 512-512S794.8 0 512 0z\" p-id=\"1473\"></path><path d=\"M425 714.9c-20.7 0-40.2-8.1-54.8-22.7L222.4 544.4c-14.1-14.1-14.1-36.9 0-50.9 14.1-14.1 36.9-14.1 50.9 0l147.8 147.8c1.3 1.3 2.9 1.6 3.9 1.6s2.6-0.3 3.9-1.6l321.7-321.6c14.1-14.1 36.9-14.1 50.9 0 14.1 14.1 14.1 36.9 0 50.9L479.9 692.2c-14.7 14.6-34.1 22.7-54.9 22.7z\" p-id=\"1474\"></path></svg>");
        icon.setSize(30);
        btn = new XmButton("自定义按钮", icon);
        btn.getStyleClass().add("my-btn");
//        btn.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
//        btn.setFinalWidth(200);

        this.setShowComponent(btn);
        setColor(btn);
        setSize(btn);
        setHue(btn);
        setRound(btn);
        setBorder(btn);
        setDisplay(btn);
        setContentDisplay();
        setLoading();
        setClickAnimateType();
        setIconAnimate();

        String javaCodePrev = "XmButton btn = new XmButton(\"自定义按钮\");\r\n"
                +"btn.getStyleClass().add(\"my-btn\");\r\n"
                + "XmSvgIcon icon = new XmSvgIcon(\""+icon.getContent().replace("\"", "\\\"")+"\");\r\n"
                + "XmButtonType t = btn.getType();\r\n";
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
            btn.getStylesheets().remove(css);
        });

        getRunBtn().setOnAction(e -> {
            btn.getStylesheets().remove(css);
            css = ".my-btn{"+getCssText().getText()+"}";
            try {
                css = "data:text/css;charset=utf-8;base64,"+
                        new String(Base64.getEncoder().encode(css.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

            btn.getStylesheets().add(css);
        });

    }

    private void playLoading() {
        Platform.runLater(() -> {
            percent = 0d;
            btn.setLoadingPercent(percent);
            btn.setLoading(true);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            Random random = new Random();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> {
                if (percent <= 1d) {
                    double np = random.nextDouble() * 0.1;
                    percent += np;
                    btn.setLoadingPercent(percent);
//                        System.out.println(np);
                } else {
                    btn.setLoadingPercent(1);
                    btn.setLoading(false);
                    timeline.stop();
                }
            });

            // 添加关键帧到 Timeline 中并启动动画
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
    }

    /**
     * 设置颜色类型
     * @param button
     */
    private void setColor(XmButton button) {

        XmSelector<ColorType> colorSelector = new XmSelector<ColorType>();
        colorSelector.setSizeType(SizeType.SMALL);
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
                    labelField.setVisible(true);
                    labelField.setManaged(true);
                    button.setColorType(ColorType.other(labelField.getText()));
                }else{
                    labelField.setVisible(false);
                    labelField.setManaged(false);

                    button.setColorType(item);

                    javaCodes.put("colorType","button.setColorType(ColorType.get(\""+item+"\"));\r\n");
                    cssCodes.put("-fx-type-color",item.toString().toLowerCase()+";");
                }
            }
        });

//        selector.setPrefix(new XmFontIcon("\ue69a"));

        XmLabel label = new XmLabel("控件颜色：");

        HBox box = new HBox(label, colorSelector);

        box.setAlignment(Pos.TOP_LEFT);
        this.addActionComponent(box);

        setMyDefineColor(button);
    }

    /**
     * 设置自定义颜色
     */
    private void setMyDefineColor(XmButton selector){
        labelField = new XmTextField("自定义颜色");
        labelField.setLabel("自定义颜色：");
        labelField.setVisible(false);
        labelField.setManaged(false);
        labelField.setText("linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #23d0f3ff 0.0%, #d791f9ff 50.0%, #fe7b84ff 100.0%)");
//        labelField.setLabelWidth(115);
        labelField.setDisplayType(XmFieldDisplayType.HORIZONTAL_OUTLINE);
        labelField.setSizeType(SizeType.SMALL);

        labelField.textProperty().addListener((ob, ov, nv) -> {
            selector.setColorType(ColorType.other(labelField.getText().trim()));
            javaCodes.put("setMyColor", "button.setColorType(ColorType.other(\""+labelField.getText()+"\"));");
            cssCodes.put("-fx-type-color", labelField.getText().trim()+";");
        });

        this.addActionComponent(labelField);
    }

    /**
     * 设置色调类型
     * @param button
     */
    private void setHue(XmButton button) {
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
            button.setHueType(nv.getValue());
            this.javaCodes.put("hueType", "button.setHueType(HueType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-hue", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置尺寸
     * @param button
     */
    private void setSize(XmButton button) {

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
            button.setSizeType(nv.getValue());
            this.javaCodes.put("sizeType", "button.setSizeType(SizeType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-size", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置圆角类型
     * @param button
     */
    private void setRound(XmButton button) {

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
            button.setRoundType(nv.getValue());
            this.javaCodes.put("roundType", "button.setRoundType(RoundType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-round", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置空间边框
     * @param button
     */
    private void setBorder(XmButton button) {
        XmLabel label = new XmLabel("控件边框：");

        XmCheckBox<BorderType> noneCb = new XmCheckBox<BorderType>();
        noneCb.setValue(BorderType.NONE);
        noneCb.setText("NONE");
        noneCb.setSizeType(SizeType.SMALL);

        XmCheckBox<BorderType> fullCb = new XmCheckBox<BorderType>();
        fullCb.setValue(BorderType.FULL);
        fullCb.setText("SMALL");
        fullCb.setSelected(true);
        fullCb.setSizeType(SizeType.SMALL);

        XmToggleGroup<BorderType> tg = new XmToggleGroup<>();
        noneCb.setToggleGroup(tg);
        fullCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, noneCb, fullCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            button.setBorderType(nv.getValue());
            this.javaCodes.put("borderType", "button.setBorderType(RoundType."+nv.getValue()+");");
            this.cssCodes.put("-fx-type-border", nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    /**
     * 设置显示类型
     * @param btn
     */
    private void setDisplay(XmButton btn) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        XmLabel label = new XmLabel("显示类型：");

        XmSelector<XmButtonType.BtnDisplayType> selector = new XmSelector<>();
        selector.setValue(XmButtonType.BtnDisplayType.FULL);
        selector.setSizeType(SizeType.SMALL);
        selector.setItems(XmButtonType.BtnDisplayType.FULL, XmButtonType.BtnDisplayType.OVER_SHOW_ICON,
                XmButtonType.BtnDisplayType.OVER_SHOW_TEXT, XmButtonType.BtnDisplayType.OVER_SWITCH_ICON,
                XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT);

        selector.getValues().addListener((ListChangeListener<XmButtonType.BtnDisplayType>) c -> {
            XmButtonType.BtnDisplayType type = selector.getValue();
            if(type != null){
                btn.setDisplayType(type);
                this.javaCodes.put("displayType", "button.setDisplayType(RoundType."+type+");");
                this.cssCodes.put(CssKeys.PropTypeDisplay, type.toString().replace("_","-").toLowerCase()+";");
            }
        });

        box.getChildren().addAll(label, selector);

        this.addActionComponent(box);

    }

    public void setContentDisplay(){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        XmLabel label = new XmLabel("内容排版：");

        XmSelector<ContentDisplay> selector = new XmSelector<>();
        selector.setSizeType(SizeType.SMALL);
        selector.setItems(ContentDisplay.CENTER, ContentDisplay.LEFT, ContentDisplay.RIGHT, ContentDisplay.BOTTOM, ContentDisplay.TOP);
        selector.setValue(ContentDisplay.CENTER);

        selector.getValues().addListener((ListChangeListener<ContentDisplay>) c -> {
            ContentDisplay type = selector.getValue();
            if(type != null){
                btn.setContentDisplay(type);
                this.javaCodes.put("contentDisplay", "button.setContentDisplay(ContentDisplay."+type+");");
                this.cssCodes.put("-fx-content-display", type.toString().replace("_","-").toLowerCase()+";");
            }
        });

        box.getChildren().addAll(label, selector);

        this.addActionComponent(box);
    }

    private void setLoading() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        XmLabel label = new XmLabel("进度条：");

        XmSelector<XmButtonType.BtnLoadingType> selector = new XmSelector<>();
        selector.setSizeType(SizeType.SMALL);
        selector.setItems(XmButtonType.BtnLoadingType.NONE, XmButtonType.BtnLoadingType.ICON, XmButtonType.BtnLoadingType.H_PROGRESS, XmButtonType.BtnLoadingType.V_PROGRESS);
        selector.setPrefWidth(200);

        selector.getValues().addListener((ListChangeListener<XmButtonType.BtnLoadingType>) c -> {
            XmButtonType.BtnLoadingType type = selector.getValue();
            if(type != null){
                btn.setLoadingType(type);
                this.javaCodes.put("loadingType", "button.setLoadingType(XmButtonType.BtnLoadingType."+type+");");
                this.cssCodes.put(CssKeys.PropTypeLoading, type.toString().replace("_","-").toLowerCase()+";");
            }
        });

        XmButton playBtn = new XmButton("演示");
        playBtn.setSizeType(SizeType.SMALL);
        playBtn.setHueType(HueType.LIGHT);
        playBtn.setColorType(ColorType.warning());
        playBtn.setOnAction(e -> {
            btn.setLoadingPercent(1);
            btn.setLoading(false);
            playLoading();
        });


        box.getChildren().addAll(label, selector, playBtn);

        this.addActionComponent(box);

    }

    /**
     * 设置点击动画类型
     */
    private void setClickAnimateType() {
        XmLabel label = new XmLabel("点击动画：");

        XmCheckBox<ClickAnimateType> noneCb = new XmCheckBox<ClickAnimateType>();
        noneCb.setValue(ClickAnimateType.NONE);
        noneCb.setText("NONE");
        noneCb.setSizeType(SizeType.SMALL);
        noneCb.setSelected(true);

        XmCheckBox<ClickAnimateType> shadowCb = new XmCheckBox<ClickAnimateType>();
        shadowCb.setValue(ClickAnimateType.SHADOW);
        shadowCb.setText("SHADOW");
        shadowCb.setSizeType(SizeType.SMALL);

        XmCheckBox<ClickAnimateType> ripperCb = new XmCheckBox<ClickAnimateType>();
        ripperCb.setValue(ClickAnimateType.RIPPER);
        ripperCb.setText("RIPPER");
        ripperCb.setSizeType(SizeType.SMALL);

        XmToggleGroup<ClickAnimateType> tg = new XmToggleGroup<>();
        noneCb.setToggleGroup(tg);
        shadowCb.setToggleGroup(tg);
        ripperCb.setToggleGroup(tg);

        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_LEFT);
        box.setSpacing(10);
        box.getChildren().addAll(label, noneCb, shadowCb, ripperCb);

        tg.selectedToggleProperty().addListener((ob, ov, nv) ->{
            btn.setClickAnimateType(nv.getValue());
            this.javaCodes.put("clickAnimateType", "button.setClickAnimateType(ClickAnimateType."+nv.getValue()+");");
            this.cssCodes.put(CssKeys.PropTypeClickAnimate, nv.getValue().toString().replace("_","-").toLowerCase()+";");
        });

        this.addActionComponent(box);
    }

    private void setIconAnimate() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        XmLabel label = new XmLabel("图标动画：");

        XmSelector<XmButtonType.BtnIconAnimationType> selector = new XmSelector<>();
        selector.setSizeType(SizeType.SMALL);
        selector.setValue(XmButtonType.BtnIconAnimationType.NONE);
        selector.setItems( XmButtonType.BtnIconAnimationType.NONE,
                XmButtonType.BtnIconAnimationType.ROTATE,
                XmButtonType.BtnIconAnimationType.JITTER,
                XmButtonType.BtnIconAnimationType.OVERTURN,
                XmButtonType.BtnIconAnimationType.FLASH,
                XmButtonType.BtnIconAnimationType.SCALE);
        selector.setPrefWidth(200);

        selector.getValues().addListener((ListChangeListener<XmButtonType.BtnIconAnimationType>) c -> {
            XmButtonType.BtnIconAnimationType type = selector.getValue();
            if(type != null){
                btn.setIconAnimateType(type);
                this.javaCodes.put("iconAnimate", "button.setIconAnimateType(XmButtonType.BtnIconAnimationType."+type+");");
                this.cssCodes.put(CssKeys.PropTypeIconAnimate, type.toString().replace("_","-").toLowerCase()+";");
            }
        });

        box.getChildren().addAll(label, selector);

        this.addActionComponent(box);

    }


}
