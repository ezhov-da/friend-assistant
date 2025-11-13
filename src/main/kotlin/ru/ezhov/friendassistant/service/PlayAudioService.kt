package ru.ezhov.friendassistant.service

import org.springframework.stereotype.Service
import ru.ezhov.friendassistant.App
import java.io.BufferedInputStream
import java.io.IOException
import java.util.concurrent.Executors
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException

@Service
class PlayAudioService {
    fun playCatchAudio() {
        Executors.newSingleThreadExecutor().submit {
            try {
                val audioInputStream: AudioInputStream =
                    AudioSystem.getAudioInputStream(BufferedInputStream(App::class.java.getResourceAsStream("/sony-7.wav")!!))
                val clip = AudioSystem.getClip()
                clip.open(audioInputStream)
                clip.start()

                Thread.sleep(clip.microsecondLength / 4000)
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
}
