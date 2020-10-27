package uk.co.jakebreen.pokecart.ui.cart

import android.net.Uri
import androidx.lifecycle.ViewModel

data class CartItem(
    val id: Int,
    val count: Int,
    val name: String,
    val image: Uri,
    val price: Int
): ViewModel() {

    override fun equals(other: Any?) = (other as CartItem).id == id
    override fun hashCode() = id

}