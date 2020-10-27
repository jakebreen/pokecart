package uk.co.jakebreen.pokecart.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon

class InMemoryCartRepository {

    private val cartItems = mutableMapOf<Pokemon, Int>()
    private val cartItemsMutableLiveData = MutableLiveData<MutableMap<Pokemon, Int>>()
    private val cartItemsLiveData = Transformations.map(cartItemsMutableLiveData) { it.toMap() }

    fun addCartItem(pokemon: Pokemon) {
        if (cartItems.contains(pokemon)) {
            cartItems.compute(pokemon) { k, v -> v?.inc() }
        } else {
            cartItems[pokemon] = 1
        }
        cartItemsMutableLiveData.postValue(cartItems)
    }

    fun removeCartItem(pokemon: Pokemon) {
        if (cartItems.contains(pokemon)) {
            if (cartItems[pokemon] == 1) {
                cartItems.remove(pokemon)
            } else {
                cartItems.compute(pokemon) { k, v -> v?.dec() }
            }
        }
        cartItemsMutableLiveData.postValue(cartItems)
    }

    fun getCartItems(): LiveData<Map<Pokemon, Int>> = cartItemsLiveData

    fun clear() = cartItems.clear().also { cartItemsMutableLiveData.postValue(mutableMapOf()) }

}