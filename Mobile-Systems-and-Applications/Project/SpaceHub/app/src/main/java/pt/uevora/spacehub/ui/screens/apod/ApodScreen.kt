package pt.uevora.spacehub.ui.screens.apod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.uevora.spacehub.data.model.ApodDto
import pt.uevora.spacehub.ui.components.ApodActions
import pt.uevora.spacehub.ui.components.ApodContent
import pt.uevora.spacehub.ui.state.UiState
import pt.uevora.spacehub.ui.theme.SpaceHubTheme
import pt.uevora.spacehub.ui.util.toIsoDate


/**
 * Displays the APOD screen with date controls and APOD content.
 */
@Composable
fun ApodScreen(
    apodUiState: UiState<ApodDto>,
    onDateSelected: (String) -> Unit,
    onRandomClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedDate by rememberSaveable { mutableStateOf(System.currentTimeMillis().toIsoDate()) }
    var showFullExplanation by rememberSaveable { mutableStateOf(true) }

    // Update date to the date that is returned by the API
    LaunchedEffect(apodUiState) {
        if (apodUiState is UiState.Success) {
            selectedDate = apodUiState.data.date
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ApodActions(
            dateText = selectedDate,
            onDateTextChange = { newDate -> selectedDate = newDate },
            onSearchClick = { onDateSelected(selectedDate) },
            onRandomClick = onRandomClick,
            modifier = Modifier,
        )

        ApodContent(
            apodUiState = apodUiState,
            onRetry = onRetry,
            showFullExplanation = showFullExplanation,
            onShowFullExplanationChange = { showFullExplanation = it },
            modifier = Modifier,
        )
    }
}


/**
 * Preview for the APOD screen on a tablet device.
 */
@Preview(showBackground = true, device = "id:pixel_tablet")
@Composable
private fun ApodScreenPreview() {
    SpaceHubTheme {
        ApodScreen(
            apodUiState = UiState.Success(
                ApodDto(
                    date = "2026-05-21",
                    explanation = "A preview explanation for the astronomy picture of the day.",
                    mediaType = "image",
                    title = "The Milky Way",
                    url = ""
                )
            ),
            onDateSelected = {},
            onRandomClick = {},
            onRetry = {}
        )
    }
}
