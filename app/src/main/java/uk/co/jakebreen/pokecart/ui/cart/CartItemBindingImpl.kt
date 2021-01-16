package uk.co.jakebreen.pokecart.ui.cart

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("cartPrice", "cartCount")
fun bindTotalCost(textView: TextView, price: Int, count: Int) {
    textView.text = "$".plus(price.times(count))
}

@BindingAdapter("cartCount")
fun bindCount(textView: TextView, count: Int) {
    textView.text = count.toString()
}