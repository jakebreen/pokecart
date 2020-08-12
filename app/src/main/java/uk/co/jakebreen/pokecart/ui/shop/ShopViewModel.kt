package uk.co.jakebreen.pokecart.ui.shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemResources
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel

class ShopViewModel(private val pokemonRepository: PokemonRepository,
                    private val filterRepository: FilterRepository,
                    private val resources: ShopItemResources): ViewModel() {

    val items = MutableLiveData<List<ShopItemViewModel>>()

    init {
        viewModelScope.launch {
            filterRepository.observeUpdates().observeForever {
                val health = it.statsMap[Stat.HEALTH] ?: Pair(0, 300)
                val attack = it.statsMap[Stat.ATTACK] ?: Pair(0, 300)
                val defense = it.statsMap[Stat.DEFENSE] ?: Pair(0, 300)
                val speed = it.statsMap[Stat.SPEED] ?: Pair(0, 300)

                pokemonRepository.getFilteredPokemon(it.typesList, health, attack, defense, speed)
                    .observeForever {
                        items.postValue(it.map { ShopItemViewModel.from(it, resources) }.toList())
                    }
            }
        }
    }

}