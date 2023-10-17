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
package com.xm2013.jfx.control.selector;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.container.SimpleVScrollPane;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

public abstract class SelectorPopup<T> extends Popup {

    protected final ObservableList<T> items;
    protected final ColorType colorType;
    protected final SizeType sizeType;
    protected final HueType hueType;
    protected final SelectorConvert<T> convert;
    protected final SelectorType selectorType;
    protected final SelectorCellFactory<T> cellFactory;
    protected final ObservableList<T> values;

    protected BooleanProperty multiple;

    protected VBox popupContentPane;

    public SelectorPopup(XmSelector<T> control){
        this.items = control.getItems();
        this.values = control.getValues();
        this.colorType = control.getColorType();
        this.sizeType = control.getSizeType();
        this.hueType = control.getHueType();
        this.convert = control.getConverter();
        this.selectorType = control.getSelectorType();
        this.cellFactory = control.getCellFactory();
        this.multiple = control.multipleProperty();

        this.init();
    }

    public SelectorPopup(ObservableList<T> items,
                         ObservableList<T> values,
                         SelectorConvert<T> convert,
                         ColorType colorType, SizeType sizeType, HueType hueType,
                         SelectorType selectorType, SelectorCellFactory<T> cellFactory,
                         BooleanProperty multiple
                         ){
        this.items = items;
        this.values = values;
        this.colorType = colorType;
        this.sizeType = sizeType;
        this.hueType = hueType;
        this.convert = convert;
        this.selectorType = selectorType;
        this.cellFactory = cellFactory;
        this.multiple = multiple;

        this.init();
        
    }

    private void init(){
        //        this.getScene().getStylesheets().add(FxKit.getResourceURL("/css/control.css"));

        popupContentPane = new VBox();

        //添加阴影效果，阴影效果，会改变组件的尺寸，因此在执行doShow()方法的时候，x，y需要加一定偏移量，才是准确的
        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.THREE_PASS_BOX);
        shadow.setRadius(30);
        shadow.setColor(Color.web("#00000022"));
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);
        popupContentPane.setFillWidth(true);
//        popupContentPane.setEffect(shadow);

        ScrollBar scroll = new ScrollBar();
        scroll.setOrientation(Orientation.VERTICAL);

        popupContentPane.setEffect(shadow);

//        popupScroll.setStyle("-fx-background-color: white;");
        String color = "white";
        if(HueType.DARK.equals(hueType)){
            popupContentPane.setStyle("-fx-background-color:"
                    +FxKit.derivePaint(FxKit.getDarkPaint(colorType.getPaint(), 0.1), -0.8)
                    .toString().replace("0x", "#")
            );
        }else{
            popupContentPane.setStyle("-fx-background-color: white;");
        }

        this.getContent().add(popupContentPane);
        buildList();
    }

    /**
     * 构建菜单
     */
    public abstract void buildList();
    public SelectorPopup setPosition(double x, double y){
        this.setX(x);
        this.setY(y);
        return this;
    }
    public abstract void doShow(Node node, ObservableList<T> values, double x, double y);
    public abstract void doHide();

    public void dispose(){

    }

    public void removeItem(T t){

    }

}
