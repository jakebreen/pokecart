package uk.co.jakebreen.pokecart

import androidx.lifecycle.MutableLiveData
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type

val filterRepositoryModule = module {
    single(named("types")) { types }
    single(named("stats")) { stats }
    factory { getFilterRepository(get(named("types")), get(named("stats"))) }
}

private var types = MutableLiveData(Type.values().toList().minus(Type.NONE).map { Pair(it, true) }.toMap())

private var stats = MutableLiveData(mutableMapOf<Stat, Pair<Int, Int>>().apply {
    put(Stat.HEALTH, Pair(0, 300))
    put(Stat.ATTACK, Pair(0, 300))
    put(Stat.DEFENSE, Pair(0, 300))
    put(Stat.SPEED, Pair(0, 300))
}.toMap())

fun getFilterRepository(types: MutableLiveData<Map<Type, Boolean>>,
                        stats: MutableLiveData<Map<Stat, Pair<Int, Int>>>) =  FilterRepository(types, stats)