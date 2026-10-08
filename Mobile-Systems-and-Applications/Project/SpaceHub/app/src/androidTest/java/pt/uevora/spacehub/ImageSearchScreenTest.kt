package pt.uevora.spacehub

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchScreen
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchUiState
import pt.uevora.spacehub.ui.theme.SpaceHubTheme

@RunWith(AndroidJUnit4::class)
class ImageSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    /**
     * Tests if there are nodes in the ImageSearchScreen wiith the following texts:
     * - Search NASA images
     * - Search
     */
    @Test
    fun imageSearchScreen_showsSearchControls() {
        composeTestRule.setContent {
            SpaceHubTheme {
                ImageSearchScreen(
                    uiState = ImageSearchUiState(),
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

        composeTestRule.onNodeWithText("Search NASA images").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }
}