package ru.ezhov.friendassistant

import ru.ezhov.friendassistant.command.CommandDispatcher
import ru.ezhov.friendassistant.command.implementation.InMemoryCommandRepository
import ru.ezhov.friendassistant.presentation.ui.AssistantFrame
import ru.ezhov.friendassistant.voiceanalyser.FriendAssistant
import ru.ezhov.friendassistant.voiceanalyser.VoskFriendAssistant
import javax.swing.SwingUtilities

fun main() {
    val friendName = "Джарвис"
    val commandRepository = InMemoryCommandRepository()
    val commandDispatcher = CommandDispatcher(commandRepository)
    val friendAssistant: FriendAssistant = VoskFriendAssistant(
        friendName = friendName,
        commandDispatcher = commandDispatcher
    )

    Thread { friendAssistant.start() }.apply {
        isDaemon = true
        start()
    }

    SwingUtilities.invokeLater {
        AssistantFrame(name = friendName, commandRepository = commandRepository).isVisible = true
    }
}
