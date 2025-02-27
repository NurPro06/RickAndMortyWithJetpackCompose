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
    private val _filters = mutableStateOf(CharacterRepository.Filters())
    val filters: State<CharacterRepository.Filters> = _filters

    val charactersPager: Flow<PagingData<FavoriteCharacterEntity>> =
        repository.getCharactersPager().cachedIn(viewModelScope)

    fun updateFilters(
        name: String? = _filters.value.name,
        status: String? = _filters.value.status,
        species: String? = _filters.value.species,
        gender: String? = _filters.value.gender
    ) {
        _filters.value = CharacterRepository.Filters(name, status, species, gender)
        repository.updateFilters(_filters.value)
    }

    fun resetFilters() {
        _filters.value = CharacterRepository.Filters()
        repository.updateFilters(_filters.value)
    }
}