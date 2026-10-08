package pt.uevora.spacehub.ui.screens.imagedetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import pt.uevora.spacehub.R
import pt.uevora.spacehub.data.model.NasaImage
import pt.uevora.spacehub.ui.components.EmptyState
import pt.uevora.spacehub.ui.theme.SpaceHubTheme
import pt.uevora.spacehub.ui.util.shareText

/**
 * Displays the selected NASA image with metadata, description, and share action.
 */
@Composable
fun ImageDetailScreen (
    image: NasaImage?,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {

    if(image == null) {
        EmptyState(
            message = stringResource(id = R.string.selected_image_missing),
            modifier = modifier.fillMaxSize()
        )
        return
    }

    val isWide = windowSize != WindowWidthSizeClass.Compact
    val context = LocalContext.current
    val imageModifier = Modifier
        .fillMaxWidth()
        .heightIn(
            min = if (isWide) 360.dp else 240.dp,
            max = if (isWide) 520.dp else 340.dp
        )

    Column(
        modifier = modifier
        .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data(image.imageUrl ?: image.thumbUrl)
                .crossfade(true)
                .build(),
            contentDescription = image.title,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )

        // Title
        Text(
            text = image.title,
            style = (if (isWide) MaterialTheme.typography.headlineMedium
                        else MaterialTheme.typography.bodyMedium)
        )

        // Date + center + photographer + location
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val metadataItems = listOfNotNull(
                image.dateCreated?.let {
                    stringResource(R.string.date) to it.take(10)
                },
                image.center?.let {
                    stringResource(R.string.center) to it
                },
                image.photographer?.let {
                    stringResource(R.string.photographer) to it
                },
                image.location?.let {
                    stringResource(R.string.location) to it
                }
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                metadataItems.forEach { metadataItem ->
                    MetadataRow(
                        label = metadataItem.first,
                        value = metadataItem.second
                    )
                }
            }

            val description = image.description
            if (!description.isNullOrEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.description),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = {
                    shareText(
                        context = context,
                        title = image.title,
                        text = "${image.title}\n${image.imageUrl ?: image.thumbUrl.orEmpty()}"
                    )
                }
            ) {
                Text(stringResource(id = R.string.share))
            }
        }
    }
}

/**
 * Displays one metadata label-value pair.
 */
@Composable
private fun MetadataRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Preview for the image detail screen.
 */
@Preview(showBackground = true)
@Composable
private fun ImageDetailScreenPreview() {
    SpaceHubTheme {
        ImageDetailScreen(
            image = NasaImage(
                nasaId = "preview",
                title = "Earth from orbit",
                dateCreated = "2026-05-21",
                center = "NASA",
                description = "A detailed NASA library result.",
                mediaType = "image",
                photographer = "NASA",
                location = "Orbit",
                thumbUrl = null,
                imageUrl = null
            ),
            windowSize = WindowWidthSizeClass.Compact
        )
    }
}