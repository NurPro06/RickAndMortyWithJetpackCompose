package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import coil3.compose.rememberAsyncImagePainter
import kg.geeks.rickandmortywithjetpackcompose.data.dto.character.CharacterResponseDto
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

    when {
        characters.loadState.refresh is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        characters.loadState.refresh is LoadState.Error -> {
            val error = characters.loadState.refresh as LoadState.Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error loading characters: ${error.error.localizedMessage}")
            }
        }
        else -> {
            LazyColumn {
                items(characters) {
                    characters.let {
                        CharacterItem(
                            character = it,
                            onClick = {
                                navController.navigate("character_detail/${it.id}")
                            },
                            onLongClick = {
                                val favoriteCharacterEntity = FavoriteCharacterEntity(
                                    id = it.id,
                                    name = it.name,
                                    species = it.species,
                                    image = it.image
                                )
                                favoriteViewModel.addCharacterToFavorites(favoriteCharacterEntity)
                            }
                        )
                    }
                }

                when (val appendState = characters.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            Text(text = "Error loading more characters: ${appendState.error.localizedMessage}")
                        }
                    }
                    is LoadState.NotLoading -> {
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterItem(character: CharacterResponseDto.Character, onClick: () -> Unit, onLongClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = rememberAsyncImagePainter(character.image)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = character.name, style = MaterialTheme.typography.titleLarge)
            Text(text = character.species, style = MaterialTheme.typography.titleMedium)
        }
    }
}