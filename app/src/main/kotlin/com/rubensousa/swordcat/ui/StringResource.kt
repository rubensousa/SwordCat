package com.rubensousa.swordcat.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

/**
 * This avoids context dependencies in UI model mappers / ViewModels
 */
@Immutable
interface StringResource {

    fun get(context: Context): String

    companion object {
        fun fromId(id: Int, vararg args: Any): StringResource {
            return IdStringResource(id, args.toList())
        }
        fun fromString(text: String): StringResource {
            return StaticStringResource(text)
        }
    }

}


@Immutable
internal data class StaticStringResource(
    private val text: String
) : StringResource {
    override fun get(context: Context): String {
        return text
    }
}

@Immutable
internal data class IdStringResource(
    private val id: Int,
    private val args: List<Any>,
) : StringResource {
    override fun get(context: Context): String {
        return if (args.isEmpty()) {
            context.getString(id)
        } else {
            context.getString(id, *args.toTypedArray())
        }
    }
}

@Composable
@ReadOnlyComposable
fun StringResource.text(): String {
    return get(LocalContext.current)
}
