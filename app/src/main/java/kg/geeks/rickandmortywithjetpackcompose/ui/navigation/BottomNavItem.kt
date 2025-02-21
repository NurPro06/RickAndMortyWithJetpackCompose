package kg.geeks.rickandmortywithjetpackcompose.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Characters : BottomNavItem("characters", Icons.Default.Person, "Characters")
    object Locations : BottomNavItem("locations", Icons.Default.LocationOn, "Locations")
    object Episodes : BottomNavItem("episodes", Icons.Default.Face, "Episodes")
    object Favorite : BottomNavItem("favorite", Icons.Default.Favorite, "Favorites")
}
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(BottomNavItem.Characters, BottomNavItem.Locations)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        if (currentRoute != item.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}

