package kg.geeks.rickandmortywithjetpackcompose.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kg.geeks.rickandmortywithjetpackcompose.ui.navigation.MainScreen
import kg.geeks.rickandmortywithjetpackcompose.ui.theme.RickAndMortyWithJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyWithJetpackComposeTheme {
                MainScreen()
            }
        }
    }
}