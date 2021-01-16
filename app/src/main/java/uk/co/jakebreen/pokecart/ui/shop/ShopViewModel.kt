package uk.co.jakebreen.pokecart.ui.shop

import androidx.lifecycle.*
import uk.co.jakebreen.pokecart.model.cart.CartRepository
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat

class ShopViewModel(private val pokemonRepository: PokemonRepository,
                    private val filterRepository: FilterRepository,
                    private val shopResources: ShopItemResources,
                    private val cartRepository: CartRepository
): ViewModel() {

    private val shopItems = Transformations.switchMap(filterRepository.observeUpdates()) { update -> updateShopItems(update) }
    private val cartItemCount = Transformations.map(cartRepository.observeUpdates()) { it.map { it.count }.sum() }

    private fun updateShopItems(update: FilterRepository.Update): LiveData<List<ShopItem>> {
        val health = update.statsMap[Stat.HEALTH] ?: Pair(0, 300)
        val attack = update.statsMap[Stat.ATTACK] ?: Pair(0, 300)
        val defense = update.statsMap[Stat.DEFENSE] ?: Pair(0, 300)
        val speed = update.statsMap[Stat.SPEED] ?: Pair(0, 300)

        return pokemonRepository.getFilteredPokemon(update.typesList, health, attack, defense, speed).let { pokemonLiveData ->
            Transformations.map(pokemonLiveData) { pokemonList ->
                pokemonList.map { pokemon -> ShopItem.from(pokemon, shopResources) }
            }
        }
    }

    fun observeShopItems() = shopItems
    fun observeCartItemCount() = cartItemCount

    fun addPokemonToCart(id: Int) {
        val pokemon = pokemonRepository.getPokemonById(id)
        cartRepository.addCartItem(pokemon)
    }

}