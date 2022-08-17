package ru.ezhov.friendassistant.presentation.ui

import mu.KotlinLogging
import ru.ezhov.friendassistant.command.CommandRepository
import ru.ezhov.friendassistant.event.Event
import ru.ezhov.friendassistant.event.EventListener
import ru.ezhov.friendassistant.event.ObserverFactory
import ru.ezhov.friendassistant.event.SayNameEvent
import ru.ezhov.friendassistant.event.SayTextEvent
import ru.ezhov.friendassistant.event.StopSayNameEvent
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Point
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ConcurrentLinkedDeque
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities

private val logger = KotlinLogging.logger {}

class AssistantPanel(name: String, commandRepository: CommandRepository) : JPanel() {
    private val labelIcon = JLabel().apply {
        icon = ImageIcon(this@AssistantPanel.javaClass.getResource("/icons8-microphone-32.png"))
        horizontalAlignment = SwingConstants.CENTER
        isOpaque = true
    }
    private val defaultBackgroundColor = labelIcon.background
    private val labelName = JLabel(name)

    init {
        layout = BorderLayout()
        border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        add(
            JMenuBar().apply {
                add(JMenu("...").also { menu ->
                    commandRepository.all().sortedBy { it.name() }.forEach { command ->
                        menu.add(JMenu(command.name()).also { menuCommand ->
                            menuCommand.add(JMenuItem(command.description()))
                        })
                    }
                }
                )
            },
            BorderLayout.NORTH
        )
        add(labelIcon, BorderLayout.CENTER)
        add(labelName, BorderLayout.SOUTH
        )

        val dialogQueue: ConcurrentLinkedDeque<JDialog> = ConcurrentLinkedDeque()
        ObserverFactory.observer.register(
            object : EventListener {
                override fun <T : Event> handle(event: T) {

                    when (event) {
                        is SayTextEvent -> {
                            logger.debug { "catch event ${SayTextEvent::class.simpleName}" }

                            SwingUtilities.invokeLater {
                                val dialog = createDialog(event.text)
                                dialog.isVisible = true
                                dialogQueue.addFirst(dialog)

                                labelIcon.background = Color.GREEN
                            }
                        }

                        is SayNameEvent -> {
                            logger.debug { "catch event ${SayNameEvent::class.simpleName}" }

                            SwingUtilities.invokeLater {
                                labelIcon.background = Color.GREEN
                            }
                        }

                        is StopSayNameEvent -> {
                            logger.debug { "catch event ${StopSayNameEvent::class.simpleName}" }

                            Timer(true)
                                .schedule(
                                    object : TimerTask() {
                                        override fun run() {
                                            SwingUtilities.invokeLater {
                                                labelIcon.background = defaultBackgroundColor
                                                dialogQueue.pollFirst()?.let {
                                                    it.dispose()
                                                }
                                            }
                                        }
                                    },
                                    2000L
                                )
                        }
                    }
                }
            }
        )
    }

    private fun createDialog(text: String): JDialog =
        JDialog().apply {
            val label = JLabel(text)
            isUndecorated = true
            isAlwaysOnTop = true
            add(label)
            pack()

            val dimensionDialog = size
            val locationPanel = this@AssistantPanel.locationOnScreen

            location = Point(locationPanel.x, locationPanel.y - dimensionDialog.height - 5)
        }
}
