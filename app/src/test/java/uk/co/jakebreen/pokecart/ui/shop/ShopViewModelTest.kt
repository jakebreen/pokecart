package uk.co.jakebreen.pokecart.ui.shop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.jakebreen.pokecart.model.filter.FilterRepository
import uk.co.jakebreen.pokecart.model.pokemon.PokemonRepository

class ShopViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var pokemonRepository: PokemonRepository
    @Mock lateinit var filterRepository: FilterRepository

    private lateinit var viewModel: ShopViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = ShopViewModel(pokemonRepository, filterRepository)
    }

}