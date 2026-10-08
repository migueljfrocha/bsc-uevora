package pt.uevora.spacehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an Astronomy Picture of the Day response from the NASA APOD API.
 */
@Serializable
data class ApodDto(
    val date: String,
    val explanation: String,
    @SerialName(value = "hdurl")
    val hdUrl: String? = null,
    @SerialName(value = "media_type")
    val mediaType: String,
    val title: String,
    val url: String,
    @SerialName(value = "thumbnail_url")
    val thumbnailUrl: String? = null,
    val copyright: String? = null,
)
