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
package com.xm2013.jfx.control.data;

import com.xm2013.jfx.control.checkbox.XmCheckBox;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class XmCheckBoxGridCell<T> extends GridCell<T>{

    private final XmCheckBox<T> checkBox;
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");


    public XmCheckBoxGridCell(){
        checkBox = new XmCheckBox<>();

        checkBox.setAllowIndeterminate(false);
        checkBox.setMouseTransparent(true);

        checkBox.selectedProperty().addListener((ob, ov, nv)->{
            GridView<T> gridView = getGridView();
            if(nv){
                T item = getItem();
                if(gridView.isMultiple()){
                    if(!gridView.getValues().contains(item))
                        gridView.getValues().add(getItem());
                }else{
                    if(!gridView.getValues().contains(item)){
                        gridView.setValues(getItem());
                    }
                }
            }else{
                gridView.getValues().remove(getItem());
            }
        });

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            pseudoClassStateChanged(SELECTED, !checkBox.isSelected());
            checkBox.setSelected(!checkBox.isSelected());
        });

        gridViewProperty().addListener((ob, ov, nv)->{

            if(ov!=null){
                ov.getValues().removeListener(listChangeListener);
            }

            if(nv!=null){
                checkBox.setColorType(nv.getColorType());
                checkBox.setSizeType(nv.getSizeType());
                nv.getValues().remove(listChangeListener);
                nv.getValues().addListener(listChangeListener);
            }
        });
    }

    private final ListChangeListener<T> listChangeListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(Change<? extends T> c) {

            while(c.next()){
                if(c.wasAdded()){
                    if(c.getAddedSubList().contains(getItem())){
                        checkBox.setSelected(true);
                    }
                }
                if(c.wasRemoved()){
                    if(c.getRemoved().contains(getItem())){
                        checkBox.setSelected(false);
                    }
                }
            }

        }
    };

    public XmCheckBox<T> getCheckBox(){
        return checkBox;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

//        System.out.println(getCheckBox().isSelected()+","+item);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {

            GridView<T> gridView = getGridView();
            checkBox.setSelected(gridView.getValues().contains(item));

            Node graphic = getGraphic();
            if(graphic == null){
                setGraphic(checkBox);
            }else{
                Pane pane = new Pane(graphic, checkBox);
                checkBox.setLayoutX(10);
                setGraphic(pane);
            }

        }
    }
}
