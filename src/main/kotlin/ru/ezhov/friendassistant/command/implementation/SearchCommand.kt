package ru.ezhov.friendassistant.command.implementation

import mu.KotlinLogging
import ru.ezhov.friendassistant.command.Command

private val logger = KotlinLogging.logger {}

class SearchCommand : Command {
    override fun name(): String = "Поиск в Google"

    override fun description(): String = "Необходимо сказать 'поиск' и произнести то, что необходимо найти"

    override fun isApply(text: String): Boolean = text.startsWith("поиск")

    override fun handle(text: String) {
        val finalText = text.split(" ").let {
            it.subList(1, it.size)
        }.joinToString(separator = " ")

        RocketActionHttpClient().send(
            id = "87171b26-1796-432e-a9bc-82224a9b3686",
            commandName = "openUrl",
            arguments = mutableMapOf("text" to finalText)
        )
    }
}
