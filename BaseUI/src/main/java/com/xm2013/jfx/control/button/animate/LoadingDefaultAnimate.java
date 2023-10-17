package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

/**
 * 默认加载动画
 */
public class LoadingDefaultAnimate implements XmButtonLoadingAnimate {

    private final Node icon;
    private XmButtonSkin skin;
    private XmButton control;
    private Text text;

    private HBox pane = null;
    private Text loadingText = null;
    private Timeline timeline;
    private int index = 0;
    private int times = 0;

    public LoadingDefaultAnimate(XmButtonSkin skin, Text text, Node icon){
        this.skin = skin;
        this.text = text;
        this.icon = icon;
        this.control = (XmButton) skin.getSkinnable();

        pane  = new HBox();
        pane.setVisible(false);
        pane.setAlignment(Pos.CENTER);
        pane.backgroundProperty().bind(control.backgroundProperty());
        pane.borderProperty().bind(Bindings.createObjectBinding(()->{

            Border br = control.getBorder();
            if(br == null) return null;

            List<BorderStroke> bs = br.getStrokes();
            BorderStroke[] bs1 = new BorderStroke[bs.size()];
            for(int i=0; i<bs.size(); i++){
                BorderStroke b = bs.get(i);
                bs1[i] = new BorderStroke(b.getLeftStroke(), b.getBottomStyle(), b.getRadii(), new BorderWidths(0));
            }
            return new Border(bs1);
        }, control.borderProperty()));
        pane.paddingProperty().bind(control.paddingProperty());
//        pane.prefWidthProperty().bind(control.prefWidthProperty());
//        pane.prefHeightProperty().bind(control.prefHeightProperty());
        double left = 0, top = 0, right = 0, bottom = 0;
        Border border = control.getBorder();
        if(border != null){
            BorderStroke bs = border.getStrokes().get(border.getStrokes().size()-1);
            left = bs.getWidths().getLeft();
            top = bs.getWidths().getTop();
            right = bs.getWidths().getRight();
            bottom = bs.getWidths().getBottom();
        }

//        double width = control.prefWidth(-1), height = control.prefHeight(-1);

        loadingText = new Text("");
        loadingText.fillProperty().bind(control.textFillProperty());
        loadingText.fontProperty().bind(control.fontProperty());
        loadingText.underlineProperty().bind(control.underlineProperty());

        pane.getChildren().add(loadingText);
        skin.getChildren().add(pane);
        skin.positionInArea1(pane, left, top,
                control.prefWidth(-1)-bottom*2,
                control.prefHeight(-1)-right*2);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> {
            String dot = "";
            int len = (index % 4);
            if(len == 0) dot = "";
            else if(len == 1) dot = ".";
            else if(len == 2) dot = "..";
            else if(len == 3) dot = "...";

            double percent = control.getLoadingPercent();

            if(percent>1){
                percent = 1;
            }

            String label = "";
            if(control.isShowLoadingPercent()){
                label = String.format("%.2f%%%s", percent * 100, dot);
            }else{
                label = String.format("加载中%s", dot);
            }

            loadingText.setText(label);
            index++;

            if(percent >= 1){
                if(times >= 3){
                    timeline.stop();
                    setVisible(false);
                }else{
                    times++;
                }
            }
        });

        // 添加关键帧到 Timeline 中并启动动画
        timeline.getKeyFrames().add(keyFrame);

        control.loadingProperty().addListener(loadingListener);
    }

    private ChangeListener<Boolean> loadingListener = (ob, ov, nv) -> {
        if(nv!=null && !nv){
            timeline.stop();
            setVisible(false);
        }
    };

    @Override
    public void showLoading() {
        times = 0;
        index = 0;
        setVisible(true);
        timeline.play();
    }

    private void setVisible(boolean v){

        if(text != null) text.setVisible(!v);
        if(icon != null) icon.setVisible(!v);
        pane.setVisible(v);

    }

    @Override
    public void dispose() {

        pane.backgroundProperty().unbind();
        pane.prefHeightProperty().unbind();
        pane.prefWidthProperty().unbind();
        pane.borderProperty().unbind();

        loadingText.underlineProperty().unbind();
        loadingText.fillProperty().unbind();
        loadingText.fontProperty().unbind();

        control.loadingProperty().removeListener(loadingListener);

    }
}
