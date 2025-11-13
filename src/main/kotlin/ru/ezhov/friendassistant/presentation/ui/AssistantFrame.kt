package ru.ezhov.friendassistant.presentation.ui

import javax.swing.JDialog

class AssistantFrame(name: String) : JDialog() {
    init {
        isUndecorated = true
        isAlwaysOnTop = true
        val panel = AssistantPanel(name = name)
        MoveUtil.addMoveAction(this, panel)
        setLocationRelativeTo(null)
        add(panel)
        pack()
    }
}
