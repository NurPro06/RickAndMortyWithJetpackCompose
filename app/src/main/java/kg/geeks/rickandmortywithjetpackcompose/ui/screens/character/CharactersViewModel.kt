package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterEntity
import kg.geeks.rickandmortywithjetpackcompose.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
class CharactersViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    val charactersPager: Flow<PagingData<FavoriteCharacterEntity>> =
        repository.getCharactersPager().cachedIn(viewModelScope)
}