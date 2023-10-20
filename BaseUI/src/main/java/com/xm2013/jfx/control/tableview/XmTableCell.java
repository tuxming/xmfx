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
                            new BorderWidths(0, 1, 0, 0),
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
