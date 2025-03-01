package kg.geeks.rickandmortywithjetpackcompose.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Characters : BottomNavItem("characters", Icons.Default.Person, "Characters")
    object Locations : BottomNavItem("locations", Icons.Default.LocationOn, "Locations")
    object Episodes : BottomNavItem("episodes", Icons.Default.Face, "Episodes")
    object Favorite : BottomNavItem("favorite", Icons.Default.Favorite, "Favorites")
}

@Composable
fun BottomNavigationBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Locations,
        BottomNavItem.Episodes,
        BottomNavItem.Favorite
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.2f else 1f,
                animationSpec = tween(durationMillis = 300),
                label = "icon_scale"
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.scale(scale)
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}