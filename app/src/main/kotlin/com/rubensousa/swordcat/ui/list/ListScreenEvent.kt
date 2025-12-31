package com.rubensousa.swordcat.ui.list

sealed interface ListScreenEvent {
    data class OpenDetail(val id: String): ListScreenEvent
}
