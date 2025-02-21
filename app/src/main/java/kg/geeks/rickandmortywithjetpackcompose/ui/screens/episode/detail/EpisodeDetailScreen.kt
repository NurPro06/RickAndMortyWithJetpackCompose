package kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto

@Composable
fun EpisodeDetailScreen(episodeId : Int?) {

    val viewModel: EpisodeDetailViewModel = viewModel()

    val episode = remember { mutableStateOf<EpisodesResponseDto.Episodes?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(episodeId) {
        episodeId?.let {
            viewModel.fetchEpisodeById(it) { fetchedEpisode ->
                episode.value = fetchedEpisode
                isLoading.value = false
            }
        }
    }
}