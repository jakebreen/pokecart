package uk.co.jakebreen.pokecart.ui.shop

import androidx.lifecycle.*
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemResources
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel

class ShopViewModel(private val pokemonRepository: PokemonRepository,
                    private val filterRepository: FilterRepository,
                    private val resources: ShopItemResources): ViewModel() {

    private val viewModels = Transformations.switchMap(filterRepository.observeUpdates()) { update -> updateViewModels(update) }

    private fun updateViewModels(update: FilterRepository.Update): LiveData<List<ShopItemViewModel>> {
        val health = update.statsMap[Stat.HEALTH] ?: Pair(0, 300)
        val attack = update.statsMap[Stat.ATTACK] ?: Pair(0, 300)
        val defense = update.statsMap[Stat.DEFENSE] ?: Pair(0, 300)
        val speed = update.statsMap[Stat.SPEED] ?: Pair(0, 300)

        return pokemonRepository.getFilteredPokemon(update.typesList, health, attack, defense, speed).let { pokemonLiveData ->
            Transformations.map(pokemonLiveData) { pokemonList ->
                pokemonList.map { pokemon -> ShopItemViewModel.from(pokemon, resources) }
            }
        }
    }

    fun observeViewModels() = viewModels

}