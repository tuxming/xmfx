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
import com.xm2013.jfx.control.svg.XmSVGView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 年份面板，用于选择年份
 */
public class YearBodyPane extends Pane {

    /**
     * 选中的数据
     */
    private IntegerProperty selectedYearProperty = new SimpleIntegerProperty();

    /**
     * 缓存每一年的items
     */
    private List<List<XmNodeButton>> years = new ArrayList<>();

    /**
     * 选择的年份的item
     */
    private XmNodeButton prevMonthBtn = null;

    private Paint textColor = Color.web("#888888"),
            textColorHover = Color.web("#333333");

    /**
     * 上一年箭头按钮
     */
    private XmSVGView prevBtn;
    /**
     * 下一年箭头按钮
     */
    private XmSVGView nextBtn;

    /**
     * 顶部显示的年份文字控件
     */
    private Text showText;

    /**
     * 分割线条
     */
    private Line line = null;

    /**
     * 点击向左，向右的步长。
     */
    private int step = 100;
    /**
     * 是显示当前年份，还是显示可选年份的范围
     */
    private BooleanProperty showRange = new SimpleBooleanProperty(true);

    /* ---------------------------------- 构造函数 / 初始化方法 ----------------------------------- */
    public YearBodyPane(){
        selectedYearProperty.set(LocalDate.now().getYear());

        this.init();
    }

    public YearBodyPane(int year){
        selectedYearProperty.set(year);
        this.init();
    }

    private void init(){
//        setStyle("-fx-background-color: #ff00ff");
        getStyleClass().add("xm-year-body-pane");
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

        showText = new Text();
        showText.setFont(Font.font(showText.getFont().getFamily(), FontWeight.BOLD, 13));
        showText.textProperty().bind(Bindings.createStringBinding(()->{
            int year = selectedYearProperty.get();
            if(showRange.get()){
                int mod = year % 10;
                return (year - mod) + " - "+(year+(10-mod));
            }else{
                return year+"年";
            }
        }, selectedYearProperty, showRange));

        line = new Line();
        line.setFill(Color.web("#88888855"));
        line.setStartY(40);
        line.setStartX(5);
        line.endXProperty().bind(prefWidthProperty().subtract(5));
        line.setEndY(40);
        line.setStrokeWidth(0.5);

        getChildren().addAll(prevBtn, nextBtn, showText, line);

        //构建可供选择的年份条目组件
        for(int i=0; i<4; i++){
            List<XmNodeButton> rows = new ArrayList<>();
            years.add(rows);
            for(int j=0; j<3; j++){
                XmNodeButton btn = new XmNodeButton("");
                btn.colorTypeProperty().bind(colorTypeProperty());
                btn.setTextAlign(Pos.CENTER);
                btn.setOnAction(new ItemClickHandler(btn));
                rows.add(btn);
                getChildren().add(btn);
            }
        }

        updateYearItems();

        selectedYearProperty.addListener(selectedChangeListener);
        prevBtn.hoverProperty().addListener(new ArrowHoverListener(prevBtn));
        nextBtn.hoverProperty().addListener(new ArrowHoverListener(nextBtn));
        prevBtn.setOnMouseClicked(clickPrevYearHandler);
        nextBtn.setOnMouseClicked(clickNextYearHandler);
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
            Integer value = (Integer) item.getValue();
            if(value != null){
                selectedYearProperty.set(value);
            }

            if(prevMonthBtn !=null){
                prevMonthBtn.setFilled(false);
            }

            item.setFilled(true);
            prevMonthBtn = item;

            if(getItemCallback()!=null)
                getItemCallback().call(item);
        }
    }

    /**
     * 上一年/下一年按钮的hover监听
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
     * 上一年按钮点击事件
     */
    private final EventHandler<MouseEvent> clickPrevYearHandler = e->{
        int year = selectedYearProperty.get();
        selectedYearProperty.set(year-step);
    };

    /**
     * 下一年按钮点击事件
     */
    private final EventHandler<MouseEvent> clickNextYearHandler = e->{
        int year = selectedYearProperty.get();
        selectedYearProperty.set(year+ step);
    };

    /**
     * 选中的年份变化监听
     */
    private final ChangeListener<Number> selectedChangeListener = (ob, ov, nv)->{
        updateYearItems();
    };

    /* ---------------------------------- properties/getter/setter ----------------------------------- */
    public void setStep(int step){
        this.step = step;
    }

    public BooleanProperty showRangeProperty() {
        return showRange;
    }

    public void setShowRange(boolean showRange) {
        this.showRange.set(showRange);
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


    public int getSelectedYear() {
        return selectedYearProperty.get();
    }

    public IntegerProperty selectedYearProperty() {
        return selectedYearProperty;
    }

    public void setSelectedProperty(int selectedYearProperty) {
        this.selectedYearProperty.set(selectedYearProperty);
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

    /**
     * 重写布局函数，支持相对布局，
     */
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

        for (List<XmNodeButton> row : years) {
            for (XmNodeButton btn : row) {
                btn.setPrefWidth(cellWidth);
                btn.setPrefHeight(cellHeight);
                layoutInArea(btn, cellX, cellY, cellWidth, cellHeight, 0, HPos.CENTER, VPos.CENTER);
                cellX += hspace + cellWidth;
            }
            cellX = hspace;
            cellY+=vspace+cellHeight;
        }

    }

    /* ---------------------------------- 私有函数 ----------------------------------- */

    /**
     * 更新供选择的年份条目组件内容
     */
    public void updateYearItems(){

        Integer currYear = selectedYearProperty.get();

        int year = currYear - (currYear%10+1);

        for (List<XmNodeButton> row : years) {
            for (XmNodeButton btn : row) {
                btn.setText(year+"");
                if(year == currYear){
                    btn.setFilled(true);
                    prevMonthBtn = btn;
                }else{
                    btn.setOutside(false);
                    btn.setFilled(false);
                    btn.setActive(false);
                }

                btn.setValue(year);

                year ++;
            }
        }
    }

}
