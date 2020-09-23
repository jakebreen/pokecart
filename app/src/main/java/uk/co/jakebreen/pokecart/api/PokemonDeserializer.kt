package uk.co.jakebreen.pokecart.api

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type
import kotlin.random.Random


class PokemonDeserializer: JsonDeserializer<Pokemon> {

    companion object {
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_STATS = "stats"
        const val KEY_STAT = "stat"
        const val KEY_BASE_STAT = "base_stat"
        const val KEY_TYPES = "types"
        const val KEY_TYPE = "type"
    }

    @SuppressLint("DefaultLocale")
    override fun deserialize(json: JsonElement?, typeOfT: java.lang.reflect.Type?, context: JsonDeserializationContext?): Pokemon? {
        val root = json?.asJsonObject

        return root?.let {
            val name = root.get(KEY_NAME)?.asString ?: return null
            val id = root.get(KEY_ID)?.asInt ?: return null

            var health = 0
            var attack = 0
            var defense = 0
            var speed = 0

            val statsArray = root.get(KEY_STATS)?.asJsonArray
            statsArray?.also {
                for (i in 0 until statsArray.size()) {
                    val stat = Stat.getStatByName(statsArray.get(i).asJsonObject.get(KEY_STAT).asJsonObject.get(KEY_NAME).asString)
                    val statValue = statsArray.get(i).asJsonObject.get(KEY_BASE_STAT).asInt
                    when(stat) {
                        Stat.HEALTH -> health = statValue
                        Stat.ATTACK -> attack = statValue
                        Stat.DEFENSE -> defense = statValue
                        Stat.SPEED -> speed = statValue
                    }
                }
            }

            val typesArray = root.get(KEY_TYPES)?.asJsonArray
            val typePrimary = typesArray?.let {
                it[0]?.asJsonObject?.get(KEY_TYPE)?.asJsonObject?.get(KEY_NAME)?.asString?.let { Type.getTypeByName(it) }
            } ?: run {
                Type.NONE
            }

            val typeSecondary = if (typesArray?.size() == 2) {
                typesArray.let {
                    it[1]?.asJsonObject?.get(KEY_TYPE)?.asJsonObject?.get(KEY_NAME)?.asString?.let { Type.getTypeByName(it) }
                } ?: run {
                    Type.NONE
                }
            } else {
                Type.NONE
            }

            val price = Random.nextInt(2, 8)
            Pokemon(id, name, price, health, attack, defense, speed, typePrimary, typeSecondary)
        }
    }

}