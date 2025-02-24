package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationDetailScreen(
    locationId: Int?,
    viewModel: LocationDetailViewModel = koinViewModel()
) {
    LaunchedEffect(locationId) {
        locationId?.let {
            viewModel.fetchLocationById(it)
        }
    }

    val location = viewModel.location.collectAsState().value

    if (location != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val painter = rememberAsyncImagePainter(location.imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = location.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black
            )
            Text(
                text = "Type: ${location.type}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )
            Text(
                text = "Dimension: ${location.dimension}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )
        }
    } else {
        Text(
            text = "Location not found.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red,
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
        )
    }
}
