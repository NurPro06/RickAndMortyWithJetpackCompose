package kg.geeks.rickandmortywithjetpackcompose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteCharacterDao {

    @Insert
    suspend fun addCharacterToFavorites(character: FavoriteCharacterEntity)

    @Delete
    suspend fun removeCharacterFromFavorites(character: FavoriteCharacterEntity)

    @Query("SELECT * FROM favorite_characters")
    suspend fun getAllFavoriteCharacters(): List<FavoriteCharacterEntity>
}