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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.util.Duration;

public class ClickShadowAnimate implements ClickAnimate{

    private final ColorType colorType;
    private Region control;

    private ResetNodePositionCallback resetPosition;
    private CallBack<Node> addNodeCallBack;
    private CallBack<Node> removeNodeCallBack;

    private Region fill;
    private DropShadow shadow;
    private Timeline timeline;
    private boolean playing = false;

    public ClickShadowAnimate(Region control, ColorType colorType){
        this.control = control;
        this.colorType = colorType;

        fill = new Region();
        fill.prefWidthProperty().bind(control.prefWidthProperty());
        fill.prefHeightProperty().bind(control.prefHeightProperty());
        fill.paddingProperty().bind(control.paddingProperty());
        fill.backgroundProperty().bind(control.backgroundProperty());
//        fill.borderProperty().bind(control.borderProperty());
//        fill.borderProperty().bind(Bindings.createObjectBinding(()->{
//
//            List<BorderStroke> strokes = control.getBorder().getStrokes();
//            BorderStroke[] ns = new BorderStroke[strokes.size()];
//            for(int i=0; i<strokes.size(); i++){
//                BorderStroke s = strokes.get(i);
//                ns[i] = new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.DOTTED, s.getRadii(), s.getWidths());
//            }
//
//            return new Border(ns);
//        }, control.borderProperty()));

        Color color = getColor();

        shadow = new DropShadow();
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setColor(color);
        shadow.setWidth(30);
        shadow.setHeight(30);
        shadow.setRadius(0);
        shadow.setSpread(1);

        KeyValue kv1 = new KeyValue(shadow.colorProperty(), color);
        KeyValue kv11 = new KeyValue(shadow.radiusProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), "kf1", kv1, kv11);

        KeyValue kv2 = new KeyValue(shadow.colorProperty(), FxKit.getOpacityColor(color, 0));
        KeyValue kv21 = new KeyValue(shadow.radiusProperty(), 15);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.5), "kf2", kv2,kv21);

        timeline = new Timeline();
        timeline.getKeyFrames().addAll(kf1, kf2);
        timeline.setOnFinished(e -> {
            if(this.removeNodeCallBack != null){
                this.removeNodeCallBack.call(fill);
            }
//            this.skin.getChildren().remove(fill);
            this.playing = false;
        });

        fill.setEffect(shadow);

    }

    @Override
    public ClickAnimate setPoint(double x, double y) {
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
        if(playing) return;
        this.playing = true;

        if(this.addNodeCallBack != null){
            this.addNodeCallBack.call(fill);
        }
//        this.skin.getChildren().add(0, fill);

        if(resetPosition != null){
            resetPosition.call(fill, this.control.prefWidth(-1), this.control.prefHeight(-1), 0 , 0);
        }

        timeline.play();
    }

    @Override
    public void dispose() {

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
        return FxKit.getLightColor(color, 0.7);
    }

}
