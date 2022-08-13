package ru.ezhov.friendassistant.event

interface Observer {
    fun register(listener: EventListener)

    fun <T : Event> fire(event: T)
}