package com.xm2013.template.template;

import com.xm2013.jfx.component.eventbus.FXEventBus;
import com.xm2013.jfx.component.eventbus.XmEvent;
import com.xm2013.jfx.control.button.XmNodeButton;
import com.xm2013.jfx.control.svg.XmSVGView;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;

public class SidebarBottomPane extends Pane {
    private static String LOGOUT_ICON = "<svg><path d=\"M796.8 64 544 64c-19.2 0-32 12.8-32 32s12.8 32 32 32l252.8 0C800 128 800 128 800 128L800 896c0 0-3.2 0-3.2 0L544 896c-19.2 0-32 12.8-32 32s12.8 32 32 32l252.8 0c38.4 0 67.2-28.8 67.2-64L864 128C864 92.8 832 64 796.8 64z\" p-id=\"12444\"></path><path d=\"M268.8 544 640 544c19.2 0 32-12.8 32-32s-12.8-32-32-32L268.8 480l137.6-137.6c12.8-12.8 12.8-32 0-44.8s-32-12.8-44.8 0l-192 192c-3.2 3.2-6.4 6.4-6.4 9.6-3.2 6.4-3.2 16 0 25.6 3.2 3.2 3.2 6.4 6.4 9.6l192 192c6.4 6.4 16 9.6 22.4 9.6s16-3.2 22.4-9.6c12.8-12.8 12.8-32 0-44.8L268.8 544z\" p-id=\"12445\"></path></svg>";
    private static String EXPAND_ICON = "<svg><path d=\"M957.9 788c2.8 0 5 3.8 5.1 8.6v62.5c0 4.8-2.2 8.7-5 8.9H456.1c-2.8 0-5-3.8-5.1-8.6v-62.5c0-4.8 2.2-8.7 5-8.9h501.9zM132.8 247.4l211.8 211.8c14.2 14.2 22 33 22 53.1s-7.8 38.9-22 53.1L132.8 777.2c-20.9 20.9-56.1 14.2-68.1-12.5l-0.4-0.8c-6.7-15.8-3.1-34.1 9-46.2l205.4-205.4L73.3 306.9c-12-12-15.6-30-9.2-45.7l0.2-0.5c11.7-27.5 47.4-34.4 68.5-13.3zM957.9 468c2.8 0 5 3.8 5.1 8.6v62.5c0 4.8-2.2 8.7-5 8.9H456.1c-2.8 0-5-3.8-5.1-8.6v-62.5c0-4.8 2.2-8.7 5-8.9h501.9z m0-312c2.8 0 5 3.8 5.1 8.6v62.5c0 4.8-2.2 8.7-5 8.9H456.1c-2.8 0-5-3.8-5.1-8.6v-62.5c0-4.8 2.2-8.7 5-8.9h501.9z\" p-id=\"15913\"></path></svg>";
    private final XmSVGView expandIcon;

    private XmNodeButton logoutLabel;
    private XmNodeButton expandLabel;

    private BooleanProperty expand;

    public SidebarBottomPane(BooleanProperty expand){
        this.expand = expand;
        XmSVGView logoutIcon = new XmSVGView(LOGOUT_ICON);
        logoutIcon.setMouseTransparent(true);
        logoutLabel = new XmNodeButton(logoutIcon);

        expandIcon = new XmSVGView(EXPAND_ICON);
        expandIcon.setMouseTransparent(true);
        expandLabel = new XmNodeButton(expandIcon);
        setExpand();
        //点击展开收缩按钮
        expandLabel.setOnAction(e -> {
            setExpand();
            FXEventBus.getDefault().fireEvent(new XmEvent<>(XmEventType.SIDE_BAR_EXPAND));
        });

        getChildren().addAll(logoutLabel, expandLabel);
    }

    private void setExpand(){
        if(expand.get()){
            expandIcon.setRotate(0);
        }else{
            expandIcon.setRotate(180);
        }
    }

    @Override
    protected double computePrefHeight(double width) {
        double height = 0;
        width = prefWidth(-1);
        if(width<=80){
            height = 10+logoutLabel.prefHeight(-1) + 10 + expandLabel.prefHeight(-1)+10;
        }else{
            height = Math.max(logoutLabel.prefHeight(-1), expandLabel.prefHeight(-1))+20;
        }
        return height;
    }

    @Override
    protected void layoutChildren() {

        double width = prefWidth(-1), height = prefHeight(-1),
            logoutWidth = logoutLabel.prefWidth(-1), logoutHeight = logoutLabel.prefHeight(-1),
            expandWidth = expandLabel.prefWidth(-1), expandHeight = expandLabel.prefHeight(-1);

        if(width<=80){
            layoutInArea(expandLabel, (width - expandWidth)/2, 10, expandWidth, expandHeight, -1, HPos.CENTER, VPos.CENTER);
            layoutInArea(logoutLabel, (width - logoutWidth)/2, 20+expandHeight, logoutWidth, logoutHeight, -1, HPos.CENTER, VPos.CENTER);
        }else{
            layoutInArea(expandLabel, 10, 10, expandWidth, expandHeight, -1, HPos.CENTER, VPos.CENTER);
            layoutInArea(logoutLabel, width - 10 - logoutWidth, 10, logoutWidth, logoutHeight, -1, HPos.CENTER, VPos.CENTER);
        }
    }
}
