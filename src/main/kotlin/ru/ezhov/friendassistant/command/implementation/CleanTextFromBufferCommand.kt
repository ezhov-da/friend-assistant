package ru.ezhov.friendassistant.command.implementation

import ru.ezhov.friendassistant.command.Command

class CleanTextFromBufferCommand : Command {
    override fun name(): String = "Очистить текст из буфера"

    override fun description(): String = "Необходимо сказать 'очистить'"

    override fun isApply(text: String): Boolean = text.startsWith("очистить")

    override fun handle(text: String) {
        RocketActionHttpClient().executeAction(
            id = "d27aab5b-8e1b-4068-bacb-ca2b90b0dc45",
            text = "text",
        )
    }
}
