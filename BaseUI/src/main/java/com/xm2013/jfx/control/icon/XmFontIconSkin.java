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

import javafx.beans.binding.Bindings;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class XmFontIconSkin extends SkinBase<XmFontIcon> {

    private XmFontIcon control;

    private Text icon = null;

    public XmFontIconSkin(XmFontIcon control) {
        super(control);
        this.control = control;

        //初始化图标
        icon = new Text();

        icon.setFont(getFont(control.getFont(), control.getSize()));

//        this.control.sizeProperty().addListener((ob, ov, nv) -> {
//            if(nv!=null){
//                icon.setFont(getFont(control.getFont(), nv.doubleValue()));
//            }
//        });

        icon.setFill(control.getColor());
        try{
            BorderStroke borderStroke = control.getBorder().getStrokes().get(0);
            icon.setStrokeWidth(borderStroke.getWidths().getTop());
            icon.setStroke(borderStroke.getTopStroke());
        }catch (Exception e){}

        icon.setTextAlignment(TextAlignment.CENTER);
        icon.textOriginProperty().bind(control.textOriginProperty());

        icon.textProperty().bind(control.iconProperty());
        icon.fontProperty().bind(Bindings.createObjectBinding(()-> getFont(control.getFont(), control.getSize()) ,
                control.sizeProperty(), control.fontProperty()));
        icon.fillProperty().bind(control.colorProperty());
        icon.strokeWidthProperty().bind(Bindings.createDoubleBinding(()->{
            try{
                return control.getBorder().getStrokes().get(0).getWidths().getTop();
            }catch(Exception e){
                return 0d;
            }
        }, control.borderProperty()));
        icon.strokeProperty().bind(Bindings.createObjectBinding(()->{
            try{
                return control.getBorder().getStrokes().get(0).getTopStroke();
            }catch (Exception e){
                return Color.TRANSPARENT;
            }
        }, control.borderProperty()));


//        icon.setPickOnBounds(false);
//        control.setMouseTransparent(true);

//        control.hoverProperty().addListener((ob, ov, nv) -> {
//            control.pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), nv);
//        });

        getChildren().add(icon);
    }

//    @Override
//    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        double width = icon.prefWidth(-1)+topInset+bottomInset;
//        System.out.println("compute pref width:"+width);
//        return width;
//
//    }


    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return icon.prefWidth(-1);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return icon.prefHeight(-1);
    }

    private Font getFont(Font font , double size){
        if(size==0){
            size = 16d;
        }
        try{
            return Font.font(font.getFamily(), size);
        }catch (Exception e){
            return Font.font(size);
        }
    }

}
