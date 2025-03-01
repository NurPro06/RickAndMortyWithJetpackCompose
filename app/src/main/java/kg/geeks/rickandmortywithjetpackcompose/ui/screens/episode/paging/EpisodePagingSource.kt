package kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kg.geeks.rickandmortywithjetpackcompose.data.api.EpisodesApiService
import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto

class EpisodePagingSource(
    private val apiService: EpisodesApiService,
    private val name: String? = null,
    private val episode: String? = null
) : PagingSource<Int, EpisodesResponseDto.Episodes>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesResponseDto.Episodes> {
        val position = params.key ?: 1
        return try {
            val response = apiService.fetchAllEpisodes(
                page = position,
                name = name,
                episode = episode
            )
            val episodes = response.results
            Log.d("EpisodePagingSource", "Loaded ${episodes.size} episodes for page $position")
            LoadResult.Page(
                data = episodes,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (episodes.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e("EpisodePagingSource", "Error loading episodes", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodesResponseDto.Episodes>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}