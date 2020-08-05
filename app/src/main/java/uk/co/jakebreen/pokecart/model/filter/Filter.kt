package uk.co.jakebreen.pokecart.model.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type


class Filter {

    private var types = MutableLiveData<Map<Type, Boolean>>()
    private var stats = MutableLiveData<Map<Stat, Pair<Int, Int>>>()
    private var updates = MediatorLiveData<Update>()

    init {
        types = MutableLiveData(Type.values().toList().minus(Type.NONE).map { Pair(it, true) }.toMap())

        val map = mutableMapOf<Stat, Pair<Int, Int>>()
        map[Stat.HEALTH] = Pair(0, 300)
        map[Stat.ATTACK] = Pair(0, 300)
        map[Stat.DEFENSE] = Pair(0, 300)
        map[Stat.SPEED] = Pair(0, 300)
        stats = MutableLiveData(map)

        updates.addSource(types) { updates.value = combineFilters(types, stats)}
        updates.addSource(stats) { updates.value = combineFilters(types, stats)}
    }

    fun postFilterTypes(types: Map<Type, Boolean>) = this.types.postValue(types)

    fun postFilterStats(stats: Map<Stat, Pair<Int, Int>>) = this.stats.postValue(stats)

    fun observerUpdates(): MediatorLiveData<Update> = updates

    private fun combineFilters(typesMap: LiveData<Map<Type, Boolean>>,
                               statsMap: LiveData<Map<Stat, Pair<Int, Int>>>) = Update(typesMap = typesMap, statsMap = statsMap)

    data class Update(val typesMap: LiveData<Map<Type, Boolean>>,
                      val statsMap: LiveData<Map<Stat, Pair<Int, Int>>>)

}