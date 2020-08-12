package uk.co.jakebreen.pokecart.ui.shop

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.filter.FilterRepository.Update
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemResources
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel
import kotlin.random.Random

class ShopViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var pokemonRepository: PokemonRepository
    @Mock lateinit var filterRepository: FilterRepository
    @Mock lateinit var resources: ShopItemResources
    @Mock lateinit var itemsObserver: Observer<List<ShopItemViewModel>>

    private val pokemon = MutableLiveData<List<Pokemon>>()
    private val updates = MediatorLiveData<Update>()

    private val pokemonOne = Pokemon(1, "pokemonOne", Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Type.GROUND, Type.FIRE)
    private val pokemonTwo = Pokemon(2, "pokemonTwo", Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Type.NORMAL, Type.POISON)
    private val pokemonThree = Pokemon(3, "pokemonThree", Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Type.GROUND, Type.BUG)

    private lateinit var viewModel: ShopViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(filterRepository.observeUpdates()).thenReturn(updates)
        Mockito.`when`(resources.getImageUriById(1)).thenReturn(Mockito.mock(Uri::class.java))
        Mockito.`when`(resources.getImageUriById(2)).thenReturn(Mockito.mock(Uri::class.java))
        Mockito.`when`(resources.getImageUriById(3)).thenReturn(Mockito.mock(Uri::class.java))

        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ShopViewModel(pokemonRepository, filterRepository, resources)
        viewModel.items.observeForever(itemsObserver)
    }

    @After
    fun cleanup() {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `givenObservedFilterRepositoryChanges whenQueryingPokemonRepository andOnePokemonReturned thenObservePokemonChangesAsViewModels`() {
        val typesList = mutableListOf<Type>().apply {
            add(Type.BUG)
            add(Type.WATER)
            add(Type.FIRE)
        }

        val statsMap = createStatsMap(listOf(80, 300), listOf(0, 300), listOf(0, 20), listOf(0, 250))

        mockRepositoryReturnValue(typesList, statsMap)

        Update(typesList, statsMap).also { updates.postValue(it) }
        pokemon.postValue(listOf(pokemonOne))

        argumentCaptor<List<ShopItemViewModel>>().apply {
            verify(itemsObserver).onChanged(capture())
        }.run { assertEquals(createViewModels(pokemon.value!!), firstValue) }
    }

    @Test
    fun `givenObservedFilterRepositoryChanges whenQueryingPokemonRepository andThreePokemonReturned thenObservePokemonChangesAsViewModels`() {
        val typesList = mutableListOf<Type>().apply {
            add(Type.FIGHTING)
            add(Type.POISON)
        }

        val statsMap = createStatsMap(listOf(0, 200), listOf(100, 300), listOf(100, 300), listOf(40, 50))

        mockRepositoryReturnValue(typesList, statsMap)

        Update(typesList, statsMap).also { updates.postValue(it) }
        pokemon.postValue(listOf(pokemonOne, pokemonTwo, pokemonThree))

        argumentCaptor<List<ShopItemViewModel>>().apply {
            verify(itemsObserver).onChanged(capture())
        }.run { assertEquals(createViewModels(pokemon.value!!), firstValue) }
    }

    private fun createStatsMap(health: List<Int>, attack: List<Int>, defense: List<Int>, speed: List<Int>) = mapOf(
        Stat.HEALTH to Pair(health[0], health[1]),
        Stat.ATTACK to Pair(attack[0], attack[1]),
        Stat.DEFENSE to Pair(defense[0], defense[1]),
        Stat.SPEED to Pair(speed[0], speed[1])
    )

    private fun createViewModels(pokemon: List<Pokemon>) = pokemon.map { ShopItemViewModel.from(it, resources) }.toList()

    private fun mockRepositoryReturnValue(types: List<Type>, stats: Map<Stat, Pair<Int, Int>>) {
        Mockito.`when`(pokemonRepository.getFilteredPokemon(
            types,
            stats.getValue(Stat.HEALTH),
            stats.getValue(Stat.ATTACK),
            stats.getValue(Stat.DEFENSE),
            stats.getValue(Stat.SPEED))
        ).thenReturn(pokemon)
    }

}