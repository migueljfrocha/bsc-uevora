package pt.uevora.spacehub.ui.screens.epic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.uevora.spacehub.data.model.EpicImage
import pt.uevora.spacehub.data.repository.NasaRepository
import pt.uevora.spacehub.ui.state.UiState
import pt.uevora.spacehub.ui.util.todayIsoDate


/**
 * Represents the EPIC image mode selected by the user.
 */
enum class EpicMode {
    Natural,
    Enhanced
}

/**
 * Holds all UI state needed by the EPIC screen.
 */
data class EpicUiState(
    val selectedDate: String = todayIsoDate(),
    val mode: EpicMode = EpicMode.Natural,
    val imagesState: UiState<List<EpicImage>> = UiState.Loading,
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
)

/**
 * Manages EPIC image loading, mode selection, and playback state.
 */
class EpicViewModel (
    private val nasaRepository : NasaRepository,
) : ViewModel() {

    private val initialDate = "2026-05-05"
    private val _epicUiState = MutableStateFlow(
        EpicUiState(selectedDate = initialDate)
    )

    val epicUiState: StateFlow<EpicUiState> = _epicUiState.asStateFlow()

    /**
     * Loads the initial EPIC images when the ViewModel is created.
     */
    init {
        loadImages(date = initialDate)
    }

    /**
     * Changes the EPIC mode and reloads the images.
     */
    fun setMode(mode: EpicMode) {
        if (_epicUiState.value.mode == mode)
            return

        _epicUiState.update { currentState ->
            currentState.copy(
                mode = mode,
                currentIndex = 0,
                isPlaying = false
            )
        }
        loadImages(date = _epicUiState.value.selectedDate)
    }

    /**
     * Changes the selected date and reloads the images.
     */
    fun setDate(date: String) {
        _epicUiState.update { currentState ->
                currentState.copy(
                    selectedDate = date,
                    currentIndex = 0,
                    isPlaying = false,
                )
        }
        loadImages(date = date)
    }

    /**
     * Updates the selected image index within the available range.
     */
    fun setCurrentIndex(index: Int) {
        val state = _epicUiState.value.imagesState

        if (state is UiState.Success) {
            val images = state.data

            if (images.isNotEmpty()) {
                val newIndex = index.coerceIn(0, images.lastIndex)

                _epicUiState.update { currentState ->
                    currentState.copy(
                        currentIndex = newIndex
                    )
                }
            }
        }
    }

    /**
     * Starts or pauses EPIC image playback.
     */
    fun togglePlayback() {
        val state = _epicUiState.value.imagesState

        if(state is UiState.Success) {
            val images = state.data

            if(images.isNotEmpty()) {
                _epicUiState.update { currentState ->
                    currentState.copy(
                        isPlaying = !currentState.isPlaying,
                    )
                }
            }
        }
    }

    /**
     * Advances playback to the next image.
     */
    fun advanceFrame() {
        val state = _epicUiState.value.imagesState

        if (state is UiState.Success) {
            val images = state.data

            if (images.isNotEmpty()) {
                _epicUiState.update { currentState ->
                    currentState.copy(
                        currentIndex = (currentState.currentIndex + 1) % images.size
                    )
                }
            }
        }
    }

    /**
     * Reloads images for the current selected date.
     */
    fun retry() {
        loadImages(date = _epicUiState.value.selectedDate)
    }

    /**
     * Loads EPIC images and updates the UI state.
     */
    private fun loadImages(date: String) {
        viewModelScope.launch {
            val mode = _epicUiState.value.mode

            _epicUiState.update { currentState ->
                currentState.copy(
                    imagesState = UiState.Loading,
                    currentIndex = 0,
                    isPlaying = false,
                )
            }

            try {
                val images = getImages(mode, date)
                if (images.isNotEmpty()) {
                    _epicUiState.update { currentState ->
                        currentState.copy(
                            selectedDate = date,
                            imagesState = UiState.Success(images),
                        )
                    }
                } else {
                    _epicUiState.update { currentState ->
                        currentState.copy(
                            selectedDate = date,
                            imagesState = UiState.Empty,
                        )
                    }
                }
            } catch (e: Exception) {
                _epicUiState.update { currentState ->
                    currentState.copy(
                        imagesState = UiState.Error(e.message)
                    )
                }
            }
        }
    }

    /**
     * Gets images from the repository according to the selected mode.
     */
    private suspend fun getImages(mode: EpicMode, date: String): List<EpicImage> {
        return when (mode) {
            EpicMode.Natural -> nasaRepository.getEpicNaturalImages(date)
            EpicMode.Enhanced -> nasaRepository.getEpicEnhancedImages(date)
        }
    }
}