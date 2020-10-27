package uk.co.jakebreen.pokecart.ui.cart

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import uk.co.jakebreen.pokecart.model.cart.CartRepository
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository

class CartDialogViewModel(
    private val cartRepository: CartRepository,
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    private val viewModels = cartRepository.observeUpdates()
    private val isCartEmpty = Transformations.map(observeUpdates()) { it.isEmpty() }
    private val subtotal = Transformations.map(observeUpdates()) { it.map { pokemon -> calculateSubtotal(pokemon.price, pokemon.count) }.sum() }
    private val poketax = Transformations.map(observeSubtotal()) { calculatePoketax(it.toDouble()) }
    private val total = Transformations.map(observeSubtotal()) { calculateTotal(it, observePoketax().value ?: 0.0) }

    fun observeUpdates() = viewModels
    fun observeCartEmpty() = isCartEmpty
    fun observeSubtotal() = subtotal
    fun observePoketax() = poketax
    fun observeTotal() = total

    fun addCartItem(id: Int) {
        val pokemon = pokemonRepository.getPokemonById(id)
        cartRepository.addCartItem(pokemon)
    }

    fun removeCartItem(id: Int) {
        val pokemon = pokemonRepository.getPokemonById(id)
        cartRepository.removeCartItem(pokemon)
    }

    fun clear() {
        cartRepository.clear()
    }

    companion object {
        private const val POKE_TAX_PERCENTAGE = 20

        fun calculateSubtotal(price: Int, count: Int): Int {
            return price.times(count)
        }

        fun calculatePoketax(total: Double): Double {
            return ((total / 100) * POKE_TAX_PERCENTAGE)
        }

        fun calculateTotal(subtotal: Int, poketax: Double): Double {
            return subtotal.plus(poketax)
        }

        @JvmStatic
        @BindingAdapter("subtotal")
        fun bindSubtotal(textView: TextView, subtotal: Int) {
            textView.text = "$".plus(subtotal)
        }

        @JvmStatic
        @BindingAdapter("poketax")
        fun bindPoketax(textView: TextView, poketax: Double) {
            textView.text = String.format("$%.2f", poketax)
        }

        @JvmStatic
        @BindingAdapter("total")
        fun bindTotal(textView: TextView, total: Double) {
            textView.text = String.format("$%.2f", total)
        }
    }

}