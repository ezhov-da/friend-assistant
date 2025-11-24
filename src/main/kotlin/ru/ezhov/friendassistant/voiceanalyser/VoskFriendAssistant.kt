package ru.ezhov.friendassistant.voiceanalyser

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import ru.ezhov.friendassistant.event.ObserverFactory
import ru.ezhov.friendassistant.event.SayNameEvent
import ru.ezhov.friendassistant.event.SayTextEvent
import ru.ezhov.friendassistant.event.StopSayNameEvent
import ru.ezhov.friendassistant.service.CommandService
import ru.ezhov.friendassistant.service.PlayAudioService
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

private val logger = KotlinLogging.logger {}
private const val PROPERTY_PATH_FILE = "vosk.model.folder.path"

class VoskFriendAssistant(
    friendName: String,
    private val commandService: CommandService,
    private val playAudioService: PlayAudioService,
) : FriendAssistant {
    private val friendName: String = friendName.lowercase()

    override fun start() {
        LibVosk.setLogLevel(LogLevel.DEBUG)

        val format = AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000f,
            16,
            2,
            4,
            44100f,
            false
        )
        val info = DataLine.Info(TargetDataLine::class.java, format)
        var microphone: TargetDataLine

        try {
            Model(System.getProperty(PROPERTY_PATH_FILE)).use { model ->
                Recognizer(model, 120000F).use { recognizer ->
                    microphone = AudioSystem.getLine(info) as TargetDataLine
                    microphone.open(format)
                    microphone.start()

                    var numBytesRead: Int
                    val CHUNK_SIZE = 1024
                    var bytesRead = 0
                    val b = ByteArray(4096)
                    while (true) {
                        numBytesRead = microphone.read(b, 0, CHUNK_SIZE)
                        bytesRead += numBytesRead
                        var lastValue = ""
                        if (recognizer.acceptWaveForm(b, numBytesRead)) {
                            val result: String = recognizer.getResult()
                            if ("" != result) {
                                val mapper = ObjectMapper()
                                lastValue = mapper.readTree(result).get("text").asText()
                            }
                        }
                        if ("" != lastValue) {
                            logger.debug { "text='$lastValue'" }
                        }
                        if (lastValue.startsWith(friendName)) {
                            logger.debug { "catch name '$friendName'" }

                            ObserverFactory.observer.fire(SayNameEvent())
                            logger.debug { "fire event 'say name'" }

                            ObserverFactory.observer.fire(SayTextEvent(lastValue))
                            logger.debug { "fire event 'say text'" }

                            commandService.doCommand(lastValue.substringAfter(" "))

                            ObserverFactory.observer.fire(StopSayNameEvent())
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
