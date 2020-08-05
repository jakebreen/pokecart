package uk.co.jakebreen.pokecart.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon

val apiModule = module {
    factory { loggingInterceptor() }
    factory { okHttpClient(get()) }
    factory { pokemonApi(get()) }
    factory { pokemonConverterFactory() }
    single { retrofit(get(), get()) }
}

fun pokemonApi(retrofit: Retrofit): PokemonApi = retrofit.create(PokemonApi::class.java)

fun loggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level =  HttpLoggingInterceptor.Level.NONE
    }
}

fun okHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
}

fun pokemonConverterFactory(): GsonConverterFactory  {
    return GsonConverterFactory.create(GsonBuilder().registerTypeAdapter(Pokemon::class.java, PokemonDeserializer()).create())
}

fun retrofit(client: OkHttpClient, pokemonConverterFactory: GsonConverterFactory): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(client)
        .addConverterFactory(pokemonConverterFactory)
//        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
