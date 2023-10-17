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
package com.xm2013.jfx.control.dropdown;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.container.SimpleVScrollPane;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下拉菜单的下拉部分
 */
public class DropdownMenuPopup extends Popup {

    /**
     * 动画从上向下渐显
     */
    public final static int DIRECTION_BOTTOM = 1;
    /**
     * 动画从左向右渐显
     */
    public final static int DIRECTION_RIGHT = 2;

    //内容容器
    private final VBox popupContentPane;
    //内容容器过高会显示滚动条
    private final SimpleVScrollPane scrollPane;
    //是否分组显示，如果分组显示，则父级菜单为不可选，最多支持三级分组
    private final boolean useGroup;
    private final SizeType sizeType;

    //当前选中的item
    private ObjectProperty<DropdownMenuItem> selectedItem = new SimpleObjectProperty<>();
    //主题颜色，根据主题颜色设置显示的颜色
    private final ColorType colorType;

    //要显示的内容
    private ObservableList<? extends DropdownMenuItem> items = null;

    //是否显示
    private BooleanProperty show = new ReadOnlyBooleanWrapper(false);
    //是否在正在执行显示动画
    private boolean isShowing = false;
    //显示动画定义
    private AnimationTimer popupShowTimer;
    //是否执正在执行隐藏动画
    private boolean isHiding;
    //隐藏动画的定义
    private AnimationTimer popupHideTimer;

    //下拉菜单进入视觉的方向，默认从左向右
    private int fadeDirection = DIRECTION_BOTTOM;

    //当useGroup为false的时候，同一级节点存在多个节点具有子菜单的时候，这里用来缓存，没有子菜单，防止多次构建。
    private Map<HBox, DropdownMenuPopup> popups = new HashMap<>();

    //单useGroup的false的时候，显示了下级菜单，并且鼠标移动到子菜单上面以后，当前节点需要展示位选中状态
    private final ObjectProperty<LabelInfo> holdItem = new SimpleObjectProperty(null);

    private final List<LabelInfo> itemLabels = new ArrayList<>();

    private CallBack<DropdownMenuItem> selectedCallback = null;

    private DropdownMenuPopup parent; //多级引用

    /**
     * 缓存item的基本信息，方便统一管理
     */
    private class LabelInfo{
        //节点数据信息
        public DropdownMenuItem item;
        //节点的label
        public Label label;
        //包裹label的容器，因为label本身不知background属性，因此添加一个包裹容器来支持background
        public HBox labelPane;
        //节点的前面投标
        public Node icon;
        //当useGroup为false的时候，会存在一个箭头图标
        public SVGPath arrow;
        //单useGroup为false的时候，会判断，当前节点是否具有子节点。
        public boolean isLeaf;
        //子节点的具体数据
        public ObservableList<? extends DropdownMenuItem> children;

        //缓存事件方便销毁
        public ItemHoverChangeListener itemHoverListener = null;
        //缓存事件，方便销毁
        public ItemClickHandler itemClickHandler = null;

        public LabelInfo(Label label, HBox labelPane, DropdownMenuItem item, Node icon, SVGPath arrow, boolean isLeaf) {
            this.label = label;
            this.labelPane = labelPane;
            this.arrow = arrow;
            this.icon = icon;
            this.isLeaf = isLeaf;
            this.item = item;
            itemLabels.add(this);
        }
    }

    /**
     * @param items ObservableList 下拉菜单要显示的数据
     * @param colorType ColorType 色彩类型
     * @param useGroup boolean 是否使用分组， true：分组显示，false: 子菜单形式的显示
     * @param sizeType SizeType
     */
    public DropdownMenuPopup(ObservableList<? extends DropdownMenuItem> items,
                             ColorType colorType, SizeType sizeType, boolean useGroup){
        this.items = items;
        this.colorType = colorType;
        this.useGroup = useGroup;
        this.sizeType = sizeType;
        popupContentPane = new VBox();

        scrollPane = new SimpleVScrollPane();

        //添加阴影效果，阴影效果，会改变组件的尺寸，因此在执行doShow()方法的时候，x，y需要加一定偏移量，才是准确的
        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.THREE_PASS_BOX);
        shadow.setRadius(30);
        shadow.setColor(Color.web("#00000022"));
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);
        popupContentPane.setFillWidth(true);
//        popupContentPane.setEffect(shadow);

        System.out.println(2);

        ScrollBar scroll = new ScrollBar();
        scroll.setOrientation(Orientation.VERTICAL);

        scrollPane.setEffect(shadow);

//        popupScroll.setStyle("-fx-background-color: white;");
        scrollPane.setStyle("-fx-background-color: white;");

        scrollPane.setContent(popupContentPane);

        //监听holdeItem来时holdItem的外观样式
        holdItem.addListener(holdItemListener);

        //构建菜单来内容
        for(int i=0; i<items.size(); i++){
            DropdownMenuItem item = items.get(i);

            //当useGroup为true的时候，父节点只作为展示使用，子节点才具有交互效果
            if(useGroup){
                ObservableList<? extends DropdownMenuItem> children = item.getChildren();
                if(children.size()>0){

                    buildGroup(item, new Insets(5, 10, 5, 10));

                    for(DropdownMenuItem child : children){
                        ObservableList<? extends DropdownMenuItem> subs = child.getChildren();

                        if(subs.size()>0){
                            buildGroup(child, new Insets(5, 10, 5, 20));

                            for(DropdownMenuItem sub : subs){
                                buildItem(sub, null, new Insets(5, 10, 5, 30));
                            }

                        }else{
                            buildItem(child, null, new Insets(5, 10, 5, 20));
                        }

                    }

                }else{
                    buildItem(item, null, new Insets(5, 10, 5, 10));
                }
            }else{
                //当useGroup为false的时候，子节点以菜单的形式展示
                ObservableList<? extends DropdownMenuItem> children = item.getChildren();
                SVGPath rightArrowIcon = null;
                if(children.size()>0){ //说明具有子节点，则在当前节点添加指示的箭头符号
                    rightArrowIcon = new SVGPath();
                    rightArrowIcon.setContent("M0,0L2,0L5,5L8,0L10,0L5,7Z");
                }
                LabelInfo labelInfo = buildItem(item, rightArrowIcon, new Insets(5, 10, 5, 10));
                labelInfo.children = item.getChildren();
            }

        }
        this.getContent().add(scrollPane);

    }

    /**
     * 构建分组节点
     * @param item
     * @param padding
     */
    private void buildGroup(DropdownMenuItem item, Insets padding){

        double fontSize = 13;
        if(SizeType.SMALL.equals(sizeType)){
            fontSize = 12;
        }else if(SizeType.LARGE.equals(sizeType)){
            fontSize = 14;
        }

        Label group = new Label(item.getLabelName());
        group.getStyleClass().add("xm-dropdown-menu-group");
        group.setFont(Font.font(fontSize));
        group.setTextFill(Color.web("#666666"));
        group.setPadding(padding);

        Node icon = item.getIcon();
        if(icon != null){
            group.setGraphic(icon);
        }
        popupContentPane.getChildren().add(group);
    }

    /**
     * 构建子节点
     * @param item
     * @param rightArrowIcon
     * @param padding
     * @return
     */
    private LabelInfo buildItem(DropdownMenuItem item, SVGPath rightArrowIcon, Insets padding){
        Label label = new Label(item.getLabelName());
        label.getStyleClass().add("xm-dropdown-menu-item");

        double fontSize = 15;
        if(SizeType.SMALL.equals(sizeType)){
            fontSize = 14;
        }else if(SizeType.LARGE.equals(sizeType)){
            fontSize = 16;
        }

        label.setFont(Font.font(label.getFont().getFamily(), fontSize));

        Node icon = item.getIcon();
        if(icon != null){
            label.setGraphic(icon);
        }

        HBox labelPane = new HBox(label);
        labelPane.setPadding(padding);
        HBox.setHgrow(label, Priority.ALWAYS);

        if(rightArrowIcon!=null){
            HBox arrowPane = new HBox(rightArrowIcon);
            labelPane.getChildren().add(arrowPane);
            HBox.setHgrow(arrowPane, Priority.ALWAYS);
            arrowPane.setAlignment(Pos.CENTER_RIGHT);
            rightArrowIcon.setRotate(-90);
        }

        LabelInfo info = new LabelInfo(label, labelPane, item, icon, rightArrowIcon, rightArrowIcon==null);

        setItemLabelStyle(info, false);

        info.itemHoverListener = new ItemHoverChangeListener(info);
        labelPane.hoverProperty().addListener(info.itemHoverListener);

        info.itemClickHandler = new ItemClickHandler(info);
        labelPane.addEventFilter(MouseEvent.MOUSE_CLICKED, info.itemClickHandler);

        popupContentPane.getChildren().add(labelPane);

        return info;
    }

    /**
     * item点击事件监听
     */
    private class ItemClickHandler implements EventHandler<MouseEvent> {
        private LabelInfo info;
        public ItemClickHandler(LabelInfo info){
            this.info = info;
        }
        @Override
        public void handle(MouseEvent event) {
            DropdownMenuItem prev = selectedItem.get();

            //设置选中数据
            selectedItem.set(info.item);

            //将前一个选中的设置为未选中
            if(prev != null){
                LabelInfo info = null;
                for(LabelInfo labelInfo : itemLabels){
                    if(labelInfo.item.equals(prev)){
                        info = labelInfo;
                        break;
                    }
                }

                if(info != null){
                    setItemLabelStyle(info, false);
                }
            }

            //关闭自身
            doHide();

            //如果设置了回调函数，则调用回调函数
            if(selectedCallback!=null){
                selectedCallback.call(info.item);
            }
        }
    }

    /**
     * 节点的hover事件定义
     */
    private class ItemHoverChangeListener implements ChangeListener<Boolean> {

        private LabelInfo info = null;
        Paint fontColor = null,
                bgColor = Color.WHITE,
                activeFontColor = Color.WHITE,
                activeBgColor = null,
                fontHoverColor = Color.WHITE,
                hoverBgColor = null;

        public ItemHoverChangeListener(LabelInfo info) {
            this.info = info;

            Paint textColor = null;
            if(info.item.getTextColor()!=null){
                textColor = info.item.getTextColor();
            }else{
                textColor = colorType.getPaint();
            }

            Paint selectColor = null;
            if(info.item.getSelectColor()!=null){
                selectColor = info.item.getSelectColor();
            }else{
                selectColor = colorType.getPaint();
            }

            fontColor = textColor;
            activeBgColor = selectColor;
            hoverBgColor = FxKit.derivePaint(selectColor, 0.15);
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> ob, Boolean ov, Boolean nv) {

            if(info.item.isDisable()){
                return;
            }

            if(nv){
                //设置hover的时候，item的样式
                info.labelPane.setBackground(new Background(new BackgroundFill(hoverBgColor, new CornerRadii(0), Insets.EMPTY)));
                info.label.setTextFill(fontHoverColor);
                if(info.icon instanceof XmIcon){
                    ((XmIcon) info.icon).setColor(fontHoverColor);
                }
                if(info.arrow!=null){
                    info.arrow.setFill(fontHoverColor);
                }

                //hover的时候，如果是有子菜单， 显示子菜单
                if(!useGroup){
                    if(!info.isLeaf){
                        //先隐藏其他的子菜单
                        for(HBox pane : popups.keySet()){
                            if(!pane.equals(info.labelPane)) {
                                DropdownMenuPopup otherPopup = popups.get(pane);
                                otherPopup.hide();
                            }
                        }

                        //说明存在子节点，所以当鼠标移入到子节点的时候，当前节点需要获取到，并设置他的外观
                        holdItem.setValue(info);

                        //从缓存中获取子菜单，如果没有获取到，则构建一个新的子菜单
                        DropdownMenuPopup popup = popups.get(info.labelPane);
                        if(popup == null){
                            popup = new DropdownMenuPopup(info.children, colorType, sizeType, useGroup);
                            popup.setFadeDirection(DIRECTION_RIGHT);
                            popup.setParent(DropdownMenuPopup.this);

                            //设置回调用户获取子菜单选中的数据
                            CallBack<DropdownMenuItem> callBack;
                            if(selectedCallback == null){
                                callBack = t -> selectedItem.set(t);
                            }else{
                                callBack = t -> {
                                    selectedItem.set(t);
                                    selectedCallback.call(t);
                                };
                            }
                            popup.setSelectedCallback(callBack);
                            popups.put(info.labelPane, popup);
                        }
                        double width = info.labelPane.getWidth(), height = info.labelPane.getHeight();
                        int index = popupContentPane.getChildren().indexOf(info.labelPane);
                        popup.setSelectedItem(selectedItem.get());
                        popup.doShow(popupContentPane, width, -1,  false, getX()+ width, getY() + height*index);
                    }else{
                        holdItem.setValue(null);

                        for(HBox pane : popups.keySet()){
                            DropdownMenuPopup otherPopup = popups.get(pane);
                            if(otherPopup.isShow()){
                                otherPopup.hide();
                            }
                        }
                    }
                }
            }else{

                //设置没有hover的时候，item的样式
                if(info.item.equals(selectedItem) || info.equals(holdItem.get())){
                    info.labelPane.setBackground(new Background(new BackgroundFill(activeBgColor, new CornerRadii(0), Insets.EMPTY)));
                    info.label.setTextFill(activeFontColor);
                    if(info.icon instanceof XmIcon){
                        ((XmIcon) info.icon).setColor(activeFontColor);
                    }
                    if(info.arrow!=null){
                        info.arrow.setFill(activeFontColor);
                    }
                }else{
                    info.labelPane.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(0), Insets.EMPTY)));
                    info.label.setTextFill(fontColor);
                    if(info.icon instanceof XmIcon){
                        ((XmIcon) info.icon).setColor(fontColor);
                    }
                    if(info.arrow!=null){
                        info.arrow.setFill(fontColor);
                    }
                }

                holdItem.setValue(null);
            }
        }
    }

    /**
     * useGroup = false的时候，item存在子菜单的时候，鼠标移入到子菜单，当前item需要保持到选中状态
     */
    private ChangeListener<LabelInfo> holdItemListener = (ob, ov, nv) -> {
        // System.out.println(String.format("ov=%s, nv=%s", ov!=null?ov.item.getLabelName():null, nv!=null?nv.item.getLabelName():null));
        if(nv!=null){
            setItemLabelStyle(nv, true);
        }
        if(ov!=null){
            //设置为非选中，
            setItemLabelStyle(ov, false);
        }
    };

    /**
     * 设置label样式
     *
     */
    private void setItemLabelStyle(LabelInfo info, boolean hold){
        Paint textColor = null;
        if(info.item.getTextColor()!=null){
            textColor = info.item.getTextColor();
        }else{
            textColor = colorType.getPaint();
        }

        Paint selectColor = null;
        if(info.item.getSelectColor()!=null){
            selectColor = info.item.getSelectColor();
        }else{
            selectColor = colorType.getPaint();
        }

        if(info.item.isDisable()){
            Paint disableColor = FxKit.getOpacityPaint(textColor, 0.75);
            info.label.setTextFill(disableColor);
            if(info.icon instanceof XmIcon){
                ((XmIcon) info.icon).setColor(disableColor);
            }
            if(info.arrow!=null){
                info.arrow.setFill(disableColor);
            }
            return;
        }

        Paint fontColor = textColor,
                bgColor = Color.WHITE,
                activeFontColor = Color.WHITE,
                activeBgColor = selectColor;

        if(info.item.equals(selectedItem.get()) || hold){
            info.labelPane.setBackground(new Background(new BackgroundFill(activeBgColor, new CornerRadii(0), Insets.EMPTY)));
            info.label.setTextFill(activeFontColor);
            if(info.icon instanceof XmIcon){
                ((XmIcon) info.icon).setColor(activeFontColor);
            }
            if(info.arrow!=null){
                info.arrow.setFill(activeFontColor);
            }
        }else{
            info.labelPane.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(0), Insets.EMPTY)));
            info.label.setTextFill(fontColor);
            if(info.icon instanceof XmIcon){
                ((XmIcon) info.icon).setColor(fontColor);
            }
            if(info.arrow!=null){
                info.arrow.setFill(fontColor);
            }
        }

    }

    /**
     * 显示
     * @param control 要显示的组件依据
     * @param width 弹窗的宽
     * @param height 弹窗的高, height为-1的时候，自动根据内容设置高度
     * @param isOffset true/false是偏移量，如果是true, 显示位置根据节点的位置加上偏移量，如果是false, offsetX, offsetY就是实际现实的位置
     * @param offsetX 相对于节点的x方向偏移量，因为弹窗时候会出现偏差，所以给与手动矫正
     * @param offsetY 相对于节点的y方向偏移量，因为弹窗时候会出现偏差，所以给与手动矫正
     */
    public void doShow(Node control, double width, double height, boolean isOffset,   double offsetX, double offsetY) {
        //在执行动画的时候，则取消执行
        if(isShowing || isHiding){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        isShowing = true;

        double x = 0, y = 0;
        if(isOffset){
            Bounds nodePoint = control.localToScreen(control.getLayoutBounds());
            x = nodePoint.getMinX()+offsetX;
            y = nodePoint.getMaxY()+offsetY;

            //因为popup我给添加dropshadow效果，会导致实际尺寸发生变化，所以，这里需要手动调整去除掉effect的变化
            x = x - 30;
            y = y - 30;
        }else{
            x = offsetX;
            y = offsetY;
        }

        if(height == -1){
            scrollPane.setViewHeight(100); //先随便设置高度，让弹窗显示出来，
        }else{
            scrollPane.setViewHeight(height);
        }

        scrollPane.setViewWidth(width);
        popupContentPane.setPrefWidth(width);

        //设置选中的条目
        for(LabelInfo labelInfo : itemLabels){
            setItemLabelStyle(labelInfo, false);
        }

        //显示，但是透明度为0, 方便调整高度，和播放显示动画
        this.setOpacity(0);
        this.show(control, x, y);

        double contentHeight = popupContentPane.prefHeight(-1);

        //调整弹窗的高度
        if(height == -1){ //自己计算高度
            Screen screen = Screen.getPrimary();

            // 获取主屏幕的可视区域
            Rectangle2D bounds = screen.getVisualBounds();

            // 获取屏幕高度
            double screenHeight = bounds.getHeight();

            double balanceHeight = screenHeight - y;
//            System.out.println(String.format("balanceHeight=%f, contentHeight=%f", balanceHeight, contentHeight));
            if(balanceHeight > contentHeight){
                this.scrollPane.setViewHeight(contentHeight);
                this.scrollPane.getScrollBar().setOpacity(0);
            }else{
                this.scrollPane.setViewHeight(balanceHeight-50);
            }
        }else{
            scrollPane.setViewHeight(Math.min(height, contentHeight));
        }

        //这里判断内容的高度，是否超过了屏幕的底部，如果超过了，则显示滚动条。

        if(popupShowTimer == null){
            initPopupShowTimer();
        }
        popupShowTimer.start();

//        System.out.println(scrollPane.getViewRect().getLayoutBounds());

    }

    /**
     * 隐藏
     */
    public void doHide(){
        //在执行动画的时候，则取消执行
        if(isHiding || isShowing){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return ;
        }
        isHiding = true;
        show.set(false);

        //如果是子菜单形式暂时，则隐藏掉所有子菜单
        for(HBox key : popups.keySet()){
            DropdownMenuPopup popup = popups.get(key);
            if(popup.isShow()){
                popup.hide();
            }
        }

        if(parent != null){
            parent.doHide();
        }

        if(popupHideTimer == null){
            initPopupHideTimer();
        }

        popupHideTimer.start();
    }

    /**
     * 初始化显示动画
     */
    private void initPopupShowTimer(){

        popupShowTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private static final int TARGET_FPS = 30; // 目标帧率
            private double start = 0;
            private double end = 0;

            @Override
            public void handle(long now) {

                //初始属性
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    if(DIRECTION_BOTTOM == fadeDirection){
                        end = getY();
                        start = end - 12;
                    }else{
                        end = getX();
                        start = end - 24;
                    }
                    return;
                }

                long elapsedNanos = now - lastUpdate;
                double elapsedSeconds = elapsedNanos / 1e9;

                if (elapsedSeconds > 1.0 / TARGET_FPS) {
                    // 更新动画逻辑
                    double opacity = getOpacity();
                    if(opacity<1){
                        opacity = opacity+0.25;
                        if(opacity>1){
                            opacity = 1;
                        }

                        setOpacity(opacity);
                    }

                    if(start < end){
                        start +=3;
                        if(start > end) {
                            start = end;
                        }
                        if(fadeDirection == DIRECTION_BOTTOM){
                            setY(start);
                        }else{
                            setX(start);
                        }
                    }

                    if(opacity>=1 && start >= end){
                        stop();
                        isShowing = false;
                        show.set(true);
                        start = 0;
                        end = 0;
                        lastUpdate = 0;
                        return;
                    }

                    // 重置计数器和时间戳
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * 初始化隐藏动画
     */
    private void initPopupHideTimer(){
        popupHideTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private static final int TARGET_FPS = 30; // 目标帧率
            private double start = 0;
            private double end = 0;

            @Override
            public void handle(long now) {

                //初始属性
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    if(fadeDirection == DIRECTION_BOTTOM){
                        start = getY();
                        end = start -12;
                    }else{
                        start = getX();
                        end = start - 24;
                    }

                    return;
                }

                long elapsedNanos = now - lastUpdate;
                double elapsedSeconds = elapsedNanos / 1e9;

                if (elapsedSeconds > 1.0 / TARGET_FPS) {
                    // 更新动画逻辑
                    double opacity = getOpacity();
                    if(opacity>=0){
                        opacity = opacity-0.25;
                        if(opacity<0){
                            opacity = 0;
                        }

                        DropdownMenuPopup.this.setOpacity(opacity);
                    }

                    if(end < start){
                        if(fadeDirection == DIRECTION_BOTTOM){
                            start -=3;
                            setY(start);
                        }else{
                            start -=3;
                            setX(start);
                        }
                    }

                    if(opacity<=0 && start <= end){
                        stop();
                        isHiding = false;
                        show.set(false);
                        start = 0;
                        end = 0;
                        lastUpdate = 0;
                        DropdownMenuPopup.super.hide();
                        return;
                    }

                    // 重置计数器和时间戳
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * 设置点击回调函数
     * @param selectedCallback CallBack
     */
    public void setSelectedCallback(CallBack<DropdownMenuItem> selectedCallback) {
        this.selectedCallback = selectedCallback;
    }

    /**
     * 设置动画的执行方向
     * @param fadeDirection int
     */
    public void setFadeDirection(int fadeDirection) {
        this.fadeDirection = fadeDirection;
    }

    /**
     * 获取当前popup是否显示的
     * @return boolean
     */
    public boolean isShow() {
        return show.get();
    }

    /**
     * 获取当前popup是否显示的
     * @return BooleanProperty
     */
    public BooleanProperty showProperty() {
        return show;
    }


    public void dispose(){

        holdItem.removeListener(holdItemListener);
        for(LabelInfo info : itemLabels){
            if(info.itemHoverListener!=null)
                info.labelPane.hoverProperty().removeListener(info.itemHoverListener);
            if(info.itemClickHandler != null)
                info.labelPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, info.itemClickHandler);
        }

        for(HBox labelPane : popups.keySet()){
            DropdownMenuPopup popup = popups.get(labelPane);
            popup.dispose();
        }

    }

    public DropdownMenuItem getSelectedItem() {
        return selectedItem.get();
    }

    public ObjectProperty<? extends DropdownMenuItem> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(DropdownMenuItem selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    public DropdownMenuPopup getParent() {
        return parent;
    }

    public void setParent(DropdownMenuPopup parent) {
        this.parent = parent;
    }
}
