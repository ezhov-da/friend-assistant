package ru.ezhov.friendassistant.command

import mu.KotlinLogging
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger {}

class CommandDispatcher(private val commandRepository: CommandRepository) {
    private val executor = Executors.newFixedThreadPool(4)

    fun doCommand(text: String) {
        val runnableCommands = commandRepository
            .all()
            .filter { it.isApply(text) }
            .let {
                if (it.size > 1) {
                    logger.warn {
                        "More than one command found for the text '$text'. ${it.joinToString { c -> c.name() }} "
                    }
                }
                it
            }
            .map { command ->
                Runnable { command.handle(text) }
            }
        runnableCommands.forEach { executor.submit(it) }
    }
}
