package pt.uevora.spacehub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.uevora.spacehub.data.model.ApodDto
import pt.uevora.spacehub.data.repository.NasaRepository
import pt.uevora.spacehub.ui.state.UiState

/**
 * Manages the home screen state and loads today's APOD.
 */
class HomeViewModel(
    private val nasaRepository: NasaRepository,
) : ViewModel() {
    private val _todayApodUiState = MutableStateFlow<UiState<ApodDto>>(UiState.Loading)
    val todayApodUiState: StateFlow<UiState<ApodDto>> = _todayApodUiState.asStateFlow()

    // Loads today's APOD when the ViewModel is created.
    init {
        loadTodayApod()
    }

    /**
     * Loads today's APOD and updates the home UI state.
     */
    fun loadTodayApod() {
        viewModelScope.launch {
            _todayApodUiState.value = UiState.Loading

            try {
                val apod = nasaRepository.getTodayApod()
                _todayApodUiState.value = UiState.Success(apod)
            } catch (e: Exception) {
                _todayApodUiState.value = UiState.Error(e.message)
            }
        }
    }
}