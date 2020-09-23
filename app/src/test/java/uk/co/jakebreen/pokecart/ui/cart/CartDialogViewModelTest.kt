package uk.co.jakebreen.pokecart.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.jakebreen.pokecart.model.cart.CartRepository
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.ui.cart.CartDialogViewModel.Companion.calculatePoketax
import uk.co.jakebreen.pokecart.ui.cart.CartDialogViewModel.Companion.calculateSubtotal
import uk.co.jakebreen.pokecart.ui.cart.CartDialogViewModel.Companion.calculateTotal

class CartDialogViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var cartRepository: CartRepository
    @Mock lateinit var pokemonRepository: PokemonRepository
    @Mock lateinit var cartViewModelsObserver: Observer<List<CartItemViewModel>>
    @Mock lateinit var cartEmptyObserver: Observer<Boolean>
    @Mock lateinit var cartSubtotalObserver: Observer<Int>
    @Mock lateinit var cartPoketaxObserver: Observer<Double>
    @Mock lateinit var cartTotalObserver: Observer<Double>

    @Mock lateinit var cartItemOne: CartItemViewModel
    @Mock lateinit var cartItemTwo: CartItemViewModel

    private val cart = MediatorLiveData<List<CartItemViewModel>>()

    private val pokemonOne = Pokemon(1, "pokemonOne", 0, 100, 100, 100, 100, Type.GROUND, Type.FIRE)
    private val pokemonTwo = Pokemon(2, "pokemonTwo", 0, 100, 100, 100, 100, Type.NORMAL, Type.POISON)
    private val pokemonThree = Pokemon(3, "pokemonThree", 0, 100, 100, 100, 100, Type.GROUND, Type.BUG)

    private lateinit var viewModel: CartDialogViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(cartRepository.observeUpdates()).thenReturn(cart)

        viewModel = CartDialogViewModel(cartRepository, pokemonRepository)

        viewModel.observeUpdates().observeForever(cartViewModelsObserver)
        viewModel.observeCartEmpty().observeForever(cartEmptyObserver)
        viewModel.observeSubtotal().observeForever(cartSubtotalObserver)
        viewModel.observePoketax().observeForever(cartPoketaxObserver)
        viewModel.observeTotal().observeForever(cartTotalObserver)
    }

    @Test
    fun `givenPokemonAddedToCart thenObserveSingleViewModelInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne))  }

        viewModel.addCartItem(pokemonOne.id)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(listOf(cartItemOne), firstValue) }
    }

    @Test
    fun `givenTwoPokemonAddedToCart thenObserveTwoViewModelsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf(cartItemOne))  }
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonTwo.id)).thenReturn(pokemonTwo)
        Mockito.`when`(cartRepository.addCartItem(pokemonTwo)).thenAnswer { cart.postValue(listOf(cartItemOne, cartItemTwo))  }

        viewModel.addCartItem(pokemonOne.id)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(listOf(cartItemOne), firstValue) }

        viewModel.addCartItem(pokemonTwo.id)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartViewModelsObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(listOf(cartItemOne, cartItemTwo), secondValue) }
    }

    @Test
    fun `givenPokemonRemoveFromCart thenObserveNoViewModelsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.removeCartItem(pokemonOne)).thenAnswer { cart.postValue(listOf())  }

        viewModel.removeCartItem(pokemonOne.id)

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(emptyList<CartItemViewModel>(), firstValue) }
    }

    @Test
    fun `givenPokemonInCart whenClearingCart thenObserveNoViewModelsInCart`() {
        Mockito.`when`(cartRepository.clear()).thenAnswer { cart.postValue(emptyList())  }

        cart.postValue(listOf(cartItemOne, cartItemTwo))

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(listOf(cartItemOne, cartItemTwo), firstValue) }

        viewModel.clear()

        argumentCaptor<List<CartItemViewModel>>().apply {
            verify(cartViewModelsObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(emptyList<CartItemViewModel>(), secondValue) }
    }

    @Test
    fun `givenNoPokemonInCart thenObserveCartEmpty`() {
        cart.postValue(emptyList())

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(true, firstValue) }
    }

    @Test
    fun `givenPokemonInCart thenObserveCartNotEmpty`() {
        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(false, firstValue) }
    }

    @Test
    fun `givenNoPokemonInCart whenPokemonAdded thenObserveCartEmptyToNotEmpty`() {
        cart.postValue(emptyList())

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(true, firstValue) }

        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(false, secondValue) }
    }

    @Test
    fun `givenPokemonInCart whenPokemonCleared thenObserveCartNotEmptyToEmpty`() {
        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(false, firstValue) }

        cart.postValue(emptyList())

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(true, secondValue) }
    }

    @Test
    fun `givenSinglePokemonInCart whenPokemonCostsTwo thenObserveSubtotalOfTwo`() {
        Mockito.`when`(cartItemOne.count).thenReturn(1)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(2, firstValue) }
    }

    @Test
    fun `givenThreePokemonInCart whenPokemonCostsTwo thenObserveSubtotalOfSix`() {
        Mockito.`when`(cartItemOne.count).thenReturn(3)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(6, firstValue) }
    }

    @Test
    fun `givenThreePokemonInCart whenPokemonCostsTwo andOnePokemonRemoved thenObserveSubtotalOfFour`() {
        Mockito.`when`(cartItemOne.count).thenReturn(3)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(6, firstValue) }

        Mockito.`when`(cartItemOne.count).thenReturn(2)
        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(4, secondValue) }
    }

    @Test
    fun `givenMultiplePokemonInCart whenPokemonCostsVarying thenObserveSubtotalOfVarying`() {
        Mockito.`when`(cartItemOne.count).thenReturn(2)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        Mockito.`when`(cartItemTwo.count).thenReturn(1)
        Mockito.`when`(cartItemTwo.price).thenReturn(8)

        cart.postValue(listOf(cartItemOne, cartItemTwo))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(12, firstValue) }
    }

    @Test
    fun `givenSinglePokemonInCart whenPokemonCostsTwo thenObservePoketax`() {
        Mockito.`when`(cartItemOne.count).thenReturn(1)
        Mockito.`when`(cartItemOne.price).thenReturn(2)

        val subtotal = calculateSubtotal(cartItemOne.price, cartItemOne.count)
        val expected = calculatePoketax(subtotal.toDouble())

        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Double>().apply {
            verify(cartPoketaxObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue, 0.0) }
    }

    @Test
    fun `givenMultiplePokemonInCart whenPokemonCostsVarying thenObservePoketax`() {
        Mockito.`when`(cartItemOne.count).thenReturn(2)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        Mockito.`when`(cartItemTwo.count).thenReturn(1)
        Mockito.`when`(cartItemTwo.price).thenReturn(5)

        val subtotal = calculateSubtotal(cartItemOne.price, cartItemOne.count) + calculateSubtotal(cartItemTwo.price, cartItemTwo.count)
        val expected = calculatePoketax(subtotal.toDouble())

        cart.postValue(listOf(cartItemOne, cartItemTwo))

        argumentCaptor<Double>().apply {
            verify(cartPoketaxObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue, 0.0) }
    }

    @Test
    fun `givenSubtotalAndPoketaxOfSinglePokemon whenCountIsOne thenObserveTotal`() {
        Mockito.`when`(cartItemOne.count).thenReturn(1)
        Mockito.`when`(cartItemOne.price).thenReturn(2)

        val subtotal = calculateSubtotal(cartItemOne.price, cartItemOne.count)
        val poketax = calculatePoketax(subtotal.toDouble())
        val expected = subtotal.plus(poketax)

        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Double>().apply {
            verify(cartTotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue, 0.0) }
    }

    @Test
    fun `givenSubtotalAndPoketaxOfSinglePokemon whenCountIsFive thenObserveTotal`() {
        Mockito.`when`(cartItemOne.count).thenReturn(5)
        Mockito.`when`(cartItemOne.price).thenReturn(2)

        val subtotal = calculateSubtotal(cartItemOne.price, cartItemOne.count)
        val poketax = calculatePoketax(subtotal.toDouble())
        val expected = calculateTotal(subtotal, poketax)

        cart.postValue(listOf(cartItemOne))

        argumentCaptor<Double>().apply {
            verify(cartTotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue, 0.0) }
    }

    @Test
    fun `givenSubtotalAndPoketaxOfmutiplePokemon whenCountIsVarying thenObserveTotal`() {
        Mockito.`when`(cartItemOne.count).thenReturn(3)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        Mockito.`when`(cartItemTwo.count).thenReturn(6)
        Mockito.`when`(cartItemTwo.price).thenReturn(3)

        val subtotal = calculateSubtotal(cartItemOne.price, cartItemOne.count) + calculateSubtotal(cartItemTwo.price, cartItemTwo.count)
        val poketax = calculatePoketax(subtotal.toDouble())
        val expected = calculateTotal(subtotal, poketax)

        cart.postValue(listOf(cartItemOne, cartItemTwo))

        argumentCaptor<Double>().apply {
            verify(cartTotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue, 0.0) }
    }

}