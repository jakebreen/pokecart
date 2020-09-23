package uk.co.jakebreen.pokecart.ui.cart

import android.net.Uri

class CartItemResources {

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