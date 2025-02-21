package kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto
import kg.geeks.rickandmortywithjetpackcompose.data.repository.EpisodeRepository
import kotlinx.coroutines.launch

class EpisodeDetailViewModel(
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    fun fetchEpisodeById(episodeId: Int, onResult: (EpisodesResponseDto.Episodes?) -> Unit) {
        viewModelScope.launch {
            try {
                val episode = episodeRepository.fetchEpisodeById(episodeId)
                onResult(episode)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}