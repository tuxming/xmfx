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
package com.xm2013.jfx.control.animate;

import com.xm2013.jfx.common.CallBack;
import javafx.scene.Node;

/**
 * 组件的点动画定义
 */
public interface ClickAnimate {
    /**
     * 设置动画的起始位置
     * @param x double
     * @param y double
     * @return ClickAnimate
     */
    ClickAnimate setPoint(double x, double y);

    /**
     * 设置节点位置，
     *  因为是不同的调用方式，所以实现方法可能不一样，所以改用回调的方式，交给组件自己实现
     * @param callback ResetNodePositionCallback
     */
    void setNodePosition(ResetNodePositionCallback callback);

    /**
     * 添加节点到control
     * 因为是不同的调用方式，所以实现方法可能不一样，所以改用回调的方式，交给组件自己实现
     * @param callback CallBack
     */
    void setAddNode(CallBack<Node> callback);

    /**
     * 移除节点control
     * 因为是不同的调用方式，所以实现方法可能不一样，所以改用回调的方式，交给组件自己实现
     * @param callback CallBack
     */
    void setRemoveNode(CallBack<Node> callback);

    /**
     * 播放动画
     */
    void play();

    /**
     * 销毁
     */
    void dispose();
}
