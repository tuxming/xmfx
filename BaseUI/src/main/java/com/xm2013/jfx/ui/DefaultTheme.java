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
package com.xm2013.jfx.ui;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.common.MenuPopup;
import com.xm2013.jfx.common.XmMenu;
import com.xm2013.jfx.component.eventbus.FXEventBus;
import com.xm2013.jfx.component.eventbus.XmEvent;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.List;

/**
 * <pre style="line-height: 100%;">
 * ┌─────────────────┬────────────────────────────────┐&lt;br/&gt;
 * │     left        │       center(BorderPane)       │&lt;br/&gt;
 * │     HBox        │          centerPane            │&lt;br/&gt;
 * │   leftPane      │ ┌────────────────────────────┐ │&lt;br/&gt;
 * │ ┌────────────┐  │ │     top(BorderPane)        │ │&lt;br/&gt;
 * │ │ VBox(logo) │  │ │ ┌──────┐                   │ │&lt;br/&gt;
 * │ │  logoBox   │  │ │ │expand│                   │ │&lt;br/&gt;
 * │ └────────────┘  │ │ └──────┘                   │ │&lt;br/&gt;
 * │                 │ │ ┌────────────────────────┐ │ │&lt;br/&gt;
 * │ ┌─────────────┐ │ │ │     center(mainPane)   │ │ │&lt;br/&gt;
 * │ │             │ │ │ │       TabPane          │ │ │&lt;br/&gt;
 * │ │  TreeView   │ │ │ │                        │ │ │&lt;br/&gt;
 * │ │    menu     │ │ │ └────────────────────────┘ │ │&lt;br/&gt;
 * │ │menuTreeView │ │ │ ┌────────────────────────┐ │ │&lt;br/&gt;
 * │ │             │ │ │ │    status bar          │ │ │&lt;br/&gt;
 * │ └─────────────┘ │ │ └────────────────────────┘ │ │&lt;br/&gt;
 * │                 │ └────────────────────────────┘ │&lt;br/&gt;
 * └─────────────────┴────────────────────────────────┘&lt;br/&gt;
 * </pre>
 */
public class DefaultTheme extends BorderPane {


    /**
     * 整个左边的区域
     */
    private VBox leftPane;
    private BorderPane centerPane;

    /**
     * 左侧菜单是否展开
     */
    private SimpleBooleanProperty expand = new SimpleBooleanProperty(true);

    /**
     * 图标旋转的设置
     */
    private Rotate expandRotate = new Rotate();

    /**
     * logo属性
     */
    private ImageView logoImg;
    private Text title;
    private Text subTitle;
    private VBox logoBox = null;

    /**
     * 右边的顶部区域
     */
    private BorderPane top =  null;

    /**
     * 最大化，最小化，关闭按钮的容器
     */
    private FlowPane winBtnBox;
    /**
     * 最大化，最小化，关闭按钮的容器是否在左边
     */
    private boolean winBtnBoxIsLeft =  true;

    /**
     * 菜单项
     */
    private ObservableList<XmMenu> menus = FXCollections.observableArrayList();
    /**
     * 菜单组件
     */
    private TreeView<XmMenu> menuTreeView;
    /**
     * 主内容区域
     */
    private TabPane mainPane;
    /**
     * 底部状态栏
     */
    private HBox statusBar;
    private Region expandIcon;

    public DefaultTheme(){
        this.getStylesheets().add(FxKit.getResourceURL("/theme/default-theme.css"));
        this.centerPane = new BorderPane();

        top = new BorderPane();
        winBtnBox = new FlowPane();

        //初始化侧边栏
        this.initLeftPane();

        //初始化右边的顶部
        this.initTop();

        this.setCenter(centerPane);

        //初始中间主要内容模块
        this.initCenter();

        this.initStausBar();

    }

    /**
     * 初始化左边的pane
     */
    public void initLeftPane(){

        /**
         * 添加，最大化，最小化，关闭按钮
         */
        this.leftPane = new VBox();
        this.leftPane.getStyleClass().add("left-pane");
        leftPane.setPrefWidth(300);
        this.setLeft(leftPane);

        Region closeIcon = new Region();
        closeIcon.getStyleClass().add("close-icon");

        Region maxIcon = new Region();
        maxIcon.getStyleClass().add("max-icon");

        Region minIcon = new Region();
        minIcon.getStyleClass().add("min-icon");

        //添加关闭按钮
        Label closeBtn = new Label(null, closeIcon);
        Label maxBtn = new Label(null,maxIcon);
        Label minBtn = new Label(null, minIcon);

        //添加class
        closeBtn.getStyleClass().addAll("win-btn", "win-close-btn");
        maxBtn.getStyleClass().addAll("win-btn", "win-max-btn");
        minBtn.getStyleClass().addAll("win-btn", "win-min-btn");

        closeBtn.setAlignment(Pos.CENTER);
        maxBtn.setAlignment(Pos.CENTER);
        minBtn.setAlignment(Pos.CENTER);

        winBtnBox.setAlignment(Pos.CENTER_LEFT);
        winBtnBox.setHgap(2);
        winBtnBox.setVgap(2);
        winBtnBox.setPadding(new Insets(5, 0, 0, 5));
        winBtnBox.getChildren().addAll(closeBtn,maxBtn, minBtn);
        setWinBtnToRight();

        //最小化事件监听
        minBtn.setOnMouseClicked((event)-> FXEventBus.getDefault().fireEvent(new XmEvent<>(XmEvent.MIN_WINDOW)));

        //最大化事件监听
        maxBtn.setOnMouseClicked((event)-> FXEventBus.getDefault().fireEvent(new XmEvent<>(XmEvent.MAX_WINDOW)));

        //关闭事件监听
        closeBtn.setOnMouseClicked((event)->FXEventBus.getDefault().fireEvent(new XmEvent<>(XmEvent.CLOSE_WINDOW)));

        /*
          添加logo, 主标题，子标题
         */
        logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER);
        logoBox.getStyleClass().add("logo-box");

        logoImg = new ImageView();
        logoImg.getStyleClass().add("logo-img");
        logoImg.setFitHeight(100);
        logoImg.setFitWidth(100);
        logoImg.setPreserveRatio(true);
        Circle circle = new Circle();
        circle.centerXProperty().bind(Bindings.createDoubleBinding(()-> logoImg.getFitWidth() / 2, logoImg.fitWidthProperty()));
        circle.centerYProperty().bind(Bindings.createDoubleBinding(()-> logoImg.getFitHeight() / 2, logoImg.fitHeightProperty()));
        circle.radiusProperty().bind(Bindings.createDoubleBinding(()-> logoImg.getFitWidth() / 2, logoImg.fitWidthProperty()));
        logoImg.setClip(circle);

        title = new Text();
        title.getStyleClass().add("x-title");
        subTitle = new Text();
        subTitle.getStyleClass().add("x-sub-title");

        logoBox.getChildren().addAll(logoImg, title, subTitle);
        this.leftPane.getChildren().add(logoBox);

        //添加左侧菜单，菜单根节点
        XmMenu root = new XmMenu() {
            @Override
            public XmIcon getIcon() {
                return new XmFontIcon("");
            }

            @Override
            public String getLabel() {
                return "Root";
            }

            @Override
            public Node getPage() {
                return null;
            }

            @Override
            public String getKey() {
                return "Root";
            }

            @Override
            public XmMenu getParent() {
                return null;
            }

            @Override
            public ObservableList<? extends XmMenu> getChildren() {
                return menus;
            }
        };

        TreeItem<XmMenu> rootItem = new TreeItem<>(root);
        rootItem.setGraphic(root.getIcon());
        rootItem.setExpanded(true);

        //构建菜单
        buildMenu(rootItem, menus);

        //初始化菜单树
        menuTreeView = new TreeView<>(rootItem);
        menuTreeView.setShowRoot(false);
        menuTreeView.getStyleClass().add("menu-tree");

        /*
        构建每一个菜单的显示样式
         */
        menuTreeView.setCellFactory(new Callback<TreeView<XmMenu>, TreeCell<XmMenu>>() {
            @Override
            public TreeCell<XmMenu> call(TreeView<XmMenu> param) {
                return new TreeCell<XmMenu>(){
                    @Override
                    protected void updateItem(XmMenu item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                            setText(null);
                        }else{
                            if(expand.get()==false){ //如果侧边栏是收缩状态，则不显示文字
                                setGraphic(item.getIcon());
                                setText(null);
                            }else{
                                setGraphic(item.getIcon());
                                setText(item.getLabel());
                            }
                        }
                    }
                };
            }
        });

        //添加监听事件
        menuTreeView.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                TreeItem<XmMenu> menuItem = menuTreeView.getSelectionModel().getSelectedItem();
                if(menuItem!=null)
                    handleClickMenuItem(menuItem);
            }
        });


        //设置滚动条样式
        Platform.runLater(() -> {
            ScrollBar bar = (ScrollBar) menuTreeView.lookup(".scroll-bar");
            if(bar != null)
                bar.getStyleClass().add("menu-scroll");

        });

        this.leftPane.getChildren().add(menuTreeView);
        //填充满剩余的空间
        VBox.setVgrow(menuTreeView, Priority.ALWAYS);

        //菜单列表发生变化时重新构建菜单
        menus.addListener((ListChangeListener<? super XmMenu>) c -> {
            buildMenu(rootItem, menus);
        });

    }

    /**
     * 处理菜单点击事件
     * @param menuItem
     */
    private void handleClickMenuItem(TreeItem<XmMenu> menuItem){
        if(menuItem.isLeaf()) { //如果没有子菜单，则显示界面
            //这里设置显示内容
            addContentPage(menuItem.getValue());
        }else { //如果是子菜单，则显示子菜单
            //这里设置子菜单展开
            if(expand.get()){
                menuItem.setExpanded(!menuItem.isExpanded());
            }else{ //如果右侧侧边栏没有展开，则菜单不展开，以弹出的形式展示子菜单
                menuItem.setExpanded(false);
                //如果是菜单没有展开，则以弹出的形式显示子菜单
                MenuPopup menuPopup = new MenuPopup(DefaultTheme.this.getScene().getWindow());
                menuPopup.setItemClicked(menu -> {
                    addContentPage(menu);
                });
                ObservableList<? extends XmMenu> children = menuItem.getValue().getChildren();
                menuPopup.getMenus().addAll(children);
                menuPopup.setPopHeight(children.size()*40);
                menuPopup.setPopWidth(200);
                menuPopup.show(menuItem.getGraphic(), 10, -15);
            }
        }
    }

    /**
     * 初始化顶部
     */
    public void initTop(){

        HBox leftBox = new HBox();
        leftBox.setPrefHeight(50);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        //设置展开按钮
        expandIcon = new Region();
        expandIcon.getStyleClass().add("menu-expand-icon");
        Label expandBtn = new Label("", expandIcon);
        expandBtn.getStyleClass().add("menu-expand-btn");

        leftBox.getChildren().add(expandBtn);
        leftBox.setPadding(new Insets(0,0,0, 8));

        expandRotate.setPivotX(7.5);
        expandRotate.setPivotY(4);
        expandIcon.getTransforms().add(expandRotate);
        //当window窗体按钮在左边时， 将最小化按钮的是否显示与expand属性绑定，
        this.winBtnBox.getChildren().get(1).visibleProperty().bind(Bindings.createBooleanBinding(()-> winBtnBoxIsLeft?expand.get(): true, expand));
        this.winBtnBox.getChildren().get(1).managedProperty().bind(Bindings.createBooleanBinding(()-> winBtnBoxIsLeft?expand.get(): true, expand));

        expandBtn.setOnMouseClicked(e -> {
            expand.set(!expand.get());
        });
        this.expandChangeListener();

        top.setLeft(leftBox);
        centerPane.setTop(top);
    }

    /**
     * 展开和关闭左边菜单的事件处理
     */
    private void expandChangeListener(){

        //左边侧边栏，展开或者关闭动画
        //图标旋转
        RotateTransition iconRatateTrans = new RotateTransition();
        iconRatateTrans.setNode(expandIcon);
        iconRatateTrans.setFromAngle(-90);
        iconRatateTrans.setToAngle(90);
        iconRatateTrans.setDuration(Duration.millis(200));

        //侧边栏宽度
        Timeline widthTrans = new Timeline();
//        System.out.println(leftPane.getPrefWidth());
        KeyValue kv1 = new KeyValue(leftPane.prefWidthProperty(), leftPane.getPrefWidth());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), "kf1", kv1);
        KeyValue kv2 = new KeyValue(leftPane.prefWidthProperty(), 60);
        KeyFrame kf2 = new KeyFrame(Duration.millis(200), "kf2", kv2);
        widthTrans.getKeyFrames().addAll(kf1, kf2);

        //logo缩放
        Timeline logoSizeTrans = new Timeline();
        KeyValue kv31 = new KeyValue(logoImg.fitHeightProperty(), logoImg.getFitHeight());
        KeyValue kv32 = new KeyValue(logoImg.fitWidthProperty(), logoImg.getFitWidth());
        KeyFrame kf3 = new KeyFrame(Duration.millis(0), "kf1", kv31, kv32);
        KeyValue kv41 = new KeyValue(logoImg.fitHeightProperty(), 45);
        KeyValue kv42 = new KeyValue(logoImg.fitWidthProperty(), 45);
        KeyFrame kf4 = new KeyFrame(Duration.millis(200), "kf2", kv41, kv42);
        logoSizeTrans.getKeyFrames().addAll(kf3, kf4);

        ParallelTransition pt = new ParallelTransition(iconRatateTrans, widthTrans, logoSizeTrans);
        pt.setRate(1);

        pt.setOnFinished(e1 -> {
            int count = pt.getCycleCount();
            if(count % 2==0){
                title.setVisible(true);
                title.setManaged(true);
                subTitle.setVisible(true);
                subTitle.setManaged(true);

                menuTreeView.refresh();
            }else{
                if(winBtnBoxIsLeft){
                    winBtnBox.setAlignment(Pos.CENTER);
                    winBtnBox.setPadding(new Insets(5, 0, 0, 0));
                }
                //左侧侧边栏收缩时，将展开菜单的全部关闭
                menuTreeView.getRoot().getChildren().forEach(e -> {
                    e.setExpanded(false);
                });
                menuTreeView.setPrefWidth(300);
                menuTreeView.setMaxWidth(300);
                menuTreeView.refresh();
            }

        });

        //展开或者关闭动作的监听
        ChangeListener<Boolean> expandListener = (observable, oldValue, newValue) -> {
            if(newValue){
                //展开侧边栏
                if(winBtnBoxIsLeft){
                    winBtnBox.setHgap(2);
                    winBtnBox.setVgap(2);
                    winBtnBox.setAlignment(Pos.CENTER_LEFT);
                    winBtnBox.setPadding(new Insets(5, 0, 0, 5));
                }
                pt.setRate(-1);
//                pt.playFrom(Duration.millis(0));
//                animationTimer.start();
                pt.play();
            }else{ //收缩侧边栏
                if(winBtnBoxIsLeft){
                    winBtnBox.setHgap(1);
                    winBtnBox.setVgap(1);
                }

                menuTreeView.setPrefWidth(60);
                menuTreeView.setMaxWidth(60);

                title.setVisible(false);
                title.setManaged(false);
                subTitle.setVisible(false);
                subTitle.setManaged(false);
//                animationTimer.start();
                pt.setRate(1);
//                pt.playFrom(Duration.millis(200));
                pt.play();
            }
        };

//        WeakChangeListener<Boolean> expandWeakChangeListener = new WeakChangeListener<>(expandListener);
        this.expand.addListener(expandListener);

    }

    /**
     * 初始化中间主要内容区域
     */
    private void initCenter() {

        this.mainPane = new TabPane();
        this.centerPane.setCenter(mainPane);

    }

    private void initStausBar(){
        statusBar = new HBox();
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(4,0,4,0));
        Label label = new Label("Powered By 芯码科技");
        statusBar.getChildren().addAll(label);
        this.centerPane.setBottom(statusBar);

    }

    ObservableList<String> pageKeys = FXCollections.observableArrayList();
    /**
     * 添加到内容到页面
     * @param menu XmMenu
     */
    public void addContentPage(XmMenu menu) {
        addPage(menu.getKey(), menu.getPage(), menu.getIcon().clone(), menu.getLabel());
    }

    /**
     * 添加到内容到页面
     * @param key String
     * @param page Node
     * @param icon Node
     * @param title String
     */
    public void addPage(String key, Node page, Node icon, String title){

        List<Tab> tabs = mainPane.getTabs();
        Tab foundTab = null;
        for(Tab tab : tabs){
            if(tab.getUserData().equals(key)){
                foundTab = tab;
                break;
            }
        }

        if(foundTab == null){
            Tab tab = new Tab(title, page);

            tab.setGraphic(icon);
            tab.setClosable(true);
            tab.setUserData(key);
            icon.setMouseTransparent(true);

            mainPane.getTabs().add(tab);
            mainPane.getSelectionModel().select(tab);
        }else{
            mainPane.getSelectionModel().select(foundTab);
        }

    }

    /**
     * 构建菜单
     * @param parent TreeItem
     * @param children ObservableList
     */
    public void buildMenu(TreeItem<XmMenu> parent, ObservableList<? extends XmMenu> children){

        if(children==null || children.size()==0){
            parent.getChildren().clear();
            return;
        }

        parent.getChildren().clear();
        for(XmMenu menu : children){
            TreeItem<XmMenu> menuItem = new TreeItem<>();
            menuItem.setValue(menu);
            menu.getIcon().setMouseTransparent(true);
            menuItem.setGraphic(menu.getIcon());
            parent.getChildren().add(menuItem);
            buildMenu(menuItem, menu.getChildren());
        }
    }

    /**
     * 将最大化，最小化，关闭按钮放置在左边
     */
    public void setWinBtnToLeft(){
        top.setRight(null);
        Button btn = new Button();
        winBtnBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        leftPane.getChildren().add(0, winBtnBox);
        this.winBtnBoxIsLeft = true;
    }

    /**
     * 将最大化，最小化，关闭按钮，放置在右边
     */
    public void setWinBtnToRight(){
        leftPane.getChildren().remove(winBtnBox);
        winBtnBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        top.setRight(winBtnBox);
        this.winBtnBoxIsLeft = false;
    }

    /**
     * 获取左边pane的布局，这是整个左边的pane,第一个元素，window按钮的Flowbox
     * @return VBox
     */
    public VBox getLeftPane() {
        return leftPane;
    }

    /**
     * 获取右边的pane的布局，这是整个右边的布局
     * @return BorderPane
     */
    public BorderPane getCenterPane() {
        return centerPane;
    }

    /**
     * 获取右边顶部的top
     * @return BorderPane
     */
    public BorderPane getTopPane(){
        return this.top;
    }

    /**
     * 左侧菜单栏是否展开
     * @return boolean
     */
    public boolean isExpand() {
        return expand.get();
    }

    /**
     * 左侧菜单栏是否展开的property
     * @return SimpleBooleanProperty
     */
    public SimpleBooleanProperty expandProperty() {
        return expand;
    }

    /**
     * 设置左侧菜单栏是否展开
     * @param expand boolean
     */
    public void setExpand(boolean expand) {
        this.expand.set(expand);
    }

    /**
     * 获取logo组件
     * @return ImageView
     */
    public ImageView getLogoImg() {
        return logoImg;
    }

    /**
     * 设置logo
     * @param image Image
     */
    public void setLogoImg(Image image){
        logoImg.setImage(image);
    }

    /**
     * 获取主标题组件
     * @return Text
     */
    public Text getTitle() {
        return title;
    }

    /**
     * 设置主标题
     * @param title String
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * 获取子标题
     * @return Text
     */
    public Text getSubTitle() {
        return subTitle;
    }

    /**
     * 设置子标题
     * @param subTitle String
     */
    public void setSubTitle(String subTitle) {
        this.subTitle.setText(subTitle);
    }

    /**
     * 返回最大化，最小化，关闭按钮的容器
     * @return FlowPane
     */
    public FlowPane getWinBtnBox() {
        return winBtnBox;
    }

    /**
     * 添加左侧侧菜单项
     * @param menu XmMenu
     */
    public void addAllMenu(XmMenu ...menu){
        this.menus.addAll(List.of(menu));
    }

    public void addAllMenu(List<? extends XmMenu> menus){
        this.menus.addAll(menus);
    }

    /**
     * 获取左侧菜单
     * @return ObservableList
     */
    public ObservableList<XmMenu> getMenus() {
        return menus;
    }

    /**
     * 获取菜单组件
     * @return TreeView
     */
    public TreeView<XmMenu> getMenuTreeView() {
        return menuTreeView;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if(!winBtnBoxIsLeft){
            logoBox.setPadding(new Insets(5, 0,0,0));
        }else{
            logoBox.setPadding(new Insets(0, 0,0,0));
        }
    }
}
