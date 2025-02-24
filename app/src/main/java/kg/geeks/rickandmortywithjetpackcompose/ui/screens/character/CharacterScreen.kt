package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterEntity
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.favorite.FavoriteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterScreen(
    navController: NavHostController,
    viewModel: CharactersViewModel = koinViewModel(),
    favoriteViewModel: FavoriteViewModel
) {
    val characters = viewModel.charactersPager.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(count = characters.itemCount) { index ->
            val character = characters[index]
            character?.let {
                CharacterItem(
                    character = it,
                    onClick = {
                        navController.navigate("character_detail/${it.id}")
                    },
                    onLongClick = {
                        favoriteViewModel.addCharacterToFavorites(it)
                    }
                )
            }
        }

        characters.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    item {
                        Text(
                            text = "Error loading characters: ${error.error.localizedMessage}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    item {
                        Text(
                            text = "Error loading more: ${error.error.localizedMessage}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterItem(
    character: FavoriteCharacterEntity,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = rememberAsyncImagePainter(character.image)
        Image(painter = painter, contentDescription = null, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = character.name, style = MaterialTheme.typography.titleLarge)
            Text(text = character.species, style = MaterialTheme.typography.titleMedium)
        }
    }
}