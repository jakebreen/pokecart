package uk.co.jakebreen.pokecart.ui.cart

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun bindImage(view: ImageView, uri: Uri?) =
    Glide.with(view)
        .load(uri)
        .into(view)

@BindingAdapter("cartPrice", "cartCount")
fun bindTotalCost(textView: TextView, price: Int, count: Int) {
    textView.text = "$".plus(price.times(count))
}

@BindingAdapter("cartCount")
fun bindCount(textView: TextView, count: Int) {
    textView.text = count.toString()
}