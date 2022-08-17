package ru.ezhov.friendassistant.presentation.ui

import ru.ezhov.friendassistant.command.CommandRepository
import javax.swing.JDialog

class AssistantFrame(name: String, commandRepository: CommandRepository) : JDialog() {
    init {
        isUndecorated = true
        isAlwaysOnTop = true
        val panel = AssistantPanel(name = name, commandRepository = commandRepository)
        MoveUtil.addMoveAction(this, panel)
        setLocationRelativeTo(null)
        add(panel)
        pack()
    }
}
