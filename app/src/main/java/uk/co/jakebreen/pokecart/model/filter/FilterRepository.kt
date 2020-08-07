package uk.co.jakebreen.pokecart.model.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type

val filterRepositoryModule = module {
    single(named("types")) { types }
    single(named("stats")) { stats }
    single { updates }
    factory { FilterRepository(get(named("types")), get(named("stats")), get()) }
}

private var types = MutableLiveData(Type.values().toList().minus(Type.NONE).map { Pair(it, true) }.toMap())

private var stats = MutableLiveData(mutableMapOf<Stat, Pair<Int, Int>>().apply {
    put(Stat.HEALTH, Pair(0, 300))
    put(Stat.ATTACK, Pair(0, 300))
    put(Stat.DEFENSE, Pair(0, 300))
    put(Stat.SPEED, Pair(0, 300))
}.toMap())

private var updates = MediatorLiveData<Update>().apply {
    addSource(types) { value = combineFilters(types, stats) }
    addSource(stats) { value = combineFilters(types, stats)}
}

private fun combineFilters(typesMap: LiveData<Map<Type, Boolean>>,
                           statsMap: LiveData<Map<Stat, Pair<Int, Int>>>) = Update(typesMap = typesMap, statsMap = statsMap)

data class Update(val typesMap: LiveData<Map<Type, Boolean>>, val statsMap: LiveData<Map<Stat, Pair<Int, Int>>>)

class FilterRepository(private val types: MutableLiveData<Map<Type, Boolean>>,
                       private val stats: MutableLiveData<Map<Stat, Pair<Int, Int>>>,
                       private val updates: MediatorLiveData<Update>) {

    fun getFilterTypes() = types

    fun getFilterStats() = stats

    fun postFilterTypes(typesMap: Map<Type, Boolean>) {
        if (types.value != typesMap)
            types.postValue(typesMap)
    }


    fun postFilterStats(statsMap: Map<Stat, Pair<Int, Int>>) {
        if (stats.value != statsMap)
            stats.postValue(statsMap)
    }

    fun observerUpdates() = updates

}