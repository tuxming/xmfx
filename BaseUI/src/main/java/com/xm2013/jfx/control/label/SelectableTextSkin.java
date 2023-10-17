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

import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

public class SelectableTextSkin extends SkinBase<SelectableText> {
    private final KeyCodeCombination ctrlC;
    private SelectableText control;

    //文本节点
    private Text textNode;

    private int startIndex;
    private int endIndex;

    private Path selectPath;
    private boolean mousePress = false;

    public SelectableTextSkin(SelectableText control) {
        super(control);
        this.control = control;
        textNode = new Text();
        textNode.getStyleClass().add("text");
        textNode.setTextOrigin(VPos.TOP);
        textNode.setFill(control.getTextFill());
        textNode.setFont(control.getFont());
        textNode.setSelectionFill(control.getSelectedTextFill());
        textNode.setTextAlignment(control.getTextAlignment());

        textNode.fontProperty().bind(this.control.fontProperty());

//        textNode.textProperty().bind(control.textProperty());
        textNode.fillProperty().bind(control.textFillProperty());
        textNode.strokeProperty().bind(control.textStrokeProperty());
        textNode.strokeWidthProperty().bind(control.textStrokeWidthProperty());
        textNode.strikethroughProperty().bind(control.textStrikeThroughProperty());
        textNode.strokeTypeProperty().bind(control.textStrokeTypeProperty());
        textNode.strokeDashOffsetProperty().bind(control.textStrokeDashOffsetProperty());
        textNode.strokeLineCapProperty().bind(control.textStrokeLineCapProperty());
        textNode.fontSmoothingTypeProperty().bind(control.textSmoothingTypeProperty());
        textNode.textAlignmentProperty().bind(control.textAlignmentProperty());
//        textNode.textOriginProperty().bind(control.textOriginProperty());
        textNode.underlineProperty().bind(control.underlineProperty());
        textNode.effectProperty().bind(control.textEffectProperty());
        textNode.selectionFillProperty().bind(control.selectedTextFillProperty());
        textNode.lineSpacingProperty().bind(control.lineSpaceProperty());

        this.selectPath = new Path();
        selectPath.setStroke(null);
        selectPath.setManaged(false);
//        selectPath.setFill(Color.RED);
        selectPath.setFill(control.getSelectedFill());
        selectPath.fillProperty().bind(control.selectedFillProperty());

        //添加鼠标事件
        textNode.addEventFilter(MouseEvent.MOUSE_PRESSED, textMousePressHandler);
        textNode.addEventFilter(MouseEvent.MOUSE_DRAGGED, textMouseDraggedHandler);
        textNode.addEventFilter(MouseEvent.MOUSE_RELEASED, textMouseReleaseHandler);

        //这种使用方法，是为了，将关于text的属性放在一个pane中，方便其他组件继承该类实现其他功能。
        Pane pane = new Pane(selectPath,textNode);
        pane.getStyleClass().add("text-pane");
        this.getChildren().addAll(pane);


//        this.getChildren().addAll(selectPath,textNode);

        //添加ctrl+c复制选中文本
        ctrlC = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN);
        Runnable ctrlCRunnable = () -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(control.getSelectedText());
            clipboard.setContent(content);
        };

        //添加全选ctrl+a
        KeyCodeCombination ctrlA = new KeyCodeCombination(KeyCode.A, KeyCodeCombination.CONTROL_DOWN);
        Runnable ctrlARunnable = () -> {
            if(this.control.isFocused()){
                int start = 0, end = control.getText().length();
                this.textNode.setSelectionStart(start);
                this.textNode.setSelectionEnd(end);
                selectPath.getElements().clear();
                selectPath.getElements().addAll(textNode.getSelectionShape());
                this.control.setSelectedStart(0);
                this.control.setSelectedEnd(0);
                this.control.setSelectedText(control.getText());
            }
        };

        textNode.getScene().getAccelerators().put(ctrlC, ctrlCRunnable);

        textNode.getScene().getAccelerators().put(ctrlA, ctrlARunnable);
        textNode.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, sceneClickHandler);

        textNode.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, sceneClickHandler);

        updateDisplayText();

        registerChangeListener(control.lineSpaceProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.fontProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.wrapTextProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.textWidthProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.textHeightProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.maxRowProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.wrapTextProperty(), e->{
            updateDisplayText();
        });

        registerChangeListener(control.textProperty(), e->updateDisplayText());

        //添加到pane
//        textPane.getChildren().add(textNode);
    }

    private final EventHandler<MouseEvent> sceneClickHandler = e -> {
        if(!e.getTarget().equals(textNode)){
            textNode.setSelectionEnd(-1);
            textNode.setSelectionStart(-1);
            control.setSelectedEnd(-1);
            control.setSelectedStart(-1);
            control.setSelectedText(null);
            selectPath.getElements().clear();
        }else{
            this.control.requestFocus();
        }

    };

    /* ------------------------Listener -----------------*/
    //绑定禁用属性
    private final ChangeListener<Boolean> disableChangeListener = (ob, ov, nv) -> textNode.setDisable(nv);


    /**
     * 鼠标按下
     */
    private final EventHandler<MouseEvent> textMousePressHandler = e -> {
        int currIndex = textNode.hitTest(new Point2D(e.getX(), e.getY())).getCharIndex();
        if(startIndex>-1 && endIndex>-1 && currIndex >=startIndex && currIndex<=endIndex){
            return;
        }else{
            startIndex = currIndex;
            endIndex = -1;
            textNode.setSelectionStart(-1);
            textNode.setSelectionEnd(-1);
            selectPath.getElements().clear();
        }
    };

    /**
     * 鼠标按下移动
     */
    private final EventHandler<MouseEvent> textMouseDraggedHandler = e -> {

        mousePress = true;

        int currIndex =  textNode.hitTest(new Point2D(e.getX(), e.getY())).getCharIndex();
        if(currIndex == endIndex)
        {
            return;
        }

        int sIndex = startIndex, eIndex = currIndex;
        if(sIndex > eIndex){
            eIndex = sIndex;
            sIndex = currIndex;
        }

        textNode.setSelectionStart(sIndex);
        textNode.setSelectionEnd(eIndex);
        selectPath.getElements().clear();
        selectPath.getElements().addAll(textNode.getSelectionShape());
    };

    /**
     * 鼠标释放
     */
    private final EventHandler<MouseEvent> textMouseReleaseHandler = e -> {
        if(mousePress){
            int currIndex =  textNode.hitTest(new Point2D(e.getX(), e.getY())).getCharIndex();

            if(startIndex>currIndex ){
                int temp = startIndex;
                startIndex = currIndex;
                endIndex = temp;
            }else{
                endIndex = currIndex;
            }

            String text = control.getText();
            int endIdx = endIndex, length = text.length();
            if(endIdx>length){
                endIdx = length;
            }

            control.setSelectedText(text.substring(startIndex, endIdx));
            mousePress = false;
        }
    };

    /* ------------------- private method -------------------*/

    /**
     * 如果 自动换行设置为false, 如果设置了宽度， 超过的宽度的文本 用 ... 替代
     * 如果 自动换行设置为true,  必须设置宽度，如果设置了高度，超过高度的部分的文本 用...替代
     */
    private void updateDisplayText(){

        String text = this.control.getText();

        if(text==null || text.length()==0){
            return;
        }

        Text tempText = new Text();
        tempText.setFont(this.control.getFont());
        tempText.setLineSpacing(this.control.getLineSpace());

        double maxWidth = this.control.getTextWidth();
        double maxHeight = this.control.getTextHeight();
        int maxRow = this.control.getMaxRow();

        tempText.setText(text);
        double textWidth = tempText.prefWidth(-1);
        tempText.setText("");

        String showText = text;

        if(control.isWrapText()){
            if(maxWidth>0 && textWidth>maxWidth){
                this.textNode.setWrappingWidth(maxWidth);
            }
//            if(width>0 && textWidth>width){
//            if((height> 0 || maxRow>0)  && width > 0 && textWidth > width){
            if((maxHeight> 0 || maxRow>0)  && maxWidth > 0){

                tempText.setWrappingWidth(maxWidth);

                StringBuilder lineBuilder = new StringBuilder();
                lineBuilder.append(text.charAt(0));

                tempText.setText(lineBuilder.toString());
                double lineHeight = tempText.getLayoutBounds().getHeight();
                double prevHeight = lineHeight;

                int len = text.length(),
                        row = 1,
                        endIndex = 1;

                for(int i=1; i<len; i++){

                    char c = text.charAt(i);
                    lineBuilder.append(c);

                    tempText.setText(lineBuilder.toString());
                    double currHeight = tempText.getLayoutBounds().getHeight();

                    if( currHeight > prevHeight){
                        row++;
                        prevHeight = currHeight + row;
                        if((maxHeight>-1 && prevHeight > maxHeight) || (maxRow>-1 && row>maxRow)){
                            endIndex = i-1;  //表示取当前的位置已经换行了，所以要取当前的上一个index
                            break;
                        }
                    }else if((i+1)>=len){
                        endIndex = i;  //表示全部遍历完都没有换行
                    }
                }

                char lastChar = lineBuilder.charAt(endIndex);
                boolean isLastWide = (lastChar >= '\u4E00' && lastChar <= '\u9FA5');

                if(isLastWide){
                    if(endIndex>1){
                        showText = lineBuilder.substring(0, endIndex)+control.getEllipsisString();
                    }else{
                        showText = lineBuilder.substring(0, 1);
                    }
                }else{
                    if(endIndex>2){
                        showText = lineBuilder.substring(0, endIndex-2)+control.getEllipsisString();
                    }else{
                        showText = lineBuilder.substring(0, 2);
                    }
                }
            }
        }else{
            if(maxWidth > 0 && textWidth>maxWidth){
                StringBuilder lineBuilder = new StringBuilder();
                int len = text.length(), index = 0;
                for(int i=0; i<len; i++){
                    char c = text.charAt(i);
                    lineBuilder.append(c);
                    tempText.setText(lineBuilder.toString());

                    double currWidth = tempText.prefWidth(-1);
                    index = i;
                    if(currWidth>=maxWidth){
                        break;
                    }

                }
                char lastChar = lineBuilder.charAt(index-1);
                boolean isLastWide = (lastChar >= '\u4E00' && lastChar <= '\u9FA5');

                if(isLastWide){
                    if(index>1){
                        showText = lineBuilder.substring(0, index-1)+control.getEllipsisString();
                    }else{
                        showText = lineBuilder.substring(0, index);
                    }
                }else{
                    if(index>2){
                        showText = lineBuilder.substring(0, index-2)+control.getEllipsisString();
                    }else{
                        showText = lineBuilder.substring(0, index);
                    }
                }

//                StringBuilder lineBuilder = new StringBuilder();
//
//                double lineHeight = tempText.getLayoutBounds().getHeight();
//                double prevHeight = lineHeight;
//
//                int len = text.length(),
//                        index=0;
//                for(int i=0; i<len; i++){
//
//                    char c = text.charAt(i);
//                    lineBuilder.append(c);
//
//                    tempText.setText(lineBuilder.toString());
//                    double currHeight = tempText.getLayoutBounds().getHeight();
//
//                    if( currHeight > prevHeight){
//                        index = i-1;   //表示取当前的位置已经换行了，所以要取当前的上一个index
//                        break;
//                    }else if((i+1)>=len){
//                        index = i; //表示全部遍历完都没有换行
//                    }
//                }
//                if(index <= 0){
//                    index = 1;
//                }
//                char lastChar = lineBuilder.charAt(index-1);
//                boolean isLastWide = (lastChar >= '\u4E00' && lastChar <= '\u9FA5');
//
//                if(isLastWide){
//                    if(index>1){
//                        showText = lineBuilder.substring(0, index-1)+control.getEllipsisString();
//                    }else{
//                        showText = lineBuilder.substring(0, 1);
//                    }
//                }else{
//                    if(index>2){
//                        showText = lineBuilder.substring(0, index-2)+control.getEllipsisString();
//                    }else{
//                        showText = lineBuilder.substring(0, 2);
//                    }
//                }
            }
        }

        textNode.setText(showText);

    }

    /*-------------------override----------------------*/

    @Override
    public void dispose() {
        super.dispose();

        textNode.textProperty().unbind();
        textNode.fillProperty().unbind();
        textNode.strokeProperty().unbind();
        textNode.strokeWidthProperty().unbind();
        textNode.strikethroughProperty().unbind();
        textNode.strokeTypeProperty().unbind();
        textNode.strokeDashOffsetProperty().unbind();
        textNode.strokeLineCapProperty().unbind();
        textNode.fontSmoothingTypeProperty().unbind();
        textNode.textAlignmentProperty().unbind();
//      textNode.textOriginProperty().unbind();
        textNode.underlineProperty().unbind();
        textNode.effectProperty().unbind();

        control.disabledProperty().removeListener(disableChangeListener);

        textNode.removeEventHandler(MouseEvent.MOUSE_PRESSED, textMousePressHandler);
        textNode.removeEventHandler(MouseEvent.MOUSE_DRAGGED, textMouseDraggedHandler);
        textNode.removeEventHandler(MouseEvent.MOUSE_RELEASED, textMouseReleaseHandler);

        selectPath.fillProperty().unbind();

        textNode.getScene().getAccelerators().remove(ctrlC);

        unregisterChangeListeners(control.lineSpaceProperty());
        unregisterChangeListeners(control.fontProperty());
        unregisterChangeListeners(control.wrapTextProperty());
        unregisterChangeListeners(control.textWidthProperty());
        unregisterChangeListeners(control.textHeightProperty());
        unregisterChangeListeners(control.maxRowProperty());
        unregisterChangeListeners(control.wrapTextProperty());
        unregisterChangeListeners(control.textProperty());
    }

}
