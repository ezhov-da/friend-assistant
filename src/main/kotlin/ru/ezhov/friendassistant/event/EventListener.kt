package ru.ezhov.friendassistant.event

interface EventListener {
    fun <T: Event> handle(event: T)
}