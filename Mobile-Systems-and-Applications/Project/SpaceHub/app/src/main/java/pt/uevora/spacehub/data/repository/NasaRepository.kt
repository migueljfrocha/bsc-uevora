package pt.uevora.spacehub.data.repository

import pt.uevora.spacehub.data.model.*
import pt.uevora.spacehub.data.network.ApodApiService
import pt.uevora.spacehub.data.network.EpicApiService
import pt.uevora.spacehub.data.network.ImageLibraryApiService
import java.text.SimpleDateFormat
import java.util.*

interface NasaRepository {
    /**
     * Gets today's Astronomy Picture of the Day.
     */
    suspend fun getTodayApod(): ApodDto

    /**
     * Gets the Astronomy Picture of the Day for a specific date.
     */
    suspend fun getApodByDate(date: String): ApodDto

    /**
     * Gets one random Astronomy Picture of the Day.
     */
    suspend fun getRandomApod(): ApodDto

    /**
     * Gets EPIC natural-color images for a specific date.
     */
    suspend fun getEpicNaturalImages(date: String): List<EpicImage>

    /**
     * Gets EPIC enhanced-color images for a specific date.
     */
    suspend fun getEpicEnhancedImages(date: String): List<EpicImage>

    /**
     * Searches NASA's image library using text and optional filters.
     */
    suspend fun searchLibraryImages(
        query: String,
        page: Int,
        photographer: String? = null,
        location: String? = null
    ): NasaImagePage
}


class NetworkNasaRepository (
    private val _apodApiService: ApodApiService,
    private val _imageLibraryApiService: ImageLibraryApiService,
    private val _epicApiService: EpicApiService,
    private val _apiKey: String
) : NasaRepository {


    /**
     * Fetches today's APOD from the APOD API.
     */
    override suspend fun getTodayApod(): ApodDto {

        return _apodApiService.getApod(
            apiKey = _apiKey,
            thumbs = "true"
        )
    }


    /**
     * Fetches the APOD for the provided date.
     */
    override suspend fun getApodByDate(date: String): ApodDto {

        return _apodApiService.getApod(
            apiKey = _apiKey,
            thumbs = "true",
            date = date
        )
    }


    /**
     * Fetches a single random APOD entry.
     */
    override suspend fun getRandomApod(): ApodDto {

        return _apodApiService.getRandomApod(
            apiKey = _apiKey,
            thumbs = "true",
            count = 1
        ).firstOrNull() ?: throw Exception("No APOD found")
    }

    /**
     * Parses an EPIC date string into a Date object.
     */
    private fun parseEpicDate(epicDto: EpicDto, sdf: SimpleDateFormat): Date {

        return sdf.parse(epicDto.date)
            ?: throw IllegalArgumentException("Invalid date: ${epicDto.date}")
    }

    /**
     * Formats an EPIC date into year, month, and day values.
     */
    private fun formatEpicDate(date: Date): Triple<String, String, String> {

        val year = SimpleDateFormat("yyyy", Locale.US).format(date)
        val month = SimpleDateFormat("MM", Locale.US).format(date)
        val day = SimpleDateFormat("dd", Locale.US).format(date)

        return Triple(year, month, day)
    }


    /**
     * Fetches natural-color EPIC images and builds their image URLs.
     */
    override suspend fun getEpicNaturalImages(date: String): List<EpicImage> {

        val naturalImages : List<EpicDto> = _epicApiService.getNaturalImages(date)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply{
            timeZone = TimeZone.getTimeZone("UTC")
        }

        return naturalImages.map { epicDto ->

            val parsedDate = parseEpicDate(epicDto, sdf)

            val (year, month, day) = formatEpicDate(parsedDate)

            EpicImage(
                image = epicDto.image,
                date = parsedDate,
                caption = epicDto.caption,
                imageUrl = "https://epic.gsfc.nasa.gov/archive/natural/$year/$month/$day/png/${epicDto.image}.png"
            )
        }
    }


    /**
     * Fetches enhanced-color EPIC images and builds their image URLs.
     */
    override suspend fun getEpicEnhancedImages(date: String): List<EpicImage> {
        val naturalImages : List<EpicDto> = _epicApiService.getEnhancedImages(date)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply{
            timeZone = TimeZone.getTimeZone("UTC")
        }

        return naturalImages.map { epicDto ->

            val parsedDate = parseEpicDate(epicDto, sdf)
            val (year, month, day) = formatEpicDate(parsedDate)

            EpicImage(
                image = epicDto.image,
                date = parsedDate,
                caption = epicDto.caption,
                imageUrl = "https://epic.gsfc.nasa.gov/archive/enhanced/$year/$month/$day/png/${epicDto.image}.png"
            )
        }
    }


    /**
     * Searches NASA's image library and maps the response to app models.
     */
    override suspend fun searchLibraryImages(
        query: String,
        page: Int,
        photographer: String?,
        location: String?
    ): NasaImagePage {
        val response : ImageLibraryDTO =
            _imageLibraryApiService.getSearch(query, page, location, photographer)

        val items = response.collection.items.mapNotNull{ item ->
            // if the first element is null return null to ignore this item
            val data = item.data.firstOrNull() ?: return@mapNotNull null
            val imageLinks = item.links
                .filter { it.render == "image" && it.href.isNotBlank() }

            if (imageLinks.isEmpty())
                return@mapNotNull null

            val (thumbUrl, imageUrl) = getSearchLibUrls(imageLinks)

            NasaImage(
                nasaId = data.nasaId,
                title = data.title,
                dateCreated = data.dateCreated,
                center = data.center,
                description = data.description,
                mediaType = data.mediaType,
                photographer = data.photographer,
                location = data.location,
                thumbUrl = thumbUrl,
                imageUrl = imageUrl
            )
        }

        val totalHits = response.collection.metadata?.totalHits ?: 0

        return NasaImagePage(
            query = query,
            page = page,
            totalHits = totalHits,
            items = items
        )
    }

    /**
     * Selects the best preview and display URLs from the available image links.
     */
    private fun getSearchLibUrls(imageLinks: List<ImageItemLinkDTO>): Pair<String?, String?> {
        val previewUrl = imageLinks
            .minByOrNull { imageLink ->
                imageLink.size ?: Long.MAX_VALUE
            }
            ?.href

        val imageUrl = imageLinks
            .minByOrNull { imageLink ->
                val width = imageLink.width

                if (width == null) {
                    Int.MAX_VALUE
                } else {
                    kotlin.math.abs(width - 1600)
                }
            }
            ?.href

        return Pair(previewUrl, imageUrl)
    }

}

