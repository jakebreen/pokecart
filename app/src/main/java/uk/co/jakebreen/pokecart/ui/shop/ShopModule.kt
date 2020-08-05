package uk.co.jakebreen.pokecart.ui.shop

import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.filter.Filter
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository

val shopModule = module {
    single { getPresenter(get(), get()) }
}

fun getPresenter(pokemonRepository: PokemonRepository, filter: Filter): ShopPresenter = ShopPresenter(pokemonRepository, filter)