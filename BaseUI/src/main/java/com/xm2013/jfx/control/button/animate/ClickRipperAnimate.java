package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

/**
 * 波纹点击动画
 */
public class ClickRipperAnimate implements XmButtonClickAnimate {
    private XmButtonSkin skin;
    private XmButton control;

    private Region fill;
    private Circle circle;
    private DoubleProperty x = new SimpleDoubleProperty(0), y = new SimpleDoubleProperty(0);
    private Timeline timeline;

    private Boolean playing = false;

    public ClickRipperAnimate(XmButtonSkin skin, XmButton control){
        this.skin = skin;
        this.control = control;

        fill = new Region();
//        fill.prefWidthProperty().bind(control.prefWidthProperty());
//        fill.prefHeightProperty().bind(control.prefHeightProperty());
        fill.paddingProperty().bind(control.paddingProperty());
        fill.borderProperty().bind(control.borderProperty());

        Color color = getColor();
        fill.backgroundProperty().bind(Bindings.createObjectBinding(()->{

            List<BackgroundFill> fillList = control.getBackground().getFills();
            BackgroundFill[] fills = new BackgroundFill[fillList.size()];
            for(int i=0; i<fillList.size(); i++){
                BackgroundFill fill = fillList.get(i);
                fills[i] = new BackgroundFill(color, fill.getRadii(), fill.getInsets());
            }
            return new Background(fills);
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
            this.skin.getChildren().remove(fill);
        });


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

        if(HueType.DARK.equals(control.getHueType())){
            return FxKit.getOpacityColor(color, 0.5);
        }else{
            return FxKit.getOpacityColor(FxKit.getLightColor(color, 0.8), 0.8);
        }

    }

    @Override
    public void setPoint(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    @Override
    public void play() {
        if(playing)
            return ;
        playing = true;

        this.skin.getChildren().add(0, fill);
        this.skin.positionInArea1(fill, 0,0, control.prefWidth(-1), control.prefHeight(-1));

        timeline.playFrom("kf1");
    }

    @Override
    public void dispose() {
        fill.prefWidthProperty().unbind();
        fill.prefHeightProperty().unbind();
        fill.paddingProperty().unbind();
        fill.borderProperty().unbind();
        this.skin.getChildren().remove(fill);
    }
}
