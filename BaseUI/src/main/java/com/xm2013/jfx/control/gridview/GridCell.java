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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * A GridCell is created to represent items in the {@link GridView} 
 * {@link GridView#getItems() items list}. As with other JavaFX UI controls
 * (like {@link ListView}, {@link TableView}, etc), the {@link GridView} control
 * is virtualised, meaning it is exceedingly memory and CPU efficient. Refer to
 * the {@link GridView} class documentation for more details.
 *  
 * @see GridView
 */
public class GridCell<T> extends IndexedCell<T> {

    private PseudoClass selected = PseudoClass.getPseudoClass("selected");

    private BooleanProperty selectedState = new SimpleBooleanProperty(false);
    public BooleanProperty selectedStateProperty(){
        return selectedState;
    }

    /**************************************************************************
     * 
     * Constructors
     * 
     **************************************************************************/

    /**
     * Creates a default GridCell instance.
     */
	public GridCell() {
		getStyleClass().add("grid-cell"); //$NON-NLS-1$
		
//		itemProperty().addListener(new ChangeListener<T>() {
//            @Override public void changed(ObservableValue<? extends T> arg0, T oldItem, T newItem) {
//                updateItem(newItem, newItem == null);
//            }
//        });
		
		// TODO listen for index change and update index and item, rather than
		// listen to just item update as above. This requires the GridCell to 
		// know about its containing GridRow (and the GridRow to know its 
		// containing GridView)
		indexProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                final GridView<T> gridView = getGridView();
                if (gridView == null) return;
                
                if(getIndex() < 0) {
                    updateItem(null, true);
                    return;
                }
                T item = gridView.getItems().get(getIndex());
                
//                updateIndex(getIndex());
                updateItem(item, item == null);
            }
        });

        setOnMouseClicked(e -> {
            selectedState.set(!selectedStateProperty().get());
            GridView<T> gv = getGridView();
            gv.setValue(getItem());
        });

        selectedStateProperty().addListener((ob, ov, nv)->{
            GridView<T> gv = getGridView();

            if(nv){
                GridCell<T> selectedCell = gv.selectedCell;
                if(selectedCell!=null){
                    selectedCell.selectedStateProperty().set(false);
                }
                gv.selectedCell = this;

            }

            pseudoClassStateChanged(selected, nv);

        });

        init();
	}

    public void init(){

    }

    /**
	 * {@inheritDoc}
	 */
	@Override protected Skin<?> createDefaultSkin() {
        return new GridCellSkin<>(this);
    }

//    @Override
//    protected void updateItem(T item, boolean empty) {
//        super.updateItem(item, empty);
//        System.out.println(this+","+selectedStateProperty().get());
//    }

    /**************************************************************************
     * 
     * Properties
     * 
     **************************************************************************/

    /**
     * The {@link GridView} that this GridCell exists within.
     * @return SimpleObjectProperty
     */
    public SimpleObjectProperty<GridView<T>> gridViewProperty() {
        return gridView;
    }
    private final SimpleObjectProperty<GridView<T>> gridView = 
            new SimpleObjectProperty<>(this, "gridView"); //$NON-NLS-1$
    
    /**
     * Sets the {@link GridView} that this GridCell exists within.
     * @param gridView GridView
     */
    public final void updateGridView(GridView<T> gridView) {
        this.gridView.set(gridView);
    }
    
    /**
     * Returns the {@link GridView} that this GridCell exists within.
     * @return GridView
     */
    public GridView<T> getGridView() {
        return gridView.get();
    }
}