package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    locationId: Int?,
    navController: NavController,
    viewModel: LocationDetailViewModel = koinViewModel()
) {
    LaunchedEffect(locationId) {
        locationId?.let { viewModel.fetchLocationById(it) }
    }
    val location by viewModel.location.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(location?.name ?: "Location Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        location?.let { loc ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                val painter = rememberAsyncImagePainter(loc.imageUrl)
                Image(painter = painter, contentDescription = null, modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = loc.name, style = MaterialTheme.typography.titleLarge, color = Color.Black)
                Text(text = "Type: ${loc.type}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = "Dimension: ${loc.dimension}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } ?: Text(
            text = "Location not found.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red,
            modifier = Modifier.fillMaxSize().padding(padding).wrapContentSize(Alignment.Center)
        )
    }
}
