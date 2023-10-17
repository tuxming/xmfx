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
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class XmCheckBoxTreeCell<T> extends XmTreeCell<T> {

    /* *************************************************************************
     *                                                                         *
     * Static cell factories                                                   *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a cell factory for use in a TreeView control, although there is a
     * major assumption when used in a TreeView: this cell factory assumes that
     * the TreeView root, and <b>all</b> children are instances of
     * {@link CheckBoxTreeItem}, rather than the default {@link TreeItem} class
     * that is used normally.
     *
     * <p>When used in a TreeView, the CheckBoxCell is rendered with a CheckBox
     * to the right of the 'disclosure node' (i.e. the arrow). The item stored
     * in {@link CheckBoxTreeItem#getValue()} will then have the StringConverter
     * called on it, and this text will take all remaining horizontal space.
     * Additionally, by using {@link CheckBoxTreeItem}, the TreeView will
     * automatically handle situations such as:
     *
     * <ul>
     *   <li>Clicking on the {@link CheckBox} beside an item that has children
     *      will result in all children also becoming selected/unselected.</li>
     *   <li>Clicking on the {@link CheckBox} beside an item that has a parent
     *      will possibly toggle the state of the parent. For example, if you
     *      select a single child, the parent will become indeterminate (indicating
     *      partial selection of children). If you proceed to select all
     *      children, the parent will then show that it too is selected. This is
     *      recursive, with all parent nodes updating as expected.</li>
     * </ul>
     *
     * <p>Unfortunately, due to limitations in Java, it is necessary to provide
     * an explicit cast when using this method. For example:
     *
     * <pre>
     * {@code
     * final TreeView<String> treeView = new TreeView<String>();
     * treeView.setCellFactory(CheckBoxCell.<String>forTreeView());}</pre>
     *
     * @param <T> The type of the elements contained within the
     *      {@link CheckBoxTreeItem} instances.
     * @return A {@link Callback} that will return a TreeCell that is able to
     *      work on the type of element contained within the TreeView root, and
     *      all of its children (recursively).
     */
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView() {
        Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty =
                item -> {
                    if (item instanceof CheckBoxTreeItem<?>) {
                        return ((CheckBoxTreeItem<?>)item).selectedProperty();
                    }
                    return null;
                };
        return forTreeView(getSelectedProperty,
                CellUtils.<T>defaultTreeItemStringConverter());
    }



    /**
     * Creates a cell factory for use in a TreeView control. Unlike
     * {@link #forTreeView()}, this method does not assume that all TreeItem
     * instances in the TreeView are {@link CheckBoxTreeItem} instances.
     *
     * <p>When used in a TreeView, the CheckBoxCell is rendered with a CheckBox
     * to the right of the 'disclosure node' (i.e. the arrow). The item stored
     * in {@link CheckBoxTreeItem#getValue()} will then have the StringConverter
     * called on it, and this text will take all remaining horizontal space.
     *
     * <p>Unlike {@link #forTreeView()}, this cell factory does not handle
     * updating the state of parent or children TreeItems - it simply toggles
     * the {@code ObservableValue<Boolean>} that is provided, and no more. Of
     * course, this functionality can then be implemented externally by adding
     * observers to the {@code ObservableValue<Boolean>}, and toggling the state
     * of other properties as necessary.
     *
     * @param <T> The type of the elements contained within the {@link TreeItem}
     *      instances.
     * @param getSelectedProperty A {@link Callback} that, given an object of
     *      type {@literal TreeItem<T>}, will return an {@code ObservableValue<Boolean>}
     *      that represents whether the given item is selected or not. This
     *      {@code ObservableValue<Boolean>} will be bound bidirectionally
     *      (meaning that the CheckBox in the cell will set/unset this property
     *      based on user interactions, and the CheckBox will reflect the state
     *      of the {@code ObservableValue<Boolean>}, if it changes externally).
     * @return A {@link Callback} that will return a TreeCell that is able to
     *      work on the type of element contained within the TreeView root, and
     *      all of its children (recursively).
     */
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(
            final Callback<TreeItem<T>,
                    ObservableValue<Boolean>> getSelectedProperty) {
        return forTreeView(getSelectedProperty, CellUtils.<T>defaultTreeItemStringConverter());
    }

    /**
     * Creates a cell factory for use in a TreeView control. Unlike
     * {@link #forTreeView()}, this method does not assume that all TreeItem
     * instances in the TreeView are {@link CheckBoxTreeItem}.
     *
     * <p>When used in a TreeView, the CheckBoxCell is rendered with a CheckBox
     * to the right of the 'disclosure node' (i.e. the arrow). The item stored
     * in {@link TreeItem#getValue()} will then have the the StringConverter
     * called on it, and this text will take all remaining horizontal space.
     *
     * <p>Unlike {@link #forTreeView()}, this cell factory does not handle
     * updating the state of parent or children TreeItems - it simply toggles
     * the {@code ObservableValue<Boolean>} that is provided, and no more. Of
     * course, this functionality can then be implemented externally by adding
     * observers to the {@code ObservableValue<Boolean>}, and toggling the state
     * of other properties as necessary.
     *
     * @param <T> The type of the elements contained within the {@link TreeItem}
     *      instances.
     * @param getSelectedProperty A Callback that, given an object of
     *      type {@literal TreeItem<T>}, will return an {@code ObservableValue<Boolean>}
     *      that represents whether the given item is selected or not. This
     *      {@code ObservableValue<Boolean>} will be bound bidirectionally
     *      (meaning that the CheckBox in the cell will set/unset this property
     *      based on user interactions, and the CheckBox will reflect the state of
     *      the {@code ObservableValue<Boolean>}, if it changes externally).
     * @param converter A StringConverter that, give an object of type
     *      {@literal TreeItem<T>}, will return a String that can be used to represent the
     *      object visually. The default implementation in {@link #forTreeView(Callback)}
     *      is to simply call .toString() on all non-null items (and to just
     *      return an empty string in cases where the given item is null).
     * @return A {@link Callback} that will return a TreeCell that is able to
     *      work on the type of element contained within the TreeView root, and
     *      all of its children (recursively).
     */
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(
            final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<TreeItem<T>> converter) {
        return tree -> new CheckBoxTreeCell<T>(getSelectedProperty, converter);
    }




    /* *************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/
    private final XmCheckBox<T> checkBox;
    private final HBox box;

    private ObservableValue<Boolean> booleanProperty;

    private BooleanProperty indeterminateProperty;

    /* *************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a default {@link CheckBoxTreeCell} that assumes the TreeView is
     * constructed with {@link CheckBoxTreeItem} instances, rather than the
     * default {@link TreeItem}.
     * By using {@link CheckBoxTreeItem}, it will internally manage the selected
     * and indeterminate state of each item in the tree.
     */
    public XmCheckBoxTreeCell() {
        // getSelectedProperty as anonymous inner class to deal with situation
        // where the user is using CheckBoxTreeItem instances in their tree
        this(item -> {
            if (item instanceof CheckBoxTreeItem<?>) {
                return ((CheckBoxTreeItem<?>)item).selectedProperty();
            }
            return null;
        });
    }

    public XmCheckBox<T> getCheckBox(){
        return checkBox;
    }

    /**
     * Creates a {@link CheckBoxTreeCell} for use in a TreeView control via a
     * cell factory. Unlike {@link CheckBoxTreeCell#CheckBoxTreeCell()}, this
     * method does not assume that all TreeItem instances in the TreeView are
     * {@link CheckBoxTreeItem}.
     *
     * <p>To call this method, it is necessary to provide a
     * {@link Callback} that, given an object of type {@literal TreeItem<T>}, will return
     * an {@code ObservableValue<Boolean>} that represents whether the given
     * item is selected or not. This {@code ObservableValue<Boolean>} will be
     * bound bidirectionally (meaning that the CheckBox in the cell will
     * set/unset this property based on user interactions, and the CheckBox will
     * reflect the state of the {@code ObservableValue<Boolean>}, if it changes
     * externally).
     *
     * <p>If the items are not {@link CheckBoxTreeItem} instances, it becomes
     * the developers responsibility to handle updating the state of parent and
     * children TreeItems. This means that, given a TreeItem, this class will
     * simply toggles the {@code ObservableValue<Boolean>} that is provided, and
     * no more. Of course, this functionality can then be implemented externally
     * by adding observers to the {@code ObservableValue<Boolean>}, and toggling
     * the state of other properties as necessary.
     *
     * @param getSelectedProperty A {@link Callback} that will return an
     *      {@code ObservableValue<Boolean>} that represents whether the given
     *      item is selected or not.
     */
    public XmCheckBoxTreeCell(
            final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty) {
        this(getSelectedProperty, CellUtils.<T>defaultTreeItemStringConverter(), null);
    }

    /**
     * Creates a {@link CheckBoxTreeCell} for use in a TreeView control via a
     * cell factory. Unlike {@link CheckBoxTreeCell#CheckBoxTreeCell()}, this
     * method does not assume that all TreeItem instances in the TreeView are
     * {@link CheckBoxTreeItem}.
     *
     * <p>To call this method, it is necessary to provide a {@link Callback}
     * that, given an object of type {@literal TreeItem<T>}, will return an
     * {@code ObservableValue<Boolean>} that represents whether the given item
     * is selected or not. This {@code ObservableValue<Boolean>} will be bound
     * bidirectionally (meaning that the CheckBox in the cell will set/unset
     * this property based on user interactions, and the CheckBox will reflect
     * the state of the {@code ObservableValue<Boolean>}, if it changes
     * externally).
     *
     * <p>If the items are not {@link CheckBoxTreeItem} instances, it becomes
     * the developers responsibility to handle updating the state of parent and
     * children TreeItems. This means that, given a TreeItem, this class will
     * simply toggles the {@code ObservableValue<Boolean>} that is provided, and
     * no more. Of course, this functionality can then be implemented externally
     * by adding observers to the {@code ObservableValue<Boolean>}, and toggling
     * the state of other properties as necessary.
     *
     * @param getSelectedProperty A {@link Callback} that will return an
     *      {@code ObservableValue<Boolean>} that represents whether the given
     *      item is selected or not.
     * @param converter {@literal A StringConverter that, give an object of type
     * TreeItem<T>, will return a String that can be used to represent the
     * object visually.}
     */
    public XmCheckBoxTreeCell(
            final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<TreeItem<T>> converter) {
        this(getSelectedProperty, converter, null);
    }

    private XmCheckBoxTreeCell(
            final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<TreeItem<T>> converter,
            final Callback<TreeItem<T>, ObservableValue<Boolean>> getIndeterminateProperty) {
        this.getStyleClass().add("check-box-tree-cell");
        setSelectedStateCallback(getSelectedProperty);
        setConverter(converter);

        this.checkBox = new XmCheckBox<>();
        this.checkBox.setSelectedHightLight(true);
        this.checkBox.setSizeType(SizeType.SMALL);
        this.box = new HBox();
        this.checkBox.setAllowIndeterminate(false);

        // by default the graphic is null until the cell stops being empty
        setGraphic(null);

        this.setOnMouseClicked(e -> {
            Node node = (Node) e.getTarget();
            ObservableList<String> styleClass = node.getStyleClass();
            if(styleClass.contains("arrow") || styleClass.contains("tree-disclosure-node")){
                return;
            }
            boolean selected = !checkBox.isSelected();
            checkBox.setSelected(selected);
            getTreeView().getSelectionModel().select(getTreeItem());
        });

//        this.checkBox.selectedProperty().addListener((ob, ov, nv)->{
//            if(nv){
//                getTreeView().getSelectionModel().select(getTreeItem());
//            }else{
//                int index = getTreeView().getSelectionModel().getSelectedItems().indexOf(getTreeItem());
//                if(index>-1){
//                    getTreeView().getSelectionModel().clearSelection(index);
//                }
//            }
//        });
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public final BooleanProperty checkedProperty(){
        return checkBox.selectedProperty();
    }

    // --- converter
    private ObjectProperty<StringConverter<TreeItem<T>>> converter =
            new SimpleObjectProperty<StringConverter<TreeItem<T>>>(this, "converter");

    /**
     * The {@link StringConverter} property.
     * @return the {@link StringConverter} property
     */
    public final ObjectProperty<StringConverter<TreeItem<T>>> converterProperty() {
        return converter;
    }

    /**
     * Sets the {@link StringConverter} to be used in this cell.
     * @param value the {@link StringConverter} to be used in this cell
     */
    public final void setConverter(StringConverter<TreeItem<T>> value) {
        converterProperty().set(value);
    }

    /**
     * Returns the {@link StringConverter} used in this cell.
     * @return the {@link StringConverter} used in this cell
     */
    public final StringConverter<TreeItem<T>> getConverter() {
        return converterProperty().get();
    }



    // --- selected state callback property
    private ObjectProperty<Callback<TreeItem<T>, ObservableValue<Boolean>>>
            selectedStateCallback =
            new SimpleObjectProperty<Callback<TreeItem<T>, ObservableValue<Boolean>>>(
                    this, "selectedStateCallback");

    /**
     * Property representing the {@link Callback} that is bound to by the
     * CheckBox shown on screen.
     * @return the property representing the {@link Callback} that is bound to
     * by the CheckBox shown on screen
     */
    public final ObjectProperty<Callback<TreeItem<T>, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return selectedStateCallback;
    }

    /**
     * Sets the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @param value the {@link Callback} that is bound to by the CheckBox shown on screen
     */
    public final void setSelectedStateCallback(Callback<TreeItem<T>, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }

    /**
     * Returns the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @return the {@link Callback} that is bound to by the CheckBox shown on screen
     */
    public final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedStateCallback() {
        return selectedStateCallbackProperty().get();
    }



    /* *************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            StringConverter<TreeItem<T>> c = getConverter();

            TreeItem<T> treeItem = getTreeItem();

            // update the node content
            setText(c != null ? c.toString(treeItem) : (treeItem == null ? "" : treeItem.toString()));

            if(treeItem.getGraphic() == null){
                box.getChildren().setAll(checkBox);
            }else{
                box.getChildren().setAll(checkBox, treeItem.getGraphic());
            }
//            checkBox.setGraphic(treeItem == null ? null : treeItem.getGraphic());
            setGraphic(box);

            // uninstall bindings
            if (booleanProperty != null) {
                checkBox.selectedProperty().unbindBidirectional((BooleanProperty)booleanProperty);
            }
            if (indeterminateProperty != null) {
                checkBox.indeterminateProperty().unbindBidirectional(indeterminateProperty);
            }

            // install new bindings.
            // We special case things when the TreeItem is a CheckBoxTreeItem
            if (treeItem instanceof CheckBoxTreeItem) {
                CheckBoxTreeItem<T> cbti = (CheckBoxTreeItem<T>) treeItem;
                booleanProperty = cbti.selectedProperty();
                checkBox.selectedProperty().bindBidirectional((BooleanProperty)booleanProperty);

                indeterminateProperty = cbti.indeterminateProperty();
                checkBox.indeterminateProperty().bindBidirectional(indeterminateProperty);

            } else {
                Callback<TreeItem<T>, ObservableValue<Boolean>> callback = getSelectedStateCallback();
                if (callback == null) {
                    throw new NullPointerException(
                            "The CheckBoxTreeCell selectedStateCallbackProperty can not be null");
                }

                booleanProperty = callback.call(treeItem);
                if (booleanProperty != null) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty)booleanProperty);
                }
            }
        }
    }

    @Override void updateDisplay(T item, boolean empty) {
        // no-op
        // This was done to resolve RT-33603, but will impact the ability for
        // TreeItem.graphic to change dynamically.
    }
}
