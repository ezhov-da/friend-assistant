package ru.ezhov.friendassistant.presentation.ui

import ru.ezhov.friendassistant.service.PlayAudioService
import javax.swing.JDialog

class AssistantFrame(name: String, playAudioService: PlayAudioService) : JDialog() {
    init {
        isUndecorated = true
        isAlwaysOnTop = true
        val panel = AssistantPanel(name = name, playAudioService = playAudioService)
        MoveUtil.addMoveAction(this, panel)
        setLocationRelativeTo(null)
        add(panel)
        pack()
    }
}
