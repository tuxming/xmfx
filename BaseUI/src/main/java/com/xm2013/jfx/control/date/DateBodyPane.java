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
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xm2013.jfx.common.FxKit.*;

public class DateBodyPane extends Pane {

    private static String[] weekStrs = {"一", "二", "三", "四", "五", "六", "日"};

    /**
     * 缓存星期"一", "二", "三", "四", "五", "六", "日"的控件节点
     */
    private List<Text> weeks = new ArrayList<>();

    /**
     * 缓存每一天的items
     */
    private List<List<XmNodeButton>> days = new ArrayList<>();

    /**
     * 上一年箭头按钮
     */
    public XmSVGIcon prevYearBtn;
    /**
     * 上一月箭头按钮
     */
    public XmSVGIcon prevMonthBtn;
    /**
     * 下一年箭头按钮
     */
    public XmSVGIcon nextYearBtn;
    /**
     * 下一年箭头按钮
     */
    public XmSVGIcon nextMonthBtn;

    /**
     * 显示年份文本控件
     */
    public XmNodeButton yearTxt;
    /**
     * 显示月份文本控件
     */
    public XmNodeButton monthTxt;

    /**
     * 中间分隔线条
     */
    private Line line = null;

    private Paint textColor = Color.web("#888888"),
            textColorHover = Color.web("#333333");

    /**
     * 今天
     */
    private ObjectProperty<LocalDateTime> today = new SimpleObjectProperty<>(LocalDateTime.now());
    /**
     * 当前所在日期
     */
    private ObjectProperty<LocalDateTime> showDate = new SimpleObjectProperty<>();

    /**
     * 选中的日期
     */
    private ObjectProperty<LocalDateTime> selectedDate = new SimpleObjectProperty<>();

    /**
     * 选择的日期的item
     */
    private XmNodeButton selectedBtn = null;

    /**
     * hover的日期
     * @param date
     */
    private ObjectProperty<LocalDateTime> hoverDate = new SimpleObjectProperty<>();

    /* ---------------------------------- 构造函数 / 初始化方法 ----------------------------------- */
    public DateBodyPane(){
        this(null, null);
    }

    /**
     *
     * @param date     要显示的月份的日期
     * @param selectedDate 选中的日期
     */
    public DateBodyPane(LocalDateTime date, LocalDateTime selectedDate){

        this.showDate.set(date);
        this.selectedDate.set(selectedDate);
        this.getStyleClass().add("xm-date-body");
        this.setPrefWidth(290);
        this.setPrefHeight(259);

        prevYearBtn = new XmSVGIcon(DOUBLE_ARROW_LEFT);
        prevYearBtn.setSize(18);
        prevYearBtn.setColor(textColor);

        prevMonthBtn = new XmSVGIcon(ARROW_LEFT);
        prevMonthBtn.setSize(14);
        prevMonthBtn.setColor(textColor);

        nextYearBtn = new XmSVGIcon(DOUBLE_ARROW_RIGHT);
        nextYearBtn.setSize(18);
        nextYearBtn.setColor(textColor);

        nextMonthBtn = new XmSVGIcon(ARROW_RIGHT);
        nextMonthBtn.setSize(14);
        nextMonthBtn.setColor(textColor);

        yearTxt = new XmNodeButton("");
        Text textNode = (Text) yearTxt.getNode();
        textNode.setFont(Font.font(textNode.getFont().getFamily(), FontWeight.BOLD, 13));
        yearTxt.setHoverStyle(2);
        yearTxt.colorTypeProperty().bind(colorTypeProperty());
        monthTxt = new XmNodeButton("");
        Text monthTxtNode = (Text) monthTxt.getNode();
        monthTxtNode.setFont(Font.font(monthTxtNode.getFont().getFamily(), FontWeight.BOLD, 13));
        monthTxt.setHoverStyle(2);
        monthTxt.colorTypeProperty().bind(colorTypeProperty());

        line = new Line();
        line.setFill(Color.web("#88888855"));
        line.setStartY(40);
        line.setStartX(5);
        line.endXProperty().bind(prefWidthProperty().subtract(5));
        line.setEndY(40);
        line.setStrokeWidth(0.5);

        getChildren().addAll(prevYearBtn, prevMonthBtn, yearTxt, monthTxt, nextMonthBtn, nextYearBtn, line);

        //构建星期
        for(int i=0; i<weekStrs.length; i++){
            Text text = new Text(weekStrs[i]);
            text.setFont(Font.font(14));
            text.setFill(Color.web("#666666"));
            getChildren().add(text);
            weeks.add(text);
        }

        buildDayItems();
        if(showDate.get()!=null){
            updateDays();
            showLabel(showDate.get());
        }
        resetDayItems();

        showDateProperty().addListener(showDateListener);

        prefWidthProperty().addListener(needUpdateItemListener);
        prefHeightProperty().addListener(needUpdateItemListener);
        colorTypeProperty().addListener(needUpdateItemListener);
        prevYearBtn.hoverProperty().addListener(new ArrowBtnHoverListener(prevYearBtn));
        prevYearBtn.setOnMouseClicked(new ArrowBtnClickHandler(-1, 0));
        prevMonthBtn.hoverProperty().addListener(new ArrowBtnHoverListener(prevMonthBtn));
        prevMonthBtn.setOnMouseClicked(new ArrowBtnClickHandler(-1, 1));
        nextYearBtn.hoverProperty().addListener(new ArrowBtnHoverListener(nextYearBtn));
        nextYearBtn.setOnMouseClicked(new ArrowBtnClickHandler(1, 0));
        nextMonthBtn.hoverProperty().addListener(new ArrowBtnHoverListener(nextMonthBtn));
        nextMonthBtn.setOnMouseClicked(new ArrowBtnClickHandler(1, 1));
        onMonthTextActionProperty().addListener(onMonthTextActionListener);
        onYearTextActionProperty().addListener(onYearTextActionListener);
    }

    /* ---------------------------------- 事件处理函数/监听函数 ----------------------------------- */

    /**
     * 当前日期变化监听
     */
    private ChangeListener<LocalDateTime> showDateListener = (ob, ov, nv)->{
        if(nv !=null){
            if(ov!=null){

                int oldMonth = ov.getMonthValue();
                int currMonth = nv.getMonthValue();

                if(oldMonth != currMonth){
                    updateDays();
                }
            }else{
                updateDays();
            }
            showLabel(nv);
        }
    };

    /**
     * 需要更新所有天数
     */
    private ChangeListener<Object> needUpdateItemListener = (ob, ov, nv) -> {
        resetDayItems();
    };

    /**
     * 监听点击箭头按钮时间的处理函数
     */
    class ArrowBtnClickHandler implements EventHandler<MouseEvent>{
        private long addMillis =0;
        //0-年，1-月
        private int type = -1;
        public ArrowBtnClickHandler(long addMillis, int type){
            this.addMillis = addMillis;
            this.type = type;
        }

        @Override
        public void handle(MouseEvent event) {
            LocalDateTime temp = showDate.get();
            if(temp == null){
                temp = LocalDateTime.now();
            }
            if(type == 0){
                showDate.set(temp.plusYears(addMillis));
            }else if(type == 1){
                showDate.set(temp.plusMonths(addMillis));
            }
            updateDays();
            showLabel(showDate.get());
        }
    }

    class ArrowBtnHoverListener implements ChangeListener<Boolean>{
        private XmSVGIcon icon = null;
        public ArrowBtnHoverListener(XmSVGIcon icon){
            this.icon = icon;
        }
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                icon.setColor(textColorHover);
            }else{
                icon.setColor(textColor);
            }
        }
    }

    /**
     * 显示显示年份文本点击回调函数监听事件
     */
    private ChangeListener<EventHandler<MouseEvent>> onYearTextActionListener = (ob, ov, nv)->{
        if(ov!=null){
            yearTxt.removeEventFilter(MouseEvent.MOUSE_CLICKED, ov);
        }
        if(nv!=null){
            yearTxt.addEventFilter(MouseEvent.MOUSE_CLICKED, nv);
        }
    };

    /**
     * 显示月份文本点击回调函数监听事件
     */
    private ChangeListener<EventHandler<MouseEvent>> onMonthTextActionListener = (ob, ov, nv)->{
        if(ov!=null){
            monthTxt.removeEventFilter(MouseEvent.MOUSE_CLICKED, ov);
        }
        if(nv!=null){
            monthTxt.addEventFilter(MouseEvent.MOUSE_CLICKED, nv);
        }
    };

    /* ---------------------------------- 私有函数 ----------------------------------- */

    /**
     * 设置显示文本，年月
     * @param date
     */
    private void showLabel(LocalDateTime date){
        yearTxt.setText(date.getYear()+"年");
        monthTxt.setText((date.getMonthValue())+"月");
    }

    /**
     * 构建每一天的按钮控件
     */
    private void buildDayItems(){
        for(int i=0; i<6; i++){

            List<XmNodeButton> rowBtns = new ArrayList<>();
            days.add(i, rowBtns);

            for(int j=0; j<7; j++){
                XmNodeButton dayBtn = new XmNodeButton("00");
                dayBtn.setTextAlign(Pos.CENTER);
                dayBtn.setYoffset(1);
                Text textNode = (Text) dayBtn.getNode();
                textNode.setFont(Font.font(textNode.getFont().getFamily(), 14));

                //选中
                dayBtn.setOnAction(e->{
                    LocalDateTime sd = (LocalDateTime) dayBtn.getValue();
                    dayBtn.setFilled(true);
                    if(selectedBtn!=null && selectedBtn!=dayBtn){
                        selectedBtn.setFilled(false);
                    }
                    selectedBtn = dayBtn;
                    selectedDate.set(sd);
                    showDate.set(sd);
                });

                getChildren().add(dayBtn);
                rowBtns.add(dayBtn);
            }
        }
    }

    /**
     * 重置按钮外观，
     */
    private void resetDayItems(){
        double width = prefWidth(-1);
        double height = prefHeight(-1);

        double hspace = 8,
                vspace = 10,
                cellWidth = (width - hspace*8)/7,
                cellHeight = (height - vspace*7)/6;

        for(List<XmNodeButton> rows : days){
            for (XmNodeButton dayBtn : rows) {
                dayBtn.setPrefWidth(cellWidth);
                dayBtn.setPrefHeight(cellHeight);
                dayBtn.setColorType(getColorType());
                CallBack<XmNodeButton> cellCallBack = cellUpdateCallbackProperty().get();
                if(cellCallBack!=null){
                    cellCallBack.call(dayBtn);
                }
            }
        }

    }

    /**
     * 更新按钮状态，内容
     */
    private void updateDays(){

        LocalDateTime todayLdt = today.get();
        int todayDay = todayLdt.getDayOfMonth();
        int todayMonth = todayLdt.getMonthValue();
        int todayYear = todayLdt.getYear();

        LocalDateTime selected = null;
        if(selectedDate.get()!=null){
            selected = selectedDate.get();
        }

        LocalDateTime date = showDate.get();
        date = date.withDayOfMonth(1);

        int month = date.getMonthValue();
        int dayOfWeek = date.getDayOfWeek().getValue();

        //(7-(2-2)) -7  //星期一,前面还有0天
        // 7-(3-2)  -7  //星期二，前面还有1天
        // 7-(4-2) - 7  //星期三，前面还有2天
        // 7-(5-2) - 7  //星期四，前面还有3天
        // 7-(6-2) - 7  //星期五，前面还有4天
        // 7-(7-2) - 7  //星期六，前面还有5天
        //   //星期日，前面还有6天

//        if(dayOfWeek == 1){
//            date = date.plusDays(-6);
//        }else{
//            date = date.plusDays(-(dayOfWeek - 2));
//        }

        date = date.plusDays(-(dayOfWeek-1));

        for(List<XmNodeButton> rows : days){
            for (XmNodeButton dayBtn : rows) {
                int y = date.getYear();
                int m = date.getMonthValue();
                int d = date.getDayOfMonth();
                String text = d < 10? "0"+ d : d+"";

                dayBtn.setText(text);
                dayBtn.setTextAlign(Pos.CENTER);
                dayBtn.setYoffset(1);
                dayBtn.setValue(LocalDateTime.of(y, m, d, date.getHour(), date.getMinute(), date.getSecond()));
                Text dayText = (Text) dayBtn.getNode();
                dayText.setFont(Font.font(dayText.getFont().getFamily(), 14));
                if(m != month){
                    dayBtn.setOutside(true);
                }else {
                    dayBtn.setOutside(false);
                    if (todayYear == y && todayMonth == m && todayDay == d) {
                        dayBtn.setActive(true);
                    } else if (selected!=null && selected.getDayOfMonth() == d) {
                        dayBtn.setFilled(true);
                        if (selectedBtn != null) {
                            selectedBtn.setFilled(false);
                        }
                        selectedBtn = dayBtn;
                    } else {
                        dayBtn.setActive(false);
                        dayBtn.setFilled(false);
                    }
                }
                CallBack<XmNodeButton> DateBodyPaneCallBack = cellUpdateCallbackProperty().get();
                if (DateBodyPaneCallBack != null) {
                    DateBodyPaneCallBack.call(dayBtn);
                }

                date = date.plusDays(1);
            }
        }
    }

    /* ---------------------------------- properties/getter/setter ----------------------------------- */

    /**
     * 显示面板的日期
     * @return LocalDateTime
     */
    public LocalDateTime getShowDate() {
        return showDate.get();
    }
    /**
     * 显示面板的日期
     * @return ObjectProperty
     */
    public ObjectProperty<LocalDateTime> showDateProperty() {
        return showDate;
    }
    /**
     * 显示面板的日期
     * @param showDate LocalDateTime
     */
    public void setShowDate(LocalDateTime showDate) {
        this.showDate.set(showDate);
    }

    /**
     * 选中的日期
     * @return LocalDateTime
     */
    public LocalDateTime getSelectedDate() {
        return selectedDate.get();
    }
    /**
     * 选中的日期
     * @return ObjectProperty
     */
    public ObjectProperty<LocalDateTime> selectedDateProperty() {
        return selectedDate;
    }
    /**
     * 选中的日期
     * @param selectedDate LocalDateTime
     */
    public void setSelectedDate(LocalDateTime selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    /**
     * 每一天按钮的文本排版位置
     */
    private ObjectProperty<HPos> titleAlign;
    public HPos getTitleAlign() {
        return titleAlignProperty().get();
    }
    public ObjectProperty<HPos> titleAlignProperty() {
        if(titleAlign == null){
            titleAlign = FxKit.newProperty(HPos.CENTER, StyleableProperties.TITLE_ALIGN, this, "titleAlign");
        }
        return titleAlign;
    }
    public void setTitleAlign(HPos titleAlign) {
        this.titleAlignProperty().set(titleAlign);
    }

    /**
     * 按钮更新回调函数
     */
    private ObjectProperty<CallBack<XmNodeButton>> cellUpdateCallback;
    public CallBack<XmNodeButton> getCellUpdateCallback() {
        return cellUpdateCallbackProperty().get();
    }
    public ObjectProperty<CallBack<XmNodeButton>> cellUpdateCallbackProperty() {
        if(cellUpdateCallback == null)
            cellUpdateCallback = new SimpleObjectProperty<>(null);
        return cellUpdateCallback;
    }
    public void setCellUpdateCallback(CallBack<XmNodeButton> cellUpdateCallback) {
        if(cellUpdateCallback!=null){
            for (List<XmNodeButton> row : days){
                for (XmNodeButton dayBtn : row) {
                    cellUpdateCallback.call(dayBtn);
                }
            }
        }
        this.cellUpdateCallbackProperty().set(cellUpdateCallback);
    }

    /**
     * 点击顶部的显示年份回调事件
     */
    private ObjectProperty<EventHandler<MouseEvent>> onYearTextAction = new SimpleObjectProperty<>(e->{});
    public final ObjectProperty<EventHandler<MouseEvent>> onYearTextActionProperty() { return onYearTextAction; }
    public final void setOnYearTextAction(EventHandler<MouseEvent> value) { onYearTextActionProperty().set(value); }
    public final EventHandler<MouseEvent> getOnYearTextAction() { return onYearTextActionProperty().get(); }

    /**
     * 点击顶部的显示月份回调事件
     */
    private ObjectProperty<EventHandler<MouseEvent>> onMonthTextAction = new SimpleObjectProperty<>(e->{});
    public final ObjectProperty<EventHandler<MouseEvent>> onMonthTextActionProperty() { return onMonthTextAction; }
    public final void setOnMonthTextAction(EventHandler<MouseEvent> value) { onMonthTextActionProperty().set(value); }
    public final EventHandler<MouseEvent> getOnMonthTextAction() { return onMonthTextActionProperty().get(); }


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

    /* ---------------------------------- override ----------------------------------- */

    @Override
    protected double computeMinWidth(double height) {
        return 290;
    }

    @Override
    protected double computeMinHeight(double width) {
        return 259;
    }

    @Override
    protected void layoutChildren() {

        double width = this.prefWidth(-1),
                height = this.prefHeight(-1),
                topHeight = 40;

        double prevYearBtnHeight = prevYearBtn.getSize(),
                prevYearBtnWidth = prevYearBtn.getSize(),
                prevYearBtnX = 10,
                prevYearBtnY = (topHeight - prevYearBtnHeight)/2;
        layoutInArea(prevYearBtn, prevYearBtnX, prevYearBtnY, prevYearBtnWidth, prevYearBtnHeight, 0, HPos.CENTER, VPos.CENTER);

//        System.out.println(String.format("prevYearBtn(%f, %f, %f, %f)", prevYearBtnX, prevYearBtnY, prevYearBtnWidth, prevYearBtnHeight));

        double prevMonthBtnWidth = prevMonthBtn.getSize(),
                prevMonthBtnHeight = prevMonthBtn.getSize(),
                prevMonthBtnX = prevYearBtnX + 10 + prevYearBtnWidth,
                prevMonthBtnY = (topHeight - prevMonthBtnWidth) / 2;
        layoutInArea(prevMonthBtn, prevMonthBtnX, prevMonthBtnY, prevMonthBtnWidth, prevMonthBtnHeight, 0, HPos.CENTER, VPos.CENTER);

        double nextMonthBtnWidth = nextMonthBtn.getSize(),
                nextMonthBtnHeight = nextMonthBtn.getSize(),
                nextYearBtnWidth = nextYearBtn.getSize(),
                nextYearBtnHeight = nextYearBtn.getSize(),
                nextMonthBtnX = width - nextYearBtnWidth - nextMonthBtnWidth - 20,
                nextMonthBtnY = (topHeight - nextMonthBtnHeight) / 2,
                nextYearBtnX = nextMonthBtnX + 10 + nextMonthBtnWidth,
                nextYearBtnY = (topHeight - nextYearBtnHeight) / 2;
        layoutInArea(nextMonthBtn, nextMonthBtnX, nextMonthBtnY, nextMonthBtnWidth, nextMonthBtnHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(nextYearBtn, nextYearBtnX, nextYearBtnY, nextYearBtnWidth, nextMonthBtnHeight, 0, HPos.CENTER, VPos.CENTER);

        double yearTxtWidth = yearTxt.prefWidth(-1),
                yearTxtHeight = yearTxt.prefHeight(-1),
                monthTxtWidth = monthTxt.prefWidth(-1),
                monthTxHeight = monthTxt.prefHeight(-1),
                yearTxtX = (width - yearTxtWidth - monthTxtWidth) / 2,
                yearTxtY = (topHeight - yearTxtHeight) / 2,
                monthTxtX = yearTxtX +  yearTxtWidth,
                monthTxtY = (topHeight - monthTxHeight) / 2;
        layoutInArea(yearTxt, yearTxtX, yearTxtY, yearTxtWidth, yearTxtHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(monthTxt, monthTxtX, monthTxtY, monthTxtWidth, monthTxHeight, 0, HPos.CENTER, VPos.CENTER);

        double hspace = 8,
                vspace = 4,
                titleHeight = weeks.get(0).prefHeight(-1) + 14,
                cellWidth = (width - hspace*7)/7,
                cellHeight = (height - vspace*8 - titleHeight - topHeight)/6,
                startX = hspace,
                startY = vspace + topHeight;

        HPos hpos = titleAlignProperty().get();
        for (int i=0; i<weeks.size(); i++) {
            Text text = weeks.get(i);
            layoutInArea(text, startX, startY, cellWidth, titleHeight, 0, hpos, VPos.CENTER);
            startX += hspace+cellWidth;
        }

        startY = vspace + topHeight+ vspace + titleHeight + vspace;

        for(int i=0; i<days.size(); i++){
            startX = hspace;
            List<XmNodeButton> rows = days.get(i);
            for(int j=0; j<rows.size(); j++){
                XmNodeButton btn = rows.get(j);
                btn.setPrefWidth(cellWidth);
                btn.setPrefHeight(cellHeight);
                layoutInArea(btn, startX, startY, cellWidth, cellHeight, 0, HPos.CENTER, VPos.CENTER);
                startX += cellWidth + hspace;
            }
            startY += vspace + cellHeight;
        }

    }

    /* ------------------------- css style ---------------------- */
    private static class StyleableProperties {

        /**
         * -fx-title-align:left, center, right
         */
        private static final CssMetaData<DateBodyPane,HPos> TITLE_ALIGN =
                new CssMetaData<DateBodyPane,HPos>("-fx-title-align",
                        new EnumConverter<HPos>(HPos.class),
                        HPos.CENTER) {

                    @Override
                    public boolean isSettable(DateBodyPane node) {
                        return node.titleAlign == null || !node.titleAlign.isBound();
                    }

                    @Override
                    public StyleableProperty<HPos> getStyleableProperty(DateBodyPane node) {
                        return (StyleableProperty<HPos>) node.titleAlignProperty();
                    }

                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<CssMetaData<? extends Styleable, ?>>(Pane.getClassCssMetaData());
            styleables.add(TITLE_ALIGN);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the
     * {@code CssMetaData} of its superclasses.
     * @return the {@code CssMetaData}
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return DateBodyPane.StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     *
     * @since JavaFX 8.0
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

}
