package ru.ezhov.friendassistant.event

class InMemoryObserver : Observer {
    private val listeners: MutableSet<EventListener> = mutableSetOf()

    override fun register(listener: EventListener) {
        listeners.add(listener)
    }

    override fun <T : Event> fire(event: T) {
        listeners.forEach { it.handle(event) }
    }
}