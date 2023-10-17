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

import com.xm2013.jfx.control.base.ColorType;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;

import java.lang.ref.WeakReference;

/**
 * 这个是copy的javafx的源代码
 */
public class XmTreeCell<T> extends TreeCell<T> {

    private HBox hbox;

    private WeakReference<TreeItem<T>> treeItemRef;

    private InvalidationListener treeItemGraphicListener = observable -> {
        updateDisplay(getItem(), isEmpty());
    };

    private InvalidationListener treeItemListener = new InvalidationListener() {
        @Override public void invalidated(Observable observable) {
            TreeItem<T> oldTreeItem = treeItemRef == null ? null : treeItemRef.get();
            if (oldTreeItem != null) {
                oldTreeItem.graphicProperty().removeListener(weakTreeItemGraphicListener);
            }

            TreeItem<T> newTreeItem = getTreeItem();
            if (newTreeItem != null) {
                newTreeItem.graphicProperty().addListener(weakTreeItemGraphicListener);
                treeItemRef = new WeakReference<TreeItem<T>>(newTreeItem);
            }
        }
    };

    private WeakInvalidationListener weakTreeItemGraphicListener =
            new WeakInvalidationListener(treeItemGraphicListener);

    private WeakInvalidationListener weakTreeItemListener =
            new WeakInvalidationListener(treeItemListener);

    public XmTreeCell() {
        treeItemProperty().addListener(weakTreeItemListener);

        if (getTreeItem() != null) {
            getTreeItem().graphicProperty().addListener(weakTreeItemGraphicListener);
        }

        init();
    }

    public void init(){
        //在这里做一些初始设置
        treeViewProperty().addListener(treeViewListener);
    }

    private ChangeListener<Boolean> tvFocusListener = (ob, ov, nv) ->{

        TreeView tv = getTreeView();
        if(tv instanceof XmTreeView) {
            XmTreeView xtv = (XmTreeView) tv;
            ObservableSet<PseudoClass> pseudoClassStates = getPseudoClassStates();
            boolean selected = pseudoClassStates.contains(PseudoClass.getPseudoClass("selected"));
//            boolean focused = pseudoClassStates.contains(PseudoClass.getPseudoClass("focused"));

            if(!nv){
                if(selected){
                    setStyle("-fx-background-color: #aaaaaa;");
                    if(this instanceof XmCheckBoxTreeCell){
                        ((XmCheckBoxTreeCell)this).getCheckBox().setColorType(xtv.getColorType());
                    }
                }
            }
        }
    };

    private ChangeListener<TreeView<T>> treeViewListener = (ob, ov, nv) ->{

        if(ov!=null){
            ov.focusedProperty().removeListener(tvFocusListener);
        }

        if(nv!=null){
            nv.focusedProperty().addListener(tvFocusListener);
        }
    };


    void updateDisplay(T item, boolean empty) {
        if (item == null || empty) {
            hbox = null;
            setText(null);
            setGraphic(null);
        } else {
            // update the graphic if one is set in the TreeItem
            TreeItem<T> treeItem = getTreeItem();
            if (treeItem != null && treeItem.getGraphic() != null) {
                if (item instanceof Node) {
                    setText(null);

                    // the item is a Node, and the graphic exists, so
                    // we must insert both into an HBox and present that
                    // to the user (see RT-15910)
                    if (hbox == null) {
                        hbox = new HBox(3);
                    }
                    hbox.getChildren().setAll(treeItem.getGraphic(), (Node)item);
                    setGraphic(hbox);
                } else {
                    hbox = null;
                    setText(item.toString());
                    setGraphic(treeItem.getGraphic());
                }
            } else {
                hbox = null;
                if (item instanceof Node) {
                    setText(null);
                    setGraphic((Node)item);
                } else {
                    setText(item.toString());
                    setGraphic(null);
                }
            }
        }
    }

    @Override public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        updateDisplay(item, empty);

        //更行状态颜色
        TreeView tv = getTreeView();
        if(tv instanceof XmTreeView){
            XmTreeView xtv = (XmTreeView) tv;
            boolean tvFocused = xtv.isFocused();
            ObservableSet<PseudoClass> pseudoClassStates = getPseudoClassStates();
            boolean selected = pseudoClassStates.contains(PseudoClass.getPseudoClass("selected"));
            boolean focused = pseudoClassStates.contains(PseudoClass.getPseudoClass("focused"));

            ColorType nc = null;
            String bg = null;

            if(tvFocused){
                if(selected){
                    if(focused){
                        bg = xtv.getColorType().getColor();
                        nc = ColorType.other("#ffffff");
                    }
                }else{
                    bg = "transparent";
                    setStyle("-fx-background-color: transparent;");
                    nc = xtv.getColorType();
                }
            }else{
                if(selected){
                    bg = "#bbbbbb";
                }else{
                    bg = "transparent";
                }
                nc = xtv.getColorType();
            }

            if(bg!=null){
                setStyle("-fx-background-color: "+bg+";");
            }

            if(nc!=null && this instanceof XmCheckBoxTreeCell){
                ((XmCheckBoxTreeCell)this).getCheckBox().setColorType(nc);
            }

        }


    }
}
