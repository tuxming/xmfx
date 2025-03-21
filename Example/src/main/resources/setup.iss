[Setup]
AppName=XmUI-Example
AppVersion=1.0
DefaultDirName={commonpf}\XmUI-Example
DefaultGroupName=XmUI-Example
OutputDir=.
OutputBaseFilename=XmUI-Example安装程序
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
SetupIconFile=build\logo.ico
UninstallDisplayIcon={app}\logo.ico
LanguageDetectionMethod=uilanguage
ShowLanguageDialog=no

[Files]
; 明确指定要包含的内容
Source: "build\*.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "build\*.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "build\*.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "build\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "build\jdk\*"; DestDir: "{app}\jdk"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\XmUI-Example"; Filename: "{app}\Example.exe"
Name: "{group}\卸载 XmUI-Example"; Filename: "{uninstallexe}"
Name: "{commondesktop}\XmUI-Example"; Filename: "{app}\Example.exe"

[Messages]
WelcomeLabel1=欢迎安装 XmUI-Example
WelcomeLabel2=这将在您的计算机上安装 [name/ver]。%n%n建议在继续之前关闭所有其他应用程序。
FinishedLabel=安装已完成。
FinishedHeadingLabel=完成 XmUI-Example 安装 