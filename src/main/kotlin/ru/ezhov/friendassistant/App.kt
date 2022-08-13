package ru.ezhov.friendassistant

import ru.ezhov.friendassistant.infrastructure.FriendAssistant
import ru.ezhov.friendassistant.presentation.ui.AssistantFrame
import javax.swing.SwingUtilities

fun main() {
    Thread { FriendAssistant().start() }.apply {
        isDaemon = true
        start()
    }
    SwingUtilities.invokeLater {
        AssistantFrame("Джарвис").isVisible = true
    }
}