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
package com.xm2013.example.example;

import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.ui.DefaultTheme;
import com.xm2013.jfx.ui.FxWindow;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private DefaultTheme layout = null;

    @Override
    public void start(Stage stage) throws IOException {

        layout = new DefaultTheme();

        FxWindow window = new FxWindow(1200, 900, layout);

        window.setMoveControl(layout.getTopPane());
        window.setTitle("XmUI Example");
        window.show();

        initPage();
    }

    private void initPage(){
        XmFontIcon.setIconFont(getClass().getResource("/font/iconfont.ttf").toExternalForm());

        Image logo = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        layout.setLogoImg(logo);
        layout.setTitle("UI Components");
        layout.setSubTitle("https://xm2013.com");

        MenuBuilder builder = new MenuBuilder();
        layout.addAllMenu(builder.buildMenu());
        layout.addContentPage(builder.getDefaultMenu());
    }

    public static void main(String[] args) {
        launch();
    }
}