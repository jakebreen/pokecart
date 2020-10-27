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
    @Mock lateinit var cartItemViewModelsObserver: Observer<Set<CartItem>>
    @Mock lateinit var cartEmptyObserver: Observer<Boolean>
    @Mock lateinit var cartSubtotalObserver: Observer<Int>
    @Mock lateinit var cartPoketaxObserver: Observer<Double>
    @Mock lateinit var cartTotalObserver: Observer<Double>

    @Mock lateinit var cartItemOne: CartItem
    @Mock lateinit var cartItemTwo: CartItem

    private val cart = MediatorLiveData<Set<CartItem>>()

    private val pokemonOne = Pokemon(1, "pokemonOne", 0, 100, 100, 100, 100, Type.GROUND, Type.FIRE)
    private val pokemonTwo = Pokemon(2, "pokemonTwo", 0, 100, 100, 100, 100, Type.NORMAL, Type.POISON)
    private val pokemonThree = Pokemon(3, "pokemonThree", 0, 100, 100, 100, 100, Type.GROUND, Type.BUG)

    private lateinit var viewModel: CartDialogViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(cartRepository.observeUpdates()).thenReturn(cart)

        viewModel = CartDialogViewModel(cartRepository, pokemonRepository)

        viewModel.observeUpdates().observeForever(cartItemViewModelsObserver)
        viewModel.observeCartEmpty().observeForever(cartEmptyObserver)
        viewModel.observeSubtotal().observeForever(cartSubtotalObserver)
        viewModel.observePoketax().observeForever(cartPoketaxObserver)
        viewModel.observeTotal().observeForever(cartTotalObserver)
    }

    @Test
    fun `givenPokemonAddedToCart thenObserveSingleViewModelInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(setOf(cartItemOne))  }

        viewModel.addCartItem(pokemonOne.id)

        argumentCaptor<Set<CartItem>>().apply {
            verify(cartItemViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(setOf(cartItemOne), firstValue) }
    }

    @Test
    fun `givenTwoPokemonAddedToCart thenObserveTwoViewModelsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.addCartItem(pokemonOne)).thenAnswer { cart.postValue(setOf(cartItemOne))  }
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonTwo.id)).thenReturn(pokemonTwo)
        Mockito.`when`(cartRepository.addCartItem(pokemonTwo)).thenAnswer { cart.postValue(setOf(cartItemOne, cartItemTwo))  }

        viewModel.addCartItem(pokemonOne.id)

        argumentCaptor<Set<CartItem>>().apply {
            verify(cartItemViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(setOf(cartItemOne), firstValue) }

        viewModel.addCartItem(pokemonTwo.id)

        argumentCaptor<Set<CartItem>>().apply {
            verify(cartItemViewModelsObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(setOf(cartItemOne, cartItemTwo), secondValue) }
    }

    @Test
    fun `givenPokemonRemoveFromCart thenObserveNoViewModelsInCart`() {
        Mockito.`when`(pokemonRepository.getPokemonById(pokemonOne.id)).thenReturn(pokemonOne)
        Mockito.`when`(cartRepository.removeCartItem(pokemonOne)).thenAnswer { cart.postValue(setOf())  }

        viewModel.removeCartItem(pokemonOne.id)

        argumentCaptor<Set<CartItem>>().apply {
            verify(cartItemViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(emptySet<CartItem>(), firstValue) }
    }

    @Test
    fun `givenPokemonInCart whenClearingCart thenObserveNoViewModelsInCart`() {
        Mockito.`when`(cartRepository.clear()).thenAnswer { cart.postValue(emptySet())  }

        cart.postValue(setOf(cartItemOne, cartItemTwo))

        argumentCaptor<Set<CartItem>>().apply {
            verify(cartItemViewModelsObserver).onChanged(capture())
        }.run { Assert.assertEquals(setOf(cartItemOne, cartItemTwo), firstValue) }

        viewModel.clear()

        argumentCaptor<Set<CartItem>>().apply {
            verify(cartItemViewModelsObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(setOf<CartItem>(), secondValue) }
    }

    @Test
    fun `givenNoPokemonInCart thenObserveCartEmpty`() {
        cart.postValue(emptySet())

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(true, firstValue) }
    }

    @Test
    fun `givenPokemonInCart thenObserveCartNotEmpty`() {
        cart.postValue(setOf(cartItemOne))

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(false, firstValue) }
    }

    @Test
    fun `givenNoPokemonInCart whenPokemonAdded thenObserveCartEmptyToNotEmpty`() {
        cart.postValue(emptySet())

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(true, firstValue) }

        cart.postValue(setOf(cartItemOne))

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(false, secondValue) }
    }

    @Test
    fun `givenPokemonInCart whenPokemonCleared thenObserveCartNotEmptyToEmpty`() {
        cart.postValue(setOf(cartItemOne))

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver).onChanged(capture())
        }.run { Assert.assertEquals(false, firstValue) }

        cart.postValue(emptySet())

        argumentCaptor<Boolean>().apply {
            verify(cartEmptyObserver, times(2)).onChanged(capture())
        }.run { Assert.assertEquals(true, secondValue) }
    }

    @Test
    fun `givenSinglePokemonInCart whenPokemonCostsTwo thenObserveSubtotalOfTwo`() {
        Mockito.`when`(cartItemOne.count).thenReturn(1)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        cart.postValue(setOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(2, firstValue) }
    }

    @Test
    fun `givenThreePokemonInCart whenPokemonCostsTwo thenObserveSubtotalOfSix`() {
        Mockito.`when`(cartItemOne.count).thenReturn(3)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        cart.postValue(setOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(6, firstValue) }
    }

    @Test
    fun `givenThreePokemonInCart whenPokemonCostsTwo andOnePokemonRemoved thenObserveSubtotalOfFour`() {
        Mockito.`when`(cartItemOne.count).thenReturn(3)
        Mockito.`when`(cartItemOne.price).thenReturn(2)
        cart.postValue(setOf(cartItemOne))

        argumentCaptor<Int>().apply {
            verify(cartSubtotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(6, firstValue) }

        Mockito.`when`(cartItemOne.count).thenReturn(2)
        cart.postValue(setOf(cartItemOne))

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

        cart.postValue(setOf(cartItemOne, cartItemTwo))

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

        cart.postValue(setOf(cartItemOne))

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

        cart.postValue(setOf(cartItemOne, cartItemTwo))

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

        cart.postValue(setOf(cartItemOne))

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

        cart.postValue(setOf(cartItemOne))

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

        cart.postValue(setOf(cartItemOne, cartItemTwo))

        argumentCaptor<Double>().apply {
            verify(cartTotalObserver).onChanged(capture())
        }.run { Assert.assertEquals(expected, firstValue, 0.0) }
    }

}