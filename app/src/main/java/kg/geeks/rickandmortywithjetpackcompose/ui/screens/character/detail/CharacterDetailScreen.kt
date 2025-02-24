package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kg.geeks.rickandmortywithjetpackcompose.data.dto.character.CharacterResponseDto

@Composable
fun CharacterDetailScreen(characterId: Int?) {

    val viewModel: CharacterDetailViewModel = viewModel()

    val character = remember { mutableStateOf<CharacterResponseDto.Character?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(characterId) {
        characterId?.let {
            viewModel.fetchCharacterById(it) { fetchedCharacter ->
                character.value = fetchedCharacter
                isLoading.value = false
            }
        }
    }

    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        character.value?.let { character ->
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ${character.status}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Species: ${character.species}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Character details will be added here.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } ?: run {
            Text(text = "Character not found", style = MaterialTheme.typography.bodyLarge)
        }
    }
}