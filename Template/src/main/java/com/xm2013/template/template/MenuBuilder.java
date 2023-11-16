package com.xm2013.template.template;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.XmMenu;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ResetNodePositionCallback;
import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.button.XmButton;
import com.xm2013.jfx.control.icon.XmIcon;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import com.xm2013.jfx.control.label.XmLabel;
import com.xm2013.jfx.control.treeview.XmMenuTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeView;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {

    private static String USER_ICON = "M512 74.666667C270.933333 74.666667 74.666667 270.933333 74.666667 512S270.933333 949.333333 512 949.333333 949.333333 753.066667 949.333333 512 753.066667 74.666667 512 74.666667zM288 810.666667c0-123.733333 100.266667-224 224-224S736 686.933333 736 810.666667c-61.866667 46.933333-140.8 74.666667-224 74.666666s-162.133333-27.733333-224-74.666666z m128-384c0-53.333333 42.666667-96 96-96s96 42.666667 96 96-42.666667 96-96 96-96-42.666667-96-96z m377.6 328.533333c-19.2-96-85.333333-174.933333-174.933333-211.2 32-29.866667 51.2-70.4 51.2-117.333333 0-87.466667-72.533333-160-160-160s-160 72.533333-160 160c0 46.933333 19.2 87.466667 51.2 117.333333-89.6 36.266667-155.733333 115.2-174.933334 211.2-55.466667-66.133333-91.733333-149.333333-91.733333-243.2 0-204.8 168.533333-373.333333 373.333333-373.333333S885.333333 307.2 885.333333 512c0 93.866667-34.133333 177.066667-91.733333 243.2z";
    private static String SYSTEM_ICON = "<svg><path d=\"M904.533333 422.4l-85.333333-14.933333-17.066667-38.4 49.066667-70.4c14.933333-21.333333 12.8-49.066667-6.4-68.266667l-53.333333-53.333333c-19.2-19.2-46.933333-21.333333-68.266667-6.4l-70.4 49.066666-38.4-17.066666-14.933333-85.333334c-2.133333-23.466667-23.466667-42.666667-49.066667-42.666666h-74.666667c-25.6 0-46.933333 19.2-53.333333 44.8l-14.933333 85.333333-38.4 17.066667L296.533333 170.666667c-21.333333-14.933333-49.066667-12.8-68.266666 6.4l-53.333334 53.333333c-19.2 19.2-21.333333 46.933333-6.4 68.266667l49.066667 70.4-17.066667 38.4-85.333333 14.933333c-21.333333 4.266667-40.533333 25.6-40.533333 51.2v74.666667c0 25.6 19.2 46.933333 44.8 53.333333l85.333333 14.933333 17.066667 38.4L170.666667 727.466667c-14.933333 21.333333-12.8 49.066667 6.4 68.266666l53.333333 53.333334c19.2 19.2 46.933333 21.333333 68.266667 6.4l70.4-49.066667 38.4 17.066667 14.933333 85.333333c4.266667 25.6 25.6 44.8 53.333333 44.8h74.666667c25.6 0 46.933333-19.2 53.333333-44.8l14.933334-85.333333 38.4-17.066667 70.4 49.066667c21.333333 14.933333 49.066667 12.8 68.266666-6.4l53.333334-53.333334c19.2-19.2 21.333333-46.933333 6.4-68.266666l-49.066667-70.4 17.066667-38.4 85.333333-14.933334c25.6-4.266667 44.8-25.6 44.8-53.333333v-74.666667c-4.266667-27.733333-23.466667-49.066667-49.066667-53.333333z m-19.2 117.333333l-93.866666 17.066667c-10.666667 2.133333-19.2 8.533333-23.466667 19.2l-29.866667 70.4c-4.266667 10.666667-2.133333 21.333333 4.266667 29.866667l53.333333 76.8-40.533333 40.533333-76.8-53.333333c-8.533333-6.4-21.333333-8.533333-29.866667-4.266667L576 768c-10.666667 4.266667-17.066667 12.8-19.2 23.466667l-17.066667 93.866666h-57.6l-17.066666-93.866666c-2.133333-10.666667-8.533333-19.2-19.2-23.466667l-70.4-29.866667c-10.666667-4.266667-21.333333-2.133333-29.866667 4.266667l-76.8 53.333333-40.533333-40.533333 53.333333-76.8c6.4-8.533333 8.533333-21.333333 4.266667-29.866667L256 576c-4.266667-10.666667-12.8-17.066667-23.466667-19.2l-93.866666-17.066667v-57.6l93.866666-17.066666c10.666667-2.133333 19.2-8.533333 23.466667-19.2l29.866667-70.4c4.266667-10.666667 2.133333-21.333333-4.266667-29.866667l-53.333333-76.8 40.533333-40.533333 76.8 53.333333c8.533333 6.4 21.333333 8.533333 29.866667 4.266667L448 256c10.666667-4.266667 17.066667-12.8 19.2-23.466667l17.066667-93.866666h57.6l17.066666 93.866666c2.133333 10.666667 8.533333 19.2 19.2 23.466667l70.4 29.866667c10.666667 4.266667 21.333333 2.133333 29.866667-4.266667l76.8-53.333333 40.533333 40.533333-53.333333 76.8c-6.4 8.533333-8.533333 21.333333-4.266667 29.866667L768 448c4.266667 10.666667 12.8 17.066667 23.466667 19.2l93.866666 17.066667v55.466666z\" fill=\"#666666\" p-id=\"4982\"></path><path d=\"M512 394.666667c-64 0-117.333333 53.333333-117.333333 117.333333s53.333333 117.333333 117.333333 117.333333 117.333333-53.333333 117.333333-117.333333-53.333333-117.333333-117.333333-117.333333z m0 170.666666c-29.866667 0-53.333333-23.466667-53.333333-53.333333s23.466667-53.333333 53.333333-53.333333 53.333333 23.466667 53.333333 53.333333-23.466667 53.333333-53.333333 53.333333z\" fill=\"#666666\" p-id=\"4983\"></path></svg>";

    private static String AUTH_ICON = "<svg><path d=\"M511.5 119.3l366.8 168.8c-0.4 14.7-1.6 35-4.5 60.2-5.9 52.4-20.6 137.5-56.7 245.4-23 68.8-86.3 145.9-183.1 222.9-48.8 38.9-95.1 68.8-122.2 85.3-26.6-16.9-71.8-47.1-119.7-86.2-95.6-78-159-155.6-183.5-224.5-38.5-108.6-53.1-192.1-58.5-243.1-2.6-24.8-3.4-44.5-3.5-58.7l364.9-170.1m-0.2-55.1L98 257s-16 126.8 63.6 351c67.6 190.3 349.8 352.3 349.8 352.3s289.2-159 353.2-350.6C938.3 388.8 928 256 928 256L511.3 64.2z\" fill=\"#999999\" p-id=\"6144\"></path><path d=\"M511.3 273.3c25.4 0 46.1 20.7 46.1 46.1s-20.7 46.1-46.1 46.1c-25.4 0-46.1-20.7-46.1-46.1s20.7-46.1 46.1-46.1m0-50c-53.1 0-96.1 43-96.1 96.1s43 96.1 96.1 96.1 96.1-43 96.1-96.1-43-96.1-96.1-96.1z\" fill=\"#999999\" p-id=\"6145\"></path><path d=\"M486.3 383.8h50v384h-50z\" fill=\"#999999\" p-id=\"6146\"></path><path d=\"M513 599h94.5v50H513zM513 717h94.5v50H513z\" fill=\"#999999\" p-id=\"6147\"></path></svg>";

    private static String LOG_ICON = "<svg><path d=\"M448.93 890.09H174.61a42.51 42.51 0 0 1-42.46-42.46V154.18a42.51 42.51 0 0 1 42.46-42.46h551.28a42.51 42.51 0 0 1 42.46 42.46v307.61H712V168.1H188.53v665.62h260.4z\" p-id=\"10503\"></path><path d=\"M253.01 241.57h410.51v56.38H253.01zM253.01 372.82h410.51v56.38H253.01zM706.86 915.29c-107.54 0-195-87.49-195-195s87.49-195 195-195 195 87.49 195 195-87.46 195-195 195z m0-333.69c-76.46 0-138.66 62.2-138.66 138.66s62.2 138.66 138.66 138.66 138.66-62.2 138.66-138.66S783.31 581.6 706.86 581.6z\" p-id=\"10504\"></path><path d=\"M785.26 742.45h-96.55V617.36h28.19v96.9h68.36v28.19z\" p-id=\"10505\"></path></svg>";

    private static String FILE_ICON = "<svg><path d=\"M832 64H296c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h496v688c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V96c0-17.7-14.3-32-32-32z\" p-id=\"11471\"></path><path d=\"M704 192H192c-17.7 0-32 14.3-32 32v530.7c0 8.5 3.4 16.6 9.4 22.6l173.3 173.3c2.2 2.2 4.7 4 7.4 5.5v1.9h4.2c3.5 1.3 7.2 2 11 2H704c17.7 0 32-14.3 32-32V224c0-17.7-14.3-32-32-32zM350 856.2L263.9 770H350v86.2zM664 888H414V746c0-22.1-17.9-40-40-40H232V264h432v624z\" p-id=\"11472\"></path></svg>";

    public static XmTreeView<XmMenu> createMenu(BooleanProperty expand){
        TreeItem<XmMenu> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        rootItem.getChildren().addAll(
                new TreeItem<>(new OneMenu(new XmSVGIcon(USER_ICON, Color.BLACK, 14), "个人中心"))
                ,new TreeItem<>(new OneMenu(new XmSVGIcon(SYSTEM_ICON, Color.BLACK, 14), "系统设置"))
                ,new TreeItem<>(new OneMenu(new XmSVGIcon(AUTH_ICON, Color.BLACK, 14), "系统权限"))
                ,new TreeItem<>(new OneMenu(new XmSVGIcon(LOG_ICON, Color.BLACK, 14), "日志管理"))
                ,new TreeItem<>(new OneMenu(new XmSVGIcon(FILE_ICON, Color.BLACK, 14), "文件管理"))
        );

        XmTreeView<XmMenu> menuView = new XmTreeView<>();
        menuView.setShowRoot(false);
        menuView.setHueType(HueType.DARK);
        menuView.setRoot(rootItem);
        setThemeOneMenuCell(menuView, expand);


        return menuView;
    }

    public static void setThemeOneMenuCell(XmTreeView<XmMenu> menuView, BooleanProperty expand){
        menuView.setCellFactory(new Callback<TreeView<XmMenu>, TreeCell<XmMenu>>() {
            @Override
            public TreeCell<XmMenu> call(TreeView<XmMenu> param) {
                return new XmMenuTreeCell<>(){
                    PseudoClass selected = PseudoClass.getPseudoClass("selected");
                    private double x=0, y=0;
                    @Override
                    public void init() {
                        super.init();

                        expand.addListener((ob, ov, nv)->{
                            XmIcon icon = getItem()!=null ? getItem().getIcon() : null;
                            if(nv){
                                setContentDisplay(ContentDisplay.LEFT);
                                if(icon!=null){
                                    icon.setTranslateY(0);
                                    icon.setTranslateX(0);
                                }
                            }else{
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                                if(icon!=null){
                                    icon.setTranslateY(9);
                                    icon.setTranslateX(2);
                                }
                            }
                        });

                        setStyle("-fx-background-color:transparent; ");
                        setTextFill(Color.web("#333333"));
                        setUpdateDefaultSkin(new CallBack<XmMenuTreeCell<XmMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<XmMenu> xmMenuXmMenuTreeCell) {
                                setStyle("-fx-background-color:transparent;");
                            }
                        });

                        setUpdateSelectedSkin(new CallBack<XmMenuTreeCell<XmMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<XmMenu> xmMenuXmMenuTreeCell) {
//                                setStyle("-fx-background-color:#ffffffaa; -fx-background-insets: 5 10 5 10; -fx-background-radius: 50; ");
//                                setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, 14));

                            }
                        });

                        setUpdateHoverSkin(new CallBack<XmMenuTreeCell<XmMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<XmMenu> xmMenuXmMenuTreeCell) {
//                                setStyle("-fx-background-color: #ffffff22;");
                                setStyle("-fx-background-color:#ffffff66; -fx-background-insets: 5 10 5 10; -fx-background-radius: 50;");
                            }
                        });

                        setUpdateOddSkin(new CallBack<XmMenuTreeCell<XmMenu>>() {
                            @Override
                            public void call(XmMenuTreeCell<XmMenu> xmMenuXmMenuTreeCell) {
                                setStyle("-fx-background-color:transparent;");
                            }
                        });

                        setOnMouseClicked(e->{
                            x = e.getX();
                            y = e.getY();
                            double width = getWidth(), height = getHeight();
                            AnimationTimer timer = new AnimationTimer() {
                                double startRadius = 0.1;
                                double endRadius = 5;
                                final double frameDuration = 1_000_000_000.0 / 60;
                                long lastUpdate = 0;

                                @Override
                                public void handle(long now) {
                                    double elapsedNanos = now - lastUpdate;
                                    if (elapsedNanos >= frameDuration) {

                                        startRadius += 0.1;
                                        if(startRadius >1){
                                            startRadius = 1;
                                        }

                                        if(startRadius<=1){
                                            if(getPseudoClassStates().contains(selected)){
                                                List<Stop> stops = new ArrayList<Stop>();
                                                stops.add(new Stop(0, Color.web("#ffffffaa")));
                                                stops.add(new Stop(startRadius-0.05, Color.web("#ffffffaa")));
                                                stops.add(new Stop(startRadius, Color.web("#ffffff33")));
                                                RadialGradient radialGradient = new RadialGradient(0, 0, x/width, y/height, 1.2,true,
                                                        CycleMethod.NO_CYCLE, stops);
                                                String rgStr = radialGradient.toString().replace("0x", "#");
                                                setStyle("-fx-background-insets: 5 10 5 10; -fx-background-radius: 50;-fx-background-color:"+rgStr+";");
                                            }
                                            if(startRadius == 1){
                                                stop();
                                            }
                                        }else{
                                            stop();
                                        }
                                        lastUpdate = now;
                                    }
                                }
                            };
                            timer.start();

                        });
                    }

                    @Override
                    public void updateItem(XmMenu item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                            setGraphic(null);
                        }else{
                            item.getIcon().setMouseTransparent(true);
                            setGraphic(item.getIcon());
                            setText(item.getLabel());
                        }

                    }
                };
            }
        });
    }


}
