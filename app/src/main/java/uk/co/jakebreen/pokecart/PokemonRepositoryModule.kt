package uk.co.jakebreen.pokecart

import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository

val pokemonRepositoryModule = module {
    factory { PokemonRepository(get()) }
}