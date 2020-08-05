package uk.co.jakebreen.pokecart.api

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import uk.co.jakebreen.pokecart.model.pokemon.Pokemon
import uk.co.jakebreen.pokecart.model.stat.Stat
import java.lang.reflect.Type


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
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Pokemon {
        val root = json?.asJsonObject!!

        val name = root.get(KEY_NAME)?.asString!!
        val id = root.get(KEY_ID)?.asInt!!

        var health = 0
        var attack = 0
        var defense = 0
        var speed = 0

        val statsArray = root.get(KEY_STATS)?.asJsonArray!!
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

        val typePrimary: uk.co.jakebreen.pokecart.model.type.Type
        val typeSecondary: uk.co.jakebreen.pokecart.model.type.Type

        val typesArray = root.get(KEY_TYPES)?.asJsonArray!!
        typePrimary = uk.co.jakebreen.pokecart.model.type.Type.getTypeByName(typesArray[0].asJsonObject.get(KEY_TYPE).asJsonObject.get(KEY_NAME).asString)!!
        typeSecondary = if (typesArray.size() == 2)
            uk.co.jakebreen.pokecart.model.type.Type.getTypeByName(typesArray[1].asJsonObject.get(KEY_TYPE).asJsonObject.get(KEY_NAME).asString)!!
        else  uk.co.jakebreen.pokecart.model.type.Type.NONE


        return Pokemon(id, name, health, attack, defense, speed, typePrimary, typeSecondary)
    }

}