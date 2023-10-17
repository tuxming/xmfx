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
package com.xm2013.jfx.control.date;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.container.SimpleVScrollPane;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.label.SelectableText;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间pane
 */
public class TimePane extends Pane {

    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(null);

    private SimpleVScrollPane hourPane, minPane, secPane;

    private XmNodeButton prevHourCell, prevMinCell, prevSecCell;
    private VBox hourBox;
    private VBox minBox;
    private VBox secBox;

    private SelectableText showText;
    private Line line;

    public TimePane(){
        this(LocalTime.now());
    }


    public TimePane(LocalTime time){
        this.time.set(time);
        this.init();
    }

    private void init() {

        getStyleClass().add("xm-time-pane");
        setPrefWidth(180);
        setPrefHeight(240);
        setMinWidth(180);
        setMinHeight(240);

        showText = new SelectableText();
        showText.setFont(Font.font(showText.getFont().getFamily(), FontWeight.BOLD, 13));
        showText.textProperty().bind(Bindings.createStringBinding(()->
                time.get().format(DateTimeFormatter.ofPattern(FxKit.TIME_PATTERN)), timeProperty()));

        line = new Line();
        line.setFill(Color.web("#88888855"));
        line.setStartY(40);
        line.setStartX(5);
        line.endXProperty().bind(prefWidthProperty().subtract(5));
        line.setEndY(40);
        line.setStrokeWidth(0.5);

        hourPane = new SimpleVScrollPane();
        hourPane.setViewHeight(190);
        hourPane.setViewWidth(60);
        hourPane.getScrollBar().colorTypeProperty().bind(colorTypeProperty());

        hourBox = new VBox();
        hourBox.setFillWidth(true);
        for(int i=0; i<24; i++){
            XmNodeButton cell = new XmNodeButton(i<10?("0"+i):(""+i));
            cell.prefWidthProperty().bind(hourPane.widthProperty().subtract(15));
            cell.colorTypeProperty().bind(colorTypeProperty());
            cell.setOnAction(e->{
                time.set(time.get().withHour(Integer.parseInt(cell.getText())));
            });
            hourBox.getChildren().add(cell);
        }
        hourPane.setContent(hourBox);

        minPane = new SimpleVScrollPane();
        minPane.setViewHeight(190);
        minPane.setViewWidth(60);
        minPane.getScrollBar().colorTypeProperty().bind(colorTypeProperty());

        minBox = new VBox();
        for(int i=0; i<60; i++){
            XmNodeButton cell = new XmNodeButton(i<10?("0"+i):(""+i));
            cell.prefWidthProperty().bind(minPane.widthProperty().subtract(15));
            cell.colorTypeProperty().bind(colorTypeProperty());
            cell.setOnAction(e->{
                time.set(time.get().withMinute(Integer.parseInt(cell.getText())));
            });
            minBox.getChildren().add(cell);
        }
        minPane.setContent(minBox);

        secPane = new SimpleVScrollPane();
        secPane.setViewHeight(190);
        secPane.setViewWidth(60);
        secPane.getScrollBar().colorTypeProperty().bind(colorTypeProperty());

        secBox = new VBox();
        for(int i=0; i<60; i++){
            XmNodeButton cell = new XmNodeButton(i<10?("0"+i):(""+i));
            cell.prefWidthProperty().bind(secPane.widthProperty().subtract(15));
            cell.colorTypeProperty().bind(colorTypeProperty());
            cell.setOnAction(e->{
                time.set(time.get().withSecond(Integer.parseInt(cell.getText())));
            });
            secBox.getChildren().add(cell);
        }
        secPane.setContent(secBox);

        getChildren().addAll(showText, line, secPane,  minPane, hourPane);

        Platform.runLater(()->{
            setStatus();
        });

        timeProperty().addListener((ob, ov, nv)->{
            setStatus();
        });

    }

    private void setStatus(){
        int hour = time.get().getHour();
        XmNodeButton cell = (XmNodeButton) hourBox.getChildren().get(hour);
        if(prevHourCell!=null){
            prevHourCell.setFilled(false);
        }
        hourPane.scrollTo(hour*cell.prefHeight(-1));
        cell.setFilled(true);
        prevHourCell = cell;

        int min = time.get().getMinute();
        cell = (XmNodeButton) minBox.getChildren().get(min);
        if(prevMinCell!=null){
            prevMinCell.setFilled(false);
        }
        double height = min * cell.prefHeight(-1);
        minPane.scrollTo(height);
        cell.setFilled(true);
        prevMinCell = cell;

        int sec = time.get().getSecond();
        cell = (XmNodeButton) secBox.getChildren().get(sec);
        if(prevSecCell!=null){
            prevSecCell.setFilled(false);
        }
        secPane.scrollTo(sec*cell.prefHeight(-1));
        cell.setFilled(true);
        prevSecCell = cell;
    }

    public LocalTime getTime(){
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    public void setTime(LocalTime time){
        this.time.set(time);
    }

    /**
     * 颜色
     */
    private ObjectProperty<ColorType> colorType;
    public ColorType getColorType() {
        return colorTypeProperty().get();
    }
    public ObjectProperty<ColorType> colorTypeProperty() {
        if(colorType == null){
            colorType = new SimpleObjectProperty<>(ColorType.primary());
        }
        return colorType;
    }
    public void setColorType(ColorType colorType) {
        this.colorTypeProperty().set(colorType);
    }

    /**
     * 以下是兼容scenebuilder属性面板的属性设置
     * @param colorType String
     */
    public void setScColorType(String colorType){
        this.colorTypeProperty().set(ColorType.get(colorType));
    }
    public String getScColorType(){
        return this.colorTypeProperty().get().toString();
    }

    @Override
    protected void layoutChildren() {

        double width = prefWidth(-1),
                height = prefHeight(-1),
                textWidth = showText.prefWidth(-1),
                textPaneHeight = 40,
                textHeight = showText.prefHeight(-1),
                textX = (width - textWidth) / 2,
                textY = (textPaneHeight - textHeight) / 2,

                paneHeight = height - 40 -10,
                paneWidth = width/3d,
                hourPaneX = 0,
                paneY = textPaneHeight + 10,
                minPaneX = paneWidth,
                secPaneX = paneWidth * 2;

        hourPane.setViewWidth(paneWidth);
        hourPane.setViewHeight(paneHeight);
        minPane.setViewWidth(paneWidth);
        minPane.setViewHeight(paneHeight);
        secPane.setViewWidth(paneWidth);
        secPane.setViewHeight(paneHeight);



        layoutInArea(showText, textX, textY, textWidth, textHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(hourPane, hourPaneX, paneY, paneWidth, height, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(minPane, minPaneX, paneY, paneWidth, height, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(secPane, secPaneX, paneY, paneWidth, height, 0, HPos.CENTER, VPos.CENTER);
    }
}
