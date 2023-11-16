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
package com.xm2013.jfx.control.tab;

import com.xm2013.jfx.control.scroll.XmScrollBar;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

public class XmTabPaneSkin extends SkinBase {
    private final TabHeadPane headPane;
    private XmTabPane control;
    private Region currentContent;
    private boolean isUpdatedSelected = false;  //初始化的时候如果存在默认选中的值，设置选中的值

    public XmTabPaneSkin(XmTabPane control) {
        super(control);
        this.control = control;
        headPane = new TabHeadPane(control.getTabs());

        //设置默认选中
        XmTab selected = control.selectedProperty().get();
        if(selected  != null){
            selected.setCloseable(true);
            selected.setSelected(true);
        }
        isUpdatedSelected = true;

        //监听选中
        control.selectedProperty().addListener((ob, ov, nv)->{
            if(ov != null){
                ov.setCloseable(false);
                ov.setSelected(false);

                FadeTransition ft = new FadeTransition();
                ft.setDuration(Duration.millis(150));
                ft.setNode(ov.getContent());
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished(e -> {
                    getChildren().remove(ov.getContent());
                });
                ft.play();
            }

            if(nv != null){
                nv.setSelected(true);
//                headPane.scrollTo(nv);
                currentContent = (Region)nv.getContent() ;
                currentContent.setOpacity(0);
                currentContent.setPrefWidth(control.getWidth());
                currentContent.setPrefHeight(control.getHeight());
//                currentContent.prefWidthProperty().bind(control.prefWidthProperty());
//                currentContent.prefHeightProperty().bind(control.prefHeightProperty().subtract(headPane.prefHeight(-1)));
                getChildren().add(currentContent);
                FadeTransition ft = new FadeTransition();
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.setNode(currentContent);
                ft.setDuration(Duration.millis(500));
                ft.play();
            }
        });

        getChildren().add(headPane);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
//        System.out.println(contentHeight+","+ control.getHeight());

        double hX = 0, hY = 0, hW = control.getTabMaxWidth(), hH = headPane.prefHeight(-1);
        layoutInArea(headPane, hX, hY, hW, hH, -1, HPos.CENTER, VPos.CENTER);

        if(currentContent!=null){
            double cX = 0, cY = hH, cW = contentWidth, cH = contentHeight-hH;
//            System.out.println(cX+", "+cY+", "+cW+", "+cH);
            layoutInArea(currentContent, cX, cY, cW, cH, -1, HPos.CENTER, VPos.CENTER);
        }
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);

        double height = headPane.prefHeight(-1);
        if(currentContent != null){
            height += currentContent.prefHeight(-1);
        }
        return height;
    }

    /**
     * 顶部tab header容器
     */
    class TabHeadPane extends Pane {

        private boolean isUpdatedScrollbar = false;
        private boolean needScroll = false;
        ObservableList<XmTab> tabs;
        TabHeadViewPane viewBox;
        XmScrollBar scrollBar;
        public TabHeadPane(ObservableList<XmTab> tabs){
            this.tabs = tabs;

            //初始一些tab的属性
            for (XmTab tab : tabs) {
                setTabCallback(tab);
            }

            //tab容器
            viewBox = new TabHeadViewPane(tabs);

            //设置tab的区域
            Rectangle viewRect = new Rectangle();
            viewRect.widthProperty().bind(widthProperty());
            viewRect.heightProperty().bind(heightProperty());
            setClip(viewRect);

            //设置滚动条
            scrollBar = new XmScrollBar();
            scrollBar.setOrientation(Orientation.HORIZONTAL);
            scrollBar.prefWidthProperty().bind(prefWidthProperty());
            scrollBar.setOpacity(0);
//            scrollBar.visibleAmountProperty().bind();
            getChildren().addAll(viewBox, scrollBar);

            prefWidthProperty().bind(control.tabMaxWidthProperty());

            //鼠标悬停时，才显示滚动条
            hoverProperty().addListener((ob, ov, nv)->{
                if(nv){
                    scrollBar.setOpacity(0.6);
                }else{
                    scrollBar.setOpacity(0);
                }
            });

            addEventFilter(ScrollEvent.SCROLL, e -> {
                e.consume();
                scrollHandler(e);
            });


            //监听鼠标滚动
            viewBox.addEventFilter(ScrollEvent.SCROLL, e->{
                e.consume();
                scrollHandler(e);
            });

            //监听滚动条的变化
            scrollBar.valueProperty().addListener((ob, ov, nv)->{
                double val = nv.doubleValue();
                if((val<0 || val > scrollBar.getMax()) && needScroll){
                    viewBox.setLayoutX(-scrollBar.getValue());
                    needScroll = false;
                    return;
                }

                viewBox.setLayoutX(-scrollBar.getValue());
            });

            //监听viewBox宽度，当viewBox的宽度小于容器宽度时，隐藏滚动条
            viewBox.widthProperty().addListener((ob, ov, nv)->{
                if(nv.doubleValue()<getWidth()){
                    scrollBar.setVisible(false);
                }else{
                    scrollBar.setVisible(true);
                }
            });

            //tabs数量发生变化时，改变viewBox中的tab
            tabs.addListener((ListChangeListener<XmTab>) c -> {
                isUpdatedScrollbar = false;
                while (c.next()) {
                    if(c.wasAdded()){
                        List<? extends XmTab> addedSubList = c.getAddedSubList();
                        for (XmTab xmTab : addedSubList) {
                            setTabCallback(xmTab);
                        }
                        viewBox.getChildren().addAll(addedSubList);
                    }
                    if(c.wasRemoved()){
                        viewBox.getChildren().removeAll(c.getRemoved());
                    }
                }
            });

            getStyleClass().add("xm-tab-header");
        }

        /**
         * 处理滚动事件
         * @param e
         */
        private void scrollHandler(ScrollEvent e){
            if(scrollBar.isVisible()){
                double value = scrollBar.getValue();
                if(value<0 && e.getDeltaY()>0){  //deltaY小于0，表示还在向左滚动
                    return;
                }

                if(value>scrollBar.getMax() && e.getDeltaY()<0){   //deltaY大于0，表示还在向右滚动
                    return;
                }

                double nv = scrollBar.getValue()-e.getDeltaY()*3;
                if(nv<0){
                    nv = -10;
                    needScroll = true;
                }

                if(nv > scrollBar.getMax()){
                    nv = scrollBar.getMax();
                    needScroll = true;
                }

                scrollBar.setValue(nv);
            }
        }

        private void setTabCallback(XmTab tab){
            //设置关闭回调函数
            tab.setCloseAction(e -> {

                //选出tab
                int index = tabs.indexOf(tab);
                tabs.remove(tab);
                isUpdatedScrollbar = false;

                //获取下一个选中的tab
                XmTab nextTab = null;

                try {
                    nextTab = tabs.get(index);
                }catch (Exception e1){

                }

                if(nextTab == null){
                    index -= 1;
                    if(index >= 0){
                        nextTab = tabs.get(index);
                    }
                }

                //设置选中tab
                if(nextTab != null){
                    nextTab.setCloseable(true);
                    control.selectedProperty().set(nextTab);
                }
            });

            //设置点击回调事件， 点击后选中点击的tab
            tab.setClickAction(e -> {
                tab.setCloseable(true);
                control.selectedProperty().set(tab);
                scrollTo(tab);
            });

        }

        /**
         * 更新滚动条的一些属性，因为tabs发生变化，或者容器在未显示之前，以下这些属性设置可能不准确
         */
        private void updateScrollBarProperty(){

            if(tabs.size() == 0){
                return;
            }

//            System.out.println("aa:"+viewBox.getChildren().get(viewBox.getChildren().size()-1).prefWidth(-1));
            //这里直接算出的宽度不对，如果再加上最后一个的宽度就刚好
            double width = viewBox.prefWidth(-1)+viewBox.getChildren().get(viewBox.getChildren().size()-1).prefWidth(-1);
            width -= this.prefWidth(-1);
//            System.out.println("-----"+width);
            scrollBar.setMax(width);
            double value = width / tabs.size();
            scrollBar.setVisibleAmount(value);
            scrollBar.setUnitIncrement(value);
            scrollBar.setBlockIncrement(value);
        }

        @Override
        protected double computePrefHeight(double width) {

            if(tabs.size()==0){
                return 45;
            }

            XmTab tab = tabs.get(0);

            return tab.prefHeight(-1);

        }

        @Override
        protected void layoutChildren() {

            double height = getHeight();

            layoutInArea(viewBox, viewBox.getLayoutX(), 0, viewBox.prefWidth(-1), height, -1, HPos.CENTER, VPos.CENTER);
            double sh = scrollBar.prefHeight(-1);

            if(scrollBar.isVisible()){
                if(!isUpdatedScrollbar){
                    updateScrollBarProperty();
                    isUpdatedScrollbar = true;
                }
                layoutInArea(scrollBar, 0, height - sh, scrollBar.prefWidth(-1), sh, -1, HPos.CENTER, VPos.CENTER);
            }

        }

        public void scrollTo(XmTab tab){
            if(scrollBar.isVisible()) {
                double width =  getWidth();
                Bounds bounds = tab.getBoundsInParent();
                if(bounds.getMaxX() - width > scrollBar.getValue()){
                    scrollBar.setValue(bounds.getMaxX() - width);
                }else if(bounds.getMinX()<scrollBar.getValue()){
//                    System.out.println("a");
                    scrollBar.setValue(bounds.getMinX());
                }

            }

        }
    }

    /**
     * 顶部tab控件容器，单独写一个容器，用于方便获取每个tab在容器中的位置。
     */
    class TabHeadViewPane extends Pane{
        ObservableList<XmTab> tabs;
        public TabHeadViewPane(ObservableList<XmTab> tabs){
            this.tabs = tabs;
            getChildren().addAll(tabs);
        }

        @Override
        protected void layoutChildren() {
            double x =  0;
            for (XmTab tab : tabs) {
                double w = tab.prefWidth(-1), h = tab.prefHeight(-1);
                layoutInArea(tab, x, 0, w, h, -1, HPos.CENTER, VPos.CENTER);
                x += w;
            }

            if(tabs.size()>0){
                XmTab tab = tabs.get(tabs.size() - 1);
                if(tab.isSelected()){
                    headPane.scrollTo(tab);
                }
            }
        }
    }

}
