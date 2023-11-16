package com.xm2013.template.template;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BasePane extends Pane {

    private ImageView rootBackground;
    private ImageView backgroundPane;

    private Rectangle background;

    public BasePane(ImageView rootBackground, DoubleProperty radius){

        this.rootBackground = rootBackground;

        backgroundPane = new ImageView();
        backgroundPane.setEffect(new GaussianBlur(30));
        Rectangle bgClip =  new Rectangle();
        bgClip.widthProperty().bind(backgroundPane.fitWidthProperty());
        bgClip.heightProperty().bind(backgroundPane.fitHeightProperty());
        bgClip.arcWidthProperty().bind(radius);
        bgClip.arcHeightProperty().bind(radius);
        backgroundPane.setClip(bgClip);

        backgroundPane.fitHeightProperty().bind(heightProperty());
        backgroundPane.fitWidthProperty().bind(widthProperty());

        background = new Rectangle();
        background.arcWidthProperty().bind(radius);
        background.arcHeightProperty().bind(radius);
        background.setFill(Color.web("#ffffff66"));
//        background.setStyle("-fx-background-color: white;");
        background.heightProperty().bind(heightProperty());
        background.widthProperty().bind(widthProperty());
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#00000099"));
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(0);
        dropShadow.setRadius(20);
        background.setEffect(dropShadow);


        getChildren().addAll(backgroundPane,background);

        heightProperty().addListener(sizeChangeChange);
        widthProperty().addListener(sizeChangeChange);
    }

    private ChangeListener<Number> sizeChangeChange = (ob, ov, nv)->{
        double w = getWidth(), h = getHeight();
        if(w>0 && h>0){
            WritableImage wi = new WritableImage((int)getWidth(), (int)getHeight());
            rootBackground.snapshot(new SnapshotParameters(), wi);
            backgroundPane.setImage(wi);
        }
    };

//    @Override
//    protected void layoutChildren() {
//
//        layoutInArea(background, 0, 0, background.prefWidth(-1), background.prefHeight(-1), -1, HPos.CENTER, VPos.CENTER);
//        layoutInArea(backgroundPane, 0, 0, backgroundPane.prefWidth(-1), backgroundPane.prefHeight(-1), -1, HPos.CENTER, VPos.CENTER);
//    }
}
