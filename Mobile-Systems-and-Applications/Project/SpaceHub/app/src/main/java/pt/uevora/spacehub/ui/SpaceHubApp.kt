package pt.uevora.spacehub.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.uevora.spacehub.ui.components.SpaceBottomNavigationBar
import pt.uevora.spacehub.ui.components.SpaceNavigationRail
import pt.uevora.spacehub.ui.components.SpacePermanentDrawerContent
import pt.uevora.spacehub.ui.components.SpaceTopAppBar
import pt.uevora.spacehub.ui.navigation.SpaceHubDestination
import pt.uevora.spacehub.ui.navigation.mainDestinations
import pt.uevora.spacehub.ui.navigation.selectedMainRoute
import pt.uevora.spacehub.ui.navigation.titleForRoute
import pt.uevora.spacehub.ui.screens.apod.ApodScreen
import pt.uevora.spacehub.ui.screens.apod.ApodViewModel
import pt.uevora.spacehub.ui.screens.epic.EpicScreen
import pt.uevora.spacehub.ui.screens.epic.EpicViewModel
import pt.uevora.spacehub.ui.screens.home.HomeScreen
import pt.uevora.spacehub.ui.screens.home.HomeViewModel
import pt.uevora.spacehub.ui.screens.imagedetail.ImageDetailScreen
import pt.uevora.spacehub.ui.screens.imagedetail.ImageDetailViewModel
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchScreen
import pt.uevora.spacehub.ui.screens.imagesearch.ImageSearchViewModel
import pt.uevora.spacehub.ui.theme.SpaceHubTheme

/**
 * Enum class representing the different types of navigation available in the app.
 */
enum class SpaceNavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER
}


/**
 * Main entry point for the SpaceHub Compose UI.
 */
@Composable
fun SpaceHubApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    SpaceHubTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SpaceHubAppContent(
                windowSize = windowSize,
                navController = navController
            )
        }
    }
}

/**
 * Reads navigation state and prepares the main app layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpaceHubAppContent(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
) {
    val navigationType = getNavigationType(windowSize)

    // get current backstack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Default when app is opened for the first time (go to home)
    val currentRoute = backStackEntry?.destination?.route ?: SpaceHubDestination.Home.route

    // To highlight the current main route on the bottom nav bar
    val selectedRoute = selectedMainRoute(currentRoute)

    // The only screen that can navigate back so far is ImageDetail
    val canNavigateBack = currentRoute == SpaceHubDestination.ImageDetail.route

    SpaceHubMainLayout(
        navigationType = navigationType,
        currentRoute = currentRoute,
        selectedRoute = selectedRoute,
        canNavigateBack = canNavigateBack,
        windowSize = windowSize,
        navController = navController
    )
}

/**
 * Chooses between drawer and scaffold layouts based on navigation type.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpaceHubMainLayout(
    navigationType: SpaceNavigationType,
    currentRoute: String,
    selectedRoute: String,
    canNavigateBack: Boolean,
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
) {
    if (navigationType == SpaceNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                SpacePermanentDrawerContent(
                    currentRoute = selectedRoute,
                    items = mainDestinations,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }
        ) {
            SpaceHubScaffold(
                navigationType = navigationType,
                currentRoute = currentRoute,
                selectedRoute = selectedRoute,
                canNavigateBack = canNavigateBack,
                windowSize = windowSize,
                navController = navController
            )
        }
    } else {
        SpaceHubScaffold(
            navigationType = navigationType,
            currentRoute = currentRoute,
            selectedRoute = selectedRoute,
            canNavigateBack = canNavigateBack,
            windowSize = windowSize,
            navController = navController
        )
    }
}

/**
 * Displays the scaffold with top bar, optional bottom bar, and screen content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpaceHubScaffold(
    navigationType: SpaceNavigationType,
    currentRoute: String,
    selectedRoute: String,
    canNavigateBack: Boolean,
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            SpaceTopAppBar(
                title = stringResource(titleForRoute(currentRoute)),
                canNavigateBack = canNavigateBack,
                onBack = { navController.navigateUp() }
            )
        },
        bottomBar = {
            if (navigationType == SpaceNavigationType.BOTTOM_NAVIGATION) {
                SpaceBottomNavigationBar(
                    currentRoute = selectedRoute,
                    items = mainDestinations,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }
        }
    ) { innerPadding ->
        SpaceHubContent(
            navigationType = navigationType,
            selectedRoute = selectedRoute,
            windowSize = windowSize,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Displays adaptive navigation and the current screen content.
 */
@Composable
private fun SpaceHubContent(
    navigationType: SpaceNavigationType,
    selectedRoute: String,
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        if (navigationType == SpaceNavigationType.NAVIGATION_RAIL) {
            SpaceNavigationRail(
                currentRoute = selectedRoute,
                items = mainDestinations,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        SpaceHubNavHost(
            navController = navController,
            windowSize = windowSize,
            modifier = Modifier.weight(1f)
        )
    }
}


/**
 * Defines the navigation graph and connects screens to their ViewModels.
 */
@Composable
private fun SpaceHubNavHost(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val imageDetailViewModel: ImageDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = SpaceHubDestination.Home.route,
        modifier = modifier
    ) {
        composable(route = SpaceHubDestination.Home.route) {
            val viewModel: HomeViewModel =
                viewModel(factory = AppViewModelProvider.Factory)

            val uiState by viewModel.todayApodUiState.collectAsStateWithLifecycle()

            HomeScreen(
                todayApodUiState = uiState,
                windowSize = windowSize,
                onRetry = viewModel::loadTodayApod
            )
        }


        composable(route = SpaceHubDestination.Apod.route) {
            val viewModel: ApodViewModel =
                viewModel(factory = AppViewModelProvider.Factory)

            val uiState by viewModel.apodUiState.collectAsStateWithLifecycle()

            ApodScreen(
                apodUiState = uiState,
                onDateSelected = viewModel::loadByDate,
                onRandomClick = viewModel::loadRandom,
                onRetry = viewModel::loadToday
            )
        }

        composable(route = SpaceHubDestination.Library.route) {
            val viewModel: ImageSearchViewModel =
                viewModel(factory = AppViewModelProvider.Factory)

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ImageSearchScreen(
                uiState = uiState,
                windowSize = windowSize,
                onQueryChange = viewModel::updateQuery,
                onPhotographerChange = viewModel::updatePhotographer,
                onLocationChange = viewModel::updateLocation,
                onFiltersExpandedChange = viewModel::setFiltersExpanded,
                onGridModeChange = viewModel::setGridMode,
                onSearch = viewModel::search,
                onClearFilters = viewModel::clearFilters,
                onLoadMore = viewModel::loadMore,
                onImageSelected = { image ->
                    imageDetailViewModel.selectImage(image)
                    navController.navigate(SpaceHubDestination.ImageDetail.route)
                }
            )
        }

        composable(route = SpaceHubDestination.ImageDetail.route) {
            val selectedImage by imageDetailViewModel.selectedImage.collectAsStateWithLifecycle()
            ImageDetailScreen(
                image = selectedImage,
                windowSize = windowSize
            )
        }

        composable(route = SpaceHubDestination.Epic.route) {
            val viewModel: EpicViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val uiState by viewModel.epicUiState.collectAsStateWithLifecycle()
            EpicScreen(
                uiState = uiState,
                windowSize = windowSize,
                onModeChange = viewModel::setMode,
                onDateSelected = viewModel::setDate,
                onIndexChange = viewModel::setCurrentIndex,
                onTogglePlayback = viewModel::togglePlayback,
                onAdvanceFrame = viewModel::advanceFrame,
                onRetry = viewModel::retry
            )
        }
    }
}


/**
 * Selects the navigation layout according to the current window size.
 */
@Composable
private fun getNavigationType(
    windowSize: WindowWidthSizeClass
): SpaceNavigationType {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val usePhoneNavigation = with(density) {
        minOf(windowInfo.containerSize.width, windowInfo.containerSize.height).toDp() < 600.dp
    }

    return when {
        usePhoneNavigation -> SpaceNavigationType.BOTTOM_NAVIGATION
        windowSize == WindowWidthSizeClass.Compact -> SpaceNavigationType.BOTTOM_NAVIGATION
        windowSize == WindowWidthSizeClass.Medium -> SpaceNavigationType.NAVIGATION_RAIL
        windowSize == WindowWidthSizeClass.Expanded -> SpaceNavigationType.PERMANENT_NAVIGATION_DRAWER
        else -> SpaceNavigationType.BOTTOM_NAVIGATION
    }
}
