package kg.geeks.rickandmortywithjetpackcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kg.geeks.rickandmortywithjetpackcompose.data.api.LocationApiService
import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.paging.LocationPagingSource
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val apiService: LocationApiService
) {

    private var currentFilters = Filters()

    data class Filters(
        val name: String? = null,
        val type: String? = null,
        val dimension: String? = null
    )

    fun getLocationsPager(): Flow<PagingData<LocationResponseDto.Location>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                LocationPagingSource(apiService, currentFilters.name, currentFilters.type, currentFilters.dimension)
            }
        ).flow
    }

    fun updateFilters(filters: Filters) {
        currentFilters = filters
    }

    fun resetFilters() {
        currentFilters = Filters()
    }

    suspend fun fetchLocationById(locationId: Int): LocationResponseDto.Location {
        val response = apiService.fetchLocationById(locationId)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Location not found")
        } else {
            throw Exception("Failed to load location")
        }
    }
}