package pt.uevora.spacehub.data.network

import pt.uevora.spacehub.data.model.EpicDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Defines requests to the NASA EPIC API.
 */
interface EpicApiService {

    /**
     * Gets natural-color EPIC images for the given date.
     */
    @GET("api/natural/date/{date}")
    suspend fun getNaturalImages(
        @Path("date") date: String,
    ): List<EpicDto>


    /**
     * Gets enhanced-color EPIC images for the given date.
     */
    @GET("api/enhanced/date/{date}")
    suspend fun getEnhancedImages(
        @Path("date") date: String,
    ): List<EpicDto>

}