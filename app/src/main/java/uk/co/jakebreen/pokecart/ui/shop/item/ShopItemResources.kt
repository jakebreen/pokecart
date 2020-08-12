package uk.co.jakebreen.pokecart.ui.shop.item

import android.net.Uri

class ShopItemResources {

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