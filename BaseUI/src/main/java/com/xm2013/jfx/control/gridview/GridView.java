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
package com.xm2013.jfx.control.gridview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A GridView is a virtualised control for displaying {@link #getItems()} in a
 * visual, scrollable, grid-like fashion. In other words, whereas a ListView 
 * shows one {@link ListCell} per row, in a GridView there will be zero or more
 * {@link GridCell} instances on a single row.
 * 
 * <p> This approach means that the number of GridCell instances
 * instantiated will be a significantly smaller number than the number of 
 * items in the GridView items list, as only enough GridCells are created for
 * the visible area of the GridView. This helps to improve performance and 
 * reduce memory consumption. 
 * 
 * <p>Because each {@link GridCell} extends from {@link Cell}, the same approach
 * of cell factories that is taken in other UI controls is also taken in GridView.
 * This has two main benefits: 
 * 
 * <ol>
 *   <li>GridCells are created on demand and without user involvement,
 *   <li>GridCells can be arbitrarily complex. A simple GridCell may just have 
 *   its {@link GridCell#textProperty() text property} set, whereas a more complex
 *   GridCell can have an arbitrarily complex scenegraph set inside its
 *   {@link GridCell#graphicProperty() graphic property} (as it accepts any Node).
 * </ol>
 *
 * being used:
 * 
 * <br>
 * <img src="gridView.png" alt="Screenshot of GridView">
 * 
 * <p>To create this GridView was simple. Note that the majority of the code below
 * is related to randomly creating the colours to be represented:
 * 
 * <pre>
 * {@code
 * GridView<Color> myGrid = new GridView<>(list);
 * myGrid.setCellFactory(new Callback<GridView<Color>, GridCell<Color>>() {
 *     public GridCell<Color> call(GridView<Color> gridView) {
 *         return new ColorGridCell();
 *     }
 * });
 * Random r = new Random(System.currentTimeMillis());
 * for(int i = 0; i < 500; i++) {
 *     list.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0));
 * }
 * }</pre>
 * 
 * @see GridCell
 */
public class GridView<T> extends XmControl {

    /**************************************************************************
     * 
     * Constructors
     * 
     **************************************************************************/
    
    /**
     * Creates a default, empty GridView control.
     */
    public GridView() {
        this(FXCollections.<T> observableArrayList());
    }
    
    /**
     * Creates a default GridView control with the provided items prepopulated.
     * 
     * @param items The items to display inside the GridView.
     */
    public GridView(ObservableList<T> items) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setItems(items);

        setVerticalCellSpacing(12);
        setHorizontalCellSpacing(12);
        setCellHeight(64);
        setCellWidth(64);

        // ...focus model
        setFocusModel(new GridViewFocusModel<>(this));
    }

    
    /**************************************************************************
     * 
     * Public API
     * 
     **************************************************************************/
    
    /**
     * {@inheritDoc}
     */
    @Override protected Skin<?> createDefaultSkin() {
        return new GridViewSkin<>(this);
    }

    /**************************************************************************
     * 
     * Properties
     * 
     **************************************************************************/
    
    // --- horizontal cell spacing
    /**
     * Property for specifying how much spacing there is between each cell
     * in a row (i.e. how much horizontal spacing there is).
     * @return DoubleProperty
     */
    public final DoubleProperty horizontalCellSpacingProperty() {
        if (horizontalCellSpacing == null) {
            horizontalCellSpacing = new StyleableDoubleProperty(12) {
                @Override public CssMetaData<GridView<?>, Number> getCssMetaData() {
                    return StyleableProperties.HORIZONTAL_CELL_SPACING;
                }
                
                @Override public Object getBean() {
                    return GridView.this;
                }

                @Override public String getName() {
                    return "horizontalCellSpacing"; //$NON-NLS-1$
                }
            };
        }
        return horizontalCellSpacing;
    }
    private DoubleProperty horizontalCellSpacing;
    
    /**
     * Sets the amount of horizontal spacing there should be between cells in
     * the same row.
     * @param value The amount of spacing to use.
     */
    public final void setHorizontalCellSpacing(double value) {
        horizontalCellSpacingProperty().set(value);
    }
    
    /**
     * Returns the amount of horizontal spacing there is between cells in
     * the same row.
     * @return double
     */
    public final double getHorizontalCellSpacing() {
        return horizontalCellSpacing == null ? 12.0 : horizontalCellSpacing.get();
    }


    
    // --- vertical cell spacing
    /**
     * Property for specifying how much spacing there is between each cell
     * in a column (i.e. how much vertical spacing there is).
     */
    private DoubleProperty verticalCellSpacing;
    public final DoubleProperty verticalCellSpacingProperty() {
        if (verticalCellSpacing == null) {
            verticalCellSpacing = new StyleableDoubleProperty(12) {
                @Override public CssMetaData<GridView<?>, Number> getCssMetaData() {
                    return StyleableProperties.VERTICAL_CELL_SPACING;
                }
                
                @Override public Object getBean() {
                    return GridView.this;
                }

                @Override public String getName() {
                    return "verticalCellSpacing"; //$NON-NLS-1$
                }
            };
        }
        return verticalCellSpacing;
    }
    
    /**
     * Sets the amount of vertical spacing there should be between cells in
     * the same column.
     * @param value The amount of spacing to use.
     */
    public final void setVerticalCellSpacing(double value) {
        verticalCellSpacingProperty().set(value);
    }

    /**
     * Returns the amount of vertical spacing there is between cells in
     * the same column.
     * @return double
     */
    public final double getVerticalCellSpacing() {
        return verticalCellSpacing == null ? 12.0 : verticalCellSpacing.get();
    }

    // --- cell width
    /**
     * Property representing the width that all cells should be.
     * @return DoubleProperty
     */
    public final DoubleProperty cellWidthProperty() {
        if (cellWidth == null) {
            cellWidth = new StyleableDoubleProperty(64) {
                @Override public CssMetaData<GridView<?>, Number> getCssMetaData() {
                    return StyleableProperties.CELL_WIDTH;
                }
                
                @Override public Object getBean() {
                    return GridView.this;
                }

                @Override public String getName() {
                    return "cellWidth"; //$NON-NLS-1$
                }
            };
        }
        return cellWidth;
    }
    private DoubleProperty cellWidth;

    /**
     * Sets the width that all cells should be.
     * @param value double
     */
    public final void setCellWidth(double value) {
        cellWidthProperty().set(value);
    }

    /**
     * Returns the width that all cells should be.
     * @return double
     */
    public final double getCellWidth() {
        return cellWidth == null ? 64.0 : cellWidth.get();
    }

    
    // --- cell height
    /**
     * Property representing the height that all cells should be.
     * @return DoubleProperty
     */
    public final DoubleProperty cellHeightProperty() {
        if (cellHeight == null) {
            cellHeight = new StyleableDoubleProperty(64) {
                @Override public CssMetaData<GridView<?>, Number> getCssMetaData() {
                    return StyleableProperties.CELL_HEIGHT;
                }
                
                @Override public Object getBean() {
                    return GridView.this;
                }

                @Override public String getName() {
                    return "cellHeight"; //$NON-NLS-1$
                }
            };
        }
        return cellHeight;
    }
    private DoubleProperty cellHeight;

    /**
     * Sets the height that all cells should be.
     * @param value double
     */
    public final void setCellHeight(double value) {
        cellHeightProperty().set(value);
    }

    /**
     * Returns the height that all cells should be.
     * @return double
     */
    public final double getCellHeight() {
        return cellHeight == null ? 64.0 : cellHeight.get();
    }

    
    // I've removed this functionality until there is a clear need for it.
    // To re-enable it, there is code in GridRowSkin that has been commented
    // out that must be re-enabled.
    // Don't forget also to enable the styleable property further down in this
    // class.
//    // --- horizontal alignment
//    private ObjectProperty<HPos> horizontalAlignment;
//    public final ObjectProperty<HPos> horizontalAlignmentProperty() {
//        if (horizontalAlignment == null) {
//            horizontalAlignment = new StyleableObjectProperty<HPos>(HPos.CENTER) {
//                @Override public CssMetaData<GridView<?>,HPos> getCssMetaData() {
//                    return GridView.StyleableProperties.HORIZONTAL_ALIGNMENT;
//                }
//                
//                @Override public Object getBean() {
//                    return GridView.this;
//                }
//
//                @Override public String getName() {
//                    return "horizontalAlignment";
//                }
//            };
//        }
//        return horizontalAlignment;
//    }
//
//    public final void setHorizontalAlignment(HPos value) {
//        horizontalAlignmentProperty().set(value);
//    }
//
//    public final HPos getHorizontalAlignment() {
//        return horizontalAlignment == null ? HPos.CENTER : horizontalAlignment.get();
//    }

    
    // --- cell factory
    /**
     * Property representing the cell factory that is currently set in this
     * GridView, or null if no cell factory has been set (in which case the 
     * default cell factory provided by the GridView skin will be used). The cell
     * factory is used for instantiating enough GridCell instances for the 
     * visible area of the GridView. Refer to the GridView class documentation
     * for more information and examples.
     * @return ObjectProperty
     */
    public final ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactoryProperty() {
        if (cellFactory == null) {
            cellFactory = new SimpleObjectProperty<>(this, "cellFactory"); //$NON-NLS-1$
        }
        return cellFactory;
    }
    private ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactory;

    /**
     * Sets the cell factory to use to create {@link GridCell} instances to 
     * show in the GridView.
     * @param value Callback
     */
    public final void setCellFactory(Callback<GridView<T>, GridCell<T>> value) {
        cellFactoryProperty().set(value);
    }

    /**
     * Returns the cell factory that will be used to create {@link GridCell} 
     * instances to show in the GridView.
     * @return Callback
     */
    public final Callback<GridView<T>, GridCell<T>> getCellFactory() {
        return cellFactory == null ? null : cellFactory.get();
    }

    
    // --- items
    /**
     * The items to be displayed in the GridView (as rendered via {@link GridCell}
     * instances). For example, if the were being used
     * (as in the case at the top of this class documentation), this items list
     * would be populated with {@link Color} values. It is important to 
     * appreciate that the items list is used for the data, not the rendering.
     * What is meant by this is that the items list should contain Color values,
     * not the {@link Node nodes} that represent the Color. The actual rendering
     * should be left up to the {@link #cellFactoryProperty() cell factory},
     * where it will take the Color value and create / update the display as
     * necessary.
     * @return ObjectProperty
     */
    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        if (items == null) {
            items = new SimpleObjectProperty<>(this, "items"); //$NON-NLS-1$
        }
        return items;
    }
    private ObjectProperty<ObservableList<T>> items;
    
    /**
     * Sets a new {@link ObservableList} as the items list underlying GridView.
     * The old items list will be discarded.
     * @param value ObservableList
     */
    public final void setItems(ObservableList<T> value) {
        itemsProperty().set(value);
    }

    /**
     * Returns the currently-in-use items list that is being used by the
     * GridView.
     * @return ObservableList
     */
    public final ObservableList<T> getItems() {
        return items == null ? null : items.get();
    }


    private ObjectProperty<FocusModel<T>> focusModel;

    /**
     * Sets the {@link FocusModel} to be used in the ListView.
     * @param value the FocusModel to be used in the ListView
     */
    public final void setFocusModel(FocusModel<T> value) {
        focusModelProperty().set(value);
    }

    /**
     * Returns the currently installed {@link FocusModel}.
     * @return the currently installed FocusModel
     */
    public final FocusModel<T> getFocusModel() {
        return focusModel == null ? null : focusModel.get();
    }

    /**
     * The FocusModel provides the API through which it is possible
     * to both get and set the focus on a single item within a ListView. Note
     * that it has a generic type that must match the type of the ListView itself.
     * @return the FocusModel property
     */
    public final ObjectProperty<FocusModel<T>> focusModelProperty() {
        if (focusModel == null) {
            focusModel = new SimpleObjectProperty<FocusModel<T>>(this, "focusModel");
        }
        return focusModel;
    }

    private ObjectProperty<T> value = new SimpleObjectProperty<>();
    public Object getValue() {
        return value.get();
    }
    public ObjectProperty<T> valueProperty() {
        return value;
    }
    public void setValue(T value) {
        this.value.set(value);
    }

    /**
     * 当使用XmCheckBoxGridCell的时候，可通过此方法获取选中的数据
     */
    private ObservableList<T> checkedValues = FXCollections.observableArrayList();
    public ObservableList<T> getCheckedValues() {
        return checkedValues;
    }
    public void setCheckedValues(List<T> checkedValues) {
        this.checkedValues.setAll(checkedValues);
    }
    public void addCheckedValue(T ...value){
        this.checkedValues.addAll(value);
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "grid-view"; //$NON-NLS-1$

    /** @treatAsPrivate */
    private static class StyleableProperties {
        private static final CssMetaData<GridView<?>,Number> HORIZONTAL_CELL_SPACING = 
            new CssMetaData<GridView<?>,Number>("-fx-horizontal-cell-spacing", StyleConverter.getSizeConverter(), 12d) { //$NON-NLS-1$

            @Override public Double getInitialValue(GridView<?> node) {
                return node.getHorizontalCellSpacing();
            }

            @Override public boolean isSettable(GridView<?> n) {
                return n.horizontalCellSpacing == null || !n.horizontalCellSpacing.isBound();
            }

            @Override
            @SuppressWarnings("unchecked")
            public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
                return (StyleableProperty<Number>)n.horizontalCellSpacingProperty();
            }
        };
        
        private static final CssMetaData<GridView<?>,Number> VERTICAL_CELL_SPACING = 
            new CssMetaData<GridView<?>,Number>("-fx-vertical-cell-spacing", StyleConverter.getSizeConverter(), 12d) { //$NON-NLS-1$

            @Override public Double getInitialValue(GridView<?> node) {
                return node.getVerticalCellSpacing();
            }

            @Override public boolean isSettable(GridView<?> n) {
                return n.verticalCellSpacing == null || !n.verticalCellSpacing.isBound();
            }

            @Override
            @SuppressWarnings("unchecked")
            public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
                return (StyleableProperty<Number>)n.verticalCellSpacingProperty();
            }
        };
        
        private static final CssMetaData<GridView<?>,Number> CELL_WIDTH = 
            new CssMetaData<GridView<?>,Number>("-fx-cell-width", StyleConverter.getSizeConverter(), 64d) { //$NON-NLS-1$

            @Override public Double getInitialValue(GridView<?> node) {
                return node.getCellWidth();
            }

            @Override public boolean isSettable(GridView<?> n) {
                return n.cellWidth == null || !n.cellWidth.isBound();
            }

            @Override
            @SuppressWarnings("unchecked")
            public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
                return (StyleableProperty<Number>)n.cellWidthProperty();
            }
        };
        
        private static final CssMetaData<GridView<?>,Number> CELL_HEIGHT = 
            new CssMetaData<GridView<?>,Number>("-fx-cell-height", StyleConverter.getSizeConverter(), 64d) { //$NON-NLS-1$

            @Override public Double getInitialValue(GridView<?> node) {
                return node.getCellHeight();
            }

            @Override public boolean isSettable(GridView<?> n) {
                return n.cellHeight == null || !n.cellHeight.isBound();
            }

            @Override
            @SuppressWarnings("unchecked")
            public StyleableProperty<Number> getStyleableProperty(GridView<?> n) {
                return (StyleableProperty<Number>)n.cellHeightProperty();
            }
        };
        
//        private static final CssMetaData<GridView<?>,HPos> HORIZONTAL_ALIGNMENT = 
//            new CssMetaData<GridView<?>,HPos>("-fx-horizontal_alignment",
//                new EnumConverter<HPos>(HPos.class), 
//                HPos.CENTER) {
//
//            @Override public HPos getInitialValue(GridView node) {
//                return node.getHorizontalAlignment();
//            }
//
//            @Override public boolean isSettable(GridView n) {
//                return n.horizontalAlignment == null || !n.horizontalAlignment.isBound();
//            }
//
//            @Override public StyleableProperty<HPos> getStyleableProperty(GridView n) {
//                return (StyleableProperty<HPos>)n.horizontalAlignmentProperty();
//            }
//        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(HORIZONTAL_CELL_SPACING);
            styleables.add(VERTICAL_CELL_SPACING);
            styleables.add(CELL_WIDTH);
            styleables.add(CELL_HEIGHT);
//            styleables.add(HORIZONTAL_ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    // package for testing
    static class GridViewFocusModel<T> extends FocusModel<T> {

        private final GridView<T> gridView;
        private int itemCount = 0;

        public GridViewFocusModel(final GridView<T> gridView) {
            if (gridView == null) {
                throw new IllegalArgumentException("ListView can not be null");
            }

            this.gridView = gridView;

            itemsObserver = new InvalidationListener() {
                private WeakReference<ObservableList<T>> weakItemsRef = new WeakReference<>(gridView.getItems());

                @Override public void invalidated(Observable observable) {
                    ObservableList<T> oldItems = weakItemsRef.get();
                    weakItemsRef = new WeakReference<>(gridView.getItems());
                    updateItemsObserver(oldItems, gridView.getItems());
                }
            };
            this.gridView.itemsProperty().addListener(new WeakInvalidationListener(itemsObserver));
            if (gridView.getItems() != null) {
                this.gridView.getItems().addListener(weakItemsContentListener);
            }

            updateItemCount();
            updateDefaultFocus();

            focusedIndexProperty().addListener(o -> {
                gridView.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
            });
        }


        private void updateItemsObserver(ObservableList<T> oldList, ObservableList<T> newList) {
            // the listview items list has changed, we need to observe
            // the new list, and remove any observer we had from the old list
            if (oldList != null) oldList.removeListener(weakItemsContentListener);
            if (newList != null) newList.addListener(weakItemsContentListener);

            updateItemCount();
            updateDefaultFocus();
        }

        private final InvalidationListener itemsObserver;

        // Listen to changes in the listview items list, such that when it
        // changes we can update the focused index to refer to the new indices.
        private final ListChangeListener<T> itemsContentListener = c -> {
            updateItemCount();

            while (c.next()) {
                // looking at the first change
                int from = c.getFrom();

                if (c.wasReplaced() || c.getAddedSize() == getItemCount()) {
                    updateDefaultFocus();
                    return;
                }

                if (getFocusedIndex() == -1 || from > getFocusedIndex()) {
                    return;
                }

                c.reset();
                boolean added = false;
                boolean removed = false;
                int addedSize = 0;
                int removedSize = 0;
                while (c.next()) {
                    added |= c.wasAdded();
                    removed |= c.wasRemoved();
                    addedSize += c.getAddedSize();
                    removedSize += c.getRemovedSize();
                }

                if (added && !removed) {
                    focus(Math.min(getItemCount() - 1, getFocusedIndex() + addedSize));
                } else if (!added && removed) {
                    focus(Math.max(0, getFocusedIndex() - removedSize));
                }
            }
        };

        private WeakListChangeListener<T> weakItemsContentListener
                = new WeakListChangeListener<T>(itemsContentListener);

        @Override protected int getItemCount() {
            return itemCount;
        }

        @Override protected T getModelItem(int index) {
            if (isEmpty()) return null;
            if (index < 0 || index >= itemCount) return null;

            return gridView.getItems().get(index);
        }

        private boolean isEmpty() {
            return itemCount == -1;
        }

        private void updateItemCount() {
            if (gridView == null) {
                itemCount = -1;
            } else {
                List<T> items = gridView.getItems();
                itemCount = items == null ? -1 : items.size();
            }
        }

        private void updateDefaultFocus() {
            // when the items list totally changes, we should clear out
            // the focus
            int newValueIndex = -1;
            if (gridView.getItems() != null) {
                T focusedItem = getFocusedItem();
                if (focusedItem != null) {
                    newValueIndex = gridView.getItems().indexOf(focusedItem);
                }

                // we put focus onto the first item, if there is at least
                // one item in the list
                if (newValueIndex == -1) {
                    newValueIndex = gridView.getItems().size() > 0 ? 0 : -1;
                }
            }

            focus(newValueIndex);
        }
    }

}
