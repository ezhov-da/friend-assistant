@echo off
chcp 866
cd /d %~dp0
..\..\java\11.0.18\bin\java -Dfile.encoding=UTF-8 -Dvosk.model.folder.path=./vosk-model-small-ru-0.22 -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -Xmx1024m -jar friend-assistant.jar
pause

