package pt.uevora.spacehub.ui.screens.epic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import kotlinx.coroutines.delay
import pt.uevora.spacehub.R
import pt.uevora.spacehub.data.model.EpicImage
import pt.uevora.spacehub.ui.components.DatePickerField
import pt.uevora.spacehub.ui.components.EmptyState
import pt.uevora.spacehub.ui.components.ErrorState
import pt.uevora.spacehub.ui.components.LoadingState
import pt.uevora.spacehub.ui.state.UiState
import pt.uevora.spacehub.ui.theme.SpaceHubTheme
import java.util.*
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

/**
 * Displays the EPIC screen with mode controls, date selection, and image playback.
 */
@Composable
fun EpicScreen(
    uiState: EpicUiState,
    windowSize: WindowWidthSizeClass,
    onModeChange: (EpicMode) -> Unit,
    onDateSelected: (String) -> Unit,
    onIndexChange: (Int) -> Unit,
    onTogglePlayback: () -> Unit,
    onAdvanceFrame: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val contentModifier = if (windowSize == WindowWidthSizeClass.Compact) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.fillMaxWidth(0.70f)
        }

        EpicControls(
            uiState = uiState,
            onModeChange = onModeChange,
            onDateSelected = onDateSelected,
            modifier = contentModifier
        )

        when (val imagesState = uiState.imagesState) {
            UiState.Loading -> LoadingState(modifier = contentModifier)
            UiState.Empty -> EmptyState(
                message = stringResource(R.string.epic_no_images),
                modifier = contentModifier
            )
            is UiState.Error -> ErrorState(
                message = imagesState.message,
                onRetry = onRetry,
                modifier = contentModifier
            )
            is UiState.Success -> EpicImagePlayer(
                images = imagesState.data,
                currentIndex = uiState.currentIndex,
                isPlaying = uiState.isPlaying,
                onIndexChange = onIndexChange,
                onTogglePlayback = onTogglePlayback,
                onAdvanceFrame = onAdvanceFrame,
                modifier = contentModifier
            )
        }
    }
}


/**
 * Displays controls for selecting the EPIC mode and date.
 */
@Composable
private fun EpicControls(
    uiState: EpicUiState,
    onModeChange: (EpicMode) -> Unit,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dateText by rememberSaveable { mutableStateOf(uiState.selectedDate) }

    LaunchedEffect(uiState.selectedDate) {
        dateText = uiState.selectedDate
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.natural))
            Switch(
                checked = (uiState.mode == EpicMode.Enhanced),
                onCheckedChange = { isChecked ->
                    onModeChange(
                        if (isChecked)
                            EpicMode.Enhanced
                        else
                            EpicMode.Natural
                    )
                }
            )
            Text(stringResource(R.string.enhanced))
        }

        DatePickerField(
            dateText = dateText,
            onDateTextChange = { newDate -> dateText = newDate },
            onConfirm = { onDateSelected(dateText) },
            confirmButtonText = stringResource(R.string.confirm)
        )
    }
}

/**
 * Displays EPIC images with a slider and playback controls.
 */
@Composable
private fun EpicImagePlayer(
    images: List<EpicImage>,
    currentIndex: Int,
    isPlaying: Boolean,
    onIndexChange: (Int) -> Unit,
    onTogglePlayback: () -> Unit,
    onAdvanceFrame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (images.isEmpty()) {
        EmptyState(message = stringResource(R.string.epic_no_images))
        return
    }

    val context = LocalContext.current

    // Preload the images for the animation
    LaunchedEffect(images) {
        images.forEach { image ->
            context.imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .data(image.imageUrl)
                    .build()
            )
        }
    }

    LaunchedEffect(isPlaying, images.size) {
        while (isPlaying) {
            delay(700.milliseconds)
            onAdvanceFrame()
        }
    }

    val selectedImage = images[currentIndex.coerceIn(0, images.lastIndex)]
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(selectedImage.imageUrl)
                .build(),
            contentDescription = selectedImage.caption,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Text(
            text = selectedImage.caption,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = stringResource(R.string.image_index, currentIndex + 1, images.size),
            style = MaterialTheme.typography.labelLarge
        )

        if (images.size > 1) {
            Slider(
                value = currentIndex.toFloat(),
                onValueChange = { i ->
                    onIndexChange(i.roundToInt())
                },
                valueRange = 0f..images.lastIndex.toFloat(),
                steps = (images.size - 2).coerceAtLeast(0),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(onClick = onTogglePlayback) {
            Text(stringResource(if (isPlaying) R.string.pause else R.string.play))
        }
    }
}

/**
 * Preview for the EPIC screen.
 */
@Preview(showBackground = true)
@Composable
private fun EpicScreenPreview() {
    SpaceHubTheme {
        EpicScreen(
            uiState = EpicUiState(
                imagesState = UiState.Success(
                    listOf(
                        EpicImage(
                            image = "preview",
                            date = Date(),
                            caption = "Earth Polychromatic Imaging Camera preview.",
                            imageUrl = ""
                        )
                    )
                )
            ),
            windowSize = WindowWidthSizeClass.Compact,
            onModeChange = {},
            onDateSelected = {},
            onIndexChange = {},
            onTogglePlayback = {},
            onAdvanceFrame = {},
            onRetry = {}
        )
    }
}
