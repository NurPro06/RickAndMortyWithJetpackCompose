package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto
import kg.geeks.rickandmortywithjetpackcompose.data.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationViewModel(
    private val repository: LocationRepository
)  : ViewModel() {

    val locationPager : Flow<PagingData<LocationResponseDto.Location>> =
        repository.getLocationsPager().cachedIn(viewModelScope)
}