package uk.co.jakebreen.pokecart.persistence

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.co.jakebreen.pokecart.api.PokemonApi
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.pokemon.PokemonDao

class DatabaseManager(private val database: PokemonRoomDatabase,
                      private val pokemonDao: PokemonDao,
                      private val pokemonApi: PokemonApi) {

    companion object {
        const val TOTAL_POKEMON = 151
    }

    fun onStart() {
        CoroutineScope(Dispatchers.IO).launch {
            if (isDatabaseComplete()) {
                clearTables()
                populateTables()
            }
        }
    }

    private suspend fun isDatabaseComplete(): Boolean = pokemonDao.getPokemonCount() != TOTAL_POKEMON

    private fun clearTables() {
        database.clearAllTables()
    }

    private suspend fun populateTables() {
        for (i in 1..TOTAL_POKEMON)
            pokemonDao.insert(getPokemonById(i))
    }

    private suspend fun getPokemonById(id: Int): Pokemon = pokemonApi.getPokemonById(id)

}