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
package com.xm2013.jfx.container;

import com.xm2013.jfx.control.scroll.XmScrollBar;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

// TODO: Auto-generated Javadoc
/**
 * ScrollPane 不知道是我使用的方法不对，还是其他什么原因， 当ScrollPane或者他的Content，或者和ScrollPane的同级节点使用DropShadow的时候，Content里面的文字，会出现模糊的效果
 * 这个SimpleVScrollPane，是借鉴ScrollPane的结构是实现的使用方式
 * 正常情况下使用ScrollBar他的滚动条是以窗口为级别，不好控制
 *
 * 这只是一个简单垂直滚动条容器，向外提供容器的基本结构，其行为，表现的自己控制
 * 结构如下
 *  <pre>Pane</pre>
 *  <pre>   StackPane(viewRect)</pre>
 *  <pre>       pane(viewContent) 这下面放具体的内容,具体的内容需要一个容器包裹</pre>
 *  <pre>   ScrollBar(scrollBar)</pre>
 *
 *  使用方式：
 *  <pre>VBox box = new VBox();</pre>
 *  <pre>for(int i=1; i&lt;=50; i++){</pre>
 *  <pre>    Label label = new Label("label text "+i);</pre>
 *  <pre>    label.setFont(Font.font(16));</pre>
 *  <pre>    box.getChildren().add(label);</pre>
 *  <pre>}</pre>
 *  <pre>SimpleVScrollPane spane = new SimpleVScrollPane();</pre>
 *  <pre>spane.setViewHeight(100);</pre>
 *  <pre>spane.setViewWidth(150);</pre>
 *  <pre>spane.setContent(box);</pre>
 *  <pre>spane.getScrollBar().setColorType(ColorType.success());</pre>
 */
public class SimpleVScrollPane extends Pane {

    /** The view rect. */
    //内容遮罩层
    private StackPane viewRect;
    
    /** 显示区域. */
    private Rectangle viewClip;
    
    /** 内容容器. */
    private Pane viewContent;
    
    /** 滚动条. */
    private XmScrollBar scrollBar;

    /** The view width. */
    private DoubleProperty viewWidth = new SimpleDoubleProperty(0);
    
    /** The view height. */
    private DoubleProperty viewHeight = new SimpleDoubleProperty(0);

    /** The is sroll. */
    private boolean isSroll = true;

    /**
     * Instantiates a new simple V scroll pane.
     */
    public SimpleVScrollPane(){
        this.getStyleClass().add("xm-vscroll-pane");

        viewRect = new StackPane();
        viewRect.getStyleClass().add("xm-view-rect");
//        viewRect.setStyle("-fx-background-color: white;");
//        viewRect.prefWidthProperty().bind(viewWidth);
//        viewRect.prefHeightProperty().bind(viewHeight);
//        viewRect.maxWidthProperty().bind(viewWidth);
//        viewRect.maxHeightProperty().bind(viewHeight);

        viewContent = new Pane();
        viewContent.getStyleClass().add("xm-view-content");
        viewContent.prefWidthProperty().bind(viewWidth);
        viewContent.prefHeightProperty().bind(viewHeight);

        viewClip = new Rectangle();
        viewClip.widthProperty().bind(viewContent.prefWidthProperty());
        viewClip.heightProperty().bind(viewContent.prefHeightProperty());

        scrollBar = new XmScrollBar();
        scrollBar.getStyleClass().add("xm-scroll-bar");
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.prefHeightProperty().bind(viewHeight);
        scrollBar.setUnitIncrement(10);

        viewRect.setClip(viewClip);
        viewRect.getChildren().setAll(viewContent);

        getChildren().addAll(viewRect, scrollBar);

        //添加监听&事件
        scrollBar.valueProperty().addListener((ob, ov, nv) -> {
            Node node = viewContent.getChildren().get(0);
            if(node!=null && isSroll){
                node.setLayoutY(-nv.doubleValue());
            }
        });

        scrollBar.opacityProperty().addListener((ob, ov, nv)->{
            System.out.println(nv);
            if(nv.doubleValue() > 0.1){
                isSroll = true;
            }else{
                isSroll = false;
            }
        });

        //添加viewport滚动监听
        this.setOnScroll(e -> {
            if(isSroll){
                double deltaY = e.getDeltaY();

                double value = scrollBar.getValue()-deltaY;
                if(value<=0){
                    value = 0;
                }

                if(value > scrollBar.getMax()){
                    value = scrollBar.getMax();
                }

                scrollBar.setValue(value);
            }
        });
    }

    /**
     * 设置内容节点
     * @param content Pane
     */
    public void setContent(Pane content){
        if(content != null){
            this.viewContent.getChildren().setAll(content);
            this.scrollBar.maxProperty().bind(content.heightProperty().subtract(viewHeight));
        }
    }

    /**
     * Compute pref width.
     *
     * @param height the height
     * @return the double
     */
    @Override
    protected double computePrefWidth(double height) {
        double width = viewContent.prefWidth(-1);
        return width;
    }

    /**
     * Layout children.
     */
    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        double sh = scrollBar.prefWidth(-1);


        layoutInArea(scrollBar, viewWidth.get() - sh, 0,
                sh, viewHeight.get(),
                0, HPos.CENTER, VPos.CENTER);

    }

    /**
     * Scroll to.
     *
     * @param value the value
     */
    public void scrollTo(double value){
        double startValue = scrollBar.getValue();
        KeyValue kv1 = new KeyValue(scrollBar.valueProperty(), startValue);
        KeyValue kv2 = new KeyValue(scrollBar.valueProperty(), value);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), "kf1", kv1);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), "kf2", kv2);
        Timeline timeline = new Timeline(kf1, kf2);
        timeline.play();
    }

    /* -------------------- getter / setter ------------------------ */


    public StackPane getViewRect() {
        return viewRect;
    }

    public Pane getViewContent() {
        return viewContent;
    }

    public XmScrollBar getScrollBar() {
        return scrollBar;
    }

    public double getViewWidth() {
        return viewWidth.get();
    }

    /**
     * View width property.
     *
     * @return the double property
     */
    public DoubleProperty viewWidthProperty() {
        return viewWidth;
    }

    public void setViewWidth(double viewWidth) {
        this.viewWidth.set(viewWidth);
    }

    public double getViewHeight() {
        return viewHeight.get();
    }

    /**
     * View height property.
     *
     * @return the double property
     */
    public DoubleProperty viewHeightProperty() {
        return viewHeight;
    }

    public void setViewHeight(double viewHeight) {
        this.viewHeight.set(viewHeight);
    }
}
