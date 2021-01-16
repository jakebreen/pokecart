package uk.co.jakebreen.pokecart.ui.shop

import android.animation.ObjectAnimator
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import uk.co.jakebreen.pokecart.model.type.Type

@BindingAdapter("image")
fun bindImage(view: ImageView, uri: Uri?) =
    Glide.with(view)
        .load(uri)
        .placeholder(getPlaceHolder(view.context))
        .into(view)

@BindingAdapter("shopPrice")
fun bindPrice(textView: TextView, price: Int) {
    textView.text = "$".plus(price)
}

@BindingAdapter("health", "healthTotal", "hasAnimated")
fun bindHealth(view: ProgressBar, health: Int, text: TextView, hasAnimated: Boolean) {
    if (!hasAnimated) {
        view.progress = 0
        animateProgress(view, health)
    } else {
        view.progress = health
    }
    text.text = getStatTotal(health)
}

@BindingAdapter("attack", "attackTotal", "hasAnimated")
fun bindAttack(view: ProgressBar, attack :Int, text: TextView, hasAnimated: Boolean) {
    if (!hasAnimated) {
        view.progress = 0
        animateProgress(view, attack)
    } else {
        view.progress = attack
    }
    text.text = getStatTotal(attack)
}

@BindingAdapter("defense", "defenseTotal", "hasAnimated")
fun bindDefense(view: ProgressBar, defense: Int, text: TextView, hasAnimated: Boolean) {
    if (!hasAnimated) {
        view.progress = 0
        animateProgress(view, defense)
    } else {
        view.progress = defense
    }
    text.text = getStatTotal(defense)
}

@BindingAdapter("speed", "speedTotal", "hasAnimated")
fun bindSpeed(view: ProgressBar, speed: Int, text: TextView, hasAnimated: Boolean) {
    if (!hasAnimated) {
        view.progress = 0
        animateProgress(view, speed)
    } else {
        view.progress = speed
    }
    text.text = getStatTotal(speed)
}

@BindingAdapter("typePrimary")
fun bindTypePrimary(view: ImageView, type: Type) =
    Glide.with(view)
        .load(Type.getResourceDrawableByType(type))
        .into(view)

@BindingAdapter("typeSecondary")
fun bindTypeSecondary(view: ImageView, type: Type) =
    Glide.with(view)
        .load(Type.getResourceDrawableByType(type))
        .fitCenter()
        .into(view)

private fun animateProgress(view: ProgressBar, value: Int) =
    ObjectAnimator.ofInt(view, "progress", value)
        .setDuration(800)
        .start()

private fun getStatTotal(total: Int) = "$total/300"

private fun getPlaceHolder(context: Context): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    return circularProgressDrawable
}