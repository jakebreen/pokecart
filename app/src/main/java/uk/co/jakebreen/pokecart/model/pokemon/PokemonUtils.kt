package uk.co.jakebreen.pokecart.model.pokemon

import android.net.Uri

object PokemonUtils {

    fun getImageUriById(id: Int): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("pokeres.bastionbot.org")
            .appendPath("images")
            .appendPath("pokemon")
            .appendPath(id.toString().plus(".png"))
            .build()
    }

}