package pt.uevora.spacehub.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.uevora.spacehub.R
import pt.uevora.spacehub.data.model.ApodDto
import pt.uevora.spacehub.ui.state.UiState

/**
 * Displays the correct APOD UI according to the current loading state.
 */
@Composable
fun ApodContent(
    apodUiState: UiState<ApodDto>,
    showFullExplanation: Boolean,
    onShowFullExplanationChange: (Boolean) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (apodUiState) {
        UiState.Loading -> LoadingState()
        UiState.Empty -> ErrorState(
            message = stringResource(R.string.unknown_error),
            onRetry = onRetry
        )
        is UiState.Error -> ErrorState(
            message = apodUiState.message,
            onRetry = onRetry
        )
        is UiState.Success -> ApodCard(
            apodDto = apodUiState.data,
            showFullExplanation = showFullExplanation,
            onShowFullExplanationChange = onShowFullExplanationChange,
            modifier = modifier
        )
    }
}