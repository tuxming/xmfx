package com.xm2013.template.template;

import com.xm2013.jfx.ui.FxWindow;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

//        Pane backgroundPane = new Pane();
//        backgroundPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        RootPane controlPane = new RootPane();
        StackPane rootPane = new StackPane();
        rootPane.getChildren().addAll(controlPane);

        FxWindow fxWindow = new FxWindow(1200, 900, rootPane);
        controlPane.init(fxWindow);
        fxWindow.setMoveControl(rootPane);
        fxWindow.setTitle("ThemeOne",new Image(HelloApplication.class.getResource("/images/logo.png").toExternalForm()));

//        XmButton btn = new XmButton("按钮");
//        Pane pane = new Pane(btn);
//        FxWindow fxWindow = new FxWindow(300, 400, pane);
//        pane.setStyle("-fx-background-color: transparent;");
//
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                WritableImage wi = new WritableImage((int)pane.getWidth(), (int)pane.getHeight());
//                fxWindow.getScene().snapshot(new Callback<SnapshotResult, Void>() {
//                    @Override
//                    public Void call(SnapshotResult param) {
//                        return null;
//                    }
//                }, wi);
//
//                File file = new File("D:/Desktop/image.png");
//
//                try {
//                    ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", file);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

        fxWindow.show();
    }


    public static void main(String[] args) {
        launch();
    }
}