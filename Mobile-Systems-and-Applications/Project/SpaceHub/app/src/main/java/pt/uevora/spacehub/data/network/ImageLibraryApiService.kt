package pt.uevora.spacehub.data.network

import pt.uevora.spacehub.data.model.ImageLibraryDTO
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines requests to the NASA Image and Video Library API.
 */
interface ImageLibraryApiService {

    /**
     * Searches NASA's image library using text, pagination, and optional filters.
     */
    @GET("search")
    suspend fun getSearch (
        @Query("q") query: String? = null,
        @Query("page") page: Int = 1,
        @Query("location") location: String? = null,
        @Query("photographer") photographer: String? = null,
        @Query("page_size") pageSize: Int = 16,
        @Query("media_type") mediaType: String = "image",
    ): ImageLibraryDTO
}