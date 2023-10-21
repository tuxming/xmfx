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
package com.xm2013.jfx.control.tableview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.base.VirtualFlowScrollHelper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class XmTableViewSkin<S> extends TableViewSkin<S> {
    private final XmTableView<S> control;

    private TableHeaderRow tableHeaderRow;

    public XmTableViewSkin(XmTableView<S> control) {
        super(control);
        this.control = control;

        VirtualFlowScrollHelper helper = new VirtualFlowScrollHelper(getVirtualFlow());
        helper.colorTypeProperty().bind(control.colorTypeProperty());

        tableHeaderRow = getTableHeaderRow();
        setBackground();
        updateHeaderColor();

        this.control.focusedProperty().addListener((ob, ov, nv)->{
            setBackground();
        });
        tableHeaderRow.getRootHeader().getColumnHeaders().addListener((ListChangeListener<Node>) c -> updateHeaderColor());

        registerChangeListener(control.sizeTypeProperty(), e -> updateSkin());
        registerChangeListener(control.colorTypeProperty(), e->updateSkin());
        registerChangeListener(control.hueTypeProperty(),  e->updateSkin());

    }

    private void updateHeaderColor(){
        Background background = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));

        tableHeaderRow.setBackground(background);
        tableHeaderRow.getRootHeader().setBackground(background);

        updateHeaderCellColor();

    }

    private void updateHeaderCellColor(){

        boolean isLight = control.getHueType().equals(HueType.LIGHT);

        Paint color = control.getHeaderBgColor();
        Paint borderRightColor = FxKit.getLightPaint(FxKit.getOpacityPaint(color, 1), 0.85);
        Paint borderRightColor1 = FxKit.getLightPaint(isLight?FxKit.getOpacityPaint(color, 1):color
                , 0.95);
        Paint borderColor = FxKit.derivePaint(color, -0.1);
        BorderWidths borderWidths = new BorderWidths(1);

        Background background = new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
        Border border = null;
        if(isLight){
            border = new Border(new BorderStroke(
                    Color.TRANSPARENT, borderRightColor1, borderColor, Color.TRANSPARENT,
                    BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, borderWidths, new Insets(0,-1,0,0)
            ), new BorderStroke(
                    Color.TRANSPARENT, borderRightColor, borderColor, Color.TRANSPARENT,
                    BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, borderWidths, new Insets(0,0,0,0)
            ), new BorderStroke(
                    Color.TRANSPARENT, borderRightColor1, borderColor, Color.TRANSPARENT,
                    BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, borderWidths, new Insets(0,1,0,0)
            ));
        }else{
            border = new Border(new BorderStroke(
                    Color.TRANSPARENT, borderRightColor, borderColor, Color.TRANSPARENT,
                    BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(0.5), new Insets(0,0,0,0)
            ));
        }

        for (Node child : tableHeaderRow.getChildren()) {
            if(child.getStyleClass().contains("filler")){
                Region r = (Region) child;
                r.setBackground(background);
                break;
            }
        }

        ObservableList<TableColumnHeader> columnHeaders = tableHeaderRow.getRootHeader().getColumnHeaders();
        for (TableColumnHeader columnHeader : columnHeaders) {

            columnHeader.setBackground(background);
            columnHeader.setBorder(border);
            columnHeader.setPrefHeight(40);

            Label label = (Label) columnHeader.getChildrenUnmodifiable().get(0);
            if(control.getHeaderFont()!=null){
                label.setFont(control.getHeaderFont());
            }else{
                label.setFont(Font.font(label.getFont().getFamily(), FontWeight.NORMAL, 16));
            }
            label.setTextFill(control.getHeaderFontColor());
        }
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
        this.control.setBorder(new Border(new BorderStroke(control.getBorderColor(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
    }

    @Override
    public void dispose() {
        super.dispose();

        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.hueTypeProperty());
    }
}
