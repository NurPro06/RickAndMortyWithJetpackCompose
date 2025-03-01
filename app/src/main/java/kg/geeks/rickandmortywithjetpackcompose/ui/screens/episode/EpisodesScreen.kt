package kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeScreen(
    navController: NavHostController,
    viewModel: EpisodesViewModel = koinViewModel()
) {
    val episodes = viewModel.episodesPager.collectAsLazyPagingItems()
    val filters = viewModel.filters.value
    var showFilterDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(filters.name ?: "") }
    var tempEpisode by remember { mutableStateOf(filters.episode) }
    var isRefreshing by remember { mutableStateOf(false) }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            episodes.refresh()
            isRefreshing = false
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = tempName,
                onValueChange = { tempName = it },
                label = { Text("Search by name") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { showFilterDialog = true }) { Text("Filters") }
                Button(onClick = {
                    viewModel.resetFilters()
                    tempName = ""
                    tempEpisode = null
                    episodes.refresh()
                }) { Text("Reset") }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                if (episodes.itemCount == 0 && episodes.loadState.refresh !is LoadState.Loading) {
                    item {
                        Text(
                            text = "No episodes found",
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                items(count = episodes.itemCount) { index ->
                    val episode = episodes[index]
                    episode?.let {
                        EpisodeItem(episode = it) {
                            navController.navigate("episode_detail/${it.id}")
                        }
                    }
                }

                episodes.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
                        }
                        loadState.refresh is LoadState.Error -> {
                            item { Text("Ошибка загрузки: ${(loadState.refresh as LoadState.Error).error.localizedMessage}", modifier = Modifier.padding(16.dp)) }
                        }
                        loadState.append is LoadState.Error -> {
                            item { Text("Ошибка загрузки дополнительных эпизодов: ${(loadState.append as LoadState.Error).error.localizedMessage}", modifier = Modifier.padding(16.dp)) }
                        }
                    }
                }
            }
        }

        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = { Text("Filter Episodes") },
                text = {
                    Column {
                        Text("Episode", style = MaterialTheme.typography.titleMedium)
                        listOf("S01E01", "S02E01", "S03E01").forEach { ep ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = tempEpisode == ep, onClick = { tempEpisode = ep })
                                Text(ep)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.updateFilters(name = tempName.takeIf { it.isNotBlank() }, episode = tempEpisode)
                        showFilterDialog = false
                        episodes.refresh()
                    }) { Text("Apply") }
                },
                dismissButton = { Button(onClick = { showFilterDialog = false }) { Text("Cancel") } }
            )
        }
    }
}

@Composable
fun EpisodeItem(
    episode: EpisodesResponseDto.Episodes,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .padding(8.dp)
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = episode.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Episode: ${episode.episode}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Air Date: ${episode.airDate}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Characters: ${episode.characters.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}