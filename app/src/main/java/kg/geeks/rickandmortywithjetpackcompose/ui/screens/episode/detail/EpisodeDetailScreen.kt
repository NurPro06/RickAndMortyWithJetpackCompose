package kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailScreen(
    episodeId: Int?,
    navController: NavController
) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(episode.value?.name ?: "Episode Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            episode.value?.let { ep ->
                Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                    Text(text = ep.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Episode: ${ep.episode}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Air Date: ${ep.airDate}", style = MaterialTheme.typography.bodyLarge)
                }
            } ?: Text("Episode not found", modifier = Modifier.padding(padding))
        }
    }
}