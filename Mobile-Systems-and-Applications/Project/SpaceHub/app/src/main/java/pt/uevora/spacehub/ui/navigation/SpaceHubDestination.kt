package pt.uevora.spacehub.ui.navigation

import pt.uevora.spacehub.R

/**
 * Represents one navigation destination in the app.
 */
data class Destination(
    val route: String,
    val title: Int,
    val icon: Int = 0,
)

/**
 * Defines all routes used by the SpaceHub navigation graph.
 */
object SpaceHubDestination {
    val Home = Destination("home", R.string.destination_home, R.drawable.ic_home)
    val Apod = Destination("apod", R.string.destination_apod, R.drawable.ic_calendar)
    val Library = Destination("library", R.string.destination_library, R.drawable.ic_search)
    val Epic = Destination("epic", R.string.destination_epic, R.drawable.ic_globe)
    val ImageDetail = Destination("image-detail", R.string.destination_image_detail)
}

/**
 * Destinations shown in the main navigation components.
 */
val mainDestinations = listOf(
    SpaceHubDestination.Home,
    SpaceHubDestination.Apod,
    SpaceHubDestination.Library,
    SpaceHubDestination.Epic,
)

/**
 * Returns the title resource associated with a route.
 */
fun titleForRoute(route: String?): Int {
    return when (route) {
        SpaceHubDestination.Home.route -> R.string.app_name
        SpaceHubDestination.Apod.route -> SpaceHubDestination.Apod.title
        SpaceHubDestination.Library.route -> SpaceHubDestination.Library.title
        SpaceHubDestination.Epic.route -> SpaceHubDestination.Epic.title
        SpaceHubDestination.ImageDetail.route -> SpaceHubDestination.ImageDetail.title
        else -> SpaceHubDestination.Home.title
    }
}

/**
 * Returns the main navigation route that should be selected for a route.
 */
fun selectedMainRoute(route: String?): String {
    return when (route) {
        SpaceHubDestination.ImageDetail.route -> SpaceHubDestination.Library.route
        SpaceHubDestination.Home.route -> SpaceHubDestination.Home.route
        SpaceHubDestination.Apod.route -> SpaceHubDestination.Apod.route
        SpaceHubDestination.Library.route -> SpaceHubDestination.Library.route
        SpaceHubDestination.Epic.route -> SpaceHubDestination.Epic.route
        else -> SpaceHubDestination.Home.route
    }
}