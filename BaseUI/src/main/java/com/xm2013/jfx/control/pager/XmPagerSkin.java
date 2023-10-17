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
package com.xm2013.jfx.control.pager;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import com.xm2013.jfx.control.selector.SelectorConvert;
import com.xm2013.jfx.control.selector.XmSelector;
import com.xm2013.jfx.control.textfield.XmSimpleTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class XmPagerSkin extends SkinBase {
    private final XmNodeButton prevBtn;
    private final XmNodeButton nextBtn;
    private final XmNodeButton prevPageBtn;
    private final XmNodeButton nextPageBtn;
    private final XmNodeButton firstBtn;
    private final XmNodeButton lastBtn;
    private final XmNodeButton btn1;
    private final XmNodeButton btn2;
    private final XmNodeButton btn3;
    private final XmNodeButton btn4;
    private final XmNodeButton btn5;
    private final Text info;
    private final XmSelector<Integer> sizeSelector;
    private XmSimpleTextField jumperField;
    private final Text label1;
    private final Text label2;
    private XmPager control;

    private  XmSVGIcon prevPageBtnHoverContent = new XmSVGIcon(FxKit.DOUBLE_ARROW_LEFT, 14);
    private  Text prevPageBtnDefaultContent = new Text("...");

    private  XmSVGIcon  nextPageBtnHoverContent = new XmSVGIcon(FxKit.DOUBLE_ARROW_RIGHT, 14);
    private  Text nextPageBtnDefaultContent = new Text("...");

    private int totalPage = 0;

    private ObservableList<Node> firstLineNodes = FXCollections.observableArrayList();
    private ObservableList<Node> secondLineNodes = FXCollections.observableArrayList();

    public XmPagerSkin(XmPager control) {
        super(control);
        this.control = control;

        double total = this.control.getTotal();
        totalPage = (int) Math.ceil(total / this.control.getLength());

        XmSVGIcon prevBtnIcon = new XmSVGIcon(FxKit.ARROW_LEFT, 14);
        prevBtnIcon.setMouseTransparent(true);
        prevBtn = new XmNodeButton(prevBtnIcon);
        prevBtn.setValue("prevBtn");

        XmSVGIcon nextBtnIcon = new XmSVGIcon(FxKit.ARROW_RIGHT, 14);
        nextBtnIcon.setMouseTransparent(true);
        nextBtn = new XmNodeButton(nextBtnIcon);
        nextBtn.setValue("nextBtn");

        prevPageBtnDefaultContent.setFont(Font.font(12));
        prevPageBtnHoverContent.setMouseTransparent(true);
        nextPageBtnDefaultContent.setFont(Font.font(12));
        nextPageBtnHoverContent.setMouseTransparent(true);

        prevPageBtn = new XmNodeButton(prevPageBtnDefaultContent);
        prevPageBtn.setValue("prevPageBtn");
        nextPageBtn = new XmNodeButton(nextPageBtnDefaultContent);
        nextPageBtn.setValue("nextPageBtn");

        firstBtn = new XmNodeButton("1");
        firstBtn.setValue("firstBtn");
        lastBtn = new XmNodeButton(totalPage+"");
        lastBtn.setValue("lastBtn");
        btn1 = new XmNodeButton("1");
        btn2 = new XmNodeButton("2");
        btn3 = new XmNodeButton("3");
        btn4 = new XmNodeButton("4");
        btn5 = new XmNodeButton("5");

        info = new Text("当前第1页/共50页");

        sizeSelector = new XmSelector<>();
        sizeSelector.setConverter(new SelectorConvert<Integer>() {
            @Override
            public String toString(Integer object) {
                return object == null?"":object+"";
            }

            @Override
            public Integer fromString(String string) {
                Integer num = null;
                if(string!=null && string.trim().length()>0){
                    try{
                        num = Integer.parseInt(string);
                    }catch (Exception e){

                    }
                }
                return num;
            }
        });
        sizeSelector.setPrefWidth(80);
        sizeSelector.setValue(10);
        sizeSelector.setSizeType(SizeType.SMALL);
        sizeSelector.setItems(control.getSizes());

        jumperField = new XmSimpleTextField();
        jumperField.setCleanable(false);
        jumperField.setPrefWidth(40);

        label1 = new Text("转至");
        label2 = new Text("页");

//        btn1.setActive(true);
//        btn2.setActive(true);
//        btn3.setActive(true);
//        btn4.setActive(true);
//        btn5.setActive(true);

//        firstLineNodes.addAll(prevBtn, firstBtn , prevPageBtn, btn1, btn2, btn3, btn4, btn5, nextPageBtn, lastBtn, nextBtn);
        setShowQuickJumper();
        updateCurrIndex(this.control.getCurrIndex());
        updateSkin();

        prevPageBtn.hoverProperty().addListener((ob, ov, nv)->{
            if(nv){
                prevPageBtn.setNode(prevPageBtnHoverContent);
            }else{
                prevPageBtn.setNode(prevPageBtnDefaultContent);
            }
        });

        nextPageBtn.hoverProperty().addListener((ob, ov, nv)->{
            if(nv){
                nextPageBtn.setNode(nextPageBtnHoverContent);
            }else{
                nextPageBtn.setNode(nextPageBtnDefaultContent);
            }
        });

        prevBtn.setOnAction(e -> {
            int currIndex = control.getCurrIndex();
            if(currIndex>0){
                control.setCurrIndex(currIndex-1);
            }
        });

        nextBtn.setOnAction(e -> {
            int currIndex = control.getCurrIndex();
            if(currIndex < totalPage-1){
                control.setCurrIndex(currIndex +1 );
            }
        });

        firstBtn.setOnAction(e -> {
            control.setCurrIndex(0);
        });

        lastBtn.setOnAction(e -> {
            control.setCurrIndex(totalPage-1);
        });

        prevPageBtn.setOnAction(e -> {
            int currIndex = control.getCurrIndex();
            currIndex = currIndex - 5;
            if(currIndex<0){
                currIndex = 0;
            }
            control.setCurrIndex(currIndex);
        });

        nextPageBtn.setOnAction(e -> {
            int currIndex = control.getCurrIndex();
            currIndex = currIndex + 5;
            if(currIndex>totalPage-1){
                currIndex = totalPage - 1;
            }
            control.setCurrIndex(currIndex);
        });

        btn1.setOnAction(new BtnHandler(btn1));
        btn2.setOnAction(new BtnHandler(btn2));
        btn3.setOnAction(new BtnHandler(btn3));
        btn4.setOnAction(new BtnHandler(btn4));
        btn5.setOnAction(new BtnHandler(btn5));

        this.control.currIndexProperty().addListener((ob, ov, nv)->{
           updateCurrIndex(nv.intValue());
        });

        sizeSelector.getValues().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> c) {
                Integer length = sizeSelector.getValue();
                if(length!=null){
                    control.setLength(length);
                    totalPage = (int) Math.ceil(control.getTotal() / (double)length);
                    int currIndex = control.getCurrIndex();
                    if(currIndex>totalPage){
                        currIndex = totalPage - 1;
                    }
                    control.setCurrIndex(currIndex);
                    updateCurrIndex(currIndex);
                }
            }
        });
        
        jumperField.addEventFilter(KeyEvent.KEY_RELEASED, jumpEnterHandler);
        registerChangeListener(control.showQuickJumperProperty(), e->{
            if(control.isShowQuickJumper()){
                secondLineNodes.setAll(info, sizeSelector, label1, jumperField, label2);
                removeFromChildren(info, sizeSelector, label1, jumperField, label2);
                getChildren().addAll(secondLineNodes);
            }else{
                secondLineNodes.setAll(info, sizeSelector);
                removeFromChildren(info, sizeSelector, label1, jumperField, label2);
                getChildren().addAll(secondLineNodes);
            }
        });

        registerChangeListener(control.sizeTypeProperty(), e -> {
            updateSkin();
        });
        registerChangeListener(control.colorTypeProperty(), e-> {
            updateSkin();
        });
        registerChangeListener(control.hueTypeProperty(), e->{
            updateSkin();
        });
        registerChangeListener(control.showQuickJumperProperty(), e->{
            setShowQuickJumper();
        });
        registerChangeListener(control.singleLineProperty(), e->{
            this.control.requestLayout();
        });

    }

    private void setShowQuickJumper(){
        secondLineNodes.clear();
        removeFromChildren(info, sizeSelector, label1, jumperField, label2);
        if(control.isShowQuickJumper()){
            secondLineNodes.addAll(info, sizeSelector, label1, jumperField, label2);
        }else{
            secondLineNodes.addAll(info, sizeSelector);
        }
        getChildren().addAll(secondLineNodes);
    }

    private void updateSkin(){

        SizeType sizeType = control.getSizeType();
        ColorType colorType = control.getColorType();
        HueType hueType = control.getHueType();

//        info, sizeSelector, label1, jumperField, label2,
//                revBtn, nextBtn, prevPageBtn, nextPageBtn, firstBtn, lastBtn, btn1, btn2, btn3, btn4, btn5

        Paint fontColor = Color.web("#333333");
        double fontSize = 0, arrowSize=12, doubleArrowSize=12, jumperWidth = 40, selectorWidth = 80;
        Insets padding = null, padding1 = null;


        if(HueType.DARK.equals(hueType)){
            fontColor = colorType.getPaint();
        }

        if(sizeType.equals(SizeType.SMALL)){
            fontSize = 14d;
            padding = new Insets(1.5d, 4d, 1.5d, 4d);
            padding1 = new Insets(2.3d, 4d, 2.2d, 4d);
        }else {
            fontSize = 16d;
            jumperWidth = 80;
            selectorWidth = 120;
            padding = new Insets(10d, 5d, 10d, 10d);
            padding1 = new Insets(5.5d, 8d, 5.5d, 8d);
            arrowSize = 18;
            doubleArrowSize = 18;
        }

        Font font = Font.font(info.getFont().getFamily(), fontSize);
        info.setFill(fontColor);
        info.setFont(font);
        label1.setFill(fontColor);
        label1.setFont(font);
        label2.setFont(font);
        label2.setFill(fontColor);

        sizeSelector.setSizeType(sizeType);
        sizeSelector.setColorType(colorType);
        sizeSelector.setPrefWidth(selectorWidth);

        jumperField.setSizeType(sizeType);
        jumperField.setColorType(colorType);
        jumperField.setPrefWidth(jumperWidth);
        jumperField.setPadding(padding);
        jumperField.setFont(font);

        btn1.setPadding(padding1);
        btn2.setPadding(padding1);
        btn3.setPadding(padding1);
        btn4.setPadding(padding1);
        btn5.setPadding(padding1);
        prevBtn.setPadding(padding1);
        nextBtn.setPadding(padding1);
        firstBtn.setPadding(padding1);
        lastBtn.setPadding(padding1);
        prevPageBtn.setPadding(padding1);
        nextPageBtn.setPadding(padding1);

        btn1.setColorType(colorType);
        btn2.setColorType(colorType);
        btn3.setColorType(colorType);
        btn4.setColorType(colorType);
        btn5.setColorType(colorType);
        prevBtn.setColorType(colorType);
        nextBtn.setColorType(colorType);
        firstBtn.setColorType(colorType);
        lastBtn.setColorType(colorType);
        prevPageBtn.setColorType(colorType);
        nextPageBtn.setColorType(colorType);

        ((Text)btn1.getNode()).setFont(font);
        ((Text)btn2.getNode()).setFont(font);
        ((Text)btn3.getNode()).setFont(font);
        ((Text)btn4.getNode()).setFont(font);
        ((Text)btn5.getNode()).setFont(font);
        ((Text)firstBtn.getNode()).setFont(font);
        ((Text)lastBtn.getNode()).setFont(font);

        ((XmSVGIcon)prevBtn.getNode()).setSize(arrowSize);
        ((XmSVGIcon)nextBtn.getNode()).setSize(arrowSize);

        prevPageBtnHoverContent.setSize(doubleArrowSize);
        prevPageBtnDefaultContent.setFont(font);
        nextPageBtnHoverContent.setSize(doubleArrowSize);
        nextPageBtnDefaultContent.setFont(font);
    }

    /**
     * 当输入enter键的时候，退出编辑模式
     */
    private EventHandler<KeyEvent> jumpEnterHandler = e -> {
        if(e.getCode().equals(KeyCode.ENTER)){
        	String text = jumperField.getText().replace("\n", "");
        	try {
				Integer index = Integer.parseInt(text);
                index -= 1;
				if(index<0) {
					index = 0;
				}else if(index>totalPage - 1) {
					index = totalPage - 1;
				}
				jumperField.setText((index+1)+"");
				control.setCurrIndex(index);
			} catch (Exception e2) {
				jumperField.setText(null);
			}
        	
        }
    };
    
    private void updateCurrIndex(int currIndex){

        lastBtn.setText(totalPage+"");
        firstLineNodes.clear();
        removeFromChildren(prevBtn, nextBtn, prevPageBtn, nextPageBtn, firstBtn, lastBtn, btn1, btn2, btn3, btn4, btn5);

        if(totalPage == 1){
            firstLineNodes.addAll(prevBtn, firstBtn, nextBtn);
        }else if(totalPage == 2){
            firstLineNodes.addAll(prevBtn, firstBtn, lastBtn, nextBtn);
        }else if(totalPage == 3){
            firstLineNodes.addAll(prevBtn, firstBtn,btn1, lastBtn, nextBtn);
        }else if(totalPage == 4){
            firstLineNodes.addAll(prevBtn, firstBtn,btn1, btn2, lastBtn, nextBtn);
        }else if(totalPage == 5){
            firstLineNodes.addAll(prevBtn, firstBtn,btn1, btn2, btn3, lastBtn, nextBtn);
        }else if(totalPage == 6){
            firstLineNodes.addAll(prevBtn, firstBtn,btn1, btn2, btn3, btn4, lastBtn, nextBtn);
        }else{
            if(currIndex<5){
                firstLineNodes.addAll(prevBtn, firstBtn, btn1, btn2, btn3, btn4, btn5, nextPageBtn,  lastBtn, nextBtn);
            }else if(currIndex>totalPage-5){
                firstLineNodes.addAll(prevBtn, firstBtn, prevPageBtn,  btn1, btn2, btn3, btn4, btn5, lastBtn, nextBtn);
            }else{
                firstLineNodes.addAll(prevBtn, firstBtn, prevPageBtn,  btn1, btn2, btn3, btn4, btn5, nextPageBtn,  lastBtn, nextBtn);
            }
        }
        getChildren().addAll(firstLineNodes);

        if(currIndex==0){
            prevBtn.setDisable(true);
        }else{
            prevBtn.setDisable(false);
        }

        if(currIndex == totalPage-1){
            nextBtn.setDisable(true);
        }else{
            nextBtn.setDisable(false);
        }

        if(currIndex<6){
            btn1.setValue(1);
            btn2.setValue(2);
            btn3.setValue(3);
            btn4.setValue(4);
            btn5.setValue(5);

            btn1.setText(2+"");
            btn2.setText(3+"");
            btn3.setText(4+"");
            btn4.setText(5+"");
            btn5.setText(6+"");
        }else if(currIndex> totalPage-6){
            btn1.setValue(totalPage-6);
            btn2.setValue(totalPage-5);
            btn3.setValue(totalPage-4);
            btn4.setValue(totalPage-3);
            btn5.setValue(totalPage-2);

            btn1.setText((totalPage-5)+"");
            btn2.setText((totalPage-4)+"");
            btn3.setText((totalPage-3)+"");
            btn4.setText((totalPage-2)+"");
            btn5.setText((totalPage-1)+"");
        }else{
            btn1.setValue(currIndex-2);
            btn2.setValue(currIndex-1);
            btn3.setValue(currIndex);
            btn4.setValue(currIndex+1);
            btn5.setValue(currIndex+2);

            btn1.setText((currIndex-1)+"");
            btn2.setText((currIndex)+"");
            btn3.setText((currIndex+1)+"");
            btn4.setText((currIndex+2)+"");
            btn5.setText((currIndex+3)+"");
        }

        btn1.setFilled(((Integer)btn1.getValue()) == currIndex);
        btn2.setFilled(((Integer)btn2.getValue()) == currIndex);
        btn3.setFilled(((Integer)btn3.getValue()) == currIndex);
        btn4.setFilled(((Integer)btn4.getValue()) == currIndex);
        btn5.setFilled(((Integer)btn5.getValue()) == currIndex);
        firstBtn.setFilled(currIndex == 0);
        lastBtn.setFilled(currIndex == totalPage-1);

        info.setText((currIndex+1) +" / "+ totalPage + " / " + control.getTotal() );

    }

    public void removeFromChildren(Node ...node){
        ObservableList children = getChildren();
        for (Node n : node) {
            children.remove(n);
        }
    }

    class BtnHandler implements EventHandler<ActionEvent>{
        private XmNodeButton btn;
        public BtnHandler(XmNodeButton btn){
            this.btn = btn;
        }

        @Override
        public void handle(ActionEvent event) {
            control.setCurrIndex((Integer) btn.getValue());
        }
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {

        double width = control.getPrefWidth();
        if(width>-1){
            return width;
        }

        double width1 = 0;
        for (Node node : firstLineNodes) {
            width1 += node.prefWidth(-1) + 5;
        }

        double width2 = 0;
        for (Node node : secondLineNodes) {
            width2 += node.prefWidth(-1) + 5;
            if(info.equals(node)){
                width2+=10;
            }
        }

        if(control.isSingleLine()){
            width = width1 + width2;
        }else{
            width = Math.max(width1, width2);
        }

        return width;

    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double height = control.getPrefHeight();
        if(height>0){
            return height;
        }

        height = firstBtn.prefHeight(-1) + 5;
        if(!control.isSingleLine()){
            height += height + 5;
        }

        return height;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        double x = 0;
        double width1 = 0;
        if(!control.isSingleLine()){
            for (Node node : firstLineNodes) {
                width1 += node.prefWidth(-1) + 5;
            }
            x = (contentWidth - width1)/2;
        }


        for (Node node : firstLineNodes) {
            double width = node.prefWidth(-1),
                    height = node.prefHeight(-1);

            double y = control.isSingleLine()? (contentHeight - height) /2 : ((contentHeight-5)/2 - height)/2;

            if(nextBtn.equals(node) || prevBtn.equals(node) || prevPageBtn.equals(node) || nextPageBtn.equals(node)){
                y-=2;
            }

            layoutInArea(node, x, y, width, height, 0, HPos.CENTER, VPos.CENTER);

            x+=width+5;
        }

        if(!control.isSingleLine()){
            double secWidth = 0;
            for (Node node : secondLineNodes){
                secWidth += node.prefWidth(-1) + 5;
                if(info.equals(node)){
                    secWidth+=10;
                }
            }

            x = (contentWidth - secWidth) /2 ;
        }

        double btnHeight = firstBtn.prefHeight(-1);

        for (Node node : secondLineNodes){
            double width = node.prefWidth(-1),
                    height = node.prefHeight(-1);
            double y = control.isSingleLine() ? (contentHeight - height) / 2 : btnHeight + 5 + (contentHeight - btnHeight - 5 - height) /2;
            layoutInArea(node, x, y, width, height, 0, HPos.CENTER, VPos.CENTER);

            x+=width+5;

            if(info.equals(node)){
                x+=10;
            }

        }

    }
}
