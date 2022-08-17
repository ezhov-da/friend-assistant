package ru.ezhov.friendassistant.command

interface CommandRepository {
    fun all(): List<Command>
}
