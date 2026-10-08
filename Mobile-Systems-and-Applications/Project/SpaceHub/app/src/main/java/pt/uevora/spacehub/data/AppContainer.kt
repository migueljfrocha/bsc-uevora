package pt.uevora.spacehub.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import pt.uevora.spacehub.BuildConfig
import pt.uevora.spacehub.data.network.ApodApiService
import pt.uevora.spacehub.data.network.EpicApiService
import pt.uevora.spacehub.data.network.ImageLibraryApiService
import pt.uevora.spacehub.data.repository.NasaRepository
import pt.uevora.spacehub.data.repository.NetworkNasaRepository
import retrofit2.Retrofit

/**
 * Provides the app-level dependencies.
 */
interface AppContainer {
    val nasaRepository: NasaRepository
}

class DefaultAppContainer : AppContainer {
    private val apodURL = "https://api.nasa.gov/"
    private val nasaImagesURL = "https://images-api.nasa.gov/"
    private val epicURL = "https://epic.gsfc.nasa.gov/"

    /**
     * JSON parser configured to ignore unknown API fields.
     */
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Creates a Retrofit instance for the provided base URL.
     */
    private fun retrofit(baseUrl: String): Retrofit {
       return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /**
     * Service used to access the APOD API.
     */
    private val apodApiService: ApodApiService by lazy {
        retrofit(apodURL).create(ApodApiService::class.java)
    }

    /**
     * Service used to access the NASA Image Library API.
     */
    private val imageLibraryApiService: ImageLibraryApiService by lazy {
        retrofit(nasaImagesURL).create(ImageLibraryApiService::class.java)
    }

    /**
     * Service used to access the EPIC API.
     */
    private val epicApiService: EpicApiService by lazy {
        retrofit(epicURL).create(EpicApiService::class.java)
    }

    /**
     * Repository that combines all NASA network services.
     */
    override val nasaRepository: NasaRepository by lazy {
        NetworkNasaRepository(
            _apodApiService = apodApiService,
            _imageLibraryApiService = imageLibraryApiService,
            _epicApiService = epicApiService,
            _apiKey = BuildConfig.NASA_API_KEY,
        )
    }
}