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
import com.xm2013.jfx.control.listview.XmListCell;
import com.xm2013.jfx.control.listview.XmListView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
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
        listView.setFocusTraversable(false);
        listView.setItems(items);
        this.popupContentPane.getChildren().add(listView);

        MultipleSelectionModel<T> selectionModel = listView.getSelectionModel();
        selectionModel.selectionModeProperty().bind(
                Bindings.createObjectBinding(
                        ()-> multiple.get()? SelectionMode.MULTIPLE:SelectionMode.SINGLE,
                        super.multiple
                ));

        setSelected(values);

//        values.addListener(new ListChangeListener<T>() {
//            @Override
//            public void onChanged(Change<? extends T> c) {
//                while(c.next()){
//                    if(c.wasRemoved()){
//                        List<? extends T> removes = c.getRemoved();
//                        for(T v : removes){
//                            int idx = items.indexOf(v);
//                            if(idx > -1){
//                                listView.getSelectionModel().clearSelection(idx);
//                            }
//                        }
//                    }
//                }
//            }
//        });

    }

    private void setSelected(ObservableList<T> values){
        MultipleSelectionModel<T> selectionModel = listView.getSelectionModel();

        selectionModel.clearSelection();
        if(multiple.get()){
            for(T v : values){
                selectionModel.select(v);
            }
        }else{
            if(values.size()>0){
                selectionModel.select(values.get(values.size()-1));
            }
        }
    }

    @Override
    public void doShow(Node node, ObservableList<T> values, double x, double y) {
//        setSelected(values);

        listView.getSelectionModel().getSelectedItems().addListener(selectionChange);
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

        listView.getSelectionModel().getSelectedItems().removeListener(selectionChange);
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

            ObservableList<T> selectedItems = listView.getSelectionModel().getSelectedItems();
            if(selectedItems.size()>0){
                if(multiple.get()){
                	List<T> oldValues = new ArrayList<>(values);
                	values.clear();
                	for (T t : oldValues) {
						if(!items.contains(t)) {
							values.add(t);
						}
					}
                	values.addAll(selectedItems);
                    
                }else{
                    values.setAll(selectedItems.get(selectedItems.size()-1));
                }
            }else{
                values.clear();
            }
        }
    };

}
