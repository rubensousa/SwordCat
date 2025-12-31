package com.rubensousa.swordcat.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.LifecycleStartEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface EventSource<T> {

    companion object {

        private val emptySource = EmptyEventSource<Any>()

        fun <T> empty(): EventSource<T> {
            @Suppress("UNCHECKED_CAST")
            return emptySource as EventSource<T>
        }

        fun <T> emptyFlow(): EventSourceFlow<T> {
            @Suppress("UNCHECKED_CAST")
            return MutableStateFlow(emptySource as EventSource<T>)
        }

    }

    fun consume(action: (event: T) -> Unit)

    private class EmptyEventSource<T> : EventSource<T> {
        override fun consume(action: (event: T) -> Unit) {}
        override fun toString(): String = "Empty Source"
    }

}

typealias EventSourceFlow<T> = StateFlow<EventSource<T>>

internal data class EventUpdateSource<T>(
    private val events: ArrayDeque<T>,
    private val onEventConsumed: () -> Unit,
) : EventSource<T> {

    private val iterator = events.iterator()

    override fun consume(action: (event: T) -> Unit) {
        while (iterator.hasNext()) {
            val event = iterator.next()
            action(event)
            iterator.remove()
            onEventConsumed()
        }
    }

}


@Composable
fun <T> ConsumeEvents(
    events: EventSourceFlow<T>,
    action: (event: T) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    LifecycleStartEffect(events) {
        val job = coroutineScope.launch(Dispatchers.Main.immediate) {
            events.collect { source ->
                source.consume(action)
            }
        }
        onStopOrDispose {
            job.cancel()
        }
    }
}
