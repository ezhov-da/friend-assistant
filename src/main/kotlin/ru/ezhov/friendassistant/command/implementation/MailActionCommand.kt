package ru.ezhov.friendassistant.command.implementation

import ru.ezhov.friendassistant.command.Command

class MailActionCommand : Command {
    override fun name(): String = "Открыть почту"

    override fun description(): String = "Необходимо сказать 'почта'"

    override fun isApply(text: String): Boolean = text.startsWith("почта")

    override fun handle(text: String) {
        RocketActionHttpClient().send(
            id = "e64910a1-89df-4c72-b327-43b409d35b9d",
            commandName = "openUrl",
            arguments = emptyMap()
        )
    }
}
