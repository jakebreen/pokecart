package uk.co.jakebreen.pokecart.model.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.ui.cart.CartItemResources
import uk.co.jakebreen.pokecart.ui.cart.CartItemViewModel

class CartRepository(
    private val cartPokemon: MutableLiveData<MutableMap<Pokemon, Int>>,
    private val cartResources: CartItemResources
) {

    private val cartItemViewModels = Transformations.map(cartPokemon) { set -> set.map { CartItemViewModel.from(it.key, it.value, cartResources) } }

    fun addCartItem(pokemon: Pokemon) {
        val set = cartPokemon.value!!
        if (set.contains(pokemon)) {
            set.compute(pokemon) { k, v -> v?.inc() }
        } else {
            set[pokemon] = 1
        }
        cartPokemon.postValue(set)
    }

    fun removeCartItem(pokemon: Pokemon) {
        val set = cartPokemon.value!!
        if (set.contains(pokemon)) {
            if (set[pokemon] == 1) {
                set.remove(pokemon)
            } else {
                set.compute(pokemon) { k, v -> v?.dec() }
            }
        }
        cartPokemon.postValue(set)
    }

    fun observeUpdates() = cartItemViewModels

    fun clear() = cartPokemon.postValue(mutableMapOf())

}