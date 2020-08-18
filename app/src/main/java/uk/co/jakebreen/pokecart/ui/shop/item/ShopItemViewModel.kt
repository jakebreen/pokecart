package uk.co.jakebreen.pokecart.ui.shop.item

import android.animation.ObjectAnimator
import android.net.Uri
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.type.Type
import kotlin.random.Random

class ShopItemViewModel(
    var id: Int,
    var name: String,
    var image: Uri,
    var price: String,
    var health: Int,
    var attack: Int,
    var defense: Int,
    var speed: Int,
    var typePrimary: Type,
    var typeSecondary: Type
): ViewModel() {

    override fun equals(other: Any?) = (other as ShopItemViewModel).id == id

    companion object {
        fun from(pokemon: Pokemon, resources: ShopItemResources) = ShopItemViewModel(
            id = pokemon.id,
            name = pokemon.name.capitalize(),
            image = resources.getImageUriById(pokemon.id),
            price = "$".plus(price()),
            health = pokemon.health,
            attack = pokemon.attack,
            defense = pokemon.defense,
            speed = pokemon.speed,
            typePrimary = pokemon.typePrimary,
            typeSecondary = pokemon.typeSecondary
        )

        @JvmStatic
        @BindingAdapter("image")
        fun bindImage(view: ImageView, uri: Uri?) =
            Glide.with(view)
                .load(uri)
                .into(view)

        @JvmStatic
        @BindingAdapter("health", "healthTotal")
        fun bindHealth(view: ProgressBar, health: Int, text: TextView) {
            animateProgress(view, health)
            text.text = getStatTotal(health)
        }

        @JvmStatic
        @BindingAdapter("attack", "attackTotal")
        fun bindAttack(view: ProgressBar, attack :Int, text: TextView) {
            animateProgress(view, attack)
            text.text = getStatTotal(attack)
        }

        @JvmStatic
        @BindingAdapter("defense", "defenseTotal")
        fun bindDefense(view: ProgressBar, defense: Int, text: TextView) {
            animateProgress(view, defense)
            text.text = getStatTotal(defense)
        }

        @JvmStatic
        @BindingAdapter("speed", "speedTotal")
        fun bindSpeed(view: ProgressBar, speed: Int, text: TextView) {
            animateProgress(view, speed)
            text.text = getStatTotal(speed)
        }

        @JvmStatic
        @BindingAdapter("typePrimary")
        fun bindTypePrimary(view: ImageView, type: Type) =
            Glide.with(view)
                .load(Type.getResourceDrawableByType(type))
                .into(view)

        @JvmStatic
        @BindingAdapter("typeSecondary")
        fun bindTypeSecondary(view: ImageView, type: Type) =
            Glide.with(view)
                .load(Type.getResourceDrawableByType(type))
                .fitCenter()
                .into(view)

        private fun price(): Int = Random.nextInt(2, 8)

        private fun animateProgress(view: ProgressBar, value: Int) =
            ObjectAnimator.ofInt(view, "progress", value)
                .setDuration(800)
                .start()

        private fun getStatTotal(total: Int) = total.toString().plus("/300")

    }

}