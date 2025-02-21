package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.geeks.rickandmortywithjetpackcompose.data.dto.character.CharacterResponseDto
import kg.geeks.rickandmortywithjetpackcompose.data.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    fun fetchCharacterById(characterId: Int, onResult: (CharacterResponseDto.Character?) -> Unit) {
        viewModelScope.launch {
            try {
                val character = characterRepository.fetchCharacterById(characterId)
                onResult(character)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}