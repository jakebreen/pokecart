package uk.co.jakebreen.pokecart

import androidx.lifecycle.MutableLiveData
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.cart.CartRepository
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.ui.cart.CartItemResources

val cartRepositoryModule = module {
    single(named("cartPokemon")) { cartPokemon }
    factory { resources() }
    factory { getCartRepository(get(named("cartPokemon")), get()) }
}

private val cartPokemon = MutableLiveData<MutableMap<Pokemon, Int>>(mutableMapOf())

fun getCartRepository(cartPokemon: MutableLiveData<MutableMap<Pokemon, Int>>,
                      resources: CartItemResources)
        = CartRepository(cartPokemon, resources)

fun resources() = CartItemResources()