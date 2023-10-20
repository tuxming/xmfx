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
package com.xm2013.jfx.control.listview;

import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.data.CellUtils;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class XmCheckBoxListCell<T> extends XmListCell<T> {
    /* *************************************************************************
     *                                                                         *
     * Static cell factories                                                   *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a cell factory for use in ListView controls. When used in a
     * ListView, the {@link CheckBoxListCell} is rendered with a CheckBox on the
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
     * ListView, the {@link CheckBoxListCell} is rendered with a CheckBox on the
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
        return list -> new CheckBoxListCell<T>(getSelectedProperty, converter);
    }

    /* *************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final XmCheckBox checkBox;

    private ObservableValue<Boolean> booleanProperty;


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

    private boolean fromCheckBoxSelected = false;
    /**
     * Creates a CheckBoxListCell with a custom string converter.
     *
     * @param getSelectedProperty A {@link Callback} that will return an
     *      {@code ObservableValue<Boolean>} given an item from the ListView.
     * @param converter A StringConverter that, given an object of type T, will
     *      return a String that can be used to represent the object visually.
     */
    public XmCheckBoxListCell(
            final Callback<T, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<T> converter) {
        this.getStyleClass().add("xm-check-box-list-cell");

        setConverter(converter);

        this.checkBox = new XmCheckBox();
        this.checkBox.setSelectedColor(Color.WHITE);

        this.checkBox.setMouseTransparent(true);
        this.selectedProperty().addListener((ob, ov, nv)->{
            if(!fromCheckBoxSelected){
                checkBox.setSelected(nv);
            }
            fromCheckBoxSelected = false;
        });

        this.setSelectedStateCallback(param -> booleanProperty);

        setAlignment(Pos.CENTER_LEFT);
        setContentDisplay(ContentDisplay.LEFT);

        // by default the graphic is null until the cell stops being empty
        setGraphic(null);
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

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

    public XmCheckBox getCheckBox() {
        return checkBox;
    }

    /** {@inheritDoc} */
    @Override public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (! empty) {
            StringConverter<T> c = getConverter();
//            Callback<T, ObservableValue<Boolean>> callback = getSelectedStateCallback();
//            if (callback == null) {
//                throw new NullPointerException(
//                        "The CheckBoxListCell selectedStateCallbackProperty can not be null");
//            }

            setGraphic(checkBox);
            setText(c != null ? c.toString(item) : (item == null ? "" : item.toString()));

//            if (booleanProperty != null) {
//                checkBox.selectedProperty().unbindBidirectional((BooleanProperty)booleanProperty);
//            }
//            booleanProperty = callback.call(item);
//            if (booleanProperty != null) {
//                checkBox.selectedProperty().bindBidirectional((BooleanProperty)booleanProperty);
//            }
        } else {
            setGraphic(null);
            setText(null);
        }
    }
}
