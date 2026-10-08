package pt.uevora.spacehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// API response models

/**
 * Represents the root response from the NASA Image Library search endpoint.
 */
@Serializable
data class ImageLibraryDTO (
    val collection: ImageCollectionDTO
)

/**
 * Contains the search results and metadata returned by the API.
 */
@Serializable
data class ImageCollectionDTO(
    val items: List<ImageItemDTO> = emptyList(),
    val metadata: ImageMetadataDTO? = null
)

/**
 * Contains metadata about the search result set.
 */
@Serializable
data class ImageMetadataDTO(
    @SerialName("total_hits")
    val totalHits: Int = 0
)

/**
 * Represents one item returned by the NASA Image Library.
 */
@Serializable
data class ImageItemDTO(
    val data: List<ImageItemDataDTO> = emptyList(),
    val links: List<ImageItemLinkDTO> = emptyList(),
)

/**
 * Contains the descriptive data for an image library item.
 */
@Serializable
data class ImageItemDataDTO(
    @SerialName(value = "nasa_id")
    val nasaId: String,
    val center: String? = null,
    @SerialName(value = "date_created")
    val dateCreated: String? = null,
    val description: String? = null,
    @SerialName(value = "media_type")
    val mediaType: String? = null,
    val title: String,
    val photographer: String? = null,
    val location: String? = null
)

/**
 * Contains a media link associated with an image library item.
 */
@Serializable
data class ImageItemLinkDTO(
    val href: String,
    val render: String? = null,
    val rel: String? = null,
    val size: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
)

// App models

/**
 * Represents a NASA image prepared for use in the app UI.
 */
data class NasaImage(
    val nasaId: String,
    val title: String,
    val dateCreated: String?,
    val center: String?,
    val description: String?,
    val mediaType: String?,
    val photographer: String?,
    val location: String?,
    val thumbUrl: String?,
    val imageUrl: String?
)

/**
 * Represents one page of NASA image library search results.
 */
data class NasaImagePage(
    val query: String,
    val page: Int,
    val totalHits: Int,
    val items: List<NasaImage>
)