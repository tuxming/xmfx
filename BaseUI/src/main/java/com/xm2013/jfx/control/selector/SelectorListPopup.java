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

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.listview.XmCheckBoxListCell;
import com.xm2013.jfx.control.listview.XmListCell;
import com.xm2013.jfx.control.listview.XmListView;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * 垂直列表
 */
public class SelectorListPopup<T> extends SelectorPopup<T>{

    private XmListView<T> listView;

    public SelectorListPopup(XmSelector<T> control){
        super(control);
    }

    public SelectorListPopup(ObservableList<T> items, ObservableList<T> values,
                             SelectorConvert<T> convert, ColorType colorType, SizeType sizeType, HueType hueType,
                             SelectorType selectorType, SelectorCellFactory<T> cellFactory,
                             BooleanProperty multiple) {
        super(items, values, convert, colorType, sizeType,hueType, selectorType, cellFactory, multiple);
    }

    @Override
    public void buildList() {

        listView = new XmListView<>();
        listView.setHueType(HueType.LIGHT);
        listView.setColorType(colorType);
        listView.setSizeType(sizeType);
        listView.setHueType(hueType);
        listView.getStyleClass().add("xm-selector-list");

        if(multiple.get()){
            listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
                @Override
                public ListCell<T> call(ListView<T> param) {
                    return new XmCheckBoxListCell<>(){
                        @Override
                        public void updateItem(T item, boolean empty) {
                            super.updateItem(item, empty);
                            if(cellFactory!=null){
                                cellFactory.updateItem(this, item, empty);
                            }else{
                                if(item == null || empty){
                                    setText(null);
                                    setGraphic(null);
                                }else{
                                    setText(convert.toString(item));
                                }
                            }
                        }
                    };
                }
            });
        }else{
            listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
                @Override
                public ListCell<T> call(ListView<T> param) {
                    return new XmListCell<>(){
                        @Override
                        protected void updateItem(T item, boolean empty) {
                            super.updateItem(item, empty);
                            if(cellFactory!=null){
                                cellFactory.updateItem(this, item, empty);
                            }else{
                                if(item == null || empty){
                                    setText(null);
                                    setGraphic(null);
                                }else{
                                    setText(convert.toString(item));
                                }
                            }
                        }
                    };
                }
            });
        }

        listView.setFocusTraversable(false);
        listView.setItems(items);
        this.popupContentPane.getChildren().add(listView);

        setSelected(values);
    }

    private void setSelected(ObservableList<T> values){
        MultipleSelectionModel<T> selectionModel = listView.getSelectionModel();

        selectionModel.clearSelection();
        if(multiple.get()){
            List<T> newValues = new ArrayList<>();
            for(T value : values){
                if(items.contains(value)){
                    newValues.add(value);
                }
            }

            listView.setCheckedValues(newValues);
        }else{
            if(values.size()>0){
                selectionModel.select(values.get(values.size()-1));
            }
        }
    }

    @Override
    public void doShow(Node node, ObservableList<T> values, double x, double y) {
        setSelected(values);

        if(multiple.get()){
            listView.getCheckedValues().addListener(checkedListener);
        }else{
            listView.getSelectionModel().getSelectedItems().addListener(selectionChange);
        }

        listView.setPrefWidth(node.getLayoutBounds().getWidth());

        double fixedCellSize = listView.getFixedCellSize();
        if(fixedCellSize <= 0){
            fixedCellSize = 30;
        }else {
            fixedCellSize += 2;
        }

        double height = listView.getItems().size() * fixedCellSize;
        if(height > 400){
            height = 400;
        }

        listView.setPrefHeight(height);

        this.show(node, x, y);
    }

    @Override
    public void doHide() {
        if(multiple.get()){
            listView.getCheckedValues().removeListener(checkedListener);
        }else{
            listView.getSelectionModel().getSelectedItems().removeListener(selectionChange);
        }
        this.hide();
    }

    @Override
    public void removeItem(T t){
        int idx = items.indexOf(t);
        if(idx>-1){
            listView.getSelectionModel().clearSelection(idx);
        }
    }

    private ListChangeListener<T> selectionChange = new ListChangeListener<T>() {
        @Override
        public void onChanged(Change<? extends T> c) {
            values.setAll(listView.getSelectionModel().getSelectedItem());
        }
    };

    private ListChangeListener<T> checkedListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(Change<? extends T> c) {
            ObservableList<T> selectedItems = listView.getCheckedValues();
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

}
