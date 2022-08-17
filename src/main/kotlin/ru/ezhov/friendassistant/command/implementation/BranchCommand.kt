package ru.ezhov.friendassistant.command.implementation

import mu.KotlinLogging
import ru.ezhov.friendassistant.command.Command
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

class BranchCommand : Command {
    override fun name(): String = "Открыть ветку по задаче в Gitlab"

    override fun description(): String = "Необходимо сказать 'ветка' и произнести номер задачи по одной цифре"

    override fun isApply(text: String): Boolean = text.startsWith("ветка")

    override fun handle(text: String) {
        val list = text.split(" ".toRegex())
        if (list.size > 2) {
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
                id = "696d2b3d-38d8-4bbd-86e9-baf4d119d880",
                commandName = "openUrl",
                arguments = mutableMapOf("text" to number)
            )
        }
    }
}
