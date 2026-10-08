package pt.uevora.spacehub.ui.screens.imagesearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.uevora.spacehub.R
import pt.uevora.spacehub.data.model.NasaImage
import pt.uevora.spacehub.ui.components.EmptyState
import pt.uevora.spacehub.ui.components.ErrorState
import pt.uevora.spacehub.ui.components.LoadingState
import pt.uevora.spacehub.ui.components.NasaImageCard
import pt.uevora.spacehub.ui.theme.SpaceHubTheme

/**
 * Displays the NASA image library search screen.
 */
@Composable
fun ImageSearchScreen(
    uiState: ImageSearchUiState,
    windowSize: WindowWidthSizeClass,
    onQueryChange: (String) -> Unit,
    onPhotographerChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onFiltersExpandedChange: (Boolean) -> Unit,
    onGridModeChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onClearFilters: () -> Unit,
    onLoadMore: () -> Unit,
    onImageSelected: (NasaImage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search + Filters + Options
        LibraryControls(
            uiState = uiState,
            onQueryChange = onQueryChange,
            onPhotographerChange = onPhotographerChange,
            onLocationChange = onLocationChange,
            onFiltersExpandedChange = onFiltersExpandedChange,
            onGridModeChange = onGridModeChange,
            onSearch = onSearch,
            onClearFilters = onClearFilters
        )

        // Cards (Data)
        when {
            uiState.isLoading -> LoadingState(modifier = Modifier.weight(1f))
            uiState.errorMessage != null && uiState.items.isEmpty() -> ErrorState(
                message = uiState.errorMessage,
                onRetry = onSearch,
                modifier = Modifier.weight(1f)
            )
            uiState.items.isEmpty() && uiState.hasSearched -> EmptyState(
                message = stringResource(R.string.no_results),
                modifier = Modifier.weight(1f)
            )
            uiState.isGrid -> LibraryGrid(
                items = uiState.items,
                windowSize = windowSize,
                isLoadingMore = uiState.isLoadingMore,
                hasMore = uiState.hasMore,
                onLoadMore = onLoadMore,
                onImageSelected = onImageSelected,
                modifier = Modifier.weight(1f)
            )
            else -> LibraryList(
                items = uiState.items,
                isLoadingMore = uiState.isLoadingMore,
                hasMore = uiState.hasMore,
                onLoadMore = onLoadMore,
                onImageSelected = onImageSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Displays search, filter, and view mode controls.
 */
@Composable
private fun LibraryControls(
    uiState: ImageSearchUiState,
    onQueryChange: (String) -> Unit,
    onPhotographerChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onFiltersExpandedChange: (Boolean) -> Unit,
    onGridModeChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onClearFilters: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                label = { Text(stringResource(R.string.search_library_hint)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onSearch,
                modifier = Modifier
                    .height(56.dp)
                    .padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.search))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.filters),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { onFiltersExpandedChange(!uiState.filtersExpanded) }) {
                    Icon(
                        imageVector = if (uiState.filtersExpanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = stringResource(R.string.filters)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(R.string.list_view))
                Switch(
                    checked = uiState.isGrid,
                    onCheckedChange = onGridModeChange
                )
                Text(stringResource(R.string.grid_view))
            }
        }

        AnimatedVisibility(visible = uiState.filtersExpanded) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.photographer,
                    onValueChange = onPhotographerChange,
                    label = { Text(stringResource(R.string.photographer)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.location,
                    onValueChange = onLocationChange,
                    label = { Text(stringResource(R.string.location)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onSearch) {
                        Text(stringResource(R.string.apply_filters))
                    }
                    OutlinedButton(onClick = onClearFilters) {
                        Text(stringResource(R.string.clear_filters))
                    }
                }
            }
        }
    }
}

/**
 * Displays image search results in a vertical list.
 */
@Composable
private fun LibraryList(
    items: List<NasaImage>,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    onImageSelected: (NasaImage) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { image -> image.nasaId }) { image ->
            NasaImageCard(
                image = image,
                isGrid = false,
                onClick = { onImageSelected(image) }
            )
        }
        item {
            LoadMoreButton(
                isLoadingMore = isLoadingMore,
                hasMore = hasMore,
                onLoadMore = onLoadMore
            )
        }
    }
}

/**
 * Displays image search results in an adaptive grid.
 */
@Composable
private fun LibraryGrid(
    items: List<NasaImage>,
    windowSize: WindowWidthSizeClass,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    onImageSelected: (NasaImage) -> Unit,
    modifier: Modifier = Modifier,
) {
    val minCellSize = if (windowSize == WindowWidthSizeClass.Compact) 160.dp else 220.dp

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minCellSize),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { image -> image.nasaId }) { image ->
            NasaImageCard(
                image = image,
                isGrid = true,
                onClick = { onImageSelected(image) }
            )
        }
        item (span = { GridItemSpan(maxLineSpan) }) {
            LoadMoreButton(
                isLoadingMore = isLoadingMore,
                hasMore = hasMore,
                onLoadMore = onLoadMore
            )
        }
    }
}

/**
 * Displays loading or load-more controls for paginated results.
 */
@Composable
private fun LoadMoreButton(
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
) {
    if (isLoadingMore) {
        LoadingState()
    } else if (hasMore) {
        Button(
            onClick = onLoadMore,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(stringResource(R.string.show_more))
        }
    }
}

/**
 * Preview for the image search screen.
 */
@Preview(showBackground = true)
@Composable
private fun ImageSearchScreenPreview() {
    SpaceHubTheme {
        ImageSearchScreen(
            uiState = ImageSearchUiState(
                items = listOf(
                    NasaImage(
                        nasaId = "preview",
                        title = "Earth from orbit",
                        dateCreated = "2026-05-21",
                        center = "NASA",
                        description = "A preview search result from the NASA image library.",
                        mediaType = "image",
                        photographer = null,
                        location = "Low Earth orbit",
                        thumbUrl = null,
                        imageUrl = null
                    )
                ),
                totalHits = 1,
                hasSearched = true
            ),
            windowSize = WindowWidthSizeClass.Compact,
            onQueryChange = {},
            onPhotographerChange = {},
            onLocationChange = {},
            onFiltersExpandedChange = {},
            onGridModeChange = {},
            onSearch = {},
            onClearFilters = {},
            onLoadMore = {},
            onImageSelected = {}
        )
    }
}