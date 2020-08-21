package uk.co.jakebreen.pokecart.model.filter

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type

class FilterRepository(private val types: MutableLiveData<Map<Type, Boolean>>,
                       private val stats: MutableLiveData<Map<Stat, Pair<Int, Int>>>) {

    fun getFilterTypes() = types

    @VisibleForTesting
    fun matchEnabledTypes(types: Map<Type, Boolean>) = types.filterValues { it }.map { it.key }.toList()

    fun getFilterStats() = stats

    fun postFilterTypes(typesMap: Map<Type, Boolean>) {
        if (types.value != typesMap) types.postValue(typesMap)
    }

    fun postFilterStats(statsMap: Map<Stat, Pair<Int, Int>>) {
        if (stats.value != statsMap)
            stats.postValue(statsMap)
    }

    private var updates = MediatorLiveData<Update>().apply {
        addSource(types) { value = Update(matchEnabledTypes(types.value ?: mapOf()), stats.value ?: mapOf()) }
        addSource(stats) { value = Update(matchEnabledTypes(types.value ?: mapOf()), stats.value ?: mapOf()) }
    }

    data class Update(val typesList: List<Type>, val statsMap: Map<Stat, Pair<Int, Int>>)

    fun observeUpdates() = updates

}