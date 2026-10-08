package pt.uevora.spacehub.ui.screens.imagesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.uevora.spacehub.data.model.NasaImage
import pt.uevora.spacehub.data.repository.NasaRepository

data class ImageSearchUiState(
    val query: String = "",
    val photographer: String = "",
    val location: String = "",
    val isGrid: Boolean = false,
    val filtersExpanded: Boolean = false,
    val items: List<NasaImage> = emptyList(),
    val page: Int = 0,
    val totalHits: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasSearched: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasMore: Boolean
        get() = items.isNotEmpty() && items.size < totalHits
}

/**
 * Manages image search filters, results, and pagination.
 */
class ImageSearchViewModel(
    private val nasaRepository: NasaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ImageSearchUiState())
    val uiState: StateFlow<ImageSearchUiState> = _uiState.asStateFlow()

    /**
     * Updates the search query text.
     */
    fun updateQuery(query: String) {
        _uiState.update { state -> state.copy(query = query) }
    }

    /**
     * Updates the photographer filter.
     */
    fun updatePhotographer(photographer: String) {
        _uiState.update { state -> state.copy(photographer = photographer) }
    }

    /**
     * Updates the location filter.
     */
    fun updateLocation(location: String) {
        _uiState.update { state -> state.copy(location = location) }
    }

    /**
     * Changes between list and grid display modes.
     */
    fun setGridMode(isGrid: Boolean) {
        _uiState.update { state -> state.copy(isGrid = isGrid) }
    }

    /**
     * Expands or collapses the filter controls.
     */
    fun setFiltersExpanded(expanded: Boolean) {
        _uiState.update { state -> state.copy(filtersExpanded = expanded) }
    }

    /**
     * Clears active filters and runs a new search.
     */
    fun clearFilters() {
        _uiState.update { state ->
            state.copy(
                photographer = "",
                location = "",
                filtersExpanded = false
            )
        }
        search()
    }

    /**
     * Runs a new image search from the first page.
     */
    fun search() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val query = currentState.query
            _uiState.value = currentState.copy(
                query = query,
                page = 1,
                items = emptyList(),
                totalHits = 0,
                isLoading = true,
                isLoadingMore = false,
                hasSearched = true,
                errorMessage = null,
            )

            _uiState.value = try {
                val page = nasaRepository.searchLibraryImages(
                    query = query,
                    page = 1,
                    photographer = currentState.photographer.ifBlank { null },
                    location = currentState.location.ifBlank { null },
                )
                _uiState.value.copy(
                    items = page.items,
                    totalHits = page.totalHits,
                    page = page.page,
                    isLoading = false,
                    filtersExpanded = false,
                )
            } catch (e: Exception) {
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message,
                )
            }
        }
    }

    /**
     * Loads the next page of image search results.
     */
    fun loadMore() {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isLoadingMore || !currentState.hasMore) return

        viewModelScope.launch {
            val nextPage = currentState.page + 1
            _uiState.value = currentState.copy(isLoadingMore = true, errorMessage = null)

            _uiState.value = try {
                val page = nasaRepository.searchLibraryImages(
                    query = currentState.query,
                    page = nextPage,
                    photographer = currentState.photographer.ifBlank { null },
                    location = currentState.location.ifBlank { null },
                )
                _uiState.value.copy(
                    items = _uiState.value.items + page.items,
                    totalHits = page.totalHits,
                    page = page.page,
                    isLoadingMore = false,
                )
            } catch (e: Exception) {
                _uiState.value.copy(
                    isLoadingMore = false,
                    errorMessage = e.message,
                )
            }
        }
    }
}