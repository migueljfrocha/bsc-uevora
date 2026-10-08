package pt.uevora.spacehub.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.uevora.spacehub.R
import pt.uevora.spacehub.data.model.ApodDto
import pt.uevora.spacehub.ui.components.ApodContent
import pt.uevora.spacehub.ui.state.UiState
import pt.uevora.spacehub.ui.theme.SpaceHubTheme

/**
 * Displays the home screen with today's APOD summary.
 */
@Composable
fun HomeScreen(
    todayApodUiState: UiState<ApodDto>,
    windowSize: WindowWidthSizeClass,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showFullExplanation by rememberSaveable { mutableStateOf(false) }
    val isWide = windowSize != WindowWidthSizeClass.Compact

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.today_apod),
            style = if (isWide) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineSmall
        )

        ApodContent (
            apodUiState = todayApodUiState,
            onRetry = onRetry,
            showFullExplanation = showFullExplanation,
            onShowFullExplanationChange = { showFullExplanation = it },
            modifier = Modifier
        )
    }
}

/**
 * Preview for the home screen.
 */
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    SpaceHubTheme {
        HomeScreen(
            todayApodUiState = UiState.Success(
                ApodDto(
                    date = "2026-05-22",
                    explanation = "This cosmic snapshot covers a field of view over twice as wide as the full Moon within the boundaries of the high-flying constellation Cygnus. Made using astronomical narrowband filters, the image highlights the bright edge of a ring-like nebula traced by the glow of ionized hydrogen and oxygen gas.",
                    mediaType = "image",
                    title = "The Nebulous Realm of WR 134",
                    url = ""
                )
            ),
            windowSize = WindowWidthSizeClass.Compact,
            onRetry = {}
        )
    }
}