package uk.co.jakebreen.pokecart.ui.shop

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import uk.co.jakebreen.pokecart.model.filter.Filter
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.ui.Presenter
import kotlin.coroutines.CoroutineContext

class ShopPresenter(private val pokemonRepository: PokemonRepository,
                    private val shopFilter: Filter):
    Presenter<ShopActivity>(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main

    private lateinit var view: ShopActivity

    override fun onAttach(view: ShopActivity) {
        this.view = view

        shopFilter.observerUpdates().observe(view, Observer {
            val types = it.typesMap.value!!.filter { it.value }.map { it.key }.toList()
            val health = it.statsMap.value!![Stat.HEALTH] ?: Pair(0, 300)
            val attack = it.statsMap.value!![Stat.ATTACK] ?: Pair(0, 300)
            val defense = it.statsMap.value!![Stat.DEFENSE] ?: Pair(0, 300)
            val speed = it.statsMap.value!![Stat.SPEED] ?: Pair(0, 300)

            pokemonRepository.getFilteredPokemon(types, health, attack, defense, speed)
                .observe(view, Observer {
                    updateViewModels(it.map {
                        ShopViewModel.from(it)
                    }.toList())
                })
        })
    }

    override fun onDetach() { }

    private fun updateViewModels(viewModels: List<ShopViewModel>) {
        view.showPokemon(viewModels)
    }

    fun onClick() {
        view.showFilters()
    }

}