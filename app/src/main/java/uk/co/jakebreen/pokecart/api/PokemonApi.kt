package uk.co.jakebreen.pokecart.api

import retrofit2.http.GET
import retrofit2.http.Path
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon

interface PokemonApi {

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Pokemon

}