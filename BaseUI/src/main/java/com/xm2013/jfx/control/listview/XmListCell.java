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
package com.xm2013.jfx.control.listview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义ListCell，实现了根据主题色的变换
 */
public class XmListCell<T> extends ListCell<T> {

    private static final String DEFAULT_STYLE_CLASS = "xm-list-cell";

    class ListCellSkinInfo {
        public Paint fontColor;
        public Background background;
        public ListCellSkinInfo(Paint fontColor, Background background){
            this.fontColor = fontColor;
            this.background = background;
        }
    }

    private Map<Integer, ListCellSkinInfo> infos = new LinkedHashMap<>();

    private static PseudoClass odd = PseudoClass.getPseudoClass("odd");
    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");
    private static PseudoClass filled = PseudoClass.getPseudoClass("filled");
    private static PseudoClass hover = PseudoClass.getPseudoClass("hover");

    private ClickAnimate clickAnimate = null;

    private double startRadius = 0.01;
    private double endRadius = 10;

    private boolean prevSelected = false;

    public XmListCell() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);

        listViewProperty().addListener((ob, ov, nv)->{
            updateSkin(0);
        });

        ObservableSet<PseudoClass> pseudoClassStates = getPseudoClassStates();
        pseudoClassStates.addListener((SetChangeListener<PseudoClass>) change -> {
//            System.out.println(getText()+": "+pseudoClassStates);
            boolean isOdd = pseudoClassStates.contains(odd);
            boolean isSelected = pseudoClassStates.contains(selected);
//            boolean isFilled = pseudoClassStates.contains(filled);
            boolean isHover = pseudoClassStates.contains(hover);

            if(isOdd){
                if(!isSelected && !isHover){
                    updateSkin(1);
                    prevSelected = false;
                    return;
                }
            }

            if(isSelected){
                if(!prevSelected){
                    updateSelectedStatus();
//                    updateSkin(2);
                }
                prevSelected = true;
                return;
            }

            prevSelected = false;
            if(isHover){
                updateSkin(3);
                return;
            }

            updateSkin(0);

        });

    }

    private void updateSelectedStatus(){
        setTextFill(Color.WHITE);

        List<Stop> stops = new ArrayList<>();
        Color color = ((XmListView)getListView()).getColorType().getFxColor();
        stops.add(new Stop(0, color));
        stops.add(new Stop(1, Color.TRANSPARENT));

        startRadius = 0.01;
        AnimationTimer timer = new AnimationTimer() {
            final double frameDuration = 1_000_000_000.0 / 60;
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                double elapsedNanos = now - lastUpdate;
                if (elapsedNanos >= frameDuration) {

                    startRadius += 0.25;
                    if(startRadius<endRadius){
                        RadialGradient radialGradient = new RadialGradient(0, 0, 0, 0.5, startRadius,true, CycleMethod.NO_CYCLE, stops);
                        setBackground(new Background(new BackgroundFill(radialGradient, CornerRadii.EMPTY, Insets.EMPTY)));

                        if(!getPseudoClassStates().contains(selected)){
                            if(getPseudoClassStates().contains(odd)){
                                updateSkin(1);
                            }else{
                                updateSkin(0);
                            }
                            stop();
                        }

                    }else{
                        stop();
                        if(!getPseudoClassStates().contains(selected)){
                            if(getPseudoClassStates().contains(odd)){
                                updateSkin(1);
                            }else{
                                updateSkin(0);
                            }
                        }
                    }
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }


    /**
     * 0-默认
     * 1-odd
     * 2-selected
     * 3-hover
     * @param status int
     */
    private void updateSkin(int status){

//        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        ListCellSkinInfo info = getInfo(status);
        if(info == null){
            return;
        }

        ListView<T> tv = listViewProperty().get();

        if(tv instanceof  XmListView){
            double fontSize = 14;
            SizeType sizeType = ((XmListView<T>) tv).getSizeType();
            if(sizeType.equals(equals(SizeType.SMALL))){
                fontSize = 12;
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 16;
            }

            this.setFont(Font.font(this.getFont().getFamily(), fontSize));
        }

        setBackground(info.background);
        setTextFill(info.fontColor);

    }

    private ListCellSkinInfo getInfo(int status){

        if(infos.size() == 0 || infos.get(status) == null){
            buildCellSkinInfo(status);
        }

        return infos.get(status);

    }

    private void buildCellSkinInfo(int status) {

        ListView<T> listView = getListView();
        boolean is = listView instanceof XmListView;
        if(!is){
            return;
        }

        XmListView lv = (XmListView) listView;
        Paint paint = lv.getColorType().getPaint(),
                fontColor = null,
                bgColor = null;

        HueType hueType = lv.getHueType();

        if(status == 0){
            if(HueType.LIGHT.equals(hueType)){
                fontColor = Color.color(0.2, 0.2,0.2);
                bgColor = Color.WHITE;
            }else{
                fontColor = Color.color(1, 1, 1);
                bgColor = Color.TRANSPARENT;
            }
        }else if(status == 1){
            if(HueType.LIGHT.equals(hueType)){
                fontColor = Color.color(0.2, 0.2,0.2);
            }else{
                fontColor = Color.color(1, 1, 1);
            }
            bgColor = FxKit.getOpacityPaint(paint, 0.08);
        }else if(status == 2){
            fontColor = Color.WHITE;
            bgColor = paint;

        }else if(status == 3){
            fontColor = paint;

            if(getPseudoClassStates().contains(odd)){
                bgColor = FxKit.getOpacityPaint(paint, 0.08);
            }else{
                bgColor = Color.TRANSPARENT;
            }

        }

        infos.put(status, new ListCellSkinInfo(fontColor, new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY))));

    }

}
