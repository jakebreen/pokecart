package uk.co.jakebreen.pokecart.ui.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type

class FilterDialogViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var filterRepository: FilterRepository
    @Mock lateinit var typesObserver: Observer<Map<Type, Boolean>>
    @Mock lateinit var statsObserver: Observer<Map<Stat, Pair<Int, Int>>>

    private val types = MutableLiveData<Map<Type, Boolean>>()
    private val stats = MutableLiveData<Map<Stat, Pair<Int, Int>>>()

    private lateinit var viewModel: FilterDialogViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(filterRepository.getFilterTypes()).thenReturn(types)
        Mockito.`when`(filterRepository.getFilterStats()).thenReturn(stats)

        viewModel = FilterDialogViewModel(filterRepository)
        viewModel.types.observeForever(typesObserver)
        viewModel.stats.observeForever(statsObserver)
    }

    @Test
    fun `givenTypesFromRepository thenObserveTypeChanges`() {
        val map = mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }.also { types.postValue(it) }

        argumentCaptor<Map<Type, Boolean>>().apply {
            verify(typesObserver).onChanged(capture())
        }.run { assertEquals(map, firstValue) }
    }

    @Test
    fun `givenStatsFromRepository thenObserveStatChanges`() {
        val map = mutableMapOf<Stat, Pair<Int, Int>>().apply {
            put(Stat.HEALTH, Pair(50, 100))
            put(Stat.SPEED, Pair(0, 300))
        }.also { stats.postValue(it) }

        argumentCaptor<Map<Stat, Pair<Int, Int>>>().apply {
            verify(statsObserver).onChanged(capture())
        }.run { assertEquals(map, firstValue) }
    }

    @Test
    fun `givenUserDefinedFilters whenSavingFilters thenObserveStatChanges`() {
        val typesMap = mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }

        val health = listOf(50F, 100F)
        val attack = listOf(0F, 300F)
        val defense = listOf(0F, 300F)
        val speed = listOf(0F, 250F)

        viewModel.saveFilters(typesMap, health, attack, defense, speed)

        val resultsMap = mapOf(
            Stat.HEALTH to Pair(health[0].toInt(), health[1].toInt()),
            Stat.ATTACK to Pair(attack[0].toInt(), attack[1].toInt()),
            Stat.DEFENSE to Pair(defense[0].toInt(), defense[1].toInt()),
            Stat.SPEED to Pair(speed[0].toInt(), speed[1].toInt())
        )

        Mockito.verify(filterRepository).postFilterTypes(typesMap)
        Mockito.verify(filterRepository).postFilterStats(resultsMap)
    }

}