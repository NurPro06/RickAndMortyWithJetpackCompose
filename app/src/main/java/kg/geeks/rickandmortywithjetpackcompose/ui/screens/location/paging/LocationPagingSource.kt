package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kg.geeks.rickandmortywithjetpackcompose.data.api.LocationApiService
import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto

class LocationPagingSource(private val apiService: LocationApiService) : PagingSource<Int, LocationResponseDto.Location>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationResponseDto.Location> {
        val position = params.key ?: 1

        return try {
            val response = apiService.fetchAllLocations(position)
            val locations = response.locationResults

            LoadResult.Page(
                data = locations,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (locations.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocationResponseDto.Location>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}