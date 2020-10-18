package uk.co.jakebreen.pokecart.model.cart

import androidx.lifecycle.Transformations
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.persistence.InMemoryCartRepository
import uk.co.jakebreen.pokecart.ui.cart.CartItem
import uk.co.jakebreen.pokecart.ui.cart.CartItemResources

class CartRepository(
    private val inMemoryCartRepository: InMemoryCartRepository,
    private val cartItemResources: CartItemResources
) {

    private val cartItemViewModelsLiveData = Transformations.map(inMemoryCartRepository.getCartItems()) { set ->
        set.map {
            val pokemon = it.key
            val count = it.value
            CartItem(pokemon.id, count,  pokemon.name, cartItemResources.getImageUriById(pokemon.id), pokemon.price)
        }.toSet()
    }

    fun addCartItem(pokemon: Pokemon) = inMemoryCartRepository.addCartItem(pokemon)

    fun removeCartItem(pokemon: Pokemon) = inMemoryCartRepository.removeCartItem(pokemon)

    fun observeUpdates() = cartItemViewModelsLiveData

    fun clear() = inMemoryCartRepository.clear()

}