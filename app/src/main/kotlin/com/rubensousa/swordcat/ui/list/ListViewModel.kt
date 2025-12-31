package com.rubensousa.swordcat.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest
import com.rubensousa.swordcat.ui.EventSink
import com.rubensousa.swordcat.ui.StringResource
import com.rubensousa.swordcat.ui.image.ImageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: CatRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val uiState = MutableStateFlow<ListScreenState>(ListScreenState.Loading)
    private val eventSink = EventSink<ListScreenEvent>()

    init {
        loadCats()
    }

    fun getUiState() = uiState.asStateFlow()

    fun getEvents() = eventSink.source()

    private fun loadCats() {
        uiState.update { ListScreenState.Loading }
        viewModelScope.launch(dispatcher) {
            repository.loadCats(
                CatRequest(
                    limit = 100,
                    offset = 0
                )
            ).onSuccess { cats ->
                uiState.update {
                    ListScreenState.Content(
                        items = mapCatItems(cats),
                        onSearchTextChanged = { text -> onSearchTextChanged(text) }
                    )
                }
            }.onFailure { error ->
                Timber.e(error)
                uiState.update {
                    ListScreenState.Error(
                        message = StringResource.fromId(R.string.error_generic),
                        onRetryClick = {
                            loadCats()
                        }
                    )
                }
            }
        }
    }

    private fun onSearchTextChanged(text: String) {

    }

    private suspend fun mapCatItems(cats: List<Cat>): ImmutableList<CatListItem> {
        return cats.map { cat ->
            CatListItem(
                breedName = cat.breedName,
                imageReference = cat.imageId?.let { ImageReference(it) },
                favoriteState = repository.observeFavoriteState(cat.id)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(),
                        initialValue = repository.isFavorite(cat.id)
                    ),
                onFavoriteClick = {
                    viewModelScope.launch {
                        repository.toggleFavorite(cat.id)
                    }
                },
                onClick = {
                    eventSink.push(ListScreenEvent.OpenDetail(cat.id))
                }
            )
        }.toImmutableList()
    }

}