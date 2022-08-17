package ru.ezhov.friendassistant.command.implementation

import mu.KotlinLogging
import ru.ezhov.friendassistant.command.Command
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

class JiraTaskCommand : Command {
    override fun name(): String = "Открыть задачу Jira"

    override fun description(): String = "Необходимо сказать 'задача' и произнести номер задачи по одной цифре"

    override fun isApply(text: String): Boolean = text.startsWith("задача")

    override fun handle(text: String) {
        val list = text.split(" ".toRegex())
        if (list.size > 1) {
            val words: List<String> = list.subList(2, list.size)
            val numbers: Map<String, String> = mapOf(
                "один" to "1",
                "два" to "2",
                "три" to "3",
                "четыре" to "4",
                "пять" to "5",
                "шесть" to "6",
                "семь" to "7",
                "восемь" to "8",
                "девять" to "9",
                "ноль" to "0",
            )
            val number = words.stream().map { key: String -> numbers[key] }.collect(Collectors.joining(""))

            RocketActionHttpClient().send(
                id = "64a9570c-6600-4635-99a5-425946f87424",
                commandName = "openUrl",
                arguments = mutableMapOf("text" to number)
            )
        }
    }
}
