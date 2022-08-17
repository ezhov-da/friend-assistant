package ru.ezhov.friendassistant.command.implementation

import ru.ezhov.friendassistant.command.Command

class ShowErrorsCommand : Command {
    override fun name(): String = "Показать ошибки PROD в Kibana"

    override fun description(): String = "Необходимо сказать 'покажи ошибки'"

    override fun isApply(text: String): Boolean = text.startsWith("покажи ошибки")

    override fun handle(text: String) {
        RocketActionHttpClient().send(
            id = "0d366248-7f72-4d40-b096-a6185fa9ead8",
            commandName = "openUrl",
            arguments = emptyMap()
        )
    }
}
