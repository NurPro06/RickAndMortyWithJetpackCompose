package kg.geeks.rickandmortywithjetpackcompose.data.api

import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApiService {
    @GET("api/location")
    suspend fun fetchAllLocations(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null
    ): LocationResponseDto

    @GET("location/{id}")
    suspend fun fetchLocationById(@Path("id") locationId: Int): Response<LocationResponseDto.Location>
}