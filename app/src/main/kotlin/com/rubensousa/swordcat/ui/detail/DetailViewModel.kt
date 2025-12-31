package com.rubensousa.swordcat.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.ui.StringResource
import com.rubensousa.swordcat.ui.image.ImageReference
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
class DetailViewModel @AssistedInject constructor(
    @Assisted private val catId: String,
    private val repository: CatRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val uiState = MutableStateFlow<DetailScreenState>(DetailScreenState.Loading)

    init {
        loadCat()
    }

    fun getUiState() = uiState.asStateFlow()

    private fun loadCat() {
        viewModelScope.launch(dispatcher) {
            repository.getCat(catId)
                .onSuccess { cat ->
                    uiState.update { createContentState(cat) }
                }
                .onFailure {
                    uiState.update {
                        DetailScreenState.Error(StringResource.fromId(R.string.error_generic))
                    }
                }
        }
    }

    private suspend fun createContentState(cat: Cat): DetailScreenState.Content {
        val infoItems = createInfoItems(cat)
        return DetailScreenState.Content(
            breedName = cat.breedName,
            infoItems = infoItems,
            imageReference = cat.imageId?.let { ImageReference(it) },
            isFavorite = repository.observeFavoriteState(cat.id)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = repository.isFavorite(cat.id)
                ),
            onFavoriteClick = {
                viewModelScope.launch {
                    repository.toggleFavorite(cat.id)
                }
            }
        )
    }

    private fun createInfoItems(cat: Cat): ImmutableList<DetailInfoItem> {
        return persistentListOf(
            DetailInfoItem(
                label = StringResource.fromId(R.string.label_breed),
                value = cat.breedName
            ),
            DetailInfoItem(
                label = StringResource.fromId(R.string.label_description),
                value = cat.description
            ),
            DetailInfoItem(
                label = StringResource.fromId(R.string.label_origin),
                value = cat.origin
            ),
            DetailInfoItem(
                label = StringResource.fromId(R.string.label_temperament),
                value = cat.temperament
            ),
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(catId: String): DetailViewModel
    }

}
