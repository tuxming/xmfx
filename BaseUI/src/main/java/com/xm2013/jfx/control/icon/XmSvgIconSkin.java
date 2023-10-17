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
package com.xm2013.jfx.control.icon;

import com.xm2013.jfx.control.svg.XmSVGView;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.Paint;

public class XmSvgIconSkin extends SkinBase<XmSVGIcon> {

    private XmSVGIcon control;

    private XmSVGView view = null;

    public XmSvgIconSkin(XmSVGIcon control) {
        super(control);
        this.control = control;
        view = control.getSvgView();

        setSize(control.getSize());
        setColor(control.getColor());

//        control.setStyle("-fx-border-color: red; -fx-border-width: 1px;");

        this.control.sizeProperty().addListener(sizeChangeListener);
        this.control.colorProperty().addListener(colorChangeListener);

        this.getChildren().add(view);

        control.svgViewProperty().addListener(viewChangerListener);

    }

    private ChangeListener<XmSVGView> viewChangerListener = (ob, ov, nv )->{
        if(ov != null && getChildren().contains(ov)){
            getChildren().remove(ov);
        }
        if(nv!=null){
            view = nv;
            getChildren().add(nv);
        }
    };

    private ChangeListener<Number> sizeChangeListener = (ob, ov, nv) -> {
        if(nv!=null){
            setSize(nv.doubleValue());
        }
    };

    private ChangeListener<Paint> colorChangeListener = (ob, ov, nv) -> {
        if(nv!=null){
            setColor(nv);
        }
    };

    private void setSize(Double size){
        if(size != null){
            this.control.getSvgView().setSize(size);
        }
    }

    private void setColor(Paint paint){
        if(paint != null){
            this.control.getSvgView().setColor(paint);
        }
    }

//    @Override
//    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//
////        double w = view.prefWidth(-1),
////                h = view.prefHeight(-1),
////                x = (contentWidth - w) / 2,
////                y = (contentHeight - h) / 2;
////
////        System.out.println("aa:"+String.format("contentX: %f, contentY: %f, contentWidth: %f, contentHeight: %f, w: %f, h:%f, x: %f, y:%f",
////                contentWidth, contentHeight, contentX, contentY, w, h, x, y));
//
//        layoutInArea(view, x, y, w, h, 0, HPos.CENTER, VPos.CENTER);
//
//    }

    @Override
    public void dispose() {
        this.control.sizeProperty().removeListener(sizeChangeListener);
        this.control.colorProperty().removeListener(colorChangeListener);
        super.dispose();
    }
}
