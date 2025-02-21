package kg.geeks.rickandmortywithjetpackcompose.data.dto.location

data class LocationResponseDto(
    val locationResults: List<Location>
) {
    data class Location(
        val id: Int,
        val name: String,
        val type: String,
        val dimension: String,
        val imageUrl: String?
    )
}