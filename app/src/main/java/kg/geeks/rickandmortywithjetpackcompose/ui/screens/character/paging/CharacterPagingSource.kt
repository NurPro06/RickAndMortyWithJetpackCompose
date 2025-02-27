package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kg.geeks.rickandmortywithjetpackcompose.data.api.CharacterApiService
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterEntity

class CharacterPagingSource(
    private val apiService: CharacterApiService,
    private val name: String? = null,
    private val status: String? = null,
    private val species: String? = null,
    private val gender: String? = null
) : PagingSource<Int, FavoriteCharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteCharacterEntity> {
        val position = params.key ?: 1
        return try {
            val response = apiService.fetchAllCharacters(
                page = position,
                name = name,
                status = status,
                species = species,
                gender = gender
            )
            val characters = response.results.map { character ->
                FavoriteCharacterEntity(
                    id = character.id,
                    name = character.name,
                    species = character.species,
                    image = character.image
                )
            }

            LoadResult.Page(
                data = characters,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (characters.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FavoriteCharacterEntity>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}