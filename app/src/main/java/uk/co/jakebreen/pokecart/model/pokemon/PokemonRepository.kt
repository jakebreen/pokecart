package uk.co.jakebreen.pokecart.model.pokemon

import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.type.Type

val repositoryModule = module {
    factory { PokemonRepository(get()) }
}

class PokemonRepository(private val pokemonDao: PokemonDao) {

    fun getFilteredPokemon(types: List<Type>,
                           health: Pair<Int, Int>,
                           attack: Pair<Int, Int>,
                           defense: Pair<Int, Int>,
                           speed: Pair<Int, Int>) = pokemonDao.getFilteredPokemon(
        types = types,
        healthMin = health.first, healthMax = health.second,
        attackMin = attack.first, attackMax = attack.second,
        defenseMin = defense.first, defenseMax = defense.second,
        speedMin = speed.first, speedMax = speed.second
    )

}