package pt.uevora.spacehub.data.model

import kotlinx.serialization.Serializable
import java.util.*

/**
 * Represents an EPIC image response from the NASA EPIC API.
 */
@Serializable
data class EpicDto (
    val caption: String,
    val image: String,
    val date: String
)

/**
 * Represents an EPIC image prepared for display in the app.
 */
data class EpicImage(
    val image: String,
    val date: Date,
    val caption: String,
    val imageUrl: String
)