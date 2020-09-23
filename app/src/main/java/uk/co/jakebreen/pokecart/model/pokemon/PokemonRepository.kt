package uk.co.jakebreen.pokecart.model.pokemon

import uk.co.jakebreen.pokecart.model.type.Type

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

    fun getPokemonById(id: Int) = pokemonDao.getPokemonById(id)

}