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
package com.xm2013.jfx.control.label;

import com.xm2013.jfx.control.base.XmAlignment;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.List;

public class XmLabelSkin extends SelectableTextSkin {


    private XmLabel control;
    private Text text;
    private Pane stextPane;
//    private SelectableText stext;

    public XmLabelSkin(XmLabel selectableLabel) {
        super(selectableLabel);
        control = selectableLabel;
        stextPane = (Pane) this.getChildren().get(0);

//        stext = new SelectableText();
//        stext.setText(control.getText());
//        stext.setFont(control.getFont());
//        stext.setLineSpace(control.getLineSpacing());
//        stext.setTextAlignment(control.getTextAlignment());
//        stext.setUnderline(control.isUnderline());
//        stext.setWrapText(control.isWrapText());
//
//        stext.textProperty().bind(control.textProperty());
//        stext.fontProperty().bind(control.fontProperty());
//        stext.textFillProperty().bind(control.textFillProperty());
//        stext.lineSpaceProperty().bind(control.lineSpacingProperty());
//        stext.textAlignmentProperty().bind(control.textAlignmentProperty());
//        stext.underlineProperty().bind(control.underlineProperty());
//        stext.ellipsisStringProperty().bind(control.ellipsisStringProperty());
//        stext.wrapTextProperty().bind(control.wrapTextProperty());

        updateChildren();

        registerChangeListener(control.graphicProperty(), e -> updateChildren());
        registerChangeListener(control.contentDisplayProperty(), e -> updateChildren());
        registerChangeListener(control.graphicTextGapProperty(), e-> updateChildren());
        registerChangeListener(control.prefWidthProperty(), e->updateChildren());
        registerChangeListener(control.prefHeightProperty(), e->updateChildren());

    }

    private void updateChildren() {

        ContentDisplay display = this.control.getContentDisplay();
        if(ContentDisplay.TEXT_ONLY.equals(display)){
            this.getChildren().setAll(stextPane);
            return ;
        }

        Node graphic = this.control.getGraphic();
        if(graphic!=null && ContentDisplay.GRAPHIC_ONLY.equals(display)){
            this.getChildren().setAll(graphic);
            return ;
        }

        if(graphic != null){
            this.getChildren().setAll(graphic, stextPane);
        }else{
            this.getChildren().setAll(stextPane);
        }

        setSize(display);
    }

    public void setSize(ContentDisplay display){

        double width = control.getPrefWidth(),
                height = control.getPrefHeight(),
                graphicWidth = 0, graphicHeight=0;

        Node graphic = control.getGraphic();
        if(graphic!=null){
            graphic.autosize();
            Bounds bounds = graphic.getLayoutBounds();
            graphicWidth = bounds.getWidth();
            graphicHeight = bounds.getHeight();
        }

        if(width>-1 && this.control.getTextWidth()<=0){
            if(ContentDisplay.LEFT.equals(display) || ContentDisplay.RIGHT.equals(display)){
//                stext.setTextWidth(width - graphicWidth - control.getGraphicTextGap());
//                stextPane.setPrefWidth(width - graphicWidth - control.getGraphicTextGap());
                this.control.setTextWidth(width - graphicWidth - control.getGraphicTextGap());
            }else{

//                stext.setTextWidth(width);
//                stextPane.setPrefWidth(width);
                this.control.setTextWidth(width);
            }
        }

        if(height>-1 && this.control.getTextHeight()<=0){
            if(ContentDisplay.TOP.equals(display) || ContentDisplay.BOTTOM.equals(display)){
//                stext.setTextHeight(height - graphicHeight - control.getGraphicTextGap());
                this.control.setTextHeight(height - graphicHeight - control.getGraphicTextGap());
            }else{
//                stext.setTextHeight(height);
                this.control.setTextHeight(height);
            }
        }
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width = 0;
        ContentDisplay display = this.control.getContentDisplay();

        Node graphic = this.control.getGraphic();

        if(ContentDisplay.TEXT_ONLY.equals(display)){
//            width = stext.prefWidth(-1);
            width = stextPane.prefWidth(-1);
        }else  if(ContentDisplay.GRAPHIC_ONLY.equals(display)){
            if(graphic != null){
                width = graphic.prefWidth(-1) ;
            }
        }else{
            if(graphic == null || ContentDisplay.TOP.equals(display) || ContentDisplay.BOTTOM.equals(display)){
//                width = stext.prefWidth(-1) ;
                width = stextPane.prefWidth(-1);
            }else {
//                width = stext.prefWidth(-1) + graphic.prefWidth(-1)+control.getGraphicTextGap() ;
                width = stextPane.prefWidth(-1) + graphic.prefWidth(-1)+control.getGraphicTextGap() ;
            }
        }

        width += leftInset + rightInset ;

        return width;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {

        ContentDisplay display = this.control.getContentDisplay();
        Node graphic = this.control.getGraphic();

        double height = 0;

        if(graphic!=null){
            if(ContentDisplay.GRAPHIC_ONLY.equals(display)){
                height = graphic.prefHeight(-1);
            }else {
//                height = stext.prefHeight(-1);
                height = stextPane.prefHeight(-1);
                if(ContentDisplay.TOP.equals(display) || ContentDisplay.BOTTOM.equals(display)){
                    height += graphic.prefHeight(-1) + control.getGraphicTextGap();
                }else {
                    double graphicHeight = graphic.prefHeight(-1);
                    height = Math.max(graphicHeight, height);
                    height += control.getGraphicTextGap();
                }
            }
        }else{
//            height = stext.prefHeight(-1);
            height = stextPane.prefHeight(-1);
        }

        height += topInset + bottomInset ;

        height = Math.max(height, super.computePrefHeight(width,topInset, leftInset, bottomInset, leftInset));

        return height;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        ContentDisplay display = this.control.getContentDisplay();
        XmAlignment align = control.getAlignment();

        double width = contentWidth,
                height = contentHeight;

        Node graphic = control.getGraphic();

        double textX = 0, textY = 0,
                graphicX = 0, graphicY = 0,
                textWidth = stextPane.prefWidth(-1),
                textHeight = stextPane.prefHeight(-1),
//                textWidth = stext.prefWidth(-1),
//                textHeight = stext.prefHeight(-1),
                gap = control.getGraphicTextGap(),
                borderLeft = 0, borderRight = 0;
        Insets padding = control.getPadding();

        Border border = control.getBorder();
        if(border != null){
            List<BorderStroke> strokes = border.getStrokes();
            if(strokes.size()>0){
                BorderWidths bs = strokes.get(0).getWidths();
                borderLeft = bs.getLeft();
                borderRight = bs.getRight();
            }
        }

        if(graphic == null || ContentDisplay.TEXT_ONLY.equals(display)){
            if(XmAlignment.LEFT.equals(align)){
                textX = padding.getLeft()+borderLeft;
            }else if(XmAlignment.RIGHT.equals(align)){
                textX = width - padding.getRight() - textWidth - borderRight;
            }else{
                textX = (width - textWidth)/2;
            }
            textY = (height-textHeight)/2;
//            layoutInArea(stext, textX, textY, textWidth, textHeight, 0, HPos.CENTER, VPos.CENTER);
            layoutInArea(stextPane, textX, textY, textWidth, textHeight, 0, HPos.CENTER, VPos.CENTER);
        }else{
            double graphicWidth = graphic.prefWidth(-1),
                    graphicHeight = graphic.prefHeight(-1);

            if(graphic != null &&  ContentDisplay.GRAPHIC_ONLY.equals(display)){

                if(XmAlignment.LEFT.equals(align)){
                    graphicX = padding.getLeft()+borderLeft;
                }else if(XmAlignment.RIGHT.equals(align)){
                    graphicX = width - padding.getRight() - graphicWidth - borderRight;
                }else{
                    graphicX = (width - graphicWidth)/2;
                }
                graphicY = (height-graphicHeight)/2;

                layoutInArea(graphic, graphicX, graphicY, graphicWidth, graphicHeight, 0, HPos.CENTER, VPos.CENTER);
            }else {

                if(ContentDisplay.LEFT.equals(display)){
                    textY = (height - textHeight) / 2;
                    graphicY = (height - graphicHeight) / 2;
                    if(XmAlignment.LEFT.equals(align)){
                        graphicX = padding.getLeft() + borderLeft;
                        textX = graphicX + graphicWidth + gap;
                    }else if(XmAlignment.RIGHT.equals(align)){
                        graphicX = width - padding.getRight() - textWidth - gap - graphicWidth - borderRight;
                        textX = graphicX + graphicWidth + gap;
                    }else if(XmAlignment.CENTER.equals(align)){
                        graphicX = (width - textWidth - gap - graphicWidth) / 2;
                        textX = graphicX + graphicWidth + gap;
                    }else if(XmAlignment.JUSTIFY.equals(align)){
                        graphicX = padding.getLeft();
                        textX = width - padding.getRight() - textWidth;
                    }
                }else if(ContentDisplay.RIGHT.equals(display)){
                    textY = (height - textHeight) / 2;
                    graphicY = (height - graphicHeight) / 2;
                    if(XmAlignment.LEFT.equals(align)){
                        textX = padding.getLeft()+borderLeft;
                        graphicX = textX + textWidth + gap;
                    }else if(XmAlignment.RIGHT.equals(align)){
                        textX = width - padding.getRight() - textWidth - gap - graphicWidth - borderRight;
                        graphicX = textX + textWidth + gap;
                    }else if(XmAlignment.CENTER.equals(align)){
                        textX = (width - textWidth - gap - graphicWidth) / 2;
                        graphicX = textX + textWidth + gap;
                    }else if(XmAlignment.JUSTIFY.equals(align)){
                        textX = padding.getLeft();
                        graphicX = width - padding.getRight() - graphicWidth;
                    }
                }else if(ContentDisplay.TOP.equals(display)){
                    if(XmAlignment.LEFT.equals(align)){
                        graphicX = padding.getLeft();
                        graphicY = (height - graphicHeight - textHeight - gap) / 2;
                        textX = padding.getLeft();
                        textY = graphicY + graphicHeight + gap;
                    }else if(XmAlignment.RIGHT.equals(align)){
                        graphicX = width - graphicWidth - padding.getRight();
                        graphicY = (height - graphicHeight - textHeight - gap) / 2;
                        textX = width - textWidth - padding.getRight();
                        textY = graphicY + graphicHeight + gap;
                    }else if(XmAlignment.CENTER.equals(align)){
                        graphicX = (width - graphicWidth) / 2;
                        graphicY = (height - graphicHeight - textHeight - gap) / 2;
                        textX = (width - textWidth) / 2;
                        textY = graphicY + graphicHeight + gap;
                    }else if(XmAlignment.JUSTIFY.equals(align)){
                        graphicX =  (width - graphicWidth) / 2;
                        graphicY = padding.getTop();
                        textX = (width - textWidth) / 2;
                        textY = height - padding.getBottom() - textHeight;
                    }
                }else if(ContentDisplay.BOTTOM.equals(display)){
                    if(XmAlignment.LEFT.equals(align)){
                        textX = padding.getLeft();
                        textY = (height - graphicHeight - textHeight - gap) / 2;
                        graphicX = padding.getLeft();
                        graphicY = textY + graphicHeight + gap;
                    }else if(XmAlignment.RIGHT.equals(align)){
                        textX = width - textWidth - padding.getRight();
                        textY = (height - graphicHeight - textHeight - gap) /  2;
                        graphicX = width - graphicWidth - padding.getRight();
                        graphicY = textY + textHeight + gap;
                    }else if(XmAlignment.CENTER.equals(align)){
                        textX = (width - textWidth)/2;
                        textY = (height - graphicHeight - textHeight - gap) /  2;
                        graphicX = (width - graphicWidth)/2;
                        graphicY = textY + textHeight+gap;
                    }else if(XmAlignment.JUSTIFY.equals(align)){
                        textX = (width - textWidth) / 2;
                        textY = padding.getTop();
                        graphicX = (width - graphicHeight) / 2;
                        graphicY = height - padding.getBottom() - textHeight;
                    }
                }

                layoutInArea(stextPane, textX, textY, textWidth, textHeight, 0, HPos.CENTER, VPos.CENTER);
//                layoutInArea(stext, textX, textY, textWidth, textHeight, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(graphic, graphicX, graphicY, graphicWidth, graphicHeight, 0, HPos.CENTER, VPos.CENTER);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        unregisterChangeListeners(control.graphicProperty());
        unregisterChangeListeners(control.contentDisplayProperty());
        unregisterChangeListeners(control.graphicTextGapProperty());
        unregisterChangeListeners(control.prefWidthProperty());
        unregisterChangeListeners(control.prefHeightProperty());
    }
}
