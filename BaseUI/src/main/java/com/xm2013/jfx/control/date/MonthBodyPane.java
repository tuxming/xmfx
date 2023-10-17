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
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.svg.XmSVGView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.xm2013.jfx.common.CallBack;
import javafx.scene.text.Text;

public class MonthBodyPane extends Pane {

    //当前月分
    ObjectProperty<SimpleMonth> monthProperty = new SimpleObjectProperty<>();

    /**
     * 选择的月份的item
     */
    private XmNodeButton prevSelectedBtn;

    /**
     * 缓存每一月的items
     */
    List<List<XmNodeButton>> months = new ArrayList<>();

    private Paint textColor = Color.web("#888888"),
            textColorHover = Color.web("#333333");

    /**
     * 上一月箭头按钮
     */
    private XmSVGView prevBtn;
    /**
     * 下一月箭头按钮
     */
    private XmSVGView nextBtn;

    /**
     * 顶部显示的月份文字控件
     */
    private XmNodeButton showText;

    /**
     * 中间分隔线条
     */
    private Line line = null;

    /* ---------------------------------- 构造函数 / 初始化方法 ----------------------------------- */

    public MonthBodyPane(){
        this.monthProperty.set(new SimpleMonth(LocalDate.now()));

        this.init();
    }

    public MonthBodyPane(LocalDate selectedMonth){
        this.monthProperty.set(new SimpleMonth(selectedMonth));
        this.init();
    }

    public MonthBodyPane(SimpleMonth selectedMonth){
        this.monthProperty.set(selectedMonth);
        this.init();
    }

    private void init() {

        getStyleClass().add("xm-month-body");
        this.setPrefWidth(290);
        this.setPrefHeight(239);
        this.setMinWidth(290);
        this.setMinHeight(239);

        prevBtn = new XmSVGView(FxKit.ARROW_LEFT);
        prevBtn.setSize(14);
        prevBtn.setColor(textColor);

        nextBtn = new XmSVGView(FxKit.ARROW_RIGHT);
        nextBtn.setSize(14);
        nextBtn.setColor(textColor);

        showText = new XmNodeButton("");
        Text textNode = (Text) showText.getNode();
        textNode.setFont(Font.font(textNode.getFont().getFamily(), FontWeight.BOLD, 13));
        showText.setHoverStyle(2);
        showText.colorTypeProperty().bind(colorTypeProperty());
        showText.textProperty().bind(Bindings.createStringBinding(()->{
            SimpleMonth sm = selectedMonthProperty().get();
            return sm.getYear()+"年";
        }, selectedMonthProperty()));

        line = new Line();
        line.setFill(Color.web("#88888855"));
        line.setStartY(40);
        line.setStartX(5);
        line.endXProperty().bind(prefWidthProperty().subtract(5));
        line.setEndY(40);
        line.setStrokeWidth(0.5);

        getChildren().addAll(prevBtn, nextBtn, showText, line);

        for(int i=0; i<4; i++){
            List<XmNodeButton> rows = new ArrayList<>();
            months.add(rows);
            for(int j=0; j<3; j++){
                XmNodeButton btn = new XmNodeButton((i*3+(j+1))+"月");
                btn.colorTypeProperty().bind(colorTypeProperty());
                btn.setTextAlign(Pos.CENTER);
                btn.setOnAction(new ItemClickHandler(btn));
                rows.add(btn);
                getChildren().add(btn);
            }
        }

        updateMonthItems();

        monthProperty.addListener(selectedMonthListener);
        prevBtn.hoverProperty().addListener(new ArrowHoverListener(prevBtn));
        nextBtn.hoverProperty().addListener(new ArrowHoverListener(nextBtn));
        prevBtn.setOnMouseClicked(new ArrowMonthClickHandler(-1));
        nextBtn.setOnMouseClicked(new ArrowMonthClickHandler(1));
//        showText.hoverProperty().addListener(textHoverListener);
        onTextActionProperty().addListener(onTextActionListener);
    }

    /* ---------------------------------- 事件处理函数/监听函数 ----------------------------------- */

    /**
     * item的点击事件
     */
    class ItemClickHandler implements  EventHandler<ActionEvent>{
        private XmNodeButton item;
        public ItemClickHandler(XmNodeButton item){
            this.item = item;
        }
        @Override
        public void handle(ActionEvent event) {
            SimpleMonth value = (SimpleMonth) item.getValue();
            if(value != null){
                monthProperty.set(value);
            }

            if(prevSelectedBtn !=null){
                prevSelectedBtn.setFilled(false);
            }

            item.setFilled(true);
            prevSelectedBtn = item;

            if(getItemCallback()!=null){
                getItemCallback().call(item);
            }

        }
    }

    /**
     * 选择月份发生变化的监听事件
     */
    private final ChangeListener<SimpleMonth> selectedMonthListener = (ob, ov, nv)->{
        if(nv.getYear() != ov.getYear() || nv.getMonth() != nv.getMonth()){
            updateMonthItems();
        }
    };

    /**
     * 箭头按钮hover监听处理
     */
    class ArrowHoverListener implements ChangeListener<Boolean> {
        private XmSVGView svg;
        public ArrowHoverListener(XmSVGView svg){
            this.svg = svg;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                svg.setColor(textColorHover);
            }else{
                svg.setColor(textColor);
            }
        }
    }

    /**
     * 上一月，下一月的按钮，点击事件
     */
    class ArrowMonthClickHandler implements EventHandler<MouseEvent>{
        private int addYear;
        public ArrowMonthClickHandler(int addMonth){
            this.addYear = addMonth;
        }
        @Override
        public void handle(MouseEvent event) {
            SimpleMonth month = selectedMonthProperty().get();
            selectedMonthProperty().set(new SimpleMonth(month.getYear()+ addYear, month.getMonth()));
        }
    }

    /**
     * 显示文本hover监听
     */
//    private ChangeListener<Boolean> textHoverListener = (ob, ov, nv)->{
//        if(nv){
//            showText.setFill(getColorType().getFxColor());
//        }else{
//            showText.setFill(textColorHover);
//        }
//    };

    /**
     * 显示文本点击回调函数监听事件
     */
    private ChangeListener<EventHandler<MouseEvent>> onTextActionListener = (ob, ov, nv)->{
        if(ov!=null){
            showText.removeEventFilter(MouseEvent.MOUSE_CLICKED, ov);
        }
        if(nv!=null){
            showText.addEventFilter(MouseEvent.MOUSE_CLICKED, nv);
        }
    };

    /* ---------------------------------- properties/getter/setter ----------------------------------- */
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

    /**
     * 点击顶部的text回调事件
     * @return
     */
    private ObjectProperty<EventHandler<MouseEvent>> onTextAction = new SimpleObjectProperty<>(e->{});
    public final ObjectProperty<EventHandler<MouseEvent>> onTextActionProperty() { return onTextAction; }
    public final void setOnTextAction(EventHandler<MouseEvent> value) { onTextActionProperty().set(value); }
    public final EventHandler<MouseEvent> getOnTextAction() { return onTextActionProperty().get(); }

    /**
     * 选中的月份
     * @return SimpleMonth
     */
    public SimpleMonth getSelectedMonth(){
        return monthProperty.get();
    }

    public ObjectProperty<SimpleMonth> selectedMonthProperty(){
        return this.monthProperty;
    }

    public void setSelectedMonthProperty(SimpleMonth selectedMonthProperty) {
        this.monthProperty.set(selectedMonthProperty);
    }

    /**
     * 每一条点击的回调函数
     */
    private ObjectProperty<CallBack<XmNodeButton>> itemCallback;
    public CallBack<XmNodeButton> getItemCallback() {
        return itemCallbackProperty().get();
    }
    public ObjectProperty<CallBack<XmNodeButton>> itemCallbackProperty() {
        if(itemCallback == null){
            itemCallback = new SimpleObjectProperty<>(null);
        }
        return itemCallback;
    }
    public void setItemCallback(CallBack<XmNodeButton> itemCallback) {
        this.itemCallbackProperty().set(itemCallback);
    }
    /* ---------------------------------- override ----------------------------------- */

    @Override
    protected void layoutChildren() {

        double width = prefWidth(-1),
                height = prefHeight(-1),
                hspace = 10,
                topHeight = 40,

                prevHeight = prevBtn.prefHeight(-1),
                prevWidth = prevBtn.prefWidth(-1),
                prevX = hspace,
                prevY = (topHeight - prevHeight)/2,

                nextBtnWidth = nextBtn.prefWidth(-1),
                nextBtnHeight = nextBtn.prefHeight(-1),
                nextX = width - hspace - nextBtnWidth,
                nextY = (topHeight - nextBtnHeight)/2,

                showTextWidth = showText.prefWidth(-1),
                showTextHeight = showText.prefHeight(-1),
                showTextX = (width - showTextWidth) / 2,
                showTextY = (topHeight - showTextHeight)/2;

        layoutInArea(prevBtn,prevX, prevY,  prevWidth, prevHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(nextBtn, nextX, nextY, nextBtnWidth, nextBtnHeight,0, HPos.CENTER, VPos.CENTER);
        layoutInArea(showText, showTextX, showTextY,showTextWidth, showTextHeight, 0, HPos.CENTER, VPos.CENTER);

        double cellHeight = 40,
                cellWidth = (width - 4*hspace) / 3,
                vspace = (height - topHeight - 10  - cellHeight*4)/5;

//        System.out.println(String.format("width:%f, height:%f, cellW: %f, cellH: %f, hspace: %f, vspace:%f",
//                width, height, cellWidth, cellHeight, hspace, vspace));

        double cellX = hspace, cellY = 50 + vspace;

        for (List<XmNodeButton> row : months) {
            for (XmNodeButton btn : row) {
                btn.setPrefWidth(cellWidth);
                btn.setPrefHeight(cellHeight);
                layoutInArea(btn,cellX, cellY, cellWidth, cellHeight, 0, HPos.CENTER, VPos.CENTER);
                cellX += hspace + cellWidth;
            }
            cellX = hspace;
            cellY+=vspace+cellHeight;
        }
    }

    /* ---------------------------------- 私有函数 ----------------------------------- */

    /**
     * 更新所有条目状态，内融
     */
    public void updateMonthItems(){

        SimpleMonth simpleMonth = monthProperty.get();
        int year = simpleMonth.getYear();
        int month = 1;

        int index = 0;
        for (List<XmNodeButton> row : months) {
            for (XmNodeButton btn : row) {

                if(simpleMonth.getMonth() == (month+1)){
                    btn.setFilled(true);
                    prevSelectedBtn = btn;
                }else{
                    btn.setOutside(false);
                    btn.setFilled(false);
                    btn.setActive(false);
                }
                if(btn.getValue() == null){
                    btn.setValue(new SimpleMonth(year, index+1));
                }else{
                    SimpleMonth value = (SimpleMonth) btn.getValue();
                    value.setYear(year);
                    value.setMonth(index+1);
                }

                index ++;
            }
        }
    }
}
