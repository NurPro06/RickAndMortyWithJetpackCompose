package kg.geeks.rickandmortywithjetpackcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kg.geeks.rickandmortywithjetpackcompose.data.api.CharacterApiService
import kg.geeks.rickandmortywithjetpackcompose.data.dto.character.CharacterResponseDto
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterEntity
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.paging.CharacterPagingSource
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val apiService: CharacterApiService
) {

    fun getCharactersPager(): Flow<PagingData<FavoriteCharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(apiService) }
        ).flow
    }

    suspend fun fetchCharacterById(characterId: Int): CharacterResponseDto.Character {
        try {
            val response = apiService.fetchCharacterById(characterId)

            return response.results.firstOrNull()
                ?: throw Exception("Character not found")
        } catch (e: Exception) {
            throw Exception("Failed to load character: ${e.message}")
        }
    }
}
