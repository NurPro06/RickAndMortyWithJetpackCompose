package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto
import org.koin.androidx.compose.koinViewModel


@Composable
fun LocationScreen(
    navController: NavHostController,
    viewModel: LocationViewModel = koinViewModel()
) {
    val locations = viewModel.locationPager.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(count = locations.itemCount) { index ->
            val location = locations[index]
            location?.let {
                LocationItem(location = it) {
                    navController.navigate("location_detail/${it.id}")
                }
            }
        }

        locations.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = loadState.refresh as LoadState.Error
                    item {
                        Text(
                            text = "Ошибка загрузки: ${e.error.localizedMessage}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(location: LocationResponseDto.Location, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = rememberAsyncImagePainter(model = location.imageUrl)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            alignment = Alignment.Center,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}