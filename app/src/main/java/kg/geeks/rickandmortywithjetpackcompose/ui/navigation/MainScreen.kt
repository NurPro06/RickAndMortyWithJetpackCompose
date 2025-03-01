package kg.geeks.rickandmortywithjetpackcompose.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.CharacterScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.CharactersViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.detail.CharacterDetailScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.EpisodeScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.detail.EpisodeDetailScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.favorite.FavoriteScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.favorite.FavoriteViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.LocationScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.detail.LocationDetailScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val favoriteViewModel: FavoriteViewModel = koinViewModel()
    val charactersViewModel: CharactersViewModel = koinViewModel()
    var bottomBarOffset by remember { mutableFloatStateOf(0f) }
    val bottomBarHeight = 56.dp
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = (bottomBarOffset + delta).coerceIn(-bottomBarHeight.value, 0f)
                bottomBarOffset = newOffset
                return Offset.Zero
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                modifier = Modifier.offset(y = bottomBarOffset.dp)
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Characters.route,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection)
        ) {
            composable(
                BottomNavItem.Characters.route,
                enterTransition = { slideInHorizontally { it } },
                exitTransition = { slideOutHorizontally { -it } }
            ) {
                CharacterScreen(navController, charactersViewModel, favoriteViewModel)
            }
            composable(
                BottomNavItem.Locations.route,
                enterTransition = { slideInHorizontally { it } },
                exitTransition = { slideOutHorizontally { -it } }
            ) {
                LocationScreen(navController)
            }
            composable(
                BottomNavItem.Episodes.route,
                enterTransition = { slideInHorizontally { it } },
                exitTransition = { slideOutHorizontally { -it } }
            ) {
                EpisodeScreen(navController)
            }
            composable(
                BottomNavItem.Favorite.route,
                enterTransition = { slideInHorizontally { it } },
                exitTransition = { slideOutHorizontally { -it } }
            ) {
                FavoriteScreen(viewModel = favoriteViewModel)
            }
            composable(
                "character_detail/{id}",
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { -it } }
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                id?.let { CharacterDetailScreen(characterId = it, navController = navController) }
            }
            composable(
                "location_detail/{id}",
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { -it } }
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                id?.let { LocationDetailScreen(locationId = it, navController = navController) }
            }
            composable(
                "episode_detail/{id}",
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { -it } }
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                id?.let { EpisodeDetailScreen(episodeId = it, navController = navController) }
            }
        }
    }
}