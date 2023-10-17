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

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.control.button.XmNodeButton;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class XmDatePickerSkin extends SkinBase {
    private XmDatePicker control = null;

    private DateBodyPane datePane;
    private MonthBodyPane monthPane;
    private YearBodyPane yearPane;

    private TimePane timePane;

    public XmDatePickerSkin(XmDatePicker control) {
        super(control);
        this.control = control;

        if(control.getPrefHeight() == -1){
            control.setPrefHeight(260);
        }

        updateLayout();

        control.dateTypeProperty().addListener((ob, ov, nv)->{
            updateLayout();
        });

    }

    /*
    只有日期面板的时候，最小宽度为290， 最小高度为259，
    如果是时间日期面板，最小宽度为290+180， 最小高度为259
     */
    private void updateLayout() {

        buildTimePane();
        buildDatePane();
        buildMonthPane();
        buildDatePane();
        buildYearPane();

        DateType dateType = control.getDateType();
        if(dateType.equals(DateType.TIME)){
            getChildren().remove(timePane);
            timePane.setPrefWidth(control.getPrefWidth());
            timePane.setPrefHeight(control.getPrefHeight());
            datePane.setVisible(false);
            monthPane.setVisible(false);
            yearPane.setVisible(false);
            timePane.setVisible(true);
            getChildren().add(timePane);
        }else if(dateType.equals(DateType.MONTH)){
            timePane.setVisible(false);
            datePane.setVisible(false);
            monthPane.setVisible(true);
            yearPane.setVisible(false);
            yearPane.setStep(100);
            yearPane.setShowRange(true);
        }else if(dateType.equals(DateType.YEAR)){
            timePane.setVisible(false);
            datePane.setVisible(false);
            monthPane.setVisible(false);
            yearPane.setVisible(true);
            yearPane.setStep(10);
            yearPane.setShowRange(false);
        }else if(dateType.equals(DateType.DATE)){

            timePane.setVisible(false);
            datePane.setVisible(true);
            monthPane.setVisible(false);
            yearPane.setVisible(false);
            yearPane.setStep(100);
            yearPane.setShowRange(true);

        }else{

            getChildren().remove(timePane);
            double width = getWidth(),
                    tw = width * 180/470,
                    height = getHeight();
            timePane.setPrefWidth(tw);
            timePane.setPrefHeight(height);

            timePane.setVisible(true);
            datePane.setVisible(true);
            monthPane.setVisible(false);
            yearPane.setVisible(false);

            yearPane.setStep(100);
            yearPane.setShowRange(true);

            getChildren().add(timePane);
//            getChildren().add(0, timePane);


        }

    }

    private double getWidth(){
        double width = control.getPrefWidth();;
        if(width == -1){
            width = control.prefWidth(-1);
        }

        if(control.getDateType().equals(DateType.DATETIME)){
            width = Math.max(width, 470);
        }else{
            width = Math.max(width, 290);
        }
       return width;
    }

    public double getHeight(){
        double height = control.getPrefHeight();
        if(height == -1){
            height = control.prefHeight(-1);
        }

        return Math.max(height, 260);
    }

    private void buildTimePane(){
        if(timePane == null){
            timePane = new TimePane(control.getDate().toLocalTime());
            timePane.colorTypeProperty().bind(control.colorTypeProperty());
            timePane.timeProperty().addListener((ob, ov, nv)->{
                LocalDateTime date = control.getDate();
                int hour = nv.getHour();
                int min = nv.getMinute();
                int sec = nv.getSecond();
                LocalDateTime newDate = date.withHour(hour).withMinute(min).withSecond(sec);
                control.setDate(newDate);
            });
        }

        if(!getChildren().contains(timePane)){
            getChildren().add(timePane);
        }
    }


    private void buildDatePane(){
        if(datePane == null){
            datePane = new DateBodyPane();
            datePane.colorTypeProperty().bind(control.colorTypeProperty());
            datePane.setShowDate(control.getDate());
            datePane.showDateProperty().bindBidirectional(control.dateProperty());
            datePane.selectedDateProperty().bindBidirectional(control.selectedDateProperty());
            datePane.prefWidthProperty().bind(Bindings.createDoubleBinding(widthBind, control.prefWidthProperty()));
            datePane.prefHeightProperty().bind(control.prefHeightProperty());

            datePane.setOnYearTextAction(e ->{
                datePane.setVisible(false);
                if(yearPane == null ){
                    buildYearPane();
                }
                yearPane.setVisible(true);
            });

            datePane.setOnMonthTextAction(e -> {
                datePane.setVisible(false);
                if(monthPane == null){
                    buildMonthPane();
                }
                monthPane.setVisible(true);
            });
        }

        if(!getChildren().contains(datePane))
            getChildren().add(datePane);
    }

    private Callable<Double> widthBind = new Callable<Double>() {
        @Override
        public Double call() throws Exception {
            double width = getWidth();
            if(control.getDateType().equals(DateType.DATETIME)){
                width = width - (470d-290d)/470d * width;
            }
            return width;
        }
    };

    private void buildYearPane(){
        if(yearPane == null){
            yearPane = new YearBodyPane();
            yearPane.colorTypeProperty().bind(control.colorTypeProperty());
            yearPane.prefWidthProperty().bind(Bindings.createDoubleBinding(widthBind, control.prefWidthProperty()));
            yearPane.prefHeightProperty().bind(control.prefHeightProperty());

            yearPane.setVisible(false);
            yearPane.setItemCallback(yearItemSelectedCallBack);
        }
        if(!getChildren().contains(yearPane))
            getChildren().add(yearPane);
    }

    private void buildMonthPane(){

        if(monthPane == null){
            monthPane = new MonthBodyPane();
            monthPane.colorTypeProperty().bind(control.colorTypeProperty());
            monthPane.prefWidthProperty().bind(Bindings.createDoubleBinding(widthBind, control.prefWidthProperty()));
            monthPane.prefHeightProperty().bind(control.prefHeightProperty());
            monthPane.setVisible(false);
            monthPane.setItemCallback(monthItemSelectedCallBack);
            monthPane.setOnTextAction(e -> {
                buildYearPane();
                monthPane.setVisible(false);
                yearPane.setVisible(true);
            });
        }

        if(!getChildren().contains(monthPane))
            getChildren().add(monthPane);
    }

    /**
     * 月份面板选中事件
     */
    private CallBack<XmNodeButton> monthItemSelectedCallBack = e -> {
        SimpleMonth value = (SimpleMonth) e.getValue();
        LocalDateTime ldt = control.getDate();

        LocalDateTime date = null;
        try{ //如果是2月份，当天是31号，就会报错，
            date = LocalDateTime.of(value.getYear(), value.getMonth(), ldt.getDayOfMonth() , ldt.getHour(), ldt.getMinute(), ldt.getSecond());
        }catch (Exception e1){
            date = LocalDateTime.of(value.getYear(), value.getMonth(), 1 , ldt.getHour(), ldt.getMinute(), ldt.getSecond());
        }

        control.setDate(date);

        if(!DateType.MONTH.equals(control.getDateType())){
            datePane.setVisible(true);
            monthPane.setVisible(false);
        }

    };

    private CallBack<XmNodeButton> yearItemSelectedCallBack = e->{

        if(DateType.YEAR.equals(control.getDateType())){
            control.setDate(control.getDate().withYear((Integer) e.getValue()));
        }else{
            yearPane.setVisible(false);
            if(monthPane == null){
                buildMonthPane();
            }
            monthPane.setSelectedMonthProperty(new SimpleMonth(yearPane.getSelectedYear(), control.getDate().getMonthValue()));
            monthPane.setVisible(true);
        }

    };

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width = super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset);
        if(control.getDateType().equals(DateType.DATETIME)){
            width = Math.max(width, 470);
        }else{
            width = Math.max(width, 290);
        }
        return width;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 260;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);

        double width = getWidth(),
                height = getHeight();

        DateType dateType = control.getDateType();
        if(dateType.equals(DateType.DATETIME)){
            double x = width - (470d-290d)/470d * width ,
                    y = 0,
                    tw = (width - x);
            timePane.setPrefWidth(tw);
            timePane.setPrefHeight(height);
            layoutInArea(timePane, x, y, tw, height, 0 , HPos.CENTER, VPos.CENTER);
        }else if(dateType.equals(DateType.TIME)){
            timePane.setPrefWidth(width);
            timePane.setPrefHeight(height);
            layoutInArea(timePane, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
        }

//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
    }
}
