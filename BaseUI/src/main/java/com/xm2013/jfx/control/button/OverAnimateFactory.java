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
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.Text;


public class OverAnimateFactory {


    public static XmButtonHoverAnimate getAnimate(XmButtonSkin skin, Node icon, Text text){

        XmButton button = (XmButton) skin.getNode();

        XmButtonType.BtnDisplayType btnDisplayType =  button.getDisplayType();
        ContentDisplay contentDisplay = button.getContentDisplay();
        XmButtonHoverAnimate animate = null;

        boolean isHorizontal = ContentDisplay.LEFT.equals(contentDisplay)
                || ContentDisplay.RIGHT.equals(contentDisplay);
        boolean isVertical = ContentDisplay.TOP.equals(contentDisplay)
                || ContentDisplay.BOTTOM.equals(contentDisplay);

        if(XmButtonType.BtnDisplayType.OVER_SHOW_ICON.equals(btnDisplayType)){
//            if(isHorizontal) animate = new OverShowIconHAnimate(skin, icon);
//            if(isVertical) animate = new OverShowIconVAnimate(skin, icon);
            animate = new OverShowIconAnimate(skin, text, icon);
        }else if(XmButtonType.BtnDisplayType.OVER_SHOW_TEXT.equals(btnDisplayType)){
//            XmButtonType.BtnRoundType round = button.getType().getRoundType();
//            boolean isCircle = XmButtonType.BtnRoundType.CIRCLE.equals(round)
//                    || XmButtonType.BtnRoundType.SEMICIRCLE.equals(round);
//            if(isVertical && isCircle ){
//                animate = new OverShowTextVAnimate(skin,icon,  text);
//            }else{
//                animate = new OverShowTextAnimate(skin, text);
//            }
            animate = new OverShowTextNAnimate(skin, text, icon);
        }else if(XmButtonType.BtnDisplayType.OVER_SWITCH_ICON.equals(btnDisplayType)){
            animate = new OverSwitchIconAnimate(skin, icon, text);
        }else if(XmButtonType.BtnDisplayType.OVER_SWITCH_TEXT.equals(btnDisplayType)){
            animate = new OverSwitchTextAnimate(skin, icon);
        }
        return animate;
    }


}
