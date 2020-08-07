package uk.co.jakebreen.pokecart.model.pokemon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakebreen.pokecart.model.type.Type

@Dao
interface PokemonDao {

    @Query("SELECT * FROM (SELECT * FROM pokemon_table WHERE type_primary IN (:types) " +
            "UNION SELECT * FROM pokemon_table WHERE type_secondary IN (:types)) AS pokemon " +
            "WHERE health BETWEEN (:healthMin) AND (:healthMax) AND attack BETWEEN (:attackMin) AND (:attackMax) " +
            "AND defense BETWEEN (:defenseMin) AND (:defenseMax) AND speed BETWEEN (:speedMin) AND (:speedMax)")
    fun getFilteredPokemon(types: List<Type>,
                           healthMin: Int, healthMax: Int,
                           attackMin: Int, attackMax: Int,
                           defenseMin: Int, defenseMax: Int,
                           speedMin: Int, speedMax: Int): LiveData<List<Pokemon>>

    @Query("SELECT COUNT(*) from pokemon_table ")
    suspend fun getPokemonCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

}