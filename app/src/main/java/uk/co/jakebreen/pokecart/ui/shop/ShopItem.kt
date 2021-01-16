package uk.co.jakebreen.pokecart.ui.shop

import android.net.Uri
import androidx.lifecycle.ViewModel
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.type.Type

class ShopItem(
    var id: Int,
    var name: String,
    var image: Uri,
    var price: Int,
    var health: Int,
    var attack: Int,
    var defense: Int,
    var speed: Int,
    var typePrimary: Type,
    var typeSecondary: Type,
    var hasAnimated: Boolean
): ViewModel() {

    override fun equals(other: Any?) = (other as ShopItem).id == id
    override fun hashCode() = id

    fun setAnimated() {
        hasAnimated = true
    }

    companion object {
        fun from(pokemon: Pokemon, resources: ShopItemResources) =
            ShopItem(
                id = pokemon.id,
                name = pokemon.name.capitalize(),
                image = resources.getImageUriById(pokemon.id),
                price = pokemon.price,
                health = pokemon.health,
                attack = pokemon.attack,
                defense = pokemon.defense,
                speed = pokemon.speed,
                typePrimary = pokemon.typePrimary,
                typeSecondary = pokemon.typeSecondary,
                hasAnimated = false
            )
    }

}