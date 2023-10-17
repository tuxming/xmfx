package com.xm2013.jfx.control.button.animate;

/**
 * 鼠标放上去时触发的动画
 */
public interface XmButtonHoverAnimate {

    /**
     * 鼠标移入到控件上
     */
    void moveIn();

    /**
     * 鼠标从空间上移出
     */
    void moveOut();

    void dispose(boolean resetContentDisplay);

}
