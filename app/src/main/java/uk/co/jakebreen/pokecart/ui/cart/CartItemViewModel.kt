package uk.co.jakebreen.pokecart.ui.cart

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon

class CartItemViewModel(
    var id: Int,
    var count: Int,
    var name: String,
    var image: Uri,
    var price: Int
): ViewModel() {

    override fun equals(other: Any?) = (other as CartItemViewModel).id == id
    override fun hashCode() = id

    companion object {
        fun from(pokemon: Pokemon, count: Int, resources: CartItemResources) =
            CartItemViewModel(
                id = pokemon.id,
                count = count,
                name = pokemon.name,
                image = resources.getImageUriById(pokemon.id),
                price = pokemon.price
            )

        @JvmStatic
        @BindingAdapter("image")
        fun bindImage(view: ImageView, uri: Uri?) =
            Glide.with(view)
                .load(uri)
                .into(view)

        @JvmStatic
        @BindingAdapter("cartPrice", "cartCount")
        fun bindTotalCost(textView: TextView, price: Int, count: Int) {
            textView.text = "$".plus(price.times(count))
        }

        @JvmStatic
        @BindingAdapter("cartCount")
        fun bindCount(textView: TextView, count: Int) {
            textView.text = count.toString()
        }
    }

}