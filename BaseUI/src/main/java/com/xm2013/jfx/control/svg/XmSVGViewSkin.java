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
package com.xm2013.jfx.control.svg;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

/**
 * @author LeeWyatt
 * QQ: 9670453
 * QQ群: 518914410
 */
public class XmSVGViewSkin extends SkinBase<XmSVGView> {
    private Pane pane;
    private InvalidationListener updateListener = ob -> updateSVG();

    private XmSVGView control;

    public XmSVGViewSkin(XmSVGView control) {
        super(control);
        this.control = control;

        pane = new Pane();
        pane.getStyleClass().add("svg-pane");
        updateSVG();
        getChildren().setAll(pane);
        control.contentProperty().addListener(updateListener);
        this.consumeMouseEvents(true);

        setSize(this.control.getSize());
        setColor(this.control.getColor());

//        this.control.setStyle("-fx-background-color: blue;");

        this.control.sizeProperty().addListener(sizeChangeListener);
        this.control.colorProperty().addListener(colorChangeListener);
    }


    /**
     * 设置尺寸大小变化
     */
    private ChangeListener<Number> sizeChangeListener = (ob, ov, nv) -> {
        if(nv != null){
            setSize(nv.doubleValue());
        }
    };

    /**
     * 设置图标颜色
     */
    private ChangeListener<Paint> colorChangeListener = (ob, ov, nv) -> {
        setColor(nv);
    };

    private void setColor(Paint color){
        if(color!=null){
            ObservableList<SVGPath> paths = control.getPaths();
            for(SVGPath path : paths){
                path.setFill(color);
            }
        }
    }

    /**
     * 重设svg的内容
     * @param size
     */
    private void setSize(double size){
        double width = pane.prefWidth(-1);
//        double width = this.control.prefWidth(-1);
        if(width <= 0){
            width = 100;
        }
        double scale = size / width;
//        System.out.println(width +","+size+","+scale);

        ObservableList<SVGPath> paths = control.getPaths();
        for (SVGPath path : paths){
            String ncontent = "";

            String content = path.getContent();

//            System.out.println(content);

            SVGParser p = new SVGParser(content);
            p.allowcomma = false;
            while (!p.isDone()) {
                char cmd = p.getChar();
                ncontent += cmd;

                int index = 0;

                boolean isNext = p.nextIsNumber();
                while(isNext){

                    //SVG的A指令，第4,5个值是boolean值，只能取值0,1而p.f()函数返回的是0.0或1.0，所以这里进行强制修改。
                    if(cmd == 'a' && (index == 4 || index == 3)){
                        double r = p.f();
                        if(r  == 1){
                            ncontent += 1;
                        }else{
                            ncontent += 0;
                        }
                    }else{
                        ncontent += p.f()*scale;
                    }

                    isNext = p.nextIsNumber();
                    if(isNext){
                        ncontent += ",";
                    }

                    index++;
                }

            }


            path.setContent(ncontent);
        }
    }

//    private void updateSVG() {
//        getChildren().clear();//首先清空
//
//        String content = getSkinnable().getContent();
//        if (content==null) {
//            return;
//        }
//
//        ObservableList<SVGPath> paths = control.getPaths();
//        for(SVGPath path : paths){
//            getChildren().add(path);
//        }
//    }
    private void updateSVG() {
        pane.getChildren().clear();//首先清空

        String content = getSkinnable().getContent();
        if (content==null) {
            return;
        }

        ObservableList<SVGPath> paths = control.getPaths();
        for(SVGPath path : paths){
            pane.getChildren().add(path);
        }
    }

//    @Override
//    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        double width = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
//
//        return width;
//    }

    @Override
    protected void layoutChildren(final double x, final double y, final double w, final double h) {
        layoutInArea(pane, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    public void dispose() {
        getSkinnable().contentProperty().removeListener(updateListener);
        control.sizeProperty().removeListener(sizeChangeListener);
        control.colorProperty().removeListener(colorChangeListener);
        getChildren().clear();
        super.dispose();
    }
}