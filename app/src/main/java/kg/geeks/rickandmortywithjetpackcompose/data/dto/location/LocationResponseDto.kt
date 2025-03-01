package kg.geeks.rickandmortywithjetpackcompose.data.dto.location

data class LocationResponseDto(
    val info: Info,
    val results: List<Location>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )

    data class Location(
        val id: Int,
        val name: String,
        val type: String,
        val dimension: String,
        val residents: List<String>,
        val url: String,
        val created: String,
        val imageUrl: String? = null
    )
}