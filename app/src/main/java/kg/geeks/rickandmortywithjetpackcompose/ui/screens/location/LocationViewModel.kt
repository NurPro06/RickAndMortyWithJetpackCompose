package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto
import kg.geeks.rickandmortywithjetpackcompose.data.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _filters = mutableStateOf(LocationRepository.Filters())
    val filters: State<LocationRepository.Filters> = _filters

    val locationPager: Flow<PagingData<LocationResponseDto.Location>> =
        repository.getLocationsPager().cachedIn(viewModelScope)

    fun updateFilters(
        name: String? = _filters.value.name,
        type: String? = _filters.value.type,
        dimension: String? = _filters.value.dimension
    ) {
        _filters.value = LocationRepository.Filters(name, type, dimension)
        repository.updateFilters(_filters.value)
    }

    fun resetFilters() {
        _filters.value = LocationRepository.Filters()
        repository.resetFilters()
    }
}