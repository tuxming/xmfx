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

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Skin;

/**
 * A GridRow is a container for {@link com.xm2013.jfx.control.data.GridCell}, and represents a single
 * row inside a {@link GridView}.
 */
class GridRow<T> extends IndexedCell<T>{


    /**************************************************************************
     * 
     * Constructors
     * 
     **************************************************************************/	

    /**
     * 
     */
    public GridRow() {
        super();
        getStyleClass().add("grid-row"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override protected Skin<?> createDefaultSkin() {
        return new GridRowSkin<>(this);
    }



    /**************************************************************************
     * 
     * Properties
     * 
     **************************************************************************/

    /**
     * The {@link GridView} that this GridRow exists within.
     */
    public SimpleObjectProperty<GridView<T>> gridViewProperty() {
        return gridView;
    }
    private final SimpleObjectProperty<GridView<T>> gridView = 
            new SimpleObjectProperty<>(this, "gridView"); //$NON-NLS-1$
    
    /**
     * Sets the {@link GridView} that this GridRow exists within.
     */
    public final void updateGridView(GridView<T> gridView) {
        this.gridView.set(gridView);
    }
    
    /**
     * Returns the {@link GridView} that this GridRow exists within.
     */
    public GridView<T> getGridView() {
        return gridView.get();
    }



    @Override
    public void updateIndex(int i) {
        super.updateIndex(i);
        // Fixes #879 (https://bitbucket.org/controlsfx/controlsfx/issues/879)
        // When VirtualFlow.setCellIndex is called GridRow should update its cells
        // even when the index did not change.
        GridRowSkin<?> skin = (GridRowSkin<?>) getSkin();
        if (skin != null) {
            skin.updateCells();
        }

        // We need to do this to allow for mouse wheel scrolling,
        // as the GridRow has to report that it is non-empty (which
        // is the second argument going into updateItem).
        updateItem(null, getIndex() == -1);
    }
}
