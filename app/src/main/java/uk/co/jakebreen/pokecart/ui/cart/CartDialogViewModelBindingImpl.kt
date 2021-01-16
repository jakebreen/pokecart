package uk.co.jakebreen.pokecart.ui.cart

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("subtotal")
fun bindSubtotal(textView: TextView, subtotal: Int) {
    textView.text = "$".plus(subtotal)
}

@BindingAdapter("poketax")
fun bindPoketax(textView: TextView, poketax: Double) {
    textView.text = String.format("$%.2f", poketax)
}

@BindingAdapter("total")
fun bindTotal(textView: TextView, total: Double) {
    textView.text = String.format("$%.2f", total)
}