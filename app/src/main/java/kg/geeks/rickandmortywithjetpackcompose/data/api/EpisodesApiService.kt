package kg.geeks.rickandmortywithjetpackcompose.data.api

import kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes.EpisodesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApiService {

    @GET("episode")
    suspend fun fetchAllEpisodes(
        @Query("page") page: Int
    ): EpisodesResponseDto

    @GET("episode/{id}")
    suspend fun fetchEpisodeById(@Path("id") episodeId: Int): Response<EpisodesResponseDto.Episodes>
}