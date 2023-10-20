package com.xm2013.jfx.control.treeview;

import com.xm2013.jfx.common.CallBack;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.lang.ref.WeakReference;

/**
 * 当使用treeView做为菜单的时候，可以实现这个类
 * @param <T> object
 */
public class XmMenuTreeCell<T> extends TreeCell<T> {

    private static String DEFAULT_CLASS = "xm-menu-tree-cell";
    private static PseudoClass odd = PseudoClass.getPseudoClass("odd");
    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");
    private static PseudoClass filled = PseudoClass.getPseudoClass("filled");
    private static PseudoClass hover = PseudoClass.getPseudoClass("hover");

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

    public XmMenuTreeCell() {
        getStyleClass().add(DEFAULT_CLASS);
        treeItemProperty().addListener(weakTreeItemListener);

        if (getTreeItem() != null) {
            getTreeItem().graphicProperty().addListener(weakTreeItemGraphicListener);
        }

        getChildren().addListener(childListener);

        treeViewProperty().addListener((ob, ov, nv)->{
            if(updateDefaultSkin!=null) updateDefaultSkin.call(this);
        });

        ObservableSet<PseudoClass> pseudoClassStates = getPseudoClassStates();
        pseudoClassStates.addListener((SetChangeListener<PseudoClass>) change -> {
            boolean isOdd = pseudoClassStates.contains(odd);
            boolean isSelected = pseudoClassStates.contains(selected);
            boolean isHover = pseudoClassStates.contains(hover);

            if(isOdd){
                if(!isSelected && !isHover){
                    if(updateOddSkin!=null) updateOddSkin.call(this);
                    prevSelected = false;
                    return;
                }
            }

            if(isSelected){
                if(!prevSelected){
                    if(updateSelectedSkin!=null) updateSelectedSkin.call(this);
                }
                prevSelected = true;

                return;
            }

            prevSelected = false;
            if(isHover){
                if(updateHoverSkin!=null) updateHoverSkin.call(this);
                return;
            }

            if(updateDefaultSkin!=null) updateDefaultSkin.call(this);

        });

        init();

    }

    /**
     * 初始化方法
     */
    public void init(){
        //在这里做一些初始设置
    }

    private ListChangeListener<Node> childListener = new ListChangeListener<Node>() {
        @Override
        public void onChanged(Change<? extends Node> c) {
            if (arrow == null) {
                for (Node child : getChildren()) {
                    if (child.getStyleClass().contains("tree-disclosure-node")) {
                        StackPane disclosure = (StackPane) child;
                        for (Node subNode : disclosure.getChildren()) {
                            if (subNode.getStyleClass().contains("arrow")) {
                                arrow = (StackPane) subNode;
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            if (arrow != null) {
                arrow.setOpacity(isVisibleArrow() ? 1 : 0);
                getChildren().removeListener(childListener);
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
     * 默认状态下外观, 一般只设置背景颜色，边框颜色，字体颜色，
     */
    private CallBack<XmMenuTreeCell<T>> updateDefaultSkin;
    public CallBack<XmMenuTreeCell<T>> getUpdateDefaultSkin() {
        return updateDefaultSkin;
    }
    public void setUpdateDefaultSkin(CallBack<XmMenuTreeCell<T>> updateDefaultSkin) {
        this.updateDefaultSkin = updateDefaultSkin;
    }

    /**
     * 选中后的cell外观回调, 一般只设置背景颜色，边框颜色，字体颜色
     */
    private CallBack<XmMenuTreeCell<T>> updateSelectedSkin;
    public CallBack<XmMenuTreeCell<T>> getUpdateSelectedSkin() {
        return updateSelectedSkin;
    }
    public void setUpdateSelectedSkin(CallBack<XmMenuTreeCell<T>> updateSelectedSkin) {
        this.updateSelectedSkin = updateSelectedSkin;
    }

    /**
     * 偶数行的外观回调, 一般只设置背景颜色，边框颜色，字体颜色
     */
    private CallBack<XmMenuTreeCell<T>> updateOddSkin;
    public CallBack<XmMenuTreeCell<T>> getUpdateOddSkin() {
        return updateOddSkin;
    }
    public void setUpdateOddSkin(CallBack<XmMenuTreeCell<T>> updateOddSkin) {
        this.updateOddSkin = updateOddSkin;
    }

    /**
     * hover状态下外观, 一般只设置背景颜色，边框颜色，字体颜色
     */
    private CallBack<XmMenuTreeCell<T>> updateHoverSkin;
    public CallBack<XmMenuTreeCell<T>> getUpdateHoverSkin() {
        return updateHoverSkin;
    }
    public void setUpdateHoverSkin(CallBack<XmMenuTreeCell<T>> updateHoverSkin) {
        this.updateHoverSkin = updateHoverSkin;
    }

    /**
     * 是否隐藏箭头
     */
    private BooleanProperty visibleArrow = new SimpleBooleanProperty(true);
    public boolean isVisibleArrow() {
        return visibleArrow.get();
    }
    public BooleanProperty visibleArrowProperty() {
        return visibleArrow;
    }
    public void setVisibleArrow(boolean visibleArrow) {
        this.visibleArrow.set(visibleArrow);
    }
}
