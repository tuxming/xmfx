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
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.button.XmNodeButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.time.LocalDateTime;

public class DateSelectorPopup extends Popup {

    private final ObjectProperty<DateType> dateType;
    private XmDatePicker picker = null;
    private Pane pickerPane = null;

    private XmNodeButton enterBtn = null;

    private ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private boolean isShowEnterBtn = false;

    private CallBack<LocalDateTime> selectedCallback = null;
    private DateSelectorPopupEnterCallback enterCallback = null;
    public DateSelectorPopup(ObjectProperty<ColorType> colorType, ObjectProperty<DateType> dateType){
        this(colorType, dateType, false);
    }

    public DateSelectorPopup(ObjectProperty<ColorType> colorType, ObjectProperty<DateType> dateType, boolean isShowEnterBtn){
        this.dateType = dateType;
        this.isShowEnterBtn = isShowEnterBtn;

        picker = new XmDatePicker();
        picker.colorTypeProperty().bind(colorType);
        picker.dateTypeProperty().bind(dateType);
        picker.dateProperty().bindBidirectional(date);

        pickerPane = new Pane(picker);
        pickerPane.setStyle("-fx-background-color: white; ");

        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.THREE_PASS_BOX);
        shadow.setRadius(30);
        shadow.setColor(Color.web("#00000022"));
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);

        pickerPane.setLayoutX(0);
        pickerPane.setLayoutY(0);
        pickerPane.setEffect(shadow);

        if(isShowEnterBtn){
            enterBtn = new XmNodeButton("确定");
            enterBtn.colorTypeProperty().bind(colorType);
            enterBtn.setOnAction(e -> {
                if(enterCallback != null){
                    enterCallback.call(this, picker.getSelectedDate());
                }
            });
            pickerPane.getChildren().add(enterBtn);
        }

        setTypePane();
        dateType.addListener((ob, ov, nv)->{
            setTypePane();
        });

        date.addListener((ob, ov, nv)->{
            picker.setSelectedDate(nv);
        });

        picker.selectedDateProperty().addListener((ob, ov, nv)->{
            if(selectedCallback !=null){
                selectedCallback.call(nv);
            }

        });

        getContent().setAll(pickerPane);

    }

    public DateSelectorPopup setDate(LocalDateTime date){

        this.date.set(date);
        return this;
    }

    private void setTypePane(){
        if(DateType.DATETIME.equals(dateType.get())){
            picker.setPrefWidth(480);
            picker.setPrefHeight(270);

            setContentHeight(480,270);
        }else{
            picker.setPrefWidth(300);
            picker.setPrefHeight(270);

            setContentHeight(300,270);
        }
    }

    private void setContentHeight(double width, double height){
        if(isShowEnterBtn){
            double btnHeight = enterBtn.prefHeight(-1);
            width += 10;
            height += btnHeight + 30;
            this.pickerPane.setPrefWidth(width);
            this.pickerPane.setPrefHeight(height);

            enterBtn.setLayoutX(width - enterBtn.prefWidth(-1) -15);
            enterBtn.setLayoutY(height - btnHeight - 15);

        }else{
            this.pickerPane.setPrefWidth(width+10);
            this.pickerPane.setPrefHeight(height+30);
        }
    }

    public void show(Node node){
        Bounds nodePoint = node.localToScreen(node.getLayoutBounds());
        double x = nodePoint.getMinX();
        double y = nodePoint.getMaxY() + pickerPane.getPrefHeight() ;

        //有时候需要设置这个属性，不然，x,y可能不生效
        setAnchorLocation(AnchorLocation.CONTENT_BOTTOM_LEFT);

        show(node, x, y);
    }

    public void setSelectedCallback(CallBack<LocalDateTime> callback) {
        this.selectedCallback = callback;
    }

    public void setEnterCallback(DateSelectorPopupEnterCallback callback){
        this.enterCallback = callback;
    }
}
