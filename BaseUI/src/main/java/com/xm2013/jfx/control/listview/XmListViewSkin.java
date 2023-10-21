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

import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.base.VirtualFlowScrollHelper;
import javafx.geometry.Insets;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class XmListViewSkin<T> extends ListViewSkin<T> {

    private XmListView<T> control;

    public XmListViewSkin(XmListView<T> control) {
        super(control);
        this.control = control;

        updateSkin();

        VirtualFlowScrollHelper helper = new VirtualFlowScrollHelper(getVirtualFlow());
        helper.colorTypeProperty().bind(control.colorTypeProperty());

        this.control.focusedProperty().addListener((ob, ov, nv)->{
            setBackground();
        });

        registerChangeListener(control.sizeTypeProperty(), e -> updateSkin());
        registerChangeListener(control.colorTypeProperty(), e->updateSkin());
        registerChangeListener(control.hueTypeProperty(),  e->updateSkin());

    }

    private void updateSkin(){

        SizeType sizeType = control.getSizeType();
        double size =  40;
        if(SizeType.SMALL.equals(sizeType)){
            size = 30;
        }else if(SizeType.LARGE.equals(sizeType)){
            size = 50;
        }
        this.control.setFixedCellSize(size);

        setBackground();
    }

    private void setBackground(){
        Paint bgColor = null;
        HueType hueType = control.getHueType();
        if(HueType.LIGHT.equals(hueType)){
            bgColor = Color.WHITE;
        }else {
            bgColor = Color.TRANSPARENT;
        }

        this.control.setBackground(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    @Override
    public void dispose() {
        super.dispose();

        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.hueTypeProperty());
    }
}
