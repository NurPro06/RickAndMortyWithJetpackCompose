package kg.geeks.rickandmortywithjetpackcompose.data.api

import kg.geeks.rickandmortywithjetpackcompose.data.dto.character.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApiService {
    @GET("api/character")
    suspend fun fetchAllCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("gender") gender: String? = null
    ): CharacterResponseDto

    @GET("character/{id}")
    suspend fun fetchCharacterById(@Path("id") characterId: Int): CharacterResponseDto
}