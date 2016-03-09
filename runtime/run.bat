
@echo off
rem Use JAVA_HOME if it's set; otherwise, just use java

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
set JAVA_EXE="%JAVA_HOME%\bin\java.exe"
echo JAVA_HOME found: %JAVA_EXE%
echo.
goto startApp

:noJavaHome

echo No Java installation exists... Install Java 1.8 or higher.
TIMEOUT /T 30

goto eof
echo The JAVA_HOME environment variable is not defined correctly.
echo Instead the PATH will be used to find the java executable.
echo.
set JAVA_EXE=java
goto startApp

:startApp

echo WARNING: If the Java version (below) is less than 1.8,
echo          this app will fail. Sorry in advance...
echo.
%JAVA_EXE% -version
TIMEOUT /T 30

set APP_ROOT=%~dp0\
pushd "%APP_ROOT%"

rem set APP_CONF_DIR=%APP_ROOT%\config
rem set TIMESTAMP=%DATE:~6,4%-%DATE:~3,2%-%DATE:~0,2%-%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%

set JAVA_ARGS=-DappRootDir="%APP_ROOT%" -DappVersion=${project.version}
rem set PROG_ARGS=configDir=

%JAVA_EXE% %JAVA_ARGS% -jar ${project.artifactId}-${project.version}.jar

:eof
