package pt.uevora.spacehub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.uevora.spacehub.R
import pt.uevora.spacehub.ui.navigation.Destination


/**
 * Displays the bottom navigation bar used on compact screens.
 */
@Composable
fun SpaceBottomNavigationBar (
    currentRoute : String,
    items : List<Destination>,
    onNavigate : (String) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val containerColor = colorScheme.primaryContainer
    val contentColor = colorScheme.onPrimaryContainer
    val selectedContainerColor = colorScheme.primary
    val selectedContentColor = colorScheme.onPrimary
    val unselectedContentColor = contentColor.copy(alpha = 0.78f)

    NavigationBar (
        contentColor = contentColor,
        containerColor = containerColor,
    ) {
        items.forEach { item ->
            val label = stringResource(item.title)
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedContentColor,
                    indicatorColor = selectedContainerColor,
                    unselectedTextColor = unselectedContentColor,
                    unselectedIconColor = unselectedContentColor
                ),
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = label
                    )
                },
                label = {
                    Text(label)
                }
            )
        }
    }
}

/**
 * Displays the navigation rail used on medium-width screens.
 */
@Composable
fun SpaceNavigationRail(
    currentRoute: String,
    items: List<Destination>,
    onNavigate : (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val containerColor = colorScheme.primaryContainer
    val contentColor = colorScheme.onPrimaryContainer
    val selectedContainerColor = colorScheme.primary
    val selectedContentColor = colorScheme.onPrimary
    val unselectedContentColor = contentColor.copy(alpha = 0.78f)
    val logoRes = if (isSystemInDarkTheme()) {
        R.drawable.ic_logo_white
    } else {
        R.drawable.ic_logo_black
    }

    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Spacer(modifier = Modifier.size(12.dp))
        Image(
            painter = painterResource(logoRes),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(60.dp)
                .padding(12.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        items.forEach { item ->
            val label = stringResource(item.title)
            NavigationRailItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = selectedContentColor,
                    indicatorColor = selectedContainerColor,
                    unselectedIconColor = unselectedContentColor,
                    unselectedTextColor = unselectedContentColor
                ),
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = label
                    )
                },
                label = {
                    Text(label)
                }
            )
        }
    }
}

/**
 * Displays the permanent navigation drawer used on expanded screens.
 */
@Composable
fun SpacePermanentDrawerContent(
    currentRoute: String,
    items: List<Destination>,
    onNavigate : (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val containerColor = colorScheme.primaryContainer
    val contentColor = colorScheme.onPrimaryContainer
    val selectedContainerColor = colorScheme.primary
    val selectedContentColor = colorScheme.onPrimary
    val unselectedContentColor = contentColor.copy(alpha = 0.78f)

    PermanentDrawerSheet(
        modifier = modifier,
        drawerContainerColor = containerColor,
        drawerContentColor = contentColor
    ) {
        Spacer(
            modifier = Modifier.size(64.dp)
        )
        items.forEach { item ->
            val label = stringResource(item.title)

            NavigationDrawerItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    selectedContainerColor = selectedContainerColor,
                    selectedIconColor = selectedContentColor,
                    selectedTextColor = selectedContentColor,
                    unselectedIconColor = unselectedContentColor,
                    unselectedTextColor = unselectedContentColor
                ),
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = label
                    )
                },
                label = {
                    Text(label)
                }

            )

        }
    }
}
