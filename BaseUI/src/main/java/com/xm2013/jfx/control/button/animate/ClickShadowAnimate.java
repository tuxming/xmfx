package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.util.Duration;

/**
 * 阴影扩散动画
 */
public class ClickShadowAnimate implements XmButtonClickAnimate{

    private XmButton control;
    private XmButtonSkin skin;

    private Region fill;
    private DropShadow shadow;
    private Timeline timeline;
    private boolean playing = false;

    public ClickShadowAnimate(XmButtonSkin skin){
        this.control = (XmButton) skin.getSkinnable();
        this.skin = skin;

        fill = new Region();
        fill.prefWidthProperty().bind(control.prefWidthProperty());
        fill.prefHeightProperty().bind(control.prefHeightProperty());
        fill.paddingProperty().bind(control.paddingProperty());
        fill.backgroundProperty().bind(control.backgroundProperty());
        fill.borderProperty().bind(control.borderProperty());
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
        KeyValue kv21 = new KeyValue(shadow.radiusProperty(), 10);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.5), "kf2", kv2,kv21);

        timeline = new Timeline();
        timeline.getKeyFrames().addAll(kf1, kf2);
        timeline.setOnFinished(e -> {
            this.skin.getChildren().remove(fill);
            this.playing = false;
        });

        fill.setEffect(shadow);

//        this.skin.getChildren().add(0, fill);
//        this.skin.positionInArea1(fill, 0,0, this.control.prefWidth(-1), this.control.prefHeight(-1));

    }

    @Override
    public void setPoint(double x, double y) {
    }


    private Color getColor(){
        Paint paint = control.getColorType().getPaint();
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

    @Override
    public void play() {

        if(playing) return;
        this.playing = true;

        this.skin.getChildren().add(0, fill);
        this.skin.positionInArea1(fill, 0,0, this.control.prefWidth(-1), this.control.prefHeight(-1));
        timeline.play();
    }

    @Override
    public void dispose() {
        this.fill.prefHeightProperty().unbind();
        this.fill.prefWidthProperty().unbind();
        this.fill.paddingProperty().unbind();
        this.fill.backgroundProperty().unbind();
        this.fill.borderProperty().unbind();

        this.skin.getChildren().remove(this.fill);

    }
}
