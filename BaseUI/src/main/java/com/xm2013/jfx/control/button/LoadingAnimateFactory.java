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
package com.xm2013.jfx.control.button;

import com.xm2013.jfx.control.button.animate.LoadingDefaultAnimate;
import com.xm2013.jfx.control.button.animate.LoadingIconAnimate;
import com.xm2013.jfx.control.button.animate.LoadingProgressBarAnimate;
import com.xm2013.jfx.control.button.animate.XmButtonLoadingAnimate;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class LoadingAnimateFactory {

    public static XmButtonLoadingAnimate getLoadingAnimate(XmButtonSkin skin, XmButton control, Text text, Node icon){
        XmButtonType.BtnLoadingType type = control.getLoadingType();
        if(XmButtonType.BtnLoadingType.ICON.equals(type)){
            return new LoadingIconAnimate(skin, text, icon);
        }else if(XmButtonType.BtnLoadingType.H_PROGRESS.equals(type)
            || XmButtonType.BtnLoadingType.V_PROGRESS.equals(type)
        ){
            return new LoadingProgressBarAnimate(skin, text, icon);
        }else{
            return new LoadingDefaultAnimate(skin, text, icon);
        }

    }

}
