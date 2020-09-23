package uk.co.jakebreen.pokecart.model.cart

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations.initMocks
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.ui.cart.CartItemResources
import uk.co.jakebreen.pokecart.ui.cart.CartItemViewModel

class CartRepositoryTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var cartItemViewModelObserver: Observer<List<CartItemViewModel>>
    @Mock lateinit var resources: CartItemResources

    private val pokemonOne = Pokemon(1, "pokemonOne", 0, 100, 100, 100, 100, Type.GROUND, Type.FIRE)
    private val pokemonTwo = Pokemon(2, "pokemonTwo", 0, 100, 100, 100, 100, Type.NORMAL, Type.POISON)
    private val pokemonThree = Pokemon(3, "pokemonThree", 0, 100, 100, 100, 100, Type.GROUND, Type.BUG)

    private val cartPokemon = MutableLiveData<MutableMap<Pokemon, Int>>()

    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        initMocks(this)

        Mockito.`when`(resources.getImageUriById(1)).thenReturn(Mockito.mock(Uri::class.java))
        Mockito.`when`(resources.getImageUriById(2)).thenReturn(Mockito.mock(Uri::class.java))
        Mockito.`when`(resources.getImageUriById(3)).thenReturn(Mockito.mock(Uri::class.java))

        cartPokemon.postValue(mutableMapOf())

        cartRepository = CartRepository(cartPokemon, resources)
        cartRepository.observeUpdates().observeForever(cartItemViewModelObserver)
        clearInvocations(cartItemViewModelObserver)
    }

    @Test
    fun `whenPokemonAdded thenAddNewViewModel`() {
        val expected = listOf(CartItemViewModel.from(pokemonOne, 1, resources))
        cartRepository.addCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver).onChanged(capture())
        }.run { assertEquals(expected, firstValue) }
    }

    @Test
    fun `whenPokemonAdded andPokemonAlreadyExists thenIncreaseViewModelCount`() {
        val expectedFirst = listOf(CartItemViewModel.from(pokemonOne, 1, resources))
        cartRepository.addCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver).onChanged(capture())
        }.run { assertEquals(expectedFirst, firstValue) }

        val expectedSecond = listOf(CartItemViewModel.from(pokemonOne, 2, resources))
        cartRepository.addCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(2)).onChanged(capture())
        }.run { assertEquals(expectedSecond, secondValue) }
    }

    @Test
    fun `whenNewPokemonAdded andPokemonAlreadyExists thenAddNewViewModel`() {
        val expectedFirst = listOf(CartItemViewModel.from(pokemonOne, 2, resources))
        cartRepository.addCartItem(pokemonOne)
        cartRepository.addCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(2)).onChanged(capture())
        }.run { assertEquals(expectedFirst, secondValue) }

        val expectedSecond = listOf(
            CartItemViewModel.from(pokemonOne, 2, resources),
            CartItemViewModel.from(pokemonTwo, 1, resources))
        cartRepository.addCartItem(pokemonTwo)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(3)).onChanged(capture())
        }.run { assertEquals(expectedSecond, thirdValue) }
    }

    @Test
    fun `whenPokemonRemoved thenRemoveViewModel`() {
        val expectedFirst = listOf(CartItemViewModel.from(pokemonOne, 1, resources))
        cartRepository.addCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver).onChanged(capture())
        }.run { assertEquals(expectedFirst, firstValue) }

        val expectedSecond = emptyList<CartItemViewModel>()
        cartRepository.removeCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(2)).onChanged(capture())
        }.run { assertEquals(expectedSecond, secondValue) }
    }

    @Test
    fun `whenPokemonRemoved andPokemonAlreadyExists thenDecreaseViewModelCount`() {
        val expectedFirst = listOf(CartItemViewModel.from(pokemonOne, 2, resources))
        cartRepository.addCartItem(pokemonOne)
        cartRepository.addCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(2)).onChanged(capture())
        }.run { assertEquals(expectedFirst, secondValue) }

        val expectedSecond = listOf(CartItemViewModel.from(pokemonOne, 1, resources))
        cartRepository.removeCartItem(pokemonOne)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(3)).onChanged(capture())
        }.run { assertEquals(expectedSecond, thirdValue) }
    }

    @Test
    fun `whenClearingPokemon thenNoViewModelsRemain`() {
        val expectedFirst = listOf(
            CartItemViewModel.from(pokemonOne, 2, resources),
            CartItemViewModel.from(pokemonTwo, 1, resources))
        cartRepository.addCartItem(pokemonOne)
        cartRepository.addCartItem(pokemonTwo)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(2)).onChanged(capture())
        }.run { assertEquals(expectedFirst, secondValue) }

        val expectedSecond = emptyList<CartItemViewModel>()

        cartRepository.clear()

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartItemViewModelObserver, times(3)).onChanged(capture())
        }.run { assertEquals(expectedSecond, thirdValue) }
    }

}