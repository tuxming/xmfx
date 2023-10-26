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
package com.xm2013.jfx.control.tableview;

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.checkbox.XmCheckBox;
import com.xm2013.jfx.control.base.CellUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class XmCheckBoxTableCell<S,T> extends XmTableCell<S,T> {

    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");

    /* *************************************************************************
     *                                                                         *
     * Static cell factories                                                   *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a cell factory for use in a {@link TableColumn} cell factory.
     * This method requires that the TableColumn be of type {@link Boolean}.
     *
     * <p>When used in a TableColumn, the CheckBoxCell is rendered with a
     * CheckBox centered in the column.
     *
     * <p>The {@code ObservableValue<Boolean>} contained within each cell in the
     * column will be bound bidirectionally. This means that the  CheckBox in
     * the cell will set/unset this property based on user interactions, and the
     * CheckBox will reflect the state of the {@code ObservableValue<Boolean>},
     * if it changes externally).
     *
     * @param <S> The type of the TableView generic type
     * @param column The TableColumn of type Boolean
     * @return A {@link Callback} that will return a {@link TableCell} that is
     *      able to work on the type of element contained within the TableColumn.
     */
    public static <S> Callback<TableColumn<S,Boolean>, TableCell<S,Boolean>> forTableColumn(
            final TableColumn<S, Boolean> column) {
        return forTableColumn(null, null);
    }

    /**
     * Creates a cell factory for use in a {@link TableColumn} cell factory.
     * This method requires that the TableColumn be of type
     * {@code ObservableValue<Boolean>}.
     *
     * <p>When used in a TableColumn, the CheckBoxCell is rendered with a
     * CheckBox centered in the column.
     *
     * @param <S> The type of the TableView generic type
     * @param <T> The type of the elements contained within the {@link TableColumn}
     *      instance.
     * @param getSelectedProperty A Callback that, given an object of
     *      type {@code TableColumn<S,T>}, will return an
     *      {@code ObservableValue<Boolean>}
     *      that represents whether the given item is selected or not. This
     *      {@code ObservableValue<Boolean>} will be bound bidirectionally
     *      (meaning that the CheckBox in the cell will set/unset this property
     *      based on user interactions, and the CheckBox will reflect the state of
     *      the {@code ObservableValue<Boolean>}, if it changes externally).
     * @return A {@link Callback} that will return a {@link TableCell} that is
     *      able to work on the type of element contained within the TableColumn.
     */
    public static <S,T> Callback<TableColumn<S,T>, TableCell<S,T>> forTableColumn(
            final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty) {
        return forTableColumn(getSelectedProperty, null);
    }

    /**
     * Creates a cell factory for use in a {@link TableColumn} cell factory.
     * This method requires that the TableColumn be of type
     * {@code ObservableValue<Boolean>}.
     *
     * <p>When used in a TableColumn, the CheckBoxCell is rendered with a
     * CheckBox centered in the column.
     *
     * @param <S> The type of the TableView generic type
     * @param <T> The type of the elements contained within the {@link TableColumn}
     *      instance.
     * @param getSelectedProperty A Callback that, given an object of
     *      type {@code TableColumn<S,T>}, will return an
     *      {@code ObservableValue<Boolean>}
     *      that represents whether the given item is selected or not. This
     *      {@code ObservableValue<Boolean>} will be bound bidirectionally
     *      (meaning that the CheckBox in the cell will set/unset this property
     *      based on user interactions, and the CheckBox will reflect the state of
     *      the {@code ObservableValue<Boolean>}, if it changes externally).
     * @param showLabel In some cases, it may be desirable to show a label in
     *      the TableCell beside the {@link CheckBox}. By default a label is not
     *      shown, but by setting this to true the item in the cell will also
     *      have toString() called on it. If this is not the desired behavior,
     *      consider using
     *      {@link #forTableColumn(javafx.util.Callback, javafx.util.StringConverter) },
     *      which allows for you to provide a callback that specifies the label for a
     *      given row item.
     * @return A {@link Callback} that will return a {@link TableCell} that is
     *      able to work on the type of element contained within the TableColumn.
     */
    public static <S,T> Callback<TableColumn<S,T>, TableCell<S,T>> forTableColumn(
            final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty,
            final boolean showLabel) {
        StringConverter<T> converter = ! showLabel ?
                null : CellUtils.<T>defaultStringConverter();
        return forTableColumn(getSelectedProperty, converter);
    }

    /**
     * Creates a cell factory for use in a {@link TableColumn} cell factory.
     * This method requires that the TableColumn be of type
     * {@code ObservableValue<Boolean>}.
     *
     * <p>When used in a TableColumn, the CheckBoxCell is rendered with a
     * CheckBox centered in the column.
     *
     * @param <S> The type of the TableView generic type
     * @param <T> The type of the elements contained within the {@link TableColumn}
     *      instance.
     * @param getSelectedProperty A Callback that, given an object of type
     *      {@code TableColumn<S,T>}, will return an
     *      {@code ObservableValue<Boolean>} that represents whether the given
     *      item is selected or not. This {@code ObservableValue<Boolean>} will
     *      be bound bidirectionally (meaning that the CheckBox in the cell will
     *      set/unset this property based on user interactions, and the CheckBox
     *      will reflect the state of the {@code ObservableValue<Boolean>}, if
     *      it changes externally).
     * @param converter A StringConverter that, give an object of type T, will return a
     *      String that can be used to represent the object visually. The default
     *      implementation in {@link #forTableColumn(Callback, boolean)} (when
     *      showLabel is true) is to simply call .toString() on all non-null
     *      items (and to just return an empty string in cases where the given
     *      item is null).
     * @return A {@link Callback} that will return a {@link TableCell} that is
     *      able to work on the type of element contained within the TableColumn.
     */
    public static <S,T> Callback<TableColumn<S,T>, TableCell<S,T>> forTableColumn(
            final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<T> converter) {
        return list -> new CheckBoxTableCell<S,T>(getSelectedProperty, converter);
    }



    /* *************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/
    private XmCheckBox checkBox;

    private boolean showLabel;

    private ObservableValue<Boolean> booleanProperty;



    /* *************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a default CheckBoxTableCell.
     */
    public XmCheckBoxTableCell() {
        this(null, null);
    }

    /**
     * Creates a default CheckBoxTableCell with a custom {@link Callback} to
     * retrieve an ObservableValue for a given cell index.
     *
     * @param getSelectedProperty A {@link Callback} that will return an {@link
     *      ObservableValue} given an index from the TableColumn.
     */
    public XmCheckBoxTableCell(
            final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty) {
        this(getSelectedProperty, null);
    }

    /**
     * Creates a CheckBoxTableCell with a custom string converter.
     *
     * @param getSelectedProperty A {@link Callback} that will return a {@link
     *      ObservableValue} given an index from the TableColumn.
     * @param converter A StringConverter that, given an object of type T, will return a
     *      String that can be used to represent the object visually.
     */
    public XmCheckBoxTableCell(
            final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty,
            final StringConverter<T> converter) {
        // we let getSelectedProperty be null here, as we can always defer to the
        // TableColumn
        this.getStyleClass().add("check-box-table-cell");

        this.checkBox = new XmCheckBox();
        this.checkBox.setSizeType(SizeType.SMALL);

        // by default the graphic is null until the cell stops being empty
        setGraphic(null);

        setSelectedStateCallback(getSelectedProperty);
        setConverter(converter);

//        // alignment is styleable through css. Calling setAlignment
//        // makes it look to css like the user set the value and css will not
//        // override. Initializing alignment by calling set on the
//        // CssMetaData ensures that css will be able to override the value.
//        final CssMetaData prop = CssMetaData.getCssMetaData(alignmentProperty());
//        prop.set(this, Pos.CENTER);

        tableRowProperty().addListener((ob, ov, newRow)->{
            newRow.getPseudoClassStates().addListener((SetChangeListener<PseudoClass>) change -> setTextColor());

            setAlignment(Pos.CENTER);
            setTextColor();
            XmTableView tableView = (XmTableView) getTableView();
            Paint borderColor = tableView.getBorderColor();
            checkBox.setColorType(tableView.getColorType());
            setBorder(new Border(
                    new BorderStroke(Color.TRANSPARENT, borderColor, Color.TRANSPARENT, Color.TRANSPARENT,
                            BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                            CornerRadii.EMPTY,
                            new BorderWidths(0, 1, 0, 0),
                            new Insets(0, 0,0,0)
                    )));

            newRow.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                checkBox.setSelected(!checkBox.isSelected());
            });

            newRow.itemProperty().addListener((ob1, ov1, nv1)->{
                if(nv1 == null)
                    return;
//                System.out.println(nv1.toString()+":"+tv.getCheckedValues()+":"+tv.getCheckedValues().contains(nv));
                checkBox.setSelected(tableView.getCheckedValues().contains(nv1));

            });

        });

        checkBox.setMouseTransparent(true);
        checkBox.selectedProperty().addListener((ob, ov, nv)->{
            S item = getTableRow().getItem();
            XmTableView tableView = (XmTableView) getTableView();
            if(nv){
                boolean contains = tableView.getCheckedValues().contains(item);
                if(!contains){
                    tableView.addCheckedValue(item);
//                    System.out.println(tableView.getCheckedValues());
                }
            }else{
                tableView.getCheckedValues().remove(item);
            }
        });


    }

    private void setTextColor(){
        if(getTableRow().getPseudoClassStates().contains(selected)){
            checkBox.setColorType(ColorType.other(Color.WHITE));
            setTextFill(Color.WHITE);
        }else{
            XmTableView xtv = (XmTableView) getTableView();
            if(xtv.getCellTextColor() == null){
                if(HueType.LIGHT.equals(xtv.getHueType())){
                    setTextFill(Color.web("#333333"));
                }else{
                    setTextFill(Color.web("#eeeeee"));
                }
            }else{
                setTextFill(xtv.getCellTextColor());
            }

            checkBox.setColorType(xtv.getColorType());

        }
    }


    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    // --- converter
    private ObjectProperty<StringConverter<T>> converter =
            new SimpleObjectProperty<StringConverter<T>>(this, "converter") {
                protected void invalidated() {
                    updateShowLabel();
                }
            };

    /**
     * The {@link StringConverter} property.
     * @return the {@link StringConverter} property
     */
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    /**
     * Sets the {@link StringConverter} to be used in this cell.
     * @param value the {@link StringConverter} to be used in this cell
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
    private ObjectProperty<Callback<Integer, ObservableValue<Boolean>>>
            selectedStateCallback =
            new SimpleObjectProperty<Callback<Integer, ObservableValue<Boolean>>>(
                    this, "selectedStateCallback");

    /**
     * Property representing the {@link Callback} that is bound to by the
     * CheckBox shown on screen.
     * @return the property representing the {@link Callback} that is bound to
     * by the CheckBox shown on screen
     */
    public final ObjectProperty<Callback<Integer, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return selectedStateCallback;
    }

    /**
     * Sets the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @param value the {@link Callback} that is bound to by the CheckBox shown
     * on screen
     */
    public final void setSelectedStateCallback(Callback<Integer, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }

    /**
     * Returns the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @return the {@link Callback} that is bound to by the CheckBox shown on screen
     */
    public final Callback<Integer, ObservableValue<Boolean>> getSelectedStateCallback() {
        return selectedStateCallbackProperty().get();
    }



    /* *************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public XmCheckBox getCheckBox(){
        return this.checkBox;
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            StringConverter<T> c = getConverter();

            if (showLabel) {
                setText(c.toString(item));
            }
            setGraphic(checkBox);

            if (booleanProperty instanceof BooleanProperty) {
                checkBox.selectedProperty().unbindBidirectional((BooleanProperty)booleanProperty);
            }
            ObservableValue<?> obsValue = getSelectedProperty();
            if (obsValue instanceof BooleanProperty) {
                booleanProperty = (ObservableValue<Boolean>) obsValue;
                checkBox.selectedProperty().bindBidirectional((BooleanProperty)booleanProperty);
            }

            checkBox.disableProperty().bind(Bindings.not(
                    getTableView().editableProperty().and(
                            getTableColumn().editableProperty()).and(
                            editableProperty())
            ));
        }
    }



    /* *************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    private void updateShowLabel() {
        this.showLabel = converter != null;
//        this.checkBox.setTextAlignment(showLabel ? Pos.CENTER_LEFT : Pos.CENTER);
    }

    private ObservableValue<?> getSelectedProperty() {
        return getSelectedStateCallback() != null ?
                getSelectedStateCallback().call(getIndex()) :
                getTableColumn().getCellObservableValue(getIndex());
    }
}