package uk.co.jakebreen.pokecart.model.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import uk.co.jakebreen.pokecart.model.filter.FilterRepository.Update
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type

class FilterRepositoryTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var typesObserver: Observer<Map<Type, Boolean>>
    @Mock lateinit var statsObserver: Observer<Map<Stat, Pair<Int, Int>>>
    @Mock lateinit var updatesObserver: Observer<Update>

    private val typesLiveData = MutableLiveData(mapOf<Type, Boolean>())
    private val statsLiveData = MutableLiveData(mapOf<Stat, Pair<Int, Int>>())

    private lateinit var repository: FilterRepository

    @Before
    fun setup() {
        initMocks(this)

        repository = FilterRepository(typesLiveData, statsLiveData)
        repository.getFilterTypes().observeForever(typesObserver)
        repository.getFilterStats().observeForever(statsObserver)
        repository.observeUpdates().observeForever(updatesObserver)
        clearInvocations(typesObserver)
        clearInvocations(statsObserver)
        clearInvocations(updatesObserver)
    }

    @Test
    fun `whenGettingFilterTypes thenReturnTypes`() {
        val map = mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }.also { repository.postFilterTypes(it) }

        argumentCaptor<Map<Type, Boolean>>().apply {
            verify(typesObserver).onChanged(capture())
        }.run { assertEquals(map, firstValue) }
    }

    @Test
    fun `whenGettingFilterStats thenReturnStats`() {
        val map = mutableMapOf<Stat, Pair<Int, Int>>().apply {
            put(Stat.HEALTH, Pair(50, 100))
            put(Stat.ATTACK, Pair(100, 150))
            put(Stat.DEFENSE, Pair(0, 300))
            put(Stat.SPEED, Pair(0, 100))
        }.also { repository.postFilterStats(it) }

        argumentCaptor<Map<Stat, Pair<Int, Int>>>().apply {
            verify(statsObserver).onChanged(capture())
        }.run { assertEquals(map, firstValue) }
    }

    @Test
    fun `whenFilteringEnabledFilterTypes thenOnlyGetMatchingTypes`() {
        mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.BUG, false)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }.let {
            repository.matchEnabledTypes(it)
        }.also {
            assertTrue(it.contains(Type.FIGHTING))
            assertFalse(it.contains(Type.BUG))
            assertTrue(it.contains(Type.FAIRY))
            assertFalse(it.contains(Type.GROUND))
        }
    }

    @Test
    fun `whenPostingNewFilterTypes thenObserveChanges`() {
        val map1 = mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }.also { repository.postFilterTypes(it) }

        argumentCaptor<Map<Type, Boolean>>().apply {
            verify(typesObserver).onChanged(capture())
        }.run { assertEquals(map1, firstValue) }

        val map2 = mutableMapOf<Type, Boolean>().apply {
            put(Type.BUG, false)
            put(Type.POISON, true)
        }.also { repository.postFilterTypes(it) }

        argumentCaptor<Map<Type, Boolean>>().apply {
            verify(typesObserver, times(2)).onChanged(capture())
        }.run { assertEquals(map2, secondValue) }
    }

    @Test
    fun `whenPostingNewFilterStats thenObserveChanges`() {
        val map1 = mutableMapOf<Stat, Pair<Int, Int>>().apply {
            put(Stat.HEALTH, Pair(50, 100))
            put(Stat.ATTACK, Pair(100, 150))
            put(Stat.DEFENSE, Pair(0, 300))
            put(Stat.SPEED, Pair(0, 100))
        }.also { repository.postFilterStats(it) }

        argumentCaptor<Map<Stat, Pair<Int, Int>>>().apply {
            verify(statsObserver).onChanged(capture())
        }.run { assertEquals(map1, firstValue) }

        val map2 = mutableMapOf<Stat, Pair<Int, Int>>().apply {
            put(Stat.HEALTH, Pair(0, 300))
            put(Stat.ATTACK, Pair(0, 280))
            put(Stat.DEFENSE, Pair(200, 300))
            put(Stat.SPEED, Pair(250, 300))
        }.also { repository.postFilterStats(it) }

        argumentCaptor<Map<Stat, Pair<Int, Int>>>().apply {
            verify(statsObserver, times(2)).onChanged(capture())
        }.run { assertEquals(map2, secondValue) }
    }

    @Test
    fun `whenPostingNewFilterTypes andTypesHaveNotChanged thenNoMoreChangesObserved`() {
        mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }.also {
            repository.postFilterTypes(it)
            repository.postFilterTypes(it)
        }.also {
            argumentCaptor<Map<Type, Boolean>>().apply {
                verify(typesObserver, times(1)).onChanged(capture())
            }
        }
    }

    @Test
    fun `whenPostingNewFilterStats andStatsHaveNotChanged thenNoMoreChangesObserved`() {
        mutableMapOf<Stat, Pair<Int, Int>>().apply {
            put(Stat.HEALTH, Pair(50, 100))
            put(Stat.ATTACK, Pair(100, 150))
            put(Stat.DEFENSE, Pair(0, 300))
            put(Stat.SPEED, Pair(0, 100))
        }.also {
            repository.postFilterStats(it)
            repository.postFilterStats(it)
        }.also {
            argumentCaptor<Map<Stat, Pair<Int, Int>>>().apply {
                verify(statsObserver, times(1)).onChanged(capture())
            }
        }
    }

    @Test
    fun `whenFilterDataChanges thenCombineIntoUpdate andObserveChanged`() {
        val matchingTypes = mutableMapOf<Type, Boolean>().apply {
            put(Type.FIGHTING, true)
            put(Type.FAIRY, true)
            put(Type.GROUND, false)
        }.also {
            repository.postFilterTypes(it)
        }.let { repository.matchEnabledTypes(it) }

        val mapStats = mutableMapOf<Stat, Pair<Int, Int>>().apply {
            put(Stat.HEALTH, Pair(50, 100))
            put(Stat.ATTACK, Pair(100, 150))
            put(Stat.DEFENSE, Pair(0, 300))
            put(Stat.SPEED, Pair(0, 100))
        }.also { repository.postFilterStats(it) }

        argumentCaptor<Update>().apply {
            verify(updatesObserver, times(2)).onChanged(capture())
        }.run { assertEquals(Update(matchingTypes, mapStats), secondValue) }
    }

}