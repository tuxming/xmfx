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
package com.xm2013.jfx.control.animate;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

/**
 * 波纹动画效果
 */
public class ClickRipperAnimate implements ClickAnimate{

    private Region control;
    private HueType hueType;
    private ColorType colorType;

    private ResetNodePositionCallback resetPosition;
    private CallBack<Node> addNodeCallBack;
    private CallBack<Node> removeNodeCallBack;

    private Region fill;
    private Circle circle;
    private DoubleProperty x = new SimpleDoubleProperty(0), y = new SimpleDoubleProperty(0);
    private Timeline timeline;

    private Boolean playing = false;

    public ClickRipperAnimate(Region control, ColorType colorType, HueType hueType){
        this.control = control;
        this.hueType = hueType;
        this.colorType = colorType;

        fill = new Region();
//        fill.paddingProperty().bind(control.paddingProperty());
        fill.borderProperty().bind(control.borderProperty());

        Color color = getColor();
        fill.backgroundProperty().bind(Bindings.createObjectBinding(()->{

           try{
               List<BackgroundFill> fillList = control.getBackground().getFills();
               BackgroundFill[] fills = new BackgroundFill[fillList.size()];
               for(int i=0; i<fillList.size(); i++){
                   BackgroundFill fill = fillList.get(i);
                   Paint fillPaint = null;
                   if(HueType.DARK.equals(hueType)){
                       fillPaint = FxKit.getOpacityPaint(color, 0.5);
                   }else{
                       fillPaint = FxKit.getOpacityPaint(FxKit.derivePaint(color, 1), 0.4);
                   }
                   fills[i] = new BackgroundFill(fillPaint, fill.getRadii(), fill.getInsets());
               }
               return new Background(fills);
           }catch (Exception e){
               return new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), Insets.EMPTY));
           }
        }, control.backgroundProperty()));

        circle = new Circle();
//        circle.setFill(getColor());
        circle.centerYProperty().bind(y);
        circle.centerXProperty().bind(x);
        circle.setRadius(0);

        double width = control.prefWidth(-1);

        fill.setClip(circle);

        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),  "kf1",
                        new KeyValue(circle.opacityProperty(),  1),
                        new KeyValue(circle.radiusProperty(), 0)
                ),
                new KeyFrame(Duration.millis(400), "kf2",
                        new KeyValue(circle.opacityProperty(), 0),
                        new KeyValue(circle.radiusProperty(), width*2)
                )
        );

        timeline.setOnFinished(e -> {
            playing = false;
            if(removeNodeCallBack!=null){
                removeNodeCallBack.call(fill);
            }
//            this.skin.getChildren().remove(fill);
        });
    }

    @Override
    public ClickAnimate setPoint(double x, double y) {
        this.x.set(x);
        this.y.set(y);
        return this;
    }

    @Override
    public void setNodePosition(ResetNodePositionCallback callback) {
        this.resetPosition = callback;
    }

    @Override
    public void setAddNode(CallBack<Node> callback) {
        this.addNodeCallBack = callback;
    }

    @Override
    public void setRemoveNode(CallBack<Node> callback) {
        this.removeNodeCallBack = callback;
    }

    @Override
    public void play() {
        if(playing)
            return ;
        playing = true;

//        this.skin.getChildren().add(0, fill);
        if(addNodeCallBack != null){
            addNodeCallBack.call(fill);
        }
        if(resetPosition!=null){
            double w = this.control.prefWidth(-1);
            double h = this.control.prefHeight(-1);
            resetPosition.call(fill, w, h, 0, 0);
        }

        timeline.playFrom("kf1");
    }

    @Override
    public void dispose() {
        fill.prefWidthProperty().unbind();
        fill.prefHeightProperty().unbind();
        fill.paddingProperty().unbind();
        fill.borderProperty().unbind();
//        this.skin.getChildren().remove(fill);
        if(removeNodeCallBack!=null){
            removeNodeCallBack.call(fill);
        }
    }


    private Color getColor(){
        Paint paint = colorType.getPaint();
        Color color = null;
        if(paint instanceof LinearGradient){
            LinearGradient lg = (LinearGradient) paint;
            color = lg.getStops().get(0).getColor();
        }else if(paint instanceof RadialGradient){
            RadialGradient rg = (RadialGradient) paint;
            color = rg.getStops().get(0).getColor();
        }else{
            color = (Color) paint;
        }

        if(HueType.DARK.equals(hueType)){
            return FxKit.getOpacityColor(color, 0.5);
        }else{
            return FxKit.getOpacityColor(FxKit.getLightColor(color, 0.8), 0.8);
        }

    }

}
