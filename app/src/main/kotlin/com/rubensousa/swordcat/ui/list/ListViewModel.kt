package com.rubensousa.swordcat.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest
import com.rubensousa.swordcat.ui.EventSink
import com.rubensousa.swordcat.ui.StringResource
import com.rubensousa.swordcat.ui.cat.CatListItem
import com.rubensousa.swordcat.ui.image.ImageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: CatRepository,
    private val favoriteSource: CatFavoriteLocalSource,
    private val listConfig: ListConfig,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val uiState = MutableStateFlow<ListScreenState>(ListScreenState.Loading)
    private val eventSink = EventSink<ListScreenEvent>()
    private val searchTextFlow = MutableSharedFlow<String>()
    private var allCatItems: ImmutableList<CatListItem> = persistentListOf()

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
            ).collectLatest { result ->
                result.onSuccess { cats ->
                    allCatItems = mapCatItems(cats)
                    setContentState(allCatItems)
                    observeSearchQuery()
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
    }

    private fun setContentState(items: ImmutableList<CatListItem>) {
        uiState.update {
            ListScreenState.Content(
                items = items,
                onSearchTextChanged = { text -> onSearchTextChanged(text) }
            )
        }
    }

    private fun onSearchTextChanged(text: String) {
        viewModelScope.launch {
            searchTextFlow.emit(text)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchTextFlow
                .debounce(listConfig.searchDebounceMs)
                .collectLatest { text ->
                    if (text.trim().isEmpty()) {
                        setContentState(allCatItems)
                    } else {
                        val newItems = search(text)
                        setContentState(newItems)
                    }
                }
        }
    }

    private suspend fun mapCatItems(cats: List<Cat>): ImmutableList<CatListItem> {
        return cats.map { cat ->
            CatListItem(
                breedName = cat.breedName,
                imageReference = cat.imageId?.let { ImageReference(it) },
                favoriteState = favoriteSource.observeIsFavorite(cat.id)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(),
                        initialValue = favoriteSource.isFavorite(cat.id)
                    ),
                onFavoriteClick = {
                    viewModelScope.launch {
                        favoriteSource.toggleFavorite(cat.id)
                    }
                },
                onClick = {
                    eventSink.push(ListScreenEvent.OpenDetail(cat.id))
                }
            )
        }.toImmutableList()
    }

    private fun search(text: String): ImmutableList<CatListItem> {
        // Local search since we have all the cats already
        return allCatItems.filter { item ->
            item.breedName.lowercase().contains(text.lowercase())
        }.toPersistentList()
    }

}
