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
package com.xm2013.jfx.common;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuPopup.
 */
public class MenuPopup extends Popup{
	
	/** The owner. */
	private Window owner;
	
	/** The menus. */
	private ObservableList<XmMenu> menus = FXCollections.observableArrayList();

	/** The width. */
	private DoubleProperty width = new SimpleDoubleProperty(300);
	
	/** The height. */
	private DoubleProperty height = new SimpleDoubleProperty(100);
	
	/** The view. */
	private VBox view;

	/** The item clicked. */
	private CallBack<XmMenu> itemClicked;

	/** The close listener. */
	private EventHandler closeListener = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

//			Node node = (Node) event.getTarget();
//
//			Point2D anchorPoint = node.localToScreen(
//					node.prefWidth(-1), node.prefHeight(-1)
//			);
//
//			System.out.println(event.getTarget());
			hide();
		}
	};

	/** The window focus listener. */
	private ChangeListener<Boolean> windowFocusListener = ((observable, oldValue, newValue) -> {
		if(newValue==false){
			hide();
		}
	});

	/** The item click listener. */
	private EventHandler<MouseEvent> itemClickListener = (e) -> {

		Node target = (Node) e.getTarget();
		if(target instanceof Label == false){
			target = target.getParent();
		}

		if(target instanceof Label == false){
			return;
		}

		Label item = (Label) target;
		List<Node> items = view.getChildren();
		for(Node child : items){
			child.getStyleClass().remove("selected");
		}

		item.getStyleClass().add("selected");

		XmMenu menuItem = (XmMenu) item.getUserData();
		if(menuItem.getChildren()!=null && menuItem.getChildren().size()>0){
			//如果存在三级菜单，继续打开popup
			MenuPopup menuPopup = new MenuPopup(owner);
			menuPopup.setItemClicked(menu -> {
				if(itemClicked!=null) {
					itemClicked.call(menuItem);
					menuPopup.hide();
				}
			});
			ObservableList<? extends XmMenu> children = menuItem.getChildren();
			menuPopup.getMenus().addAll(children);
			menuPopup.setPopHeight(children.size()*40);
			menuPopup.setPopWidth(200);
			menuPopup.show(this, this.getX()+this.getWidth(), this.getY());
		}else{
			if(itemClicked!=null) {
				itemClicked.call(menuItem);
				hide();
			}
		}

	};

	/**
	 * Instantiates a new menu popup.
	 *
	 * @param owner the owner
	 */
	public MenuPopup(Window owner) {
		this.owner = owner;
		this.setWidth(width.get());
		this.setHeight(height.get());

		view = new VBox();
		view.prefWidthProperty().bind(width);
		view.prefHeightProperty().bind(height);
		view.setFillWidth(true);
		view.getStylesheets().add(FxKit.getResourceURL("/theme/default-theme.css"));
		view.getStyleClass().add("menu-list");

		menus.addListener(new ListChangeListener<XmMenu>() {
			@Override
			public void onChanged(Change<? extends XmMenu> c) {

				view.getChildren().clear();
				for(XmMenu menu : menus){
					Label m = new Label();
					m.prefWidthProperty().bind(width);
					m.setGraphic(menu.getIcon().clone());
					m.setText(menu.getLabel());
					m.setUserData(menu);
					view.getChildren().add(m);
				}

			}
		});

		view.addEventFilter(MouseEvent.MOUSE_CLICKED, itemClickListener);

//		view.setCellFactory(new Callback<ListView<XmMenu>, ListCell<XmMenu>>() {
//			@Override
//			public ListCell<XmMenu> call(ListView<XmMenu> param) {
//				return new ListCell<XmMenu>(){
//					protected void updateItem(XmMenu item, boolean empty) {
//						super.updateItem(item, empty);
//
//						if (empty || item == null) {
//							setText(null);
//							setGraphic(null);
//						} else {
//							setText(item.getLabel());
//							setGraphic(item.getIcon());
//							setGraphicTextGap(6);
//						}
//					}
//				};
//			}
//		});
//
//		view.setOnMouseClicked(event -> {
//			if (event.getClickCount() == 1) { // 如果是单击
//
//				XmMenu selectedItem = view.getSelectionModel().getSelectedItem();
//				if(selectedItem == null) return;
//				if(selectedItem.getChildren()!=null && selectedItem.getChildren().size()>0){
//					//如果存在三级菜单，继续打开popup
//					MenuPopup menuPopup = new MenuPopup(owner);
//					menuPopup.setItemClicked(menu -> {
//						if(itemClicked!=null) {
//							itemClicked.call(selectedItem);
//							menuPopup.hide();
//						}
//					});
//					ObservableList<? extends XmMenu> children = selectedItem.getChildren();
//					menuPopup.getMenus().addAll(children);
//					menuPopup.setPopHeight(children.size()*50);
//					menuPopup.show(this, this.getX()+this.getWidth(), this.getY());
//				}else{
//					if(itemClicked!=null) {
//						itemClicked.call(selectedItem);
//						hide();
//					}
//				}
//			}
//		});

		this.getContent().add(view);
	}

	public void setItemClicked(CallBack<XmMenu> itemClicked) {
		this.itemClicked = itemClicked;
	}

	public ObservableList<XmMenu> getMenus() {
		return menus;
	}

	/**
	 * Show.
	 *
	 * @param node the node
	 * @param x the x
	 * @param y the y
	 */
	public void show(Node node, int x, int y){

		double width = node.prefWidth(-1);
		double height = node.prefHeight(-1);

		Point2D anchorPoint = node.localToScreen(
				width,height
		);

		this.show(node, anchorPoint.getX()+width+x, anchorPoint.getY()-height+y);
		if(owner!=null){
			owner.addEventFilter(MouseEvent.MOUSE_CLICKED, closeListener);
			owner.focusedProperty().addListener(windowFocusListener);
		}
	}

	/**
	 * Hide.
	 */
	public void hide(){
		super.hide();
		if(owner!=null){
			owner.removeEventFilter(MouseEvent.MOUSE_CLICKED, closeListener);
			owner.focusedProperty().removeListener(windowFocusListener);
		}
		view.removeEventFilter(MouseEvent.MOUSE_CLICKED, itemClickListener);
	}

	public double getPopWidth() {
		return width.get();
	}

	/**
	 * Pop width property.
	 *
	 * @return the double property
	 */
	public DoubleProperty popWidthProperty() {
		return width;
	}

	public void setPopWidth(double width) {
		this.width.set(width);
	}

	public double getPopHeight() {
		return height.get();
	}

	/**
	 * Pop height property.
	 *
	 * @return the double property
	 */
	public DoubleProperty popHeightProperty() {
		return height;
	}

	public void setPopHeight(double height) {
		if(height<100){
			height = 100;
		}
		this.height.set(height);
	}


}
