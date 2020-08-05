package uk.co.jakebreen.pokecart.ui.filter

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import uk.co.jakebreen.pokecart.model.filter.Filter
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.ui.Presenter
import kotlin.coroutines.CoroutineContext

class FilterDialogPresenter(private val filter: Filter): Presenter<FilterDialogFragment>(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main

    private lateinit var view: FilterDialogFragment

    override fun onAttach(view: FilterDialogFragment) {
        this.view = view

        filter.observerUpdates().observe(view, Observer {
            applyTypes(it.typesMap.value!!)
            applyStats(it.statsMap.value!!)
        })
    }

    override fun onDetach() { }

    private fun applyTypes(types: Map<Type, Boolean>) {
        view.showFilterChips(types)
    }

    private fun applyStats(stats: Map<Stat, Pair<Int, Int>>) {
        stats.forEach {
            when(it.key) {
                Stat.HEALTH -> view.showHealthStats(it.value.first.toFloat(), it.value.second.toFloat())
                Stat.ATTACK -> view.showAttackStats(it.value.first.toFloat(), it.value.second.toFloat())
                Stat.DEFENSE -> view.showDefenseStats(it.value.first.toFloat(), it.value.second.toFloat())
                Stat.SPEED -> view.showSpeedStats(it.value.first.toFloat(), it.value.second.toFloat())
            }
        }
    }

    fun saveFilters() {
        view.onSaveFilters()
    }

    fun updateFilters(chips: Map<Type, Boolean>, health: List<Float>, attack: List<Float>, defense: List<Float>, speed: List<Float>) {
        filter.postFilterTypes(chips)
        filter.postFilterStats(mapOf(
            Stat.HEALTH to Pair(health[0].toInt(), health[1].toInt()),
            Stat.ATTACK to Pair(attack[0].toInt(), attack[1].toInt()),
            Stat.DEFENSE to Pair(defense[0].toInt(), defense[1].toInt()),
            Stat.SPEED to Pair(speed[0].toInt(), speed[1].toInt())))
    }

}