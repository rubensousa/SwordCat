package com.rubensousa.swordcat.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import com.rubensousa.swordcat.ui.EventSink
import com.rubensousa.swordcat.ui.image.ImageReference
import com.rubensousa.swordcat.ui.list.ListScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    favoriteSource: CatFavoriteLocalSource,
) : ViewModel() {

    private val eventSink = EventSink<ListScreenEvent>()

    val uiState: StateFlow<FavoriteScreenState> = favoriteSource.observeFavoriteCats()
        .map { cats ->
            FavoriteScreenState(
                items = mapCatItems(cats),
                averageLifespan = formatAverageLifespan(calculateAverageLifespan(cats))
            )
        }
        /**
         * Lazy flow to avoid changes outside this screen immediately affecting this
         */
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FavoriteScreenState(
                items = persistentListOf(),
                averageLifespan = "0.0"
            )
        )

    fun getEvents() = eventSink.source()

    private fun mapCatItems(cats: List<Cat>): ImmutableList<CatFavoriteItem> {
        return cats.map { cat ->
            CatFavoriteItem(
                breedName = cat.breedName,
                imageReference = cat.imageId?.let { imageId -> ImageReference(imageId) },
                onClick = {
                    eventSink.push(ListScreenEvent.OpenDetail(cat.id))
                }
            )
        }.toImmutableList()
    }

    private fun calculateAverageLifespan(cats: List<Cat>): Double {
        if (cats.isEmpty()) return 0.0
        return cats.map { cat -> cat.lifespan.first }.average()
    }

    private fun formatAverageLifespan(average: Double): String {
        return String.format(Locale.getDefault(), "%.1f", average)
    }
}
