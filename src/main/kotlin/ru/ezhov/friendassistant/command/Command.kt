package ru.ezhov.friendassistant.command

/**
 * Команда для выполнения
 */
interface Command {
    /**
     * Описание предназначения команды
     */
    fun name(): String

    /**
     * Описание предназначения команды
     */
    fun description(): String

    /**
     * Нужно ли выполнить эту команду
     *
     * true - нужно
     */
    fun isApply(text: String): Boolean

    /**
     * Обработчик, который будет выполнен если [isApply] вернёт true
     */
    fun handle(text: String)
}
