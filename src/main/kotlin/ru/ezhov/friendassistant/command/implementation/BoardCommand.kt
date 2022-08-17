package ru.ezhov.friendassistant.command.implementation

import ru.ezhov.friendassistant.command.Command

class BoardCommand : Command {
    override fun name(): String = "Открыть доску Jira"

    override fun description(): String = "Необходимо сказать 'доска'"

    override fun isApply(text: String): Boolean = text.startsWith("доска")

    override fun handle(text: String) {
        RocketActionHttpClient().send(
            id = "c35aa877-3a6a-4a30-8617-7c7c793db23a",
            commandName = "openUrl",
            arguments = emptyMap()
        )
    }
}
