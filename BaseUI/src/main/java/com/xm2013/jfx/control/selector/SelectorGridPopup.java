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

import java.util.ArrayList;
import java.util.List;

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.gridview.GridCell;
import com.xm2013.jfx.control.gridview.GridView;
import com.xm2013.jfx.control.gridview.XmCheckBoxGridCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MultipleSelectionModel;
import javafx.util.Callback;


public class SelectorGridPopup<T> extends SelectorPopup<T>{

    private GridView<T> gridView;

    public SelectorGridPopup(XmSelector<T> control){
        super(control);
    }

    public SelectorGridPopup(ObservableList<T> items, ObservableList<T> values, SelectorConvert<T> convert,
                             ColorType colorType, SizeType sizeType, HueType hueType, SelectorType selectorType,
                             SelectorCellFactory<T> cellFactory, BooleanProperty multiple) {
        super(items, values, convert, colorType, sizeType, hueType, selectorType, cellFactory, multiple);
    }

    @Override
    public void buildList() {

        gridView = new GridView<>(items);
//        gridView.multipleProperty().bind(multiple);
        gridView.setPrefHeight(400);

        if(multiple.get()){
            gridView.setCellFactory(new Callback<GridView<T>, GridCell<T>>() {
                public GridCell<T> call(GridView<T> gridView) {
                    return new XmCheckBoxGridCell<T>(){
                        @Override
                        public void updateItem(T item, boolean empty) {
                            if(cellFactory!=null){
                                cellFactory.updateItem(this, item, empty);
                            }
                            super.updateItem(item, empty);
                        }
                    };
                }
            });
        }else{
            gridView.setCellFactory(new Callback<GridView<T>, GridCell<T>>() {
                @Override
                public GridCell<T> call(GridView<T> param) {
                    return new GridCell<>(){
                        @Override
                        protected void updateItem(T item, boolean empty) {
                            if(cellFactory!=null){
                                cellFactory.updateItem(this, item, empty);
                            }
                            super.updateItem(item, empty);
                        }
                    };
                }
            });
        }


        this.popupContentPane.getChildren().add(gridView);

//        gridView.getValues().addListener(new ListChangeListener<T>() {
//            @Override
//            public void onChanged(Change<? extends T> c) {
//
//            	List<T> oldValues = new ArrayList<>(values);
//            	values.clear();
//            	for (T t : oldValues) {
//					if(!items.contains(t)) {
//						values.add(t);
//					}
//				}
//            	values.addAll(gridView.getValues());
//
//            }
//        });
    }

    private void setSelected(ObservableList<T> values){
        if(multiple.get()){
            List<T> newValues = new ArrayList<>();
            for(T value : values){
                if(items.contains(value)){
                    newValues.add(value);
                }
            }
            gridView.setCheckedValues(newValues);
        }else{
            if(values.size()>0){
                gridView.setValue(values.get(values.size()-1));
            }
        }
    }

    @Override
    public void doShow(Node node, ObservableList<T> values, double x, double y) {
        setSelected(values);

        if(multiple.get()){
            gridView.getCheckedValues().addListener(checkedListener);
        }else{
            gridView.valueProperty().addListener(selectionListener);
        }

        gridView.setPrefWidth(node.getLayoutBounds().getWidth());
        this.show(node, x, y);
    }

    @Override
    public void doHide() {
        this.hide();
    }

    @Override
    public void removeItem(T t) {
//        gridView.getValues().remove(t);
    }

    private ListChangeListener<T> checkedListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(Change<? extends T> c) {
            ObservableList<T> selectedItems = gridView.getCheckedValues();
            List<T> oldValues = new ArrayList<>(values);
            values.clear();

            for(T old : oldValues){
                boolean is = items.contains(old);
                if(!is) {
                    values.add(old);
                }
            }
            values.addAll(selectedItems);
        }
    };

    private ChangeListener<T> selectionListener = (ob, ov, nv)->{
        values.setAll(nv);
    };

}
