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

import com.xm2013.jfx.common.XmMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {
    private Menu defaultMenu = null;
    public List<Menu> buildMenu(){

        List<Menu> menus = new ArrayList<>();

        //关于我们
        Menu menu1 = new Menu("\ue640", "关于我们",0);
        Menu subMenu11 = new Menu("\ue62e", "概述", "", 1, menu1);
        Menu subMenu12 = new Menu("\ue67b", "反馈", "",2, menu1);
        Menu subMenu13 = new Menu("\ue640", "关于我们","",3, menu1);
        Menu subMenu14 = new Menu("\ue640", "使用帮助","",4, menu1);

        Menu child111 = new Menu("\ue62e", "三级菜单","", 5, subMenu11);
        Menu child112 = new Menu("\ue62e", "三级菜单","", 6, subMenu11);
        Menu child113 = new Menu("\ue62e", "三级菜单","", 7, subMenu11);
        Menu child114 = new Menu("\ue62e", "三级菜单","", 8, subMenu11);

        subMenu11.addAllChildren(child111, child112, child113, child114);

        //主题
        Menu menu2 = new Menu("\ue7cd", "主题",9);
        Menu subMenu21 = new Menu("\ue645", "默认主题","", 10, menu2);
        menu2.addAllChildren(subMenu21);

        menu1.addAllChildren(subMenu11, subMenu12, subMenu13, subMenu14);

        //组件
        Menu menu3 = new Menu("\ue6de", "组件", "", 11);
        Menu menu31 = new Menu("\ue65c", "标签（XmLabel）", "com.xm2013.example.example.page.LabelPage", 12, menu3);
        Menu menu32 = new Menu("\ue6fc", "按钮（XmButton）", "com.xm2013.example.example.page.ButtonPage", 13, menu3);
        Menu menu33 = new Menu("\ue77c", "文本/密码框（XmTextField）", "com.xm2013.example.example.page.TextFieldPage", 14, menu3);
        Menu menu34 = new Menu("\ue6da", "单选/复选（CheckBox）", "com.xm2013.example.example.page.CheckBoxPage", 15, menu3);
        Menu menu35 = new Menu("\ue751", "Tag", "com.xm2013.example.example.page.TagPage", 16, menu3);
        Menu menu36 = new Menu("\ue61c", "下拉菜单", "com.xm2013.example.example.page.DropdownMenuPage", 17, menu3);
        Menu menu37 = new Menu("\ueb94", "下拉选择框", "com.xm2013.example.example.page.SelectorPage", 18, menu3);
        Menu menu38 = new Menu("\ue65e", "日期选择框", "com.xm2013.example.example.page.DateSelectorPage", 19, menu3);
        Menu menu39 = new Menu("\ue65e", "日期范围选择框", "com.xm2013.example.example.page.DateRangeSelectorPage", 20, menu3);
        Menu menu40 = defaultMenu= new Menu("\ue6fd", "分页", "com.xm2013.example.example.page.PagerPage", 21, menu3);

        menu3.addAllChildren(menu31, menu32, menu33, menu34, menu35, menu36, menu37, menu38, menu39, menu40);

        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);

        return menus;
    }

    public XmMenu getDefaultMenu() {
        return defaultMenu;
    }
}
