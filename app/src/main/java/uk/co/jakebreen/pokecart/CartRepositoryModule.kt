package uk.co.jakebreen.pokecart

import org.koin.core.qualifier.named
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.cart.CartRepository
import uk.co.jakebreen.pokecart.persistence.InMemoryCartRepository
import uk.co.jakebreen.pokecart.ui.cart.CartItemResources

val cartRepositoryModule = module {
    single(named("cartPokemon")) { getInMemoryCartRepository() }
    factory { resources() }
    factory { getCartRepository(get(named("cartPokemon")), get()) }
}

fun getInMemoryCartRepository() = InMemoryCartRepository()

fun getCartRepository(inMemoryCartRepository: InMemoryCartRepository,
                      resources: CartItemResources) =
    CartRepository(inMemoryCartRepository, resources)

fun resources() = CartItemResources()