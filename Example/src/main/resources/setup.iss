[Setup]
AppName=XmUI-Example
AppVersion=1.0
DefaultDirName={commonpf}\XmUI-Example
DefaultGroupName=XmUI-Example
OutputDir=.
OutputBaseFilename=XmUI-Example��װ����
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
SetupIconFile=build\logo.ico
UninstallDisplayIcon={app}\logo.ico
LanguageDetectionMethod=uilanguage
ShowLanguageDialog=no

[Files]
; ��ȷָ��Ҫ����������
Source: "build\*.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "build\*.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "build\*.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "build\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "build\jdk\*"; DestDir: "{app}\jdk"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\XmUI-Example"; Filename: "{app}\Example.exe"
Name: "{group}\ж�� XmUI-Example"; Filename: "{uninstallexe}"
Name: "{commondesktop}\XmUI-Example"; Filename: "{app}\Example.exe"

[Messages]
WelcomeLabel1=��ӭ��װ XmUI-Example
WelcomeLabel2=�⽫�����ļ�����ϰ�װ [name/ver]��%n%n�����ڼ���֮ǰ�ر���������Ӧ�ó���
FinishedLabel=��װ����ɡ�
FinishedHeadingLabel=��� XmUI-Example ��װ 