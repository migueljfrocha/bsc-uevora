package pt.uevora.spacehub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import pt.uevora.spacehub.data.model.NasaImage

/**
 * Displays a NASA image card in either grid or list mode.
 */
@Composable
fun NasaImageCard(
    image : NasaImage,
    isGrid : Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isGrid) {
        GridImageCard(
            image = image,
            onClick = onClick,
            modifier = modifier,
        )
    }
    else {
        ListImageCard(
            image = image,
            onClick = onClick,
            modifier = modifier,
        )
    }
}

/**
 * Displays a compact NASA image card for grid layouts.
 */
@Composable
private fun GridImageCard(
    image: NasaImage,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card (
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            ImagePreview (
                image = image,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
            Column (
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = image.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if(!image.dateCreated.isNullOrBlank()) {
                    Text(
                        text = image.dateCreated.take(10),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Displays a wider NASA image card with title, date, and description (List Style).
 */
@Composable
private fun ListImageCard(
    image: NasaImage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    ) {

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Image
            ImagePreview(
                image = image,
                modifier = Modifier.size(120.dp)
            )

            // Column with title + date + description
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = image.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                    if(!image.dateCreated.isNullOrBlank()) {
                    Text(
                        text = image.dateCreated.take(10),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!image.description.isNullOrBlank()) {
                    Text(
                        text = image.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Loads and displays the preview image for a NASA image result.
 */
@Composable
private fun ImagePreview(
    image: NasaImage,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest
            .Builder(context)
            .data(image.thumbUrl ?: image.imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = image.title,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}