package ru.ezhov.friendassistant

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.stereotype.Component
import ru.ezhov.friendassistant.presentation.ui.AssistantFrame
import ru.ezhov.friendassistant.service.CommandService
import ru.ezhov.friendassistant.service.PlayAudioService
import ru.ezhov.friendassistant.voiceanalyser.FriendAssistant
import ru.ezhov.friendassistant.voiceanalyser.VoskFriendAssistant
import javax.swing.SwingUtilities


@SpringBootApplication
class App

fun main(args: Array<String>) {
//    runApplication<App>(*args)
    val builder = SpringApplicationBuilder(App::class.java)
    builder.headless(false)
    builder.run(*args)
}

@Component
class Main(
    @Value("\${friend.name}")
    private val friendName: String,
    private val commandService: CommandService,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val playAudioService = PlayAudioService()
        val friendAssistant: FriendAssistant = VoskFriendAssistant(
            friendName = friendName,
            commandService = commandService,
            playAudioService = playAudioService,
        )

        Thread { friendAssistant.start() }.apply {
            isDaemon = true
            start()
        }

        SwingUtilities.invokeLater {
            AssistantFrame(
                name = friendName,
                playAudioService = playAudioService
            ).isVisible = true
        }
    }
}
