package ru.ezhov.friendassistant.command.implementation

import mu.KotlinLogging
import ru.ezhov.friendassistant.command.Command

private val logger = KotlinLogging.logger {}

class BackupPasswordCommand : Command {
    override fun name(): String = "Сделать бэкап паролей"

    override fun description(): String = "Необходимо сказать 'бэкап паролей'"

    override fun isApply(text: String): Boolean = text.startsWith("бэкап паролей")

    override fun handle(text: String) {
        RocketActionHttpClient().send(
            id = "95629ac7-c988-4799-9d0c-380eca2cae16",
            commandName = "executeScript",
            arguments = emptyMap()
        )
    }
}
