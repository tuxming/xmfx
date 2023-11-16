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

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ResetNodePositionCallback;
import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class XmTabSkin extends SkinBase {
    //关闭图标path
    private static String closeIcon = "M 0,0 H1 L 4,3 7,0 H8 V1 L 5,4 8,7 V8 H7 L 4,5 1,8 H0 V7 L 3,4 0,1 Z";
    private XmTab control;

    //显示文本
    private Text textNode;
    //关闭图标
    private Region closeNode;

    //圆角大小
    private double radiusWidth = 0;

    //点击动画
    private ClickAnimate clickAnimate;
    //选中时的下划线
    private Line selectedLine;

    //状态外观缓存
    private final Map<Integer, TabSkinInfo> skins = new HashMap<Integer, TabSkinInfo>();

    /**
     * 状态颜色
     */
    class TabSkinInfo{
        public Paint fontColor, backgroundColor;
        public TabSkinInfo(Paint fontColor, Paint backgroundColor){
            this.fontColor = fontColor;
            this.backgroundColor = backgroundColor;
        }
    }

    public XmTabSkin(XmTab control) {
        super(control);
        this.control = control;

        textNode = new Text();
        textNode.textProperty().bind(control.textProperty());
        textNode.getStyleClass().add("tab-text");

        selectedLine = new Line();
        selectedLine.setManaged(false);
        getChildren().addAll(textNode, selectedLine);
        updateGraphic(null, control.getGraphic());
        updateCloseable();
        updateSize();
        updateSkin(control.isSelected()?3:1);
        updateLine();
        showSelectedLine();

        //hover时
        registerChangeListener(control.hoverProperty(), e->{
            updateStatus();
        });

        //选中变化时
        registerChangeListener(control.selectedProperty(), e->{
            updateStatus();
            showSelectedLine(); //显示线条
        });

        //颜色变化时
        registerChangeListener(control.colorTypeProperty(), e->{
            skins.clear();  //清除状态缓存
            updateLine();   //更新选中线的颜色
            updateSkin(control.isSelected()?3:1); //更新显示颜色
        });

        //尺寸规格变化时
        registerChangeListener(control.sizeTypeProperty(), e -> updateSize());
        //圆角规格变化时
        registerChangeListener(control.roundTypeProperty(), e-> updateSize());
        //色调类型变化时
        registerChangeListener(control.hueTypeProperty(), e->{
            skins.clear();  //清除状态缓存
            updateLine();   //更新选中线的颜色
            updateSkin(control.isSelected()?3:1); //更新显示颜色
        });
        //是否显示关闭按钮
        registerChangeListener(control.closeableProperty(), e->{
            updateCloseable();      //更新关闭图标
        });
        //显示图标变化
        control.graphicProperty().addListener((ob, ov, nv)->{
            updateGraphic(ov, nv); //更新显示图标
        });

        //监听点击事件
        control.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            control.setCloseable(true);  //显示图标
            getClickAnimate().setPoint(e.getX(), e.getY()).play();  //播放点击动画
            if(control.getClickAction()!=null){    //执行点击回调函数
                control.getClickAction().call(e);
            }
        });

        //监听鼠标按下，缩放组件
        control.addEventFilter(MouseEvent.MOUSE_PRESSED, e->{
            Node graphic = control.getGraphic();
            if(graphic != null){
                graphic.setScaleX(1);
                graphic.setScaleY(1);
            }
            textNode.setScaleX(1);
            textNode.setScaleY(1);
        });

        //监听鼠标松开，还原组件大小
        control.addEventFilter(MouseEvent.MOUSE_RELEASED, e->{
            Node graphic = control.getGraphic();
            if(graphic != null){
                graphic.setScaleX(1.05);
                graphic.setScaleY(1.05);
            }
            textNode.setScaleX(1.05);
            textNode.setScaleY(1.05);
        });
    }

    /**
     * 播放动画，显示或者隐藏选中时下划线
     */
    private void showSelectedLine() {
        double y = control.getHeight()-3;
        selectedLine.setStartY(y);
        selectedLine.setEndY(y);

        if(control.isSelected()){
            double startX = 0,
                    endX = control.getWidth(),
                    beginX = (endX - startX) / 2;

//            System.out.println(startX+","+endX+", "+beginX);

            selectedLine.setStartX(beginX);
            selectedLine.setEndX(beginX);
            selectedLine.setOpacity(0);
            selectedLine.setVisible(true);

            KeyValue kv11 = new KeyValue(selectedLine.startXProperty(), beginX);
            KeyValue kv12 = new KeyValue(selectedLine.endXProperty(), beginX);
            KeyValue kv13 = new KeyValue(selectedLine.opacityProperty(), 0);

            KeyValue kv21 = new KeyValue(selectedLine.startXProperty(), startX);
            KeyValue kv22 = new KeyValue(selectedLine.endXProperty(), endX);
            KeyValue kv23 = new KeyValue(selectedLine.opacityProperty(), 1);

            selectedLine.setVisible(true);
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(0), "kf1", kv11, kv12, kv13),
                    new KeyFrame(Duration.millis(150), "kf2", kv21, kv22, kv23));
            tl.setOnFinished(e->{
                //1，在界面没显示之前，宽高都为0，所以在动画播放完成以后再次设置位置
                //2，是选中后才显示关闭图标，所以宽度会发生变化，所以在动画完成以后，在设置一次endX
                selectedLine.setEndX(control.getWidth());
                double h = control.getHeight();
                selectedLine.setStartY(h-3);
                selectedLine.setEndY(h-3);
            });
            tl.play();
        }else{

            double startX = selectedLine.getStartX(),
                    endX = selectedLine.getEndX(),
                    finishX = (endX - startX) / 2;

            KeyValue kv11 = new KeyValue(selectedLine.startXProperty(), startX);
            KeyValue kv12 = new KeyValue(selectedLine.endXProperty(), endX);
            KeyValue kv13 = new KeyValue(selectedLine.opacityProperty(), 1);

            KeyValue kv21 = new KeyValue(selectedLine.startXProperty(), finishX);
            KeyValue kv22 = new KeyValue(selectedLine.endXProperty(), finishX);
            KeyValue kv23 = new KeyValue(selectedLine.opacityProperty(), 0);

            selectedLine.setVisible(true);
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(0), "kf1", kv11, kv12, kv13),
                    new KeyFrame(Duration.millis(150), "kf2", kv21, kv22, kv23));
            tl.setOnFinished(e -> {
                selectedLine.setVisible(false);
            });
            tl.play();
        }
    }

    /**
     * 更新选中时下划线的外观
     */
    private void updateLine() {

        Paint paint = control.getColorType().getPaint();
        boolean isDark = HueType.DARK.equals(control.getHueType());

        selectedLine.setStroke(isDark?paint : Color.WHITE);
        selectedLine.setStrokeWidth(1.5);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2);
        shadow.setOffsetX(0);
        shadow.setColor(control.getColorType().getFxColor());
        shadow.setRadius(1);
        selectedLine.setEffect(shadow);
        selectedLine.setVisible(false);
    }

    /**
     * 初始化点击动画
     * @return
     */
    private ClickAnimate getClickAnimate(){
        if(clickAnimate == null){
            clickAnimate = new ClickRipperAnimate(control, control.getColorType(), control.getHueType());
            clickAnimate.setAddNode(node -> getChildren().add(node));
            clickAnimate.setNodePosition(new ResetNodePositionCallback() {
                @Override
                public void call(Node node, double width, double height, double x, double y) {
                    node.setManaged(false);
                    layoutInArea(node, x, y, width, height, -1, HPos.CENTER, VPos.CENTER);
                }
            });
            clickAnimate.setRemoveNode(node -> getChildren().remove(node));
        }
        return clickAnimate;
    }

    /**
     * 根据状态更新组件外观
     */
    private void updateStatus(){
        boolean hover = control.isHover();
        boolean selected = control.isSelected();

        Node graphic = control.getGraphic();

        if(hover){

            if(graphic != null){
                graphic.setScaleX(1.05);
                graphic.setScaleY(1.05);
            }
            textNode.setScaleX(1.05);
            textNode.setScaleY(1.05);

            if(selected){
                updateSkin(3);
            }else{
                updateSkin(2);
            }
        }else{

            if(graphic != null){
                graphic.setScaleX(1);
                graphic.setScaleY(1);
            }
            textNode.setScaleX(1);
            textNode.setScaleY(1);

            if(selected){
                updateSkin(4);
            }else{
                updateSkin(1);
            }
        }
    }

    /**
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover, not selected
     * status: 3 : hover,selected状态下的颜色
     * status: 4 : out hover, selected状态的颜色
     * @param status int
     */
    private void updateSkin(int status) {

        TabSkinInfo info = getSkinInfo(status);

        Paint fontColor = info.fontColor, bgColor = info.backgroundColor;
        control.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(radiusWidth, radiusWidth, 0, 0, false), Insets.EMPTY)));
        textNode.setFill(fontColor);

        Node graphic = control.getGraphic();
        if(graphic!=null && graphic instanceof XmIcon){
            XmIcon icon = (XmIcon) graphic;
            icon.setColor(fontColor);
        }

    }

    /**
     * 更新组件尺寸，圆角大小
     */
    private void updateSize() {
        RoundType roundType = control.getRoundType();
        SizeType sizeType = control.getSizeType();
        double fontSize = 0;
        Insets padding = null;
        if(roundType.equals(RoundType.CIRCLE)){
            if(sizeType.equals(SizeType.SMALL)){
                fontSize = 14d;
                padding = new Insets(2.3d, 7.5d, 2.2d, 7.5d);
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 18d;
                padding = new Insets(13d, 20d, 13d, 20d);
            }else {
                fontSize = 16d;
                padding = new Insets(10d, 17.5d, 10.5d, 17d);
            }
        }else{
            if(sizeType.equals(SizeType.SMALL)){
                fontSize = 14d;
                padding = new Insets(2.3d, 8d, 2.2d, 8d);
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 18d;
                padding = new Insets(13.2d, 30d, 13d, 30d);
            }else {
                fontSize = 16d;
                padding = new Insets(10d, 15d, 10.5d, 15d);
            }
        }

        //计算圆角大小
        if(RoundType.SMALL.equals(roundType)){
            radiusWidth = 4d;
        }else if(RoundType.SEMICIRCLE.equals(roundType)){
            radiusWidth = 50d;
        }else if(RoundType.CIRCLE.equals(roundType)){
//            double raius = Math.max(control.prefHeight(-1), control.prefWidth(-1));
            radiusWidth = 1000d;
        }

        textNode.setFont(Font.font(textNode.getFont().getFamily(), fontSize));

        Node graphic = control.getGraphic();
        if(graphic!=null && graphic instanceof XmIcon){
            XmIcon icon = (XmIcon) graphic;
            icon.setSize(fontSize);
        }

        control.setPadding(padding);
    }


    /**
     * 获取外观样式信息
     * @param status
     * @return
     */
    private TabSkinInfo getSkinInfo(int status){
        TabSkinInfo info = skins.get(status);
        if(info == null){
            info = buildSkinInfo(status);
        }
        return info;
    }

    /**
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover, not selected
     * status: 3 : hover,selected状态下的颜色
     * status: 4 : out hover, selected状态的颜色
     * @param status int
     */
    private TabSkinInfo buildSkinInfo(int status){

        Paint fontColor = null, backgroundColor = null;

        ColorType colorType = control.getColorType();
        HueType hueType = control.getHueType();

        boolean isDark = HueType.DARK.equals(hueType);

        if(status == 1){
            fontColor  = control.getFontColor() == null?Color.WHITE:control.getFontColor();
            if(isDark){
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.05);
            }else{
                backgroundColor = colorType.getPaint();
            }
        }else if(status == 2){
            fontColor  = control.getFontColor() == null?Color.WHITE:control.getFontColor();
            if(isDark){
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.1);
            }else{
                backgroundColor = FxKit.getLightPaint(colorType.getPaint(), 0.3);
            }
        }else if(status == 3){
            if(isDark){
                fontColor = colorType.getPaint();
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.2);
            }else{
                fontColor = Color.WHITE;
                backgroundColor = FxKit.getLightPaint(colorType.getPaint(), 0.35);
            }
        }else{
            if(isDark){
                fontColor = colorType.getPaint();
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.15);
            }else{
                fontColor = Color.WHITE;
                backgroundColor = FxKit.getLightPaint(colorType.getPaint(), 0.3);
            }
        }

        TabSkinInfo tabSkinInfo = new TabSkinInfo(fontColor, backgroundColor);
        skins.put(status,tabSkinInfo);
        return tabSkinInfo;

    }

    /**
     * 更新删除图标，
     */
    private void updateCloseable(){
        ObservableList children = getChildren();
        if(control.isCloseable()){
            if(closeNode == null){
                closeNode = new Region();
                closeNode.getStyleClass().add("tab-close-icon");
                SVGPath path = new SVGPath();
                path.setContent(closeIcon);
                closeNode.setShape(path);
                closeNode.setPrefWidth(11);
                closeNode.setPrefHeight(11);

                closeNode.setBackground(new Background(new BackgroundFill(Color.web(control.getHueType().equals(HueType.LIGHT)?"#ffffff":ColorType.SECONDARY), CornerRadii.EMPTY, Insets.EMPTY)));
                closeNode.hoverProperty().addListener((ob, ov, nv)->{
                   if(nv){
                       closeNode.setBackground(new Background(new BackgroundFill(Color.web(ColorType.DANGER), CornerRadii.EMPTY, Insets.EMPTY)));
                   }else{
                       closeNode.setBackground(new Background(new BackgroundFill(Color.web(control.getHueType().equals(HueType.LIGHT)?"#ffffff":ColorType.SECONDARY), CornerRadii.EMPTY, Insets.EMPTY)));
                   }
                });

                closeNode.setOnMouseClicked(e -> {
                    if(control.getCloseAction()!=null){
                        control.getCloseAction().call(e);
                    }
                    e.consume();
                });
            }

            if(!children.contains(closeNode)){
                children.add(closeNode);
            }

        }else{
            if(closeNode!=null){
                if(children.contains(closeNode)){
                    children.remove(closeNode);
                }
            }
        }
    }

    /**
     * 更新显示的图标
     * @param oldGraphic
     * @param newGraphic
     */
    private void updateGraphic(Node oldGraphic, Node newGraphic){
        ObservableList children = getChildren();
        if(newGraphic != null){
            if(!children.contains(newGraphic)){
                newGraphic.setMouseTransparent(true);
                children.add(newGraphic);
            }
        }else{
            if(oldGraphic != null && children.contains(oldGraphic)){
                children.remove(oldGraphic);
            }
        }
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {

        double height = textNode.prefHeight(-1);
        Insets padding = control.getPadding();
        if(padding!=null){
            height += padding.getTop() + padding.getBottom();
        }

        return height;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {

        double width = textNode.prefWidth(-1);
        if(control.getGraphic()!=null){
            width += control.getGraphic().prefWidth(-1)+8;
        }

        if(control.isCloseable()){
            width += closeNode.prefWidth(-1) + 8;
        }

        Insets padding = control.getPadding();
        if(padding!=null){
            width += padding.getLeft() + padding.getRight();
        }


        return width;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double height = control.prefHeight(-1);
        Insets padding = control.getPadding();
        if(padding == null){
            padding = Insets.EMPTY;
        }

        double startX = padding.getLeft();
        Node graphic = control.getGraphic();
        if(graphic != null){
            double x = startX, w = graphic.prefWidth(-1), h = graphic.prefHeight(-1), y = (height - h) / 2;
            layoutInArea(graphic, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
            startX += w + 8;
        }

        double tX = startX, tW = textNode.prefWidth(-1), tH = textNode.prefHeight(-1), tY = (height - tH) / 2;
        layoutInArea(textNode, tX, tY, tW, tH, -1, HPos.CENTER, VPos.CENTER);
        startX += tW + 8;

        if(control.isCloseable()){
            double cX = startX, cW = closeNode.prefWidth(-1), cH = closeNode.prefHeight(-1), cY = (height - cH) / 2;
            layoutInArea(closeNode, cX, cY, cW, cH, -1, HPos.CENTER, VPos.CENTER);
        }

    }
}
