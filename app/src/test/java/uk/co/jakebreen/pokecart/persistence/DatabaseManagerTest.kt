package uk.co.jakebreen.pokecart.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.clearInvocations
import org.mockito.MockitoAnnotations.initMocks
import uk.co.jakebreen.pokecart.api.PokemonApi
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.pokemon.PokemonDao
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.persistence.DatabaseManager.Companion.TOTAL_POKEMON
import kotlin.random.Random

class DatabaseManagerTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var database: PokemonRoomDatabase
    @Mock lateinit var pokemonDao: PokemonDao
    @Mock lateinit var pokemonApi: PokemonApi

    private val pokemonOne = Pokemon(1, "pokemonOne", Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Type.GROUND, Type.FIRE)
    private val pokemonTwo = Pokemon(2, "pokemonTwo", Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Type.NORMAL, Type.POISON)
    private val pokemonThree = Pokemon(3, "pokemonThree", Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt(), Type.GROUND, Type.BUG)

    lateinit var databaseManager: DatabaseManager

    @Before
    fun setup() {
        initMocks(this)

        Dispatchers.setMain(Dispatchers.Unconfined)
        databaseManager = DatabaseManager(database, pokemonDao, pokemonApi)
    }

    @After
    fun cleanup() {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `whenDatabaseIncomplete thenPopulateDatabase`() = runBlocking {
        Mockito.`when`(pokemonDao.getPokemonCount()).thenReturn(TOTAL_POKEMON.dec())

        Mockito.`when`(pokemonApi.getPokemonById(1)).thenReturn(pokemonOne)
        Mockito.`when`(pokemonApi.getPokemonById(2)).thenReturn(pokemonTwo)
        Mockito.`when`(pokemonApi.getPokemonById(3)).thenReturn(pokemonThree)

        databaseManager.onStart()

        verify(database, times(1)).clearAllTables()
        verify(pokemonDao, times(1)).insert(pokemonOne)
        verify(pokemonDao, times(1)).insert(pokemonTwo)
        verify(pokemonDao, times(1)).insert(pokemonThree)
    }

    @Test
    fun `whenDatabaseComplete thenDoNotRepopulateDatabase`() = runBlocking {
        Mockito.`when`(pokemonDao.getPokemonCount()).thenReturn(TOTAL_POKEMON)

        databaseManager.onStart()

        clearInvocations(pokemonDao)
        verifyZeroInteractions(database)
        verifyZeroInteractions(pokemonDao)
    }

}