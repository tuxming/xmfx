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
package com.xm2013.jfx.control.textfield;

import javafx.scene.control.skin.TextFieldSkin;

public class InnerPasswordFieldSkin extends TextFieldSkin {
    private InnerPasswordField control;
    public InnerPasswordFieldSkin(InnerPasswordField control) {
        super(control);
        this.control = control;
    }

    @Override
    protected String maskText(String txt) {
//        System.out.println("exec maskText:"+txt);
        if(control!=null && control.isShowPassword()){
//            System.out.println("111");
            return txt;
        } else {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            String echo = "*";
            if(control!=null){
                echo = control.getEchochar();
            }
            for (int i = 0; i < n; i++) {

                if(control == null){

                }
                passwordBuilder.append(echo);
            }

//            System.out.println("222:"+passwordBuilder.toString());
            return passwordBuilder.toString();
        }
    }
}
