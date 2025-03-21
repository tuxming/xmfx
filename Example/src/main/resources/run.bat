@echo off
set JAVA_HOME=%~dp0jdk
set LIB_DIR=%~dp0lib

start "" "%JAVA_HOME%\bin\javaw" --module-path "%LIB_DIR%" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "Example-1.0.jar;%LIB_DIR%\*" com.xm2013.example.example.MainApplication