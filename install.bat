@ECHO OFF
cls

set JAVA_HOME=C:\desenvolvimento\java\jdk1.8.0_202
set JAVA=%JAVA_HOME%\bin

echo.
echo #############################################################
echo [INFO] BUILDING
echo [INFO] JAVA_HOME = %JAVA_HOME%
echo #############################################################
echo.

REM call xcopy /s /e /y src\main\java src\main\resources

if exist build call rmdir /s /q build

REM Comando que gera o jar EXECUTANTO os cenarios de testes
REM mvnw clean install
REM Comando que gera o jar SEM EXECUTAR os cenarios de testes
call call mvnw clean install -DskipTests=true

call mkdir build

call copy target\gisjmsservices-0.0.1-SNAPSHOT.jar build\gisjmsservices.jar
call copy application.properties build\application.properties
call copy log4j2.xml build\log4j2.xml

echo.

call pause