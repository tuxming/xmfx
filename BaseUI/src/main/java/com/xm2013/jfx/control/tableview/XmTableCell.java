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

import com.xm2013.jfx.control.base.HueType;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class XmTableCell<S,T> extends TableCell<S,T> {
    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");

    public XmTableCell(){
        tableRowProperty().addListener((ob, ov, nv)->{
            nv.getPseudoClassStates().addListener((SetChangeListener<PseudoClass>) change -> setTextColor());

            setAlignment(Pos.CENTER_LEFT);
            setTextColor();

            Paint borderColor = ((XmTableView) getTableView()).getBorderColor();
            setBorder(new Border(
                    new BorderStroke(Color.TRANSPARENT, borderColor, Color.TRANSPARENT, Color.TRANSPARENT,
                            BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                            CornerRadii.EMPTY,
                            new BorderWidths(0, 0.5, 0, 0),
                            new Insets(0, 0,0,0)
            )));
        });

    }

    private void setTextColor(){

        boolean isSelected = getTableRow().getPseudoClassStates().contains(selected);

        if(isSelected){
            setTextFill(Color.WHITE);
        }else{
            XmTableView xtv = (XmTableView) getTableView();
            if(xtv.getCellTextColor() == null){
                if(HueType.LIGHT.equals(xtv.getHueType())){
                    setTextFill(Color.web("#333333"));
                }else{
                    setTextFill(Color.web("#eeeeee"));
                }
            }else{
                setTextFill(xtv.getCellTextColor());
            }
        }
    }


}
