package uk.co.jakebreen.pokecart.ui.shop

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.jakebreen.pokecart.model.cart.CartRepository
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.filter.FilterRepository.Update
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.ui.cart.CartItemViewModel

class ShopViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var pokemonRepository: PokemonRepository
    @Mock lateinit var filterRepository: FilterRepository
    @Mock lateinit var resources: ShopItemResources
    @Mock lateinit var itemsObserver: Observer<List<ShopItemViewModel>>
    @Mock lateinit var cartRepository: CartRepository
    @Mock lateinit var cartItemCountObserver: Observer<Int>

    @Mock lateinit var cartItemOne: CartItemViewModel
    @Mock lateinit var cartItemTwo: CartItemViewModel

    private val pokemon = MutableLiveData<List<Pokemon>>()
    private val updates = MediatorLiveData<Update>()
    private val cart = MediatorLiveData<List<CartItemViewModel>>()

    private val pokemonOne = Pokemon(1, "pokemonOne", 0, 100, 100, 100, 100, Type.GROUND, Type.FIRE)
    private val pokemonTwo = Pokemon(2, "pokemonTwo", 0, 100, 100, 100, 100, Type.NORMAL, Type.POISON)
    private val pokemonThree = Pokemon(3, "pokemonThree", 0, 100, 100, 100, 100, Type.GROUND, Type.BUG)

    private lateinit var viewModel: ShopViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(filterRepository.observeUpdates()).thenReturn(updates)
        Mockito.`when`(cartRepository.observeUpdates()).thenReturn(cart)
        Mockito.`when`(resources.getImageUriById(1)).thenReturn(Mockito.mock(Uri::class.java))
        Mockito.`when`(resources.getImageUriById(2)).thenReturn(Mockito.mock(Uri::class.java))
        Mockito.`when`(resources.getImageUriById(3)).thenReturn(Mockito.mock(Uri::class.java))

        viewModel = ShopViewModel(pokemonRepository, filterRepository, resources, cartRepository)
        viewModel.observeViewModels().observeForever(itemsObserver)
        viewModel.observeCartItemCount().observeForever(cartItemCountObserver)
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
        }.run { assertEquals(pokemon.value?.let { createViewModels(it) }, firstValue) }
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
        }.run { assertEquals(pokemon.value?.let { createViewModels(it) }, firstValue) }
    }

    @Test
    fun `givenPokemonAddedToCart thenObserveSingleItemInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne))  }
        Mockito.`when`(cartItemOne.count).thenReturn(1)

        viewModel.addPokemonToCart(pokemonOne.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(1, firstValue) }
    }

    @Test
    fun `givenOneThenTwoMorePokemonAddedToCart thenObserveSingleItemInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne))  }
        Mockito.`when`(cartItemOne.count).thenReturn(1)

        viewModel.addPokemonToCart(pokemonOne.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(1, firstValue) }

        Mockito.`when`(pokemonRepository.getPokemonById(pokemonTwo.id)).thenReturn(pokemonTwo)
        Mockito.`when`(cartRepository.addCartItem(pokemonTwo)).thenAnswer { cart.postValue(listOf(cartItemOne, cartItemTwo))  }
        Mockito.`when`(cartItemTwo.count).thenReturn(1)

        viewModel.addPokemonToCart(pokemonTwo.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver, times(2)).onChanged(capture())
        }.run { assertEquals(2, secondValue) }
    }

    @Test
    fun `givenCartEmpty thenObserveNoItemsInCart`() {
        cart.postValue(listOf())

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(0, firstValue) }
    }

    @Test
    fun `givePokemonRemovedFromCart thenObserveNoItemsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne, cartItemTwo))  }
        Mockito.`when`(cartItemOne.count).thenReturn(1)
        Mockito.`when`(cartItemTwo.count).thenReturn(1)

        viewModel.addPokemonToCart(pokemonOne.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(2, firstValue) }

        cart.postValue(listOf())

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver, times(2)).onChanged(capture())
        }.run { assertEquals(0, secondValue) }
    }

    @Test
    fun `givenTwoOfTheSamePokemonAddedToCart thenObserveTwoItemsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne))  }
        Mockito.`when`(cartItemOne.count).thenReturn(2)

        viewModel.addPokemonToCart(pokemonOne.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(2, firstValue) }
    }

    @Test
    fun `givenFiveOfTheSamePokemonAddedToCart thenObserveFiveItemsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne))  }
        Mockito.`when`(cartItemOne.count).thenReturn(5)

        viewModel.addPokemonToCart(pokemonOne.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(5, firstValue) }
    }

    @Test
    fun `givenTwoOfOnePokemonAndThreeOfAnotherAddedToCart thenObserveFiveItemsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne, cartItemTwo))  }
        Mockito.`when`(cartItemOne.count).thenReturn(2)
        Mockito.`when`(cartItemTwo.count).thenReturn(3)

        viewModel.addPokemonToCart(pokemonOne.id)

        argumentCaptor<Int>().apply {
            verify(cartItemCountObserver).onChanged(capture())
        }.run { assertEquals(5, firstValue) }
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