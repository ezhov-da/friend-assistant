package ru.ezhov.friendassistant.service

import org.springframework.stereotype.Service
import ru.ezhov.friendassistant.App
import java.io.BufferedInputStream
import java.io.IOException
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException

@Service
class PlayAudioService {
    fun playCatchAudio() {
        try {
            val audioInputStream: AudioInputStream =
                AudioSystem.getAudioInputStream(BufferedInputStream(App::class.java.getResourceAsStream("/sony-7.wav")!!))
            val clip = AudioSystem.getClip()
            clip.addLineListener { event ->
                if (event.type == LineEvent.Type.STOP) {
                    clip.close()
                }
            }
            clip.open(audioInputStream)
            clip.framePosition = 0
            clip.start()

            try {
                // Блокируем поток до тех пор, пока клип не остановится
                clip.drain()
            } catch (ie: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
