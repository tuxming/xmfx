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
package com.xm2013.jfx.control.textfield;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SkinInfo;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.Map;

public class XmSimpleTextFieldSkin extends TextFieldSkin {

    private Map<Integer, SkinInfo> skins = new HashMap<Integer, SkinInfo>();
    private XmSimpleTextField control;

    private XmSVGIcon clearIcon = null;
    private boolean isSetedPadding = false;

    public XmSimpleTextFieldSkin(XmSimpleTextField control) {
        super(control);
        this.control = control;

        if(control.isCleanable()){
            setCleanable();
        }

        if (this.control.getSuffixIcon() != null) {
            setSuffixIcon(control.getSuffixIcon());
        }

        updateSkin(1);

        control.suffixIconProperty().addListener(suffixIconListener);
        control.colorTypeProperty().addListener(skinListener);
        control.roundTypeProperty().addListener(skinListener);
        control.hoverProperty().addListener(statusListener);
        control.focusedProperty().addListener(statusListener);
        control.cleanableProperty().addListener(clearIconListener);

    }

    /**
     * 更新组件外观
     *
     * @param status status: 1 ： 默认状态下的颜色
     *               status: 2:  hover, out focus
     *               status: 3 : hover,focus状态下的颜色
     *               status: 4 : out hover, focus状态的颜色
     */
    private void updateSkin(int status) {

        if(control.isEnableSkin()){
            return;
        }

        SkinInfo skin = getSkinInfo(status);

        Paint outColor = skin.getBorderOutColor(),
                innerColor = skin.getInnerBorderColor();

        BorderWidths innWidths = new BorderWidths(skin.getInnerBorderWidth()),
                outWidths = new BorderWidths(skin.getOuterBorderWidth());

        CornerRadii radiusWidth = new CornerRadii(skin.getRadiusWidth());

        if (status == 1) {
            innerColor = Color.web("#888888");
        }

        Border innBorder = new Border(
                new BorderStroke(outColor, BorderStrokeStyle.SOLID, radiusWidth, outWidths, skin.getOuterBorderInsets()),
                new BorderStroke(innerColor, BorderStrokeStyle.SOLID, radiusWidth, innWidths, skin.getInnerBorderInsets())
        );

        control.setBorder(innBorder);
    }

    private SkinInfo getSkinInfo(int status) {
        SkinInfo info = skins.get(status);
        if (info == null) {
            info = new SkinInfo(control.getColorType(), control.getSizeType(), control.getRoundType(), HueType.LIGHT, control.getBorderType());
            info.compute(status);
            skins.put(status, info);
        }
        return info;
    }

    private void setSuffixIcon(Node nv) {
        getChildren().add(nv);
        nv.setManaged(false);

        Platform.runLater(()->{
            Insets padding = control.getPadding();
            double w = this.control.prefWidth(-1),
                    h = this.control.prefHeight(-1),
                    sw = nv.prefWidth(-1),
                    sh = nv.prefHeight(-1),
                    x = isSetedPadding?(w - sw):(w + 4),
                    y =  (h - sh) / 2 + sh/2 ;
            if(!isSetedPadding){
                padding = new Insets(padding.getTop(), padding.getLeft() + sw, padding.getBottom(), padding.getRight());
                control.setPadding(padding);
                isSetedPadding = true;
            }
            nv.setLayoutX(x);
            nv.setLayoutY(y);

        });
    }

    /**
     * 是否可以清除
     */
    private void setCleanable() {
        if(clearIcon == null){
            clearIcon = new XmSVGIcon(FxKit.CLEAN_PATH);
            clearIcon.setSize(16);
            clearIcon.setColor(Color.RED);
            clearIcon.setCursor(Cursor.HAND);
            clearIcon.setOnMouseClicked(e -> {
                control.clear();
            });
        }

        getChildren().add(clearIcon);
        clearIcon.setManaged(false);
        clearIcon.setVisible(false);
//        Platform.runLater(()->{
//            Insets padding = control.getPadding();
//            double w = this.control.prefWidth(-1),
//                    h = this.control.prefHeight(-1),
//                    sw = clearIcon.prefWidth(-1),
//                    sh = clearIcon.prefHeight(-1),
////                    x = isSetedPadding?(w-sw-padding.getRight()): (w - sw),
//                    x = w,
//                    y = (h + padding.getTop() + padding.getBottom() - sh) / 2 ;
//
//            System.out.println(String.format("setPadding: %b, width: %f, height:%f, sw:%f, sh:%f, x:%f, y:%f, padding:%s", isSetedPadding, w, h, sw, sh, x, y, padding.toString()));
//
//            if(!isSetedPadding){
//                padding = new Insets(padding.getTop(), padding.getLeft() + sw, padding.getBottom(), padding.getRight());
//                control.setPadding(padding);
//                isSetedPadding = true;
//            }
//            clearIcon.setLayoutX(x);
//            clearIcon.setLayoutY(y);
//        });
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double height = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);

        if(clearIcon!=null){
            height = Math.max(clearIcon.prefHeight(-1)+topInset+bottomInset, height);
        }

        if(control.getSuffixIcon()!=null){
            height = Math.max(control.getSuffixIcon().prefHeight(-1)+topInset, height);
        }

        return height;
    }

    @Override
    protected void layoutChildren(double x1, double y1, double w1, double h1) {
        super.layoutChildren(x1, y1, w1, h1);

        if(clearIcon != null) {
            Insets padding = control.getPadding();
            double w = this.control.prefWidth(-1),
                    h = this.control.prefHeight(-1),
                    sw = clearIcon.prefWidth(-1),
                    sh = clearIcon.prefHeight(-1),
                    x = w - sw - (isSetedPadding? padding.getLeft() : padding.getRight()),
                    y = (h - sh)/2;

//            System.out.println(String.format("setPadding: %b, width: %f, height:%f, sw:%f, sh:%f, x:%f, y:%f, padding:%s", isSetedPadding, w, h, sw, sh, x, y, padding.toString()));

            if(!isSetedPadding){
                padding = new Insets(padding.getTop(), padding.getLeft() + sw, padding.getBottom(), padding.getRight());
                control.setPadding(padding);
                isSetedPadding = true;
            }
            clearIcon.setLayoutX(x);
            clearIcon.setLayoutY(y);
        }

    }

    /* --------------------------- listener ------------------- */
    private ChangeListener<Node> suffixIconListener = (ob, ov, nv) -> {
        if (ov != null) {
            getChildren().remove(ov);
        }

        if (nv != null) {
           setSuffixIcon(nv);
        }
    };

    private ChangeListener<Object> skinListener = (ob, ov, nv) -> {
        skins.clear();
    };

    /**
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover, out focus
     * status: 3 : hover,focus状态下的颜色
     * status: 4 : out hover, focus状态的颜色
     */
    private ChangeListener<Boolean> statusListener = (ob, ov, nv) -> {

        boolean isHover = control.isHover(),
                isFocus = control.focusedProperty().get();

        if (isHover) {
            if (isFocus) {
                updateSkin(3);
            } else {
                updateSkin(2);
            }
        } else {
            if (isFocus) {
                updateSkin(4);
            } else {
                updateSkin(1);
            }
        }

        if(control.isCleanable()){
            clearIcon.setVisible(isFocus);
            if(control.getSuffixIcon() != null){
                control.getSuffixIcon().setVisible(!isFocus);
            }
        }
    };

    private ChangeListener<Boolean> clearIconListener =(ob, ov, nv)->{
        if(nv){
            setCleanable();
        }else{
            if(clearIcon!=null){
                getChildren().remove(clearIcon);
            }
        }
    };

}

