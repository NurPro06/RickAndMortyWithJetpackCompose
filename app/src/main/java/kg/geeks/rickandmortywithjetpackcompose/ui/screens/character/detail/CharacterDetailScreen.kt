package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kg.geeks.rickandmortywithjetpackcompose.data.dto.character.CharacterResponseDto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int?,
    navController: NavController
) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(character.value?.name ?: "Character Details") },
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
            character.value?.let { char ->
                Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                    AsyncImage(
                        model = char.image,
                        contentDescription = char.name,
                        modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = char.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Status: ${char.status}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Species: ${char.species}", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Character details will be added here.", style = MaterialTheme.typography.bodyMedium)
                }
            } ?: Text("Character not found", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(padding))
        }
    }
}