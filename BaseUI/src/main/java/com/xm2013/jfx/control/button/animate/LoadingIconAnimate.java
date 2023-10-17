package com.xm2013.jfx.control.button.animate;

import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.button.XmButtonSkin;
import com.xm2013.jfx.control.button.XmButtonType;
import com.xm2013.jfx.control.icon.XmIcon;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 图标加载动画
 */
public class LoadingIconAnimate implements XmButtonLoadingAnimate{
    private static String loadingSvg = "M1023.849566 529.032144C1022.533495 457.744999 1007.544916 386.64064 979.907438 321.641387 952.343075 256.605575 912.349158 197.674868 863.252422 148.980264 814.192243 100.249102 755.992686 61.717486 693.004095 36.310016 630.052062 10.792874 562.347552-1.380777 495.483865 0.081523 428.620178 1.470709 362.012394 16.495846 301.144139 44.206439 240.202769 71.807359 185.000928 111.874391 139.377154 161.044242 93.753381 210.177537 57.707676 268.450209 33.945294 331.475357 10.073239 394.463948-1.296147 462.1319 0.166154 529.032144 1.482224 595.968946 15.593423 662.503615 41.549256 723.371871 67.468531 784.240126 105.013094 839.405409 151.075558 884.956067 197.101464 930.579841 251.645269 966.552431 310.612534 990.241698 369.543241 1014.040637 432.860849 1025.336908 495.483865 1023.874608 558.143438 1022.485422 620.291206 1008.337666 677.174693 982.381833 734.094737 956.462558 785.677384 918.954552 828.230327 872.892089 870.819826 826.902741 904.416179 772.395492 926.533473 713.5379 939.986637 677.85777 949.089457 640.605667 953.915048 602.841758 955.194561 602.951431 956.510631 602.987988 957.790144 602.987988 994.27454 602.987988 1023.849566 572.425909 1023.849566 534.735116 1023.849566 532.834125 1023.739893 530.933135 1023.593663 529.032144L1023.849566 529.032144 1023.849566 529.032144ZM918.892953 710.284282C894.691881 767.021538 859.596671 818.421398 816.568481 860.82811 773.540291 903.307938 722.652236 936.75806 667.706298 958.729124 612.760359 980.773303 553.902767 991.192193 495.483865 989.729893 437.064963 988.377265 379.304096 975.106889 326.441936 950.832702 273.543218 926.668187 225.616322 891.682649 186.097653 848.764132 146.542426 805.91873 115.35887 755.176905 94.959779 700.486869 74.451015 645.796833 64.799833 587.195144 66.189018 529.032144 67.541646 470.869145 79.934642 413.437296 102.563741 360.867595 125.119725 308.297895 157.765582 260.663459 197.759499 221.364135 237.716858 182.064811 284.985719 151.137157 335.910331 130.884296 386.834944 110.55832 441.305634 101.01681 495.483865 102.47911 549.662096 103.868296 603.036061 116.261292 651.876895 138.780718 700.754287 161.22703 745.025432 193.690099 781.509828 233.428113 818.067339 273.166127 846.764984 320.142529 865.518987 370.665008 884.346105 421.224045 893.156465 475.256046 891.76728 529.032144L891.986625 529.032144C891.840395 530.933135 891.76728 532.797568 891.76728 534.735116 891.76728 569.939999 917.540325 598.893547 950.66143 602.585856 944.227308 639.728286 933.589072 675.956779 918.892953 710.284282Z";

    private XmButtonSkin skin;
    private XmButton control;
    private Text text;
    private Node icon;

    private Pane pane;
    private XmSVGIcon loadingIcon = null;
    private Text loadingText;
    ParallelTransition animate;

    public LoadingIconAnimate(XmButtonSkin skin, Text text, Node icon){
        this.skin = skin;
        this.control = (XmButton) skin.getSkinnable();
        this.text = text;
        this.icon = icon;

        XmButtonType.BtnDisplayType type = control.getDisplayType();

        boolean isV = ContentDisplay.TOP.equals(control.getContentDisplay()) || ContentDisplay.BOTTOM.equals(control.getContentDisplay());

        if(isV){
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.setFillWidth(false);
            pane = box;
        }else{
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER);
            box.setFillHeight(false);
            pane = box;
        }

        loadingIcon = new XmSVGIcon(loadingSvg);
        loadingIcon.colorProperty().bind(control.textFillProperty());
        loadingIcon.sizeProperty().bind(Bindings.createObjectBinding(()->{
            if(icon != null && icon instanceof XmIcon){
                return ((XmIcon)icon).getSize();
            }
            return control.getFont().getSize()+2;
        }, control.fontProperty()));

        pane.getChildren().add(loadingIcon);

        loadingText = new Text();
        boolean isFull = XmButtonType.BtnDisplayType.FULL.equals(type) || type == null;

        if(isFull){
            loadingText.setText(control.getContent());
            loadingText.fillProperty().bind(control.textFillProperty());
            loadingText.fontProperty().bind(control.fontProperty());
            if(ContentDisplay.RIGHT.equals(control.getContentDisplay()) || ContentDisplay.BOTTOM.equals(control.getContentDisplay())){
                pane.getChildren().add(0, loadingText);
            }else{
                pane.getChildren().add(loadingText);
            }
        }

        pane.backgroundProperty().bind(control.backgroundProperty());
        pane.borderProperty().bind(control.borderProperty());

        skin.getChildren().add(pane);
        skin.positionInArea1(pane, 0, 0, control.prefWidth(-1), control.prefHeight(-1));

        animate = new ParallelTransition();

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(loadingIcon);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setDuration(Duration.millis(1500));

        animate.getChildren().add(rotateTransition);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        if(control.isShowLoadingPercent() && isFull){
            KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> {

                double percent = control.getLoadingPercent();

                if(percent>1){
                    percent = 1;
                }

                String label = "";
                if(control.isShowLoadingPercent()){
                    label = String.format("%.2f%%", percent * 100);
                }

                loadingText.setText(label);
            });

            // 添加关键帧到 Timeline 中并启动动画
            timeline.getKeyFrames().add(keyFrame);

            animate.getChildren().add(timeline);
        }

        animate.setCycleCount(Timeline.INDEFINITE);

        control.loadingProperty().addListener(loadingListener);

    }

    private ChangeListener<Boolean> loadingListener = (ob, ov, nv) -> {
        if(!nv){
            animate.stop();
            setVisible(false);
        }
    };

    private void setVisible(boolean v){

        if(text != null) text.setVisible(!v);
        if(icon != null) icon.setVisible(!v);
        pane.setVisible(v);

    }

    @Override
    public void showLoading() {
        setVisible(true);
        animate.play();
    }

    @Override
    public void dispose() {
        skin.getChildren().remove(pane);

        pane.backgroundProperty().unbind();
        pane.borderProperty().unbind();

        loadingIcon.colorProperty().unbind();
        loadingIcon.sizeProperty().unbind();

        control.loadingProperty().removeListener(loadingListener);

        if(loadingText != null){
            loadingText.fontProperty().unbind();
            loadingText.fontProperty().unbind();
        }
    }
}
