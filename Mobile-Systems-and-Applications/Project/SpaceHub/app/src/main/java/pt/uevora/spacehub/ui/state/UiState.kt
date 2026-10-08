package pt.uevora.spacehub.ui.state

/**
 * Represents the possible states of data shown in the UI.
 */
sealed interface UiState <out T> {

    /**
     * Indicates that data is being loaded.
     */
    data object Loading : UiState<Nothing>

    /**
     * Indicates that the request succeeded but returned no content.
     */
    data object Empty : UiState<Nothing>

    /**
     * Holds successfully loaded data.
     */
    data class Success<T>(val data: T) : UiState<T>

    /**
     * Holds an error message for a failed request.
     */
    data class Error(val message: String? = null) : UiState<Nothing>
}