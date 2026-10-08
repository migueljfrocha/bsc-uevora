package pt.uevora.spacehub

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pt.uevora.spacehub.data.model.ApodDto
import pt.uevora.spacehub.data.model.EpicImage
import pt.uevora.spacehub.data.model.NasaImagePage
import pt.uevora.spacehub.data.repository.NasaRepository
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchViewModel


/**
 * Unit tests that validate state updates in `ImageSearchViewModel`.
 *
 * Focuses on simple, synchronous assertions against `uiState` after update calls.
 */
class ImageSearchViewModelTest {

    /**
     * Verifies that `updateQuery` updates the `query` field in `uiState`.
     */
    @Test
    fun updateQuery_updatesUiStateQuery() {
        val viewModel = ImageSearchViewModel(FakeNasaRepository())

        viewModel.updateQuery("moon")

        assertEquals("moon", viewModel.uiState.value.query)
    }

    /**
     * Verifies that `setGridMode` updates the grid mode flag (`isGrid`) in `uiState`.
     */
    @Test
    fun setGridMode_updatesUiStateGridMode() {
        val viewModel = ImageSearchViewModel(FakeNasaRepository())

        viewModel.setGridMode(true)

        assertTrue(viewModel.uiState.value.isGrid)
    }
}

/**
 * Fake `NasaRepository` implementation to isolate `ImageSearchViewModel`
 * in unit tests. Unused methods throw errors to surface unexpected calls.
 */
private class FakeNasaRepository : NasaRepository {
    override suspend fun getTodayApod(): ApodDto = error("Not needed")
    override suspend fun getApodByDate(date: String): ApodDto = error("Not needed")
    override suspend fun getRandomApod(): ApodDto = error("Not needed")
    override suspend fun getEpicNaturalImages(date: String): List<EpicImage> = error("Not needed")
    override suspend fun getEpicEnhancedImages(date: String): List<EpicImage> = error("Not needed")

    override suspend fun searchLibraryImages(
        query: String,
        page: Int,
        photographer: String?,
        location: String?
    ): NasaImagePage = error("Not needed")
}