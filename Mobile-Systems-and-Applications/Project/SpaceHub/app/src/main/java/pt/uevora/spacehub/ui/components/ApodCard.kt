package pt.uevora.spacehub.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import pt.uevora.spacehub.R
import pt.uevora.spacehub.data.model.ApodDto
import pt.uevora.spacehub.ui.util.shareText


/**
 * Displays an APOD card with media, metadata, explanation, and sharing actions.
 */
@Composable
fun ApodCard (
    apodDto : ApodDto,
    showFullExplanation: Boolean,
    onShowFullExplanationChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    var fullScreenImageUrl by rememberSaveable { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title + Date
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = apodDto.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = apodDto.date,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Image
            ApodMedia(
                apodDto = apodDto,
                context = context,
                onImageClick = { imageUrl ->
                    fullScreenImageUrl = imageUrl
                }
            )

            // Copyright
            if(!apodDto.copyright.isNullOrBlank()){
                Text(
                    text = stringResource(R.string.copyright, apodDto.copyright),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Full explanation text + switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.show_full_explanation),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Switch(
                    checked = showFullExplanation,
                    onCheckedChange = onShowFullExplanationChange
                )
            }

            // Explanation text
            var maxLines = Int.MAX_VALUE
            if (!showFullExplanation) {
                maxLines = 4
            }

            Text(
                text = apodDto.explanation,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            // Share Button
            Button(
                onClick = {
                    shareText(
                        context = context,
                        title = apodDto.title,
                        text = "${apodDto.title}\n${apodDto.date}\n${apodDto.url}"
                    )
                }
            ) {
                Text(stringResource(R.string.share))
            }
        }
    }
    val imageUrl = fullScreenImageUrl
    if (!imageUrl.isNullOrBlank()) {
        FullScreenApodImage(
            imageUrl = imageUrl,
            contentDescription = apodDto.title,
            onDismiss = { fullScreenImageUrl = null }
        )
    }
}


/**
 * Displays either the APOD image or the video thumbnail and open-video action.
 */
@Composable
private fun ApodMedia(
    apodDto : ApodDto,
    context: Context,
    onImageClick: (String) -> Unit
) {

    if(apodDto.mediaType == "image"){
        val url = apodDto.hdUrl ?: apodDto.url
        AsyncImage(
            model = ImageRequest
                .Builder(context = context)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = apodDto.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clickable { onImageClick(url) }
        )
    }else{
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            if(apodDto.thumbnailUrl != null){
                AsyncImage(
                    model = ImageRequest
                        .Builder(context = context)
                        .data(apodDto.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = apodDto.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clickable { onImageClick(apodDto.thumbnailUrl) },
                )
            }else{
                Text(stringResource(R.string.no_video_thumbnail))
            }
            Button(onClick = { openUrl(context, apodDto.url) }) {
                Text(stringResource(R.string.open_video))
            }
        }
    }
}

/**
 * Shows the selected APOD image in a full-screen dialog.
 */
@Composable
private fun FullScreenApodImage(
    imageUrl: String,
    contentDescription: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box (
          modifier = Modifier
              .fillMaxSize()
              .background(Color.Black.copy(alpha = 0.2f)) // Transparent background
              .padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context = context)
                    .data(imageUrl)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onDismiss() }
            )

            Button(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(stringResource(R.string.close))
            }
        }
    }
}

/**
 * Displays APOD date search and random APOD actions.
 */
@Composable
fun ApodActions(
    dateText: String,
    onDateTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onRandomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DatePickerField(
            dateText = dateText,
            onDateTextChange = onDateTextChange,
            onConfirm = onSearchClick,
            confirmButtonText = stringResource(R.string.search)
        )
        Button(onClick = onRandomClick) {
            Text(stringResource(R.string.random_apod))
        }
    }
}

/**
 * Opens the provided URL in an external app.
 */
private fun openUrl(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}
