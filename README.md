# friend-assistant

## Vosk

https://alphacephei.com/vosk/

Set `-Dvosk.model.folder.path` folder https://alphacephei.com/vosk/models.

Example:

```shell
-Dvosk.model.folder.path=./vosk-model-small-ru-0.22
```

See [run-example.bat](run-example.bat) for run.

## Logger

```
-Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

## Build

```shell
gradlew clean build
```

## Errors

If an error occurs when launching from the console, there is most likely an access problem.

```java
javax.sound.sampled.LineUnavailableException: line with format PCM_SIGNED 60000.0 Hz, 16 bit, stereo, 4 bytes/frame, 44100.0 frames/second, little-endian not supported.
        at java.desktop/com.sun.media.sound.DirectAudioDevice$DirectDL.implOpen(DirectAudioDevice.java:503)
        at java.desktop/com.sun.media.sound.AbstractDataLine.open(AbstractDataLine.java:119)
        at java.desktop/com.sun.media.sound.AbstractDataLine.open(AbstractDataLine.java:150)
        at ru.ezhov.friendassistant.voiceanalyser.VoskFriendAssistant.start(VoskFriendAssistant.kt:48)
        at ru.ezhov.friendassistant.AppKt.main$lambda-0(App.kt:19)
        at java.base/java.lang.Thread.run(Thread.java:829)
```

Try launching the console and calling the launch script from it.
