package ru.ezhov.friendassistant.command.implementation

import mu.KotlinLogging
import ru.ezhov.friendassistant.command.Command

private val logger = KotlinLogging.logger {}

class BackupActionCommand : Command {
    override fun name(): String = "Сделать бэкап Rocket Action"

    override fun description(): String = "Необходимо сказать 'бэкап действий'"

    override fun isApply(text: String): Boolean = text.startsWith("бэкап действий")

    override fun handle(text: String) {
        RocketActionHttpClient().send(
            id = "2f359ee7-6bfb-4adf-84ab-cfaef73b8cff",
            commandName = "executeScript",
            arguments = emptyMap()
        )
    }
}
