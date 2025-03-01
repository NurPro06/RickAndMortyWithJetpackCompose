package kg.geeks.rickandmortywithjetpackcompose.data.dto.episodes

data class EpisodesResponseDto(
    val info: Info,
    val results: List<Episodes>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
    data class Episodes(
        val id: Int,
        val name: String,
        val airDate: String,
        val episode: String,
        val characters: List<String>,
        val url: String,
        val created: String
    )
}
