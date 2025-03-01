package kg.geeks.rickandmortywithjetpackcompose.ui.screens.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterDao
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteCharacterDao: FavoriteCharacterDao) : ViewModel() {

    private val _favoriteCharactersEntity = MutableStateFlow<List<FavoriteCharacterEntity>>(emptyList())
    val favoriteCharactersEntity: StateFlow<List<FavoriteCharacterEntity>> = _favoriteCharactersEntity

    init {
        getFavoriteCharacters()
    }

    private fun getFavoriteCharacters() {
        viewModelScope.launch {
            val favorites = favoriteCharacterDao.getAllFavoriteCharacters()
            _favoriteCharactersEntity.value = favorites
            Log.d("FavoriteViewModel", "Loaded ${favorites.size} favorite characters")
        }
    }

    fun addCharacterToFavorites(character: FavoriteCharacterEntity) {
        viewModelScope.launch {
            favoriteCharacterDao.addCharacterToFavorites(character)
            getFavoriteCharacters()
            Log.d("FavoriteViewModel", "Added character: ${character.name}")
        }
    }

    fun removeCharacterFromFavorites(character: FavoriteCharacterEntity) {
        viewModelScope.launch {
            favoriteCharacterDao.removeCharacterFromFavorites(character)
            getFavoriteCharacters()
            Log.d("FavoriteViewModel", "Removed character: ${character.name}")
        }
    }
}