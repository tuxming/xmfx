package com.xm2013.template.template;

import com.xm2013.jfx.control.label.XmLabel;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class LogoPane extends Pane {

    private ImageView logo;
    private XmLabel title;
    private XmLabel subTitle;

    public LogoPane(){
        String url = HelloApplication.class.getResource("/images/logo.png").toExternalForm();
        logo = new ImageView(new Image(url));
        logo.setFitHeight(64);
        logo.setFitWidth(64);
        title = new XmLabel("系统模板");
        subTitle =  new XmLabel("xm2013.com");

        widthProperty().addListener((ob, ov, nv)->{
            if(nv.doubleValue()<100){
                logo.setFitHeight(40);
                logo.setFitWidth(40);
                title.setVisible(false);
                subTitle.setVisible(false);
            }else{
                logo.setFitHeight(64);
                logo.setFitWidth(64);
                title.setVisible(true);
                subTitle.setVisible(true);
            }
//            System.out.println(logo.getFitHeight()+","+ logo.getFitWidth()+", "+getWidth());
        });

        getChildren().addAll(logo, title, subTitle);
    }

    @Override
    protected double computePrefHeight(double width) {

        double height = 20 + logo.prefHeight(-1);
        if(title.isVisible()){
            height += 8 + title.prefHeight(-1);
        }

        if(subTitle.isVisible()){
            height += 8 + subTitle.prefHeight(-1);
        }

        height += 20;

        return height;
    }

    @Override
    protected void layoutChildren() {

        double width = this.prefWidth(-1), height = this.prefHeight(-1);
        double logoWidth = logo.prefWidth(-1), logoHeight = logo.prefHeight(-1), logoX = (width - logoWidth)/2, logoY = 20;

        layoutInArea(logo, logoX, logoY, logoWidth, logoHeight, -1, HPos.CENTER, VPos.CENTER);

        double titleY = logoY, titleHeight = 0;
        if(title.isVisible()){
            double titleWidth = title.prefWidth(-1), titleX = (width - titleWidth) / 2;
            titleY = logoY + logoHeight+8;
            titleHeight = title.prefHeight(-1);
            layoutInArea(title, titleX, titleY, titleWidth, titleHeight, -1, HPos.CENTER, VPos.CENTER);
        }

        if(subTitle.isVisible()){
            double subTitleWidth = subTitle.prefWidth(-1), subTitleHeight = subTitle.prefHeight(-1), subTitleX = (width - subTitleWidth)/2, subTitleY = titleY + titleHeight;
            layoutInArea(subTitle, subTitleX, subTitleY, subTitleWidth, subTitleHeight, -1, HPos.CENTER, VPos.CENTER);
        }

    }
}
