package pt.uevora.spacehub.ui.screens.apod

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
 * Manages the APOD screen state and loads APOD data from the repository.
 */
class ApodViewModel (
    private val nasaRepository: NasaRepository,
) : ViewModel() {
    private val _apodUiState = MutableStateFlow<UiState<ApodDto>>(UiState.Loading)
    val apodUiState : StateFlow<UiState<ApodDto>> = _apodUiState.asStateFlow()

    /**
     * Loads today's Astronomy Picture of the Day when the view model is initialized.
     */
    init {
        loadToday()
    }

    /**
     * Loads today's Astronomy Picture of the Day.
     */
    fun loadToday() {
        loadApod { nasaRepository.getTodayApod() }
    }

    /**
     * Loads the Astronomy Picture of the Day for the selected date.
     */
    fun loadByDate(date : String) {
        loadApod { nasaRepository.getApodByDate(date) }
    }

    /**
     * Loads a random Astronomy Picture of the Day.
     */
    fun loadRandom() {
        loadApod { nasaRepository.getRandomApod() }
    }

    /**
     * Runs an APOD request and updates the UI state.
     */
    private fun loadApod(block: suspend () -> ApodDto) {
        viewModelScope.launch {
            _apodUiState.value = UiState.Loading

            try {
                val result = block()
                _apodUiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _apodUiState.value = UiState.Error(e.message)
            }
        }
    }
}