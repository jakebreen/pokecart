package uk.co.jakebreen.pokecart.persistence

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.api.PokemonApi
import uk.co.jakebreen.pokecart.model.pokemon.PokemonDao

val databaseModule = module {
    single { getDatabase(androidContext()) }
    factory { getPokemonDao(androidContext()) }
    single { getDatabaseManager(get(), get(), get()) }
}

fun getDatabase(context: Context) = PokemonRoomDatabase.getDatabase(context)

fun getPokemonDao(context: Context) = PokemonRoomDatabase.getDatabase(context).pokemonDao()

fun getDatabaseManager(database: PokemonRoomDatabase, pokemonDao: PokemonDao, pokemonApi: PokemonApi) =
    DatabaseManager(database, pokemonDao, pokemonApi)