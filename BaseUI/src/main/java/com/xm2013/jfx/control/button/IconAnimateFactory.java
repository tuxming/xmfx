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

import com.xm2013.jfx.control.button.animate.*;
import javafx.scene.Node;

public class IconAnimateFactory {

    public static XmButtonIconAnimate get(XmButton control, Node icon){
        XmButtonType.BtnIconAnimationType type = control.getIconAnimateType();

        XmButtonIconAnimate animate = null;

        if(XmButtonType.BtnIconAnimationType.ROTATE.equals(type)){
            animate = new IconRotateAnimate(icon);
        }else if(XmButtonType.BtnIconAnimationType.JITTER.equals(type)){
            animate = new IconJitterAnimate(icon);
        }else if(XmButtonType.BtnIconAnimationType.OVERTURN.equals(type)){
            animate = new IconTranslateYAnimate(icon);
        }else if(XmButtonType.BtnIconAnimationType.FLASH.equals(type)){
            animate = new IconFlashAnimate(icon);
        }else if(XmButtonType.BtnIconAnimationType.SCALE.equals(type)){
            animate = new IconScaleAnimate(icon);
        }
        return animate;

    }

}
