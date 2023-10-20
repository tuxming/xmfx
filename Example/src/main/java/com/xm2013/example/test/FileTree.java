package com.xm2013.example.test;

import com.xm2013.jfx.control.icon.XmFontIcon;

public class FileTree {

    public static MyDMenu build(){
        MyDMenu rootMenu = new MyDMenu("我的电脑", new XmFontIcon("\ue69a"));

        MyDMenu c = new MyDMenu("System(C:)", new XmFontIcon("\ue663"), rootMenu);

        MyDMenu ProgramFiles = new MyDMenu("Program Files", new XmFontIcon("\uec17"), c);
        MyDMenu ProgramData = new MyDMenu("Program Data", new XmFontIcon("\uec17"), c);
        MyDMenu Users = new MyDMenu("Users", new XmFontIcon("\uec17"), c);
        MyDMenu Windows = new MyDMenu("Windows", new XmFontIcon("\uec17"), c);

        c.getChildren().addAll(ProgramFiles, ProgramData, Users, Windows);
        ProgramFiles.getChildren().addAll(
                new MyDMenu("Android", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Application Verifier", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Common Files", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Google", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft.NET", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Windows Defender", new XmFontIcon("\uec17"), ProgramFiles)
        );

        ProgramData.getChildren().addAll(
                new MyDMenu("Android", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Application Verifier", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Common Files", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Google", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Microsoft.NET", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Windows Defender", new XmFontIcon("\uec17"), ProgramFiles)
        );

        Users.getChildren().addAll(
                new MyDMenu("Administrator", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Default", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Public", new XmFontIcon("\uec17"), ProgramFiles)
        );

        Windows.getChildren().addAll(
                new MyDMenu("boot", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("fonts", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("System32", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("bootstat.dat", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("notepad.exe", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("regedit.exe", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("system.ini", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("win.ini", new XmFontIcon("\ueabe"), ProgramFiles),
                new MyDMenu("write.exe", new XmFontIcon("\ueabe"), ProgramFiles)
        );


        MyDMenu d = new MyDMenu("Application(D:)", new XmFontIcon("\ue663"), rootMenu);
        MyDMenu app = new MyDMenu("App", new XmFontIcon("\uec17"), d);
        MyDMenu developApp = new MyDMenu("develop-app", new XmFontIcon("\uec17"), d);
        d.getChildren().addAll(app, developApp);

        app.getChildren().addAll(
                new MyDMenu("7-Zip", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Adobe", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("aDrive", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("aliwangwang", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Apifox", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("baidu", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("bilibili", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Camtasia 2021", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("CocosDashboard_1.2.2", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("DesktopLite", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("DocBox", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("dzclient", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Eziriz", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Ezviz Studio", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Fiddler", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("FileZilla FTP Client", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("foxmail", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Git", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("i4Tools7", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("IQIYI Video", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("leidian", new XmFontIcon("\uec17"), ProgramFiles)
        );

        developApp.getChildren().addAll(
                new MyDMenu("AndroidSDK", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Another-Redis-Desktop-Manager", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("apache-maven-3.8.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("apache-tomcat-10.1.7", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("eclipse", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("exe4j", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("gradle-7.5.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("ideaIC-2022", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Inno Setup 6", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("java", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("javafx-sdk-19", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("mysql-8.0.32-winx64", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("Python", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("redis-windows-7.0.8.1", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("SceneBuilder", new XmFontIcon("\uec17"), ProgramFiles),
                new MyDMenu("微信web开发者工具", new XmFontIcon("\uec17"), ProgramFiles)
        );

        MyDMenu e = new MyDMenu("Document(E:)", new XmFontIcon("\ue663"), rootMenu);
        MyDMenu Videos = new MyDMenu("视频", new XmFontIcon("\uec17"), e);
        MyDMenu d3s = new MyDMenu("3D 对象", new XmFontIcon("\uec17"), e);
        MyDMenu Pictures = new MyDMenu("图片", new XmFontIcon("\uec17"), e);
        MyDMenu Downloads = new MyDMenu("下载", new XmFontIcon("\uec17"), e);
        MyDMenu Musics = new MyDMenu("音乐", new XmFontIcon("\uec17"), e);

        e.getChildren().addAll(
                Videos,
                d3s,
                Pictures,
                Downloads,
                Musics
        );

        MyDMenu f = new MyDMenu("Game(F:)", new XmFontIcon("\ue663"), rootMenu);
        MyDMenu AlienShooter = new MyDMenu("AlienShooter", new XmFontIcon("\uec17"), f);
        MyDMenu GenshinImpact = new MyDMenu("Genshin Impact", new XmFontIcon("\uec17"), f);
        MyDMenu KingdomChronicles = new MyDMenu("KingdomChronicles", new XmFontIcon("\uec17"), f);
        MyDMenu Netease = new MyDMenu("Netease", new XmFontIcon("\uec17"), f);
        MyDMenu war3 = new MyDMenu("war3", new XmFontIcon("\uec17"), f);

        f.getChildren().addAll(
                AlienShooter,
                GenshinImpact,
                KingdomChronicles,
                Netease,
                war3
        );

        rootMenu.getChildren().addAll(c, d, e, f);

        return rootMenu;
    }

}
