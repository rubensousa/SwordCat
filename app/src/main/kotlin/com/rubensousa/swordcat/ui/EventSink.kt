package com.rubensousa.swordcat.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * I use this do dispatch events from ViewModels that need to be consumed once by the UI.
 * It avoids the ping-pong of resetting state flags
 * Check these:
 * - https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
 * - https://developer.android.com/topic/architecture/ui-layer/events#other-use-cases
 */
class EventSink<T> {

    private val queue = ArrayDeque<T>()
    private val currentSource = MutableStateFlow(EventSource.empty<T>())
    private val readOnlySource = currentSource.asStateFlow()
    private val onEventConsumed = {
        synchronized(queue) {
            queue.removeFirstOrNull()
        }
        Unit
    }

    fun <V : T> push(event: V) {
        synchronized(queue) {
            discardEventsOfType(requireNotNull(event)::class.java)
            queue.addLast(event)
            dispatchEventState(ArrayDeque(queue))
        }
    }

    fun <V : T> pushAll(events: List<V>) {
        synchronized(queue) {
            events.forEach { event ->
                discardEventsOfType(requireNotNull(event)::class.java)
                queue.addLast(event)
            }
            dispatchEventState(ArrayDeque(queue))
        }
    }

    fun source(): StateFlow<EventSource<T>> = readOnlySource

    private fun dispatchEventState(state: ArrayDeque<T>) {
        val eventSource = EventUpdateSource(state, onEventConsumed)
        currentSource.value = eventSource
    }

    private fun <V : T> discardEventsOfType(clazz: Class<V>) {
        queue.removeAll { event -> requireNotNull(event)::class.java == clazz }
    }

}
