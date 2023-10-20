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
package com.xm2013.jfx.control.treeview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.icon.XmIcon;
import com.xm2013.jfx.control.listview.XmCheckBoxListCell;
import com.xm2013.jfx.control.listview.XmListView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 这个是copy的javafx的源代码
 */
public class XmTreeCell<T> extends TreeCell<T> {

    private static PseudoClass odd = PseudoClass.getPseudoClass("odd");
    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");
    private static PseudoClass filled = PseudoClass.getPseudoClass("filled");
    private static PseudoClass hover = PseudoClass.getPseudoClass("hover");

    class TreeCellSkinInfo {
        public Paint fontColor;
        public Background background;
        public TreeCellSkinInfo(Paint fontColor, Background background){
            this.fontColor = fontColor;
            this.background = background;
        }
    }

    private Map<Integer, TreeCellSkinInfo> infos = new LinkedHashMap<>();

    private HBox hbox;
    private boolean prevSelected = false;
    private StackPane arrow = null;

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

        getChildren().addListener(childListener);

        init();
    }

    public void init(){
        //在这里做一些初始设置
//        treeViewProperty().addListener(treeViewListener);

        treeViewProperty().addListener((ob, ov, nv)->{
            updateSkin(0);
        });

        ObservableSet<PseudoClass> pseudoClassStates = getPseudoClassStates();
        pseudoClassStates.addListener((SetChangeListener<PseudoClass>) change -> {
//            System.out.println(getText()+": "+pseudoClassStates);
            boolean isOdd = pseudoClassStates.contains(odd);
            boolean isSelected = pseudoClassStates.contains(selected);
//            boolean isFilled = pseudoClassStates.contains(filled);
            boolean isHover = pseudoClassStates.contains(hover);

            if(isOdd){
                if(!isSelected && !isHover){
                    updateSkin(1);
                    prevSelected = false;
                    return;
                }
            }

            if(isSelected){
                if(!prevSelected){
                    updateSkin(2);
                }
                prevSelected = true;

                return;
            }

            prevSelected = false;
            if(isHover){
                updateSkin(3);
                return;
            }

            updateSkin(0);

        });
    }

    private ListChangeListener<Node> childListener = new ListChangeListener<Node>() {
        @Override
        public void onChanged(Change<? extends Node> c) {
            TreeView<T> tv = getTreeView();
            if(tv instanceof XmTreeView){
                XmTreeView xtv = (XmTreeView) tv;
                if(arrow == null){
                    for (Node child : getChildren()) {
                        if(child.getStyleClass().contains("tree-disclosure-node")){
                            StackPane disclosure = (StackPane) child;
                            for (Node subNode : disclosure.getChildren()) {
                                if(subNode.getStyleClass().contains("arrow")){
                                    arrow = (StackPane) subNode;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                if(arrow != null){
                    arrow.setOpacity(xtv.isVisibleArrow()?1:0);
                    getChildren().removeListener(childListener);
                }
            }
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
    }

    /**
     * 0-默认
     * 1-odd
     * 2-selected
     * 3-hover
     * @param status int
     */
    private void updateSkin(int status){

//        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        TreeCellSkinInfo info = getInfo(status);
        if(info == null){
            return;
        }

        TreeView<T> tv = getTreeView();
        if(this instanceof XmCheckBoxTreeCell){
            XmCheckBoxTreeCell cell = (XmCheckBoxTreeCell) this;

            if(tv instanceof XmTreeView){
                cell.getCheckBox().setColorType(((XmTreeView<T>) tv).getColorType());
                cell.getCheckBox().sizeTypeProperty().bind(((XmTreeView<T>) tv).sizeTypeProperty());
            }

        }

        if(tv instanceof  XmTreeView){
            XmTreeView xtv = (XmTreeView) tv;
            double fontSize = 14;
            SizeType sizeType = xtv.getSizeType();
            if(sizeType.equals(SizeType.SMALL)){
                fontSize = 12;
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 16;
            }

            this.setFont(Font.font(this.getFont().getFamily(), fontSize));

            if(getTreeItem()!=null && getTreeItem().getGraphic()!=null){
                Node graphicNode = getTreeItem().getGraphic();
                if(graphicNode instanceof XmIcon){
                    XmIcon graphic = (XmIcon) graphicNode;
                    graphic.setSize(fontSize+2);
                    graphic.setColor(info.fontColor);
                }
            }
        }

        setBackground(info.background);
        setTextFill(info.fontColor);
    }

    private TreeCellSkinInfo getInfo(int status){

        if(infos.size() == 0 || infos.get(status) == null){
            buildCellSkinInfo(status);
        }

        return infos.get(status);

    }

    private void buildCellSkinInfo(int status) {

        TreeView<T> treeView = getTreeView();
        boolean is = treeView instanceof XmTreeView;
        if(!is){
            return;
        }

        XmTreeView lv = (XmTreeView) treeView;
        Paint paint = lv.getColorType().getPaint(),
                fontColor = null,
                bgColor = null;

        HueType hueType = lv.getHueType();

        if(status == 0){
            if(HueType.LIGHT.equals(hueType)){
                fontColor = Color.color(0.2, 0.2,0.2);
                bgColor = Color.WHITE;
            }else{
                fontColor = Color.color(1, 1, 1);
                bgColor = Color.TRANSPARENT;
            }
        }else if(status == 1){
            if(HueType.LIGHT.equals(hueType)){
                fontColor = Color.color(0.2, 0.2,0.2);
            }else{
                fontColor = Color.color(1, 1, 1);
            }
            bgColor = FxKit.getOpacityPaint(paint, 0.08);
        }else if(status == 2){
            fontColor = Color.WHITE;
            bgColor = paint;

        }else if(status == 3){
            fontColor = paint;

            if(getPseudoClassStates().contains(odd)){
                bgColor = FxKit.getOpacityPaint(paint, 0.08);
            }else{
                bgColor = Color.TRANSPARENT;
            }

        }

        infos.put(status, new TreeCellSkinInfo(fontColor, new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY))));

    }

}
