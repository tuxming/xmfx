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

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.skin.VirtualContainerBase;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class GridViewSkin<T> extends VirtualContainerBase<GridView<T>, GridRow<T>> {

    private VirtualFlow<GridRow<T>> flow;

    private ScrollBar vbar;
    private GridView<T> control;

    private final ListChangeListener<T> gridViewItemsListener = change -> {
        updateItemCount();
        getSkinnable().requestLayout();
    };

    private final WeakListChangeListener<T> weakGridViewItemsListener = new WeakListChangeListener<>(gridViewItemsListener);

    @SuppressWarnings("rawtypes")
    public GridViewSkin(GridView<T> control) {
        super(control);
        this.control = control;

        flow = getVirtualFlow();
        flow.setId("virtual-flow"); //$NON-NLS-1$
        flow.setPannable(false);
        flow.setVertical(true);
        flow.setFocusTraversable(getSkinnable().isFocusTraversable());
        flow.setCellFactory(param -> createCell());

        getChildren().add(flow);

        updateGridViewItems();
        updateItemCount();

        getBar();
        updateScrollBarSkin();

        // Register listeners
        registerChangeListener(control.itemsProperty(), e -> updateGridViewItems());
        registerChangeListener(control.cellFactoryProperty(), e ->  getFlow().recreateCells());
        registerChangeListener(control.parentProperty(), e -> {
            if (getSkinnable().getParent() != null && getSkinnable().isVisible()) {
                getSkinnable().requestLayout();
            }
        });
        registerChangeListener(control.cellHeightProperty(), e -> getFlow().recreateCells());
        registerChangeListener(control.cellWidthProperty(), e -> {
            updateItemCount();
            getFlow().recreateCells();
        });
        registerChangeListener(control.horizontalCellSpacingProperty(), e -> {
            updateItemCount();
            getFlow().recreateCells();
        });
        registerChangeListener(control.verticalCellSpacingProperty(), e -> getFlow().recreateCells());
        registerChangeListener(control.widthProperty(), e ->  updateItemCount());
        registerChangeListener(control.heightProperty(), e ->  updateItemCount());
    }

    /**
     * 获取虚拟滚动条, 因为gridView理论上是不会出现水平滚动条的，所以就不需要针对水平滚动条做处理
     */
    private void getBar(){
        ObservableList<Node> flowChildren = flow.getChildrenUnmodifiable();
        for (Node flowChild : flowChildren) {
            if(flowChild instanceof ScrollBar){
                ScrollBar bar = (ScrollBar) flowChild;
                Orientation orientation = bar.getOrientation();
                if(Orientation.VERTICAL.equals(orientation)){
                    vbar = bar;
                    break;
                }
            }
        }
    }

    private void updateScrollBarSkin(){
        Paint color = control.getColorType().getPaint();
        Paint scrollBarArrowHoverColor = FxKit.getOpacityPaint(color, 0.85),
                scrollBarArrowColor = FxKit.getOpacityPaint(color, 0.5),
                scrollBarThumbHoverColor =  FxKit.getOpacityPaint(color, 0.85),
                scrollBarThumbColor = FxKit.getOpacityPaint(color, 0.5);

        String scrollBarBgColor = FxKit.getLightPaint(color, 0.9).toString().replace("0x", "#");

        ObservableList<Node> vbarChildren = vbar.getChildrenUnmodifiable();
        for (Node vbarChild : vbarChildren) {
            ObservableList<String> styleClass = vbarChild.getStyleClass();
            if(styleClass.contains("track-background")){
                vbarChild.setStyle("-fx-background-color: "+scrollBarBgColor);
            }else if(styleClass.contains("increment-button")){
                Region incrementBtn = (Region) vbarChild;
                Region incrementArrow = (Region) incrementBtn.getChildrenUnmodifiable().get(0);
                SVGPath incPath = new SVGPath();
                incPath.setContent("M0,0L10,0L5,3Z");
                incrementArrow.setShape(incPath);
                incrementBtn.setStyle("-fx-background-color: transparent;");

                String bg = "-fx-background-color: "+scrollBarArrowColor.toString().replace("0x", "#")+";";
                String hoverBg = "-fx-background-color: "+scrollBarArrowHoverColor.toString().replace("0x", "#")+";";
                incrementArrow.setStyle(bg);
                incrementBtn.setScaleY(1.2);
                incrementBtn.setScaleX(1.2);
                incrementArrow.setStyle(bg);

                incrementBtn.hoverProperty().addListener((ob, ov, nv)->{
                    if(nv){
                        incrementBtn.setScaleY(1.2);
                        incrementBtn.setScaleX(1.2);
                        incrementArrow.setStyle(hoverBg);
                    }else{
                        incrementBtn.setScaleY(1.2);
                        incrementBtn.setScaleX(1.2);
                        incrementArrow.setStyle(bg);
                    }
                });
            }else if(styleClass.contains("decrement-button")){
                Region decrementBtn = (Region) vbarChild;
                Region decrementArrow = (Region) decrementBtn.getChildrenUnmodifiable().get(0);
                SVGPath decPath = new SVGPath();
                decPath.setContent("M0,3L10,3L5,0Z");
                decrementArrow.setShape(decPath);
                decrementBtn.setStyle("-fx-background-color: transparent;");

                String bg = "-fx-background-color: "+scrollBarArrowColor.toString().replace("0x", "#")+";";
                String hoverBg = "-fx-background-color: "+scrollBarArrowHoverColor.toString().replace("0x", "#")+";";
                decrementArrow.setStyle(bg);
                decrementBtn.setScaleY(1.2);
                decrementBtn.setScaleX(1.2);
                decrementArrow.setStyle(bg);

                decrementBtn.hoverProperty().addListener((ob, ov, nv)->{
                    if(nv){
                        decrementBtn.setScaleY(1.2);
                        decrementBtn.setScaleX(1.2);
                        decrementArrow.setStyle(hoverBg);
                    }else{
                        decrementBtn.setScaleY(1.2);
                        decrementBtn.setScaleX(1.2);
                        decrementArrow.setStyle(bg);
                    }
                });

            }else if(styleClass.contains("track")){
                vbarChild.setStyle("-fx-background-color: transparent;");
            }else if(styleClass.contains("thumb")){
                StackPane thumb = (StackPane) vbarChild;

                Insets ins = new Insets(0,2,0,2);
                Background bg = new Background(new BackgroundFill(scrollBarThumbColor, new CornerRadii(10), ins));
                Background hoverBg = new Background(new BackgroundFill(scrollBarThumbHoverColor, new CornerRadii(10), ins));
                thumb.setBackground(bg);
                thumb.hoverProperty().addListener((ob, ov, nv)->{
                    if(nv){
                        thumb.setBackground(hoverBg);
                    }else{
                        thumb.setBackground(bg);
                    }
                });
            }
        }
    }

    @Override
    protected VirtualFlow<GridRow<T>> createVirtualFlow() {
        return new GridVirtualFlow();
    }

    public void updateGridViewItems() {
        if (getSkinnable().getItems() != null) {
            getSkinnable().getItems().removeListener(weakGridViewItemsListener);
        }

        if (getSkinnable().getItems() != null) {
            getSkinnable().getItems().addListener(weakGridViewItemsListener);
        }

        updateItemCount();
        getFlow().recreateCells();
        getSkinnable().requestLayout();
    }

    @Override protected void layoutChildren(double x, double y, double w, double h) {
        double x1 = getSkinnable().getInsets().getLeft();
        double y1 = getSkinnable().getInsets().getTop();
        double w1 = getSkinnable().getWidth() - (getSkinnable().getInsets().getLeft() + getSkinnable().getInsets().getRight());
        double h1 = getSkinnable().getHeight() - (getSkinnable().getInsets().getTop() + getSkinnable().getInsets().getBottom());

        flow.resizeRelocate(x1, y1, w1, h1);
    }

    /**
     *  Returns the number of row needed to display the whole set of cells
     *  @return GridView row count
     */
    @Override public int getItemCount() {
        final ObservableList<?> items = getSkinnable().getItems();
        // Fix for #98 : int division should be cast to get the result as
        // double and ceiled to get the max int of it (as we are looking for
        // the max number of necessary row)
        return items == null ? 0 : (int)Math.ceil((double)items.size() / computeMaxCellsInRow());
    }

    @Override
    protected void updateItemCount() {
        if (flow == null)
            return;

        int oldCount = flow.getCellCount();
        int newCount = getItemCount();

        if (newCount != oldCount) {
            flow.setCellCount(newCount);
            getFlow().rebuildCells();
        } else {
            getFlow().reconfigureCells();
        }
        updateRows(newCount);
        getSkinnable().requestLayout();
    }

    /**
     *  Returns the max number of cell per row
     *  @return Max cell number per row 
     */
    public int computeMaxCellsInRow() {
        return Math.max((int) Math.floor(computeRowWidth() / computeCellWidth()), 1);
    }

    /**
     *  Returns the width of a row
     *  (should be GridView.width - GridView.Scrollbar.width)
     *  @return Computed width of a row 
     */
    protected double computeRowWidth() {
        // Fix for #98 : width calculation should take the scrollbar size
        // into account
        
        // TODO: need to figure out how to get the real scrollbar width and
        // replace the 18 value
        return getSkinnable().getWidth() - 18;
    }

    /**
     *  Returns the width of a cell
     *  @return Computed width of a cell 
     */
    protected double computeCellWidth() {
        return getSkinnable().cellWidthProperty().doubleValue() + (getSkinnable().horizontalCellSpacingProperty().doubleValue() * 2);
    }

    protected void updateRows(int rowCount) {
        for (int i = 0; i < rowCount; i++) {
            GridRow<T> row = flow.getVisibleCell(i);
            if (row != null) {
                // We do not have to force a change of the index by setting the index to -1
                // before setting it to its actual value. GridRow will update its cells every
                // time updateIndex is called even if the index did not change.
                row.updateIndex(i);
            }
        }
    }

    @Override protected double computeMinHeight(double height, double topInset, double rightInset, double bottomInset,
            double leftInset) {
        return 0;
    }

    private GridRow<T> createCell() {
        GridRow<T> row = new GridRow<>();
        row.updateGridView(getSkinnable());
        return row;
    }

    private GridVirtualFlow getFlow() {
        return (GridVirtualFlow) getVirtualFlow();
    }

    /**
     * Custom VirtualFlow to grant access to protected methods.
     */
    private class GridVirtualFlow extends VirtualFlow<GridRow<T>> {

        public void recreateCells(){
            super.recreateCells();
        }

        public void rebuildCells(){
            super.rebuildCells();
        }

        public void reconfigureCells(){
            super.reconfigureCells();
        }
    }
}
