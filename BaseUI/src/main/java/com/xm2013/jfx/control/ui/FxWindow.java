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
package com.xm2013.jfx.control.ui;

import com.xm2013.jfx.borderless.BorderlessPane;
import com.xm2013.jfx.borderless.BorderlessScene;
import com.xm2013.jfx.borderless.CustomStage;
import com.xm2013.jfx.component.eventbus.FXEventBus;
import com.xm2013.jfx.component.eventbus.XmEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;

public class FxWindow {
    private double width, height;
    private CustomStage stage;
    private BorderlessScene scene = null;

    public FxWindow(double width, double height, Pane rootPane) throws IOException {
        this.width = width;
        this.height = height;

        stage = new CustomStage(StageStyle.TRANSPARENT);
        stage.setWidth(this.width+40);
        stage.setHeight(this.height+40);

        scene = stage.craftBorderlessScene(rootPane);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        scene.removeDefaultCSS();

        rootPane.setStyle("-fx-background-color: white;");

        final BorderlessPane root = (BorderlessPane) scene.getRoot();
        root.setStyle("-fx-background-color: transparent; "
                + "-fx-padding:20px; "
                + "-fx-effect: dropshadow(gaussian, rgba(10,10,10,0.2), 20, 0.1,0px, 0px);");

        scene.maximizedProperty().addListener((ob, ov, nv)->{
            if(nv) {
                root.setStyle("");
            }else {
                root.setStyle("-fx-background-color: transparent; "
                        + "-fx-padding:20px; "
                        + "-fx-effect: dropshadow(gaussian, rgba(10,10,10,0.2), 20, 0.1,0px, 0px);");
            }
        });

        FXEventBus.getDefault().addEventHandler(XmEvent.MAX_WINDOW, e -> {
            scene.maximizeStage();
        });

        FXEventBus.getDefault().addEventHandler(XmEvent.MIN_WINDOW, e-> {
            stage.setIconified(true);
        });

        FXEventBus.getDefault().addEventHandler(XmEvent.CLOSE_WINDOW, e->{
            stage.close();
        });

    }

    public void setMoveControl(Node node){
        scene.setMoveControl(node);
    }

    public void show() {
        stage.showAndAdjust();
    }

    public void setTitle(String text, Image graphics) {
        stage.setTitle(text);
        if(graphics!=null) {
            stage.getIcons().add(graphics);
        }
    }

    public void setTitle(String text) {
        stage.setTitle(text);
    }

    public CustomStage getStage() {
        return stage;
    }

    public BorderlessScene getScene() {
        return scene;
    }
}
