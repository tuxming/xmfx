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

import javafx.scene.Node;
import javafx.scene.control.skin.CellSkinBase;

public class GridRowSkin<T> extends CellSkinBase<GridRow<T>> {

    public GridRowSkin(GridRow<T> control) {
        super(control);

        // Remove any children before creating cells (by default a LabeledText exist and we don't need it)
        getChildren().clear();
        updateCells();

        // We do not have to register a listener for the index property.
        // Calling updateCells is handled by GridRow if the index is updated.
        registerChangeListener(getSkinnable().widthProperty(), e -> updateCells());
        registerChangeListener(getSkinnable().heightProperty(), e -> updateCells());
    }
    
    /**
     *  Returns a cell element at a desired index
     *  @param index The index of the wanted cell element
     *  @return Cell element if exist else null
     */
    @SuppressWarnings("unchecked")
	public GridCell<T> getCellAtIndex(int index) {
        if( index < getChildren().size() ) {
            return (GridCell<T>)getChildren().get(index);
        }
        return null;
    }
        
    /**
     *  Update all cells
     *  <p>Cells are only created when needed and re-used when possible.</p>
     */
    public void updateCells() {
        int rowIndex = getSkinnable().getIndex();
        if (rowIndex >= 0) {
            GridView<T> gridView = getSkinnable().getGridView();
            GridViewSkin<?> gridViewSkin = (GridViewSkin<?>) gridView.getSkin();
            if (gridViewSkin == null) return;
            int maxCellsInRow = gridViewSkin.computeMaxCellsInRow();
            int totalCellsInGrid = gridView.getItems().size();
            int startCellIndex = rowIndex * maxCellsInRow;
            int endCellIndex = startCellIndex + maxCellsInRow - 1;
            int cacheIndex = 0;

            for (int cellIndex = startCellIndex; cellIndex <= endCellIndex; cellIndex++, cacheIndex++) {
                if (cellIndex < totalCellsInGrid) {
                    // Check if we can re-use a cell at this index or create a new one
                    GridCell<T> cell = getCellAtIndex(cacheIndex);
                    if( cell == null ) {
                        cell = createCell();
                        getChildren().add(cell);
                    }
                    cell.updateIndex(-1);
                    cell.updateIndex(cellIndex);
                }
                // we are going out of bounds -> exist the loop
                else { break; }
            }
            
            // In case we are re-using a row that previously had more cells than
            // this one, we need to remove the extra cells that remain
            getChildren().remove(cacheIndex, getChildren().size());
        }
    }

    private GridCell<T> createCell() {
        GridView<T> gridView = getSkinnable().gridViewProperty().get();
        GridCell<T> cell;
        if (gridView.getCellFactory() != null) {
            cell = gridView.getCellFactory().call(gridView);
        } else {
            cell = createDefaultCellImpl();
        }
        cell.updateGridView(gridView);
        return cell;
    }

    private GridCell<T> createDefaultCellImpl() {
        return new GridCell<T>() {
            @Override protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(""); //$NON-NLS-1$
                } else {
                    setText(item.toString());
                }
            }
        };
    }
    
    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.MAX_VALUE;
    }

    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        GridView<T> gv = getSkinnable().gridViewProperty().get();
        return gv.getCellHeight() + gv.getVerticalCellSpacing() * 2;
    }

    @Override protected void layoutChildren(double x, double y, double w, double h) {
//        double currentWidth = getSkinnable().getWidth();
        double cellWidth = getSkinnable().gridViewProperty().get().getCellWidth();
        double cellHeight = getSkinnable().gridViewProperty().get().getCellHeight();
        double horizontalCellSpacing = getSkinnable().gridViewProperty().get().getHorizontalCellSpacing();
        double verticalCellSpacing = getSkinnable().gridViewProperty().get().getVerticalCellSpacing();

        double xPos = 0;
        double yPos = 0;

        // This has been commented out as I removed the API from GridView until
        // a use case was created.
//        HPos currentHorizontalAlignment = getSkinnable().gridViewProperty().get().getHorizontalAlignment();
//        if (currentHorizontalAlignment != null) {
//            if (currentHorizontalAlignment.equals(HPos.CENTER)) {
//                xPos = (currentWidth % computeCellWidth()) / 2;
//            } else if (currentHorizontalAlignment.equals(HPos.RIGHT)) {
//                xPos = currentWidth % computeCellWidth();
//            }
//        }

        for (Node child : getChildren()) {
            child.relocate(xPos + horizontalCellSpacing, yPos + verticalCellSpacing);
            child.resize(cellWidth, cellHeight);
            xPos = xPos + horizontalCellSpacing + cellWidth + horizontalCellSpacing;
        }
    }
}
