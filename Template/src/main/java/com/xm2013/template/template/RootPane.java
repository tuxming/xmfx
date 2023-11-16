package com.xm2013.template.template;


import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.XmMenu;
import com.xm2013.jfx.component.eventbus.FXEventBus;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.treeview.XmMenuTreeCell;
import com.xm2013.jfx.control.treeview.XmTreeView;
import com.xm2013.jfx.ui.FxWindow;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.security.Key;

public class RootPane extends Pane {

    private final SideBar sideBar;
    private final MainPane mainPane;
    ImageView backgroundPane;

    private DoubleProperty sideBarWidth = new SimpleDoubleProperty(150);
    private BooleanProperty expand = new SimpleBooleanProperty(true);
    private DoubleProperty spacing = new SimpleDoubleProperty(10);
    private DoubleProperty radius = new SimpleDoubleProperty(5);

    private Timeline timeline;

    public RootPane(){

        Image image = new Image("file:///E:/Pictures/壁纸/423ee155-684e-4a78-9a88-1c7f8af402fb.jpg");
        backgroundPane = new ImageView(image);

        sideBar = new SideBar(backgroundPane, expand, radius);
        sideBar.prefWidthProperty().bind(sideBarWidth);
//        sideBar.setStyle("-fx-background-color: #000000aa");
        mainPane = new MainPane(backgroundPane, radius);

        getChildren().addAll(backgroundPane, mainPane, sideBar);
    }

    public void init(FxWindow window){

        //绑定宽高
        prefHeightProperty().bind(window.heightProperty());
        prefWidthProperty().bind(window.widthProperty());

        backgroundPane.fitWidthProperty().bind(window.widthProperty());
        backgroundPane.fitHeightProperty().bind(window.heightProperty());

        sideBar.prefHeightProperty().bind(window.heightProperty());


        //侧边栏展开，收缩
        double width = sideBarWidth.get();
        timeline = new Timeline(new KeyFrame(Duration.millis(0), "kf1",new KeyValue(sideBarWidth, width)),
                    new KeyFrame(Duration.millis(200), "kf2", new KeyValue(sideBarWidth, 64))
                );

        FXEventBus.getDefault().addEventHandler(XmEventType.SIDE_BAR_EXPAND, e->{
            boolean ep = expand.get();
            expand.set(!ep);
            if(ep){
                timeline.setRate(1);
                timeline.playFrom("kf1");
            }else{
                timeline.setRate(-1);
                timeline.playFrom("kf2");
            }
        });

    }

    @Override
    protected void layoutChildren() {

        super.layoutChildren();

        double spacing = this.spacing.get();
        double width = getWidth(), height = getHeight();

        double sideBarWidth = sideBar.prefWidth(-1);
        layoutInArea(sideBar, spacing, spacing, sideBarWidth, height-spacing*2, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(mainPane, spacing+sideBarWidth+spacing, spacing, width - sideBarWidth - spacing*3 , height-spacing*2, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(backgroundPane, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
    }
}
