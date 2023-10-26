/*
 * Copyright (c) 2012, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.xm2013.jfx.control.listview;

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.base.CellUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * A class containing a {@link ListCell} implementation that draws a
 * {@link CheckBox} node inside the cell, optionally with a label to indicate
 * what the checkbox represents.
 *
 * <p>The CheckBoxListCell is rendered with a CheckBox on the left-hand side of
 * the {@link ListView}, and the text related to the list item taking up all
 * remaining horizontal space.
 *
 * <p>To construct an instance of this class, it is necessary to provide a
 * {@link Callback} that, given an object of type T, will return a
 * {@code ObservableValue<Boolean>} that represents whether the given item is
 * selected or not. This ObservableValue will be bound bidirectionally (meaning
 * that the CheckBox in the cell will set/unset this property based on user
 * interactions, and the CheckBox will reflect the state of the
 * {@code ObservableValue<Boolean>}, if it changes externally).
 *
 * <p>Note that the CheckBoxListCell renders the CheckBox 'live', meaning that
 * the CheckBox is always interactive and can be directly toggled by the user.
 * This means that it is not necessary that the cell enter its
 * {@link #editingProperty() editing state} (usually by the user double-clicking
 * on the cell). A side-effect of this is that the usual editing callbacks
 * (such as {@link ListView#onEditCommitProperty() on edit commit})
 * will <strong>not</strong> be called. If you want to be notified of changes,
 * it is recommended to directly observe the boolean properties that are
 * manipulated by the CheckBox.</p>
 *
 * @see CheckBox
 * @see ListCell
 * @param <T> The type of the elements contained within the ListView.
 * @since JavaFX 2.2
 */
public class XmCheckBoxListCell<T> extends XmListCell<T> {

    /* *************************************************************************
     *                                                                         *
     * Static cell factories                                                   *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a cell factory for use in ListView controls. When used in a
     * ListView, the {@link XmCheckBoxListCell} is rendered with a CheckBox on the
     * left-hand side of the ListView, with the text related to the list item
     * taking up all remaining horizontal space.
     *
     * @param <T> The type of the elements contained within the ListView.
     * @param getSelectedProperty A {@link Callback} that, given an object of
     *      type T (which is a value taken out of the
     *      {@code ListView<T>.items} list),
     *      will return an {@code ObservableValue<Boolean>} that represents
     *      whether the given item is selected or not. This ObservableValue will
     *      be bound bidirectionally (meaning that the CheckBox in the cell will
     *      set/unset this property based on user interactions, and the CheckBox
     *      will reflect the state of the ObservableValue, if it changes
     *      externally).
     * @return A {@link Callback} that will return a ListCell that is able to
     *      work on the type of element contained within the ListView items list.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(
            final Callback<T, ObservableValue<Boolean>> getSelectedProperty) {
        return forListView(getSelectedProperty, CellUtils.<T>defaultStringConverter());
    }

    /**
     * Creates a cell factory for use in ListView controls. When used in a
     * ListView, the {@link XmCheckBoxListCell} is rendered with a CheckBox on the
     * left-hand side of the ListView, with the text related to the list item
     * taking up all remaining horizontal space.
     *
     * @param <T> The type of the elements contained within the ListView.
     * @param getSelectedProperty A {@link Callback} that, given an object
     *      of type T (which is a value taken out of the
     *      {@code ListView<T>.items} list),
     *      will return an {@code ObservableValue<Boolean>} that represents
     *      whether the given item is selected or not. This ObservableValue will
     *      be bound bidirectionally (meaning that the CheckBox in the cell will
     *      set/unset this property based on user interactions, and the CheckBox
     *      will reflect the state of the ObservableValue, if it changes
     *      externally).
     * @param converter A StringConverter that, give an object of type T, will
     *      return a String that can be used to represent the object visually.
     * @return A {@link Callback} that will return a ListCell that is able to
     *      work on the type of element contained within the ListView.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(
            final Callback<T, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<T> converter) {
        return list -> new XmCheckBoxListCell<T>(getSelectedProperty, converter);
    }

    /* *************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final XmCheckBox checkBox;
    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");

    private ObservableValue<Boolean> booleanProperty = new SimpleBooleanProperty();



    /* *************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a default CheckBoxListCell.
     */
    public XmCheckBoxListCell() {
        this(null);
    }

    /**
     * Creates a default CheckBoxListCell.
     *
     * @param getSelectedProperty A {@link Callback} that will return an
     *      {@code ObservableValue<Boolean>} given an item from the ListView.
     */
    public XmCheckBoxListCell(
            final Callback<T, ObservableValue<Boolean>> getSelectedProperty) {
        this(getSelectedProperty, CellUtils.<T>defaultStringConverter());
    }

    /**
     * Creates a CheckBoxListCell with a custom string converter.
     *
     * @param getSelectedProperty A {@link Callback} that will return an
     *      {@code ObservableValue<Boolean>} given an item from the ListView.
     * @param converter A StringConverter that, given an object of type T, will
     *      return a String that can be used to represent the object visually.
     */
    public XmCheckBoxListCell(
            Callback<T, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<T> converter) {
        this.getStyleClass().add("check-box-list-cell");

        setSelectedStateCallback(getSelectedProperty);
        setConverter(converter);

        this.checkBox = new XmCheckBox();
        this.checkBox.setMouseTransparent(true);

        setAlignment(Pos.CENTER_LEFT);
        setContentDisplay(ContentDisplay.LEFT);

        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            checkBox.setSelected(!checkBox.isSelected());
        });

        checkBox.selectedProperty().addListener((ob, ov, nv)->{
            T item = getItem();
            XmListView listView = (XmListView) getListView();
            if(nv){
                boolean contains = listView.getCheckedValues().contains(item);
                if(!contains){
                    listView.addCheckedValue(item);
                }
            }else{
                listView.getCheckedValues().remove(item);
            }
        });

        getPseudoClassStates().addListener((SetChangeListener<PseudoClass>) change -> setCheckBoxColor());
        listViewProperty().addListener((observable, oldValue, newValue) -> setCheckBoxColor());

        itemProperty().addListener((observable, oldValue, newValue) -> {
            boolean checked = ((XmListView)getListView()).getCheckedValues().contains(newValue);
            if(checked){
                checkBox.setSelected(true);
            }else{
                checkBox.setSelected(false);
            }
        });

        // by default the graphic is null until the cell stops being empty
        setGraphic(null);
    }

    private void setCheckBoxColor(){
        if(getPseudoClassStates().contains(selected)){
            checkBox.setColorType(ColorType.other(Color.WHITE));
        }else{
            XmListView xtv = (XmListView) getListView();
            checkBox.setColorType(xtv.getColorType());
        }
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public XmCheckBox getCheckBox(){
        return checkBox;
    }

    // --- converter
    private ObjectProperty<StringConverter<T>> converter =
            new SimpleObjectProperty<StringConverter<T>>(this, "converter");

    /**
     * The {@link StringConverter} property.
     * @return the {@link StringConverter} property
     */
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    /**
     * Sets the {@link StringConverter} to be used in this cell.
     * @param value the {@link StringConverter}
     */
    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    /**
     * Returns the {@link StringConverter} used in this cell.
     * @return the {@link StringConverter} used in this cell
     */
    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }


    // --- selected state callback property
    private ObjectProperty<Callback<T, ObservableValue<Boolean>>>
            selectedStateCallback =
            new SimpleObjectProperty<Callback<T, ObservableValue<Boolean>>>(
            this, "selectedStateCallback");

    /**
     * Property representing the {@link Callback} that is bound to by the
     * CheckBox shown on screen.
     * @return the {@link Callback} that is bound to by the CheckBox shown on
     * screen
     */
    public final ObjectProperty<Callback<T, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return selectedStateCallback;
    }

    /**
     * Sets the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @param value the {@link Callback}
     */
    public final void setSelectedStateCallback(Callback<T, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }

    /**
     * Returns the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @return the {@link Callback} that is bound to by the CheckBox shown on screen
     */
    public final Callback<T, ObservableValue<Boolean>> getSelectedStateCallback() {
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

        if (! empty) {
            StringConverter<T> c = getConverter();

            setGraphic(checkBox);
            setText(c != null ? c.toString(item) : (item == null ? "" : item.toString()));

        } else {
            setGraphic(null);
            setText(null);
        }
    }
}
