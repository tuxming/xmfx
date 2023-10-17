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
import com.xm2013.jfx.control.data.XmCheckBoxTreeCell;
import com.xm2013.jfx.control.data.XmTreeCell;
import com.xm2013.jfx.control.data.XmTreeView;
import com.xm2013.jfx.control.icon.XmFontIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.*;

public class SelectorTreePopup<T> extends SelectorPopup<T>{
    private XmTreeView<T> treeView;
    private Map<T, TreeItem<T>> itemMap ;

    public SelectorTreePopup(XmSelector<T> control){
        super(control);
    }

    public SelectorTreePopup(ObservableList<T> items, ObservableList<T> values,
                             SelectorConvert<T> convert, ColorType colorType,
                             SizeType sizeType, HueType hueType, SelectorType selectorType,
                             SelectorCellFactory<T> cellFactory, BooleanProperty multiple) {
        super(items, values, convert, colorType, sizeType, hueType, selectorType, cellFactory, multiple);
    }

    @Override
    public void buildList() {

        if(itemMap == null){
            itemMap = new HashMap<>();
        }

        T t = (T) items.get(0);

        if(multiple.get()){
            CheckBoxTreeItem<T> root = new CheckBoxTreeItem<>();
            root.setGraphic(convert.getIcon(t));
            root.setValue(t);
            root.setExpanded(true);

            buildCheckBoxItems(root, t);

            treeView = new XmTreeView<>(root);
        }else{
            TreeItem<T> root = new TreeItem<>();
            root.setGraphic(convert.getIcon(t));
            root.setValue(t);
            root.setExpanded(true);
            buildItems(root, t);

            treeView = new XmTreeView<>(root);

            if(values!=null && values.size()>0){
                TreeItem<T> item = itemMap.get(values.get(values.size() - 1));
                if(item!=null){
                    treeView.getSelectionModel().select(item);
                }
            }
        }

        treeView.setShowRoot(false);
        treeView.setColorType(colorType);
        treeView.setHueType(hueType);
        treeView.setSizeType(sizeType);

        final Map<T, TreeItem<T>> finalMap = itemMap;
        treeView.setCellFactory(new Callback<TreeView<T>, TreeCell<T>>() {
            @Override
            public TreeCell<T> call(TreeView<T> param) {
                if(multiple.get()){
                    XmCheckBoxTreeCell<T> treeCell = new XmCheckBoxTreeCell<>(new Callback<TreeItem<T>, ObservableValue<Boolean>>() {
                        @Override
                        public ObservableValue<Boolean> call(TreeItem<T> param) {
                            if (param instanceof CheckBoxTreeItem<?>) {
                                return ((CheckBoxTreeItem<?>) param).selectedProperty();
                            }
                            return null;
                        }
                    }, new StringConverter<TreeItem<T>>() {
                        @Override
                        public String toString(TreeItem<T> object) {
                            return convert.toString(object.getValue());
                        }

                        @Override
                        public TreeItem<T> fromString(String string) {
//                        return new TreeItem<>(new T(string));
                            return null;
                        }
                    }) {
                        @Override
                        public void updateItem(T item, boolean empty) {
                            super.updateItem(item, empty);
                            if (cellFactory != null) {
                                cellFactory.updateItem(this, item, empty);
                            }
                        }
                    };

                    treeCell.checkedProperty().addListener((ob, ov, nv)->{
                        List<T> selectValues = XmTreeView.getSelectValues(treeView);
//                        values.setAll(selectValues);
                        
                        List<T> oldValues = new ArrayList<>(values);
                    	values.clear();
                    	for (T t : oldValues) {
    						if(!finalMap.keySet().contains(t)) {
    							values.add(t);
    						}
    					}
                    	values.addAll(selectValues);
                        
//                        values.clear();
//                        for (T value : selectValues) {
//                            values.add(value);
//                        }
                    });

                    return treeCell;
                }else{
                    return new XmTreeCell<>(){
                        @Override
                        public void updateItem(T item, boolean empty) {
                            super.updateItem(item, empty);
                            if(cellFactory!=null){
                                cellFactory.updateItem(this, item, empty);
                            }
                        }
                    };
                }
            }
        });
        

        this.popupContentPane.getChildren().add(treeView);
    }

    public void setSelects(ObservableList<T> selects){

        if(selects == null || selects.size()==0){
            return;
        }

        if(multiple.get()){
            traverseTree((CheckBoxTreeItem<T>) treeView.getRoot(), selects);
        }else{
            TreeItem<T> item = itemMap.get(values.get(values.size() - 1));
            if(item!=null){
                treeView.getSelectionModel().select(item);
            }
        }
    }

    private  void traverseTree(CheckBoxTreeItem<T> item, List<T> selects) {
        item.setSelected(selects.contains(item.getValue()));
        for (TreeItem<T> child : item.getChildren()) {
            if (child instanceof CheckBoxTreeItem<?>) {
                traverseTree((CheckBoxTreeItem<T>) child, selects);
            }
        }
    }

    public void buildCheckBoxItems(CheckBoxTreeItem<T> parent, T item){

        List<T> children = convert.getChildren(item);

        if(children==null || children.size()==0){
            parent.getChildren().clear();
            return;
        }

        parent.getChildren().clear();
        for(T t : children){
            CheckBoxTreeItem<T> menuItem = new CheckBoxTreeItem<>();
            menuItem.setValue(t);
            menuItem.setGraphic(convert.getIcon(t));
            parent.getChildren().add(menuItem);
            itemMap.put(t, menuItem);
            buildCheckBoxItems(menuItem, t);
        }
    }

    public void buildItems(TreeItem<T> parent, T item){

        List<T> children = convert.getChildren(item);

        if(children==null || children.size()==0){
            parent.getChildren().clear();
            return;
        }

        parent.getChildren().clear();
        for(T t : children){
            TreeItem<T> menuItem = new TreeItem<>();
            menuItem.setValue(t);
            menuItem.setGraphic(convert.getIcon(t));
            parent.getChildren().add(menuItem);
            itemMap.put(t, menuItem);
            buildItems(menuItem, t);
        }
    }

    @Override
    public void doShow(Node node, ObservableList<T> values, double x, double y) {
        setSelects(values);

        treeView.setPrefWidth(node.getLayoutBounds().getWidth());
        if(!multiple.get()){
            treeView.getSelectionModel().getSelectedIndices().addListener(selectionChange);
        }
        this.show(node, x, y);
    }

    @Override
    public void doHide() {
        if(!multiple.get()){
            treeView.getSelectionModel().getSelectedIndices().removeListener(selectionChange);
        }
        this.hide();
    }

    @Override
    public void removeItem(T t) {
        setSelects(values);
    }
    
    private ListChangeListener<? super Integer> selectionChange = new ListChangeListener<Integer>() {
        @Override
        public void onChanged(Change<? extends Integer> c) {
            MultipleSelectionModel<TreeItem<T>> selectionModel = treeView.getSelectionModel();
            if(!multiple.get()){
                values.setAll(selectionModel.getSelectedItem().getValue());
            }
        }
    };
}
