package pt.uevora.spacehub.data.network

import pt.uevora.spacehub.data.model.ApodDto

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines requests to the NASA APOD API.
 */
interface ApodApiService {

    /**
     * Gets the APOD entry for today or for a specific date.
     */
    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String,
        @Query("thumbs") thumbs: String,
        @Query("date") date: String? = null
    ): ApodDto

    /**
     * Gets one or more random APOD entries.
     */
    @GET("planetary/apod")
    suspend fun getRandomApod(
        @Query("api_key") apiKey: String,
        @Query("thumbs") thumbs: String,
        @Query("count") count: Int = 1
    ): List<ApodDto>
}