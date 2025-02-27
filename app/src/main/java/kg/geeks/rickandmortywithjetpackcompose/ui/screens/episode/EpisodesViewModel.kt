package kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto
import kg.geeks.rickandmortywithjetpackcompose.data.repository.EpisodeRepository
import kotlinx.coroutines.flow.Flow

class EpisodesViewModel(
    private val repository: EpisodeRepository
) : ViewModel() {

    private val _filters = mutableStateOf(EpisodeRepository.Filters())
    val filters: State<EpisodeRepository.Filters> = _filters

    val episodesPager: Flow<PagingData<EpisodesResponseDto.Episodes>> =
        repository.getEpisodesPager().cachedIn(viewModelScope)

    fun updateFilters(
        name: String? = _filters.value.name,
        episode: String? = _filters.value.episode
    ) {
        _filters.value = EpisodeRepository.Filters(name, episode)
        repository.updateFilters(_filters.value)
    }

    fun resetFilters() {
        _filters.value = EpisodeRepository.Filters()
        repository.resetFilters()
    }
}
