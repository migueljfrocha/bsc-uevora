package pt.uevora.spacehub

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pt.uevora.spacehub.data.model.NasaImage
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchUiState

class ImageSearchStateTest {

    /**
     * Tests if the hasMore object returns true when there
     * are more search results to load.
     */
    @Test
    fun hasMore_returnsTrueWhenItemsAreLessThanTotalHits() {
        val fakeImage = NasaImage(
            nasaId = "nasa-fake-image",
            title = "Image Name",
            dateCreated = "2026-05-23",
            center = "Somewhere-at-NASA",
            description = "Some description",
            mediaType = "image",
            photographer = "Someone",
            location = null,
            thumbUrl = null,
            imageUrl = null
        )

        val state = ImageSearchUiState(
            items = listOf(fakeImage),
            totalHits = 2
        )

        assertTrue(state.hasMore)

    }


    /**
     * Tests if the hasMore object returns false if there
     * are no more search results to load.
     */
    @Test
    fun hasMore_returnsFalseWhenAllItemsAreLoaded() {
        val state = ImageSearchUiState(
            items = emptyList(),
            totalHits = 0
        )

        assertFalse(state.hasMore)
    }
}