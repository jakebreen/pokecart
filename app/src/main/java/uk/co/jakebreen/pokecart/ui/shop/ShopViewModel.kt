package uk.co.jakebreen.pokecart.ui.shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel

class ShopViewModel(private val pokemonRepository: PokemonRepository,
                    private val shopFilter: FilterRepository): ViewModel() {

    private val viewModels = MutableLiveData<List<ShopItemViewModel>>()

    init {
        viewModelScope.launch {
            shopFilter.observerUpdates()
                .observeForever {
                    val types = it.typesMap.value!!.filter { it.value }.map { it.key }.toList()
                    val health = it.statsMap.value!![Stat.HEALTH] ?: Pair(0, 300)
                    val attack = it.statsMap.value!![Stat.ATTACK] ?: Pair(0, 300)
                    val defense = it.statsMap.value!![Stat.DEFENSE] ?: Pair(0, 300)
                    val speed = it.statsMap.value!![Stat.SPEED] ?: Pair(0, 300)

                    pokemonRepository.getFilteredPokemon(types, health, attack, defense, speed)
                        .observeForever {
                            viewModels.postValue(it.map {
                                ShopItemViewModel.from(it)
                            }.toList())
                        }
                }
        }
    }

    fun observeViewModelUpdates() = this.viewModels

}