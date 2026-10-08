package pt.uevora.spacehub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.uevora.spacehub.R

/**
 * Displays the app top bar with either the logo or a back button (depending on the screen).
 */
@ExperimentalMaterial3Api
@Composable
fun SpaceTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {

    val logoRes = if (isSystemInDarkTheme()) {
        R.drawable.ic_logo_white
    } else {
        R.drawable.ic_logo_black
    }
    val appBarContainerColor = MaterialTheme.colorScheme.primaryContainer
    val appBarContentColor = MaterialTheme.colorScheme.onPrimaryContainer

    if (!canNavigateBack) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = appBarContainerColor,
                    titleContentColor = appBarContentColor,
                    navigationIconContentColor = appBarContentColor,
                    actionIconContentColor = appBarContentColor,
            ),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(logoRes),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(title)
                }
            },
            modifier = modifier
        )
    } else {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = appBarContainerColor,
                titleContentColor = appBarContentColor,
                navigationIconContentColor = appBarContentColor,
                actionIconContentColor = appBarContentColor,
            ),
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBack ?: {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
            modifier = modifier
        )
    }
}

/**
 * Preview for the top app bar without back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SpaceTopAppBarPreview_Home() {
    SpaceTopAppBar(
        title = "SpaceHub",
        canNavigateBack = false,
        onBack = null
    )
}

/**
 * Preview for the top app bar with back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SpaceTopAppBarPreview_Details() {
    SpaceTopAppBar(
        title = "Details",
        canNavigateBack = true,
        onBack = {}
    )
}
